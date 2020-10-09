package com.taobao.weex.devtools.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JsLogAdapter extends RecyclerView.Adapter<ItemHolder> implements WXLogUtils.JsLogWatcher, Filterable {
    private static JsLogAdapter sInstance;
    /* access modifiers changed from: private */
    public int currentLogLevel = 2;
    /* access modifiers changed from: private */
    public List<LogItem> displayedLogItems = new CopyOnWriteArrayList();
    /* access modifiers changed from: private */
    public List<LogItem> originLogItems = new CopyOnWriteArrayList();

    public static synchronized JsLogAdapter getInstance() {
        JsLogAdapter jsLogAdapter;
        synchronized (JsLogAdapter.class) {
            if (sInstance == null) {
                sInstance = new JsLogAdapter();
            }
            jsLogAdapter = sInstance;
        }
        return jsLogAdapter;
    }

    private JsLogAdapter() {
    }

    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = new LinearLayout(viewGroup.getContext());
        linearLayout.setOrientation(1);
        TextView textView = new TextView(viewGroup.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        linearLayout.addView(textView);
        View view = new View(viewGroup.getContext());
        view.setBackgroundColor(-7829368);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
        linearLayout.addView(view);
        return new ItemHolder(linearLayout);
    }

    public void onBindViewHolder(ItemHolder itemHolder, int i) {
        itemHolder.render(this.displayedLogItems.get(i));
    }

    public int getItemCount() {
        return this.displayedLogItems.size();
    }

    public void onJsLog(int i, String str) {
        while (this.originLogItems.size() >= 2000) {
            this.originLogItems.remove(0);
        }
        LogItem logItem = new LogItem();
        logItem.level = i;
        logItem.msg = str;
        this.originLogItems.add(logItem);
        if (i >= this.currentLogLevel) {
            this.displayedLogItems.add(logItem);
            notifyItemInserted(this.displayedLogItems.size() - 1);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            /* access modifiers changed from: protected */
            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = new ArrayList();
                for (LogItem logItem : JsLogAdapter.this.originLogItems) {
                    if (logItem.level >= JsLogAdapter.this.currentLogLevel) {
                        if (TextUtils.isEmpty(charSequence)) {
                            ((List) filterResults.values).add(logItem);
                        } else if (logItem.msg.toLowerCase().contains(charSequence.toString())) {
                            ((List) filterResults.values).add(logItem);
                        }
                    }
                }
                filterResults.count = ((List) filterResults.values).size();
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                JsLogAdapter.this.displayedLogItems.clear();
                JsLogAdapter.this.displayedLogItems.addAll((List) filterResults.values);
                JsLogAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public void setLogLevel(int i) {
        this.currentLogLevel = i;
        getFilter().filter("");
    }

    public int getLogLevel() {
        return this.currentLogLevel;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View view) {
            super(view);
        }

        public void render(LogItem logItem) {
            TextView textView = getTextView();
            if (textView != null) {
                switch (logItem.level) {
                    case 2:
                    case 3:
                        textView.setTextColor(Color.parseColor("#B4000000"));
                        break;
                    case 4:
                        textView.setTextColor(Color.parseColor("#1E00CA"));
                        break;
                    case 5:
                        textView.setTextColor(Color.parseColor("#E9B200"));
                        break;
                    case 6:
                        textView.setTextColor(Color.parseColor("#EF0000"));
                        break;
                }
                textView.setText(logItem.msg);
            }
        }

        private TextView getTextView() {
            if (!(this.itemView instanceof ViewGroup)) {
                return null;
            }
            ViewGroup viewGroup = (ViewGroup) this.itemView;
            if (viewGroup.getChildCount() != 2 || !(viewGroup.getChildAt(0) instanceof TextView)) {
                return null;
            }
            return (TextView) viewGroup.getChildAt(0);
        }
    }

    class LogItem {
        int level;
        String msg;

        LogItem() {
        }
    }
}
