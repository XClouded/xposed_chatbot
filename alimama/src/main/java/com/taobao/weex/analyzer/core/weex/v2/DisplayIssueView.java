package com.taobao.weex.analyzer.core.weex.v2;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.weex.v2.PerformanceV2Repository;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.highlight.MutipleViewHighlighter;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DisplayIssueView extends AbstractBizItemView<PerformanceV2Repository.APMInfo> {
    private IssueListAdapter mAdapter;

    public DisplayIssueView(Context context) {
        super(context);
    }

    public DisplayIssueView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DisplayIssueView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAdapter = new IssueListAdapter();
        recyclerView.setAdapter(this.mAdapter);
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_display_stats;
    }

    private class IssueItemData {
        String component;
        String instanceId;
        String msg;
        String title;

        private IssueItemData() {
        }
    }

    /* access modifiers changed from: package-private */
    public void render(@NonNull PerformanceV2Repository.APMInfo aPMInfo, String str) {
        if (this.mAdapter != null && !aPMInfo.detailMap.isEmpty()) {
            LinkedList linkedList = new LinkedList();
            for (Map.Entry next : aPMInfo.detailMap.entrySet()) {
                IssueItemData issueItemData = new IssueItemData();
                issueItemData.title = transfer((String) next.getKey());
                if (next.getValue() != null) {
                    issueItemData.msg = next.getValue().toString();
                } else {
                    issueItemData.msg = "NA";
                }
                issueItemData.component = "NA";
                issueItemData.instanceId = str;
                linkedList.add(issueItemData);
            }
            this.mAdapter.setDataSource(linkedList);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0037 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003a A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003d A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String transfer(java.lang.String r3) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = -924041585(0xffffffffc8ec3e8f, float:-483828.47)
            if (r0 == r1) goto L_0x0028
            r1 = 35167668(0x2189db4, float:1.1212455E-37)
            if (r0 == r1) goto L_0x001e
            r1 = 159531639(0x9824277, float:3.1358865E-33)
            if (r0 == r1) goto L_0x0014
            goto L_0x0032
        L_0x0014:
            java.lang.String r0 = "wxWrongImgSizeCount"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 2
            goto L_0x0033
        L_0x001e:
            java.lang.String r0 = "wxLargeImgMaxCount"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 1
            goto L_0x0033
        L_0x0028:
            java.lang.String r0 = "wxCellExceedNum"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 0
            goto L_0x0033
        L_0x0032:
            r0 = -1
        L_0x0033:
            switch(r0) {
                case 0: goto L_0x003d;
                case 1: goto L_0x003a;
                case 2: goto L_0x0037;
                default: goto L_0x0036;
            }
        L_0x0036:
            return r3
        L_0x0037:
            java.lang.String r3 = "view/图片尺寸不匹配"
            return r3
        L_0x003a:
            java.lang.String r3 = "大图"
            return r3
        L_0x003d:
            java.lang.String r3 = "大cell"
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.core.weex.v2.DisplayIssueView.transfer(java.lang.String):java.lang.String");
    }

    private static class IssueListAdapter extends RecyclerView.Adapter<IssueViewHolder> {
        private List<IssueItemData> list = new LinkedList();

        IssueListAdapter() {
        }

        /* access modifiers changed from: package-private */
        public void setDataSource(List<IssueItemData> list2) {
            this.list.clear();
            this.list.addAll(list2);
            notifyDataSetChanged();
        }

        public IssueViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new IssueViewHolder(createItemView(viewGroup.getContext(), viewGroup, i));
        }

        public void onBindViewHolder(IssueViewHolder issueViewHolder, int i) {
            issueViewHolder.bind(this.list.get(i));
        }

        public int getItemCount() {
            return this.list.size();
        }

        private View createItemView(Context context, ViewGroup viewGroup, int i) {
            return LayoutInflater.from(context).inflate(R.layout.wxt_display_issue_item_view, viewGroup, false);
        }
    }

    static class IssueViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public IssueItemData data;
        /* access modifiers changed from: private */
        public boolean highLightFlag = false;
        private TextView mComponentText;
        private TextView mMsgText;
        private TextView mTitleText;
        /* access modifiers changed from: private */
        public MutipleViewHighlighter mViewHighlighter;

        IssueViewHolder(View view) {
            super(view);
            this.mTitleText = (TextView) view.findViewById(R.id.issue_title);
            this.mComponentText = (TextView) view.findViewById(R.id.issue_component_ref);
            this.mMsgText = (TextView) view.findViewById(R.id.issue_msg);
            this.mViewHighlighter = MutipleViewHighlighter.newInstance();
            this.mViewHighlighter.setColor(Color.parseColor("#420000ff"));
            this.mMsgText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int i;
                    View findViewByRef;
                    if (IssueViewHolder.this.highLightFlag) {
                        boolean unused = IssueViewHolder.this.highLightFlag = false;
                        IssueViewHolder.this.mViewHighlighter.clearHighlight();
                    }
                    if (IssueViewHolder.this.data != null && IssueViewHolder.this.data.msg != null) {
                        Toast.makeText(view.getContext(), IssueViewHolder.this.data.msg, 0).show();
                        int indexOf = IssueViewHolder.this.data.msg.indexOf("ref:");
                        int indexOf2 = IssueViewHolder.this.data.msg.indexOf(",[");
                        if (indexOf != -1 && indexOf2 != -1 && (i = indexOf + 4) <= indexOf2 && (findViewByRef = ViewUtils.findViewByRef(IssueViewHolder.this.data.instanceId, IssueViewHolder.this.data.msg.substring(i, indexOf2))) != null) {
                            boolean unused2 = IssueViewHolder.this.highLightFlag = true;
                            IssueViewHolder.this.mViewHighlighter.addHighlightedView(findViewByRef);
                        }
                    }
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void bind(IssueItemData issueItemData) {
            this.mTitleText.setText(issueItemData.title);
            this.mComponentText.setText(issueItemData.component);
            this.mMsgText.setText(issueItemData.msg);
            this.data = issueItemData;
        }
    }
}
