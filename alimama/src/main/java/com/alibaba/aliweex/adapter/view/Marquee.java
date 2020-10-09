package com.alibaba.aliweex.adapter.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import com.alibaba.aliweex.R;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"HandlerLeak"})
public class Marquee extends FrameLayout {
    private static final int sSTOP_MSG = 1;
    private Context context;
    /* access modifiers changed from: private */
    public int currentY;
    /* access modifiers changed from: private */
    public long delayTime;
    /* access modifiers changed from: private */
    public long durationTime;
    /* access modifiers changed from: private */
    public Handler handler;
    /* access modifiers changed from: private */
    public long intervalTime;
    /* access modifiers changed from: private */
    public boolean isFirst;
    /* access modifiers changed from: private */
    public boolean isStop;
    private Runnable mLogic;
    /* access modifiers changed from: private */
    public FrameLayout marqueeRealView;
    private ScrollView scrollView;
    /* access modifiers changed from: private */
    public long time;
    /* access modifiers changed from: private */
    public long times;
    /* access modifiers changed from: private */
    public int viewHeight;
    /* access modifiers changed from: private */
    public List<View> viewList;

    static /* synthetic */ long access$708(Marquee marquee) {
        long j = marquee.times;
        marquee.times = 1 + j;
        return j;
    }

    static /* synthetic */ long access$908(Marquee marquee) {
        long j = marquee.time;
        marquee.time = 1 + j;
        return j;
    }

    public Marquee(Context context2) {
        this(context2, (AttributeSet) null);
    }

    public Marquee(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        this.handler = new MHandler(Looper.getMainLooper());
        this.mLogic = new Runnable() {
            public void run() {
                if (Marquee.this.marqueeRealView.getChildCount() > 0 && Marquee.this.viewHeight != 0) {
                    if (Marquee.this.isFirst) {
                        boolean unused = Marquee.this.isFirst = false;
                        Marquee.this.postDelayedInner(this, Marquee.this.delayTime);
                    } else if (!Marquee.this.isStop) {
                        int unused2 = Marquee.this.currentY = Marquee.this.currentY + 1;
                        Marquee.this.marqueeRealView.scrollTo(0, Marquee.this.currentY);
                        if (Marquee.this.marqueeRealView.getScrollY() % Marquee.this.viewHeight == 0 && Marquee.this.handler != null) {
                            boolean unused3 = Marquee.this.isStop = true;
                            Marquee.this.handler.sendEmptyMessageDelayed(1, Marquee.this.intervalTime);
                            View childAt = Marquee.this.marqueeRealView.getChildAt(0);
                            Marquee.this.marqueeRealView.removeViewAt(0);
                            Marquee.access$708(Marquee.this);
                            if (Marquee.this.times % ((long) Marquee.this.viewList.size()) == 0) {
                                Marquee.access$908(Marquee.this);
                            }
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                            layoutParams.setMargins(0, (int) (((long) Marquee.this.viewHeight) * (((Marquee.this.times % ((long) Marquee.this.viewList.size())) - 1) + (Marquee.this.time * ((long) Marquee.this.viewList.size())))), 0, 0);
                            childAt.setLayoutParams(layoutParams);
                            Marquee.this.marqueeRealView.addView(childAt);
                        }
                        Marquee.this.postDelayedInner(this, Marquee.this.durationTime / ((long) Marquee.this.viewHeight));
                    } else {
                        Marquee.this.postInner(this);
                    }
                }
            }
        };
        this.context = context2;
        init();
    }

    public View getRealView() {
        return this.marqueeRealView;
    }

    private void init() {
        this.delayTime = 0;
        this.intervalTime = 5000;
        this.durationTime = 500;
        this.time = 1;
        this.times = 0;
        this.isFirst = true;
        this.currentY = 0;
        this.viewList = new ArrayList();
        LayoutInflater.from(this.context).inflate(R.layout.huichang_marquee_layout, this, true);
        this.scrollView = (ScrollView) findViewById(R.id.huichang_marquee_scroll_view);
        this.marqueeRealView = (FrameLayout) findViewById(R.id.huichang_marquee_layout);
    }

    /* access modifiers changed from: private */
    public void postDelayedInner(Runnable runnable, long j) {
        if (this.handler != null && j >= 0) {
            this.handler.postDelayed(runnable, j);
        }
    }

    /* access modifiers changed from: private */
    public void postInner(Runnable runnable) {
        if (this.handler != null) {
            this.handler.post(runnable);
        }
    }

    public void startScrollA() {
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages((Object) null);
            this.marqueeRealView.scrollTo(0, 0);
            this.currentY = 0;
            postDelayedInner(this.mLogic, 20);
        }
    }

    public void startScroll() {
        if (this.handler != null) {
            this.handler.removeCallbacks((Runnable) null);
            this.isStop = false;
            if (this.viewHeight > 0) {
                postDelayedInner(this.mLogic, this.durationTime / ((long) this.viewHeight));
            }
        }
    }

    public void stopScroll() {
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages((Object) null);
        }
    }

    public void setIntervalTime(long j) {
        this.intervalTime = j;
    }

    public void setDelayTime(long j) {
        this.delayTime = j;
    }

    public void setDurationTime(long j) {
        this.durationTime = j;
    }

    public void setViewList(List<View> list, FrameLayout.LayoutParams layoutParams) {
        this.handler.removeCallbacksAndMessages((Object) null);
        this.marqueeRealView.removeAllViews();
        this.viewList.clear();
        this.viewList.addAll(list);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
        layoutParams2.setMargins(0, 0, 0, 0);
        this.scrollView.setLayoutParams(layoutParams2);
        this.viewHeight = layoutParams.height;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        int size = this.viewList.size();
        for (int i = 0; i < size; i++) {
            View view = this.viewList.get(i);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(-1, view.getMeasuredHeight());
            layoutParams3.setMargins(0, this.viewHeight * this.viewList.indexOf(view), 0, 0);
            view.setLayoutParams(layoutParams3);
            view.forceLayout();
            view.requestLayout();
            this.marqueeRealView.addView(view, layoutParams3);
        }
    }

    public class MHandler extends Handler {
        public MHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                boolean unused = Marquee.this.isStop = false;
            }
        }
    }

    public void destroy() {
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages((Object) null);
        }
    }
}
