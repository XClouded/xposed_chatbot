package com.taobao.weex.analyzer.core.lint;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.AbstractLoopTask;
import com.taobao.weex.analyzer.core.lint.DomTracker;
import com.taobao.weex.analyzer.pojo.HealthReport;
import com.taobao.weex.analyzer.view.highlight.MutipleViewHighlighter;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.PermissionOverlayView;
import com.taobao.weex.ui.component.WXComponent;

public class ProfileDomView extends PermissionOverlayView {
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    private SampleTask mTask;

    public ProfileDomView(Context context, Config config) {
        super(context, true, config);
        this.mWidth = -1;
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_RENDER_ANALYSIS);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        View inflate = View.inflate(this.mContext, R.layout.wxt_depth_sample_view, (ViewGroup) null);
        inflate.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ProfileDomView.this.isViewAttached && ProfileDomView.this.mOnCloseListener != null) {
                    ProfileDomView.this.mOnCloseListener.close(ProfileDomView.this);
                    ProfileDomView.this.dismiss();
                }
            }
        });
        return inflate;
    }

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
        }
        this.mTask = new SampleTask(this.mWholeView);
        this.mTask.start();
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
        }
    }

    public void bindInstance(WXSDKInstance wXSDKInstance) {
        if (this.mTask != null) {
            this.mTask.setInstance(wXSDKInstance);
        }
    }

    private static class SampleTask extends AbstractLoopTask implements DomTracker.OnTrackNodeListener {
        static final int MAX_REAL_DOM_LAYER = 20;
        static final int MAX_VDOM_LAYER = 14;
        WXSDKInstance instance;
        MutipleViewHighlighter mViewHighlighter = MutipleViewHighlighter.newInstance();
        TextView resultTextView;

        private String convertResult(boolean z) {
            return z ? "✓ " : "✕ ";
        }

        SampleTask(@NonNull View view) {
            super(false);
            this.mDelayMillis = 1000;
            this.resultTextView = (TextView) view.findViewById(R.id.result);
            this.mViewHighlighter.setColor(Color.parseColor("#420000ff"));
        }

        /* access modifiers changed from: package-private */
        public void setInstance(WXSDKInstance wXSDKInstance) {
            this.instance = wXSDKInstance;
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            if (this.instance != null) {
                DomTracker domTracker = new DomTracker(this.instance);
                domTracker.setOnTrackNodeListener(this);
                HealthReport traverse = domTracker.traverse();
                if (traverse != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(convertResult(traverse.maxLayerOfRealDom < 20));
                    sb.append("检测到native最深嵌套层级为 ");
                    sb.append(traverse.maxLayerOfRealDom);
                    sb.append("(仅统计weex自身渲染出来的层级)");
                    sb.append("\n");
                    boolean z = traverse.maxLayer >= 14;
                    sb.append(convertResult(!z));
                    sb.append("检测到VDOM最深嵌套层级为 ");
                    sb.append(traverse.maxLayer);
                    sb.append(",建议<14");
                    if (z && this.mViewHighlighter != null && this.mViewHighlighter.isSupport()) {
                        sb.append(",深层嵌套已高亮透出");
                    }
                    sb.append("\n");
                    if (traverse.hasScroller) {
                        sb.append(convertResult(true));
                        sb.append("检测到该页面使用了纵向的Scroller,长列表建议使用ListView");
                        sb.append("\n");
                    }
                    if (traverse.hasList && traverse.listDescMap != null) {
                        int size = traverse.listDescMap.size();
                        sb.append(convertResult(true));
                        sb.append("检测到该页面使用了List,共");
                        sb.append(size);
                        sb.append("个");
                        sb.append("\n");
                        for (HealthReport.ListDesc next : traverse.listDescMap.values()) {
                            sb.append(convertResult(true));
                            sb.append("检测到ref为'");
                            sb.append(next.ref);
                            sb.append("'的list,其cell个数为");
                            sb.append(next.cellNum);
                            sb.append("\n");
                        }
                        sb.append(convertResult(!traverse.hasBigCell));
                        if (traverse.hasBigCell) {
                            sb.append("检测到页面可能存在大cell,最大的cell中包含");
                            sb.append(traverse.componentNumOfBigCell);
                            sb.append("个组件,建议按行合理拆分");
                            sb.append("\n");
                        } else {
                            sb.append("经检测，cell大小合理");
                            sb.append("\n");
                        }
                    }
                    if (traverse.hasEmbed) {
                        int size2 = traverse.embedDescList.size();
                        sb.append(convertResult(true));
                        sb.append("检测到该页面使用了Embed标签");
                        sb.append(",");
                        sb.append("该标签数目为");
                        sb.append(size2);
                        sb.append("\n");
                        int i = 0;
                        while (i < size2) {
                            HealthReport.EmbedDesc embedDesc = traverse.embedDescList.get(i);
                            sb.append(convertResult(!(embedDesc.actualMaxLayer >= 14)));
                            sb.append("第");
                            i++;
                            sb.append(i);
                            sb.append("个embed标签地址为");
                            sb.append(embedDesc.src);
                            sb.append(",内容最深嵌套层级为");
                            sb.append(embedDesc.actualMaxLayer);
                            sb.append("\n");
                        }
                    }
                    runOnUIThread(new Runnable() {
                        public void run() {
                            SampleTask.this.resultTextView.setText(sb.toString());
                        }
                    });
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            this.instance = null;
            if (this.mViewHighlighter != null) {
                this.mViewHighlighter.clearHighlight();
            }
        }

        public void onTrackNode(@NonNull WXComponent wXComponent, int i) {
            View hostView;
            if (i >= 14 && (hostView = wXComponent.getHostView()) != null && this.mViewHighlighter != null) {
                this.mViewHighlighter.addHighlightedView(hostView);
            }
        }
    }
}
