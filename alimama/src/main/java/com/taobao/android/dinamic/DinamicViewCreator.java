package com.taobao.android.dinamic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DinamicProperty;
import com.taobao.android.dinamic.view.DLoopLinearLayout;
import com.taobao.android.dinamic.view.DinamicError;
import com.taobao.android.dinamic.view.ViewResult;
import java.util.ArrayList;

public final class DinamicViewCreator {
    public static View createView(String str, Context context, AttributeSet attributeSet, DinamicParams dinamicParams) {
        DinamicViewAdvancedConstructor viewConstructor = Dinamic.getViewConstructor(str);
        if (viewConstructor == null) {
            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_NOT_FOUND, str);
            return null;
        }
        View initializeViewWithModule = viewConstructor.initializeViewWithModule(str, context, attributeSet, dinamicParams);
        if (initializeViewWithModule == null) {
            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_NOT_FOUND, str);
            return null;
        }
        DinamicProperty handleAttributeSet = viewConstructor.handleAttributeSet(attributeSet);
        viewConstructor.applyDefaultProperty(initializeViewWithModule, handleAttributeSet.fixedProperty, dinamicParams);
        if (!handleAttributeSet.dinamicProperty.isEmpty() || !handleAttributeSet.eventProperty.isEmpty()) {
            dinamicParams.getViewResult().getBindDataList().add(initializeViewWithModule);
        }
        handleAttributeSet.viewIdentify = str;
        initializeViewWithModule.setTag(DinamicTagKey.PROPERTY_KEY, handleAttributeSet);
        ArrayList arrayList = new ArrayList(20);
        arrayList.addAll(handleAttributeSet.fixedProperty.keySet());
        viewConstructor.bindDataImpl(initializeViewWithModule, handleAttributeSet.fixedProperty, arrayList, dinamicParams);
        return initializeViewWithModule;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x003c, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003d, code lost:
        r8.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0040, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0041, code lost:
        r5 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x003c A[ExcHandler: Exception (r8v2 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:4:0x0012] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean rMatchViewType(android.view.View r8, com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor r9, java.lang.Object[] r10, java.lang.Object[] r11) {
        /*
            java.lang.Class r9 = r9.getDebugClass()
            java.lang.Class<com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor> r0 = com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor.class
            r1 = 1
            if (r9 != r0) goto L_0x000a
            return r1
        L_0x000a:
            java.lang.Class r9 = r9.getSuperclass()
            r0 = 0
            r2 = 2
            r3 = 3
            r4 = 0
            java.lang.Object r5 = r9.newInstance()     // Catch:{ NoSuchMethodException -> 0x0041, Exception -> 0x003c }
            java.lang.String r0 = "initializeViewWithModule"
            r6 = 4
            java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            r6[r4] = r7     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            java.lang.Class<android.content.Context> r7 = android.content.Context.class
            r6[r1] = r7     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            java.lang.Class<android.util.AttributeSet> r7 = android.util.AttributeSet.class
            r6[r2] = r7     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            java.lang.Class<com.taobao.android.dinamic.model.DinamicParams> r7 = com.taobao.android.dinamic.model.DinamicParams.class
            r6[r3] = r7     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            java.lang.reflect.Method r0 = r9.getDeclaredMethod(r0, r6)     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            java.lang.Object r10 = r0.invoke(r5, r10)     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            java.lang.Class r10 = r10.getClass()     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            boolean r10 = r10.isInstance(r8)     // Catch:{ NoSuchMethodException -> 0x0042, Exception -> 0x003c }
            return r10
        L_0x003c:
            r8 = move-exception
            r8.printStackTrace()
            return r4
        L_0x0041:
            r5 = r0
        L_0x0042:
            java.lang.String r10 = "initializeView"
            java.lang.Class[] r0 = new java.lang.Class[r3]     // Catch:{ Exception -> 0x0063 }
            java.lang.Class<java.lang.String> r3 = java.lang.String.class
            r0[r4] = r3     // Catch:{ Exception -> 0x0063 }
            java.lang.Class<android.content.Context> r3 = android.content.Context.class
            r0[r1] = r3     // Catch:{ Exception -> 0x0063 }
            java.lang.Class<android.util.AttributeSet> r3 = android.util.AttributeSet.class
            r0[r2] = r3     // Catch:{ Exception -> 0x0063 }
            java.lang.reflect.Method r9 = r9.getDeclaredMethod(r10, r0)     // Catch:{ Exception -> 0x0063 }
            java.lang.Object r9 = r9.invoke(r5, r11)     // Catch:{ Exception -> 0x0063 }
            java.lang.Class r9 = r9.getClass()     // Catch:{ Exception -> 0x0063 }
            boolean r8 = r9.isInstance(r8)     // Catch:{ Exception -> 0x0063 }
            return r8
        L_0x0063:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.DinamicViewCreator.rMatchViewType(android.view.View, com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor, java.lang.Object[], java.lang.Object[]):boolean");
    }

    public static View cloneView(Context context, View view, ViewResult viewResult, DinamicParams dinamicParams) {
        DinamicProperty viewProperty = DinamicViewUtils.getViewProperty(view);
        DinamicViewAdvancedConstructor viewConstructor = Dinamic.getViewConstructor(viewProperty.viewIdentify);
        if (viewConstructor == null) {
            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_NOT_FOUND, viewProperty.viewIdentify);
            return null;
        }
        View initializeViewWithModule = viewConstructor.initializeViewWithModule(viewProperty.viewIdentify, context, (AttributeSet) null, dinamicParams);
        if (initializeViewWithModule == null) {
            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_NOT_FOUND, viewProperty.viewIdentify);
            return null;
        }
        if ((initializeViewWithModule instanceof DLoopLinearLayout) && (view instanceof DLoopLinearLayout)) {
            ((DLoopLinearLayout) initializeViewWithModule).setTemplateViews(((DLoopLinearLayout) view).cloneTemplateViews());
        }
        viewConstructor.applyDefaultProperty(initializeViewWithModule);
        initializeViewWithModule.setTag(DinamicTagKey.PROPERTY_KEY, viewProperty);
        if (!viewProperty.dinamicProperty.isEmpty() || !viewProperty.eventProperty.isEmpty()) {
            viewResult.getBindDataList().add(initializeViewWithModule);
        }
        ArrayList arrayList = new ArrayList(20);
        arrayList.addAll(viewProperty.fixedProperty.keySet());
        viewConstructor.bindDataImpl(initializeViewWithModule, viewProperty.fixedProperty, arrayList, dinamicParams);
        return initializeViewWithModule;
    }
}
