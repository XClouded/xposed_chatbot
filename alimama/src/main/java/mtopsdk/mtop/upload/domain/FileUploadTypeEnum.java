package mtopsdk.mtop.upload.domain;

public enum FileUploadTypeEnum {
    RESUMABLE("resumable"),
    NORMAL("normal");
    
    private String uploadType;

    public String getUploadType() {
        return this.uploadType;
    }

    private FileUploadTypeEnum(String str) {
        this.uploadType = str;
    }
}
