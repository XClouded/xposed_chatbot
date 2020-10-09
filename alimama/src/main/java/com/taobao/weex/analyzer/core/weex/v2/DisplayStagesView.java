package com.taobao.weex.analyzer.core.weex.v2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.module.WXPerformanceModule;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.weex.v2.PerformanceV2Repository;
import com.taobao.weex.analyzer.view.chart.GanttChartView;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import com.taobao.weex.performance.WXInstanceApm;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DisplayStagesView extends AbstractBizItemView<PerformanceV2Repository.APMInfo> {
    private TextView mBundleUrlView;
    private GanttChartView mGanttChartView;

    public DisplayStagesView(Context context) {
        super(context);
    }

    public DisplayStagesView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DisplayStagesView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        this.mGanttChartView = (GanttChartView) findViewById(R.id.gantt);
        this.mBundleUrlView = (TextView) findViewById(R.id.bundle_url);
    }

    /* access modifiers changed from: package-private */
    public void render(@NonNull PerformanceV2Repository.APMInfo aPMInfo) {
        PerformanceV2Repository.APMInfo aPMInfo2 = aPMInfo;
        Map<String, Object> map = aPMInfo2.stageMap;
        if (!map.isEmpty()) {
            LinkedList linkedList = new LinkedList();
            Long l = (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_START);
            Long l2 = (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_END);
            Long l3 = (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_RENDER_ORGIGIN);
            Long l4 = (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_LOAD_BUNDLE_END);
            Long l5 = (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_FIRST_INTERACTION_VIEW);
            Long l6 = (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_INTERACTION);
            Long l7 = (Long) map.get(WXPerformanceModule.KEY_STAGE_JS_ASYNC_DATA_START);
            Long l8 = (Long) map.get(WXPerformanceModule.KEY_STAGE_JS_ASYNC_DATA_END);
            Long l9 = (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_END_EXCUTE_BUNDLE);
            Integer num = (Integer) aPMInfo2.statsMap.get(WXInstanceApm.KEY_PAGE_STATS_LAYOUT_TIME);
            Integer num2 = (Integer) aPMInfo2.statsMap.get(WXInstanceApm.KEY_PAGE_STATS_COMPONENT_CREATE_COST);
            Integer num3 = (Integer) aPMInfo2.statsMap.get(WXInstanceApm.KEY_PAGE_STATS_VIEW_CREATE_COST);
            Integer num4 = (Integer) aPMInfo2.statsMap.get(WXInstanceApm.KEY_PAGE_STATS_EXECUTE_JS_CALLBACK_COST);
            Long judgeOffset = judgeOffset(Arrays.asList(new Long[]{l, l2, l3, (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_LOAD_BUNDLE_START), l4, l5, (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_CREATE_FINISH), (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_FSRENDER), (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_NEW_FSRENDER), l6, (Long) map.get(WXInstanceApm.KEY_PAGE_STAGES_DESTROY)}));
            if (judgeOffset == null) {
                Toast.makeText(getContext(), "数据获取失败!", 0).show();
                return;
            }
            if (l6 != null) {
                linkedList.add(new GanttChartView.Data((int) ((l != null ? l : judgeOffset).longValue() - judgeOffset.longValue()), (int) (l6.longValue() - judgeOffset.longValue()), "可交互时间"));
            }
            if (!(l2 == null || l == null)) {
                linkedList.add(new GanttChartView.Data((int) (l.longValue() - judgeOffset.longValue()), (int) (l2.longValue() - judgeOffset.longValue()), "请求资源"));
            }
            if (!(l4 == null || l3 == null)) {
                linkedList.add(new GanttChartView.Data((int) (l3.longValue() - judgeOffset.longValue()), (int) (l4.longValue() - judgeOffset.longValue()), "处理bundle"));
            }
            if (!(l5 == null || l4 == null)) {
                linkedList.add(new GanttChartView.Data((int) (l4.longValue() - judgeOffset.longValue()), (int) (l5.longValue() - judgeOffset.longValue()), "第一个view出现"));
            }
            if (!(l8 == null || l7 == null)) {
                linkedList.add(new GanttChartView.Data((int) (l7.longValue() - judgeOffset.longValue()), (int) (l8.longValue() - judgeOffset.longValue()), "业务异步请求时间"));
            }
            if (!(l9 == null || l4 == null)) {
                linkedList.add(new GanttChartView.Data((int) (l4.longValue() - judgeOffset.longValue()), (int) (l9.longValue() - judgeOffset.longValue()), "jsBundle执行时间"));
            }
            if (num != null) {
                linkedList.add(new GanttChartView.Data(0, num.intValue(), "layout耗时(首屏累计)"));
            }
            if (num2 != null) {
                linkedList.add(new GanttChartView.Data(0, num2.intValue(), "component创建耗时(首屏累计)"));
            }
            if (num3 != null) {
                linkedList.add(new GanttChartView.Data(0, num3.intValue(), "view创建耗时(首屏累计)"));
            }
            if (num4 != null) {
                linkedList.add(new GanttChartView.Data(0, num4.intValue(), "jsCallBack执行耗时(首屏累计,包含jsdomdiff)"));
            }
            if (this.mGanttChartView != null) {
                this.mGanttChartView.setData(linkedList);
            }
        }
    }

    private Long judgeOffset(List<Long> list) {
        Long l = Long.MAX_VALUE;
        for (Long next : list) {
            if (next != null) {
                l = Long.valueOf(Math.min(next.longValue(), l.longValue()));
            }
        }
        if (l.longValue() == Long.MAX_VALUE) {
            return null;
        }
        return l;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_display_stages;
    }
}
