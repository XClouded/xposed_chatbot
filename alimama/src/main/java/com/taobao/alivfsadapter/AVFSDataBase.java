package com.taobao.alivfsadapter;

public abstract class AVFSDataBase {

    public interface IExecCallback {
        void onExecDone(boolean z);
    }

    public abstract int close();

    public abstract AVFSDBCursor execQuery(String str) throws Exception;

    public abstract AVFSDBCursor execQuery(String str, Object[] objArr) throws Exception;

    public abstract void execUpdate(String str, IExecCallback iExecCallback);

    public abstract void execUpdate(String str, Object[] objArr, IExecCallback iExecCallback);

    public abstract boolean execUpdate(String str) throws Exception;

    public abstract boolean execUpdate(String str, Object[] objArr) throws Exception;

    public AVFSDataBase(String str, int i) {
    }

    public AVFSDataBase(String str, String str2, int i) {
    }
}
