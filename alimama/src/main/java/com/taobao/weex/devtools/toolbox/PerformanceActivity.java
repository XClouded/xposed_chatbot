package com.taobao.weex.devtools.toolbox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.weex.inspector.R;

public class PerformanceActivity extends ActionBarActivity {
    /* access modifiers changed from: private */
    public LinearLayout segmentHost;

    public static void start(Context context, int i) {
        Intent intent = new Intent(context, PerformanceActivity.class);
        intent.putExtra(BindingXConstants.KEY_INSTANCE_ID, i);
        context.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        PerformanceActivity.super.onCreate(bundle);
        setContentView(R.layout.activity_performance);
        this.segmentHost = (LinearLayout) findViewById(R.id.segment_control);
        addTab("页面性能", true, new View.OnClickListener() {
            public void onClick(View view) {
                PerformanceActivity.this.switchFragment(EventOverviewFragment.getInstance(PerformanceActivity.this.getIntent().getIntExtra(BindingXConstants.KEY_INSTANCE_ID, -1)));
            }
        });
        addTab("JS LOG", false, new View.OnClickListener() {
            public void onClick(View view) {
                PerformanceActivity.this.switchFragment(new JsLogFragment());
            }
        });
        addTab("环境变量", false, new View.OnClickListener() {
            public void onClick(View view) {
                PerformanceActivity.this.switchFragment(new EnvironmentFragment());
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle((CharSequence) "Weex Monitor");
        }
    }

    /* access modifiers changed from: private */
    public void switchFragment(Fragment fragment) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
    }

    /* access modifiers changed from: protected */
    public void onResumeFragments() {
        PerformanceActivity.super.onResumeFragments();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, (Fragment) EventOverviewFragment.getInstance(getIntent().getIntExtra(BindingXConstants.KEY_INSTANCE_ID, -1))).commitAllowingStateLoss();
        }
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [android.content.Context, com.taobao.weex.devtools.toolbox.PerformanceActivity] */
    private void addTab(String str, boolean z, final View.OnClickListener onClickListener) {
        TextView textView = new TextView(this);
        textView.setText(str);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.weight = 1.0f;
        layoutParams.setMargins(1, 1, 1, 1);
        if (z) {
            textView.setBackgroundColor(0);
        } else {
            textView.setBackgroundColor(-1);
        }
        textView.setGravity(17);
        textView.setLayoutParams(layoutParams);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!view.isSelected() && (view instanceof TextView)) {
                    onClickListener.onClick(view);
                    for (int i = 0; i < PerformanceActivity.this.segmentHost.getChildCount(); i++) {
                        PerformanceActivity.this.segmentHost.getChildAt(i).setSelected(false);
                        PerformanceActivity.this.segmentHost.getChildAt(i).setBackgroundColor(-1);
                        ((TextView) PerformanceActivity.this.segmentHost.getChildAt(i)).setTextColor(Color.parseColor("#1E90FF"));
                    }
                    view.setSelected(true);
                    view.setBackgroundColor(0);
                    ((TextView) view).setTextColor(-1);
                }
            }
        });
        textView.setSelected(z);
        if (z) {
            textView.setTextColor(-1);
        } else {
            textView.setTextColor(Color.parseColor("#1E90FF"));
        }
        this.segmentHost.addView(textView);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return PerformanceActivity.super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }
}
