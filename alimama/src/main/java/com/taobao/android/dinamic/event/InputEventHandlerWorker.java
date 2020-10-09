package com.taobao.android.dinamic.event;

import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DinamicEventHandlerWorker;
import com.taobao.android.dinamic.property.DinamicProperty;
import java.util.ArrayList;
import java.util.Map;

public class InputEventHandlerWorker extends DinamicEventHandlerWorker {
    public void bindEventHandler(View view, DinamicParams dinamicParams) {
        super.bindEventHandler(view, dinamicParams);
        bindEvent(view, dinamicParams);
    }

    public void bindEvent(View view, DinamicParams dinamicParams) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.setFocusable(true);
            viewGroup.setFocusableInTouchMode(true);
        }
        final DinamicProperty dinamicProperty = (DinamicProperty) view.getTag(DinamicTagKey.PROPERTY_KEY);
        if (dinamicProperty != null) {
            final Map<String, String> map = dinamicProperty.eventProperty;
            if (map.isEmpty()) {
                return;
            }
            if (view.isFocusable()) {
                if (map.containsKey(DAttrConstant.VIEW_EVENT_CHANGE)) {
                    InputTextWatcher inputTextWatcher = (InputTextWatcher) view.getTag(DinamicTagKey.TEXT_WATCHER);
                    if (inputTextWatcher != null) {
                        ((EditText) view).removeTextChangedListener(inputTextWatcher);
                    }
                    InputTextWatcher inputTextWatcher2 = new InputTextWatcher(view, dinamicProperty);
                    inputTextWatcher2.setDinamicParams(dinamicParams);
                    view.setTag(DinamicTagKey.TEXT_WATCHER, inputTextWatcher2);
                    ((EditText) view).addTextChangedListener(inputTextWatcher2);
                }
                if (map.containsKey(DAttrConstant.VIEW_EVENT_FINISH) || map.containsKey(DAttrConstant.VIEW_EVENT_BEGIN)) {
                    final View view2 = view;
                    final DinamicParams dinamicParams2 = dinamicParams;
                    view.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getActionMasked() == 1 && ((KeyboardListener) view2.getTag(DinamicTagKey.KEY_BOARD_LISTENER)) == null) {
                                ((InputMethodManager) view2.getContext().getSystemService("input_method")).showSoftInput(view2, 0);
                                if (map.containsKey(DAttrConstant.VIEW_EVENT_BEGIN)) {
                                    String str = (String) map.get(DAttrConstant.VIEW_EVENT_BEGIN);
                                    if (!TextUtils.isEmpty(str)) {
                                        ArrayList arrayList = new ArrayList(5);
                                        arrayList.add(((EditText) view2).getText());
                                        view2.setTag(DinamicTagKey.VIEW_PARAMS, arrayList);
                                        DinamicEventHandlerWorker.handleEvent(view2, dinamicParams2, dinamicProperty, str);
                                    }
                                }
                                KeyboardListener keyboardListener = new KeyboardListener(view2, dinamicProperty);
                                keyboardListener.setDinamicParams(dinamicParams2);
                                view2.getViewTreeObserver().addOnGlobalLayoutListener(keyboardListener);
                                view2.setTag(DinamicTagKey.KEY_BOARD_LISTENER, keyboardListener);
                            }
                            return false;
                        }
                    });
                    return;
                }
                return;
            }
            view.setOnTouchListener((View.OnTouchListener) null);
            InputTextWatcher inputTextWatcher3 = (InputTextWatcher) view.getTag(DinamicTagKey.TEXT_WATCHER);
            if (inputTextWatcher3 != null) {
                ((EditText) view).removeTextChangedListener(inputTextWatcher3);
            }
            view.setOnFocusChangeListener((View.OnFocusChangeListener) null);
        }
    }

    public class KeyboardListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private DinamicParams dinamicParams;
        /* access modifiers changed from: private */
        public boolean isRemoved;
        private String onFinishEventExp;
        private DinamicProperty property;
        private View view;

        private void onShowKeyBoard() {
        }

        public KeyboardListener(View view2, DinamicProperty dinamicProperty) {
            this.property = dinamicProperty;
            this.view = view2;
            Map<String, String> map = dinamicProperty.eventProperty;
            if (!map.isEmpty()) {
                this.onFinishEventExp = map.get(DAttrConstant.VIEW_EVENT_FINISH);
            }
        }

        public void setDinamicParams(DinamicParams dinamicParams2) {
            this.dinamicParams = dinamicParams2;
            this.view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    if (!z && !KeyboardListener.this.isRemoved) {
                        KeyboardListener.this.onHideKeyBoard();
                    }
                }
            });
        }

        public void onGlobalLayout() {
            Rect rect = new Rect();
            View rootView = this.view.getRootView();
            rootView.getWindowVisibleDisplayFrame(rect);
            int height = rootView.getHeight();
            if (height - rect.bottom > height / 3) {
                onShowKeyBoard();
            } else {
                onHideKeyBoard();
            }
        }

        /* access modifiers changed from: private */
        public void onHideKeyBoard() {
            if (!TextUtils.isEmpty(this.onFinishEventExp)) {
                ArrayList arrayList = new ArrayList(5);
                arrayList.add(((EditText) this.view).getText());
                this.view.setTag(DinamicTagKey.VIEW_PARAMS, arrayList);
                DinamicEventHandlerWorker.handleEvent(this.view, this.dinamicParams, this.property, this.onFinishEventExp);
            }
            if (Build.VERSION.SDK_INT >= 16) {
                this.view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                this.view.setTag(DinamicTagKey.KEY_BOARD_LISTENER, (Object) null);
            } else {
                this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                this.view.setTag(DinamicTagKey.KEY_BOARD_LISTENER, (Object) null);
            }
            this.isRemoved = true;
        }
    }

    public class InputTextWatcher implements TextWatcher {
        private DinamicParams dinamicParams;
        private String onBeginEventExp;
        private String onChangeEventExp;
        private DinamicProperty property;
        private View view;

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public InputTextWatcher(View view2, DinamicProperty dinamicProperty) {
            this.property = dinamicProperty;
            this.view = view2;
            Map<String, String> map = dinamicProperty.eventProperty;
            if (!map.isEmpty()) {
                this.onChangeEventExp = map.get(DAttrConstant.VIEW_EVENT_CHANGE);
                this.onBeginEventExp = map.get(DAttrConstant.VIEW_EVENT_BEGIN);
            }
        }

        public void setDinamicParams(DinamicParams dinamicParams2) {
            this.dinamicParams = dinamicParams2;
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!TextUtils.isEmpty(this.onChangeEventExp)) {
                ArrayList arrayList = new ArrayList(5);
                arrayList.add(((EditText) this.view).getText());
                this.view.setTag(DinamicTagKey.VIEW_PARAMS, arrayList);
                DinamicEventHandlerWorker.handleEvent(this.view, this.dinamicParams, this.property, this.onChangeEventExp);
            }
        }
    }
}
