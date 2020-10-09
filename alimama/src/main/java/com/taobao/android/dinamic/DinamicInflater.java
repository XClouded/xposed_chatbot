package com.taobao.android.dinamic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrUtils;
import com.taobao.android.dinamic.view.CompatibleView;
import com.taobao.android.dinamic.view.DinamicError;

public class DinamicInflater extends LayoutInflater {
    private DinamicParams dinamicParams;

    protected DinamicInflater(Context context) {
        super(context);
    }

    protected DinamicInflater(LayoutInflater layoutInflater, Context context) {
        super(layoutInflater, context);
    }

    public static DinamicInflater from(Context context, DinamicParams dinamicParams2) {
        DinamicInflater dinamicInflater = new DinamicInflater(LayoutInflater.from(context), context);
        dinamicInflater.setDinamicParams(dinamicParams2);
        return dinamicInflater;
    }

    /* access modifiers changed from: protected */
    public View onCreateView(String str, AttributeSet attributeSet) throws ClassNotFoundException {
        if (Dinamic.getViewConstructor(str) != null) {
            try {
                return DinamicViewCreator.createView(str, getContext(), attributeSet, this.dinamicParams);
            } catch (Throwable th) {
                this.dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_EXCEPTION, str);
                DinamicLog.e("DinamicInflater", th, "onCreateView failed");
                return buildCompatibleView(DinamicError.ERROR_CODE_VIEW_EXCEPTION, str);
            }
        } else {
            this.dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_NOT_FOUND, str);
            return buildCompatibleView(DinamicError.ERROR_CODE_VIEW_NOT_FOUND, str);
        }
    }

    public LayoutInflater cloneInContext(Context context) {
        return from(context);
    }

    @Deprecated
    public View inflateView(int i, ViewGroup viewGroup, boolean z) {
        View inflate = super.inflate(i, (ViewGroup) null);
        DAttrUtils.handleRootViewLayoutParams(inflate, viewGroup);
        if (z) {
            viewGroup.addView(inflate);
        }
        return inflate;
    }

    public void setDinamicParams(DinamicParams dinamicParams2) {
        this.dinamicParams = dinamicParams2;
    }

    private CompatibleView buildCompatibleView(String str, String str2) {
        Context context = getContext();
        return new CompatibleView(context, str2 + str);
    }
}
