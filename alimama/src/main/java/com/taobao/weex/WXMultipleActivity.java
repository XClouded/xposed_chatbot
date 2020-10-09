package com.taobao.weex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.taobao.windvane.webview.WVSchemeInterceptService;
import android.taobao.windvane.webview.WVSchemeIntercepterInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.taobao.TBActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.alibaba.aliweex.hc.bundle.HCWeexPageFragment;
import com.taobao.android.festival.FestivalMgr;
import com.taobao.android.nav.Nav;
import com.taobao.baseactivity.CustomBaseActivity;
import com.taobao.weex.adapter.R;
import com.taobao.weex.adapter.TBNavBarAdapter;
import com.taobao.weex.constant.Constants;
import com.taobao.weex.utils.TBWXConfigManger;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.List;

public class WXMultipleActivity extends CustomBaseActivity {
    private static final String ACTION_MULTIPLE_DEGRADE_TO_WINDVANE = "multiple_degrade_to_windvane";
    public static final String BROWSER_ONLY_CATEGORY = "com.taobao.intent.category.HYBRID_UI";
    private static int MAX_INSTANCE = 10;
    private static final String TAG = "WXActivity";
    private WXAnalyzerDelegate mAnalyzerDelegate;
    private List<String> mBundles = new ArrayList();
    private BroadcastReceiver mDegradeReceive = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), WXMultipleActivity.ACTION_MULTIPLE_DEGRADE_TO_WINDVANE)) {
                WXMultipleActivity.this.degradeToWindVane(intent);
                String stringExtra = intent.getStringExtra(Constants.WEEX_URL);
                WXMultipleActivity.this.mPages.remove(stringExtra);
                if (WXMultipleActivity.this.mPages.size() <= 0) {
                    boolean unused = WXMultipleActivity.this.mIsDegrade = true;
                    WXMultipleActivity.this.finish();
                    return;
                }
                Fragment findFragmentByTag = WXMultipleActivity.this.getSupportFragmentManager().findFragmentByTag(stringExtra);
                if (findFragmentByTag != null) {
                    WXMultipleActivity.this.getSupportFragmentManager().beginTransaction().remove(findFragmentByTag).commit();
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsDegrade = false;
    /* access modifiers changed from: private */
    public List<String> mPages = new ArrayList();
    private TBNavBarAdapter mTBNavBarAdapter;
    private List<String> mTempPages = new ArrayList();

    /* JADX WARNING: type inference failed for: r1v0, types: [android.content.Context, com.taobao.baseactivity.CustomBaseActivity, com.taobao.weex.WXMultipleActivity, androidx.appcompat.app.AppCompatActivity] */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        WXMultipleActivity.super.onCreate(bundle);
        if (!isEnvironmentReady()) {
            if (WXEnvironment.isApkDebugable()) {
                Toast.makeText(this, "WEEX_SDK 初始化失败!", 0).show();
            }
            degradeToWindVane(getIntent());
            finish();
            return;
        }
        setContentView(R.layout.weex_multiple_layout);
        this.mTBNavBarAdapter = new TBNavBarAdapter(this);
        this.mAnalyzerDelegate = new WXAnalyzerDelegate(this);
        this.mAnalyzerDelegate.onCreate();
        createWXPage(getIntent());
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [android.content.Context, com.taobao.weex.WXMultipleActivity, androidx.appcompat.app.AppCompatActivity] */
    private void createWXPage(Intent intent) {
        if (intent != null) {
            String stringExtra = intent.getStringExtra(Constants.WEEX_URL);
            String stringExtra2 = intent.getStringExtra(Constants.WEEX_BUNDLE_URL);
            WVSchemeIntercepterInterface wVSchemeIntercepter = WVSchemeInterceptService.getWVSchemeIntercepter();
            if (wVSchemeIntercepter != null) {
                stringExtra2 = wVSchemeIntercepter.dealUrlScheme(stringExtra2);
            }
            this.mPages.add(stringExtra);
            this.mTempPages.add(stringExtra);
            this.mBundles.add(stringExtra2);
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
            if (this.mPages.size() > MAX_INSTANCE) {
                beginTransaction.remove(supportFragmentManager.findFragmentByTag(this.mTempPages.remove(0)));
            }
            if (this.mPages.size() > 1) {
                beginTransaction.hide(supportFragmentManager.findFragmentByTag(this.mPages.get(this.mPages.size() - 2)));
            }
            HCWeexPageFragment hCWeexPageFragment = (HCWeexPageFragment) HCWeexPageFragment.newInstance((Context) this, (Class<? extends WeexPageFragment>) HCWeexPageFragment.class, stringExtra, stringExtra2);
            hCWeexPageFragment.setRenderListener(new WXMultipleRenderListenerImp(this.mAnalyzerDelegate, hCWeexPageFragment));
            this.mTBNavBarAdapter = new TBNavBarAdapter(this);
            this.mTBNavBarAdapter.setWeexUrl(stringExtra);
            hCWeexPageFragment.setNavBarAdapter(this.mTBNavBarAdapter);
            beginTransaction.setCustomAnimations(17432576, 17432577);
            beginTransaction.add(R.id.weex_multiple_container, hCWeexPageFragment, stringExtra);
            beginTransaction.commit();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        WXMultipleActivity.super.onDestroy();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onDestroy();
        }
        Log.e("error", "into--[onDestroy]");
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        WXMultipleActivity.super.onNewIntent(intent);
        createWXPage(intent);
        invalidateOptionsMenu();
    }

    private boolean isEnvironmentReady() {
        boolean isDegrade = TBWXConfigManger.getInstance().isDegrade();
        boolean isHardwareSupport = WXEnvironment.isHardwareSupport();
        boolean isInitialized = WXSDKEngine.isInitialized();
        WXLogUtils.d(TAG, "degrade:" + isDegrade + " support:" + isHardwareSupport + " init:" + isInitialized);
        return !isDegrade && isHardwareSupport && isInitialized;
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.content.Context, com.taobao.weex.WXMultipleActivity] */
    /* access modifiers changed from: private */
    public void degradeToWindVane(Intent intent) {
        Nav.from(this).withCategory(BROWSER_ONLY_CATEGORY).skipPreprocess().disableTransition().disallowLoopback().toUri(intent.getStringExtra(Constants.WEEX_URL));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return WXMultipleActivity.super.onCreateOptionsMenu(menu);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4 || !onWXBackPressed()) {
            return WXMultipleActivity.super.onKeyDown(i, keyEvent);
        }
        return true;
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [android.content.Context, com.taobao.weex.WXMultipleActivity, androidx.appcompat.app.AppCompatActivity] */
    public boolean onWXBackPressed() {
        if (this.mPages.size() <= 1) {
            return false;
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag(this.mPages.remove(this.mPages.size() - 1));
        this.mBundles.remove(this.mBundles.size() - 1);
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        beginTransaction.remove(findFragmentByTag);
        Fragment findFragmentByTag2 = supportFragmentManager.findFragmentByTag(this.mPages.get(this.mPages.size() - 1));
        if (findFragmentByTag2 != null) {
            beginTransaction.show(findFragmentByTag2);
        } else if (findFragmentByTag2 == null) {
            HCWeexPageFragment hCWeexPageFragment = (HCWeexPageFragment) HCWeexPageFragment.newInstance((Context) this, (Class<? extends WeexPageFragment>) HCWeexPageFragment.class, this.mPages.get(this.mPages.size() - 1), this.mBundles.get(this.mBundles.size() - 1));
            this.mTBNavBarAdapter = new TBNavBarAdapter(this);
            this.mTBNavBarAdapter.setWeexUrl(this.mPages.get(this.mPages.size() - 1));
            hCWeexPageFragment.setRenderListener(new WXMultipleRenderListenerImp(this.mAnalyzerDelegate, hCWeexPageFragment));
            hCWeexPageFragment.setNavBarAdapter(this.mTBNavBarAdapter);
            beginTransaction.setCustomAnimations(17432576, 17432577);
            beginTransaction.add(R.id.weex_multiple_container, hCWeexPageFragment, this.mPages.get(this.mPages.size() - 1));
        }
        beginTransaction.commit();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        WXMultipleActivity.super.onStart();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onStart();
        }
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [android.content.Context, com.taobao.baseactivity.CustomBaseActivity, com.taobao.weex.WXMultipleActivity] */
    /* access modifiers changed from: protected */
    public void onResume() {
        WXMultipleActivity.super.onResume();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onResume();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mDegradeReceive, new IntentFilter(ACTION_MULTIPLE_DEGRADE_TO_WINDVANE));
        if (this.mTBNavBarAdapter != null && !this.mTBNavBarAdapter.isMainHC() && getSupportActionBar() != null) {
            FestivalMgr.getInstance().setBgUI4Actionbar(this, TBActionBar.ActionBarStyle.NORMAL);
        }
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [android.content.Context, com.taobao.baseactivity.CustomBaseActivity, com.taobao.weex.WXMultipleActivity] */
    /* access modifiers changed from: protected */
    public void onPause() {
        WXMultipleActivity.super.onPause();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onPause();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mDegradeReceive);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        WXMultipleActivity.super.onStop();
        if (this.mAnalyzerDelegate != null) {
            this.mAnalyzerDelegate.onStop();
        }
    }

    public void finish() {
        if (this.mIsDegrade) {
            overridePendingTransition(0, 0);
        }
        WXMultipleActivity.super.finish();
    }

    public static class WXMultipleRenderListenerImp extends WeexPageFragment.WXRenderListenerAdapter {
        WXAnalyzerDelegate mAnalyzerDelegate;
        WeexPageFragment mWeexPageFragment;

        public WXMultipleRenderListenerImp(WXAnalyzerDelegate wXAnalyzerDelegate, WeexPageFragment weexPageFragment) {
            this.mAnalyzerDelegate = wXAnalyzerDelegate;
            this.mWeexPageFragment = weexPageFragment;
        }

        public View onCreateView(WXSDKInstance wXSDKInstance, View view) {
            View onWeexViewCreated = this.mAnalyzerDelegate != null ? this.mAnalyzerDelegate.onWeexViewCreated(wXSDKInstance, view) : null;
            return onWeexViewCreated == null ? view : onWeexViewCreated;
        }

        public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
            WXLogUtils.d(WXMultipleActivity.TAG, "into--[onViewCreated]");
        }

        public void onRenderSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
            WXLogUtils.d(WXMultipleActivity.TAG, "into--[onRenderSuccess]");
            if (this.mAnalyzerDelegate != null) {
                this.mAnalyzerDelegate.onWeexRenderSuccess(wXSDKInstance);
            }
        }

        public void onException(WXSDKInstance wXSDKInstance, boolean z, String str, String str2) {
            super.onException(wXSDKInstance, z, str, str2);
            if (this.mAnalyzerDelegate != null) {
                this.mAnalyzerDelegate.onException(wXSDKInstance, str, str2);
            }
            if (z) {
                Intent intent = new Intent(WXMultipleActivity.ACTION_MULTIPLE_DEGRADE_TO_WINDVANE);
                intent.putExtra(Constants.WEEX_URL, this.mWeexPageFragment.getOriginalUrl());
                LocalBroadcastManager.getInstance(wXSDKInstance.getContext()).sendBroadcast(intent);
            }
        }
    }
}
