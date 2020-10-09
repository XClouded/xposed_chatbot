package com.alimama.union.app.privacy;

import androidx.annotation.NonNull;
import com.alimama.union.app.privacy.PrivacyDialog;

public class PrivacyConfig {
    private PrivacyDialog.PrivacyCallBack centerBtnCallback;
    private String centerBtnText;
    private String content;
    private boolean isNotShowCloseImg;
    private boolean isVertical;
    private String jumpUrl;
    private PrivacyDialog.PrivacyCallBack leftCallback;
    private String leftText;
    private PrivacyDialog.PrivacyCallBack rightCallback;
    private String rightText;
    private String spanStr;
    private String title;
    private PrivacyDialog.PrivacyCallBack topRightCloseBtnCallback;

    public PrivacyConfig() {
    }

    public PrivacyConfig(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4) {
        this.title = str;
        this.content = str2;
        this.leftText = str3;
        this.rightText = str4;
    }

    public PrivacyConfig(@NonNull String str, @NonNull String str2, @NonNull String str3) {
        this.title = str;
        this.content = str2;
        this.centerBtnText = str3;
    }

    public boolean isNotShowCloseImg() {
        return this.isNotShowCloseImg;
    }

    public void setNotShowCloseImg(boolean z) {
        this.isNotShowCloseImg = z;
    }

    public PrivacyConfig setTitle(String str) {
        this.title = str;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public PrivacyConfig setContent(String str) {
        this.content = str;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public PrivacyConfig setVertical(boolean z) {
        this.isVertical = z;
        return this;
    }

    public boolean isVertical() {
        return this.isVertical;
    }

    public PrivacyConfig setLeftCallback(PrivacyDialog.PrivacyCallBack privacyCallBack) {
        this.leftCallback = privacyCallBack;
        return this;
    }

    public PrivacyConfig setRightCallback(PrivacyDialog.PrivacyCallBack privacyCallBack) {
        this.rightCallback = privacyCallBack;
        return this;
    }

    public PrivacyConfig setCenterBtnCallback(PrivacyDialog.PrivacyCallBack privacyCallBack) {
        this.centerBtnCallback = privacyCallBack;
        return this;
    }

    public String getCenterBtnText() {
        return this.centerBtnText;
    }

    public PrivacyDialog.PrivacyCallBack getTopRightCloseBtnCallback() {
        return this.topRightCloseBtnCallback;
    }

    public PrivacyConfig setCenterBtnText(String str) {
        this.centerBtnText = str;
        return this;
    }

    public PrivacyConfig setTopRightCloseBtnCallback(PrivacyDialog.PrivacyCallBack privacyCallBack) {
        this.topRightCloseBtnCallback = privacyCallBack;
        return this;
    }

    public PrivacyConfig setRightText(String str) {
        this.rightText = str;
        return this;
    }

    public PrivacyConfig setSpanStr(String str) {
        this.spanStr = str;
        return this;
    }

    public PrivacyConfig setJumpUrl(String str) {
        this.jumpUrl = str;
        return this;
    }

    public PrivacyConfig setLeftText(String str) {
        this.leftText = str;
        return this;
    }

    public String getSpanStr() {
        return this.spanStr;
    }

    public String getJumpUrl() {
        return this.jumpUrl;
    }

    public PrivacyDialog.PrivacyCallBack getLeftCallback() {
        return this.leftCallback;
    }

    public String getLeftText() {
        return this.leftText;
    }

    public PrivacyDialog.PrivacyCallBack getRightCallback() {
        return this.rightCallback;
    }

    public PrivacyDialog.PrivacyCallBack getCenterBtnCallback() {
        return this.centerBtnCallback;
    }

    public String getRightText() {
        return this.rightText;
    }
}
