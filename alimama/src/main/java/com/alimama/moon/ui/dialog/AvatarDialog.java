package com.alimama.moon.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.DialogUTHelper;
import com.alimama.moon.windvane.jsbridge.model.ShowAvatarDialogModel;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.utils.LocalDisplay;

public class AvatarDialog extends Dialog implements View.OnClickListener {
    private EtaoDraweeView mAvatarImg;
    private String mAvatarUrl;
    private BottomBarType mBottomBarType;
    private WVCallBackContext mCallBackContext;
    private TextView mCancel;
    private String mCancelBtnTitle;
    private TextView mConfirm;
    private String mContent;
    private TextView mContentTv;
    private Context mContext;
    private TextView mGoToInvite;
    private String mOkBtnTitle;
    private LinearLayout mOneBtnLl;
    private String mRawUrl;
    private String mTitle;
    private TextView mTitleTv;
    private LinearLayout mTwoBtnLl;

    public enum BottomBarType {
        TwoButton,
        OneButton
    }

    public static void showAvatarDialog(Context context, String str, String str2, String str3, String str4) {
        new AvatarDialog(context, str, str2, str3, str4, "", "", BottomBarType.OneButton);
    }

    public static void showAvatarDialog(Context context, ShowAvatarDialogModel showAvatarDialogModel, WVCallBackContext wVCallBackContext) {
        new AvatarDialog(context, showAvatarDialogModel.getTitle(), showAvatarDialogModel.getMsg(), showAvatarDialogModel.getPictUrl(), "", showAvatarDialogModel.getCancelBtnTitle(), showAvatarDialogModel.getOkBtnTitle(), BottomBarType.TwoButton).setWVCallBackContext(wVCallBackContext);
    }

    private void setWVCallBackContext(WVCallBackContext wVCallBackContext) {
        this.mCallBackContext = wVCallBackContext;
    }

    private AvatarDialog(Context context, String str, String str2, String str3, String str4, String str5, String str6, BottomBarType bottomBarType) {
        super(context, R.style.common_dialog_style);
        this.mContext = context;
        this.mTitle = str;
        this.mContent = str2;
        this.mAvatarUrl = str3;
        this.mRawUrl = str4;
        this.mCancelBtnTitle = str5;
        this.mOkBtnTitle = str6;
        this.mBottomBarType = bottomBarType;
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.y = -LocalDisplay.dp2px(80.0f);
        window.setAttributes(attributes);
        show();
        window.setGravity(17);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.avatar_dialog_layout);
        initView();
        initData();
    }

    private void initView() {
        this.mAvatarImg = (EtaoDraweeView) findViewById(R.id.avatar_img);
        this.mTitleTv = (TextView) findViewById(R.id.title_tv);
        this.mContentTv = (TextView) findViewById(R.id.content);
        this.mGoToInvite = (TextView) findViewById(R.id.go_to_invite);
        this.mGoToInvite.setOnClickListener(this);
        this.mCancel = (TextView) findViewById(R.id.cancel_btn);
        this.mCancel.setOnClickListener(this);
        this.mConfirm = (TextView) findViewById(R.id.confirm_btn);
        this.mConfirm.setOnClickListener(this);
        this.mOneBtnLl = (LinearLayout) findViewById(R.id.one_btn_ll);
        this.mTwoBtnLl = (LinearLayout) findViewById(R.id.two_btn_ll);
        findViewById(R.id.avatar_btn_close).setOnClickListener(this);
    }

    private void initData() {
        setCanceledOnTouchOutside(false);
        this.mAvatarImg.setAnyImageUrl(this.mAvatarUrl);
        this.mTitleTv.setText(this.mTitle);
        this.mContentTv.setText(this.mContent);
        if (this.mBottomBarType == BottomBarType.OneButton) {
            this.mOneBtnLl.setVisibility(0);
            this.mTwoBtnLl.setVisibility(8);
        } else if (this.mBottomBarType == BottomBarType.TwoButton) {
            this.mOneBtnLl.setVisibility(8);
            this.mTwoBtnLl.setVisibility(0);
            this.mCancel.setText(this.mCancelBtnTitle);
            this.mConfirm.setText(this.mOkBtnTitle);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_to_invite:
                MoonComponentManager.getInstance().getPageRouter().gotoPage(this.mRawUrl);
                DialogUTHelper.clickDialog(DialogUTHelper.AVATAR_DIALOG_PAGE_NAME, DialogUTHelper.GO_TO_INVITE_CONTROL_DNAME);
                break;
            case R.id.cancel_btn:
                if (this.mCallBackContext != null) {
                    this.mCallBackContext.error(new WVResult("HY_FAILED"));
                    break;
                }
                break;
            case R.id.confirm_btn:
                if (this.mCallBackContext != null) {
                    this.mCallBackContext.success(WVResult.SUCCESS);
                    break;
                }
                break;
            case R.id.avatar_btn_close:
                dismiss();
                DialogUTHelper.clickDialog(DialogUTHelper.AVATAR_DIALOG_PAGE_NAME, DialogUTHelper.CLICK_DIALOG_CLOSE_CONTROL_NAME);
                break;
        }
        dismiss();
    }
}
