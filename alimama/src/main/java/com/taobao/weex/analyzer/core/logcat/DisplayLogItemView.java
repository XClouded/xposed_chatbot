package com.taobao.weex.analyzer.core.logcat;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.logcat.LogcatDumper;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import java.util.ArrayList;
import java.util.List;

public class DisplayLogItemView extends AbstractBizItemView<List<LogcatDumper.LogInfo>> {
    private RecyclerView mList;
    private LogListAdapter mLogAdapter;

    public void inflateData(List<LogcatDumper.LogInfo> list) {
    }

    public DisplayLogItemView(Context context) {
        super(context);
    }

    public DisplayLogItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DisplayLogItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        this.mList = (RecyclerView) findViewById(R.id.list);
        this.mList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mLogAdapter = new LogListAdapter(getContext(), this.mList);
        this.mList.setAdapter(this.mLogAdapter);
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_display_log_item_view;
    }

    /* access modifiers changed from: package-private */
    public LogListAdapter getLogAdapter() {
        return this.mLogAdapter;
    }

    /* access modifiers changed from: package-private */
    public View getContentView() {
        return this.mList;
    }

    static class LogListAdapter extends RecyclerView.Adapter {
        private boolean isHoldMode = false;
        private Context mContext;
        private RecyclerView mList;
        private List<LogcatDumper.LogInfo> mLogData;

        LogListAdapter(@NonNull Context context, @NonNull RecyclerView recyclerView) {
            this.mContext = context;
            this.mLogData = new ArrayList();
            this.mList = recyclerView;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new LogViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.wxt_item_log, viewGroup, false));
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof LogViewHolder) {
                ((LogViewHolder) viewHolder).bind(this.mLogData.get(i));
            }
        }

        public int getItemCount() {
            return this.mLogData.size();
        }

        /* access modifiers changed from: package-private */
        public void addLog(@NonNull LogcatDumper.LogInfo logInfo) {
            this.mLogData.add(logInfo);
            notifyItemInserted(this.mLogData.size());
        }

        /* access modifiers changed from: package-private */
        public void addLog(@NonNull List<LogcatDumper.LogInfo> list) {
            if (list.size() == 1) {
                addLog(list.get(0));
            } else {
                int size = this.mLogData.size();
                this.mLogData.addAll(list);
                notifyItemRangeInserted(size, list.size());
            }
            if (!this.isHoldMode) {
                try {
                    this.mList.smoothScrollToPosition(getItemCount() - 1);
                } catch (Exception unused) {
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.mLogData.clear();
            notifyDataSetChanged();
        }

        /* access modifiers changed from: package-private */
        public void setHoldModeEnabled(boolean z) {
            this.isHoldMode = z;
        }

        /* access modifiers changed from: package-private */
        public boolean isHoldModeEnabled() {
            return this.isHoldMode;
        }

        /* access modifiers changed from: package-private */
        public String dumpAll() {
            StringBuilder sb = new StringBuilder();
            if (this.mLogData != null) {
                for (LogcatDumper.LogInfo next : this.mLogData) {
                    if (!TextUtils.isEmpty(next.message)) {
                        sb.append(next.message);
                        sb.append("\n");
                    }
                }
            }
            return sb.toString();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public LogcatDumper.LogInfo mCurLog;
        private TextView mText;

        LogViewHolder(View view) {
            super(view);
            this.mText = (TextView) view.findViewById(R.id.text_log);
            this.mText.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (LogViewHolder.this.mCurLog != null) {
                        try {
                            SDKUtils.copyToClipboard(view.getContext(), LogViewHolder.this.mCurLog.message, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void bind(LogcatDumper.LogInfo logInfo) {
            this.mCurLog = logInfo;
            switch (logInfo.level) {
                case 2:
                    this.mText.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 3:
                    this.mText.setTextColor(Color.parseColor("#4CAF50"));
                    break;
                case 4:
                    this.mText.setTextColor(Color.parseColor("#2196F3"));
                    break;
                case 5:
                    this.mText.setTextColor(Color.parseColor("#FFEB3B"));
                    break;
                case 6:
                    this.mText.setTextColor(Color.parseColor("#F44336"));
                    break;
                default:
                    this.mText.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
            }
            this.mText.setText(logInfo.message);
        }
    }
}
