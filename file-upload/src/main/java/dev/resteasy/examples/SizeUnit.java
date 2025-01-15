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

import java.text.DecimalFormat;

public enum SizeUnit {
    BYTE(1L, "B") {
        @Override
        public String toString(final long size) {
            return size + "B";
        }
    },
    KILOBYTE(BYTE, "KB"),
    MEGABYTE(KILOBYTE, "MB"),
    GIGABYTE(MEGABYTE, "GB"),
    TERABYTE(GIGABYTE, "TB"),
    PETABYTE(TERABYTE, "PB"),
    EXABYTE(TERABYTE, "EB"),

    ;

    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");
    private final long sizeInBytes;
    private final String abbreviation;

    SizeUnit(final long sizeInBytes, final String abbreviation) {
        this.sizeInBytes = sizeInBytes;
        this.abbreviation = abbreviation;
    }

    SizeUnit(final SizeUnit base, final String abbreviation) {
        this.sizeInBytes = base.sizeInBytes << 10;
        this.abbreviation = abbreviation;
    }

    /**
     * Returns the abbreviation for the unit.
     *
     * @return the abbreviation for the unit
     */
    public String abbreviation() {
        return abbreviation;
    }

    /**
     * Converts the given size to bytes from this unit. For example {@code SizeUnit.KILOBYTES.toBytes(1L)} would return
     * 1024.
     *
     * @param size the size to convert
     *
     * @return the size in bytes
     */
    public long toBytes(final long size) {
        return Math.multiplyExact(sizeInBytes, size);
    }

    /**
     * Converts the given size to the given unit to this unit.
     *
     * @param size the size to convert
     * @param unit the unit to convert the size to
     *
     * @return the converted units
     */
    public double convert(final long size, final SizeUnit unit) {
        if (unit == BYTE) {
            return toBytes(size);
        }
        final long bytes = toBytes(size);
        return ((double) bytes / unit.sizeInBytes);
    }

    /**
     * Converts the size to a human-readable string format.
     * <p>
     * For example {@code SizeUnit.KILOBYTE.toString(1024L)} would return "1 KB".
     * </p>
     *
     * @param size the size, in bytes
     *
     * @return a human-readable size
     */
    public String toString(final long size) {
        return FORMAT.format((double) size / sizeInBytes) + abbreviation;
    }

    /**
     * Converts the size, in bytes, to a human-readable form. For example {@code 1024} bytes return "1 KB".
     *
     * @param size the size, in bytes, to convert
     *
     * @return a human-readable size
     */
    public static String toHumanReadable(final long size) {
        if (size == 0L) {
            return "0B";
        }
        final SizeUnit[] values = values();
        for (int i = values.length - 1; i >= 0; i--) {
            final SizeUnit unit = values[i];
            if (size >= unit.sizeInBytes) {
                return unit.toString(size);
            }
        }
        return size + "B";
    }
}