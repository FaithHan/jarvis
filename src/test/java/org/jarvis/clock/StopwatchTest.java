package org.jarvis.clock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StopwatchTest {

    @Test
    void create() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        TimeUtils.sleep(1);
        System.out.println(stopwatch.elapsedSeconds());
    }
}