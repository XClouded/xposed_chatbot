package com.alimama.moon.update;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import com.alibaba.android.update4mtl.data.ResponseUpdateInfo;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.permission.Permission;
import javax.inject.Inject;
import javax.inject.Named;

public class UpdateRemindDialog extends AppCompatDialog {
    /* access modifiers changed from: private */
    public ResponseUpdateInfo info;
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission storagePermission;

    private UpdateRemindDialog(Context context) {
        super(context, R.style.common_dialog_style);
        App.getAppComponent().inject(this);
    }

    public static void show(@NonNull Activity activity, ResponseUpdateInfo responseUpdateInfo) {
        if (responseUpdateInfo != null) {
            UpdateRemindDialog updateRemindDialog = new UpdateRemindDialog(activity);
            updateRemindDialog.setOwnerActivity(activity);
            updateRemindDialog.info = responseUpdateInfo;
            updateRemindDialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.dialog_update_remind);
        ((TextView) findViewById(R.id.content)).setText(this.info.info);
        ((TextView) findViewById(R.id.btn_ignore)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UpdateRemindDialog.this.dismiss();
            }
        });
        ((TextView) findViewById(R.id.btn_update)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (UpdateHelper.checkPermissionGranted(UpdateRemindDialog.this.getOwnerActivity(), UpdateRemindDialog.this.storagePermission)) {
                    UpdateHelper.startUpdateService(UpdateRemindDialog.this.getOwnerActivity(), UpdateRemindDialog.this.info);
                    ToastUtil.toast(UpdateRemindDialog.this.getContext(), (int) R.string.downloading_package);
                    UpdateRemindDialog.this.dismiss();
                }
            }
        });
        setCanceledOnTouchOutside(false);
    }
}
