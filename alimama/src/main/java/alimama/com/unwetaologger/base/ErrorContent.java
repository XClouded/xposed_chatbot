package alimama.com.unwetaologger.base;

public class ErrorContent extends LogContent {
    public String errorCode;

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String str) {
        this.errorCode = str;
    }
}
