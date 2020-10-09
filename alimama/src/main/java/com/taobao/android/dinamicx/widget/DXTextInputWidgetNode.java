package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.expression.event.DXTextInputEvent;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXTextInputWidgetNode extends DXWidgetNode implements IDXBuilderWidgetNode {
    public static final int DEFAULT_TEXT_COLOR = -16777216;
    public static int DEFAULT_TEXT_SIZE = 0;
    static final int INPUT_TYPE_DIGIT = 1;
    static final int INPUT_TYPE_NORMAL = 0;
    static final int INPUT_TYPE_PHONE = 2;
    int keyboard;
    int maxLength;
    String placeHolder;
    int placeHolderColor;
    CharSequence text;
    int textColor;
    int textGravity;
    float textSize;

    public class DXTextGravity {
        public static final int CENTER = 1;
        public static final int LEFT = 0;
        public static final int RIGHT = 2;

        public DXTextGravity() {
        }
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXTextInputWidgetNode();
        }
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXTextInputWidgetNode();
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        EditText editText = new EditText(context);
        editText.setLines(1);
        editText.setSingleLine();
        editText.setImeOptions(6);
        editText.setEllipsize((TextUtils.TruncateAt) null);
        return editText;
    }

    public DXTextInputWidgetNode() {
        this.textColor = -16777216;
        this.textGravity = 0;
        this.placeHolderColor = -7829368;
        this.text = "";
        this.textColor = -16777216;
        if (DEFAULT_TEXT_SIZE == 0 && DinamicXEngine.getApplicationContext() != null) {
            DEFAULT_TEXT_SIZE = DXScreenTool.dip2px(DinamicXEngine.getApplicationContext(), 12.0f);
        }
        this.textSize = (float) DEFAULT_TEXT_SIZE;
        this.textGravity = 0;
        this.accessibility = 1;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int mode = DXWidgetNode.DXMeasureSpec.getMode(i);
        int mode2 = DXWidgetNode.DXMeasureSpec.getMode(i2);
        boolean z = true;
        int i3 = 0;
        boolean z2 = mode == 1073741824;
        if (mode2 != 1073741824) {
            z = false;
        }
        int size = z2 ? DXWidgetNode.DXMeasureSpec.getSize(i) : 0;
        if (z) {
            i3 = DXWidgetNode.DXMeasureSpec.getSize(i2);
        }
        setMeasuredDimension(size, i3);
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXTextInputWidgetNode) {
            DXTextInputWidgetNode dXTextInputWidgetNode = (DXTextInputWidgetNode) dXWidgetNode;
            this.text = dXTextInputWidgetNode.text;
            this.textColor = dXTextInputWidgetNode.textColor;
            this.textSize = dXTextInputWidgetNode.textSize;
            this.textGravity = dXTextInputWidgetNode.textGravity;
            this.keyboard = dXTextInputWidgetNode.keyboard;
            this.placeHolder = dXTextInputWidgetNode.placeHolder;
            this.maxLength = dXTextInputWidgetNode.maxLength;
            this.placeHolderColor = dXTextInputWidgetNode.placeHolderColor;
        }
    }

    public int getDefaultValueForIntAttr(long j) {
        if (j == 5737767606580872653L) {
            return -16777216;
        }
        if (j == 6751005219504497256L) {
            return DEFAULT_TEXT_SIZE;
        }
        return super.getDefaultValueForIntAttr(j);
    }

    public void onSetIntAttribute(long j, int i) {
        if (5737767606580872653L == j) {
            this.textColor = i;
        } else if (-1564827143683948874L == j) {
            this.textGravity = i;
        } else if (DXHashConstant.DX_TEXTINPUT_MAXLENGTH == j) {
            this.maxLength = i;
        } else if (DXHashConstant.DX_TEXTINPUT_PLACEHOLDERCOLOR == j) {
            this.placeHolderColor = i;
        } else if (6751005219504497256L == j) {
            this.textSize = (float) i;
        } else if (DXHashConstant.DX_TEXTINPUT_KEYBOARD == j) {
            this.keyboard = i;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (38178040921L == j) {
            this.text = str;
        } else if (5980555813819279758L == j) {
            this.placeHolder = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }

    public void setBackground(View view) {
        super.setBackground(view);
        if (!this.needSetBackground) {
            view.setBackgroundColor(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        if (view != null && (view instanceof EditText)) {
            final EditText editText = (EditText) view;
            editText.setHint(this.placeHolder);
            editText.setHintTextColor(tryFetchDarkModeColor("placeholderColor", 0, this.placeHolderColor));
            editText.setText(this.text);
            editText.setTextSize(0, this.textSize);
            editText.setTextColor(tryFetchDarkModeColor("textColor", 0, this.textColor));
            setNativeTextGravity(editText, this.textGravity);
            setKeyBoardType(editText, this.keyboard);
            editText.setCursorVisible(false);
            editText.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() != 1) {
                        return false;
                    }
                    editText.setCursorVisible(true);
                    return false;
                }
            });
            if (this.maxLength <= 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.MAX_VALUE)});
                return;
            }
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.maxLength)});
        }
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        if (j == 5288679823228297259L) {
            InputTextWatcher inputTextWatcher = (InputTextWatcher) view.getTag(DinamicTagKey.TEXT_WATCHER);
            if (inputTextWatcher != null) {
                ((EditText) view).removeTextChangedListener(inputTextWatcher);
            }
            InputTextWatcher inputTextWatcher2 = new InputTextWatcher(this, view);
            view.setTag(DinamicTagKey.TEXT_WATCHER, inputTextWatcher2);
            ((EditText) view).addTextChangedListener(inputTextWatcher2);
            return;
        }
        super.onBindEvent(context, view, j);
    }

    private void setKeyBoardType(EditText editText, int i) {
        switch (i) {
            case 0:
                editText.setInputType(1);
                return;
            case 1:
                editText.setInputType(2);
                return;
            case 2:
                editText.setInputType(3);
                return;
            default:
                editText.setInputType(1);
                return;
        }
    }

    private void setNativeTextGravity(EditText editText, int i) {
        if (i == 0) {
            editText.setGravity(19);
        } else if (i == 1) {
            editText.setGravity(17);
        } else if (i == 2) {
            editText.setGravity(21);
        } else {
            editText.setGravity(16);
        }
    }

    public CharSequence getText() {
        return this.text;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public int getTextGravity() {
        return this.textGravity;
    }

    public int getKeyboard() {
        return this.keyboard;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public String getPlaceholder() {
        return this.placeHolder;
    }

    public int getPlaceholderColor() {
        return this.placeHolderColor;
    }

    public static class InputTextWatcher implements TextWatcher {
        DXTextInputEvent dxTextInputEvent = new DXTextInputEvent(5288679823228297259L);
        private DXTextInputWidgetNode textInputWidgetNode;
        private View view;

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public InputTextWatcher(DXTextInputWidgetNode dXTextInputWidgetNode, View view2) {
            this.textInputWidgetNode = dXTextInputWidgetNode;
            this.view = view2;
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            this.dxTextInputEvent.setText(((EditText) this.view).getText());
            this.textInputWidgetNode.postEvent(this.dxTextInputEvent);
        }
    }
}
