package com.taobao.weex.analyzer.core.weex.v2;

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
import com.taobao.weex.analyzer.core.weex.v2.PerformanceV2Repository;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DisplayStatsView extends AbstractBizItemView<PerformanceV2Repository.APMInfo> {
    private Adapter mAdapter;

    public DisplayStatsView(Context context) {
        super(context);
    }

    public DisplayStatsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DisplayStatsView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAdapter = new Adapter();
        recyclerView.setAdapter(this.mAdapter);
    }

    /* access modifiers changed from: package-private */
    public void render(@NonNull PerformanceV2Repository.APMInfo aPMInfo) {
        if (this.mAdapter != null && !aPMInfo.statsMap.isEmpty()) {
            LinkedList linkedList = new LinkedList();
            for (Map.Entry next : aPMInfo.statsMap.entrySet()) {
                Item item = new Item();
                item.title = translation((String) next.getKey());
                if (next.getValue() != null) {
                    item.value = next.getValue().toString();
                } else {
                    item.value = "NA";
                }
                linkedList.add(item);
            }
            this.mAdapter.setDataSource(linkedList);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String translation(java.lang.String r2) {
        /*
            r1 = this;
            int r0 = r2.hashCode()
            switch(r0) {
                case -1829434809: goto L_0x016c;
                case -1799419369: goto L_0x0162;
                case -1613960083: goto L_0x0157;
                case -1522216827: goto L_0x014d;
                case -1454901385: goto L_0x0142;
                case -1389118539: goto L_0x0137;
                case -1192893744: goto L_0x012d;
                case -924041585: goto L_0x0122;
                case -905186284: goto L_0x0117;
                case -851766761: goto L_0x010c;
                case -221797057: goto L_0x0100;
                case -190174802: goto L_0x00f4;
                case -180477236: goto L_0x00e8;
                case -157562807: goto L_0x00dc;
                case 35167668: goto L_0x00d0;
                case 52741724: goto L_0x00c5;
                case 76147416: goto L_0x00ba;
                case 152518402: goto L_0x00ae;
                case 159531639: goto L_0x00a2;
                case 195539510: goto L_0x0096;
                case 256201757: goto L_0x008a;
                case 434423213: goto L_0x007e;
                case 531099982: goto L_0x0073;
                case 665844068: goto L_0x0068;
                case 744667320: goto L_0x005c;
                case 837982100: goto L_0x0050;
                case 848410853: goto L_0x0045;
                case 1275929562: goto L_0x0039;
                case 1367270232: goto L_0x002d;
                case 1799424239: goto L_0x0021;
                case 1959880903: goto L_0x0015;
                case 2048793000: goto L_0x0009;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x0177
        L_0x0009:
            java.lang.String r0 = "wxBodyRatio"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 15
            goto L_0x0178
        L_0x0015:
            java.lang.String r0 = "wxImgLoadCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 24
            goto L_0x0178
        L_0x0021:
            java.lang.String r0 = "wxNetworkRequestFailCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 29
            goto L_0x0178
        L_0x002d:
            java.lang.String r0 = "wxJSLibInitTime"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 30
            goto L_0x0178
        L_0x0039:
            java.lang.String r0 = "wxInteractionAllViewCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 20
            goto L_0x0178
        L_0x0045:
            java.lang.String r0 = "wxFSCallNativeTotalNum"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 5
            goto L_0x0178
        L_0x0050:
            java.lang.String r0 = "wxScrollerCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 16
            goto L_0x0178
        L_0x005c:
            java.lang.String r0 = "wxJSDataPrefetchSuccess"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 31
            goto L_0x0178
        L_0x0068:
            java.lang.String r0 = "wxBundleSize"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 0
            goto L_0x0178
        L_0x0073:
            java.lang.String r0 = "wxFSCallNativeTotalTime"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 4
            goto L_0x0178
        L_0x007e:
            java.lang.String r0 = "wxInteractionScreenViewCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 19
            goto L_0x0178
        L_0x008a:
            java.lang.String r0 = "wxMaxDeepViewLayer"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 9
            goto L_0x0178
        L_0x0096:
            java.lang.String r0 = "wxMaxDeepVDomLayer"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 10
            goto L_0x0178
        L_0x00a2:
            java.lang.String r0 = "wxWrongImgSizeCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 12
            goto L_0x0178
        L_0x00ae:
            java.lang.String r0 = "wxCellDataUnRecycleCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 17
            goto L_0x0178
        L_0x00ba:
            java.lang.String r0 = "wxFSTimerCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 3
            goto L_0x0178
        L_0x00c5:
            java.lang.String r0 = "wxFSCallJsTotalTime"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 1
            goto L_0x0178
        L_0x00d0:
            java.lang.String r0 = "wxLargeImgMaxCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 14
            goto L_0x0178
        L_0x00dc:
            java.lang.String r0 = "wxImgLoadFailCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 26
            goto L_0x0178
        L_0x00e8:
            java.lang.String r0 = "wxActualNetworkTime"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 23
            goto L_0x0178
        L_0x00f4:
            java.lang.String r0 = "wxNetworkRequestSuccessCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 28
            goto L_0x0178
        L_0x0100:
            java.lang.String r0 = "wxTimerInBackCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 22
            goto L_0x0178
        L_0x010c:
            java.lang.String r0 = "wxCellUnReUseCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 18
            goto L_0x0178
        L_0x0117:
            java.lang.String r0 = "wxImgLoadSuccessCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 25
            goto L_0x0178
        L_0x0122:
            java.lang.String r0 = "wxCellExceedNum"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 8
            goto L_0x0178
        L_0x012d:
            java.lang.String r0 = "wxFSCallEventTotalNum"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 6
            goto L_0x0178
        L_0x0137:
            java.lang.String r0 = "wxMaxComponentCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 11
            goto L_0x0178
        L_0x0142:
            java.lang.String r0 = "wxEmbedCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 13
            goto L_0x0178
        L_0x014d:
            java.lang.String r0 = "wxFSRequestNum"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 7
            goto L_0x0178
        L_0x0157:
            java.lang.String r0 = "wxNetworkRequestCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 27
            goto L_0x0178
        L_0x0162:
            java.lang.String r0 = "wxFSCallJsTotalNum"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 2
            goto L_0x0178
        L_0x016c:
            java.lang.String r0 = "wxInteractionComponentCreateCount"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0177
            r0 = 21
            goto L_0x0178
        L_0x0177:
            r0 = -1
        L_0x0178:
            switch(r0) {
                case 0: goto L_0x01d9;
                case 1: goto L_0x01d6;
                case 2: goto L_0x01d3;
                case 3: goto L_0x01d0;
                case 4: goto L_0x01cd;
                case 5: goto L_0x01ca;
                case 6: goto L_0x01c7;
                case 7: goto L_0x01c4;
                case 8: goto L_0x01c1;
                case 9: goto L_0x01be;
                case 10: goto L_0x01bb;
                case 11: goto L_0x01b8;
                case 12: goto L_0x01b5;
                case 13: goto L_0x01b2;
                case 14: goto L_0x01af;
                case 15: goto L_0x01ac;
                case 16: goto L_0x01a9;
                case 17: goto L_0x01a6;
                case 18: goto L_0x01a3;
                case 19: goto L_0x01a0;
                case 20: goto L_0x019d;
                case 21: goto L_0x019a;
                case 22: goto L_0x0197;
                case 23: goto L_0x0194;
                case 24: goto L_0x0191;
                case 25: goto L_0x018e;
                case 26: goto L_0x018b;
                case 27: goto L_0x0188;
                case 28: goto L_0x0185;
                case 29: goto L_0x0182;
                case 30: goto L_0x017f;
                case 31: goto L_0x017c;
                default: goto L_0x017b;
            }
        L_0x017b:
            return r2
        L_0x017c:
            java.lang.String r2 = "data prefetch是否成功"
            return r2
        L_0x017f:
            java.lang.String r2 = "JSFM初始化时间"
            return r2
        L_0x0182:
            java.lang.String r2 = "网络请求失败次数"
            return r2
        L_0x0185:
            java.lang.String r2 = "网络请求成功次数"
            return r2
        L_0x0188:
            java.lang.String r2 = "网络请求次数"
            return r2
        L_0x018b:
            java.lang.String r2 = "图片加载失败个数"
            return r2
        L_0x018e:
            java.lang.String r2 = "图片加载成功个数"
            return r2
        L_0x0191:
            java.lang.String r2 = "图片加载个数"
            return r2
        L_0x0194:
            java.lang.String r2 = "网络库打点的bundle下载时间"
            return r2
        L_0x0197:
            java.lang.String r2 = "后台执行timer次数"
            return r2
        L_0x019a:
            java.lang.String r2 = "首屏时间内,创建component个数"
            return r2
        L_0x019d:
            java.lang.String r2 = "首屏时间内,总共add view的次数"
            return r2
        L_0x01a0:
            java.lang.String r2 = "首屏时间内,屏幕内add view次数"
            return r2
        L_0x01a3:
            java.lang.String r2 = "没有开启复用cell的个数"
            return r2
        L_0x01a6:
            java.lang.String r2 = "内容不回收的cell组件个数"
            return r2
        L_0x01a9:
            java.lang.String r2 = "使用scroller个数"
            return r2
        L_0x01ac:
            java.lang.String r2 = "wx页面的屏占比，[0-100]"
            return r2
        L_0x01af:
            java.lang.String r2 = "大图个数（最多那次）"
            return r2
        L_0x01b2:
            java.lang.String r2 = "embed个数"
            return r2
        L_0x01b5:
            java.lang.String r2 = "图片和view大小不匹配个数"
            return r2
        L_0x01b8:
            java.lang.String r2 = "组件个数（最多那次）"
            return r2
        L_0x01bb:
            java.lang.String r2 = "dom结点最大层级"
            return r2
        L_0x01be:
            java.lang.String r2 = "view最大层级"
            return r2
        L_0x01c1:
            java.lang.String r2 = "大cell个数"
            return r2
        L_0x01c4:
            java.lang.String r2 = "首屏网络请求次数"
            return r2
        L_0x01c7:
            java.lang.String r2 = "首屏时间event次数"
            return r2
        L_0x01ca:
            java.lang.String r2 = "首屏调用native次数"
            return r2
        L_0x01cd:
            java.lang.String r2 = "首屏调用native时间"
            return r2
        L_0x01d0:
            java.lang.String r2 = "首屏调用timer次数"
            return r2
        L_0x01d3:
            java.lang.String r2 = "首屏调用js次数"
            return r2
        L_0x01d6:
            java.lang.String r2 = "首屏调用js耗时"
            return r2
        L_0x01d9:
            java.lang.String r2 = "bundle大小"
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.core.weex.v2.DisplayStatsView.translation(java.lang.String):java.lang.String");
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_display_stats;
    }

    static class Item {
        String title;
        String value;

        Item() {
        }
    }

    static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Item> list = new LinkedList();

        Adapter() {
        }

        /* access modifiers changed from: package-private */
        public void setDataSource(List<Item> list2) {
            this.list.clear();
            this.list.addAll(list2);
            notifyDataSetChanged();
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(createItemView(viewGroup.getContext(), viewGroup, i));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.bind(this.list.get(i));
        }

        public int getItemCount() {
            return this.list.size();
        }

        private View createItemView(Context context, ViewGroup viewGroup, int i) {
            return LayoutInflater.from(context).inflate(R.layout.wxt_display_stats_item_view, viewGroup, false);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleText;
        private TextView mValueText;

        ViewHolder(View view) {
            super(view);
            this.mValueText = (TextView) view.findViewById(R.id.value);
            this.mTitleText = (TextView) view.findViewById(R.id.title);
        }

        /* access modifiers changed from: package-private */
        public void bind(Item item) {
            this.mValueText.setText(item.value);
            this.mTitleText.setText(item.title);
        }
    }
}
