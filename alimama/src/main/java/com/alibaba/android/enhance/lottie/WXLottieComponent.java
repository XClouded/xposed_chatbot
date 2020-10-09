package com.alibaba.android.enhance.lottie;

import android.animation.Animator;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class WXLottieComponent extends WXComponent<WXLottieView> {
    private static final String ATTR_AUTO_PLAY = "autoplay";
    private static final String ATTR_LOOP = "loop";
    private static final String ATTR_PROGRESS = "progress";
    private static final String ATTR_RESIZE_MODE = "resize";
    private static final String ATTR_SOURCE_JSON = "sourcejson";
    private static final String ATTR_SPEED = "speed";
    private static final int CODE_PLAY = 1;
    private static final String STATE_CANCEL = "cancel";
    private static final String STATE_END = "complete";
    private static final String STATE_REPEAT = "repeat";
    private static final String STATE_START = "start";
    private static final String TAG = "wx-lottie";
    private boolean isDestroyed;
    private LottieAnimatorListener mAnimatorListener;
    private boolean mAutoPlay = true;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                WXLottieComponent.this.setAndPlayAnimation();
            }
        }
    };
    /* access modifiers changed from: private */
    public byte[] mJsonBytes = null;

    public WXLottieComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXLottieComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, i, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public WXLottieView initComponentHostView(@NonNull Context context) {
        WXLottieView wXLottieView = new WXLottieView(context);
        this.mAnimatorListener = new LottieAnimatorListener();
        wXLottieView.addAnimatorListener(this.mAnimatorListener);
        return wXLottieView;
    }

    class LottieAnimatorListener implements Animator.AnimatorListener {
        LottieAnimatorListener() {
        }

        public void onAnimationStart(Animator animator) {
            WXLottieComponent.this.fireEvent("start", Collections.emptyMap());
        }

        public void onAnimationEnd(Animator animator) {
            WXLottieComponent.this.fireEvent(WXLottieComponent.STATE_END, Collections.emptyMap());
        }

        public void onAnimationCancel(Animator animator) {
            WXLottieComponent.this.fireEvent("cancel", Collections.emptyMap());
        }

        public void onAnimationRepeat(Animator animator) {
            WXLottieComponent.this.fireEvent("repeat", Collections.emptyMap());
        }
    }

    public void destroy() {
        super.destroy();
        if (!(this.mAnimatorListener == null || getHostView() == null)) {
            ((WXLottieView) getHostView()).removeAnimatorListener(this.mAnimatorListener);
        }
        this.mAnimatorListener = null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r5, java.lang.Object r6) {
        /*
            r4 = this;
            int r0 = r5.hashCode()
            r1 = 1
            r2 = 0
            switch(r0) {
                case -1110782077: goto L_0x0046;
                case -1001078227: goto L_0x003c;
                case -934437708: goto L_0x0032;
                case 114148: goto L_0x0028;
                case 3327652: goto L_0x001e;
                case 109641799: goto L_0x0014;
                case 1439562083: goto L_0x000a;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x0050
        L_0x000a:
            java.lang.String r0 = "autoplay"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0050
            r0 = 5
            goto L_0x0051
        L_0x0014:
            java.lang.String r0 = "speed"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0050
            r0 = 3
            goto L_0x0051
        L_0x001e:
            java.lang.String r0 = "loop"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0050
            r0 = 2
            goto L_0x0051
        L_0x0028:
            java.lang.String r0 = "src"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0050
            r0 = 0
            goto L_0x0051
        L_0x0032:
            java.lang.String r0 = "resize"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0050
            r0 = 4
            goto L_0x0051
        L_0x003c:
            java.lang.String r0 = "progress"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0050
            r0 = 6
            goto L_0x0051
        L_0x0046:
            java.lang.String r0 = "sourcejson"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0050
            r0 = 1
            goto L_0x0051
        L_0x0050:
            r0 = -1
        L_0x0051:
            r3 = 0
            switch(r0) {
                case 0: goto L_0x00a9;
                case 1: goto L_0x00a1;
                case 2: goto L_0x0091;
                case 3: goto L_0x007f;
                case 4: goto L_0x0077;
                case 5: goto L_0x0067;
                case 6: goto L_0x0056;
                default: goto L_0x0055;
            }
        L_0x0055:
            goto L_0x00b0
        L_0x0056:
            r0 = 0
            java.lang.Float r0 = java.lang.Float.valueOf(r0)
            java.lang.Float r0 = com.taobao.weex.utils.WXUtils.getFloat(r6, r0)
            float r0 = r0.floatValue()
            r4.setProgressValue(r0)
            goto L_0x00b0
        L_0x0067:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r0 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r0)
            boolean r0 = r0.booleanValue()
            r4.setAutoPlay(r0)
            goto L_0x00b0
        L_0x0077:
            java.lang.String r0 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            r4.setResizeMode(r0)
            goto L_0x00b0
        L_0x007f:
            r0 = 1065353216(0x3f800000, float:1.0)
            java.lang.Float r0 = java.lang.Float.valueOf(r0)
            java.lang.Float r0 = com.taobao.weex.utils.WXUtils.getFloat(r6, r0)
            float r0 = r0.floatValue()
            r4.setSpeed(r0)
            goto L_0x00b0
        L_0x0091:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r2)
            java.lang.Boolean r0 = com.taobao.weex.utils.WXUtils.getBoolean(r6, r0)
            boolean r0 = r0.booleanValue()
            r4.setLoop(r0)
            goto L_0x00b0
        L_0x00a1:
            java.lang.String r0 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            r4.setSourceJSON(r0)
            goto L_0x00b0
        L_0x00a9:
            java.lang.String r0 = com.taobao.weex.utils.WXUtils.getString(r6, r3)
            r4.setSourceURI(r0)
        L_0x00b0:
            boolean r5 = super.setProperty(r5, r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.lottie.WXLottieComponent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "sourcejson")
    public void setSourceJSON(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (getHostView() != null) {
                    ((WXLottieView) getHostView()).setAnimation(jSONObject);
                    if (this.mAutoPlay) {
                        ((WXLottieView) getHostView()).playAnimation();
                    }
                }
            } catch (Throwable th) {
                WXLogUtils.e(TAG, "play failed. " + th.getMessage());
            }
        }
    }

    @WXComponentProp(name = "src")
    public void setSourceURI(String str) {
        if (!TextUtils.isEmpty(str)) {
            String trim = str.trim();
            IWXHttpAdapter iWXHttpAdapter = WXSDKEngine.getIWXHttpAdapter();
            Uri uri = null;
            try {
                uri = Uri.parse(trim);
            } catch (Throwable th) {
                WXLogUtils.e(TAG, "uri not valid. \n" + th.getMessage());
            }
            if (uri != null && iWXHttpAdapter != null) {
                if ("local".equals(uri.getScheme()) || "file".equals(uri.getScheme())) {
                    loadSourceFromLocal(uri);
                } else if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                    WXRequest wXRequest = new WXRequest();
                    wXRequest.url = trim;
                    wXRequest.method = "GET";
                    iWXHttpAdapter.sendRequest(wXRequest, new WXLottieDownloadHttpListener());
                }
            }
        }
    }

    private void loadSourceFromLocal(@NonNull Uri uri) {
        if (uri.getScheme().equals("local")) {
            try {
                InputStream open = getInstance().getContext().getAssets().open(uri.getPath().substring(1));
                byte[] bArr = new byte[open.available()];
                open.read(bArr);
                open.close();
                this.mJsonBytes = bArr;
                onLottieJsonUpdated();
            } catch (IOException e) {
                WXLogUtils.d(TAG, e.toString());
            }
        } else if (uri.getScheme().equals("file") && "mounted".equals(Environment.getExternalStorageState())) {
            try {
                FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + uri.getPath());
                byte[] bArr2 = new byte[fileInputStream.available()];
                fileInputStream.read(bArr2);
                this.mJsonBytes = bArr2;
                fileInputStream.close();
                onLottieJsonUpdated();
            } catch (Throwable th) {
                WXLogUtils.e(TAG, th.getMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public void onLottieJsonUpdated() {
        if (!this.isDestroyed) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 1;
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    @WXComponentProp(name = "loop")
    public void setLoop(boolean z) {
        if (getHostView() != null) {
            ((WXLottieView) getHostView()).loop(z);
        }
    }

    @WXComponentProp(name = "speed")
    public void setSpeed(float f) {
        if (getHostView() != null) {
            ((WXLottieView) getHostView()).setSpeed(f);
        }
    }

    @WXComponentProp(name = "resize")
    public void setResizeMode(String str) {
        if (getHostView() != null && !TextUtils.isEmpty(str)) {
            if ("cover".equals(str)) {
                ((WXLottieView) getHostView()).setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else if ("contain".equals(str)) {
                ((WXLottieView) getHostView()).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else if ("center".equals(str)) {
                ((WXLottieView) getHostView()).setScaleType(ImageView.ScaleType.CENTER);
            }
        }
    }

    @WXComponentProp(name = "autoplay")
    public void setAutoPlay(boolean z) {
        this.mAutoPlay = z;
        if (!this.mAutoPlay) {
            reset();
        } else {
            play();
        }
    }

    @WXComponentProp(name = "progress")
    public void setProgressValue(float f) {
        setProgress(f);
    }

    @JSMethod
    public void play() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (WXLottieComponent.this.getHostView() == null || !ViewCompat.isAttachedToWindow(WXLottieComponent.this.getHostView())) {
                    WXLogUtils.e(WXLottieComponent.TAG, "can not play! maybe view is not attached to window");
                    return;
                }
                ((WXLottieView) WXLottieComponent.this.getHostView()).cancelAnimation();
                ((WXLottieView) WXLottieComponent.this.getHostView()).setProgress(0.0f);
                ((WXLottieView) WXLottieComponent.this.getHostView()).playAnimation();
            }
        });
    }

    @JSMethod
    public void pause() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (WXLottieComponent.this.getHostView() == null || !ViewCompat.isAttachedToWindow(WXLottieComponent.this.getHostView())) {
                    WXLogUtils.e(WXLottieComponent.TAG, "can not pause! maybe view is not attached to window");
                } else {
                    ((WXLottieView) WXLottieComponent.this.getHostView()).pauseAnimation();
                }
            }
        });
    }

    @JSMethod
    public void reset() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (WXLottieComponent.this.getHostView() == null || !ViewCompat.isAttachedToWindow(WXLottieComponent.this.getHostView())) {
                    WXLogUtils.e(WXLottieComponent.TAG, "can not reset! maybe view is not attached to window");
                    return;
                }
                ((WXLottieView) WXLottieComponent.this.getHostView()).cancelAnimation();
                ((WXLottieView) WXLottieComponent.this.getHostView()).setProgress(0.0f);
            }
        });
    }

    @JSMethod
    public void setProgress(final float f) {
        this.mHandler.post(new Runnable() {
            public void run() {
                float max = Math.max(0.0f, Math.min(1.0f, f));
                if (WXLottieComponent.this.getHostView() == null || !ViewCompat.isAttachedToWindow(WXLottieComponent.this.getHostView())) {
                    WXLogUtils.e(WXLottieComponent.TAG, "can not set propress! maybe view is not attached to window");
                } else {
                    ((WXLottieView) WXLottieComponent.this.getHostView()).setProgress(max);
                }
            }
        });
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        this.isDestroyed = true;
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    /* access modifiers changed from: private */
    public void setAndPlayAnimation() {
        if (this.mJsonBytes != null) {
            try {
                JSONObject jSONObject = new JSONObject(new String(this.mJsonBytes));
                if (getHostView() != null) {
                    ((WXLottieView) getHostView()).setAnimation(jSONObject);
                    if (this.mAutoPlay) {
                        ((WXLottieView) getHostView()).playAnimation();
                    }
                }
                this.mJsonBytes = null;
            } catch (JSONException e) {
                WXLogUtils.e(TAG, "play failed" + e.toString());
            }
        }
    }

    class WXLottieDownloadHttpListener implements IWXHttpAdapter.OnHttpListener {
        public void onHeadersReceived(int i, Map<String, List<String>> map) {
        }

        public void onHttpResponseProgress(int i) {
        }

        public void onHttpStart() {
        }

        public void onHttpUploadProgress(int i) {
        }

        WXLottieDownloadHttpListener() {
        }

        public void onHttpFinish(WXResponse wXResponse) {
            if (wXResponse == null) {
                return;
            }
            if (wXResponse.errorCode == null || !wXResponse.errorCode.equals("-1")) {
                int intValue = wXResponse.errorCode != null ? Integer.getInteger(wXResponse.errorCode).intValue() : 200;
                if (intValue >= 200 && intValue < 300) {
                    byte[] unused = WXLottieComponent.this.mJsonBytes = wXResponse.originalData;
                    WXLottieComponent.this.onLottieJsonUpdated();
                    return;
                }
                return;
            }
            WXLogUtils.e(WXLottieComponent.TAG, "get json failed" + wXResponse.errorMsg);
        }
    }
}
