package com.taobao.weex.analyzer.core.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.DevOptionsConfig;

public class SettingsActivity extends Activity {
    private CheckBox mCbExceptionNotification;
    private CheckBox mCbJSException;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.wxt_activity_settings);
        final DevOptionsConfig instance = DevOptionsConfig.getInstance(this);
        this.mCbJSException = (CheckBox) findViewById(R.id.cb_js_exception);
        this.mCbExceptionNotification = (CheckBox) findViewById(R.id.cb_allow_exception_notification);
        this.mCbJSException.setChecked(instance.isShownJSException());
        this.mCbExceptionNotification.setChecked(instance.isAllowExceptionNotification());
        this.mCbJSException.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                instance.setShownJSException(z);
            }
        });
        this.mCbExceptionNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                instance.setAllowExceptionNotification(z);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public static void launch(@NonNull Context context) {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }
}
