package com.taobao.android.dinamic.property;

import android.view.View;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.expressionv2.ExpressionProcessor;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.view.DinamicError;

public class DinamicEventListener implements View.OnClickListener, View.OnLongClickListener {
    private DinamicParams dinamicParams;
    private String eventExp;
    private DinamicProperty property;

    public boolean onLongClick(View view) {
        long nanoTime = System.nanoTime();
        try {
            this.dinamicParams.setCurrentData(view.getTag(DinamicTagKey.SUBDATA));
            ExpressionProcessor.handleEvent(view, this.eventExp, this.dinamicParams);
            DinamicLog.logHandleEvent(this.dinamicParams.getModule(), this.property.viewIdentify, System.nanoTime() - nanoTime);
            return true;
        } catch (Throwable unused) {
            this.dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_EXCEPTION, this.property.viewIdentify);
            DinamicLog.logHandleEvent(this.dinamicParams.getModule(), this.property.viewIdentify, System.nanoTime() - nanoTime);
            return true;
        }
    }

    public DinamicEventListener(DinamicParams dinamicParams2, String str, DinamicProperty dinamicProperty) {
        this.dinamicParams = dinamicParams2;
        this.eventExp = str;
        this.property = dinamicProperty;
    }

    public void onClick(View view) {
        long nanoTime = System.nanoTime();
        try {
            this.dinamicParams.setCurrentData(view.getTag(DinamicTagKey.SUBDATA));
            ExpressionProcessor.handleEvent(view, this.eventExp, this.dinamicParams);
            DinamicLog.logHandleEvent(this.dinamicParams.getModule(), this.property.viewIdentify, System.nanoTime() - nanoTime);
        } catch (Throwable unused) {
            this.dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_EXCEPTION, this.property.viewIdentify);
            DinamicLog.logHandleEvent(this.dinamicParams.getModule(), this.property.viewIdentify, System.nanoTime() - nanoTime);
        }
    }
}
