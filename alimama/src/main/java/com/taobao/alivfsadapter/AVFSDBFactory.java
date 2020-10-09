package com.taobao.alivfsadapter;

public abstract class AVFSDBFactory {
    public abstract AVFSDataBase createDataBase(String str) throws Exception;

    public abstract AVFSDataBase createDataBase(String str, int i) throws Exception;

    public abstract AVFSDataBase createDataBase(String str, String str2) throws Exception;

    public abstract AVFSDataBase createDataBase(String str, String str2, int i) throws Exception;
}
