package com.taobao.alivfsadapter.database.alidb;

import com.taobao.alivfsadapter.AVFSDBFactory;
import com.taobao.alivfsadapter.AVFSDataBase;

public class AVFSAliDBFactory extends AVFSDBFactory {
    public AVFSDataBase createDataBase(String str) throws Exception {
        return new AVFSAliDBImpl(str, 1);
    }

    public AVFSDataBase createDataBase(String str, int i) throws Exception {
        return new AVFSAliDBImpl(str, i);
    }

    public AVFSDataBase createDataBase(String str, String str2) throws Exception {
        return new AVFSAliDBImpl(str, str2, 1);
    }

    public AVFSDataBase createDataBase(String str, String str2, int i) throws Exception {
        return new AVFSAliDBImpl(str, str2, i);
    }
}
