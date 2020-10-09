package com.alibaba.android.enhance.gpuimage.core;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import com.alibaba.android.WeexEnhance;
import com.alibaba.android.enhance.gpuimage.ImageFilterPlugin;
import com.alibaba.android.enhance.gpuimage.core.GLImageFilterRender;
import com.alibaba.android.enhance.gpuimage.utils.Utils;
import com.taobao.accs.common.Constants;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WXFilterModule extends WXSDKEngine.DestroyableModule {
    static final String NO_EFFECT = "raw";
    private static final String RESULT_FAILED = "failed";
    private static final String RESULT_OK = "success";
    private static final Map<String, String> SUPPORT_EFFECTS_MAP = new HashMap();

    public void destroy() {
    }

    static {
        SUPPORT_EFFECTS_MAP.put(NO_EFFECT, NO_EFFECT);
        SUPPORT_EFFECTS_MAP.put("brightness", "android.media.effect.effects.BrightnessEffect");
        SUPPORT_EFFECTS_MAP.put("contrast", "android.media.effect.effects.ContrastEffect");
        SUPPORT_EFFECTS_MAP.put("fisheye", "android.media.effect.effects.FisheyeEffect");
        SUPPORT_EFFECTS_MAP.put("backDropper", "android.media.effect.effects.BackDropperEffect");
        SUPPORT_EFFECTS_MAP.put("autoFix", "android.media.effect.effects.AutoFixEffect");
        SUPPORT_EFFECTS_MAP.put("blackWhite", "android.media.effect.effects.BlackWhiteEffect");
        SUPPORT_EFFECTS_MAP.put("crop", "android.media.effect.effects.CropEffect");
        SUPPORT_EFFECTS_MAP.put("crossProcess", "android.media.effect.effects.CrossProcessEffect");
        SUPPORT_EFFECTS_MAP.put("documentary", "android.media.effect.effects.DocumentaryEffect");
        SUPPORT_EFFECTS_MAP.put("overlay", "android.media.effect.effects.BitmapOverlayEffect");
        SUPPORT_EFFECTS_MAP.put("duotone", "android.media.effect.effects.DuotoneEffect");
        SUPPORT_EFFECTS_MAP.put("fillLight", "android.media.effect.effects.FillLightEffect");
        SUPPORT_EFFECTS_MAP.put("flip", "android.media.effect.effects.FlipEffect");
        SUPPORT_EFFECTS_MAP.put("grain", "android.media.effect.effects.GrainEffect");
        SUPPORT_EFFECTS_MAP.put("gray", "android.media.effect.effects.GrayscaleEffect");
        SUPPORT_EFFECTS_MAP.put("lomoish", "android.media.effect.effects.LomoishEffect");
        SUPPORT_EFFECTS_MAP.put("negative", "android.media.effect.effects.NegativeEffect");
        SUPPORT_EFFECTS_MAP.put("posterize", "android.media.effect.effects.PosterizeEffect");
        SUPPORT_EFFECTS_MAP.put("redEye", "android.media.effect.effects.RedEyeEffect");
        SUPPORT_EFFECTS_MAP.put("rotate", "android.media.effect.effects.RotateEffect");
        SUPPORT_EFFECTS_MAP.put("saturate", "android.media.effect.effects.SaturateEffect");
        SUPPORT_EFFECTS_MAP.put("sepia", "android.media.effect.effects.SepiaEffect");
        SUPPORT_EFFECTS_MAP.put("sharpen", "android.media.effect.effects.SharpenEffect");
        SUPPORT_EFFECTS_MAP.put("straighten", "android.media.effect.effects.StraightenEffect");
        SUPPORT_EFFECTS_MAP.put("temperature", "android.media.effect.effects.ColorTemperatureEffect");
        SUPPORT_EFFECTS_MAP.put("tint", "android.media.effect.effects.TintEffect");
        SUPPORT_EFFECTS_MAP.put("vignette", "android.media.effect.effects.VignetteEffect");
    }

    @JSMethod
    public void apply(@Nullable final Map<String, Object> map, @Nullable final JSCallback jSCallback) {
        if (map != null && !map.isEmpty() && jSCallback != null) {
            AsyncTask.execute(new Runnable() {
                public void run() {
                    Benchmark.markTaskStart();
                    WXFilterModule.this.applyAsync(map, jSCallback);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void applyAsync(@NonNull Map<String, Object> map, @NonNull JSCallback jSCallback) {
        GLImageFilterView findTarget = findTarget(WXUtils.getString(map.get(Constants.KEY_TARGET), (String) null));
        if (findTarget == null) {
            emitWith(jSCallback, "failed", "invalid params | missing target");
            return;
        }
        Bitmap allocateBitmapFrom = allocateBitmapFrom(WXUtils.getString(map.get("source"), (String) null));
        if (allocateBitmapFrom == null) {
            emitWith(jSCallback, "failed", "invalid params | bad source");
            return;
        }
        Benchmark.markBitmapProcessEnd();
        ImageFilterConfig readFilterConfig = readFilterConfig(map.get("filter"));
        if (readFilterConfig == null) {
            emitWith(jSCallback, "failed", "invalid params | missing filter");
        } else {
            applyFilterWith(findTarget, allocateBitmapFrom, readFilterConfig, jSCallback);
        }
    }

    private void applyFilterWith(@NonNull GLImageFilterView gLImageFilterView, @NonNull Bitmap bitmap, @NonNull ImageFilterConfig imageFilterConfig, @NonNull final JSCallback jSCallback) {
        if (gLImageFilterView.isFilterValid(imageFilterConfig)) {
            gLImageFilterView.applyFilterToBitmap(imageFilterConfig, bitmap, new GLImageFilterRender.FilterCallback() {
                public void onSuccess() {
                    WXFilterModule.this.emitWith(jSCallback, "success", (String) null);
                }

                public void onFailed(String str) {
                    WXFilterModule.this.emitWith(jSCallback, "failed", str);
                }
            });
        } else {
            emitWith(jSCallback, "failed", "invalid params | unknown filter");
        }
    }

    @Nullable
    private ImageFilterConfig readFilterConfig(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Map)) {
            return null;
        }
        Map map = (Map) obj;
        String string = WXUtils.getString(map.get("name"), (String) null);
        Object obj2 = map.get("props");
        ImageFilterConfig imageFilterConfig = new ImageFilterConfig();
        imageFilterConfig.name = resolveRealEffectName(string);
        imageFilterConfig.props = obj2 instanceof Map ? (Map) obj2 : Collections.emptyMap();
        return imageFilterConfig;
    }

    @Nullable
    private String resolveRealEffectName(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return SUPPORT_EFFECTS_MAP.get(str);
    }

    @Nullable
    private GLImageFilterView findTarget(@Nullable String str) {
        if (TextUtils.isEmpty(str) || this.mWXSDKInstance == null) {
            return null;
        }
        View findViewByRef = findViewByRef(this.mWXSDKInstance.getInstanceId(), str);
        if (findViewByRef instanceof GLImageFilterView) {
            return (GLImageFilterView) findViewByRef;
        }
        return null;
    }

    @Nullable
    private Bitmap allocateBitmapFrom(@Nullable String str) {
        if (TextUtils.isEmpty(str) || this.mWXSDKInstance == null) {
            return null;
        }
        if (!isImageURI(str)) {
            return Utils.allocateBitmapFromView(findViewByRef(this.mWXSDKInstance.getInstanceId(), str));
        }
        WeexEnhance.ImageLoadAdapter imageAdapter = ImageFilterPlugin.getImageAdapter();
        if (imageAdapter == null) {
            return null;
        }
        return imageAdapter.fetchBitmapSync(str);
    }

    private boolean isImageURI(@NonNull String str) {
        return str.startsWith("http") || str.startsWith("//");
    }

    @Nullable
    private View findViewByRef(@Nullable String str, @NonNull String str2) {
        WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(str, str2);
        if (wXComponent == null) {
            return null;
        }
        return wXComponent.getHostView();
    }

    /* access modifiers changed from: private */
    public void emitWith(@NonNull JSCallback jSCallback, @NonNull String str, @Nullable String str2) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("result", str);
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("message", str2);
        }
        jSCallback.invoke(hashMap);
    }
}
