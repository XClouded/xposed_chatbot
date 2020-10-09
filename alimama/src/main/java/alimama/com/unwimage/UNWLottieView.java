package alimama.com.unwimage;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IOrange;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;
import com.airbnb.lottie.RenderMode;

public class UNWLottieView extends FrameLayout {
    private LottieAnimationView lottieAnimationView;
    private UNWImageView tUrlImageView;

    public UNWLottieView(Context context) {
        super(context);
        initView(context);
    }

    public UNWLottieView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public UNWLottieView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
        }
    }

    private void initView(Context context) {
        try {
            this.lottieAnimationView = new LottieAnimationView(context);
        } catch (Throwable th) {
            UNWManager.getInstance().getLogger().error("Lottie", "draw", th.getMessage());
        }
        this.tUrlImageView = new UNWImageView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        this.tUrlImageView.setImageScaleType(ImageView.ScaleType.FIT_XY);
        if (this.lottieAnimationView != null) {
            this.lottieAnimationView.setScaleType(ImageView.ScaleType.FIT_XY);
            this.lottieAnimationView.setFailureListener(new LottieListener<Throwable>() {
                public void onResult(Throwable th) {
                    if (th != null) {
                        UNWManager.getInstance().getLogger().error("Lottie", "draw", th.getMessage());
                    }
                }
            });
            addView(this.lottieAnimationView, layoutParams);
        }
        addView(this.tUrlImageView, layoutParams);
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (this.lottieAnimationView != null) {
            this.lottieAnimationView.setScaleType(scaleType);
        }
        this.tUrlImageView.setImageScaleType(scaleType);
    }

    public void setImageUrl(@NonNull String str) {
        this.tUrlImageView.setAnyImageUrl(str);
        if (this.lottieAnimationView != null) {
            this.lottieAnimationView.setVisibility(8);
        }
        this.tUrlImageView.setVisibility(0);
    }

    public void setAnimUrl(@NonNull String str) {
        this.tUrlImageView.setVisibility(8);
        if (this.lottieAnimationView != null) {
            this.lottieAnimationView.setVisibility(0);
            this.lottieAnimationView.setAnimationFromUrl(str);
            this.lottieAnimationView.playAnimation();
            this.lottieAnimationView.loop(true);
        }
    }

    public void setAnimUrl(@NonNull String str, @NonNull String str2) {
        IOrange iOrange = (IOrange) UNWManager.getInstance().getService(IOrange.class);
        boolean booleanValue = iOrange != null ? Boolean.valueOf(iOrange.getConfig("UNWLottieImage", "enableLottie", "true")).booleanValue() : true;
        if (this.lottieAnimationView == null || !booleanValue) {
            this.tUrlImageView.setAnyImageUrl(str2);
            this.tUrlImageView.setVisibility(0);
            return;
        }
        this.tUrlImageView.setVisibility(8);
        this.lottieAnimationView.setVisibility(0);
        if (iOrange != null) {
            String config = iOrange.getConfig("UNWLottieImage", "floder", "");
            if (!TextUtils.isEmpty(config)) {
                this.lottieAnimationView.setImageAssetsFolder(config);
            }
        }
        this.lottieAnimationView.setAnimationFromUrl(str);
        this.lottieAnimationView.playAnimation();
        this.lottieAnimationView.loop(true);
    }

    public void play() {
        if (this.lottieAnimationView != null && this.lottieAnimationView.getVisibility() == 0 && !this.lottieAnimationView.isAnimating()) {
            this.lottieAnimationView.playAnimation();
        }
    }

    public void stop() {
        if (this.lottieAnimationView != null && this.lottieAnimationView.getVisibility() == 0 && this.lottieAnimationView.isAnimating()) {
            this.lottieAnimationView.pauseAnimation();
        }
    }

    private RenderMode getModeByOrange() {
        IOrange iOrange = (IOrange) UNWManager.getInstance().getService(IOrange.class);
        int i = Build.VERSION.SDK_INT == 24 ? 3 : 0;
        if (iOrange != null) {
            if (Build.VERSION.SDK_INT == 24) {
                i = Integer.valueOf(iOrange.getConfig("UNWLottieImage", "renderMode", "3")).intValue();
            } else {
                i = Integer.valueOf(iOrange.getConfig("UNWLottieImage", "renderMode", "0")).intValue();
            }
        }
        if (i == 0) {
            return null;
        }
        if (i == 1) {
            return RenderMode.AUTOMATIC;
        }
        if (i == 2) {
            return RenderMode.HARDWARE;
        }
        if (i == 3) {
            return RenderMode.SOFTWARE;
        }
        return null;
    }
}
