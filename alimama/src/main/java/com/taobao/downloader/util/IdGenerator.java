package com.taobao.downloader.util;

public class IdGenerator {
    private static int id;

    public static synchronized int nextId() {
        int i;
        synchronized (IdGenerator.class) {
            if (id >= Integer.MAX_VALUE) {
                id = 0;
            }
            i = id;
            id = i + 1;
        }
        return i;
    }
}
