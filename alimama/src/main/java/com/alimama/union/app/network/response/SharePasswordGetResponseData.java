package com.alimama.union.app.network.response;

import java.util.Date;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class SharePasswordGetResponseData implements IMTOPDataObject {
    private String bizId;
    private String content;
    private Long createAppkey;
    private String extendInfo;
    private String myTaopwdToast;
    private String ownerName;
    private String password;
    private String picUrl;
    private String taopwdOwnerId;
    private String templateId;
    private String title;
    private String url;
    private Date validDate;

    public Long getCreateAppkey() {
        return this.createAppkey;
    }

    public void setCreateAppkey(Long l) {
        this.createAppkey = l;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String str) {
        this.templateId = str;
    }

    public String getExtendInfo() {
        return this.extendInfo;
    }

    public void setExtendInfo(String str) {
        this.extendInfo = str;
    }

    public String getMyTaopwdToast() {
        return this.myTaopwdToast;
    }

    public void setMyTaopwdToast(String str) {
        this.myTaopwdToast = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String str) {
        this.ownerName = str;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String str) {
        this.picUrl = str;
    }

    public String getTaopwdOwnerId() {
        return this.taopwdOwnerId;
    }

    public void setTaopwdOwnerId(String str) {
        this.taopwdOwnerId = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public Date getValidDate() {
        return this.validDate;
    }

    public void setValidDate(Date date) {
        this.validDate = date;
    }

    public String getBizId() {
        return this.bizId;
    }

    public void setBizId(String str) {
        this.bizId = str;
    }
}
