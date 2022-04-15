package util;

public class Logger {
    public static void e(String tag, Object data) {
        System.err.println("ERROR: " + tag + ": " + data);
    }

    public static void d(Object data) {
        System.out.println("DEBUG: " + data);
    }

    public static void d(String tag, Object data) {
        System.out.println("DEBUG: " + tag + ": " + data);
    }

    public static void i(Object data) {
        System.out.println("INFO: " + data);
    }
}
