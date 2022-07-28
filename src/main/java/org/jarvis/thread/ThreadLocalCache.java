//package org.jarvis.thread;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public abstract class ThreadLocalCache {
//
//    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);
//
//    public static void set(String key, Object value) {
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
