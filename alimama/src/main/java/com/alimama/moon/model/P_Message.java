package com.alimama.moon.model;

import com.google.gson.annotations.SerializedName;

public class P_Message {
    private long cid;
    @SerializedName("content")
    private String context;
    @SerializedName("createTime")
    private long create_time;
    private String others;
    private long pub_user_id;
    @SerializedName("pubNick")
    private String pub_user_name;
    private long receive_time;
    @SerializedName("id")
    private long server_id;
    private int status;
    private long sub_user_id;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private int type;

    public long getCid() {
        return this.cid;
    }

    public void setCid(long j) {
        this.cid = j;
    }

    public long getServer_id() {
        return this.server_id;
    }

    public void setServer_id(long j) {
        this.server_id = j;
    }

    public long getPub_user_id() {
        return this.pub_user_id;
    }

    public void setPub_user_id(long j) {
        this.pub_user_id = j;
    }

    public String getPub_user_name() {
        return this.pub_user_name;
    }

    public void setPub_user_name(String str) {
        this.pub_user_name = str;
    }

    public long getSub_user_id() {
        return this.sub_user_id;
    }

    public void setSub_user_id(long j) {
        this.sub_user_id = j;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getContext() {
        return this.context;
    }

    public void setContext(String str) {
        this.context = str;
    }

    public String getOthers() {
        return this.others;
    }

    public void setOthers(String str) {
        this.others = str;
    }

    public long getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(long j) {
        this.create_time = j;
    }

    public long getReceive_time() {
        return this.receive_time;
    }

    public void setReceive_time(long j) {
        this.receive_time = j;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[P_Message]");
        stringBuffer.append("cid: " + this.cid);
        stringBuffer.append(", server_id: " + this.server_id);
        stringBuffer.append(", sub_id: " + this.sub_user_id);
        stringBuffer.append(", title: " + this.title);
        stringBuffer.append(", content: " + this.context);
        stringBuffer.append(", create_time: " + this.create_time);
        stringBuffer.append(", type: " + this.type);
        stringBuffer.append(", pubNick: " + this.pub_user_name);
        stringBuffer.append(", pubID: " + this.pub_user_id);
        return stringBuffer.toString();
    }
}
