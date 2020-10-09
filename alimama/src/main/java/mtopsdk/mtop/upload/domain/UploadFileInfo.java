package mtopsdk.mtop.upload.domain;

import com.taobao.weex.el.parse.Operators;
import mtopsdk.common.util.StringUtils;
import mtopsdk.mtop.upload.DefaultFileUploadListener;
import mtopsdk.mtop.upload.FileUploadBaseListener;
import mtopsdk.mtop.upload.FileUploadListener;

public class UploadFileInfo {
    private String bizCode;
    private String filePath;
    private FileStreamInfo fileStreamInfo;
    private FileUploadBaseListener listener;
    private String ownerNick;
    private String privateData;
    private FileUploadTypeEnum type = FileUploadTypeEnum.RESUMABLE;
    private boolean useHttps = false;

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public FileStreamInfo getFileStreamInfo() {
        return this.fileStreamInfo;
    }

    public void setFileStreamInfo(FileStreamInfo fileStreamInfo2) {
        this.fileStreamInfo = fileStreamInfo2;
    }

    public String getBizCode() {
        return this.bizCode;
    }

    public void setBizCode(String str) {
        this.bizCode = str;
    }

    public String getOwnerNick() {
        return this.ownerNick;
    }

    public void setOwnerNick(String str) {
        this.ownerNick = str;
    }

    public String getPrivateData() {
        return this.privateData;
    }

    public void setPrivateData(String str) {
        this.privateData = str;
    }

    public FileUploadTypeEnum getType() {
        return this.type;
    }

    public void setType(FileUploadTypeEnum fileUploadTypeEnum) {
        if (fileUploadTypeEnum != null) {
            this.type = fileUploadTypeEnum;
        }
    }

    public FileUploadBaseListener getListener() {
        return this.listener;
    }

    @Deprecated
    public void setListener(FileUploadListener fileUploadListener) {
        if (fileUploadListener != null) {
            if (fileUploadListener instanceof FileUploadBaseListener) {
                this.listener = (FileUploadBaseListener) fileUploadListener;
            } else {
                this.listener = new DefaultFileUploadListener(fileUploadListener);
            }
        }
    }

    public void setListener(FileUploadBaseListener fileUploadBaseListener) {
        this.listener = fileUploadBaseListener;
    }

    public boolean isUseHttps() {
        return this.useHttps;
    }

    public void setUseHttps(boolean z) {
        this.useHttps = z;
    }

    public boolean isValid() {
        if (StringUtils.isBlank(this.bizCode)) {
            return false;
        }
        if (!StringUtils.isBlank(this.filePath) || (this.fileStreamInfo != null && this.fileStreamInfo.isValid())) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "UploadFileInfo [" + "filePath=" + this.filePath + ", fileStreamInfo=" + this.fileStreamInfo + ", bizCode=" + this.bizCode + ", ownerNick=" + this.ownerNick + ", privateData=" + this.type + ", listener=" + this.listener + Operators.ARRAY_END_STR;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((((((((this.bizCode == null ? 0 : this.bizCode.hashCode()) + 31) * 31) + (this.filePath == null ? 0 : this.filePath.hashCode())) * 31) + (this.fileStreamInfo == null ? 0 : this.fileStreamInfo.hashCode())) * 31) + (this.listener == null ? 0 : this.listener.hashCode())) * 31) + (this.ownerNick == null ? 0 : this.ownerNick.hashCode())) * 31) + (this.privateData == null ? 0 : this.privateData.hashCode())) * 31;
        if (this.type != null) {
            i = this.type.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UploadFileInfo uploadFileInfo = (UploadFileInfo) obj;
        if (this.bizCode == null) {
            if (uploadFileInfo.bizCode != null) {
                return false;
            }
        } else if (!this.bizCode.equals(uploadFileInfo.bizCode)) {
            return false;
        }
        if (this.filePath == null) {
            if (uploadFileInfo.filePath != null) {
                return false;
            }
        } else if (!this.filePath.equals(uploadFileInfo.filePath)) {
            return false;
        }
        if (this.fileStreamInfo == null) {
            if (uploadFileInfo.fileStreamInfo != null) {
                return false;
            }
        } else if (!this.fileStreamInfo.equals(uploadFileInfo.fileStreamInfo)) {
            return false;
        }
        if (this.listener == null) {
            if (uploadFileInfo.listener != null) {
                return false;
            }
        } else if (!this.listener.equals(uploadFileInfo.listener)) {
            return false;
        }
        if (this.ownerNick == null) {
            if (uploadFileInfo.ownerNick != null) {
                return false;
            }
        } else if (!this.ownerNick.equals(uploadFileInfo.ownerNick)) {
            return false;
        }
        if (this.privateData == null) {
            if (uploadFileInfo.privateData != null) {
                return false;
            }
        } else if (!this.privateData.equals(uploadFileInfo.privateData)) {
            return false;
        }
        if (this.type == uploadFileInfo.type) {
            return true;
        }
        return false;
    }
}
