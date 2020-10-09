package com.alimama.union.app.share.flutter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.share.SocialCamp;
import java.util.ArrayList;
import java.util.List;

public class SharePosterChannelDialog extends Dialog implements View.OnClickListener {
    private final ArrayList<Uri> mLocalUris;
    private final ShareChannelClickHandler mShareChannelHandler;
    private boolean mSystemShare = false;

    public SharePosterChannelDialog(@NonNull Context context, List<Uri> list) {
        super(context, R.style.common_dialog_style);
        SocialCamp.getInstance().init(context);
        this.mLocalUris = new ArrayList<>();
        this.mLocalUris.addAll(list);
        this.mShareChannelHandler = new ShareChannelClickHandler(context);
    }

    public static void inAppShare(Activity activity, List<Uri> list) {
        if (!activity.isFinishing()) {
            new SharePosterChannelDialog(activity, list).show();
        }
    }

    public static void immediateShare(Activity activity, List<Uri> list) {
        if (!activity.isFinishing()) {
            SharePosterChannelDialog sharePosterChannelDialog = new SharePosterChannelDialog(activity, list);
            sharePosterChannelDialog.mSystemShare = true;
            sharePosterChannelDialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.new_share_panel_layout);
        ((TextView) findViewById(R.id.tv_panel_title)).setText(R.string.save_poster);
        TextView textView = (TextView) findViewById(R.id.tv_result);
        View findViewById = findViewById(R.id.share_wechat_moments);
        findViewById.setOnClickListener(this);
        findViewById(R.id.share_wechat).setOnClickListener(this);
        findViewById(R.id.share_qq).setOnClickListener(this);
        findViewById(R.id.share_weibo).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_saved_checkmark), (Drawable) null, (Drawable) null, (Drawable) null);
        textView.setCompoundDrawablePadding(getContext().getResources().getDimensionPixelOffset(R.dimen.common_padding_3));
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.share_content_saved_text_color));
        textView.setText(R.string.save_success);
        ((TextView) findViewById(R.id.tv_panel_content)).setText(R.string.share_save_images_panel_content);
        if (!this.mSystemShare) {
            findViewById.setVisibility(8);
            findViewById(R.id.wechat_moments_spacer).setVisibility(8);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_close) {
            if (id != R.id.share_wechat_moments) {
                switch (id) {
                    case R.id.share_wechat:
                        if (!this.mSystemShare) {
                            UTHelper.SharePosterPage.clickWechatShare();
                            this.mShareChannelHandler.shareToWechat();
                            break;
                        } else {
                            UTHelper.SharePosterPage.clickWechatSysShare();
                            this.mShareChannelHandler.immediateShareToWechat(this.mLocalUris);
                            break;
                        }
                    case R.id.share_qq:
                        if (!this.mSystemShare) {
                            UTHelper.SharePosterPage.clickQqShare();
                            this.mShareChannelHandler.shareToQQ();
                            break;
                        } else {
                            UTHelper.SharePosterPage.clickQqSysShare();
                            this.mShareChannelHandler.immediateShareToQq(this.mLocalUris);
                            break;
                        }
                    case R.id.share_weibo:
                        if (this.mSystemShare) {
                            UTHelper.SharePosterPage.clickWeiboSysShare();
                        } else {
                            UTHelper.SharePosterPage.clickWeiboShare();
                        }
                        this.mShareChannelHandler.shareToWeibo(CommonUtils.getClipboardContent(), this.mLocalUris);
                        break;
                }
            } else {
                UTHelper.SharePosterPage.clickWechatMoments();
                this.mShareChannelHandler.shareToWechatMoments(this.mLocalUris);
            }
        }
        dismiss();
    }

    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
