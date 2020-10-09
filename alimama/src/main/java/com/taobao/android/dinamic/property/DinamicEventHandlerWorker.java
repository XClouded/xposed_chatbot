package com.taobao.android.dinamic.property;

import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.DinamicViewUtils;
import com.taobao.android.dinamic.dinamic.DinamicEventHandler;
import com.taobao.android.dinamic.expression.DinamicExpression;
import com.taobao.android.dinamic.expressionv2.ExpressionProcessor;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.view.DinamicError;
import java.util.Map;

public class DinamicEventHandlerWorker {
    public void bindEventHandler(View view, DinamicParams dinamicParams) {
        DinamicProperty dinamicProperty = (DinamicProperty) view.getTag(DinamicTagKey.PROPERTY_KEY);
        if (dinamicProperty != null) {
            Map<String, String> map = dinamicProperty.eventProperty;
            if (!map.isEmpty()) {
                for (String next : map.keySet()) {
                    String str = map.get(next);
                    if (str.startsWith(DinamicConstant.DINAMIC_PREFIX_AT)) {
                        bindEvent(view, dinamicParams, dinamicProperty, next, str);
                    } else {
                        bindEventOld(view, dinamicParams, dinamicProperty, next, str);
                    }
                }
            }
        }
    }

    private void bindEventOld(View view, DinamicParams dinamicParams, DinamicProperty dinamicProperty, String str, String str2) {
        View view2 = view;
        DinamicProperty dinamicProperty2 = dinamicProperty;
        String str3 = str;
        Pair<String, String> eventInfo = DinamicViewUtils.getEventInfo(str2);
        if (eventInfo == null) {
            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_NOT_FOUND, dinamicProperty2.viewIdentify);
            if (Dinamic.isDebugable()) {
                DinamicLog.i(Dinamic.TAG, String.format("事件表达式 %s=%s 解析出错", new Object[]{str3, str2}));
                return;
            }
            return;
        }
        DinamicEventHandler eventHandler = Dinamic.getEventHandler((String) eventInfo.first);
        if (eventHandler == null) {
            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_NOT_FOUND, dinamicProperty2.viewIdentify);
            if (Dinamic.isDebugable()) {
                DinamicLog.i(Dinamic.TAG, String.format("%s=%s，没有找到%s对应的DinamicEventHandler", new Object[]{str3, str2, eventInfo.first}));
                return;
            }
            return;
        }
        Object value = DinamicExpression.getValue((String) eventInfo.second, dinamicProperty2.viewIdentify, dinamicParams);
        if (TextUtils.equals(str3, DAttrConstant.VIEW_EVENT_TAP)) {
            final DinamicEventHandler dinamicEventHandler = eventHandler;
            final DinamicParams dinamicParams2 = dinamicParams;
            final Object obj = value;
            final DinamicProperty dinamicProperty3 = dinamicProperty;
            view2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    long nanoTime = System.nanoTime();
                    try {
                        dinamicEventHandler.handleEvent(view, dinamicParams2.getModule(), obj, dinamicParams2.getOriginalData(), dinamicParams2.getDinamicContext());
                        DinamicLog.logHandleEvent(dinamicParams2.getModule(), dinamicProperty3.viewIdentify, System.nanoTime() - nanoTime);
                    } catch (Throwable th) {
                        dinamicParams2.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_EXCEPTION, dinamicProperty3.viewIdentify);
                        DinamicLog.e("DinamicEventHandler", th, "handle onclick event failed, handler=", dinamicEventHandler.getClass().getName());
                        DinamicLog.logHandleEvent(dinamicParams2.getModule(), dinamicProperty3.viewIdentify, System.nanoTime() - nanoTime);
                    }
                }
            });
            try {
                eventHandler.prepareBindEvent(view2, value, dinamicParams.getOriginalData());
            } catch (Throwable th) {
                dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_EXCEPTION, dinamicProperty2.viewIdentify);
                DinamicLog.e("DinamicEventHandler", th, "handler prepareBindEvent failed, handler=", eventHandler.getClass().getName());
            }
        } else if (TextUtils.equals(str3, DAttrConstant.VIEW_EVENT_LONGTAP)) {
            final DinamicEventHandler dinamicEventHandler2 = eventHandler;
            final DinamicParams dinamicParams3 = dinamicParams;
            final Object obj2 = value;
            final DinamicProperty dinamicProperty4 = dinamicProperty;
            view2.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    long nanoTime = System.nanoTime();
                    try {
                        dinamicEventHandler2.handleEvent(view, dinamicParams3.getModule(), obj2, dinamicParams3.getOriginalData(), dinamicParams3.getDinamicContext());
                        DinamicLog.logHandleEvent(dinamicParams3.getModule(), dinamicProperty4.viewIdentify, System.nanoTime() - nanoTime);
                    } catch (Throwable th) {
                        dinamicParams3.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_EXCEPTION, dinamicProperty4.viewIdentify);
                        DinamicLog.e("DinamicEventHandler", th, "handle onlongclick event failed, handler=", dinamicEventHandler2.getClass().getName());
                        DinamicLog.logHandleEvent(dinamicParams3.getModule(), dinamicProperty4.viewIdentify, System.nanoTime() - nanoTime);
                    }
                    return true;
                }
            });
            try {
                eventHandler.prepareBindEvent(view2, value, dinamicParams.getOriginalData());
            } catch (Throwable th2) {
                dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_EXCEPTION, dinamicProperty2.viewIdentify);
                DinamicLog.e("DinamicEventHandler", th2, "handler prepareBindEvent failed, handler=", eventHandler.getClass().getName());
            }
        }
    }

    public void bindEvent(View view, DinamicParams dinamicParams, DinamicProperty dinamicProperty, String str, String str2) {
        if (TextUtils.equals(str, DAttrConstant.VIEW_EVENT_TAP)) {
            view.setOnClickListener(new DinamicEventListener(dinamicParams, str2, dinamicProperty));
            ExpressionProcessor.handlePreEvent(view, str2, dinamicParams, dinamicProperty);
        } else if (TextUtils.equals(str, DAttrConstant.VIEW_EVENT_LONGTAP)) {
            view.setOnLongClickListener(new DinamicEventListener(dinamicParams, str2, dinamicProperty));
            ExpressionProcessor.handlePreEvent(view, str2, dinamicParams, dinamicProperty);
        }
    }

    public static void handleEvent(View view, DinamicParams dinamicParams, DinamicProperty dinamicProperty, String str) {
        long nanoTime = System.nanoTime();
        try {
            dinamicParams.setCurrentData(view.getTag(DinamicTagKey.SUBDATA));
            ExpressionProcessor.handleEvent(view, str, dinamicParams);
            DinamicLog.logHandleEvent(dinamicParams.getModule(), dinamicProperty.viewIdentify, System.nanoTime() - nanoTime);
        } catch (Throwable unused) {
            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_EXCEPTION, dinamicProperty.viewIdentify);
            DinamicLog.logHandleEvent(dinamicParams.getModule(), dinamicProperty.viewIdentify, System.nanoTime() - nanoTime);
        }
    }
}
