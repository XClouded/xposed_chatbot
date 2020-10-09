package com.taobao.monitor.impl.processor.launcher;

import java.util.ArrayList;
import java.util.List;

public class PageList {
    private static List<String> blackList = new ArrayList();
    private static List<String> complexPageList = new ArrayList();
    private static List<String> whiteList = new ArrayList();

    public static void addBlackPage(String str) {
        blackList.add(str);
    }

    public static boolean inBlackList(String str) {
        return blackList.contains(str);
    }

    public static void addWhitePage(String str) {
        whiteList.add(str);
    }

    public static boolean inWhiteList(String str) {
        return whiteList.contains(str);
    }

    public static boolean isWhiteListEmpty() {
        return whiteList.isEmpty();
    }

    public static void addComplexPage(String str) {
        complexPageList.add(str);
    }

    public static boolean inComplexPage(String str) {
        return complexPageList.contains(str);
    }
}
