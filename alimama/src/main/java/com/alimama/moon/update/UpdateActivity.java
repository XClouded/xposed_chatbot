package com.alimama.moon.update;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.alibaba.android.update4mtl.data.ResponseUpdateInfo;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.ui.PageRouterActivity;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.permission.Permission;
import javax.inject.Inject;
import javax.inject.Named;

public class UpdateActivity extends PageRouterActivity {
    public static final String EXTRA_INFO = "com.alimama.moon.update.UpdateActivity.EXTRA_INFO";
    private ResponseUpdateInfo info;
    private String infoStr;
    @Inject
    @Named("WRITE_EXTERNAL_STORAGE")
    Permission storagePermission;
    private TextView textView;
    private TextView updateBtn;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_update);
        App.getAppComponent().inject(this);
        if (bundle == null) {
            this.infoStr = getIntent().getStringExtra(EXTRA_INFO);
            this.info = (ResponseUpdateInfo) JSON.parseObject(this.infoStr, ResponseUpdateInfo.class);
        } else {
            this.infoStr = bundle.getString(EXTRA_INFO);
            this.info = (ResponseUpdateInfo) JSON.parseObject(this.infoStr, ResponseUpdateInfo.class);
        }
        this.textView = (TextView) findViewById(R.id.text_view);
        this.textView.setText(this.info.info);
        this.updateBtn = (TextView) findViewById(R.id.btn_update);
        this.updateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UpdateActivity.this.clickUpdateBtn();
            }
        });
    }

    /* access modifiers changed from: private */
    public void clickUpdateBtn() {
        if (UpdateHelper.checkPermissionGranted(this, this.storagePermission)) {
            UpdateHelper.startUpdateService(this, this.info);
            ToastUtil.toast((Context) this, (int) R.string.downloading_package);
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(EXTRA_INFO, this.infoStr);
        super.onSaveInstanceState(bundle);
    }
}
