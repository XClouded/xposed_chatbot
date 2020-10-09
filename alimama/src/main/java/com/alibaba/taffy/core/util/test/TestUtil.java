package com.alibaba.taffy.core.util.test;

import java.io.PrintStream;

public class TestUtil {
    public static long testPerformance(String str, int i, Runnable runnable) {
        if (i <= 0) {
            return 0;
        }
        long currentTimeMillis = System.currentTimeMillis();
        for (int i2 = 0; i2 < i; i2++) {
            runnable.run();
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        PrintStream printStream = System.out;
        printStream.println(str + " for " + i + " times");
        PrintStream printStream2 = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("all time:\t");
        sb.append(currentTimeMillis2);
        printStream2.println(sb.toString());
        PrintStream printStream3 = System.out;
        printStream3.println("per time:\t" + (currentTimeMillis2 / ((long) i)) + "\n");
        return currentTimeMillis2;
    }
}
