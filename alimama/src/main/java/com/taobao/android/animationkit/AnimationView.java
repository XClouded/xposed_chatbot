package com.taobao.android.animationkit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.FloatRange;
import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieImageAsset;
import com.taobao.orange.OrangeConfig;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class AnimationView extends FrameLayout {
    public static final int ANIM_KEY_DECISION_SLICE = 1;
    public static final int ANIM_KEY_FRAMEWORK_SLICE = 2;
    private static final int ANIM_KEY_UNKNOW = 0;
    public static final int ANIM_KEY_VOICE_THINKING = 3;
    private static final String TAG = "AnimationView";
    private JSONObject animationJson;
    private String animationName;
    private boolean autoPlay;
    /* access modifiers changed from: private */
    public final Map<String, Bitmap> bitmapCache;
    /* access modifiers changed from: private */
    public BitmapFetcher bitmapFetcher;
    private String lastAnimResFolder;
    private boolean loop;
    private long loopDelay;
    private LoopRunnable loopRunnable;
    private GuardedLottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public boolean pausedOrCanceled;

    public @interface AnimationKey {
    }

    public interface BitmapFetcher {
        Bitmap fetchBitmap(String str);
    }

    public void reverseAnimation() {
    }

    public AnimationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AnimationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AnimationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.autoPlay = true;
        this.animationName = "";
        this.animationJson = null;
        this.pausedOrCanceled = false;
        this.bitmapCache = new HashMap();
        this.loopDelay = 0;
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        this.lottieAnimationView = new GuardedLottieAnimationView(getContext());
        addView(this.lottieAnimationView);
        this.loopRunnable = new LoopRunnable(this);
        this.lottieAnimationView.addAnimatorListener(new AnimatorListener(this));
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.AnimationView);
        loop(obtainStyledAttributes.getBoolean(R.styleable.AnimationView_ak_loop, false));
        setupAnimKey(obtainStyledAttributes.getInt(R.styleable.AnimationView_ak_animKey, 0));
        String string = obtainStyledAttributes.getString(R.styleable.AnimationView_ak_jsonFilePath);
        String string2 = obtainStyledAttributes.getString(R.styleable.AnimationView_ak_imageAssetsFolder);
        if (!TextUtils.isEmpty(string)) {
            setAnimation(string);
        }
        if (!TextUtils.isEmpty(string2)) {
            setImageAssetsFolder(string2);
        }
        this.autoPlay = obtainStyledAttributes.getBoolean(R.styleable.AnimationView_ak_autoPlay, true);
        if (this.autoPlay) {
            setupAutoPlay();
            playAnimation();
        }
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: private */
    public void onLottieAnimationEnd() {
        if (this.loop && !this.pausedOrCanceled) {
            postDelayed(this.loopRunnable, this.loopDelay);
        }
    }

    public void setAutoPlay(boolean z) {
        this.autoPlay = z;
        setupAutoPlay();
    }

    private void setupAutoPlay() {
        try {
            Field declaredField = this.lottieAnimationView.getClass().getDeclaredField("autoPlay");
            declaredField.setAccessible(true);
            declaredField.set(this.lottieAnimationView, Boolean.valueOf(this.autoPlay));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public void setAnimationKey(@AnimationKey int i) {
        setupAnimKey(i);
    }

    private void setupAnimKey(@AnimationKey int i) {
        switch (i) {
            case 0:
                return;
            case 1:
                if (checkAnimationEnable("decision_slice")) {
                    setAnimation("decision_slice/decision_slice.json");
                    setImageAssetsFolder("decision_slice/images");
                    this.animationName = "decision_slice";
                    return;
                }
                return;
            case 2:
                if (checkAnimationEnable("framework_slice")) {
                    setAnimation("framework_slice/framework_slice.json");
                    setImageAssetsFolder("framework_slice/images");
                    this.animationName = "framework_slice";
                    return;
                }
                return;
            case 3:
                if (checkAnimationEnable("voice_thinking")) {
                    setAnimation("voice_thinking/voice_thinking.json");
                    setImageAssetsFolder("voice_thinking/images");
                    this.animationName = "voice_thinking";
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setAnimation(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.animationName = str;
            this.lottieAnimationView.setAnimation(str);
        }
    }

    public void setAnimation(JSONObject jSONObject) {
        this.animationJson = jSONObject;
        this.lottieAnimationView.setAnimation(jSONObject);
    }

    public void setBitmapFetcher(BitmapFetcher bitmapFetcher2) {
        this.bitmapFetcher = bitmapFetcher2;
        if (this.bitmapFetcher == null) {
            this.lottieAnimationView.setImageAssetDelegate((ImageAssetDelegate) null);
        } else {
            this.lottieAnimationView.setImageAssetDelegate(new ImageAssetDelegate() {
                public Bitmap fetchBitmap(LottieImageAsset lottieImageAsset) {
                    return AnimationView.this.bitmapFetcher.fetchBitmap(lottieImageAsset.getFileName());
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x00c9 A[SYNTHETIC, Splitter:B:44:0x00c9] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00d3 A[SYNTHETIC, Splitter:B:50:0x00d3] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00dd A[SYNTHETIC, Splitter:B:56:0x00dd] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00e7 A[SYNTHETIC, Splitter:B:62:0x00e7] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x00f2 A[SYNTHETIC, Splitter:B:67:0x00f2] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:59:0x00e2=Splitter:B:59:0x00e2, B:41:0x00c4=Splitter:B:41:0x00c4, B:53:0x00d8=Splitter:B:53:0x00d8, B:47:0x00ce=Splitter:B:47:0x00ce} */
    @androidx.annotation.CheckResult
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setAnimResFolder(java.lang.String r11) {
        /*
            r10 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r11)
            r1 = 0
            if (r0 != 0) goto L_0x00fb
            java.lang.String r0 = r10.lastAnimResFolder
            boolean r0 = r11.equals(r0)
            r2 = 1
            if (r0 == 0) goto L_0x0011
            return r2
        L_0x0011:
            java.lang.String r0 = java.io.File.separator
            boolean r0 = r11.endsWith(r0)
            if (r0 == 0) goto L_0x00fb
            java.io.File r0 = new java.io.File
            r0.<init>(r11)
            boolean r3 = r0.isDirectory()
            if (r3 == 0) goto L_0x00fb
            com.taobao.android.animationkit.AnimationView$2 r3 = new com.taobao.android.animationkit.AnimationView$2
            r3.<init>()
            java.io.File[] r0 = r0.listFiles(r3)
            int r3 = r0.length
            if (r3 <= 0) goto L_0x00fb
            r0 = r0[r1]
            r3 = 0
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x00e1, UnsupportedEncodingException -> 0x00d7, IOException -> 0x00cd, JSONException -> 0x00c3 }
            r4.<init>(r0)     // Catch:{ FileNotFoundException -> 0x00e1, UnsupportedEncodingException -> 0x00d7, IOException -> 0x00cd, JSONException -> 0x00c3 }
            int r0 = r4.available()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            byte[] r0 = new byte[r0]     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r4.read(r0)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.lang.String r3 = new java.lang.String     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.lang.String r5 = "UTF-8"
            r3.<init>(r0, r5)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r0.<init>(r3)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r3.<init>()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r3.append(r11)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.lang.String r5 = "images"
            r3.append(r5)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.lang.String r5 = java.io.File.separator     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r3.append(r5)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.lang.String r3 = r3.toString()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.io.File r5 = new java.io.File     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r5.<init>(r3)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            boolean r6 = r5.exists()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            if (r6 == 0) goto L_0x00a4
            boolean r5 = r5.isDirectory()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            if (r5 == 0) goto L_0x00a4
            java.util.Map<java.lang.String, android.graphics.Bitmap> r5 = r10.bitmapCache     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r5.clear()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.io.File r5 = new java.io.File     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r5.<init>(r3)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.io.File[] r3 = r5.listFiles()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            int r5 = r3.length     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r6 = 0
        L_0x0084:
            if (r6 >= r5) goto L_0x009c
            r7 = r3[r6]     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.lang.String r8 = r7.getPath()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            android.graphics.Bitmap r8 = android.graphics.BitmapFactory.decodeFile(r8)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.util.Map<java.lang.String, android.graphics.Bitmap> r9 = r10.bitmapCache     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            java.lang.String r7 = r7.getName()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r9.put(r7, r8)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            int r6 = r6 + 1
            goto L_0x0084
        L_0x009c:
            com.taobao.android.animationkit.AnimationView$3 r3 = new com.taobao.android.animationkit.AnimationView$3     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r3.<init>()     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r10.setBitmapFetcher(r3)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
        L_0x00a4:
            r10.setAnimation((org.json.JSONObject) r0)     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r10.lastAnimResFolder = r11     // Catch:{ FileNotFoundException -> 0x00bd, UnsupportedEncodingException -> 0x00ba, IOException -> 0x00b7, JSONException -> 0x00b4, all -> 0x00b2 }
            r4.close()     // Catch:{ IOException -> 0x00ad }
            goto L_0x00b1
        L_0x00ad:
            r11 = move-exception
            r11.printStackTrace()
        L_0x00b1:
            return r2
        L_0x00b2:
            r11 = move-exception
            goto L_0x00f0
        L_0x00b4:
            r11 = move-exception
            r3 = r4
            goto L_0x00c4
        L_0x00b7:
            r11 = move-exception
            r3 = r4
            goto L_0x00ce
        L_0x00ba:
            r11 = move-exception
            r3 = r4
            goto L_0x00d8
        L_0x00bd:
            r11 = move-exception
            r3 = r4
            goto L_0x00e2
        L_0x00c0:
            r11 = move-exception
            r4 = r3
            goto L_0x00f0
        L_0x00c3:
            r11 = move-exception
        L_0x00c4:
            r11.printStackTrace()     // Catch:{ all -> 0x00c0 }
            if (r3 == 0) goto L_0x00fb
            r3.close()     // Catch:{ IOException -> 0x00eb }
            goto L_0x00fb
        L_0x00cd:
            r11 = move-exception
        L_0x00ce:
            r11.printStackTrace()     // Catch:{ all -> 0x00c0 }
            if (r3 == 0) goto L_0x00fb
            r3.close()     // Catch:{ IOException -> 0x00eb }
            goto L_0x00fb
        L_0x00d7:
            r11 = move-exception
        L_0x00d8:
            r11.printStackTrace()     // Catch:{ all -> 0x00c0 }
            if (r3 == 0) goto L_0x00fb
            r3.close()     // Catch:{ IOException -> 0x00eb }
            goto L_0x00fb
        L_0x00e1:
            r11 = move-exception
        L_0x00e2:
            r11.printStackTrace()     // Catch:{ all -> 0x00c0 }
            if (r3 == 0) goto L_0x00fb
            r3.close()     // Catch:{ IOException -> 0x00eb }
            goto L_0x00fb
        L_0x00eb:
            r11 = move-exception
            r11.printStackTrace()
            goto L_0x00fb
        L_0x00f0:
            if (r4 == 0) goto L_0x00fa
            r4.close()     // Catch:{ IOException -> 0x00f6 }
            goto L_0x00fa
        L_0x00f6:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00fa:
            throw r11
        L_0x00fb:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.animationkit.AnimationView.setAnimResFolder(java.lang.String):boolean");
    }

    public void setImageAssetsFolder(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.lottieAnimationView.setImageAssetsFolder(str);
        }
    }

    public void addAnimatorUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        this.lottieAnimationView.addAnimatorUpdateListener(animatorUpdateListener);
    }

    public void removeUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        this.lottieAnimationView.removeUpdateListener(animatorUpdateListener);
    }

    public void addAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.lottieAnimationView.addAnimatorListener(animatorListener);
    }

    public void removeAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.lottieAnimationView.removeAnimatorListener(animatorListener);
    }

    public void loop(boolean z) {
        this.loop = z;
    }

    public boolean isAnimating() {
        return this.lottieAnimationView.isAnimating();
    }

    public void resumeAnimation() {
        this.lottieAnimationView.resumeAnimation();
        this.pausedOrCanceled = false;
    }

    public void playAnimation() {
        if (!TextUtils.isEmpty(this.animationName)) {
            if (checkAnimationEnable(this.animationName)) {
                this.lottieAnimationView.playAnimation();
                this.pausedOrCanceled = false;
            }
        } else if (this.animationJson != null) {
            this.lottieAnimationView.playAnimation();
            this.pausedOrCanceled = false;
        }
    }

    /* access modifiers changed from: private */
    public void playAnimationWithoutCheck() {
        this.lottieAnimationView.playAnimation();
        this.pausedOrCanceled = false;
    }

    public void cancelAnimation() {
        this.lottieAnimationView.cancelAnimation();
        this.pausedOrCanceled = true;
    }

    public void pauseAnimation() {
        this.pausedOrCanceled = true;
        this.lottieAnimationView.pauseAnimation();
    }

    private void resumeReverseAnimation() {
        this.pausedOrCanceled = false;
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        this.lottieAnimationView.setProgress(f);
    }

    public float getProgress() {
        return this.lottieAnimationView.getProgress();
    }

    public long getDuration() {
        return this.lottieAnimationView.getDuration();
    }

    public void setHardwareEnable(boolean z) {
        if (!this.lottieAnimationView.isHardwareAccelerated() || !z) {
            this.lottieAnimationView.setLayerType(1, (Paint) null);
        } else {
            this.lottieAnimationView.setLayerType(2, (Paint) null);
        }
    }

    private static class LoopRunnable implements Runnable {
        WeakReference<AnimationView> ref = null;

        public LoopRunnable(AnimationView animationView) {
            this.ref = new WeakReference<>(animationView);
        }

        public void run() {
            AnimationView animationView = (AnimationView) this.ref.get();
            if (animationView != null) {
                animationView.playAnimationWithoutCheck();
            }
        }
    }

    private static class AnimatorListener extends AnimatorListenerAdapter {
        WeakReference<AnimationView> ref = null;

        public AnimatorListener(AnimationView animationView) {
            this.ref = new WeakReference<>(animationView);
        }

        public void onAnimationCancel(Animator animator) {
            super.onAnimationCancel(animator);
            AnimationView animationView = (AnimationView) this.ref.get();
            if (animationView != null) {
                boolean unused = animationView.pausedOrCanceled = true;
            }
        }

        public void onAnimationEnd(Animator animator) {
            AnimationView animationView = (AnimationView) this.ref.get();
            if (animationView != null) {
                animationView.onLottieAnimationEnd();
            }
        }
    }

    public void setLoopDelay(long j) {
        if (j < 0) {
            j = 0;
        }
        this.loopDelay = j;
    }

    private boolean checkAnimationEnable(String str) {
        String str2;
        try {
            str2 = OrangeConfig.getInstance().getConfig("animation_kit_switch", str, "true");
        } catch (Exception e) {
            e.printStackTrace();
            str2 = "true";
        }
        boolean equals = "true".equals(str2);
        if (equals) {
            setVisibility(0);
            this.lottieAnimationView.setVisibility(0);
        } else {
            setVisibility(8);
            this.lottieAnimationView.setVisibility(8);
        }
        return equals;
    }
}
