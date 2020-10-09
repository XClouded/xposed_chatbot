package com.alimama.union.app.privacy;

import android.content.Context;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.privacy.PrivacyDialog;

public class PrivacyPermissionManager {
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public PermissionInterface mPermissionInterface;
    /* access modifiers changed from: private */
    public PrivacyInterface mPrivacyInterface;

    public PrivacyPermissionManager(Context context, PrivacyInterface privacyInterface) {
        this.mContext = context;
        this.mPrivacyInterface = privacyInterface;
    }

    public PrivacyPermissionManager(Context context, PermissionInterface permissionInterface) {
        this.mContext = context;
        this.mPermissionInterface = permissionInterface;
    }

    public void initPrivacyDialog() {
        new PrivacyDialog(this.mContext, getFirstDialogConfig(), 0, PrivacyDialog.BottomBarType.TwoButton).show();
    }

    public void showReadWritePermissionDialog() {
        new PrivacyDialog(this.mContext, getPermissionDialogConfig(), 0, PrivacyDialog.BottomBarType.OneButton).show();
    }

    public void showReadContactPermissionDialog() {
        new PrivacyDialog(this.mContext, getReadContactPermissionDialogConfig(), 0, PrivacyDialog.BottomBarType.OneButton).show();
    }

    private PrivacyConfig getReadContactPermissionDialogConfig() {
        PrivacyConfig privacyConfig = new PrivacyConfig();
        privacyConfig.setTitle(this.mContext.getString(R.string.permission_read_contact_title)).setContent(this.mContext.getString(R.string.permission_read_contact_content)).setVertical(false).setCenterBtnText(this.mContext.getString(R.string.center_btn_text_open)).setCenterBtnCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("read_contact_dialog_agree");
                PrivacyPermissionManager.this.mPermissionInterface.openPermissionRequest();
            }
        }).setTopRightCloseBtnCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("read_contact_dialog_disagree");
                PrivacyPermissionManager.this.mPermissionInterface.closePermissionRequest();
            }
        });
        return privacyConfig;
    }

    private PrivacyConfig getPermissionDialogConfig() {
        PrivacyConfig privacyConfig = new PrivacyConfig();
        privacyConfig.setTitle(this.mContext.getString(R.string.permission_read_write_title)).setContent(this.mContext.getString(R.string.permission_read_write_content)).setVertical(false).setCenterBtnText(this.mContext.getString(R.string.center_btn_text_open)).setCenterBtnCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("photo_dialog_agree");
                PrivacyPermissionManager.this.mPermissionInterface.openPermissionRequest();
            }
        }).setTopRightCloseBtnCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("photo_dialog_disagree");
                PrivacyPermissionManager.this.mPermissionInterface.closePermissionRequest();
            }
        });
        return privacyConfig;
    }

    private PrivacyConfig getFirstDialogConfig() {
        PrivacyConfig privacyConfig = new PrivacyConfig();
        privacyConfig.setTitle(this.mContext.getString(R.string.privacy_title1)).setContent(this.mContext.getString(R.string.privacy_content1)).setSpanStr("《隐私权政策》").setLeftText("不同意").setRightText("同意").setVertical(false).setJumpUrl(PrivacyUtil.PRIVACY_DETAIL_JUMP_URL).setLeftCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("first_dialog_disagree");
                new PrivacyDialog(PrivacyPermissionManager.this.mContext, PrivacyPermissionManager.this.getSecondDialogConfig(), 1, PrivacyDialog.BottomBarType.TwoButton).show();
            }
        }).setRightCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("first_dialog_agree");
                PrivacyUtil.setPrivacyDialogAppeared(PrivacyPermissionManager.this.mContext);
                PrivacyPermissionManager.this.mPrivacyInterface.goToNextPage();
            }
        });
        return privacyConfig;
    }

    /* access modifiers changed from: private */
    public PrivacyConfig getSecondDialogConfig() {
        PrivacyConfig privacyConfig = new PrivacyConfig();
        privacyConfig.setTitle(this.mContext.getString(R.string.privacy_title2)).setContent(this.mContext.getString(R.string.privacy_content2)).setSpanStr("《隐私权政策》").setLeftText("仍不同意").setRightText("同意").setVertical(false).setJumpUrl(PrivacyUtil.PRIVACY_DETAIL_JUMP_URL).setLeftCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("second_dialog_disagree");
                new PrivacyDialog(PrivacyPermissionManager.this.mContext, PrivacyPermissionManager.this.getThirdDialogConfig(), 2, PrivacyDialog.BottomBarType.TwoButton).show();
            }
        }).setRightCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("second_dialog_agree");
                PrivacyUtil.setPrivacyDialogAppeared(PrivacyPermissionManager.this.mContext);
                PrivacyPermissionManager.this.mPrivacyInterface.goToNextPage();
            }
        });
        return privacyConfig;
    }

    /* access modifiers changed from: private */
    public PrivacyConfig getThirdDialogConfig() {
        PrivacyConfig privacyConfig = new PrivacyConfig();
        privacyConfig.setTitle(this.mContext.getString(R.string.privacy_title3)).setContent(this.mContext.getString(R.string.privacy_content3)).setSpanStr("《隐私权政策》").setLeftText("退出应用").setRightText("同意").setVertical(false).setJumpUrl(PrivacyUtil.PRIVACY_DETAIL_JUMP_URL).setLeftCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("third_dialog_disagree");
                MoonComponentManager.getInstance().getPageRouter().finishAll();
            }
        }).setRightCallback(new PrivacyDialog.PrivacyCallBack() {
            public void callback() {
                UTHelper.PrivacyAndPermissionDialog.clickPrivacyDialog("third_dialog_agree");
                PrivacyUtil.setPrivacyDialogAppeared(PrivacyPermissionManager.this.mContext);
                PrivacyPermissionManager.this.mPrivacyInterface.goToNextPage();
            }
        });
        return privacyConfig;
    }
}
