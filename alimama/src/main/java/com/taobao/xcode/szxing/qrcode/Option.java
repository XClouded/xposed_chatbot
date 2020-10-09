package com.taobao.xcode.szxing.qrcode;

import android.graphics.Bitmap;
import com.taobao.xcode.szxing.qrcode.decoder.ErrorCorrectionLevel;
import com.taobao.xcode.szxing.qrcode.decoder.Version;
import java.io.Serializable;

public final class Option implements Serializable {
    private static final long serialVersionUID = 1280406242341959719L;
    private Integer backgroundColor = -1;
    private String characterSet = "utf-8";
    private Bitmap.Config colorDepth = Bitmap.Config.ARGB_8888;
    private ErrorCorrectionLevel ecLevel = ErrorCorrectionLevel.M;
    private Integer foregroundColor = -16777216;
    private Bitmap logo;
    private int margin = 1;
    private Integer pdpInnerColor = -16777216;
    private Version version;

    public ErrorCorrectionLevel getEcLevel() {
        return this.ecLevel;
    }

    public void setEcLevel(ErrorCorrectionLevel errorCorrectionLevel) {
        this.ecLevel = errorCorrectionLevel;
    }

    public int getMargin() {
        return this.margin;
    }

    public void setMargin(int i) {
        this.margin = i;
    }

    public Version getVersion() {
        return this.version;
    }

    public void setVersion(Version version2) {
        this.version = version2;
    }

    public String getCharacterSet() {
        return this.characterSet;
    }

    public void setCharacterSet(String str) {
        this.characterSet = str;
    }

    public Integer getPdpInnerColor() {
        return this.pdpInnerColor;
    }

    public void setPdpInnerColor(Integer num) {
        this.pdpInnerColor = num;
    }

    public Integer getForegroundColor() {
        return this.foregroundColor;
    }

    public void setForegroundColor(Integer num) {
        this.foregroundColor = num;
    }

    public Integer getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(Integer num) {
        this.backgroundColor = num;
    }

    public Bitmap.Config getColorDepth() {
        return this.colorDepth;
    }

    public void setColorDepth(Bitmap.Config config) {
        this.colorDepth = config;
    }

    public Bitmap getLogo() {
        return this.logo;
    }

    public void setLogo(Bitmap bitmap) {
        this.logo = bitmap;
    }
}
