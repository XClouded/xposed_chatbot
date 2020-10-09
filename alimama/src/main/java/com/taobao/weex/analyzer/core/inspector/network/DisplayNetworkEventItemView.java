package com.taobao.weex.analyzer.core.inspector.network;

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
import com.alibaba.fastjson.JSON;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import com.taobao.weex.analyzer.core.inspector.network.NetworkEventInspector;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import com.taobao.weex.el.parse.Operators;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DisplayNetworkEventItemView extends AbstractBizItemView<NetworkEventInspector.MessageBean> {
    private NetworkEventListAdapter mAdapter;
    private RecyclerView mList;

    public DisplayNetworkEventItemView(Context context) {
        super(context);
    }

    public DisplayNetworkEventItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DisplayNetworkEventItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        this.mList = (RecyclerView) findViewById(R.id.list);
        this.mList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAdapter = new NetworkEventListAdapter(getContext(), this.mList);
        this.mList.setAdapter(this.mAdapter);
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_display_network_event_item_view;
    }

    /* access modifiers changed from: package-private */
    public View getContentView() {
        return this.mList;
    }

    /* access modifiers changed from: package-private */
    public NetworkEventListAdapter getListAdapter() {
        return this.mAdapter;
    }

    static class NetworkEventListAdapter extends RecyclerView.Adapter<ViewHolder> {
        private boolean isHoldMode = false;
        private RecyclerView list;
        private Context mContext;
        private List<NetworkEventInspector.MessageBean> mMessageList = new ArrayList();

        NetworkEventListAdapter(Context context, RecyclerView recyclerView) {
            this.mContext = context;
            this.list = recyclerView;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.wxt_item_message, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.bind(this.mMessageList.get(i));
        }

        public int getItemCount() {
            if (this.mMessageList == null) {
                return 0;
            }
            return this.mMessageList.size();
        }

        /* access modifiers changed from: package-private */
        public void addMessage(@NonNull NetworkEventInspector.MessageBean messageBean) {
            this.mMessageList.add(messageBean);
            notifyItemInserted(this.mMessageList.size());
            if (!this.isHoldMode) {
                try {
                    this.list.smoothScrollToPosition(getItemCount() - 1);
                } catch (Exception unused) {
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.mMessageList.clear();
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
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        private TextView bodyView;
        private TextView descView;
        private View line;
        /* access modifiers changed from: private */
        public NetworkEventInspector.MessageBean mCurMessage;
        private TextView timestampView;
        private TextView titleView;
        private TextView typeView;

        ViewHolder(View view) {
            super(view);
            this.bodyView = (TextView) view.findViewById(R.id.body);
            this.typeView = (TextView) view.findViewById(R.id.type);
            this.titleView = (TextView) view.findViewById(R.id.title);
            this.descView = (TextView) view.findViewById(R.id.desc);
            this.timestampView = (TextView) view.findViewById(R.id.timestamp);
            this.line = view.findViewById(R.id.line);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (ViewHolder.this.mCurMessage != null) {
                        Map<String, String> map = ViewHolder.this.mCurMessage.extendProps;
                        if (map == null) {
                            try {
                                if (ViewHolder.this.mCurMessage.type == null || !ViewHolder.this.mCurMessage.type.equalsIgnoreCase("request")) {
                                    if (ViewHolder.this.mCurMessage.type != null && ViewHolder.this.mCurMessage.type.equalsIgnoreCase(NetworkEventSender.TYPE_RESPONSE) && !TextUtils.isEmpty(ViewHolder.this.mCurMessage.body)) {
                                        SDKUtils.copyToClipboard(view.getContext(), ViewHolder.this.mCurMessage.body, true);
                                    }
                                } else if (!TextUtils.isEmpty(ViewHolder.this.mCurMessage.title)) {
                                    SDKUtils.copyToClipboard(view.getContext(), ViewHolder.this.mCurMessage.title, true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            String str = map.get("bizType");
                            if ("mtop".equalsIgnoreCase(str)) {
                                try {
                                    if (ViewHolder.this.mCurMessage.type == null || !ViewHolder.this.mCurMessage.type.equalsIgnoreCase("request")) {
                                        if (ViewHolder.this.mCurMessage.type != null && ViewHolder.this.mCurMessage.type.equalsIgnoreCase(NetworkEventSender.TYPE_RESPONSE) && !TextUtils.isEmpty(ViewHolder.this.mCurMessage.body)) {
                                            SDKUtils.copyToClipboard(view.getContext(), ViewHolder.this.mCurMessage.body, true);
                                        }
                                    } else if (!TextUtils.isEmpty(ViewHolder.this.mCurMessage.title)) {
                                        SDKUtils.copyToClipboard(view.getContext(), ViewHolder.this.mCurMessage.title, true);
                                    }
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            } else if ("image".equalsIgnoreCase(str)) {
                                try {
                                    if (!TextUtils.isEmpty(ViewHolder.this.mCurMessage.title)) {
                                        SDKUtils.copyToClipboard(view.getContext(), ViewHolder.this.mCurMessage.title, true);
                                    }
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }
                            } else if ("http".equalsIgnoreCase(str)) {
                                try {
                                    if (!TextUtils.isEmpty(ViewHolder.this.mCurMessage.body)) {
                                        SDKUtils.copyToClipboard(view.getContext(), ViewHolder.this.mCurMessage.body, true);
                                    }
                                } catch (Exception e4) {
                                    e4.printStackTrace();
                                }
                            }
                        }
                    }
                    return true;
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void bind(@NonNull NetworkEventInspector.MessageBean messageBean) {
            this.mCurMessage = messageBean;
            this.typeView.setText(TextUtils.isEmpty(messageBean.type) ? "UNKNOWN" : messageBean.type.toUpperCase());
            this.titleView.setText(TextUtils.isEmpty(messageBean.title) ? BuildConfig.buildJavascriptFrameworkVersion : messageBean.title);
            String upperCase = TextUtils.isEmpty(messageBean.desc) ? BuildConfig.buildJavascriptFrameworkVersion : messageBean.desc.toUpperCase();
            if (TextUtils.isEmpty(messageBean.type)) {
                this.descView.setText(upperCase);
                this.bodyView.setTextColor(-1);
                this.typeView.setTextColor(-1);
                this.titleView.setTextColor(-1);
                this.descView.setTextColor(-1);
                this.timestampView.setTextColor(-1);
            } else if ("request".equalsIgnoreCase(messageBean.type)) {
                TextView textView = this.descView;
                textView.setText("Method(" + upperCase + Operators.BRACKET_END_STR);
                this.bodyView.setTextColor(Color.parseColor("#2196F3"));
                this.typeView.setTextColor(Color.parseColor("#2196F3"));
                this.titleView.setTextColor(Color.parseColor("#2196F3"));
                this.descView.setTextColor(Color.parseColor("#2196F3"));
                this.timestampView.setTextColor(Color.parseColor("#2196F3"));
            } else if (NetworkEventSender.TYPE_RESPONSE.equalsIgnoreCase(messageBean.type)) {
                TextView textView2 = this.descView;
                textView2.setText("Code(" + upperCase + Operators.BRACKET_END_STR);
                this.bodyView.setTextColor(Color.parseColor("#FFEB3B"));
                this.typeView.setTextColor(Color.parseColor("#FFEB3B"));
                this.titleView.setTextColor(Color.parseColor("#FFEB3B"));
                this.descView.setTextColor(Color.parseColor("#FFEB3B"));
                this.timestampView.setTextColor(Color.parseColor("#FFEB3B"));
            }
            this.timestampView.setText(now());
            if (!TextUtils.isEmpty(messageBean.body)) {
                try {
                    if (messageBean.content != null) {
                        this.bodyView.setText(JSON.toJSONString((Object) messageBean.content, true));
                    } else {
                        this.bodyView.setText(messageBean.body);
                    }
                } catch (Exception unused) {
                    this.bodyView.setText(messageBean.body);
                }
                this.line.setVisibility(0);
                this.bodyView.setVisibility(0);
                return;
            }
            this.line.setVisibility(8);
            this.bodyView.setVisibility(8);
        }

        private static String now() {
            return sFormatter.format(new Date());
        }
    }
}
