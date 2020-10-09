package com.taobao.alivfsadapter;

public class AVFSDefaultDBFactory extends AVFSDBFactory {
    public AVFSDataBase createDataBase(String str) throws Exception {
        return new AVFSDefaultDataBaseImpl(str, 1);
    }

    public AVFSDataBase createDataBase(String str, int i) throws Exception {
        return new AVFSDefaultDataBaseImpl(str, i);
    }

    public AVFSDataBase createDataBase(String str, String str2) throws Exception {
        return new AVFSDefaultDataBaseImpl(str, str2, 1);
    }

    public AVFSDataBase createDataBase(String str, String str2, int i) throws Exception {
        return new AVFSDefaultDataBaseImpl(str, str2, i);
    }
}
