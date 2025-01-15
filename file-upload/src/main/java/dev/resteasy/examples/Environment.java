/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2025 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.resteasy.examples;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Optional;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class Environment {

    private static final Path UPLOAD_DIRECTORY;
    private static final boolean SUPPORTS_POSIX;

    static {
        final String user = System.getProperty("user.name", "anonymous");
        final String uploadDirectory = System.getProperty("upload.directory");
        Path dir = null;
        if (uploadDirectory == null) {
            final String jbossHome = System.getProperty("jboss.home.dir");
            if (jbossHome != null) {
                dir = Path.of(jbossHome, "file-uploads", user);
            }
        } else {
            dir = Path.of(uploadDirectory, user);
        }
        try {
            if (dir == null) {
                dir = Files.createTempDirectory("file-uploads").resolve(user);
            }
            if (Files.notExists(dir)) {
                Files.createDirectories(dir);
            }
            UPLOAD_DIRECTORY = dir;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        SUPPORTS_POSIX = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
    }

    public static Path uploadDirectory() {
        return UPLOAD_DIRECTORY;
    }

    public static boolean supportsPosix() {
        return SUPPORTS_POSIX;
    }

    public static Optional<String> resolvePermissions(final Path file) {
        if (Files.isRegularFile(file)) {
            if (SUPPORTS_POSIX) {
                try {
                    return Optional.of(PosixFilePermissions.toString(Files.getPosixFilePermissions(file)));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
        return Optional.empty();
    }
}
