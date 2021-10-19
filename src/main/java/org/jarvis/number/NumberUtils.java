package org.jarvis.number;

import org.jarvis.misc.Assert;

import java.lang.reflect.Array;

public abstract class NumberUtils {

    public static int min(int... array) {
        validateArray(array);
        int min = array[0];
        for (int number : array) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }

    public static long min(long... array) {
        validateArray(array);
        long min = array[0];
        for (long number : array) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }

    public static float min(float... array) {
        validateArray(array);
        float min = array[0];
        for (float number : array) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }

    public static double min(double... array) {
        validateArray(array);
        double min = array[0];
        for (double number : array) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }

    public static int max(int... array) {
        validateArray(array);
        int max = array[0];
        for (int number : array) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    public static long max(long... array) {
        validateArray(array);
        long max = array[0];
        for (long number : array) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    public static float max(float... array) {
        validateArray(array);
        float max = array[0];
        for (float number : array) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    public static double max(double... array) {
        validateArray(array);
        double max = array[0];
        for (double number : array) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    private static void validateArray(Object array) {
        Assert.notNull(array, "intArray can not be null.");
        Assert.isTrue(Array.getLength(array) != 0, "intArray cannot be empty.");
    }

}
