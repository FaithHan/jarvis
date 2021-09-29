package org.jarvis.clock;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;



public class Stopwatch {

    private boolean isRunning;

    private long elapsedNanos;

    private long startTick;

    public static Stopwatch create() {
        return new Stopwatch();
    }

    public static Stopwatch createStarted() {
        return new Stopwatch().start();
    }

    private Stopwatch start() {
        checkState(!isRunning, "This stopwatch is already running.");
        isRunning = true;
        startTick = System.nanoTime();
        return this;
    }

    public Stopwatch stop() {
        long tick = System.nanoTime();
        checkState(isRunning, "This stopwatch is already stopped.");
        isRunning = false;
        elapsedNanos += tick - startTick;
        return this;
    }

    public Stopwatch reset() {
        elapsedNanos = 0;
        isRunning = false;
        return this;
    }

    private long elapsedNanos() {
        return isRunning ? System.nanoTime() - startTick + elapsedNanos : elapsedNanos;
    }

    public long elapsed(TimeUnit desiredUnit) {
        return desiredUnit.convert(elapsedNanos(), NANOSECONDS);
    }

    public long elapsedMillis() {
        return elapsed(TimeUnit.MILLISECONDS);
    }

    public long elapsedSeconds() {
        return elapsed(TimeUnit.SECONDS);
    }

    private static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }
}
