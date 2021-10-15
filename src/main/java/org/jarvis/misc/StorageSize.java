package org.jarvis.misc;

/**
 * 计算机存储容量
 */
public class StorageSize {

    public static final long BYTES_PER_KB = 1024;

    /**
     * Bytes per Megabyte.
     */
    public static final long BYTES_PER_MB = BYTES_PER_KB * 1024;

    /**
     * Bytes per Gigabyte.
     */
    public static final long BYTES_PER_GB = BYTES_PER_MB * 1024;

    /**
     * Bytes per Terabyte.
     */
    public static final long BYTES_PER_TB = BYTES_PER_GB * 1024;

    /**
     * Bytes per Petabyte.
     */
    public static final long BYTES_PER_PB = BYTES_PER_TB * 1024;


    private final long bytes;


    private StorageSize(long bytes) {
        this.bytes = bytes;
    }


    public static StorageSize ofBytes(long bytes) {
        return new StorageSize(bytes);
    }


    public static StorageSize ofKilobytes(long kilobytes) {
        return new StorageSize(Math.multiplyExact(kilobytes, BYTES_PER_KB));
    }


    public static StorageSize ofMegabytes(long megabytes) {
        return new StorageSize(Math.multiplyExact(megabytes, BYTES_PER_MB));
    }


    public static StorageSize ofGigabytes(long gigabytes) {
        return new StorageSize(Math.multiplyExact(gigabytes, BYTES_PER_GB));
    }


    public static StorageSize ofTerabytes(long terabytes) {
        return new StorageSize(Math.multiplyExact(terabytes, BYTES_PER_TB));
    }

    public static StorageSize ofPetabytes(long petabytes) {
        return new StorageSize(Math.multiplyExact(petabytes, BYTES_PER_PB));
    }


    public long toBytes() {
        return this.bytes;
    }


    public long toKilobytes() {
        return this.bytes / BYTES_PER_KB;
    }


    public long toMegabytes() {
        return this.bytes / BYTES_PER_MB;
    }


    public long toGigabytes() {
        return this.bytes / BYTES_PER_GB;
    }


    public long toTerabytes() {
        return this.bytes / BYTES_PER_TB;
    }

    public long toPetabytes() {
        return this.bytes / BYTES_PER_PB;
    }

}