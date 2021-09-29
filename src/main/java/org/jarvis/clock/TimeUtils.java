package org.jarvis.clock;

import java.util.concurrent.TimeUnit;

public abstract class TimeUtils {

    public static void sleep(int seconds) {
        sleep(seconds, TimeUnit.SECONDS);
    }

    public static void sleep(long duration, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
