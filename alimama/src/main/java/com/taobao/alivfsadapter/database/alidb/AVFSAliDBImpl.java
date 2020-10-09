package com.taobao.alivfsadapter.database.alidb;

import com.taobao.alivfsadapter.AVFSDBCursor;
import com.taobao.alivfsadapter.AVFSDataBase;
import com.taobao.android.alivfsdb.AliDB;
import com.taobao.android.alivfsdb.AliDBExecResult;
import com.taobao.android.alivfsdb.IExecCallback;
import com.taobao.android.alivfsdb.IUpgradeCallback;

public class AVFSAliDBImpl extends AVFSDataBase {
    private final AliDB mDB;

    public AVFSAliDBImpl(String str, int i) throws Exception {
        this(str, (String) null, i);
    }

    public AVFSAliDBImpl(String str, String str2, int i) throws Exception {
        super(str, str2, i);
        this.mDB = AliDB.create(str, i, str2, (IUpgradeCallback) null);
    }

    public AVFSDBCursor execQuery(String str) throws Exception {
        AliDBExecResult execQuery = this.mDB.execQuery(str);
        AVFSAliDBCursorImpl aVFSAliDBCursorImpl = new AVFSAliDBCursorImpl();
        if (execQuery != null) {
            if (execQuery.aliDBError != null) {
                throw new Exception("Error in AVFSAliDBImpl execQuery: " + execQuery.aliDBError.errorMsg);
            } else if (execQuery.aliResultSet != null) {
                aVFSAliDBCursorImpl.resultSet = execQuery.aliResultSet;
            }
        }
        return aVFSAliDBCursorImpl;
    }

    public AVFSDBCursor execQuery(String str, Object[] objArr) throws Exception {
        AliDBExecResult execQuery = this.mDB.execQuery(str, objArr);
        AVFSAliDBCursorImpl aVFSAliDBCursorImpl = new AVFSAliDBCursorImpl();
        if (execQuery != null) {
            if (execQuery.aliDBError != null) {
                throw new Exception("Error in AVFSAliDBImpl execQuery: " + execQuery.aliDBError.errorMsg);
            } else if (execQuery.aliResultSet != null) {
                aVFSAliDBCursorImpl.resultSet = execQuery.aliResultSet;
            }
        }
        return aVFSAliDBCursorImpl;
    }

    public boolean execUpdate(String str) throws Exception {
        AliDBExecResult execUpdate = this.mDB.execUpdate(str);
        if (execUpdate == null) {
            throw new Exception("Error in AVFSAliDBImpl execUpdate: resultSet is null!");
        } else if (execUpdate.aliDBError == null) {
            return true;
        } else {
            throw new Exception("Error in AVFSAliDBImpl execUpdate: " + execUpdate.aliDBError.errorMsg);
        }
    }

    public boolean execUpdate(String str, Object[] objArr) throws Exception {
        AliDBExecResult execUpdate = this.mDB.execUpdate(str, objArr);
        if (execUpdate == null) {
            throw new Exception("Error in AVFSAliDBImpl execUpdate: resultSet is null!");
        } else if (execUpdate.aliDBError == null) {
            return true;
        } else {
            throw new Exception(execUpdate.aliDBError.errorMsg);
        }
    }

    public void execUpdate(String str, AVFSDataBase.IExecCallback iExecCallback) {
        this.mDB.execUpdate(str, new IExecCallbackAdapter(iExecCallback));
    }

    public void execUpdate(String str, Object[] objArr, AVFSDataBase.IExecCallback iExecCallback) {
        this.mDB.execUpdate(str, objArr, new IExecCallbackAdapter(iExecCallback));
    }

    public int close() {
        return this.mDB.closeConnections();
    }

    private static class IExecCallbackAdapter implements IExecCallback {
        private final AVFSDataBase.IExecCallback mCallback;

        public IExecCallbackAdapter(AVFSDataBase.IExecCallback iExecCallback) {
            this.mCallback = iExecCallback;
        }

        public void onExecDone(AliDBExecResult aliDBExecResult) {
            this.mCallback.onExecDone(aliDBExecResult.aliDBError == null);
        }
    }
}
