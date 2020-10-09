package com.taobao.weex.ui.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Layout;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.layout.measurefunc.TextContentBoxMeasurement;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.flat.FlatComponent;
import com.taobao.weex.ui.flat.widget.TextWidget;
import com.taobao.weex.ui.view.WXTextView;
import com.taobao.weex.utils.FontDO;
import com.taobao.weex.utils.TypefaceUtil;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.InvocationTargetException;

@Component(lazyload = false)
public class WXText extends WXComponent<WXTextView> implements FlatComponent<TextWidget> {
    public static final int sDEFAULT_SIZE = 32;
    /* access modifiers changed from: private */
    public String mFontFamily;
    private TextWidget mTextWidget;
    private BroadcastReceiver mTypefaceObserver;

    public boolean promoteToView(boolean z) {
        if (getInstance().getFlatUIContext() != null) {
            return getInstance().getFlatUIContext().promoteToView(this, z, WXText.class);
        }
        return false;
    }

    @NonNull
    public TextWidget getOrCreateFlatWidget() {
        if (this.mTextWidget == null) {
            this.mTextWidget = new TextWidget(getInstance().getFlatUIContext());
        }
        return this.mTextWidget;
    }

    public boolean isVirtualComponent() {
        return true ^ promoteToView(true);
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXText(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public WXText(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXText(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        setContentBoxMeasurement(new TextContentBoxMeasurement(this));
    }

    /* access modifiers changed from: protected */
    public WXTextView initComponentHostView(@NonNull Context context) {
        WXTextView wXTextView = new WXTextView(context);
        wXTextView.holdComponent(this);
        return wXTextView;
    }

    public void updateExtra(Object obj) {
        super.updateExtra(obj);
        if (obj instanceof Layout) {
            Layout layout = (Layout) obj;
            if (!promoteToView(true)) {
                getOrCreateFlatWidget().updateTextDrawable(layout);
            } else if (getHostView() != null && !obj.equals(((WXTextView) getHostView()).getTextLayout())) {
                ((WXTextView) getHostView()).setTextLayout(layout);
                ((WXTextView) getHostView()).invalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setAriaLabel(String str) {
        WXTextView wXTextView = (WXTextView) getHostView();
        if (wXTextView != null) {
            wXTextView.setAriaLabel(str);
        }
    }

    public void refreshData(WXComponent wXComponent) {
        super.refreshData(wXComponent);
        if (wXComponent instanceof WXText) {
            updateExtra(wXComponent.getExtra());
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r3, java.lang.Object r4) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = 1
            switch(r0) {
                case -1550943582: goto L_0x0072;
                case -1224696685: goto L_0x0067;
                case -1065511464: goto L_0x005d;
                case -879295043: goto L_0x0053;
                case -734428249: goto L_0x0049;
                case -515807685: goto L_0x003e;
                case 94842723: goto L_0x0034;
                case 102977279: goto L_0x002a;
                case 111972721: goto L_0x001f;
                case 261414991: goto L_0x0015;
                case 365601008: goto L_0x000a;
                default: goto L_0x0008;
            }
        L_0x0008:
            goto L_0x007c
        L_0x000a:
            java.lang.String r0 = "fontSize"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 1
            goto L_0x007d
        L_0x0015:
            java.lang.String r0 = "textOverflow"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 7
            goto L_0x007d
        L_0x001f:
            java.lang.String r0 = "value"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 9
            goto L_0x007d
        L_0x002a:
            java.lang.String r0 = "lines"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 0
            goto L_0x007d
        L_0x0034:
            java.lang.String r0 = "color"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 4
            goto L_0x007d
        L_0x003e:
            java.lang.String r0 = "lineHeight"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 8
            goto L_0x007d
        L_0x0049:
            java.lang.String r0 = "fontWeight"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 2
            goto L_0x007d
        L_0x0053:
            java.lang.String r0 = "textDecoration"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 5
            goto L_0x007d
        L_0x005d:
            java.lang.String r0 = "textAlign"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 6
            goto L_0x007d
        L_0x0067:
            java.lang.String r0 = "fontFamily"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 10
            goto L_0x007d
        L_0x0072:
            java.lang.String r0 = "fontStyle"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x007c
            r0 = 3
            goto L_0x007d
        L_0x007c:
            r0 = -1
        L_0x007d:
            switch(r0) {
                case 0: goto L_0x008f;
                case 1: goto L_0x008f;
                case 2: goto L_0x008f;
                case 3: goto L_0x008f;
                case 4: goto L_0x008f;
                case 5: goto L_0x008f;
                case 6: goto L_0x008f;
                case 7: goto L_0x008f;
                case 8: goto L_0x008f;
                case 9: goto L_0x008f;
                case 10: goto L_0x0085;
                default: goto L_0x0080;
            }
        L_0x0080:
            boolean r3 = super.setProperty(r3, r4)
            return r3
        L_0x0085:
            if (r4 == 0) goto L_0x008e
            java.lang.String r3 = r4.toString()
            r2.registerTypefaceObserver(r3)
        L_0x008e:
            return r1
        L_0x008f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXText.setProperty(java.lang.String, java.lang.Object):boolean");
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
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXText.convertEmptyProperty(java.lang.String, java.lang.Object):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    public void createViewImpl() {
        if (promoteToView(true)) {
            super.createViewImpl();
        }
    }

    public void destroy() {
        super.destroy();
        if (WXEnvironment.getApplication() != null && this.mTypefaceObserver != null) {
            WXLogUtils.d("WXText", "Unregister the typeface observer");
            LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).unregisterReceiver(this.mTypefaceObserver);
            this.mTypefaceObserver = null;
        }
    }

    private void registerTypefaceObserver(String str) {
        if (WXEnvironment.getApplication() == null) {
            WXLogUtils.w("WXText", "ApplicationContent is null on register typeface observer");
            return;
        }
        this.mFontFamily = str;
        if (this.mTypefaceObserver == null) {
            this.mTypefaceObserver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    FontDO fontDO;
                    String stringExtra = intent.getStringExtra("fontFamily");
                    if (WXText.this.mFontFamily.equals(stringExtra) && (fontDO = TypefaceUtil.getFontDO(stringExtra)) != null && fontDO.getTypeface() != null && WXText.this.getHostView() != null) {
                        Layout textLayout = ((WXTextView) WXText.this.getHostView()).getTextLayout();
                        if (textLayout != null) {
                            textLayout.getPaint().setTypeface(fontDO.getTypeface());
                        } else {
                            WXLogUtils.d("WXText", "Layout not created");
                        }
                        WXBridgeManager.getInstance().markDirty(WXText.this.getInstanceId(), WXText.this.getRef(), true);
                        WXText.this.forceRelayout();
                    }
                }
            };
            LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).registerReceiver(this.mTypefaceObserver, new IntentFilter(TypefaceUtil.ACTION_TYPE_FACE_AVAILABLE));
        }
    }

    /* access modifiers changed from: protected */
    public void layoutDirectionDidChanged(boolean z) {
        forceRelayout();
    }

    /* access modifiers changed from: private */
    public void forceRelayout() {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                if (WXText.this.contentBoxMeasurement instanceof TextContentBoxMeasurement) {
                    ((TextContentBoxMeasurement) WXText.this.contentBoxMeasurement).forceRelayout();
                }
            }
        });
    }
}
