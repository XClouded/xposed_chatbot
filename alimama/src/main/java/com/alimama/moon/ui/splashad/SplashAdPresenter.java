package com.alimama.moon.ui.splashad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import com.alimama.moon.detail.DetailUrlUtil;
import com.alimama.moon.ui.WeexActivity;
import com.alimama.moon.ui.splashad.ISplashAdContract;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimama.union.app.infrastructure.weex.WeexPageGenerator;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.taobao.vessel.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplashAdPresenter implements ISplashAdContract.ISplashAdPresenter {
    /* access modifiers changed from: private */
    public ISplashAdContract.ISplashAdView adView;
    private CountDownTimer countDownTimer;
    /* access modifiers changed from: private */
    public SplashAdInfo info;
    private boolean isViewClosed = false;
    private final String jumpValue = "跳过";
    private Logger logger = LoggerFactory.getLogger((Class<?>) SplashAdPresenter.class);
    private CountDownTimer timeOutTimer;

    interface CountDownTimerListener {
        void countDown(int i);
    }

    public void start() {
    }

    public SplashAdPresenter(ISplashAdContract.ISplashAdView iSplashAdView) {
        this.adView = iSplashAdView;
        this.adView.setPresenter(this);
    }

    public void requestAd() {
        this.info = getSplashAdInfo();
        if (this.info != null) {
            startTimeOutTimer(Integer.valueOf(this.info.getFetchDelayTime()).intValue());
            String img = this.info.getImg();
            if (!TextUtils.isEmpty(img) && !img.startsWith("http")) {
                img = Utils.HTTPS_SCHEMA + img;
            }
            ImageLoader.getInstance().loadImage(img, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                    super.onLoadingComplete(str, view, bitmap);
                    if (SplashAdPresenter.this.adView != null && bitmap != null) {
                        SplashAdPresenter.this.stopTimeOutTimer();
                        SplashAdPresenter.this.adView.setAdImageview(bitmap);
                        ISplashAdContract.ISplashAdView access$000 = SplashAdPresenter.this.adView;
                        access$000.updateControlView("跳过 " + SplashAdPresenter.this.info.getDisplayTime());
                        SplashAdPresenter.this.startCountDownTimer(Integer.valueOf(SplashAdPresenter.this.info.getDisplayTime()).intValue());
                    }
                }
            });
            return;
        }
        closeAdView();
    }

    public void clickControlView(String str) {
        if (!TextUtils.isEmpty(str) && str.contains("跳过")) {
            closeAdView();
        }
    }

    public void clickAdView(Activity activity) {
        if (this.info != null && !TextUtils.isEmpty(this.info.getUrl())) {
            if (EtaoConfigCenter.getInstance().getSwitch(ConfigKeyList.UNION_SWITCH, "ad_clear_clipboard", true)) {
                UTHelper.sendControlHit("Page_tblm_lmapp_SplashAd", "clearClipboard");
                CommonUtils.clearClipboard();
            }
            if (isActivityUrl(this.info.getUrl())) {
                stopTimeOutTimer();
                closeAdView();
                String queryParameter = Uri.parse(this.info.getUrl()).getQueryParameter("targetUrl");
                Intent intent = new Intent(activity, WeexActivity.class);
                Uri.Builder buildUpon = WeexPageGenerator.getShareCreatorUri().buildUpon();
                buildUpon.appendQueryParameter("url", queryParameter);
                intent.setData(buildUpon.build());
                activity.startActivity(intent);
            } else if (isPureUrl(this.info.getUrl())) {
                stopTimeOutTimer();
                closeAdView();
                activity.startActivity(DetailUrlUtil.getIntent(activity, this.info.getUrl(), ""));
            } else {
                this.logger.error("not supported url: {}", (Object) this.info.getUrl());
            }
        }
    }

    private boolean isPureUrl(String str) {
        try {
            Uri parse = Uri.parse(str);
            if ("http".equals(parse.getScheme()) || "https".equals(parse.getScheme())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            this.logger.error("invalid url, ex: {}, clickUrl: {}", (Object) e.getMessage(), (Object) str);
            return false;
        }
    }

    private boolean isActivityUrl(String str) {
        try {
            Uri parse = Uri.parse(str);
            String scheme = parse.getScheme();
            String queryParameter = parse.getQueryParameter("targetUrl");
            if (!"moon".equals(scheme) || TextUtils.isEmpty(queryParameter)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            this.logger.error("invalid activity url, ex: {}, clickUrl: {}", (Object) e.getMessage(), (Object) str);
            return false;
        }
    }

    private SplashAdInfo getSplashAdInfo() {
        String configResult = EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_AD);
        if (!TextUtils.isEmpty(configResult)) {
            try {
                SafeJSONObject optJSONObject = new SafeJSONObject(configResult).optJSONObject("data");
                SplashAdInfo splashAdInfo = new SplashAdInfo();
                splashAdInfo.setDisplayTime(optJSONObject.optString("displayTime"));
                splashAdInfo.setImg(optJSONObject.optString("img"));
                splashAdInfo.setUrl(optJSONObject.optString("url"));
                splashAdInfo.setFetchDelayTime(optJSONObject.optString("fetchDelayTime"));
                if (splashAdInfo.isPass()) {
                    return splashAdInfo;
                }
            } catch (Exception unused) {
                return null;
            }
        }
        return null;
    }

    private void startTimeOutTimer(int i) {
        if (this.timeOutTimer == null) {
            this.timeOutTimer = new CountDownTimer(i, new CountDownTimerListener() {
                public void countDown(int i) {
                    if (i == 0) {
                        SplashAdPresenter.this.timeOut();
                    }
                }
            });
        }
        this.timeOutTimer.start();
    }

    /* access modifiers changed from: private */
    public void stopTimeOutTimer() {
        if (this.timeOutTimer != null) {
            this.timeOutTimer.stop();
        }
    }

    /* access modifiers changed from: private */
    public void timeOut() {
        closeAdView();
    }

    /* access modifiers changed from: private */
    public void startCountDownTimer(int i) {
        if (this.countDownTimer == null) {
            this.countDownTimer = new CountDownTimer(i, new CountDownTimerListener() {
                public void countDown(int i) {
                    if (SplashAdPresenter.this.adView != null) {
                        if (i >= 0) {
                            ISplashAdContract.ISplashAdView access$000 = SplashAdPresenter.this.adView;
                            access$000.updateControlView("跳过 " + i);
                        }
                        if (i <= 0) {
                            SplashAdPresenter.this.closeAdView();
                        }
                    }
                }
            });
        }
        this.countDownTimer.start();
    }

    private void stopCountDownTimer() {
        if (this.countDownTimer != null) {
            this.countDownTimer.stop();
        }
    }

    /* access modifiers changed from: private */
    public void closeAdView() {
        if (!this.isViewClosed) {
            this.isViewClosed = true;
            stopCountDownTimer();
            stopTimeOutTimer();
            if (this.adView != null) {
                this.adView.closeView();
            }
        }
    }

    class CountDownTimer implements Runnable {
        private Handler mHandler = new Handler(Looper.myLooper());
        private CountDownTimerListener timerListener;
        private int times;

        public CountDownTimer(int i, CountDownTimerListener countDownTimerListener) {
            this.times = i;
            this.timerListener = countDownTimerListener;
        }

        public void start() {
            if (this.mHandler != null) {
                this.mHandler.post(this);
            }
        }

        public void stop() {
            if (this.mHandler != null) {
                this.mHandler.removeCallbacks(this);
            }
        }

        public void run() {
            if (this.timerListener != null) {
                this.timerListener.countDown(this.times);
            }
            this.times--;
            if (this.times >= 0) {
                this.mHandler.postDelayed(this, 1000);
            } else {
                this.mHandler.removeCallbacks(this);
            }
        }
    }
}
