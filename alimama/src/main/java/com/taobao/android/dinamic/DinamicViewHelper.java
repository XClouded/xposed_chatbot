package com.taobao.android.dinamic;

import android.text.TextUtils;
import com.taobao.android.dinamic.constructor.DCheckBoxConstructor;
import com.taobao.android.dinamic.constructor.DCountDownTimerConstructor;
import com.taobao.android.dinamic.constructor.DFrameLayoutConstructor;
import com.taobao.android.dinamic.constructor.DHorizontalScrollLayoutConstructor;
import com.taobao.android.dinamic.constructor.DImageViewConstructor;
import com.taobao.android.dinamic.constructor.DLinearLayoutConstructor;
import com.taobao.android.dinamic.constructor.DLoopLinearLayoutConstructor;
import com.taobao.android.dinamic.constructor.DSwitchConstructor;
import com.taobao.android.dinamic.constructor.DTextInputConstructor;
import com.taobao.android.dinamic.constructor.DTextViewConstructor;
import com.taobao.android.dinamic.dinamic.AbsDinamicEventHandler;
import com.taobao.android.dinamic.dinamic.DinamicEventHandler;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.exception.DinamicException;
import java.util.HashMap;
import java.util.Map;

class DinamicViewHelper {
    private static Map<String, AbsDinamicEventHandler> eventHandlers = new HashMap();
    private static Map<String, DinamicViewAdvancedConstructor> viewConstructors = new HashMap();

    DinamicViewHelper() {
    }

    static {
        viewConstructors.put(DinamicConstant.D_VIEW, new DinamicViewAdvancedConstructor());
        viewConstructors.put(DinamicConstant.D_TEXT_VIEW, new DTextViewConstructor());
        viewConstructors.put(DinamicConstant.D_IMAGE_VIEW, new DImageViewConstructor());
        viewConstructors.put(DinamicConstant.D_FRAME_LAYOUT, new DFrameLayoutConstructor());
        viewConstructors.put(DinamicConstant.D_LINEAR_LAYOUT, new DLinearLayoutConstructor());
        viewConstructors.put(DinamicConstant.D_HORIZONTAL_SCROLL_LAYOUT, new DHorizontalScrollLayoutConstructor());
        viewConstructors.put(DinamicConstant.D_COUNT_DOWN_TIMER_VIEW, new DCountDownTimerConstructor());
        viewConstructors.put(DinamicConstant.D_LOOP_LINEAR_LAYOUT, new DLoopLinearLayoutConstructor());
        viewConstructors.put(DinamicConstant.D_TEXT_INPUT, new DTextInputConstructor());
        viewConstructors.put("DCheckBox", new DCheckBoxConstructor());
        viewConstructors.put("DSwitch", new DSwitchConstructor());
    }

    static void registerViewConstructor(String str, DinamicViewAdvancedConstructor dinamicViewAdvancedConstructor) throws DinamicException {
        if (TextUtils.isEmpty(str) || dinamicViewAdvancedConstructor == null) {
            throw new DinamicException("viewIdentify or assistant is null");
        } else if (viewConstructors.get(str) == null) {
            viewConstructors.put(str, dinamicViewAdvancedConstructor);
        } else {
            throw new DinamicException("assistant already registed by current identify:" + str);
        }
    }

    static void registerReplaceViewConstructor(String str, DinamicViewAdvancedConstructor dinamicViewAdvancedConstructor) throws DinamicException {
        if (TextUtils.isEmpty(str) || dinamicViewAdvancedConstructor == null) {
            throw new DinamicException("viewIdentify or assistant is null");
        }
        viewConstructors.put(str, dinamicViewAdvancedConstructor);
    }

    static void registerEventHandler(String str, AbsDinamicEventHandler absDinamicEventHandler) throws DinamicException {
        if (TextUtils.isEmpty(str) || absDinamicEventHandler == null) {
            throw new DinamicException("registerEventHandler failed, eventIdentify or handler is null");
        } else if (eventHandlers.get(str) == null) {
            eventHandlers.put(str, absDinamicEventHandler);
        } else {
            throw new DinamicException("registerEventHandler failed, eventHander already register by current identify:" + str);
        }
    }

    static void registerReplaceEventHandler(String str, AbsDinamicEventHandler absDinamicEventHandler) throws DinamicException {
        if (TextUtils.isEmpty(str) || absDinamicEventHandler == null) {
            throw new DinamicException("registerEventHandler failed, eventIdentify or handler is null");
        }
        eventHandlers.put(str, absDinamicEventHandler);
    }

    static DinamicViewAdvancedConstructor getViewConstructor(String str) {
        return viewConstructors.get(str);
    }

    static DinamicEventHandler getEventHandler(String str) {
        return eventHandlers.get(str);
    }
}
