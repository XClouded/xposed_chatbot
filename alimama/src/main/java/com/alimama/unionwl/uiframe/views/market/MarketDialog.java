package com.alimama.unionwl.uiframe.views.market;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.base.jumpcenter.PageRouter;
import com.alimama.unionwl.uiframe.R;
import com.alimama.unionwl.uiframe.views.dialog.ISDialog;
import com.alimama.unionwl.utils.LocalDisplay;
import java.util.ArrayList;

public class MarketDialog extends ISDialog implements DialogInterface.OnShowListener, Animator.AnimatorListener, View.OnClickListener {
    private static final int MATCH_PARENT = -1;
    private static final int WRAP_COTENT = -2;
    private int height;
    private ArrayList<Animator> mAnimators;
    ImageView mCloseView;
    private Context mContext;
    EtaoDraweeView mImageView;
    private String mImg;
    private View mLayout;
    private View mTopView;
    /* access modifiers changed from: private */
    public String mUrl;
    private Window mWindow;
    /* access modifiers changed from: private */
    public MarketDialogInterceptor marketDialogInterceptor;
    private int width;

    public void onAnimationCancel(Animator animator) {
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public MarketDialog(Context context, String str, String str2, int i, int i2, MarketDialogInterceptor marketDialogInterceptor2) {
        this(context, R.style.ShareDialogStyle, str, str2, i, i2, marketDialogInterceptor2);
    }

    public MarketDialog(Context context, int i, String str, String str2, int i2, int i3, MarketDialogInterceptor marketDialogInterceptor2) {
        super(context, i);
        this.mContext = context;
        this.mImg = str;
        this.mUrl = str2;
        this.width = i2;
        this.height = i3;
        this.marketDialogInterceptor = marketDialogInterceptor2;
        initView();
    }

    public static void doShow(Context context, String str, String str2, int i, int i2, MarketDialogInterceptor marketDialogInterceptor2) {
        new MarketDialog(context, str, str2, i, i2, marketDialogInterceptor2).show();
    }

    public void initView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_market, (ViewGroup) null);
        this.mCloseView = (ImageView) inflate.findViewById(R.id.home_market_close);
        this.mImageView = (EtaoDraweeView) inflate.findViewById(R.id.home_market_image);
        this.mLayout = inflate.findViewById(R.id.home_market_close_layout);
        this.mTopView = inflate;
        this.mLayout.setOnClickListener(this);
        setContentView(inflate);
        this.mTopView.setVisibility(4);
        int i = LocalDisplay.SCREEN_WIDTH_PIXELS;
        if (this.width == 0 || this.height == 0) {
            this.mImageView.getLayoutParams().height = i;
        } else {
            this.mImageView.getLayoutParams().height = (i * this.height) / this.width;
        }
        this.mImageView.setImageURI(Uri.parse(this.mImg));
        setCanceledOnTouchOutside(false);
        setOnShowListener(this);
        this.mWindow = getWindow();
        this.mWindow.setWindowAnimations(R.style.HomeMarketDiaAnimStyle);
        this.mWindow.setLayout(-1, -2);
        inflate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MarketDialog.this.marketDialogInterceptor != null) {
                    MarketDialog.this.marketDialogInterceptor.onImageClickIntercept(view);
                }
                PageRouter.getInstance().gotoPage(MarketDialog.this.mUrl);
                if (MarketDialog.this.isShowing()) {
                    MarketDialog.this.dismiss();
                }
            }
        });
    }

    public void onShow(DialogInterface dialogInterface) {
        showAnimation();
        if (this.marketDialogInterceptor != null) {
            this.marketDialogInterceptor.onShowIntercept();
        }
    }

    private void showAnimation() {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this.mContext, R.animator.market_dialog_show_anim);
        animatorSet.setTarget(this.mTopView);
        this.mAnimators = animatorSet.getChildAnimations();
        this.mAnimators.get(0).addListener(this);
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
        if (this.mAnimators != null && animator == this.mAnimators.get(0)) {
            this.mTopView.setVisibility(0);
        }
    }

    public void onAnimationEnd(Animator animator) {
        if (this.mAnimators != null && animator == this.mAnimators.get(this.mAnimators.size() - 1)) {
            showClose();
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.marketDialogInterceptor != null) {
            this.marketDialogInterceptor.onDismissIntercept();
        }
    }

    public void onClick(View view) {
        if (view == this.mLayout) {
            dismiss();
        }
    }
}
