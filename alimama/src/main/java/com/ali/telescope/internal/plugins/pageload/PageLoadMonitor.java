package com.ali.telescope.internal.plugins.pageload;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.ViewTreeObserver;
import com.ali.telescope.data.PageGetter;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.util.TelescopeLog;
import java.io.Serializable;

public class PageLoadMonitor {
    static final int MSG_ON_CHECK_ACTIVITY_LOAD = 16;
    static final String TAG = "pageload@PageLoadMonitor";
    ActivityLifecycleCallback mActivityLifecycleCallback;
    int mColdBootOffsetTime = 1000;
    MyHandlerThread mHandlerThread = new MyHandlerThread("PageLoadMonitor", 0);
    volatile boolean mIsInBootStep = true;
    short mLayoutTimes;
    LoadTimeCalculate mLoadTimeCalculate;
    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    PageLoadPlugin mPageLoadPlugin;
    PageStat mPageStat;
    Handler mThreadHandler;

    public static class PageStat implements Serializable {
        long activityCreateTime;
        public int activityViewCount;
        public int activityVisibleViewCount;
        int checkSystemInfoCount;
        public short firstRelativeLayoutDepth;
        public int idleTime = 0;
        public boolean isColdOpen = false;
        public short layoutTimesOnLoad;
        public long loadStartTime = 0;
        public int loadTime = 0;
        public short maxLayoutDepth;
        public long maxLayoutUseTime;
        public short maxRelativeLayoutDepth;
        public short measureTimes;
        public String pageHashCode = "";
        public String pageName = "";
        public short redundantLayout;
        public int stayTime = 0;
        public short suspectRelativeLayout;
        public short totalLayoutCount;
        public long totalLayoutUseTime;
    }

    public PageLoadMonitor(Application application, PageLoadPlugin pageLoadPlugin) {
        this.mPageLoadPlugin = pageLoadPlugin;
        this.mActivityLifecycleCallback = new ActivityLifecycleCallback(application);
        this.mLoadTimeCalculate = new LoadTimeCalculate();
        this.mLoadTimeCalculate.mPageLoadMonitor = this;
        this.mActivityLifecycleCallback.mPageLoadMonitor = this;
        this.mActivityLifecycleCallback.mLoadTimeCalculate = this.mLoadTimeCalculate;
    }

    /* access modifiers changed from: package-private */
    public void start() {
        if (this.mHandlerThread != null) {
            this.mHandlerThread.start();
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityCreate(Activity activity) {
        if (this.mPageStat == null) {
            this.mPageStat = new PageStat();
        }
        this.mPageStat.isColdOpen = true;
        onPageLoadStart(this.mActivityLifecycleCallback.mActivityOnCreate, activity);
    }

    /* access modifiers changed from: package-private */
    public void onActivityResume(Activity activity) {
        if (this.mPageStat == null) {
            this.mPageStat = new PageStat();
        }
        if (!this.mPageStat.isColdOpen) {
            onPageLoadStart(this.mActivityLifecycleCallback.mActivityOnResume, activity);
            this.mLoadTimeCalculate.needStopLoadTimeCalculate(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void onPageLoadStart(long j, Activity activity) {
        if (this.mPageStat == null) {
            this.mPageStat = new PageStat();
        }
        this.mPageStat.activityCreateTime = this.mActivityLifecycleCallback.mActivityOnCreate;
        this.mPageStat.pageName = getPageName(activity);
        this.mPageStat.pageHashCode = getPageHashCode(activity);
        this.mPageStat.loadStartTime = j;
        this.mPageStat.totalLayoutUseTime = 0;
        this.mPageStat.layoutTimesOnLoad = 0;
        this.mPageStat.maxLayoutUseTime = 0;
        this.mPageStat.measureTimes = 0;
        this.mPageStat.suspectRelativeLayout = 0;
        this.mPageStat.maxLayoutDepth = 0;
        this.mPageStat.redundantLayout = 0;
        this.mPageStat.loadTime = 0;
        this.mPageStat.firstRelativeLayoutDepth = 0;
        this.mPageStat.maxRelativeLayoutDepth = 0;
        this.mPageStat.activityViewCount = 0;
        this.mPageStat.activityVisibleViewCount = 0;
        this.mPageStat.totalLayoutCount = 0;
        this.mPageStat.checkSystemInfoCount = 0;
        this.mPageLoadPlugin.mITelescopeContext.getBeanReport().send(new PageOpenBean(activity, System.currentTimeMillis(), this.mPageStat.pageName, this.mPageStat.pageHashCode));
    }

    /* access modifiers changed from: package-private */
    public ViewTreeObserver.OnGlobalLayoutListener createOnGlobalLayoutListener(int i) {
        return new OnLineMonitorGlobalLayoutListener(i);
    }

    /* access modifiers changed from: package-private */
    public void onActivityPause(Activity activity) {
        if (this.mPageStat == null) {
            this.mPageStat = new PageStat();
        }
        if (this.mPageStat != null) {
            if (this.mPageStat.loadTime == 0) {
                this.mLoadTimeCalculate.needStopLoadTimeCalculate(true);
                if (this.mPageStat.loadTime <= 0) {
                    this.mPageStat.loadTime = 0;
                }
                this.mLoadTimeCalculate.setActivityStat(this.mPageStat);
            }
            if (this.mPageStat.idleTime <= 0) {
                this.mPageStat.idleTime = 0;
            }
            this.mPageStat.stayTime = (int) ((System.nanoTime() / 1000000) - this.mPageStat.loadStartTime);
            final PageLoadRecord pageLoadRecord = new PageLoadRecord();
            pageLoadRecord.pageName = this.mPageStat.pageName;
            pageLoadRecord.pageStartTime = this.mPageStat.loadStartTime;
            pageLoadRecord.pageLoadTime = this.mPageStat.loadTime;
            pageLoadRecord.pageStayTime = this.mPageStat.stayTime;
            TelescopeLog.w(TAG, "time cost", "pageName=" + this.mPageStat.pageName, "pageStartTime=" + this.mPageStat.loadStartTime, "stayTime=" + this.mPageStat.stayTime);
            Loopers.getTelescopeHandler().post(new Runnable() {
                public void run() {
                    synchronized (PageLoadMonitor.this.mPageLoadPlugin.mPageLoadRecords) {
                        PageLoadMonitor.this.mPageLoadPlugin.mPageLoadRecords.add(pageLoadRecord);
                    }
                }
            });
        }
        this.mPageStat.isColdOpen = false;
        this.mPageStat.loadTime = 0;
    }

    /* access modifiers changed from: package-private */
    public String getPageName(Activity activity) {
        return PageGetter.getPageName(activity, this.mPageLoadPlugin.mINameConvert);
    }

    /* access modifiers changed from: package-private */
    public String getPageHashCode(Activity activity) {
        return PageGetter.getPageHashCode(activity);
    }

    class OnLineMonitorGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int mIndex;

        public OnLineMonitorGlobalLayoutListener(int i) {
            this.mIndex = i;
        }

        public void onGlobalLayout() {
            if (PageLoadMonitor.this.mActivityLifecycleCallback == null || this.mIndex == PageLoadMonitor.this.mActivityLifecycleCallback.mCreateIndex) {
                PageLoadMonitor pageLoadMonitor = PageLoadMonitor.this;
                pageLoadMonitor.mLayoutTimes = (short) (pageLoadMonitor.mLayoutTimes + 1);
                if (PageLoadMonitor.this.mPageStat != null) {
                    PageStat pageStat = PageLoadMonitor.this.mPageStat;
                    pageStat.totalLayoutCount = (short) (pageStat.totalLayoutCount + 1);
                }
            }
        }
    }

    class MyHandlerThread extends HandlerThread {
        public MyHandlerThread(String str, int i) {
            super(str, i);
        }

        /* access modifiers changed from: protected */
        public void onLooperPrepared() {
            PageLoadMonitor.this.mThreadHandler = new Handler() {
                public void handleMessage(Message message) {
                    try {
                        if (message.what == 16) {
                            if (PageLoadMonitor.this.mLoadTimeCalculate != null) {
                                PageLoadMonitor.this.mLoadTimeCalculate.needStopLoadTimeCalculate(false);
                            }
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            };
        }
    }
}
