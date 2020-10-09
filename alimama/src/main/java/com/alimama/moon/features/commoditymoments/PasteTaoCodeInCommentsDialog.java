package com.alimama.moon.features.commoditymoments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.DialogUTHelper;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.union.app.share.flutter.ShareChannelClickHandler;

public class PasteTaoCodeInCommentsDialog extends Dialog implements View.OnClickListener {
    private RelativeLayout mCopyTaoCodeSuccessRl;
    private DialogType mDialogType;
    private final ShareChannelClickHandler mShareChannelHandler;
    private String mTaoCodeStr;
    private TextView mTaoCodeTv;

    public enum DialogType {
        Click_Copy_Taocode_Btn,
        Jump_To_WeChat_Back
    }

    private PasteTaoCodeInCommentsDialog(Context context, DialogType dialogType, String str) {
        super(context, R.style.common_dialog_style);
        this.mDialogType = dialogType;
        this.mTaoCodeStr = str;
        this.mShareChannelHandler = new ShareChannelClickHandler(context);
    }

    public static void showPasteTaoCodeInCommentsDialog(Context context, DialogType dialogType, String str) {
        new PasteTaoCodeInCommentsDialog(context, dialogType, str).show();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.paste_taocode_in_comments_dialog_layout);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(false);
        this.mTaoCodeTv = (TextView) findViewById(R.id.tao_code_tv);
        this.mCopyTaoCodeSuccessRl = (RelativeLayout) findViewById(R.id.copy_taocode_success_rl);
        findViewById(R.id.go_to_wechat_comments_ll).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        if (this.mDialogType == DialogType.Click_Copy_Taocode_Btn) {
            this.mTaoCodeTv.setVisibility(8);
            this.mCopyTaoCodeSuccessRl.setVisibility(0);
            CommonUtils.copyToClipboard(this.mTaoCodeStr);
        } else if (this.mDialogType == DialogType.Jump_To_WeChat_Back) {
            this.mTaoCodeTv.setVisibility(8);
            setTaoCodeText(this.mTaoCodeStr);
            this.mCopyTaoCodeSuccessRl.setVisibility(8);
        }
    }

    private void setTaoCodeText(String str) {
        this.mTaoCodeTv.setText(str);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_close) {
            if (id == R.id.go_to_wechat_comments_ll) {
                this.mShareChannelHandler.shareToWechat();
                CommonUtils.copyToClipboard(this.mTaoCodeStr);
                if (this.mDialogType == DialogType.Click_Copy_Taocode_Btn) {
                    DialogUTHelper.clickDialog(DialogUTHelper.PASTE_WECHAT_COMMENTS_CONTROL_NAME);
                } else if (this.mDialogType == DialogType.Jump_To_WeChat_Back) {
                    DialogUTHelper.clickDialog(DialogUTHelper.PASTE_WECHAT_COMMENTS_AGAIN_CONTROL_NAME);
                }
            }
        } else if (this.mDialogType == DialogType.Click_Copy_Taocode_Btn) {
            DialogUTHelper.clickDialog(DialogUTHelper.CLOSE_PASTE_WECHAT_COMMENTS_DIALOG);
        } else if (this.mDialogType == DialogType.Jump_To_WeChat_Back) {
            DialogUTHelper.clickDialog(DialogUTHelper.CLOSE_PASTE_WECHAT_COMMENTS_AGAIN_DIALOG);
        }
        dismiss();
    }
}
