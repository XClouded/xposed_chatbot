package com.taobao.android.dinamic.view;

import android.view.View;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import java.util.ArrayList;

public class ViewResult {
    private ArrayList<View> bindDataList;
    private DinamicError dinamicError;
    private DinamicTemplate dinamicTemplate;
    private String module;
    private View view;

    public ViewResult(String str) {
        this.module = str;
    }

    public DinamicTemplate getDinamicTemplate() {
        return this.dinamicTemplate;
    }

    public void setDinamicTemplate(DinamicTemplate dinamicTemplate2) {
        this.dinamicTemplate = dinamicTemplate2;
    }

    public DinamicError getDinamicError() {
        if (this.dinamicError == null) {
            this.dinamicError = new DinamicError(this.module);
        }
        return this.dinamicError;
    }

    public boolean isRenderSuccess() {
        return this.dinamicError == null || this.dinamicError.isEmpty();
    }

    public boolean isBindDataSuccess() {
        return this.dinamicError == null || this.dinamicError.isEmpty();
    }

    public View getView() {
        return this.view;
    }

    public void setView(View view2) {
        this.view = view2;
    }

    public ArrayList<View> getBindDataList() {
        return this.bindDataList;
    }

    public void setBindDataList(ArrayList<View> arrayList) {
        this.bindDataList = arrayList;
    }
}
