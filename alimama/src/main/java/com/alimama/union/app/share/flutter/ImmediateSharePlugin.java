package com.alimama.union.app.share.flutter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import com.alimama.moon.R;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.union.app.infrastructure.image.download.StoragePermissionValidator;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import com.alimama.union.app.views.AlertMsgDialog;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import java.util.List;

public class ImmediateSharePlugin implements MethodChannel.MethodCallHandler, PermissionInterface {
    private Activity mActivity;
    private final StoragePermissionValidator permissionValidator = new StoragePermissionValidator();
    private PrivacyPermissionManager privacyPermissionManager = new PrivacyPermissionManager((Context) this.mActivity, (PermissionInterface) this);

    public void closePermissionRequest() {
    }

    public static void register(Activity activity, BinaryMessenger binaryMessenger) {
        new MethodChannel(binaryMessenger, "com.alimama.moon/immediateShare").setMethodCallHandler(new ImmediateSharePlugin(activity));
    }

    public ImmediateSharePlugin(Activity activity) {
        this.mActivity = activity;
    }

    public void openPermissionRequest() {
        this.permissionValidator.checkRequiredPermission(this.mActivity);
    }

    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        if ("share".equals(methodCall.method)) {
            List list = (List) methodCall.argument("imgs");
            CommonUtils.copyToClipboard((String) methodCall.argument("shared_text"));
            if (!PrivacyUtil.hasWriteExternalStorage(this.mActivity)) {
                this.privacyPermissionManager.showReadWritePermissionDialog();
            } else if (ShareUtils.isFirstTimeUsingSysShare(this.mActivity)) {
                ShareUtils.markUsedSystemShare(this.mActivity);
                AlertMsgDialog positiveButtonText = new AlertMsgDialog(this.mActivity).title(R.string.first_use_system_share_tips_title).content((int) R.string.first_use_system_share_tips_content).positiveButtonText(R.string.confirm_okay);
                positiveButtonText.setOnDismissListener(new DialogInterface.OnDismissListener(list) {
                    private final /* synthetic */ List f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void onDismiss(DialogInterface dialogInterface) {
                        ImageShareSaverDialog.immediateShareDownloading(ImmediateSharePlugin.this.mActivity, this.f$1);
                    }
                });
                positiveButtonText.show();
            } else {
                ImageShareSaverDialog.immediateShareDownloading(this.mActivity, list);
            }
        }
    }
}
