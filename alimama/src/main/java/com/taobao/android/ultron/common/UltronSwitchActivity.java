package com.taobao.android.ultron.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.taobao.android.ultron.common.utils.ApiModifyUtils;
import com.taobao.android.ultron.common.utils.TimeProfileUtil;
import com.taobao.android.ultron.common.utils.UnifyLog;
import com.taobao.android.ultron.common.utils.WriteRenderDataSwitch;

public class UltronSwitchActivity extends Activity {
    LinearLayout container;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ScrollView scrollView = new ScrollView(this);
        this.container = new LinearLayout(this);
        this.container.setGravity(17);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.topMargin = 60;
        scrollView.addView(this.container, layoutParams);
        setContentView(scrollView);
        processSwitch();
    }

    private void processSwitch() {
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            addSwitchInfoView(TimeProfileUtil.processSwitch(this, data));
            addSwitchInfoView(UnifyLog.processSwitch(this, data));
            addSwitchInfoView(ApiModifyUtils.processSwitch(this, data));
            addSwitchInfoView(WriteRenderDataSwitch.processSwitch(this, data));
        }
    }

    private void addSwitchInfoView(String str) {
        if (str != null) {
            TextView textView = new TextView(this);
            textView.setText(str);
            textView.setGravity(17);
            this.container.addView(textView);
        }
    }
}
