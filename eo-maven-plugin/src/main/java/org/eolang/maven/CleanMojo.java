/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2022 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang.maven;

import com.jcabi.log.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Implementation of maven clean plugin,
 * just deleting target/eo directory.
 *
 * @since 0.28.6
 */
@Mojo(
    name = "clean",
    defaultPhase = LifecyclePhase.CLEAN,
    threadSafe = true
)
public class CleanMojo extends SafeMojo {

    @Override
    final void exec() throws IOException {
        if (!this.targetDir.exists()) {
            this.log("Directory %s isn't exist.", targetDir);
            return;
        }
        new Walk(this.targetDir.toPath())
            .reversed()
            .stream()
            .map(Path::toFile)
            .forEach(
                dir -> {
                    this.purge(dir);
                    this.log("purged %s", dir);
                }
            );
        Logger.info(
            this,
            "Deleted all files in: %s",
            this.targetDir
        );
    }

    /**
     * Logging.
     *
     * @param msg Message for logging
     * @param dir The directory
     */
    private void log(final String msg, final File dir) {
        Logger.info(
            this,
            msg,
            dir.toString()
        );
    }

    /**
     * Remove single file if existed.
     *
     * @param file File to purge
     */
    private void purge(final File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (final IOException ex) {
            Logger.error(
                this,
                "Error while deleting: %s\nmessage: %s",
                file.toString(),
                ex.getMessage()
            );
        }
    }
}
