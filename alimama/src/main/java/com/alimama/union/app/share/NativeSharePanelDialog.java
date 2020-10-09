package com.alimama.union.app.share;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.share.EtaoShare;
import com.alimama.share.SocialCamp;
import com.alimama.share.beans.Platform;
import com.alimama.share.beans.ShareImageAction;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import java.util.ArrayList;
import java.util.List;

public class NativeSharePanelDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private List<Uri> mImageList;
    private View mShareMomentsView;
    private boolean mSystemShare = false;
    private String mText;

    public NativeSharePanelDialog(@NonNull Context context, String str, List<Uri> list, boolean z) {
        super(context);
        SocialCamp.getInstance().init(context);
        this.mContext = context;
        this.mText = str;
        this.mImageList = list;
        this.mSystemShare = z;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().requestFeature(1);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.share_panel_layout, (ViewGroup) null);
        inflate.findViewById(R.id.ll_share_wechat).setOnClickListener(this);
        inflate.findViewById(R.id.ll_share_qq).setOnClickListener(this);
        inflate.findViewById(R.id.ll_share_sina).setOnClickListener(this);
        inflate.findViewById(R.id.share_dialog_blank).setOnClickListener(this);
        this.mShareMomentsView = inflate.findViewById(R.id.ll_share_moments);
        if (this.mSystemShare) {
            this.mShareMomentsView.setVisibility(0);
            this.mShareMomentsView.setOnClickListener(this);
        }
        setCancelable(false);
        setContentView(inflate);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(-1, -1);
        inflate.findViewById(R.id.btn_close).setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_close) {
            if (id != R.id.ll_share_moments) {
                switch (id) {
                    case R.id.ll_share_wechat:
                        shareToWechat();
                        break;
                    case R.id.ll_share_sina:
                        if (!ShareUtils.shareWeibo(this.mContext, this.mText, (ArrayList) this.mImageList)) {
                            ToastUtil.showToast(getContext(), (int) R.string.weibo_not_installed);
                        }
                        UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_IMG_SHARE_TO_WEIBO);
                        break;
                    case R.id.ll_share_qq:
                        shareToQQ();
                        break;
                }
            } else {
                shareToWechatMoments();
            }
        }
        dismiss();
    }

    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    private void shareToWechat() {
        if (!this.mSystemShare) {
            if (!ShareUtils.openWechat(getContext())) {
                ToastUtil.showToast(getContext(), (int) R.string.wechat_not_installed);
            }
            UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_IMG_SHARE_TO_WECHAT);
        } else if (!ShareUtils.isWechatInstalled(this.mContext)) {
            ToastUtil.showToast(getContext(), (int) R.string.wechat_not_installed);
        } else {
            ShareImageAction shareImageAction = new ShareImageAction(Platform.WEIXIN);
            shareImageAction.withUri(this.mImageList.get(0));
            shareImageAction.isCirCle(false);
            EtaoShare.getInstance().exec(shareImageAction);
        }
    }

    private void shareToWechatMoments() {
        if (this.mSystemShare) {
            if (!ShareUtils.isWechatInstalled(this.mContext)) {
                ToastUtil.showToast(getContext(), (int) R.string.wechat_not_installed);
                return;
            }
            ShareImageAction shareImageAction = new ShareImageAction(Platform.WEIXIN);
            shareImageAction.withUri(this.mImageList.get(0));
            shareImageAction.isCirCle(true);
            EtaoShare.getInstance().exec(shareImageAction);
        }
    }

    private void shareToQQ() {
        if (!this.mSystemShare) {
            if (!ShareUtils.openQq(getContext())) {
                ToastUtil.showToast(getContext(), (int) R.string.qq_not_installed);
            }
            UTHelper.sendControlHit("union/union_share", UTHelper.SPM_CLICK_IMG_SHARE_TO_QQ);
        } else if (!ShareUtils.isQqInstalled(this.mContext)) {
            ToastUtil.showToast(getContext(), (int) R.string.qq_not_installed);
        } else {
            ShareImageAction shareImageAction = new ShareImageAction(Platform.QQ);
            shareImageAction.withUri(this.mImageList.get(0));
            shareImageAction.isCirCle(false);
            EtaoShare.getInstance().exec(shareImageAction);
        }
    }
}
