package com.taobao.android.dinamicx;

public class DXResult<T> {
    private DXError dxError;
    public T result;

    public DXResult(T t) {
        this.result = t;
    }

    public DXResult(DXError dXError) {
        this.dxError = dXError;
    }

    public DXResult(T t, DXError dXError) {
        this.result = t;
        this.dxError = dXError;
    }

    public DXResult() {
    }

    public DXError getDxError() {
        return this.dxError;
    }

    public void setDxError(DXError dXError) {
        this.dxError = dXError;
    }

    public void setResult(T t) {
        this.result = t;
    }

    public boolean hasError() {
        return this.dxError != null && this.dxError.dxErrorInfoList.size() > 0;
    }
}
