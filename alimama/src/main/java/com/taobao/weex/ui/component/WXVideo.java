package com.taobao.weex.ui.component;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.WXVideoView;
import com.taobao.weex.utils.WXLogUtils;
import com.uc.webview.export.media.MessageID;
import java.util.HashMap;

@Component(lazyload = false)
public class WXVideo extends WXComponent<FrameLayout> {
    /* access modifiers changed from: private */
    public boolean mAutoPlay;
    /* access modifiers changed from: private */
    public boolean mError;
    boolean mPrepared;
    /* access modifiers changed from: private */
    public boolean mStopped;
    private WXVideoView.Wrapper mWrapper;

    @Deprecated
    public WXVideo(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXVideo(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public FrameLayout initComponentHostView(@NonNull Context context) {
        final WXVideoView.Wrapper wrapper = new WXVideoView.Wrapper(context);
        wrapper.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", "onError:" + i);
                }
                wrapper.getProgressBar().setVisibility(8);
                WXVideo.this.mPrepared = false;
                boolean unused = WXVideo.this.mError = true;
                if (WXVideo.this.getEvents().contains(Constants.Event.FAIL)) {
                    WXVideo.this.notify(Constants.Event.FAIL, "stop");
                }
                return true;
            }
        });
        wrapper.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", MessageID.onPrepared);
                }
                wrapper.getProgressBar().setVisibility(8);
                WXVideo.this.mPrepared = true;
                if (WXVideo.this.mAutoPlay) {
                    wrapper.start();
                }
                wrapper.getVideoView().seekTo(5);
                if (wrapper.getMediaController() != null) {
                    if (!WXVideo.this.mStopped) {
                        wrapper.getMediaController().show(3);
                    } else {
                        wrapper.getMediaController().hide();
                    }
                }
                boolean unused = WXVideo.this.mStopped = false;
            }
        });
        wrapper.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", MessageID.onCompletion);
                }
                if (WXVideo.this.getEvents().contains(Constants.Event.FINISH)) {
                    WXVideo.this.notify(Constants.Event.FINISH, "stop");
                }
            }
        });
        wrapper.setOnVideoPauseListener(new WXVideoView.VideoPlayListener() {
            public void onPause() {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", MessageID.onPause);
                }
                if (WXVideo.this.getEvents().contains("pause")) {
                    WXVideo.this.notify("pause", "pause");
                }
            }

            public void onStart() {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("Video", UmbrellaConstants.LIFECYCLE_START);
                }
                if (WXVideo.this.getEvents().contains("start")) {
                    WXVideo.this.notify("start", Constants.Value.PLAY);
                }
            }
        });
        this.mWrapper = wrapper;
        return wrapper;
    }

    /* access modifiers changed from: private */
    public void notify(String str, String str2) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("playStatus", str2);
        hashMap.put("timeStamp", Long.valueOf(System.currentTimeMillis()));
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        hashMap3.put("playStatus", str2);
        hashMap2.put(TemplateDom.KEY_ATTRS, hashMap3);
        WXSDKManager.getInstance().fireEvent(getInstanceId(), getRef(), str, hashMap, hashMap2);
    }

    public void bindData(WXComponent wXComponent) {
        super.bindData(wXComponent);
        addEvent(Constants.Event.APPEAR);
    }

    public void notifyAppearStateChange(String str, String str2) {
        super.notifyAppearStateChange(str, str2);
        this.mWrapper.createVideoViewIfVisible();
    }

    public void destroy() {
        super.destroy();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r4, java.lang.Object r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 1
            switch(r0) {
                case -167173695: goto L_0x0031;
                case 114148: goto L_0x0027;
                case 1438608771: goto L_0x001d;
                case 1439562083: goto L_0x0013;
                case 1582764102: goto L_0x0009;
                default: goto L_0x0008;
            }
        L_0x0008:
            goto L_0x003b
        L_0x0009:
            java.lang.String r0 = "playStatus"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003b
            r0 = 4
            goto L_0x003c
        L_0x0013:
            java.lang.String r0 = "autoplay"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003b
            r0 = 2
            goto L_0x003c
        L_0x001d:
            java.lang.String r0 = "autoPlay"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003b
            r0 = 1
            goto L_0x003c
        L_0x0027:
            java.lang.String r0 = "src"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003b
            r0 = 0
            goto L_0x003c
        L_0x0031:
            java.lang.String r0 = "zOrderTop"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003b
            r0 = 3
            goto L_0x003c
        L_0x003b:
            r0 = -1
        L_0x003c:
            r2 = 0
            switch(r0) {
                case 0: goto L_0x0071;
                case 1: goto L_0x0063;
                case 2: goto L_0x0063;
                case 3: goto L_0x004f;
                case 4: goto L_0x0045;
                default: goto L_0x0040;
            }
        L_0x0040:
            boolean r4 = super.setProperty(r4, r5)
            return r4
        L_0x0045:
            java.lang.String r4 = com.taobao.weex.utils.WXUtils.getString(r5, r2)
            if (r4 == 0) goto L_0x004e
            r3.setPlaystatus(r4)
        L_0x004e:
            return r1
        L_0x004f:
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r2)
            if (r4 == 0) goto L_0x0062
            com.taobao.weex.ui.view.WXVideoView$Wrapper r5 = r3.mWrapper
            com.taobao.weex.ui.view.WXVideoView r5 = r5.getVideoView()
            boolean r4 = r4.booleanValue()
            r5.setZOrderOnTop(r4)
        L_0x0062:
            return r1
        L_0x0063:
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r2)
            if (r4 == 0) goto L_0x0070
            boolean r4 = r4.booleanValue()
            r3.setAutoPlay(r4)
        L_0x0070:
            return r1
        L_0x0071:
            java.lang.String r4 = com.taobao.weex.utils.WXUtils.getString(r5, r2)
            if (r4 == 0) goto L_0x007a
            r3.setSrc(r4)
        L_0x007a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXVideo.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "src")
    public void setSrc(String str) {
        if (!TextUtils.isEmpty(str) && getHostView() != null && !TextUtils.isEmpty(str)) {
            this.mWrapper.setVideoURI(getInstance().rewriteUri(Uri.parse(str), "video"));
            this.mWrapper.getProgressBar().setVisibility(0);
        }
    }

    @WXComponentProp(name = "autoPlay")
    public void setAutoPlay(boolean z) {
        this.mAutoPlay = z;
        if (z) {
            this.mWrapper.createIfNotExist();
            this.mWrapper.start();
        }
    }

    @WXComponentProp(name = "controls")
    public void setControls(String str) {
        if (TextUtils.equals(Constants.Name.CONTROLS, str)) {
            this.mWrapper.setControls(true);
        } else if (TextUtils.equals("nocontrols", str)) {
            this.mWrapper.setControls(false);
        }
    }

    @WXComponentProp(name = "playStatus")
    public void setPlaystatus(String str) {
        if (!this.mPrepared || this.mError || this.mStopped) {
            if ((this.mError || this.mStopped) && str.equals(Constants.Value.PLAY)) {
                this.mError = false;
                this.mWrapper.resume();
                this.mWrapper.getProgressBar().setVisibility(0);
            }
        } else if (str.equals(Constants.Value.PLAY)) {
            this.mWrapper.start();
        } else if (str.equals("pause")) {
            this.mWrapper.pause();
        } else if (str.equals("stop")) {
            this.mWrapper.stopPlayback();
            this.mStopped = true;
        }
    }
}
