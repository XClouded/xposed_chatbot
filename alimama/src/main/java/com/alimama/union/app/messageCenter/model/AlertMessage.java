package com.alimama.union.app.messageCenter.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import java.util.Date;

@Entity(primaryKeys = {"taobaoNumId", "msgType"})
public class AlertMessage {
    public String action;
    public String actionUrl;
    public String content;
    @NonNull
    public Date createTime;
    public Integer expireDay;
    public Long id;
    @NonNull
    public Integer msgType;
    @NonNull
    public Integer read = 1;
    @Nullable
    public Date readTime;
    @NonNull
    public Long taobaoNumId;
    public String title;

    public Long getTaobaoNumId() {
        return this.taobaoNumId;
    }

    public void setTaobaoNumId(Long l) {
        this.taobaoNumId = l;
    }

    @NonNull
    public Integer getMsgType() {
        return this.msgType;
    }

    public void setMsgType(@NonNull Integer num) {
        this.msgType = num;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String str) {
        this.action = str;
    }

    public String getActionUrl() {
        return this.actionUrl;
    }

    public void setActionUrl(String str) {
        this.actionUrl = str;
    }

    @NonNull
    public Integer getRead() {
        return this.read;
    }

    public void setRead(@NonNull Integer num) {
        this.read = num;
    }

    @NonNull
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(@NonNull Date date) {
        this.createTime = date;
    }

    public Integer getExpireDay() {
        return this.expireDay;
    }

    public void setExpireDay(Integer num) {
        this.expireDay = num;
    }

    @Nullable
    public Date getReadTime() {
        return this.readTime;
    }

    public void setReadTime(@Nullable Date date) {
        this.readTime = date;
    }
}
