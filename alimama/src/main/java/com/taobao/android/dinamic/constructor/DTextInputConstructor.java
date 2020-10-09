package com.taobao.android.dinamic.constructor;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.event.InputEventHandlerWorker;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DAttrUtils;
import com.taobao.android.dinamic.property.ScreenTool;
import com.taobao.weex.common.Constants;
import java.util.ArrayList;
import java.util.Map;

public class DTextInputConstructor extends DinamicViewAdvancedConstructor {
    public static final int DEFAULT_TEXT_COLOR = -16777216;
    public static final String TAG = "DTextInputConstructor";

    public View initializeView(String str, Context context, AttributeSet attributeSet) {
        return new EditText(context, attributeSet);
    }

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        super.setAttributes(view, map, arrayList, dinamicParams);
        EditText editText = (EditText) view;
        if (arrayList.contains(DAttrConstant.TV_TEXT)) {
            setText(editText, (String) map.get(DAttrConstant.TV_TEXT));
        }
        if (arrayList.contains(DAttrConstant.TV_TEXT_SIZE)) {
            setTextSize(editText, (String) map.get(DAttrConstant.TV_TEXT_SIZE));
        }
        if (arrayList.contains(DAttrConstant.TV_TEXT_COLOR)) {
            setTextColor(editText, (String) map.get(DAttrConstant.TV_TEXT_COLOR));
        }
        if (arrayList.contains(DAttrConstant.TV_TEXT_GRAVITY) || arrayList.contains(DAttrConstant.TV_TEXT_ALIGNMENT)) {
            setTextGravity(editText, (String) map.get(DAttrConstant.TV_TEXT_GRAVITY), (String) map.get(DAttrConstant.TV_TEXT_ALIGNMENT));
        }
        if (arrayList.contains(DAttrConstant.ET_PLACE_HOLDER)) {
            editText.setHint((String) map.get(DAttrConstant.ET_PLACE_HOLDER));
        }
        if (arrayList.contains(DAttrConstant.ET_PLACE_HOLDER_COLOR)) {
            setHintColor(editText, (String) map.get(DAttrConstant.ET_PLACE_HOLDER_COLOR));
        }
        if (arrayList.contains(DAttrConstant.ET_KEYBOARD)) {
            setKeyBoardType(editText, (String) map.get(DAttrConstant.ET_KEYBOARD));
        }
        if (arrayList.contains(DAttrConstant.ET_MAX_LENGTH)) {
            setMaxLength(editText, (String) map.get(DAttrConstant.ET_MAX_LENGTH));
        }
        if (arrayList.contains(DAttrConstant.INPUT_FOCUSABLE)) {
            boolean parseBoolean = NumberUtil.parseBoolean((String) map.get(DAttrConstant.INPUT_FOCUSABLE));
            editText.setFocusable(parseBoolean);
            editText.setFocusableInTouchMode(parseBoolean);
        }
    }

    private void setMaxLength(EditText editText, String str) {
        if (TextUtils.isEmpty(str)) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.MAX_VALUE)});
            return;
        }
        Integer valueOf = Integer.valueOf(str);
        if (valueOf.intValue() <= 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.MAX_VALUE)});
            return;
        }
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(valueOf.intValue())});
    }

    private void setKeyBoardType(EditText editText, String str) {
        if (TextUtils.isEmpty(str)) {
            editText.setInputType(1);
            return;
        }
        switch (Integer.valueOf(str).intValue()) {
            case 0:
                editText.setInputType(1);
                return;
            case 1:
                editText.setInputType(2);
                return;
            case 2:
                editText.setInputType(3);
                return;
            case 3:
                editText.setInputType(128);
                return;
            default:
                editText.setInputType(1);
                return;
        }
    }

    private void setHintColor(EditText editText, String str) {
        editText.setHintTextColor(DAttrUtils.parseColor(str, -16777216));
    }

    private void setBackground(EditText editText, String str) {
        editText.setBackgroundColor(DAttrUtils.parseColor(str, -16777216));
    }

    public void setText(EditText editText, String str) {
        editText.setText(str);
    }

    public void applyDefaultProperty(View view, Map<String, Object> map, DinamicParams dinamicParams) {
        super.applyDefaultProperty(view, map, dinamicParams);
        EditText editText = (EditText) view;
        editText.setLines(1);
        editText.setSingleLine();
        editText.setImeOptions(6);
        if (!map.containsKey(DAttrConstant.TV_TEXT_SIZE)) {
            editText.setTextSize(1, 12.0f);
        }
        if (!map.containsKey(DAttrConstant.TV_TEXT_COLOR)) {
            editText.setTextColor(-16777216);
        }
        if (!map.containsKey(DAttrConstant.TV_LINE_BREAK_MODE)) {
            editText.setEllipsize((TextUtils.TruncateAt) null);
        }
        if (!map.containsKey(DAttrConstant.TV_TEXT_GRAVITY) && !map.containsKey(DAttrConstant.TV_TEXT_ALIGNMENT)) {
            editText.setGravity(16);
        }
    }

    public void setTextSize(EditText editText, String str) {
        int px = ScreenTool.getPx(editText.getContext(), str, -1);
        if (px == -1) {
            editText.setTextSize(1, 12.0f);
        } else {
            editText.setTextSize(0, (float) px);
        }
    }

    public void setTextStyle(EditText editText, String str) {
        if (TextUtils.isEmpty(str)) {
            editText.setTypeface(Typeface.defaultFromStyle(0));
            return;
        }
        switch (Integer.valueOf(str).intValue()) {
            case 0:
                editText.setTypeface(Typeface.defaultFromStyle(0));
                return;
            case 1:
                editText.setTypeface(Typeface.defaultFromStyle(1));
                return;
            case 2:
                editText.setTypeface(Typeface.defaultFromStyle(2));
                return;
            case 3:
                editText.setTypeface(Typeface.defaultFromStyle(3));
                return;
            default:
                return;
        }
    }

    public void setTextTheme(EditText editText, String str, String str2) {
        if (str == null) {
            setTextStyle(editText, str2);
        } else if ("normal".equals(str)) {
            editText.setTypeface(Typeface.defaultFromStyle(0));
        } else if (Constants.Value.BOLD.equals(str)) {
            editText.setTypeface(Typeface.defaultFromStyle(1));
        } else if (Constants.Value.ITALIC.equals(str)) {
            editText.setTypeface(Typeface.defaultFromStyle(2));
        } else {
            editText.setTypeface(Typeface.defaultFromStyle(0));
        }
    }

    public void setTextColor(EditText editText, String str) {
        editText.setTextColor(DAttrUtils.parseColor(str, -16777216));
    }

    public void setTextLineBreakMode(EditText editText, String str) {
        switch (Integer.valueOf(str).intValue()) {
            case 0:
                editText.setEllipsize((TextUtils.TruncateAt) null);
                return;
            case 1:
                editText.setEllipsize(TextUtils.TruncateAt.START);
                return;
            case 2:
                editText.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                return;
            case 3:
                editText.setEllipsize(TextUtils.TruncateAt.END);
                return;
            default:
                return;
        }
    }

    public void setTextGravity(EditText editText, String str, String str2) {
        if (str == null) {
            setTextAlignment(editText, str2);
        } else if ("left".equals(str)) {
            editText.setGravity(19);
        } else if ("center".equals(str)) {
            editText.setGravity(17);
        } else if ("right".equals(str)) {
            editText.setGravity(21);
        } else {
            editText.setGravity(16);
        }
    }

    public void setTextAlignment(EditText editText, String str) {
        switch (Integer.valueOf(str).intValue()) {
            case 0:
                editText.setGravity(19);
                return;
            case 1:
                editText.setGravity(17);
                return;
            case 2:
                editText.setGravity(21);
                return;
            default:
                return;
        }
    }

    public void setMaxLines(EditText editText, String str) {
        Integer valueOf = Integer.valueOf(str);
        if (valueOf.intValue() <= 0) {
            editText.setMaxLines(Integer.MAX_VALUE);
        } else if (valueOf.intValue() == 1) {
            editText.setMaxLines(1);
        } else {
            editText.setMaxLines(valueOf.intValue());
        }
    }

    public void setMaxWidth(EditText editText, String str) {
        int px = ScreenTool.getPx(editText.getContext(), str, -1);
        if (px != -1) {
            editText.setMaxWidth(px);
        }
    }

    public void setDeleteLine(EditText editText, String str) {
        if (TextUtils.equals("single", str)) {
            editText.getPaint().setFlags(16);
        }
    }

    public void setEvents(View view, DinamicParams dinamicParams) {
        new InputEventHandlerWorker().bindEventHandler(view, dinamicParams);
    }
}
