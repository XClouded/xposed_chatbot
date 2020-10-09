package com.taobao.weex.analyzer.core.weex;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WXPerfItemView extends AbstractBizItemView<Performance> {
    private RecyclerView mPerformanceList;

    public WXPerfItemView(Context context) {
        super(context);
    }

    public WXPerfItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WXPerfItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        this.mPerformanceList = (RecyclerView) findViewById(R.id.overlay_list);
        this.mPerformanceList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_panel_cur_perf_view;
    }

    public void inflateData(Performance performance) {
        this.mPerformanceList.setAdapter(new PerformanceViewAdapter(getContext(), performance));
    }

    private static class PerformanceViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;
        private List<String> mValues;
        private Performance rawPerformance;

        public int getItemViewType(int i) {
            return i == 0 ? 100 : 101;
        }

        PerformanceViewAdapter(@NonNull Context context, @NonNull Performance performance) {
            this.mContext = context;
            this.rawPerformance = performance;
            this.mValues = transfer(performance);
        }

        private List<String> transfer(Performance performance) {
            Map<String, Double> measureMap = performance.getMeasureMap();
            ArrayList arrayList = new ArrayList();
            for (Map.Entry next : measureMap.entrySet()) {
                arrayList.add(((String) next.getKey()) + " : " + next.getValue());
            }
            return arrayList;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(createItemView(this.mContext, viewGroup, i), i);
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (!(viewHolder instanceof ViewHolder)) {
                return;
            }
            if (viewHolder.getItemViewType() == 101) {
                ((ViewHolder) viewHolder).bind(this.mValues.get(i - 1));
            } else {
                ((ViewHolder) viewHolder).bindHeader(this.rawPerformance);
            }
        }

        public int getItemCount() {
            return this.mValues.size() + 1;
        }

        /* access modifiers changed from: package-private */
        public View createItemView(Context context, ViewGroup viewGroup, int i) {
            if (i != 101) {
                return LayoutInflater.from(context).inflate(R.layout.wxt_cur_perf_header, viewGroup, false);
            }
            TextView textView = new TextView(context);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-1, -2);
            int dip2px = WXViewUtils.dip2px(5.0f);
            layoutParams.bottomMargin = dip2px;
            layoutParams.topMargin = dip2px;
            TextView textView2 = textView;
            textView2.setGravity(19);
            textView2.setTextSize(2, 14.0f);
            textView.setLayoutParams(layoutParams);
            textView.setPadding((int) ViewUtils.dp2px(context, 15), 0, 0, 0);
            textView2.setTextColor(-16777216);
            return textView;
        }
    }

    private static class ViewType {
        static final int TYPE_HEADER = 100;
        static final int TYPE_ITEM = 101;

        private ViewType() {
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView interactionTime;
        private TextView jsTemplateSizeView;
        private TextView jsfmVersionView;
        private TextView mValueText;
        private TextView networkTime;
        private TextView pageNameView;
        private TextView renderTimeView;
        private TextView sdkInitTime;
        private TextView sdkVersionView;

        ViewHolder(View view, int i) {
            super(view);
            if (i == 101) {
                this.mValueText = (TextView) view;
                return;
            }
            this.jsfmVersionView = (TextView) view.findViewById(R.id.text_jsfm_version);
            this.sdkVersionView = (TextView) view.findViewById(R.id.text_version_sdk);
            this.renderTimeView = (TextView) view.findViewById(R.id.text_screen_render_time);
            this.interactionTime = (TextView) view.findViewById(R.id.text_interaction_time);
            this.sdkInitTime = (TextView) view.findViewById(R.id.text_sdk_init_time);
            this.networkTime = (TextView) view.findViewById(R.id.text_network_time);
            this.pageNameView = (TextView) view.findViewById(R.id.page_name);
            this.jsTemplateSizeView = (TextView) view.findViewById(R.id.text_template_size);
        }

        /* access modifiers changed from: package-private */
        public void bind(@NonNull String str) {
            this.mValueText.setText(str);
        }

        /* access modifiers changed from: package-private */
        public void bindHeader(Performance performance) {
            Map<String, String> dimensionMap = performance.getDimensionMap();
            Map<String, Double> measureMap = performance.getMeasureMap();
            if (dimensionMap != null && measureMap != null) {
                TextView textView = this.pageNameView;
                textView.setText("页面名称: " + dimensionMap.get(WXPerformance.Dimension.pageName.toString()));
                TextView textView2 = this.sdkVersionView;
                textView2.setText("Weex Sdk版本: " + dimensionMap.get(WXPerformance.Dimension.WXSDKVersion.toString()) + "");
                TextView textView3 = this.sdkInitTime;
                textView3.setText("Weex SDK初始化时间 : " + measureMap.get(WXPerformance.Measure.SDKInitTime.toString()) + "ms");
                TextView textView4 = this.jsfmVersionView;
                textView4.setText("JSFramework版本 : " + dimensionMap.get(WXPerformance.Dimension.JSLibVersion.toString()) + "");
                TextView textView5 = this.renderTimeView;
                textView5.setText("首屏时间 : " + measureMap.get(WXPerformance.Measure.fsRenderTime.toString()) + "ms");
                TextView textView6 = this.interactionTime;
                textView6.setText("可交互时间: " + measureMap.get(WXPerformance.Measure.interactionTime.toString()) + "ms");
                TextView textView7 = this.networkTime;
                textView7.setText("网络时间 : " + measureMap.get(WXPerformance.Measure.networkTime.toString()) + "ms");
                TextView textView8 = this.jsTemplateSizeView;
                textView8.setText("jsBundle大小 : " + measureMap.get(WXPerformance.Measure.JSTemplateSize.toString()) + "KB");
            }
        }
    }
}
