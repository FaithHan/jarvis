//package org.jarvis.thread;
//
//import java.util.Map;
//
//public abstract class InheritableThreadLocalLocalCache {
//
//    private static final InheritableThreadLocal<Map<String, Object>> THREAD_LOCAL = new InheritableThreadLocal<>();
//
//    public static void set(Object value) {
//        THREAD_LOCAL.set(value);
//    }
//
//    public static Boolean getBoolean() {
//        return (Boolean) THREAD_LOCAL.get();
//    }
//
//    public static Integer getInteger() {
//        return (Integer) THREAD_LOCAL.get();
//    }
//
//    public static Long getLong() {
//        return (Long) THREAD_LOCAL.get();
//    }
//
//    public static String getString() {
//        return (String) THREAD_LOCAL.get();
//    }
//
//    @SuppressWarnings({"unchecked"})
//    public static <T> T get(Class<T> classValue) {
//        return (T) THREAD_LOCAL.get();
//    }
//
//}
