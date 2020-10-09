package com.taobao.monitor.impl.data.newvisible;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.data.DefaultWebView;
import com.taobao.monitor.impl.data.IVisibleDetector;
import com.taobao.monitor.impl.data.ViewUtils;
import com.taobao.monitor.impl.data.WebViewProxy;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.weex.el.parse.Operators;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VisibleDetectorStatusImpl implements IVisibleDetector, Runnable {
    private static final List<BlackViewInfo> BLACK_VIEW_INFO_LIST = new ArrayList();
    private static final long CONTINUOUS_OBSERVER_DURATION = 5000;
    private static final long INTERVAL = 75;
    private static final String TAG = "VisibleDetectorStatusImpl";
    private static final int WEEX_VISIBLE_KEY = -307;
    private IVisibleDetector.IDetectorCallback callback;
    private final WeakReference<View> containRef;
    private long lastChangedTime = TimeUtils.currentTimeMillis();
    private Set<String> moveViewCacheSet = new HashSet();
    private Map<String, Integer> oldViews = new HashMap();
    private final String pageName;
    final PagePercentCalculate pagePercentCalculate;
    private boolean stopImmediately = false;
    private volatile boolean stopped = false;
    private Map<String, String> typeKeyStatusMap = new HashMap();
    private Set<String> typeLocationStatusSet = new HashSet();
    private int validElementCount = 0;

    static {
        BLACK_VIEW_INFO_LIST.add(new BlackViewInfo("TBMainActivity", id("uik_refresh_header_second_floor"), "*"));
        BLACK_VIEW_INFO_LIST.add(new BlackViewInfo("MainActivity3", id("uik_refresh_header_second_floor"), "*"));
        BLACK_VIEW_INFO_LIST.add(new BlackViewInfo("*", id("mytaobao_carousel"), "RecyclerView"));
        BLACK_VIEW_INFO_LIST.add(new BlackViewInfo("*", -1, "HLoopView"));
        BLACK_VIEW_INFO_LIST.add(new BlackViewInfo("*", -1, "HGifView"));
        BLACK_VIEW_INFO_LIST.add(new BlackViewInfo("TBLiveVideoActivity", id("recyclerview"), "AliLiveRecyclerView"));
    }

    public VisibleDetectorStatusImpl(View view, String str, float f) {
        try {
            View findViewById = view.findViewById(view.getResources().getIdentifier("content", "id", "android"));
            if (findViewById != null) {
                view = findViewById;
            }
        } catch (Exception unused) {
        }
        this.containRef = new WeakReference<>(view);
        this.pageName = str;
        this.pagePercentCalculate = new PagePercentCalculate(f);
        Logger.d(TAG, str);
    }

    public void setCallback(IVisibleDetector.IDetectorCallback iDetectorCallback) {
        this.callback = iDetectorCallback;
    }

    public void execute() {
        if (((View) this.containRef.get()) == null) {
            stop();
            return;
        }
        this.lastChangedTime = TimeUtils.currentTimeMillis();
        Global.instance().getAsyncUiHandler().postDelayed(this, 75);
    }

    public void stop() {
        this.stopped = true;
        Global.instance().getAsyncUiHandler().removeCallbacks(this);
    }

    public void run() {
        long currentTimeMillis = TimeUtils.currentTimeMillis();
        if (this.stopped) {
            return;
        }
        if (currentTimeMillis - this.lastChangedTime > CONTINUOUS_OBSERVER_DURATION || this.stopImmediately) {
            visibleEndByType("NORMAL");
            if (this.callback != null) {
                this.callback.completed(this.pagePercentCalculate.getPercentTime(this.lastChangedTime));
            }
            stop();
            return;
        }
        check();
        Global.instance().getAsyncUiHandler().postDelayed(this, 75);
    }

    public long getLastChangedTime() {
        return this.lastChangedTime;
    }

    private void check() {
        View view = (View) this.containRef.get();
        long j = this.lastChangedTime;
        this.validElementCount = 0;
        if (view == null) {
            stop();
            return;
        }
        try {
            if (view.getHeight() * view.getWidth() != 0) {
                this.pagePercentCalculate.beforeCalculate();
                calculateStatus(view, view);
                if (j != this.lastChangedTime) {
                    this.pagePercentCalculate.afterCalculate();
                }
                if ((j != this.lastChangedTime || this.stopImmediately) && this.callback != null) {
                    this.callback.changed(j);
                    this.callback.validElement(this.validElementCount);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidView(View view) {
        if ("INVALID".equals(view.getTag(WEEX_VISIBLE_KEY)) || view.getVisibility() != 0 || (view instanceof ViewStub)) {
            return false;
        }
        if ((view instanceof EditText) && view.hasFocus()) {
            return true;
        }
        if (view.getHeight() < ViewUtils.screenHeight / 25) {
            return false;
        }
        if ((view instanceof TextView) || (view instanceof ImageView) || (view instanceof ViewGroup)) {
            return true;
        }
        return false;
    }

    private void calculateStatus(View view, View view2) {
        View[] children;
        if (isValidView(view)) {
            boolean z = !inBlackList(view);
            if (view instanceof WebView) {
                int webViewProgress = DefaultWebView.INSTANCE.webViewProgress(view);
                if (webViewProgress != 100) {
                    this.lastChangedTime = TimeUtils.currentTimeMillis();
                } else {
                    this.stopImmediately = true;
                }
                this.validElementCount = webViewProgress;
            } else if (WebViewProxy.INSTANCE.isWebView(view)) {
                int webViewProgress2 = WebViewProxy.INSTANCE.webViewProgress(view);
                if (webViewProgress2 != 100) {
                    this.lastChangedTime = TimeUtils.currentTimeMillis();
                } else {
                    this.stopImmediately = true;
                }
                this.validElementCount = webViewProgress2;
            } else if (!(view instanceof EditText) || !view.hasFocus()) {
                boolean z2 = view instanceof TextView;
                if (z2) {
                    this.validElementCount++;
                } else if (view instanceof ImageView) {
                    if (((ImageView) view).getDrawable() != null) {
                        this.validElementCount++;
                    }
                } else if (view.getBackground() != null) {
                    this.validElementCount++;
                }
                if (z2) {
                    doValidViewAction(view, view2);
                } else if ((view instanceof ImageView) && ((ImageView) view).getDrawable() != null) {
                    doValidViewAction(view, view2);
                }
                if ((view instanceof ViewGroup) && z && (children = ViewUtils.getChildren((ViewGroup) view)) != null) {
                    int length = children.length;
                    int i = 0;
                    while (i < length) {
                        View view3 = children[i];
                        if (view3 != null) {
                            calculateStatus(view3, view2);
                            i++;
                        } else {
                            return;
                        }
                    }
                }
            } else {
                this.stopImmediately = true;
            }
        }
    }

    private void doValidViewAction(View view, View view2) {
        this.pagePercentCalculate.calculate(view);
        String createViewType = ViewStatusUtils.createViewType(view);
        String createViewLocation = ViewStatusUtils.createViewLocation(view2, view);
        String createViewStatus = ViewStatusUtils.createViewStatus(view);
        String createViewKey = ViewStatusUtils.createViewKey(view);
        String str = createViewType + createViewLocation + createViewStatus;
        String str2 = createViewType + createViewKey + createViewStatus;
        String str3 = createViewType + createViewKey;
        String createViewPath = ViewStatusUtils.createViewPath(view2, view);
        if (ViewUtils.isInVisibleArea(view, view2) && !this.typeKeyStatusMap.containsKey(str2)) {
            if (this.oldViews.containsKey(str3)) {
                if (!this.typeKeyStatusMap.containsKey(str2)) {
                    this.lastChangedTime = TimeUtils.currentTimeMillis();
                    Logger.d(TAG, createViewPath + Operators.SPACE_STR + str);
                }
            } else if (!this.moveViewCacheSet.contains(createViewPath) && !this.typeLocationStatusSet.contains(str)) {
                this.lastChangedTime = TimeUtils.currentTimeMillis();
                Logger.d(TAG, createViewPath + Operators.SPACE_STR + str);
            }
        }
        Integer num = this.oldViews.get(str3);
        if (num == null) {
            this.oldViews.put(str3, 1);
            num = 1;
        }
        String str4 = this.typeKeyStatusMap.get(str2);
        if (!createViewLocation.equals(str4) && !TextUtils.isEmpty(str4)) {
            Integer valueOf = Integer.valueOf(num.intValue() + 1);
            this.oldViews.put(str3, valueOf);
            if (valueOf.intValue() > 2) {
                this.moveViewCacheSet.add(createViewPath);
            }
        }
        this.typeKeyStatusMap.put(str2, createViewLocation);
        this.typeLocationStatusSet.add(str);
    }

    private boolean inBlackList(View view) {
        for (BlackViewInfo next : BLACK_VIEW_INFO_LIST) {
            if ((next.pageName.equals("*") || this.pageName.endsWith(next.pageName)) && ((view.getId() == next.viewId || next.viewId == -1) && (next.viewType.equals("*") || next.viewType.equals(view.getClass().getSimpleName())))) {
                return true;
            }
        }
        return false;
    }

    private static int id(String str) {
        try {
            return Global.instance().context().getResources().getIdentifier(str, "id", Global.instance().context().getPackageName());
        } catch (Exception unused) {
            return -1;
        }
    }

    /* access modifiers changed from: package-private */
    public void visibleEndByType(String str) {
        if (!this.stopped) {
            stop();
        }
    }

    private static class BlackViewInfo {
        /* access modifiers changed from: private */
        public String pageName;
        /* access modifiers changed from: private */
        public int viewId;
        /* access modifiers changed from: private */
        public String viewType;

        public BlackViewInfo(String str, int i, String str2) {
            this.pageName = str;
            this.viewId = i;
            this.viewType = str2;
        }
    }
}
