package com.ali.alihadeviceevaluator.util;

import android.content.SharedPreferences;

public class KVStorageUtils {
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sp;

    public static SharedPreferences getSP() {
        initSP();
        return sp;
    }

    public static SharedPreferences.Editor getEditor() {
        initEditor();
        return editor;
    }

    private static void initSP() {
        if (sp == null) {
            sp = Global.context.getSharedPreferences("deviceevaluator", 0);
        }
    }

    private static void initEditor() {
        if (editor == null) {
            initSP();
            editor = sp.edit();
        }
    }
}
