package alimama.com.unwlottiedialog;

import alimama.com.unwbase.tools.ScaleUtils;
import alimama.com.unwimage.UNWLottieView;
import alimama.com.unwviewbase.abstractview.UNWAbstractDialog;
import alimama.com.unwweex.etaovessel.UNWVesselView;
import alimama.com.unwweex.etaovessel.VesselViewCallBack;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class UNWLottieDialog extends UNWAbstractDialog implements DialogInterface.OnShowListener, View.OnClickListener, Animator.AnimatorListener {
    /* access modifiers changed from: private */
    public LottieDialogCallback callback;
    private UNWVesselView.Icreater icreater;
    private LottieData lottieData;
    private ArrayList<Animator> mAnimators;
    ImageView mCloseView;
    private Context mContext;
    public UNWLottieView mImageView;
    public View mLayout;
    private View mTopView;
    private Window mWindow;
    public View rootView;
    private UNWVesselView vesselView;
    private VesselViewCallBack vesselViewListener;
    private ViewGroup viewGroup;

    public void onAnimationCancel(Animator animator) {
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public UNWLottieDialog(@NonNull Context context, @NonNull LottieData lottieData2, @Nullable LottieDialogCallback lottieDialogCallback) {
        this(context, lottieData2, lottieDialogCallback, (VesselViewCallBack) null);
    }

    public UNWLottieDialog(@NonNull Context context, @NonNull LottieData lottieData2, @Nullable LottieDialogCallback lottieDialogCallback, VesselViewCallBack vesselViewCallBack) {
        this(context, lottieData2, lottieDialogCallback, vesselViewCallBack, (UNWVesselView.Icreater) null);
    }

    public UNWLottieDialog(@NonNull Context context, @NonNull LottieData lottieData2, @Nullable LottieDialogCallback lottieDialogCallback, VesselViewCallBack vesselViewCallBack, UNWVesselView.Icreater icreater2) {
        super(context, R.style.UNWDialogStyle);
        this.lottieData = lottieData2;
        this.callback = lottieDialogCallback;
        this.mContext = context;
        this.vesselViewListener = vesselViewCallBack;
        this.icreater = icreater2;
        initView();
    }

    public void initView() {
        if (this.lottieData != null) {
            this.rootView = LayoutInflater.from(this.mContext).inflate(R.layout.unw_lottie_dialog, (ViewGroup) null);
            this.mCloseView = (ImageView) this.rootView.findViewById(R.id.home_market_close);
            this.mImageView = (UNWLottieView) this.rootView.findViewById(R.id.home_market_image);
            this.mLayout = this.rootView.findViewById(R.id.home_market_close_layout);
            this.viewGroup = (ViewGroup) this.rootView.findViewById(R.id.dialog_container);
            this.vesselView = (UNWVesselView) this.rootView.findViewById(R.id.unw_dialog_vessel);
            if (!(this.vesselView == null || this.icreater == null)) {
                this.vesselView.setCreater(this.icreater);
            }
            if (!(this.vesselView == null || this.vesselViewListener == null)) {
                this.vesselView.setCallBack(this.vesselViewListener);
            }
            this.mTopView = this.rootView;
            if (this.lottieData.close_res == 0) {
                this.lottieData.close_res = R.drawable.icon_close;
            }
            this.mCloseView.setImageResource(this.lottieData.close_res);
            this.mLayout.setOnClickListener(this);
            setContentView(this.rootView);
            this.mTopView.setVisibility(4);
            int i = ScaleUtils.getDisplayMetrics(this.mContext).widthPixels;
            if (this.lottieData.width == 0 || this.lottieData.height == 0) {
                this.mImageView.getLayoutParams().width = i;
            } else {
                this.mImageView.getLayoutParams().height = (this.lottieData.height * i) / this.lottieData.width;
            }
            if (!TextUtils.isEmpty(this.lottieData.lottie_url)) {
                this.vesselView.setVisibility(8);
                this.mImageView.setVisibility(0);
                this.mImageView.setAnimUrl(this.lottieData.lottie_url, this.lottieData.img);
            } else if (!TextUtils.isEmpty(this.lottieData.img)) {
                this.vesselView.setVisibility(8);
                this.mImageView.setVisibility(0);
                this.mImageView.setImageUrl(this.lottieData.img);
            } else if (!TextUtils.isEmpty(this.lottieData.other_url)) {
                this.mImageView.setVisibility(8);
                this.vesselView.setVisibility(0);
                ViewGroup.LayoutParams layoutParams = this.vesselView.getLayoutParams();
                if (this.lottieData.width == 0 || this.lottieData.height == 0) {
                    layoutParams.width = i;
                } else {
                    layoutParams.height = (i * this.lottieData.height) / this.lottieData.width;
                }
                this.vesselView.setLayoutParams(layoutParams);
                this.vesselView.loadUrl(this.lottieData.other_url);
            }
            if (!this.lottieData.isShowCloseBtn) {
                this.mCloseView.setVisibility(8);
            }
            setCanceledOnTouchOutside(false);
            setOnShowListener(this);
            this.mWindow = getWindow();
            if (this.mWindow != null) {
                this.mWindow.setWindowAnimations(R.style.UNWDiaAnimStyle);
                this.mWindow.setLayout(-1, -2);
            }
            this.mCloseView.setVisibility(4);
            this.rootView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (UNWLottieDialog.this.callback != null && UNWLottieDialog.this.callback.clickBg()) {
                        UNWLottieDialog.this.dismiss();
                    }
                }
            });
            this.mImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (UNWLottieDialog.this.callback.clickContent()) {
                        UNWLottieDialog.this.dismiss();
                    }
                }
            });
        }
    }

    public void onShow(DialogInterface dialogInterface) {
        showAnimation();
        if (this.callback != null) {
            this.callback.startShow();
        }
    }

    public void onClick(View view) {
        if (this.callback != null && this.callback.clickClose()) {
            dismiss();
        }
    }

    private void showAnimation() {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this.mContext, R.animator.unw_lottie_dialog_anim);
        animatorSet.setTarget(this.mTopView);
        this.mAnimators = animatorSet.getChildAnimations();
        if (this.mAnimators.size() > 0) {
            this.mAnimators.get(0).addListener(this);
        }
        this.mAnimators.get(this.mAnimators.size() - 1).addListener(this);
        animatorSet.setStartDelay(1000);
        animatorSet.start();
    }

    private void showClose() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(300);
        this.mCloseView.setVisibility(0);
        this.mCloseView.startAnimation(scaleAnimation);
    }

    public void onAnimationStart(Animator animator) {
        if (this.mAnimators != null && this.mAnimators.size() > 0 && animator == this.mAnimators.get(0)) {
            this.mTopView.setVisibility(0);
        }
    }

    public void onAnimationEnd(Animator animator) {
        if (this.mAnimators != null && animator == this.mAnimators.get(this.mAnimators.size() - 1)) {
            showClose();
        }
    }
}
