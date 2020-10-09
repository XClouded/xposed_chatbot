package com.taobao.android.dinamic.constructor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.R;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DinamicEventHandlerWorker;
import com.taobao.android.dinamic.property.DinamicProperty;
import com.taobao.android.dinamic.property.ScreenTool;
import java.util.ArrayList;
import java.util.Map;

public class DSwitchConstructor extends DinamicViewAdvancedConstructor {
    private static final int CHECKED_COLOR = -45056;
    private static final String D_HEIGHT = "dHeight";
    private static final String D_OFF_COLOR = "dOffColor";
    private static final String D_ON_COLOR = "dOnColor";
    private static final String D_SWITCH_ON = "dSwitchOn";
    private static final String D_WIDTH = "dWidth";
    private static final String STRING_CHECKED_COLOR = "#ffff5000";
    private static final String STRING_UNCHECKED_COLOR = "#ffe5e5e5";
    private static final int UNCHECKED_COLOR = -1710619;
    private static final String VIEW_EVENT_ON_CHANGE = "onChange";
    public static final String VIEW_TAG = "DSwitch";

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        super.setAttributes(view, map, arrayList, dinamicParams);
        SwitchCompat switchCompat = view instanceof SwitchCompat ? (SwitchCompat) view : null;
        if (arrayList.contains("dHeight") || arrayList.contains(D_ON_COLOR) || arrayList.contains(D_OFF_COLOR)) {
            String str = STRING_CHECKED_COLOR;
            String str2 = STRING_UNCHECKED_COLOR;
            Object obj = map.get("dHeight");
            Object obj2 = map.get(D_ON_COLOR);
            Object obj3 = map.get(D_OFF_COLOR);
            if (obj2 instanceof String) {
                str = (String) obj2;
            }
            if (obj3 instanceof String) {
                str2 = (String) obj3;
            }
            int px = ScreenTool.getPx(view.getContext(), obj, -1);
            if (px != -1) {
                updateInternalStatus(switchCompat, getTrackDrawable(view.getContext(), str, CHECKED_COLOR, px), getTrackDrawable(view.getContext(), str2, UNCHECKED_COLOR, px), getThumbDrawable(view.getContext(), px));
            }
        }
        if (arrayList.contains("dWidth")) {
            Object obj4 = map.get("dWidth");
            Object obj5 = map.get("dHeight");
            int px2 = ScreenTool.getPx(view.getContext(), obj4, -1);
            int px3 = ScreenTool.getPx(view.getContext(), obj5, -1);
            if (!(px2 == -1 || px3 == -1 || px2 < px3 * 2)) {
                setSwitchMinWidth(switchCompat, px2);
            }
        }
        if (arrayList.contains(D_SWITCH_ON)) {
            setChecked(switchCompat, NumberUtil.parseBoolean((String) map.get(D_SWITCH_ON)));
        }
        if (arrayList.contains(DAttrConstant.VIEW_ENABLED)) {
            String str3 = (String) map.get(DAttrConstant.VIEW_ENABLED);
            if (!TextUtils.isEmpty(str3)) {
                setEnable(view, NumberUtil.parseBoolean(str3));
            } else {
                setEnable(view, true);
            }
        }
    }

    private void setEnable(View view, boolean z) {
        view.setEnabled(z);
    }

    public void setEvents(View view, DinamicParams dinamicParams) {
        new DSwitchEventHandlerWorker().bindEventHandler(view, dinamicParams);
    }

    public void setChecked(SwitchCompat switchCompat, boolean z) {
        if (switchCompat != null) {
            switchCompat.setTag(R.id.change_with_attribute, "true");
            switchCompat.setChecked(z);
            switchCompat.setTag(R.id.change_with_attribute, "false");
        }
    }

    public void setSwitchMinWidth(SwitchCompat switchCompat, int i) {
        if (switchCompat != null) {
            switchCompat.setSwitchMinWidth(i);
        }
    }

    private GradientDrawable getTrackDrawable(Context context, String str, int i, int i2) {
        return DrawableTools.getShape(0, 16777215, i2 / 2, ColorTools.parseColor(str, i), i2, i2);
    }

    private GradientDrawable getThumbDrawable(Context context, int i) {
        return DrawableTools.getShape((int) TypedValue.applyDimension(1, 2.0f, context.getResources().getDisplayMetrics()), 16777215, i / 2, -1, i, i);
    }

    private StateListDrawable getSelector(Drawable drawable, Drawable drawable2) {
        return DrawableTools.getSelector(drawable, drawable2, DrawableTools.STATE_CHECKED);
    }

    public View initializeView(String str, Context context, AttributeSet attributeSet) {
        SwitchCompat switchCompat = new SwitchCompat(context, attributeSet);
        switchCompat.setClickable(true);
        switchCompat.setTextOn("");
        switchCompat.setTextOff("");
        switchCompat.setShowText(false);
        switchCompat.setThumbTextPadding(0);
        switchCompat.setSplitTrack(false);
        return switchCompat;
    }

    private void updateInternalStatus(SwitchCompat switchCompat, Drawable drawable, Drawable drawable2, Drawable drawable3) {
        if (switchCompat != null) {
            switchCompat.setTrackDrawable(getSelector(drawable, drawable2));
            switchCompat.setThumbDrawable(drawable3);
        }
    }

    private static class DSwitchEventHandlerWorker extends DinamicEventHandlerWorker {
        private DSwitchEventHandlerWorker() {
        }

        public void bindEventHandler(View view, DinamicParams dinamicParams) {
            bindSelfEvent(view, dinamicParams);
        }

        public void bindSelfEvent(View view, DinamicParams dinamicParams) {
            DinamicProperty dinamicProperty = (DinamicProperty) view.getTag(DinamicTagKey.PROPERTY_KEY);
            if (dinamicProperty != null) {
                Map<String, String> map = dinamicProperty.eventProperty;
                if (!map.isEmpty() && map.containsKey("onChange") && (view instanceof SwitchCompat)) {
                    ((SwitchCompat) view).setOnCheckedChangeListener(new OnChangeListener(this, dinamicParams, dinamicProperty, view));
                }
            }
        }
    }

    private static class OnChangeListener implements CompoundButton.OnCheckedChangeListener {
        private DinamicParams mDinamicParams;
        private DSwitchEventHandlerWorker mHandler;
        private String mOnChangeExpression;
        private DinamicProperty mProperty;
        private View mView;

        public OnChangeListener(DSwitchEventHandlerWorker dSwitchEventHandlerWorker, DinamicParams dinamicParams, DinamicProperty dinamicProperty, View view) {
            this.mHandler = dSwitchEventHandlerWorker;
            this.mDinamicParams = dinamicParams;
            this.mProperty = dinamicProperty;
            this.mView = view;
            Map<String, String> map = dinamicProperty.eventProperty;
            if (!map.isEmpty()) {
                this.mOnChangeExpression = map.get("onChange");
            }
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Object tag = compoundButton.getTag(R.id.change_with_attribute);
            if (!TextUtils.isEmpty(this.mOnChangeExpression) && !"true".equals(tag)) {
                ArrayList arrayList = new ArrayList(5);
                arrayList.add(Boolean.valueOf(compoundButton.isChecked()));
                this.mView.setTag(DinamicTagKey.VIEW_PARAMS, arrayList);
                DSwitchEventHandlerWorker dSwitchEventHandlerWorker = this.mHandler;
                DSwitchEventHandlerWorker.handleEvent(this.mView, this.mDinamicParams, this.mProperty, this.mOnChangeExpression);
            }
        }
    }
}
