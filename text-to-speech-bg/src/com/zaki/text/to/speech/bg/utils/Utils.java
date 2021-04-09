package com.zaki.text.to.speech.bg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Utils {

    private Utils() {
        noop();
    }

    public static void noop() {
        // nothing to do
    }

    public static List<String> getFileLines(Class<?> loader, String fileName) {
        List result = new ArrayList<>();
        try (Scanner sc = new Scanner(loader.getClassLoader().getResourceAsStream(fileName))) {
            while (sc.hasNextLine()) {
                result.add(sc.nextLine().trim());
            }
        }
        return result;
    }

    public static void logSilently(Throwable t) {
        System.out.println(t.getMessage());
        t.printStackTrace(System.out);
    }

    public static void logAndThrow(Throwable t) throws Throwable {
        logSilently(t);
        throw t;
    }
}
