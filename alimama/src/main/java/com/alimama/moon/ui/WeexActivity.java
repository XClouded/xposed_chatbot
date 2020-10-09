package com.alimama.moon.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alimama.moon.R;
import com.alimama.union.app.infrastructure.image.request.TaoImageLoader;
import com.alimama.union.app.logger.NewMonitorLogger;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.uc.webview.export.media.MessageID;
import com.ut.mini.UTAnalytics;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeexActivity extends BaseActivity implements IActivityNavBarSetter {
    private static final String TAG = "WeexActivity";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) WeexActivity.class);
    private Toolbar toolbar;
    private ImageView toolbarLeft;
    private ImageView toolbarRight;
    private TextView toolbarTitle;
    private WeexActivityFragment weexPageFragment;
    /* access modifiers changed from: private */
    public WXSDKInstance wxsdkInstance;

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        logger.info("onNewIntent");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        logger.info(UmbrellaConstants.LIFECYCLE_CREATE);
        setContentView((int) R.layout.activity_weex);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbarLeft = (ImageView) this.toolbar.findViewById(R.id.btn_left);
        this.toolbarTitle = (TextView) this.toolbar.findViewById(R.id.toolbar_title);
        this.toolbarRight = (ImageView) this.toolbar.findViewById(R.id.btn_right);
        this.toolbarLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WeexActivity.this.wxsdkInstance != null) {
                    WeexActivity.this.wxsdkInstance.fireGlobalEventCallback("clickLeftItem", (Map<String, Object>) null);
                }
            }
        });
        this.toolbarRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WeexActivity.this.wxsdkInstance != null) {
                    WeexActivity.this.wxsdkInstance.fireGlobalEventCallback("clickRightItem", (Map<String, Object>) null);
                }
            }
        });
        final Uri data = getIntent().getData();
        Bundle bundle2 = new Bundle();
        bundle2.putString(WeexPageFragment.FRAGMENT_ARG_URI, data.toString());
        this.weexPageFragment = (WeexActivityFragment) WeexActivityFragment.instantiate(this, WeexActivityFragment.class.getName(), bundle2);
        this.weexPageFragment.setRenderListener(new WeexPageFragment.WXRenderListenerAdapter() {
            public View onCreateView(WXSDKInstance wXSDKInstance, View view) {
                WXSDKInstance unused = WeexActivity.this.wxsdkInstance = wXSDKInstance;
                return super.onCreateView(wXSDKInstance, view);
            }

            public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
                super.onException(wXSDKInstance, str, str2);
                NewMonitorLogger.Weex.loadException(WeexActivity.TAG, "weex onException", data.toString(), str, str2);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, this.weexPageFragment, WeexPageFragment.FRAGMENT_TAG).commit();
        WXSDKEngine.setActivityNavBarSetter(this);
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        logger.info("onRestart");
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        logger.info(UmbrellaConstants.LIFECYCLE_START);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        logger.info(UmbrellaConstants.LIFECYCLE_RESUME);
        UTAnalytics.getInstance().getDefaultTracker().pageAppear(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        logger.info(MessageID.onPause);
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(this);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        logger.info(MessageID.onStop);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        logger.info("onDestroy");
        if (this.weexPageFragment != null) {
            WXSDKEngine.setActivityNavBarSetter((IActivityNavBarSetter) null);
            this.weexPageFragment.destroyWeex();
        }
    }

    public boolean push(String str) {
        logger.info("push");
        return false;
    }

    public boolean pop(String str) {
        logger.info("pop");
        return false;
    }

    public boolean setNavBarRightItem(String str) {
        logger.info("setNavBarRightItem");
        try {
            TaoImageLoader.load(buildImageUri(new JSONObject(str).optString("icon", "")).toString()).placeholder(R.drawable.img_loading_bg).error(R.drawable.img_loading_bg).into(this.toolbarRight);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean clearNavBarRightItem(String str) {
        logger.info("clearNavBarRightItem");
        return false;
    }

    public boolean setNavBarLeftItem(String str) {
        logger.info("setNavBarLeftItem");
        try {
            TaoImageLoader.load(buildImageUri(new JSONObject(str).optString("icon", "")).toString()).placeholder(R.drawable.img_loading_bg).error(R.drawable.img_loading_bg).into(this.toolbarLeft);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean clearNavBarLeftItem(String str) {
        logger.info("clearNavBarLeftItem");
        return false;
    }

    public boolean setNavBarMoreItem(String str) {
        logger.info("setNavBarMoreItem");
        return false;
    }

    public boolean clearNavBarMoreItem(String str) {
        logger.info("clearNavBarMoreItem");
        return false;
    }

    public boolean setNavBarTitle(String str) {
        logger.info("setNavBarTitle");
        try {
            this.toolbarTitle.setText(new JSONObject(str).optString("title", ""));
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    private Uri buildImageUri(String str) {
        Uri parse = Uri.parse(str);
        String scheme = parse.getScheme();
        Uri.Builder buildUpon = parse.buildUpon();
        if (TextUtils.isEmpty(scheme)) {
            buildUpon.scheme("https");
        }
        return buildUpon.build();
    }
}
