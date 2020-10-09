package com.taobao.weex.ui.component;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.layout.MeasureMode;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.helper.SoftKeyboardDetector;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.WXEditText;
import com.taobao.weex.utils.TypefaceUtil;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class AbstractEditComponent extends WXComponent<WXEditText> {
    private static final int MAX_TEXT_FORMAT_REPEAT = 3;
    private boolean mAutoFocus;
    /* access modifiers changed from: private */
    public String mBeforeText = "";
    /* access modifiers changed from: private */
    public int mEditorAction = 6;
    /* access modifiers changed from: private */
    public List<TextView.OnEditorActionListener> mEditorActionListeners;
    /* access modifiers changed from: private */
    public int mFormatRepeatCount = 0;
    /* access modifiers changed from: private */
    public TextFormatter mFormatter = null;
    /* access modifiers changed from: private */
    public boolean mIgnoreNextOnInputEvent = false;
    /* access modifiers changed from: private */
    public final InputMethodManager mInputMethodManager = ((InputMethodManager) getContext().getSystemService("input_method"));
    private boolean mKeepSelectionIndex = false;
    /* access modifiers changed from: private */
    public String mLastValue = "";
    private int mLineHeight = -1;
    /* access modifiers changed from: private */
    public boolean mListeningKeyboard = false;
    /* access modifiers changed from: private */
    public String mMax = null;
    /* access modifiers changed from: private */
    public String mMin = null;
    private WXComponent.OnClickListener mOnClickListener = new WXComponent.OnClickListener() {
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002e  */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x004a  */
        /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onHostViewClick() {
            /*
                r3 = this;
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                java.lang.String r0 = r0.mType
                int r1 = r0.hashCode()
                r2 = 3076014(0x2eefae, float:4.310414E-39)
                if (r1 == r2) goto L_0x001f
                r2 = 3560141(0x3652cd, float:4.98882E-39)
                if (r1 == r2) goto L_0x0015
                goto L_0x0029
            L_0x0015:
                java.lang.String r1 = "time"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L_0x0029
                r0 = 1
                goto L_0x002a
            L_0x001f:
                java.lang.String r1 = "date"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L_0x0029
                r0 = 0
                goto L_0x002a
            L_0x0029:
                r0 = -1
            L_0x002a:
                switch(r0) {
                    case 0: goto L_0x004a;
                    case 1: goto L_0x002e;
                    default: goto L_0x002d;
                }
            L_0x002d:
                goto L_0x0071
            L_0x002e:
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                r0.hideSoftKeyboard()
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                com.taobao.weex.ui.component.WXVContainer r0 = r0.getParent()
                if (r0 == 0) goto L_0x0044
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                com.taobao.weex.ui.component.WXVContainer r0 = r0.getParent()
                r0.interceptFocus()
            L_0x0044:
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                com.taobao.weex.ui.component.helper.WXTimeInputHelper.pickTime(r0)
                goto L_0x0071
            L_0x004a:
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                r0.hideSoftKeyboard()
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                com.taobao.weex.ui.component.WXVContainer r0 = r0.getParent()
                if (r0 == 0) goto L_0x0060
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                com.taobao.weex.ui.component.WXVContainer r0 = r0.getParent()
                r0.interceptFocus()
            L_0x0060:
                com.taobao.weex.ui.component.AbstractEditComponent r0 = com.taobao.weex.ui.component.AbstractEditComponent.this
                java.lang.String r0 = r0.mMax
                com.taobao.weex.ui.component.AbstractEditComponent r1 = com.taobao.weex.ui.component.AbstractEditComponent.this
                java.lang.String r1 = r1.mMin
                com.taobao.weex.ui.component.AbstractEditComponent r2 = com.taobao.weex.ui.component.AbstractEditComponent.this
                com.taobao.weex.ui.component.helper.WXTimeInputHelper.pickDate(r0, r1, r2)
            L_0x0071:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.AbstractEditComponent.AnonymousClass3.onHostViewClick():void");
        }
    };
    private TextPaint mPaint = new TextPaint();
    /* access modifiers changed from: private */
    public String mReturnKeyType = null;
    private TextWatcher mTextChangedEventDispatcher;
    /* access modifiers changed from: private */
    public List<TextWatcher> mTextChangedListeners;
    /* access modifiers changed from: private */
    public String mType = "text";
    private SoftKeyboardDetector.Unregister mUnregister;

    private interface ReturnTypes {
        public static final String DEFAULT = "default";
        public static final String DONE = "done";
        public static final String GO = "go";
        public static final String NEXT = "next";
        public static final String SEARCH = "search";
        public static final String SEND = "send";
    }

    /* access modifiers changed from: protected */
    public int getVerticalGravity() {
        return 16;
    }

    public AbstractEditComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                if (CSSConstants.isUndefined(f) || i == MeasureMode.UNSPECIFIED) {
                    f = 0.0f;
                }
                this.mMeasureWidth = f;
                this.mMeasureHeight = AbstractEditComponent.this.getMeasureHeight();
            }

            public void layoutBefore() {
                AbstractEditComponent.this.updateStyleAndAttrs();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void layoutDirectionDidChanged(boolean z) {
        int textAlign = getTextAlign((String) getStyles().get("textAlign"));
        if (textAlign <= 0) {
            textAlign = GravityCompat.START;
        }
        if (getHostView() instanceof WXEditText) {
            ((WXEditText) getHostView()).setGravity(textAlign | getVerticalGravity());
        }
    }

    /* access modifiers changed from: protected */
    public final float getMeasuredLineHeight() {
        return (this.mLineHeight == -1 || this.mLineHeight <= 0) ? this.mPaint.getFontMetrics((Paint.FontMetrics) null) : (float) this.mLineHeight;
    }

    /* access modifiers changed from: protected */
    public float getMeasureHeight() {
        return getMeasuredLineHeight();
    }

    /* access modifiers changed from: protected */
    public void updateStyleAndAttrs() {
        if (getStyles().size() > 0) {
            String str = null;
            int fontSize = getStyles().containsKey("fontSize") ? WXStyle.getFontSize(getStyles(), getViewPortWidth()) : -1;
            if (getStyles().containsKey("fontFamily")) {
                str = WXStyle.getFontFamily(getStyles());
            }
            int fontStyle = getStyles().containsKey("fontStyle") ? WXStyle.getFontStyle(getStyles()) : -1;
            int fontWeight = getStyles().containsKey("fontWeight") ? WXStyle.getFontWeight(getStyles()) : -1;
            int lineHeight = WXStyle.getLineHeight(getStyles(), getViewPortWidth());
            if (lineHeight != -1) {
                this.mLineHeight = lineHeight;
            }
            if (fontSize != -1) {
                this.mPaint.setTextSize((float) fontSize);
            }
            if (str != null) {
                TypefaceUtil.applyFontStyle(this.mPaint, fontStyle, fontWeight, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public WXEditText initComponentHostView(@NonNull Context context) {
        WXEditText wXEditText = new WXEditText(context);
        appleStyleAfterCreated(wXEditText);
        return wXEditText;
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(WXEditText wXEditText) {
        super.onHostViewInitialized(wXEditText);
        addFocusChangeListener(new WXComponent.OnFocusChangeListener() {
            public void onFocusChange(boolean z) {
                if (!z) {
                    AbstractEditComponent.this.decideSoftKeyboard();
                }
                AbstractEditComponent.this.setPseudoClassStatus(Constants.PSEUDO.FOCUS, z);
            }
        });
        addKeyboardListener(wXEditText);
    }

    /* access modifiers changed from: protected */
    public boolean isConsumeTouch() {
        return !isDisabled();
    }

    private void applyOnClickListener() {
        addClickListener(this.mOnClickListener);
    }

    /* access modifiers changed from: protected */
    public void appleStyleAfterCreated(final WXEditText wXEditText) {
        int textAlign = getTextAlign((String) getStyles().get("textAlign"));
        if (textAlign <= 0) {
            textAlign = GravityCompat.START;
        }
        wXEditText.setGravity(textAlign | getVerticalGravity());
        int color = WXResourceUtils.getColor("#999999");
        if (color != Integer.MIN_VALUE) {
            wXEditText.setHintTextColor(color);
        }
        this.mTextChangedEventDispatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (AbstractEditComponent.this.mTextChangedListeners != null) {
                    for (TextWatcher beforeTextChanged : AbstractEditComponent.this.mTextChangedListeners) {
                        beforeTextChanged.beforeTextChanged(charSequence, i, i2, i3);
                    }
                }
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (AbstractEditComponent.this.mFormatter != null) {
                    String access$800 = AbstractEditComponent.this.mFormatter.format(AbstractEditComponent.this.mFormatter.recover(charSequence.toString()));
                    if (access$800.equals(charSequence.toString()) || AbstractEditComponent.this.mFormatRepeatCount >= 3) {
                        int unused = AbstractEditComponent.this.mFormatRepeatCount = 0;
                    } else {
                        int unused2 = AbstractEditComponent.this.mFormatRepeatCount = AbstractEditComponent.this.mFormatRepeatCount + 1;
                        int length = AbstractEditComponent.this.mFormatter.format(AbstractEditComponent.this.mFormatter.recover(charSequence.subSequence(0, wXEditText.getSelectionStart()).toString())).length();
                        wXEditText.setText(access$800);
                        wXEditText.setSelection(length);
                        return;
                    }
                }
                if (AbstractEditComponent.this.mTextChangedListeners != null) {
                    for (TextWatcher onTextChanged : AbstractEditComponent.this.mTextChangedListeners) {
                        onTextChanged.onTextChanged(charSequence, i, i2, i3);
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
                if (AbstractEditComponent.this.mTextChangedListeners != null) {
                    for (TextWatcher afterTextChanged : AbstractEditComponent.this.mTextChangedListeners) {
                        afterTextChanged.afterTextChanged(editable);
                    }
                }
            }
        };
        wXEditText.addTextChangedListener(this.mTextChangedEventDispatcher);
        wXEditText.setTextSize(0, (float) WXStyle.getFontSize(getStyles(), getInstance().getInstanceViewPortWidth()));
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (getHostView() != null && !TextUtils.isEmpty(str)) {
            final TextView textView = (TextView) getHostView();
            if (str.equals(Constants.Event.CHANGE)) {
                addFocusChangeListener(new WXComponent.OnFocusChangeListener() {
                    public void onFocusChange(boolean z) {
                        if (z) {
                            String unused = AbstractEditComponent.this.mLastValue = textView.getText().toString();
                            return;
                        }
                        CharSequence text = textView.getText();
                        if (text == null) {
                            text = "";
                        }
                        if (!text.toString().equals(AbstractEditComponent.this.mLastValue)) {
                            AbstractEditComponent.this.fireEvent(Constants.Event.CHANGE, text.toString());
                            String unused2 = AbstractEditComponent.this.mLastValue = textView.getText().toString();
                        }
                    }
                });
                addEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i != AbstractEditComponent.this.mEditorAction) {
                            return false;
                        }
                        CharSequence text = textView.getText();
                        if (text == null) {
                            text = "";
                        }
                        if (!text.toString().equals(AbstractEditComponent.this.mLastValue)) {
                            AbstractEditComponent.this.fireEvent(Constants.Event.CHANGE, text.toString());
                            String unused = AbstractEditComponent.this.mLastValue = textView.getText().toString();
                        }
                        if (AbstractEditComponent.this.getParent() != null) {
                            AbstractEditComponent.this.getParent().interceptFocus();
                        }
                        AbstractEditComponent.this.hideSoftKeyboard();
                        return true;
                    }
                });
            } else if (str.equals("input")) {
                addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable editable) {
                    }

                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        if (AbstractEditComponent.this.mIgnoreNextOnInputEvent) {
                            boolean unused = AbstractEditComponent.this.mIgnoreNextOnInputEvent = false;
                            String unused2 = AbstractEditComponent.this.mBeforeText = charSequence.toString();
                        } else if (!AbstractEditComponent.this.mBeforeText.equals(charSequence.toString())) {
                            String unused3 = AbstractEditComponent.this.mBeforeText = charSequence.toString();
                            AbstractEditComponent.this.fireEvent("input", charSequence.toString());
                        }
                    }
                });
            }
            if (Constants.Event.RETURN.equals(str)) {
                addEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i != AbstractEditComponent.this.mEditorAction) {
                            return false;
                        }
                        HashMap hashMap = new HashMap(2);
                        hashMap.put(Constants.Name.RETURN_KEY_TYPE, AbstractEditComponent.this.mReturnKeyType);
                        hashMap.put("value", textView.getText().toString());
                        AbstractEditComponent.this.fireEvent(Constants.Event.RETURN, hashMap);
                        return true;
                    }
                });
            }
            if (Constants.Event.KEYBOARD.equals(str)) {
                this.mListeningKeyboard = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public void fireEvent(String str, String str2) {
        if (str != null) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("value", str2);
            hashMap.put("timeStamp", Long.valueOf(System.currentTimeMillis()));
            HashMap hashMap2 = new HashMap();
            HashMap hashMap3 = new HashMap();
            hashMap3.put("value", str2);
            hashMap2.put(TemplateDom.KEY_ATTRS, hashMap3);
            WXSDKManager.getInstance().fireEvent(getInstanceId(), getRef(), str, hashMap, hashMap2);
        }
    }

    public void performOnChange(String str) {
        if (getEvents() != null) {
            fireEvent(getEvents().contains(Constants.Event.CHANGE) ? Constants.Event.CHANGE : null, str);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r5, java.lang.Object r6) {
        /*
            r4 = this;
            int r0 = r5.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -1898657397: goto L_0x00ba;
                case -1576785488: goto L_0x00b0;
                case -1065511464: goto L_0x00a6;
                case -791400086: goto L_0x009b;
                case 107876: goto L_0x0090;
                case 108114: goto L_0x0085;
                case 3575610: goto L_0x007b;
                case 94842723: goto L_0x0071;
                case 102977279: goto L_0x0066;
                case 124732746: goto L_0x005b;
                case 270940796: goto L_0x0050;
                case 365601008: goto L_0x0045;
                case 598246771: goto L_0x003a;
                case 914346044: goto L_0x002e;
                case 947486441: goto L_0x0022;
                case 1625554645: goto L_0x0016;
                case 1667607689: goto L_0x000b;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x00c5
        L_0x000b:
            java.lang.String r0 = "autofocus"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 4
            goto L_0x00c6
        L_0x0016:
            java.lang.String r0 = "allowCopyPaste"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 16
            goto L_0x00c6
        L_0x0022:
            java.lang.String r0 = "returnKeyType"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 14
            goto L_0x00c6
        L_0x002e:
            java.lang.String r0 = "singleline"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 8
            goto L_0x00c6
        L_0x003a:
            java.lang.String r0 = "placeholder"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 1
            goto L_0x00c6
        L_0x0045:
            java.lang.String r0 = "fontSize"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 6
            goto L_0x00c6
        L_0x0050:
            java.lang.String r0 = "disabled"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 0
            goto L_0x00c6
        L_0x005b:
            java.lang.String r0 = "maxlength"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 11
            goto L_0x00c6
        L_0x0066:
            java.lang.String r0 = "lines"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 9
            goto L_0x00c6
        L_0x0071:
            java.lang.String r0 = "color"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 5
            goto L_0x00c6
        L_0x007b:
            java.lang.String r0 = "type"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 3
            goto L_0x00c6
        L_0x0085:
            java.lang.String r0 = "min"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 13
            goto L_0x00c6
        L_0x0090:
            java.lang.String r0 = "max"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 12
            goto L_0x00c6
        L_0x009b:
            java.lang.String r0 = "maxLength"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 10
            goto L_0x00c6
        L_0x00a6:
            java.lang.String r0 = "textAlign"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 7
            goto L_0x00c6
        L_0x00b0:
            java.lang.String r0 = "placeholderColor"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 2
            goto L_0x00c6
        L_0x00ba:
            java.lang.String r0 = "keepSelectionIndex"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x00c5
            r0 = 15
            goto L_0x00c6
        L_0x00c5:
            r0 = -1
        L_0x00c6:
            r3 = 0
            switch(r0) {
                case 0: goto L_0x0194;
                case 1: goto L_0x018a;
                case 2: goto L_0x0180;
                case 3: goto L_0x0176;
                case 4: goto L_0x0168;
                case 5: goto L_0x015e;
                case 6: goto L_0x0154;
                case 7: goto L_0x014a;
                case 8: goto L_0x013c;
                case 9: goto L_0x012e;
                case 10: goto L_0x0120;
                case 11: goto L_0x0112;
                case 12: goto L_0x010a;
                case 13: goto L_0x0102;
                case 14: goto L_0x00fa;
                case 15: goto L_0x00eb;
                case 16: goto L_0x00cf;
                default: goto L_0x00ca;
            }
        L_0x00ca:
            boolean r5 = super.setProperty(r5, r6)
            return r5
        L_0x00cf:
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r2)
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r5)
            boolean r5 = r5.booleanValue()
            android.view.View r6 = r4.getHostView()
            if (r6 == 0) goto L_0x00ea
            android.view.View r6 = r4.getHostView()
            com.taobao.weex.ui.view.WXEditText r6 = (com.taobao.weex.ui.view.WXEditText) r6
            r6.setAllowCopyPaste(r5)
        L_0x00ea:
            return r2
        L_0x00eb:
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r5)
            boolean r5 = r5.booleanValue()
            r4.mKeepSelectionIndex = r5
            return r2
        L_0x00fa:
            java.lang.String r5 = java.lang.String.valueOf(r6)
            r4.setReturnKeyType(r5)
            return r2
        L_0x0102:
            java.lang.String r5 = java.lang.String.valueOf(r6)
            r4.setMin(r5)
            return r2
        L_0x010a:
            java.lang.String r5 = java.lang.String.valueOf(r6)
            r4.setMax(r5)
            return r2
        L_0x0112:
            java.lang.Integer r5 = com.taobao.weex.utils.WXUtils.getInteger(r6, r3)
            if (r5 == 0) goto L_0x011f
            int r5 = r5.intValue()
            r4.setMaxLength(r5)
        L_0x011f:
            return r2
        L_0x0120:
            java.lang.Integer r5 = com.taobao.weex.utils.WXUtils.getInteger(r6, r3)
            if (r5 == 0) goto L_0x012d
            int r5 = r5.intValue()
            r4.setMaxLength(r5)
        L_0x012d:
            return r2
        L_0x012e:
            java.lang.Integer r5 = com.taobao.weex.utils.WXUtils.getInteger(r6, r3)
            if (r5 == 0) goto L_0x013b
            int r5 = r5.intValue()
            r4.setLines(r5)
        L_0x013b:
            return r2
        L_0x013c:
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r3)
            if (r5 == 0) goto L_0x0149
            boolean r5 = r5.booleanValue()
            r4.setSingleLine(r5)
        L_0x0149:
            return r2
        L_0x014a:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x0153
            r4.setTextAlign(r5)
        L_0x0153:
            return r2
        L_0x0154:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x015d
            r4.setFontSize(r5)
        L_0x015d:
            return r2
        L_0x015e:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x0167
            r4.setColor(r5)
        L_0x0167:
            return r2
        L_0x0168:
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r3)
            if (r5 == 0) goto L_0x0175
            boolean r5 = r5.booleanValue()
            r4.setAutofocus(r5)
        L_0x0175:
            return r2
        L_0x0176:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x017f
            r4.setType(r5)
        L_0x017f:
            return r2
        L_0x0180:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x0189
            r4.setPlaceholderColor(r5)
        L_0x0189:
            return r2
        L_0x018a:
            java.lang.String r5 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            if (r5 == 0) goto L_0x0193
            r4.setPlaceholder(r5)
        L_0x0193:
            return r2
        L_0x0194:
            java.lang.Boolean r5 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r3)
            if (r5 == 0) goto L_0x01c1
            android.view.View r6 = r4.mHost
            if (r6 == 0) goto L_0x01c1
            boolean r5 = r5.booleanValue()
            if (r5 == 0) goto L_0x01b3
            android.view.View r5 = r4.mHost
            com.taobao.weex.ui.view.WXEditText r5 = (com.taobao.weex.ui.view.WXEditText) r5
            r5.setFocusable(r1)
            android.view.View r5 = r4.mHost
            com.taobao.weex.ui.view.WXEditText r5 = (com.taobao.weex.ui.view.WXEditText) r5
            r5.setFocusableInTouchMode(r1)
            goto L_0x01c1
        L_0x01b3:
            android.view.View r5 = r4.mHost
            com.taobao.weex.ui.view.WXEditText r5 = (com.taobao.weex.ui.view.WXEditText) r5
            r5.setFocusableInTouchMode(r2)
            android.view.View r5 = r4.mHost
            com.taobao.weex.ui.view.WXEditText r5 = (com.taobao.weex.ui.view.WXEditText) r5
            r5.setFocusable(r2)
        L_0x01c1:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.AbstractEditComponent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "returnKeyType")
    public void setReturnKeyType(String str) {
        if (getHostView() != null) {
            this.mReturnKeyType = str;
            char c = 65535;
            switch (str.hashCode()) {
                case -906336856:
                    if (str.equals("search")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3304:
                    if (str.equals(ReturnTypes.GO)) {
                        c = 1;
                        break;
                    }
                    break;
                case 3089282:
                    if (str.equals(ReturnTypes.DONE)) {
                        c = 5;
                        break;
                    }
                    break;
                case 3377907:
                    if (str.equals(ReturnTypes.NEXT)) {
                        c = 2;
                        break;
                    }
                    break;
                case 3526536:
                    if (str.equals("send")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1544803905:
                    if (str.equals("default")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.mEditorAction = 0;
                    break;
                case 1:
                    this.mEditorAction = 2;
                    break;
                case 2:
                    this.mEditorAction = 5;
                    break;
                case 3:
                    this.mEditorAction = 3;
                    break;
                case 4:
                    this.mEditorAction = 4;
                    break;
                case 5:
                    this.mEditorAction = 6;
                    break;
            }
            blur();
            ((WXEditText) getHostView()).setImeOptions(this.mEditorAction);
        }
    }

    @WXComponentProp(name = "placeholder")
    public void setPlaceholder(String str) {
        if (str != null && getHostView() != null) {
            ((WXEditText) getHostView()).setHint(str);
        }
    }

    @WXComponentProp(name = "placeholderColor")
    public void setPlaceholderColor(String str) {
        int color;
        if (getHostView() != null && !TextUtils.isEmpty(str) && (color = WXResourceUtils.getColor(str)) != Integer.MIN_VALUE) {
            ((WXEditText) getHostView()).setHintTextColor(color);
        }
    }

    @WXComponentProp(name = "type")
    public void setType(String str) {
        Log.e("weex", "setType=" + str);
        if (str != null && getHostView() != null) {
            this.mType = str;
            ((EditText) getHostView()).setInputType(getInputType(this.mType));
            String str2 = this.mType;
            char c = 65535;
            int hashCode = str2.hashCode();
            if (hashCode != 3076014) {
                if (hashCode == 3560141 && str2.equals("time")) {
                    c = 1;
                }
            } else if (str2.equals("date")) {
                c = 0;
            }
            switch (c) {
                case 0:
                case 1:
                    applyOnClickListener();
                    return;
                default:
                    return;
            }
        }
    }

    @WXComponentProp(name = "autofocus")
    public void setAutofocus(boolean z) {
        if (getHostView() != null) {
            this.mAutoFocus = z;
            EditText editText = (EditText) getHostView();
            if (this.mAutoFocus) {
                editText.setFocusable(true);
                editText.requestFocus();
                editText.setFocusableInTouchMode(true);
                showSoftKeyboard();
                return;
            }
            hideSoftKeyboard();
        }
    }

    @WXComponentProp(name = "value")
    public void setValue(String str) {
        WXEditText wXEditText = (WXEditText) getHostView();
        if (wXEditText != null && !TextUtils.equals(wXEditText.getText(), str)) {
            this.mIgnoreNextOnInputEvent = true;
            int selectionStart = wXEditText.getSelectionStart();
            wXEditText.setText(str);
            if (!this.mKeepSelectionIndex) {
                selectionStart = str.length();
            }
            if (str == null) {
                selectionStart = 0;
            }
            wXEditText.setSelection(selectionStart);
        }
    }

    @WXComponentProp(name = "color")
    public void setColor(String str) {
        int color;
        if (getHostView() != null && !TextUtils.isEmpty(str) && (color = WXResourceUtils.getColor(str)) != Integer.MIN_VALUE) {
            ((WXEditText) getHostView()).setTextColor(color);
        }
    }

    @WXComponentProp(name = "fontSize")
    public void setFontSize(String str) {
        if (getHostView() != null && str != null) {
            HashMap hashMap = new HashMap(1);
            hashMap.put("fontSize", str);
            ((WXEditText) getHostView()).setTextSize(0, (float) WXStyle.getFontSize(hashMap, getInstance().getInstanceViewPortWidth()));
        }
    }

    @WXComponentProp(name = "textAlign")
    public void setTextAlign(String str) {
        int textAlign = getTextAlign(str);
        if (textAlign > 0) {
            ((WXEditText) getHostView()).setGravity(textAlign | getVerticalGravity());
        }
    }

    @WXComponentProp(name = "singleline")
    public void setSingleLine(boolean z) {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).setSingleLine(z);
        }
    }

    @WXComponentProp(name = "lines")
    public void setLines(int i) {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).setLines(i);
        }
    }

    @WXComponentProp(name = "maxLength")
    public void setMaxLength(int i) {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
        }
    }

    @WXComponentProp(name = "maxlength")
    @Deprecated
    public void setMaxlength(int i) {
        setMaxLength(i);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getInputType(java.lang.String r6) {
        /*
            r5 = this;
            int r0 = r6.hashCode()
            r1 = 3
            r2 = 4
            r3 = 1
            r4 = 0
            switch(r0) {
                case -1034364087: goto L_0x005c;
                case 114715: goto L_0x0052;
                case 116079: goto L_0x0048;
                case 3076014: goto L_0x003e;
                case 3556653: goto L_0x0034;
                case 3560141: goto L_0x002a;
                case 96619420: goto L_0x0020;
                case 1216985755: goto L_0x0016;
                case 1793702779: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0067
        L_0x000c:
            java.lang.String r0 = "datetime"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 2
            goto L_0x0068
        L_0x0016:
            java.lang.String r0 = "password"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 4
            goto L_0x0068
        L_0x0020:
            java.lang.String r0 = "email"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 3
            goto L_0x0068
        L_0x002a:
            java.lang.String r0 = "time"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 6
            goto L_0x0068
        L_0x0034:
            java.lang.String r0 = "text"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 0
            goto L_0x0068
        L_0x003e:
            java.lang.String r0 = "date"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 1
            goto L_0x0068
        L_0x0048:
            java.lang.String r0 = "url"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 7
            goto L_0x0068
        L_0x0052:
            java.lang.String r0 = "tel"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 5
            goto L_0x0068
        L_0x005c:
            java.lang.String r0 = "number"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x0067
            r6 = 8
            goto L_0x0068
        L_0x0067:
            r6 = -1
        L_0x0068:
            switch(r6) {
                case 0: goto L_0x006b;
                case 1: goto L_0x009e;
                case 2: goto L_0x009c;
                case 3: goto L_0x0099;
                case 4: goto L_0x0083;
                case 5: goto L_0x00a8;
                case 6: goto L_0x0073;
                case 7: goto L_0x0070;
                case 8: goto L_0x006d;
                default: goto L_0x006b;
            }
        L_0x006b:
            r1 = 1
            goto L_0x00a8
        L_0x006d:
            r1 = 8194(0x2002, float:1.1482E-41)
            goto L_0x00a8
        L_0x0070:
            r1 = 17
            goto L_0x00a8
        L_0x0073:
            android.view.View r6 = r5.getHostView()
            if (r6 == 0) goto L_0x00a7
            android.view.View r6 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r6 = (com.taobao.weex.ui.view.WXEditText) r6
            r6.setFocusable(r4)
            goto L_0x00a7
        L_0x0083:
            r1 = 129(0x81, float:1.81E-43)
            android.view.View r6 = r5.getHostView()
            if (r6 == 0) goto L_0x00a8
            android.view.View r6 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r6 = (com.taobao.weex.ui.view.WXEditText) r6
            android.text.method.PasswordTransformationMethod r0 = android.text.method.PasswordTransformationMethod.getInstance()
            r6.setTransformationMethod(r0)
            goto L_0x00a8
        L_0x0099:
            r1 = 33
            goto L_0x00a8
        L_0x009c:
            r1 = 4
            goto L_0x00a8
        L_0x009e:
            android.view.View r6 = r5.getHostView()
            com.taobao.weex.ui.view.WXEditText r6 = (com.taobao.weex.ui.view.WXEditText) r6
            r6.setFocusable(r4)
        L_0x00a7:
            r1 = 0
        L_0x00a8:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.AbstractEditComponent.getInputType(java.lang.String):int");
    }

    @WXComponentProp(name = "max")
    public void setMax(String str) {
        this.mMax = str;
    }

    @WXComponentProp(name = "min")
    public void setMin(String str) {
        this.mMin = str;
    }

    private boolean showSoftKeyboard() {
        if (getHostView() == null) {
            return false;
        }
        ((WXEditText) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
            public void run() {
                if (!(AbstractEditComponent.this.getInstance() == null || AbstractEditComponent.this.getInstance().getApmForInstance() == null)) {
                    AbstractEditComponent.this.getInstance().getApmForInstance().forceStopRecordInteraction = true;
                }
                AbstractEditComponent.this.mInputMethodManager.showSoftInput(AbstractEditComponent.this.getHostView(), 1);
            }
        }), 100);
        return true;
    }

    /* access modifiers changed from: private */
    public void hideSoftKeyboard() {
        if (getHostView() != null) {
            ((WXEditText) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    AbstractEditComponent.this.mInputMethodManager.hideSoftInputFromWindow(((WXEditText) AbstractEditComponent.this.getHostView()).getWindowToken(), 0);
                }
            }), 16);
        }
    }

    private int getTextAlign(String str) {
        int i = isLayoutRTL() ? GravityCompat.END : GravityCompat.START;
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        if (str.equals("left")) {
            return GravityCompat.START;
        }
        if (str.equals("center")) {
            return 17;
        }
        return str.equals("right") ? GravityCompat.END : i;
    }

    @JSMethod
    public void blur() {
        WXEditText wXEditText = (WXEditText) getHostView();
        if (wXEditText != null && wXEditText.hasFocus()) {
            if (getParent() != null) {
                getParent().interceptFocus();
            }
            wXEditText.clearFocus();
            hideSoftKeyboard();
        }
    }

    @JSMethod
    public void focus() {
        WXEditText wXEditText = (WXEditText) getHostView();
        if (wXEditText != null && !wXEditText.hasFocus()) {
            if (getParent() != null) {
                getParent().ignoreFocus();
            }
            wXEditText.requestFocus();
            wXEditText.setFocusable(true);
            wXEditText.setFocusableInTouchMode(true);
            showSoftKeyboard();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002c A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object convertEmptyProperty(java.lang.String r3, java.lang.Object r4) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = 94842723(0x5a72f63, float:1.5722012E-35)
            if (r0 == r1) goto L_0x0019
            r1 = 365601008(0x15caa0f0, float:8.1841065E-26)
            if (r0 == r1) goto L_0x000f
            goto L_0x0023
        L_0x000f:
            java.lang.String r0 = "fontSize"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0023
            r0 = 0
            goto L_0x0024
        L_0x0019:
            java.lang.String r0 = "color"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0023
            r0 = 1
            goto L_0x0024
        L_0x0023:
            r0 = -1
        L_0x0024:
            switch(r0) {
                case 0: goto L_0x002f;
                case 1: goto L_0x002c;
                default: goto L_0x0027;
            }
        L_0x0027:
            java.lang.Object r3 = super.convertEmptyProperty(r3, r4)
            return r3
        L_0x002c:
            java.lang.String r3 = "black"
            return r3
        L_0x002f:
            r3 = 32
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.AbstractEditComponent.convertEmptyProperty(java.lang.String, java.lang.Object):java.lang.Object");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = getContext();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void decideSoftKeyboard() {
        /*
            r4 = this;
            android.view.View r0 = r4.getHostView()
            if (r0 == 0) goto L_0x001e
            android.content.Context r1 = r4.getContext()
            if (r1 == 0) goto L_0x001e
            boolean r2 = r1 instanceof android.app.Activity
            if (r2 == 0) goto L_0x001e
            com.taobao.weex.ui.component.AbstractEditComponent$11 r2 = new com.taobao.weex.ui.component.AbstractEditComponent$11
            r2.<init>(r1)
            java.lang.Runnable r1 = com.taobao.weex.common.WXThread.secure((java.lang.Runnable) r2)
            r2 = 16
            r0.postDelayed(r1, r2)
        L_0x001e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.AbstractEditComponent.decideSoftKeyboard():void");
    }

    @JSMethod
    public void setSelectionRange(int i, int i2) {
        int length;
        EditText editText = (EditText) getHostView();
        if (editText != null && i <= (length = ((WXEditText) getHostView()).length()) && i2 <= length) {
            focus();
            editText.setSelection(i, i2);
        }
    }

    @JSMethod
    public void getSelectionRange(String str) {
        HashMap hashMap = new HashMap(2);
        EditText editText = (EditText) getHostView();
        if (editText != null) {
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();
            if (!editText.hasFocus()) {
                selectionStart = 0;
                selectionEnd = 0;
            }
            hashMap.put(Constants.Name.SELECTION_START, Integer.valueOf(selectionStart));
            hashMap.put(Constants.Name.SELECTION_END, Integer.valueOf(selectionEnd));
        }
        WXBridgeManager.getInstance().callback(getInstanceId(), str, hashMap, false);
    }

    @JSMethod
    public void setTextFormatter(JSONObject jSONObject) {
        try {
            String string = jSONObject.getString("formatRule");
            String string2 = jSONObject.getString("formatReplace");
            String string3 = jSONObject.getString("recoverRule");
            String string4 = jSONObject.getString("recoverReplace");
            PatternWrapper parseToPattern = parseToPattern(string, string2);
            PatternWrapper parseToPattern2 = parseToPattern(string3, string4);
            if (parseToPattern != null && parseToPattern2 != null) {
                this.mFormatter = new TextFormatter(parseToPattern, parseToPattern2);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public final void addEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        TextView textView;
        if (onEditorActionListener != null && (textView = (TextView) getHostView()) != null) {
            if (this.mEditorActionListeners == null) {
                this.mEditorActionListeners = new ArrayList();
                textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    private boolean handled = true;

                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        for (TextView.OnEditorActionListener onEditorActionListener : AbstractEditComponent.this.mEditorActionListeners) {
                            if (onEditorActionListener != null) {
                                this.handled = onEditorActionListener.onEditorAction(textView, i, keyEvent) & this.handled;
                            }
                        }
                        return this.handled;
                    }
                });
            }
            this.mEditorActionListeners.add(onEditorActionListener);
        }
    }

    public final void addTextChangedListener(TextWatcher textWatcher) {
        if (this.mTextChangedListeners == null) {
            this.mTextChangedListeners = new ArrayList();
        }
        this.mTextChangedListeners.add(textWatcher);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0003, code lost:
        r3 = r3.getContext();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addKeyboardListener(com.taobao.weex.ui.view.WXEditText r3) {
        /*
            r2 = this;
            if (r3 != 0) goto L_0x0003
            return
        L_0x0003:
            android.content.Context r3 = r3.getContext()
            if (r3 == 0) goto L_0x0018
            boolean r0 = r3 instanceof android.app.Activity
            if (r0 == 0) goto L_0x0018
            r0 = r3
            android.app.Activity r0 = (android.app.Activity) r0
            com.taobao.weex.ui.component.AbstractEditComponent$13 r1 = new com.taobao.weex.ui.component.AbstractEditComponent$13
            r1.<init>(r3)
            com.taobao.weex.ui.component.helper.SoftKeyboardDetector.registerKeyboardEventListener(r0, r1)
        L_0x0018:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.AbstractEditComponent.addKeyboardListener(com.taobao.weex.ui.view.WXEditText):void");
    }

    public void destroy() {
        super.destroy();
        if (this.mUnregister != null) {
            try {
                this.mUnregister.execute();
                this.mUnregister = null;
            } catch (Throwable th) {
                WXLogUtils.w("Unregister throw ", th);
            }
        }
    }

    private PatternWrapper parseToPattern(String str, String str2) {
        Pattern pattern;
        if (str == null || str2 == null) {
            return null;
        }
        if (!Pattern.compile("/[\\S]+/[i]?[m]?[g]?").matcher(str).matches()) {
            WXLogUtils.w("WXInput", "Illegal js pattern syntax: " + str);
            return null;
        }
        int i = 0;
        String substring = str.substring(str.lastIndexOf("/") + 1);
        String substring2 = str.substring(str.indexOf("/") + 1, str.lastIndexOf("/"));
        if (substring.contains(UploadQueueMgr.MSGTYPE_INTERVAL)) {
            i = 2;
        }
        if (substring.contains(WXComponent.PROP_FS_MATCH_PARENT)) {
            i |= 32;
        }
        boolean contains = substring.contains("g");
        try {
            pattern = Pattern.compile(substring2, i);
        } catch (PatternSyntaxException unused) {
            WXLogUtils.w("WXInput", "Pattern syntax error: " + substring2);
            pattern = null;
        }
        if (pattern == null) {
            return null;
        }
        PatternWrapper patternWrapper = new PatternWrapper();
        boolean unused2 = patternWrapper.global = contains;
        Pattern unused3 = patternWrapper.matcher = pattern;
        String unused4 = patternWrapper.replace = str2;
        return patternWrapper;
    }

    private static class PatternWrapper {
        /* access modifiers changed from: private */
        public boolean global;
        /* access modifiers changed from: private */
        public Pattern matcher;
        /* access modifiers changed from: private */
        public String replace;

        private PatternWrapper() {
            this.global = false;
        }
    }

    private static class TextFormatter {
        private PatternWrapper format;
        private PatternWrapper recover;

        private TextFormatter(PatternWrapper patternWrapper, PatternWrapper patternWrapper2) {
            this.format = patternWrapper;
            this.recover = patternWrapper2;
        }

        /* access modifiers changed from: private */
        public String format(String str) {
            try {
                if (this.format != null) {
                    if (this.format.global) {
                        return this.format.matcher.matcher(str).replaceAll(this.format.replace);
                    }
                    return this.format.matcher.matcher(str).replaceFirst(this.format.replace);
                }
            } catch (Throwable th) {
                WXLogUtils.w("WXInput", "[format] " + th.getMessage());
            }
            return str;
        }

        /* access modifiers changed from: private */
        public String recover(String str) {
            try {
                if (this.recover != null) {
                    if (this.recover.global) {
                        return this.recover.matcher.matcher(str).replaceAll(this.recover.replace);
                    }
                    return this.recover.matcher.matcher(str).replaceFirst(this.recover.replace);
                }
            } catch (Throwable th) {
                WXLogUtils.w("WXInput", "[formatted] " + th.getMessage());
            }
            return str;
        }
    }
}
