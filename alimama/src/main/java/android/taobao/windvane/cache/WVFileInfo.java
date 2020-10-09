package android.taobao.windvane.cache;

import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

public class WVFileInfo {
    private static final String DEFAULT_ENCODING = "utf-8";
    private static final String DEFAULT_NULL = "";
    private static final String DEFAULT_TIME_ZERO = "0000000000000";
    public static final char DIVISION = '~';
    private static final char PARTITION = '_';
    public String encoding;
    public String etag;
    public long expireTime;
    public String fileName;
    public long lastModified;
    public String mimeType;
    public long pos;
    public String sha256ToHex;
    public boolean valid = true;

    public WVFileInfo convertToFileInfo() {
        if (getClass().equals(WVFileInfo.class)) {
            return this;
        }
        WVFileInfo wVFileInfo = new WVFileInfo();
        wVFileInfo.expireTime = this.expireTime;
        wVFileInfo.lastModified = this.lastModified;
        wVFileInfo.fileName = this.fileName;
        wVFileInfo.mimeType = this.mimeType;
        wVFileInfo.sha256ToHex = this.sha256ToHex;
        wVFileInfo.etag = this.etag;
        wVFileInfo.encoding = this.encoding;
        wVFileInfo.pos = this.pos;
        wVFileInfo.valid = this.valid;
        return wVFileInfo;
    }

    public int compareTo(WVFileInfo wVFileInfo) {
        if (this == wVFileInfo) {
            return 0;
        }
        return this.expireTime > wVFileInfo.expireTime ? 1 : -1;
    }

    public byte[] composeFileInfoStr() {
        StringBuilder sb = new StringBuilder();
        if (this.expireTime > 0) {
            sb.append(this.expireTime);
        } else {
            sb.append(DEFAULT_TIME_ZERO);
        }
        if (this.valid) {
            sb.append(DIVISION);
        } else {
            sb.append(PARTITION);
        }
        if (this.lastModified > 0) {
            sb.append(this.lastModified);
        } else {
            sb.append(DEFAULT_TIME_ZERO);
        }
        if (this.valid) {
            sb.append(DIVISION);
        } else {
            sb.append(PARTITION);
        }
        if (this.fileName == null) {
            sb.append("");
        } else {
            sb.append(this.fileName);
        }
        if (this.valid) {
            sb.append(DIVISION);
        } else {
            sb.append(PARTITION);
        }
        if (this.sha256ToHex == null) {
            sb.append("");
        } else {
            sb.append(this.sha256ToHex);
        }
        if (this.valid) {
            sb.append(DIVISION);
        } else {
            sb.append(PARTITION);
        }
        if (this.mimeType == null) {
            sb.append("");
        } else {
            sb.append(this.mimeType);
        }
        if (this.valid) {
            sb.append(DIVISION);
        } else {
            sb.append(PARTITION);
        }
        if (this.etag == null) {
            sb.append("");
        } else {
            sb.append(this.etag);
        }
        if (this.valid) {
            sb.append(DIVISION);
        } else {
            sb.append(PARTITION);
        }
        if (TextUtils.isEmpty(this.encoding)) {
            sb.append("utf-8");
        } else {
            sb.append(this.encoding);
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d("FileInfo", "composeFileInfoStr:" + sb);
        }
        try {
            return sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
