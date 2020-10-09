package com.taobao.weex.analyzer.core.weex.v2;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.weex.v2.PerformanceV2Repository;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.highlight.MutipleViewHighlighter;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.component.richtext.node.RichTextNode;
import com.taobao.weex.utils.WXUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DisplayInteractionView extends AbstractBizItemView<PerformanceV2Repository.APMInfo> {
    private static final long DEFAULT_THRESHOLD = 20;
    private View emptyView;
    /* access modifiers changed from: private */
    public boolean isFilterAll = true;
    private Adapter mAdapter;
    private List<Item> mDataSource;
    private RadioButton mFilterAll;
    private RadioGroup mFilterGroup;
    private RadioButton mFilterTimeout;

    public void onDetach() {
    }

    public DisplayInteractionView(Context context) {
        super(context);
    }

    public DisplayInteractionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DisplayInteractionView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        this.emptyView = findViewById(R.id.empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAdapter = new Adapter();
        recyclerView.setAdapter(this.mAdapter);
        TextView textView = (TextView) findViewById(R.id.tips);
        textView.setText("点击列表项可以使对应页面节点高亮");
        textView.setTextColor(SupportMenu.CATEGORY_MASK);
        this.mFilterGroup = (RadioGroup) findViewById(R.id.filter_group);
        this.mFilterAll = (RadioButton) findViewById(R.id.filter_all);
        this.mFilterTimeout = (RadioButton) findViewById(R.id.filter_timeout);
        this.mFilterAll.setChecked(true);
        this.mFilterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.filter_all) {
                    boolean unused = DisplayInteractionView.this.isFilterAll = true;
                } else if (i == R.id.filter_timeout) {
                    boolean unused2 = DisplayInteractionView.this.isFilterAll = false;
                }
                DisplayInteractionView.this.doFilter();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void doFilter() {
        if (this.mDataSource != null && !this.mDataSource.isEmpty() && this.mAdapter != null) {
            if (this.isFilterAll) {
                this.mAdapter.setDataSource(this.mDataSource);
                return;
            }
            LinkedList linkedList = new LinkedList();
            for (Item next : this.mDataSource) {
                if (next.timeout) {
                    linkedList.add(next);
                }
            }
            this.mAdapter.setDataSource(linkedList);
        }
    }

    /* access modifiers changed from: package-private */
    public void render(@NonNull PerformanceV2Repository.APMInfo aPMInfo, String str) {
        if (this.mAdapter != null && !aPMInfo.wxinteractionArray.isEmpty()) {
            LinkedList linkedList = new LinkedList();
            int i = -1;
            for (Map next : aPMInfo.wxinteractionArray) {
                Item item = new Item();
                String str2 = (String) next.get("type");
                if (TextUtils.isEmpty(str2)) {
                    str2 = "NA";
                }
                item.componentType = str2;
                String str3 = (String) next.get("ref");
                if (TextUtils.isEmpty(str3)) {
                    str3 = "NA";
                }
                item.componentRef = str3;
                String string = WXUtils.getString(next.get("renderOriginDiffTime"), "NA");
                if ("NA".equals(string)) {
                    Log.e("weex-analyzer", "unexpected diff time");
                } else {
                    int intValue = WXUtils.getInteger(string, -1).intValue();
                    if (intValue == -1) {
                        Log.e("weex-analyzer", "unexpected diff time");
                    } else {
                        if (i > 0) {
                            item.elapsedTime = intValue - i;
                        } else {
                            item.elapsedTime = 0;
                        }
                        String str4 = (String) next.get(TemplateDom.KEY_ATTRS);
                        if (TextUtils.isEmpty(str4)) {
                            str4 = "NA";
                        }
                        item.attrs = str4;
                        String str5 = (String) next.get(RichTextNode.STYLE);
                        if (TextUtils.isEmpty(str5)) {
                            str5 = "NA";
                        }
                        item.style = str5;
                        item.instanceId = str;
                        linkedList.add(item);
                        if (((long) item.elapsedTime) >= DEFAULT_THRESHOLD) {
                            item.timeout = true;
                        } else {
                            item.timeout = false;
                        }
                        i = intValue;
                    }
                }
            }
            if (linkedList.size() > 0) {
                this.emptyView.setVisibility(8);
            } else {
                this.emptyView.setVisibility(0);
            }
            this.mDataSource = linkedList;
            if (this.isFilterAll) {
                this.mAdapter.setDataSource(linkedList);
                return;
            }
            LinkedList linkedList2 = new LinkedList();
            for (Item next2 : this.mDataSource) {
                if (next2.timeout) {
                    linkedList2.add(next2);
                }
            }
            this.mAdapter.setDataSource(linkedList2);
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_display_interaction;
    }

    static class Item {
        String attrs;
        String componentRef;
        String componentType;
        int elapsedTime;
        String instanceId;
        String style;
        boolean timeout;

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
            return LayoutInflater.from(context).inflate(R.layout.wxt_display_interaction_item_view, viewGroup, false);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private static final int COLOR_GREEN = Color.parseColor("#c600ff00");
        private static final int COLOR_RED = Color.parseColor("#c6ff0000");
        boolean flag = false;
        /* access modifiers changed from: private */
        public Item item;
        private TextView mAttrsView;
        private TextView mComponentRefView;
        private TextView mComponentTypeView;
        private TextView mElapsedTimeView;
        private TextView mStyleView;
        /* access modifiers changed from: private */
        public MutipleViewHighlighter mViewHighlighter;

        ViewHolder(View view) {
            super(view);
            final boolean z = false;
            this.mComponentRefView = (TextView) view.findViewById(R.id.text_ref);
            this.mComponentTypeView = (TextView) view.findViewById(R.id.text_type);
            this.mElapsedTimeView = (TextView) view.findViewById(R.id.text_elapsed);
            this.mAttrsView = (TextView) view.findViewById(R.id.text_attr);
            this.mStyleView = (TextView) view.findViewById(R.id.text_style);
            this.mViewHighlighter = MutipleViewHighlighter.newInstance();
            this.mViewHighlighter.setColor(Color.parseColor("#420000ff"));
            if (this.mViewHighlighter != null && this.mViewHighlighter.isSupport()) {
                z = true;
            }
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    View findViewByRef;
                    if (ViewHolder.this.item != null) {
                        Toast.makeText(view.getContext(), "type:" + ViewHolder.this.item.componentType + "\n" + "ref:" + ViewHolder.this.item.componentRef + "\n" + "属性:" + ViewHolder.this.item.attrs + "\n" + "样式:" + ViewHolder.this.item.style, 1).show();
                        if (!ViewHolder.this.flag) {
                            if (z && !TextUtils.isEmpty(ViewHolder.this.item.instanceId) && !TextUtils.isEmpty(ViewHolder.this.item.componentRef) && (findViewByRef = ViewUtils.findViewByRef(ViewHolder.this.item.instanceId, ViewHolder.this.item.componentRef)) != null) {
                                ViewHolder.this.mViewHighlighter.addHighlightedView(findViewByRef);
                            }
                            ViewHolder.this.flag = true;
                            return;
                        }
                        ViewHolder.this.flag = false;
                        ViewHolder.this.mViewHighlighter.clearHighlight();
                    }
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void bind(Item item2) {
            this.item = item2;
            this.mComponentRefView.setText(String.format(Locale.getDefault(), "ref:%s", new Object[]{item2.componentRef}));
            this.mComponentTypeView.setText(item2.componentType);
            this.mAttrsView.setText(item2.attrs);
            this.mStyleView.setText(item2.style);
            if (((long) item2.elapsedTime) >= DisplayInteractionView.DEFAULT_THRESHOLD) {
                this.mElapsedTimeView.setBackgroundColor(COLOR_RED);
            } else {
                this.mElapsedTimeView.setBackgroundColor(COLOR_GREEN);
            }
            this.mElapsedTimeView.setText(String.format(Locale.getDefault(), "+%sms", new Object[]{Integer.valueOf(item2.elapsedTime)}));
        }
    }
}
