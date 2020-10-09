package com.taobao.android.nav;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.taobao.android.internal.RuntimeGlobals;
import com.taobao.android.modular.MLog;
import com.taobao.weex.BuildConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Nav {
    private static final NavResolver DEFAULT_RESOLVER = new DefaultResovler();
    private static final String EXTRA_INTENT_FILTER_LABEL = "INTENT_FILTER_LABEL";
    public static final String KExtraReferrer = "referrer";
    public static final String NAV_START_ACTIVITY_TIME = "NAV_START_ACTIVITY_TIME";
    public static final String NAV_TO_URL_START_TIME = "NAV_TO_URL_START_TIME";
    private static final String TAG = "Nav";
    private static final List<NavAfterProcessor> mAfterprocessor = new CopyOnWriteArrayList();
    private static NavExceptionHandler mExceptionHandler = null;
    private static NavPreprocessor mLastProcessor = null;
    private static volatile NavResolver mNavResolver = DEFAULT_RESOLVER;
    private static final List<NavPreprocessor> mPreprocessor = new CopyOnWriteArrayList();
    private static final SparseArray<NavHooker> mPriorHookers = new SparseArray<>();
    private static final List<NavPreprocessor> mStickPreprocessor = new ArrayList();
    private static int[] mTransition = null;
    public static Drawable sDetailImageDrawable = null;
    private static NavigationTimeMonitor sNavMonitor = null;
    private static boolean sUseWelcome = true;
    private boolean mAllowLeaving;
    private final Context mContext;
    private boolean mDisableTransition;
    private boolean mDisallowLoopback;
    private Fragment mFragment = null;
    private final Intent mIntent;
    private Bundle mOptions;
    private int mRequestCode = -1;
    private boolean mSkipHooker;
    private boolean mSkipPreprocess;
    private boolean mSkipPriorHooker;
    private List<Intent> mTaskStack;

    public interface NavAfterProcessor {
        boolean afterNavTo(Nav nav, Intent intent);
    }

    public interface NavExceptionHandler {
        boolean onException(Intent intent, Exception exc);
    }

    public interface NavHooker {
        public static final int NAVHOOKER_HIGH_PRIORITY = 3;
        public static final int NAVHOOKER_LOW_PRIORITY = 1;
        public static final int NAVHOOKER_NORMAL_PRIORITY = 2;
        public static final int NAVHOOKER_STICKMAX_PRIORITY = 4;

        boolean hook(Context context, Intent intent);
    }

    public interface NavHookerExt extends NavHooker {
        boolean hook(Context context, Nav nav, Intent intent);
    }

    public interface NavPreprocessor {
        boolean beforeNavTo(Intent intent);
    }

    public interface NavResolver {
        List<ResolveInfo> queryIntentActivities(PackageManager packageManager, Intent intent, int i);

        ResolveInfo resolveActivity(PackageManager packageManager, Intent intent, int i);

        ResolveInfo resolveActivity(PackageManager packageManager, Intent intent, int i, boolean z);
    }

    public interface NavigationTimeMonitor {
        void onNavException(Context context, String str, Exception exc, boolean z);

        void onTimeConsuming(Context context, String str, int i, long j, long j2, long j3);
    }

    public interface RedirectNavPreprocessor extends NavPreprocessor {
        boolean beforeNavTo(Nav nav, Intent intent);
    }

    @SuppressLint({"Registered"})
    static class DemoActivity extends Activity {
        DemoActivity() {
        }

        /* access modifiers changed from: package-private */
        public void openItem(long j) {
            Nav.from(this).toUri(NavUri.host("item.taobao.com").path("item.htm").param("id", j));
        }

        /* access modifiers changed from: package-private */
        public void buildTaskStack(Uri uri, Uri uri2) {
            Nav.from(this).stack(uri).toUri(uri2);
        }

        /* access modifiers changed from: package-private */
        public void openUriWithinWebview(Uri uri) {
            Nav from = Nav.from(this);
            if (!from.disallowLoopback().toUri(uri)) {
                from.skipPreprocess().allowEscape().toUri(uri);
            }
        }
    }

    public static class NavigationCanceledException extends Exception {
        private static final long serialVersionUID = 5015146091187397488L;

        public NavigationCanceledException(String str) {
            super(str);
        }
    }

    public static Nav from(Context context) {
        return new Nav(context);
    }

    public Nav withFragment(Fragment fragment) {
        this.mFragment = fragment;
        return this;
    }

    public Nav withOptions(Bundle bundle) {
        if (this.mOptions != null) {
            this.mOptions.putAll(bundle);
        } else {
            this.mOptions = bundle;
        }
        return this;
    }

    public Nav withCategory(String str) {
        this.mIntent.addCategory(str);
        return this;
    }

    public Nav withExtras(Bundle bundle) {
        if (bundle == null) {
            return this;
        }
        this.mIntent.putExtras(bundle);
        return this;
    }

    public Nav withFlags(int i) {
        this.mIntent.addFlags(i);
        return this;
    }

    public Nav forResult(int i) {
        if (this.mContext instanceof Activity) {
            this.mRequestCode = i;
            return this;
        }
        throw new IllegalStateException("Only valid from Activity, but from " + this.mContext);
    }

    public boolean isForesultSet() {
        return this.mRequestCode >= 0;
    }

    public Nav allowEscape() {
        this.mAllowLeaving = true;
        return this;
    }

    public Nav disallowLoopback() {
        this.mDisallowLoopback = true;
        return this;
    }

    public Nav allowLoopback() {
        this.mDisallowLoopback = false;
        return this;
    }

    public Nav disableTransition() {
        this.mDisableTransition = true;
        return this;
    }

    public Nav skipPreprocess() {
        this.mSkipPreprocess = true;
        return this;
    }

    public Nav skipHooker() {
        this.mSkipHooker = true;
        return this;
    }

    public Nav skipPriorHooker() {
        this.mSkipPriorHooker = true;
        return this;
    }

    public Context getContext() {
        return this.mContext;
    }

    public boolean toUri(NavUri navUri) {
        return toUri(navUri.build());
    }

    public boolean toUri(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return toUri(Uri.parse(str));
    }

    /* JADX WARNING: Removed duplicated region for block: B:111:0x023f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean toUri(android.net.Uri r13) {
        /*
            r12 = this;
            r0 = 0
            if (r13 != 0) goto L_0x0004
            return r0
        L_0x0004:
            long r1 = java.lang.System.currentTimeMillis()
            com.taobao.android.nav.Nav$NavExceptionHandler r3 = mExceptionHandler
            android.content.Context r4 = r12.mContext
            if (r4 != 0) goto L_0x003a
            if (r3 == 0) goto L_0x001c
            android.content.Intent r1 = r12.mIntent
            com.taobao.android.nav.Nav$NavigationCanceledException r2 = new com.taobao.android.nav.Nav$NavigationCanceledException
            java.lang.String r4 = "Context shouldn't null"
            r2.<init>(r4)
            r3.onException(r1, r2)
        L_0x001c:
            java.lang.String r1 = "Nav"
            java.lang.String r2 = "Nav context was null"
            android.util.Log.e(r1, r2)
            com.taobao.android.nav.Nav$NavigationTimeMonitor r1 = sNavMonitor
            if (r1 == 0) goto L_0x0039
            com.taobao.android.nav.Nav$NavigationTimeMonitor r1 = sNavMonitor
            android.content.Context r2 = r12.mContext
            java.lang.String r13 = r13.toString()
            java.lang.Exception r3 = new java.lang.Exception
            java.lang.String r4 = "Nav context was null!"
            r3.<init>(r4)
            r1.onNavException(r2, r13, r3, r0)
        L_0x0039:
            return r0
        L_0x003a:
            boolean r4 = r13.isHierarchical()
            r5 = 1
            if (r4 != 0) goto L_0x0058
            com.taobao.android.nav.Nav$NavigationTimeMonitor r4 = sNavMonitor
            if (r4 == 0) goto L_0x0078
            com.taobao.android.nav.Nav$NavigationTimeMonitor r4 = sNavMonitor
            android.content.Context r6 = r12.mContext
            java.lang.String r7 = r13.toString()
            java.lang.Exception r8 = new java.lang.Exception
            java.lang.String r9 = "Nav Url is not hierarchical"
            r8.<init>(r9)
            r4.onNavException(r6, r7, r8, r5)
            goto L_0x0078
        L_0x0058:
            java.lang.String r4 = r13.toString()
            boolean r4 = android.webkit.URLUtil.isValidUrl(r4)
            if (r4 != 0) goto L_0x0078
            com.taobao.android.nav.Nav$NavigationTimeMonitor r4 = sNavMonitor
            if (r4 == 0) goto L_0x0078
            com.taobao.android.nav.Nav$NavigationTimeMonitor r4 = sNavMonitor
            android.content.Context r6 = r12.mContext
            java.lang.String r7 = r13.toString()
            java.lang.Exception r8 = new java.lang.Exception
            java.lang.String r9 = "Nav Url is not valid"
            r8.<init>(r9)
            r4.onNavException(r6, r7, r8, r5)
        L_0x0078:
            r4 = 0
        L_0x0079:
            android.content.Intent r6 = r12.intentForUri(r13, r5)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r6 != 0) goto L_0x00a4
            if (r3 == 0) goto L_0x008d
            android.content.Intent r6 = r12.mIntent     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            com.taobao.android.nav.Nav$NavigationCanceledException r7 = new com.taobao.android.nav.Nav$NavigationCanceledException     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r8 = "Intent resolve was null"
            r7.<init>(r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r3.onException(r6, r7)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x008d:
            com.taobao.android.nav.Nav$NavigationTimeMonitor r6 = sNavMonitor     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r6 == 0) goto L_0x00a3
            com.taobao.android.nav.Nav$NavigationTimeMonitor r6 = sNavMonitor     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r8 = r13.toString()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.Exception r9 = new java.lang.Exception     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r10 = "Intent resolve was null"
            r9.<init>(r10)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r6.onNavException(r7, r8, r9, r0)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x00a3:
            return r0
        L_0x00a4:
            boolean r7 = r6 instanceof com.taobao.android.nav.Nav.NavHookIntent     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x00a9
            return r5
        L_0x00a9:
            java.util.List<com.taobao.android.nav.Nav$NavAfterProcessor> r7 = mAfterprocessor     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            boolean r7 = r7.isEmpty()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 != 0) goto L_0x00ca
            java.util.List<com.taobao.android.nav.Nav$NavAfterProcessor> r7 = mAfterprocessor     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.util.Iterator r7 = r7.iterator()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x00b7:
            boolean r8 = r7.hasNext()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r8 == 0) goto L_0x00ca
            java.lang.Object r8 = r7.next()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            com.taobao.android.nav.Nav$NavAfterProcessor r8 = (com.taobao.android.nav.Nav.NavAfterProcessor) r8     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            boolean r8 = r8.afterNavTo(r12, r6)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r8 != 0) goto L_0x00b7
            return r0
        L_0x00ca:
            java.lang.String r7 = "NAV_TO_URL_START_TIME"
            r6.putExtra(r7, r1)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.content.ComponentName r7 = r6.getComponent()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x00e4
            com.taobao.android.hresource.HResourceManager r8 = com.taobao.android.hresource.HResourceManager.getInstance()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r9 = r7.getClassName()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r8.enterPage(r9, r10)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x00e4:
            boolean r8 = r12.mDisallowLoopback     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r8 == 0) goto L_0x0115
            android.content.Context r8 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            boolean r8 = r8 instanceof android.app.Activity     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r8 == 0) goto L_0x0115
            if (r7 == 0) goto L_0x0115
            android.content.Context r8 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.app.Activity r8 = (android.app.Activity) r8     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.content.ComponentName r8 = r8.getComponentName()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            boolean r7 = r7.equals(r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x0115
            java.lang.String r6 = "Nav"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.<init>()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r8 = "Loopback disallowed: "
            r7.append(r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.append(r13)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r7 = r7.toString()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.util.Log.w(r6, r7)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            return r0
        L_0x0115:
            java.lang.String r7 = "NAV_START_ACTIVITY_TIME"
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r6.putExtra(r7, r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            boolean r7 = hasWelcome()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 != 0) goto L_0x0141
            android.content.ComponentName r7 = r6.getComponent()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r7 = r7.getClassName()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r8 = "com.taobao.tao.welcome.Welcome"
            boolean r7 = r7.equals(r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x0141
            int r7 = r6.getFlags()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r8 = 67108864(0x4000000, float:1.5046328E-36)
            r7 = r7 | r8
            r8 = 4194304(0x400000, float:5.877472E-39)
            r7 = r7 | r8
            r6.setFlags(r7)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x0141:
            java.util.List<android.content.Intent> r7 = r12.mTaskStack     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x0168
            int r7 = android.os.Build.VERSION.SDK_INT     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r8 = 11
            if (r7 < r8) goto L_0x0168
            java.util.List<android.content.Intent> r7 = r12.mTaskStack     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.content.Intent r8 = r12.mIntent     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.add(r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.util.List<android.content.Intent> r7 = r12.mTaskStack     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.util.List<android.content.Intent> r8 = r12.mTaskStack     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            int r8 = r8.size()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.content.Intent[] r8 = new android.content.Intent[r8]     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.Object[] r7 = r7.toArray(r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.content.Intent[] r7 = (android.content.Intent[]) r7     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.os.Bundle r8 = r12.mOptions     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r12.startActivities(r7, r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            goto L_0x01be
        L_0x0168:
            int r7 = r12.mRequestCode     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r8 = 16
            if (r7 < 0) goto L_0x01a2
            int r7 = android.os.Build.VERSION.SDK_INT     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 < r8) goto L_0x018c
            androidx.fragment.app.Fragment r7 = r12.mFragment     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x0180
            androidx.fragment.app.Fragment r7 = r12.mFragment     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            int r8 = r12.mRequestCode     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.os.Bundle r9 = r12.mOptions     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.startActivityForResult(r6, r8, r9)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            goto L_0x01be
        L_0x0180:
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.app.Activity r7 = (android.app.Activity) r7     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            int r8 = r12.mRequestCode     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.os.Bundle r9 = r12.mOptions     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.startActivityForResult(r6, r8, r9)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            goto L_0x01be
        L_0x018c:
            androidx.fragment.app.Fragment r7 = r12.mFragment     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x0198
            androidx.fragment.app.Fragment r7 = r12.mFragment     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            int r8 = r12.mRequestCode     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.startActivityForResult(r6, r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            goto L_0x01be
        L_0x0198:
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.app.Activity r7 = (android.app.Activity) r7     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            int r8 = r12.mRequestCode     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.startActivityForResult(r6, r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            goto L_0x01be
        L_0x01a2:
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            boolean r7 = r7 instanceof android.app.Activity     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 != 0) goto L_0x01ad
            r7 = 268435456(0x10000000, float:2.5243549E-29)
            r6.addFlags(r7)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x01ad:
            int r7 = android.os.Build.VERSION.SDK_INT     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 < r8) goto L_0x01b9
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.os.Bundle r8 = r12.mOptions     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.startActivity(r6, r8)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            goto L_0x01be
        L_0x01b9:
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.startActivity(r6)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x01be:
            boolean r7 = r12.mDisableTransition     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 != 0) goto L_0x01db
            int[] r7 = mTransition     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x01db
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            boolean r7 = r7 instanceof android.app.Activity     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x01db
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.app.Activity r7 = (android.app.Activity) r7     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            int[] r8 = mTransition     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r8 = r8[r0]     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            int[] r9 = mTransition     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r9 = r9[r5]     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r7.overridePendingTransition(r8, r9)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x01db:
            boolean r7 = r12.isDebug()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            if (r7 == 0) goto L_0x0210
            android.net.Uri r6 = r6.getData()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r6 = r6.toString()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            int r7 = r6.length()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r8 = 5120(0x1400, float:7.175E-42)
            if (r7 <= r8) goto L_0x0210
            android.content.Context r7 = r12.mContext     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r8.<init>()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r9 = "Your url : "
            r8.append(r9)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r8.append(r6)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r6 = " is too large which may cause Exception, plz check it!"
            r8.append(r6)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            java.lang.String r6 = r8.toString()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            android.widget.Toast r6 = android.widget.Toast.makeText(r7, r6, r5)     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
            r6.show()     // Catch:{ ActivityNotFoundException -> 0x022d, SecurityException -> 0x0211 }
        L_0x0210:
            return r5
        L_0x0211:
            r6 = move-exception
            if (r3 == 0) goto L_0x021d
            android.content.Intent r7 = r12.mIntent
            boolean r3 = r3.onException(r7, r6)
            if (r3 == 0) goto L_0x021d
            goto L_0x0238
        L_0x021d:
            com.taobao.android.nav.Nav$NavigationTimeMonitor r1 = sNavMonitor
            if (r1 == 0) goto L_0x022c
            com.taobao.android.nav.Nav$NavigationTimeMonitor r1 = sNavMonitor
            android.content.Context r2 = r12.mContext
            java.lang.String r13 = r13.toString()
            r1.onNavException(r2, r13, r6, r0)
        L_0x022c:
            return r0
        L_0x022d:
            r6 = move-exception
            if (r3 == 0) goto L_0x023b
            android.content.Intent r7 = r12.mIntent
            boolean r3 = r3.onException(r7, r6)
            if (r3 == 0) goto L_0x023b
        L_0x0238:
            r3 = r4
            goto L_0x0079
        L_0x023b:
            com.taobao.android.nav.Nav$NavigationTimeMonitor r1 = sNavMonitor
            if (r1 == 0) goto L_0x024a
            com.taobao.android.nav.Nav$NavigationTimeMonitor r1 = sNavMonitor
            android.content.Context r2 = r12.mContext
            java.lang.String r13 = r13.toString()
            r1.onNavException(r2, r13, r6, r0)
        L_0x024a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.nav.Nav.toUri(android.net.Uri):boolean");
    }

    public Intent intentForUri(NavUri navUri) {
        return intentForUri(navUri.build());
    }

    public Intent intentForUri(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return intentForUri(Uri.parse(str));
    }

    public Intent intentForUri(Uri uri) {
        return intentForUri(uri, false);
    }

    public Intent intentForUri(Uri uri, boolean z) {
        Log.d(TAG, uri == null ? BuildConfig.buildJavascriptFrameworkVersion : uri.toString());
        ResolveInfo resolveInfo = null;
        if (this.mContext == null) {
            Log.e(TAG, "Nav context was null");
            return null;
        }
        Intent intent = to(uri);
        if (intent == null) {
            return null;
        }
        if (intent instanceof NavHookIntent) {
            return intent;
        }
        try {
            if (this.mAllowLeaving) {
                ResolveInfo resolveActivity = mNavResolver.resolveActivity(this.mContext.getPackageManager(), intent, 65536, this.mAllowLeaving);
                if (resolveActivity == null) {
                    List<ResolveInfo> queryIntentActivities = mNavResolver.queryIntentActivities(this.mContext.getPackageManager(), intent, 65536);
                    if (queryIntentActivities != null && queryIntentActivities.size() >= 1) {
                        resolveInfo = queryIntentActivities.get(0);
                    }
                    if (resolveInfo != null) {
                        if (resolveInfo.labelRes != 0) {
                            intent.putExtra(EXTRA_INTENT_FILTER_LABEL, resolveInfo.labelRes);
                        }
                        intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                    } else {
                        throw new ActivityNotFoundException("No Activity found to handle " + intent);
                    }
                } else {
                    if (resolveActivity.labelRes != 0) {
                        intent.putExtra(EXTRA_INTENT_FILTER_LABEL, resolveActivity.labelRes);
                    }
                    intent.setClassName(resolveActivity.activityInfo.packageName, resolveActivity.activityInfo.name);
                }
            } else if (!RuntimeGlobals.isMultiPackageMode(this.mContext)) {
                intent.setPackage(this.mContext.getPackageName());
                ResolveInfo resolveActivity2 = mNavResolver.resolveActivity(this.mContext.getPackageManager(), intent, 65536, this.mAllowLeaving);
                if (resolveActivity2 == null) {
                    ResolveInfo optimum = optimum(getContext(), mNavResolver.queryIntentActivities(this.mContext.getPackageManager(), intent, 65536));
                    if (optimum != null) {
                        if (optimum.labelRes != 0) {
                            intent.putExtra(EXTRA_INTENT_FILTER_LABEL, optimum.labelRes);
                        }
                        intent.setClassName(optimum.activityInfo.packageName, optimum.activityInfo.name);
                    } else {
                        throw new ActivityNotFoundException("No Activity found to handle " + intent);
                    }
                } else {
                    if (resolveActivity2.labelRes != 0) {
                        intent.putExtra(EXTRA_INTENT_FILTER_LABEL, resolveActivity2.labelRes);
                    }
                    intent.setClassName(resolveActivity2.activityInfo.packageName, resolveActivity2.activityInfo.name);
                }
            } else {
                ResolveInfo optimum2 = optimum(getContext(), mNavResolver.queryIntentActivities(this.mContext.getPackageManager(), intent, 65536));
                if (optimum2 != null) {
                    if (optimum2.labelRes != 0) {
                        intent.putExtra(EXTRA_INTENT_FILTER_LABEL, optimum2.labelRes);
                    }
                    intent.setClassName(optimum2.activityInfo.packageName, optimum2.activityInfo.name);
                } else {
                    throw new ActivityNotFoundException("No Activity found to handle " + intent);
                }
            }
            return intent;
        } catch (ActivityNotFoundException e) {
            if (isDebug()) {
                Context context = this.mContext;
                Toast.makeText(context, uri.toString() + " CANN'T FOUND RESOLVED ACTIVITY", 1).show();
            }
            if (z) {
                throw e;
            }
            return intent;
        } catch (SecurityException e2) {
            if (isDebug()) {
                Context context2 = this.mContext;
                Toast.makeText(context2, uri.toString() + "SecurityException", 1).show();
            }
            if (z) {
                throw e2;
            }
            return intent;
        }
    }

    @TargetApi(11)
    private void startActivities(Intent[] intentArr, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.mContext.startActivities(intentArr, bundle);
        } else {
            this.mContext.startActivities(intentArr);
        }
    }

    public PendingIntent toPendingUri(Uri uri, int i, int i2) {
        Intent specify = specify(to(uri, false));
        if (specify == null) {
            return null;
        }
        if (this.mTaskStack == null || Build.VERSION.SDK_INT < 11) {
            specify.addFlags(268435456);
            return PendingIntent.getActivity(this.mContext, i, specify, i2);
        }
        this.mTaskStack.add(this.mIntent);
        return getActivities(this.mContext, i, (Intent[]) this.mTaskStack.toArray(new Intent[this.mTaskStack.size()]), i2);
    }

    @TargetApi(11)
    private static PendingIntent getActivities(Context context, int i, Intent[] intentArr, int i2) {
        return getActivities(context, i, intentArr, i2);
    }

    @TargetApi(11)
    public Nav stack(Uri uri) {
        if (this.mRequestCode < 0) {
            Intent intent = new Intent(to(uri, false));
            if (this.mTaskStack == null) {
                this.mTaskStack = new ArrayList(1);
                intent.addFlags(268435456);
                if (Build.VERSION.SDK_INT >= 11) {
                    intent.addFlags(49152);
                }
            }
            this.mTaskStack.add(intent);
            Nav nav = new Nav(this.mContext);
            nav.mTaskStack = this.mTaskStack;
            return nav;
        }
        throw new IllegalStateException("Cannot stack URI for result");
    }

    private Intent to(Uri uri) {
        return to(uri, !this.mSkipPreprocess);
    }

    private Intent to(Uri uri, boolean z) {
        boolean z2;
        boolean beforeNavTo;
        NavHooker navHooker;
        this.mIntent.setData(uri);
        NavHooker navHooker2 = mPriorHookers.get(4);
        if (!this.mSkipHooker && navHooker2 != null) {
            if (navHooker2 instanceof NavHookerExt) {
                if (!((NavHookerExt) navHooker2).hook(this.mContext, this, this.mIntent)) {
                    return new NavHookIntent();
                }
            } else if (!navHooker2.hook(this.mContext, this.mIntent)) {
                return new NavHookIntent();
            }
        }
        if (!this.mSkipPriorHooker) {
            for (int i = 0; i < mPriorHookers.size(); i++) {
                int keyAt = mPriorHookers.keyAt(i);
                if (!(keyAt == 4 || (navHooker = mPriorHookers.get(keyAt)) == null)) {
                    if (navHooker instanceof NavHookerExt) {
                        if (!((NavHookerExt) navHooker).hook(this.mContext, this, this.mIntent)) {
                            return new NavHookIntent();
                        }
                    } else if (!navHooker.hook(this.mContext, this.mIntent)) {
                        return new NavHookIntent();
                    }
                }
            }
        }
        if (!this.mIntent.hasExtra(KExtraReferrer)) {
            if (this.mContext instanceof Activity) {
                Intent intent = ((Activity) this.mContext).getIntent();
                if (intent != null) {
                    Uri data = intent.getData();
                    if (data != null) {
                        this.mIntent.putExtra(KExtraReferrer, data.toString());
                    } else {
                        ComponentName component = intent.getComponent();
                        if (component != null) {
                            this.mIntent.putExtra(KExtraReferrer, new Intent().setComponent(component).toUri(0));
                        } else {
                            this.mIntent.putExtra(KExtraReferrer, intent.toUri(0));
                        }
                    }
                }
            } else {
                this.mIntent.putExtra(KExtraReferrer, this.mContext.getPackageName());
            }
        }
        int myTid = Process.myTid();
        System.currentTimeMillis();
        Debug.threadCpuTimeNanos();
        if (!mStickPreprocessor.isEmpty()) {
            for (NavPreprocessor next : mStickPreprocessor) {
                long currentTimeMillis = System.currentTimeMillis();
                long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
                MLog.e(TAG, "url before processor " + next.getClass().getName() + " is: " + this.mIntent.getDataString());
                boolean beforeNavTo2 = next.beforeNavTo(this.mIntent);
                MLog.e(TAG, "url after processor " + next.getClass().getName() + " is: " + this.mIntent.getDataString());
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                long threadCpuTimeNanos2 = Debug.threadCpuTimeNanos() - threadCpuTimeNanos;
                if (sNavMonitor != null && currentTimeMillis2 > 5) {
                    sNavMonitor.onTimeConsuming(this.mContext, next.getClass().getName(), myTid, currentTimeMillis, currentTimeMillis2, threadCpuTimeNanos2);
                    continue;
                }
                if (!beforeNavTo2) {
                    return null;
                }
            }
        }
        if (z && !mPreprocessor.isEmpty()) {
            for (NavPreprocessor next2 : mPreprocessor) {
                long currentTimeMillis3 = System.currentTimeMillis();
                long threadCpuTimeNanos3 = Debug.threadCpuTimeNanos();
                MLog.e(TAG, "url before processor " + next2.getClass().getName() + " is: " + this.mIntent.getDataString());
                if (next2 instanceof RedirectNavPreprocessor) {
                    beforeNavTo = ((RedirectNavPreprocessor) next2).beforeNavTo(this, this.mIntent);
                } else {
                    beforeNavTo = next2.beforeNavTo(this.mIntent);
                }
                boolean z3 = beforeNavTo;
                MLog.e(TAG, "url after processor " + next2.getClass().getName() + " is: " + this.mIntent.getDataString());
                long currentTimeMillis4 = System.currentTimeMillis() - currentTimeMillis3;
                long threadCpuTimeNanos4 = Debug.threadCpuTimeNanos() - threadCpuTimeNanos3;
                if (sNavMonitor != null && currentTimeMillis4 > 5) {
                    sNavMonitor.onTimeConsuming(this.mContext, next2.getClass().getName(), myTid, currentTimeMillis3, currentTimeMillis4, threadCpuTimeNanos4);
                    continue;
                }
                if (!z3) {
                    return null;
                }
            }
        }
        if (z && mLastProcessor != null) {
            long currentTimeMillis5 = System.currentTimeMillis();
            long threadCpuTimeNanos5 = Debug.threadCpuTimeNanos();
            MLog.e(TAG, "url before processor " + mLastProcessor.getClass().getName() + " is: " + this.mIntent.getDataString());
            if (mLastProcessor instanceof RedirectNavPreprocessor) {
                z2 = ((RedirectNavPreprocessor) mLastProcessor).beforeNavTo(this, this.mIntent);
            } else {
                z2 = mLastProcessor.beforeNavTo(this.mIntent);
            }
            MLog.e(TAG, "url after processor " + mLastProcessor.getClass().getName() + " is: " + this.mIntent.getDataString());
            long currentTimeMillis6 = System.currentTimeMillis() - currentTimeMillis5;
            long threadCpuTimeNanos6 = Debug.threadCpuTimeNanos() - threadCpuTimeNanos5;
            if (sNavMonitor != null && currentTimeMillis6 > 5) {
                sNavMonitor.onTimeConsuming(this.mContext, mLastProcessor.getClass().getName(), myTid, currentTimeMillis5, currentTimeMillis6, threadCpuTimeNanos6);
            }
            if (!z2) {
                return null;
            }
        }
        return this.mIntent;
    }

    private Intent specify(Intent intent) {
        ResolveInfo optimum;
        if (this.mAllowLeaving || (optimum = optimum(getContext(), mNavResolver.queryIntentActivities(this.mContext.getPackageManager(), intent, 65536))) == null) {
            return intent;
        }
        intent.setClassName(optimum.activityInfo.packageName, optimum.activityInfo.name);
        return intent;
    }

    public static ResolveInfo optimum(Context context, List<ResolveInfo> list) {
        if (context == null || list == null) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo next : list) {
            if (!TextUtils.isEmpty(next.activityInfo.packageName)) {
                if (next.activityInfo.packageName.endsWith(context.getPackageName())) {
                    arrayList.add(new SortedResolveInfo(next, next.priority, 1));
                } else {
                    String str = next.activityInfo.packageName;
                    String packageName = context.getPackageName();
                    String[] split = str.split("\\.");
                    String[] split2 = packageName.split("\\.");
                    if (split.length >= 2 && split2.length >= 2 && split[0].equals(split2[0]) && split[1].equals(split2[1])) {
                        arrayList.add(new SortedResolveInfo(next, next.priority, 0));
                    }
                }
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        if (arrayList.size() > 1) {
            Collections.sort(arrayList);
        }
        ResolveInfo access$100 = ((SortedResolveInfo) arrayList.get(0)).info;
        arrayList.clear();
        return access$100;
    }

    private static final class SortedResolveInfo implements Comparable<SortedResolveInfo> {
        /* access modifiers changed from: private */
        public final ResolveInfo info;
        private int same = 0;
        private int weight = 0;

        public SortedResolveInfo(ResolveInfo resolveInfo, int i, int i2) {
            this.info = resolveInfo;
            this.weight = i;
            this.same = i2;
        }

        public int compareTo(SortedResolveInfo sortedResolveInfo) {
            if (this == sortedResolveInfo) {
                return 0;
            }
            if (sortedResolveInfo.weight != this.weight) {
                return sortedResolveInfo.weight - this.weight;
            }
            if (sortedResolveInfo.same != this.same) {
                return sortedResolveInfo.same - this.same;
            }
            return System.identityHashCode(this) < System.identityHashCode(sortedResolveInfo) ? -1 : 1;
        }
    }

    public static void registerStickPreprocessor(NavPreprocessor navPreprocessor) {
        mStickPreprocessor.add(navPreprocessor);
    }

    public static void unregisterStickPreprocessor(NavPreprocessor navPreprocessor) {
        mStickPreprocessor.remove(navPreprocessor);
    }

    public static void registerPreprocessor(NavPreprocessor navPreprocessor) {
        mPreprocessor.add(navPreprocessor);
    }

    public static void registerLastPreprocessor(NavPreprocessor navPreprocessor) {
        if (mLastProcessor == null) {
            mLastProcessor = navPreprocessor;
        }
    }

    public static void unregisterPreprocessor(NavPreprocessor navPreprocessor) {
        mPreprocessor.remove(navPreprocessor);
    }

    public static void registerAfterProcessor(NavAfterProcessor navAfterProcessor) {
        mAfterprocessor.add(navAfterProcessor);
    }

    public static void unregisterAfterProcessor(NavAfterProcessor navAfterProcessor) {
        mAfterprocessor.remove(navAfterProcessor);
    }

    public static void registerHooker(NavHooker navHooker) {
        if (mPriorHookers == null) {
            return;
        }
        if (mPriorHookers.get(4) != null) {
            Log.e(TAG, "There is already one NavHooker with priority NAVHOOKER_HIGH_PRIORITY! Cannot override this NavHooker!");
        } else {
            mPriorHookers.put(4, navHooker);
        }
    }

    public static void registerPriorHooker(NavHooker navHooker, int i) {
        if (i > 3 || i < 1) {
            throw new RuntimeException("NavHooker's priority less than NAVHOOKER_HIGH_PRIORITY, larger than NAVHOOKER_LOW_PRIORITY");
        }
        mPriorHookers.put(i, navHooker);
    }

    public static void registerNavMonitor(NavigationTimeMonitor navigationTimeMonitor) {
        sNavMonitor = navigationTimeMonitor;
    }

    public static void setExceptionHandler(NavExceptionHandler navExceptionHandler) {
        mExceptionHandler = navExceptionHandler;
    }

    public static void setNavResolver(NavResolver navResolver) {
        mNavResolver = navResolver;
    }

    public static void setTransition(int i, int i2) {
        mTransition = new int[2];
        mTransition[0] = i;
        mTransition[1] = i2;
    }

    private Nav(Context context) {
        this.mContext = context;
        this.mIntent = new Intent("android.intent.action.VIEW");
        this.mOptions = null;
    }

    private static final class DefaultResovler implements NavResolver {
        private DefaultResovler() {
        }

        public List<ResolveInfo> queryIntentActivities(PackageManager packageManager, Intent intent, int i) {
            return packageManager.queryIntentActivities(intent, i);
        }

        public ResolveInfo resolveActivity(PackageManager packageManager, Intent intent, int i) {
            return packageManager.resolveActivity(intent, i);
        }

        public ResolveInfo resolveActivity(PackageManager packageManager, Intent intent, int i, boolean z) {
            return packageManager.resolveActivity(intent, i);
        }
    }

    private static class NavHookIntent extends Intent {
        private NavHookIntent() {
        }
    }

    private boolean isDebug() {
        return (this.mContext.getApplicationInfo().flags & 2) != 0;
    }

    public static void useWelcome(boolean z) {
        sUseWelcome = z;
    }

    public static boolean hasWelcome() {
        return sUseWelcome;
    }
}
