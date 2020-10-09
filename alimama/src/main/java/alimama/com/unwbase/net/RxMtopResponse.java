package alimama.com.unwbase.net;

import androidx.annotation.Nullable;

public class RxMtopResponse<T> {
    public boolean isReqSuccess;
    @Nullable
    public T result;
    public String retCode;
    public String retMsg;
}
