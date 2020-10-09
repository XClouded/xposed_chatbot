package com.ut.mini.module.process;

public class MultiProcessManager {
    private static AbsMultiProcessAdapter multiProcessAdapter;

    public static void setMultiProcessAdapter(AbsMultiProcessAdapter absMultiProcessAdapter) {
        multiProcessAdapter = absMultiProcessAdapter;
    }

    public static AbsMultiProcessAdapter getMultiProcessAdapter() {
        return multiProcessAdapter;
    }
}
