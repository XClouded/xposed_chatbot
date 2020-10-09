package com.taobao.orange.model;

import anet.channel.statist.Dimension;
import anet.channel.statist.Monitor;
import anet.channel.statist.StatObject;
import java.io.Serializable;

@Monitor(module = "private_orange", monitorPoint = "index_ack")
public class IndexAckDO extends StatObject implements Serializable {
    @Dimension
    public String indexId;
    @Dimension
    public String md5;
    @Dimension
    public String updateTime;

    public IndexAckDO() {
    }

    public IndexAckDO(String str, String str2, String str3) {
        this.indexId = str;
        this.updateTime = str2;
        this.md5 = str3;
    }

    public String toString() {
        return "IndexAckDO{" + "indexId='" + this.indexId + '\'' + ", updateTime='" + this.updateTime + '\'' + ", md5='" + this.md5 + '\'' + '}';
    }
}
