package com.taobao.weex.analyzer.core.storage;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.storage.StorageHacker;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.alert.CompatibleAlertDialogBuilder;
import com.taobao.weex.analyzer.view.alert.PermissionAlertView;
import java.util.ArrayList;
import java.util.List;

public class StorageView extends PermissionAlertView {
    /* access modifiers changed from: private */
    public PerformanceViewAdapter mAdapter;
    /* access modifiers changed from: private */
    public StorageHacker mStorageHacker;
    /* access modifiers changed from: private */
    public RecyclerView mStorageList;

    public StorageView(Context context, Config config) {
        super(context, config);
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        if (this.mStorageHacker == null || this.mStorageHacker.isDestroy()) {
            this.mStorageHacker = new StorageHacker(getContext(), SDKUtils.isDebugMode(getContext()));
        }
        this.mStorageHacker.fetch(new StorageHacker.OnLoadListener() {
            public void onLoad(List<StorageHacker.StorageInfo> list) {
                if (StorageView.this.mAdapter == null) {
                    PerformanceViewAdapter unused = StorageView.this.mAdapter = new PerformanceViewAdapter(StorageView.this.getContext(), list);
                    StorageView.this.mStorageList.setAdapter(StorageView.this.mAdapter);
                    return;
                }
                StorageView.this.mAdapter.refreshData(list);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        super.onDismiss();
        if (this.mStorageHacker != null) {
            this.mStorageHacker.destroy();
        }
    }

    /* access modifiers changed from: protected */
    public void onInitView(@NonNull Window window) {
        window.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StorageView.this.dismiss();
            }
        });
        this.mStorageList = (RecyclerView) window.findViewById(R.id.list);
        this.mStorageList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAdapter = new PerformanceViewAdapter(getContext(), new ArrayList(6));
        this.mStorageList.setAdapter(this.mAdapter);
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_storage_view;
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_STORAGE);
    }

    private class PerformanceViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        /* access modifiers changed from: private */
        public Context mContext;
        /* access modifiers changed from: private */
        public List<StorageHacker.StorageInfo> mStorageData;

        PerformanceViewAdapter(@NonNull Context context, @NonNull List<StorageHacker.StorageInfo> list) {
            this.mContext = context;
            this.mStorageData = list;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.wxt_item_storage, viewGroup, false));
            viewHolder.setOnItemLongClickListener(new ViewHolder.OnItemLongClickListener() {
                public void onItemClick(final int i, final String str) {
                    CompatibleAlertDialogBuilder compatibleAlertDialogBuilder = new CompatibleAlertDialogBuilder(PerformanceViewAdapter.this.mContext);
                    compatibleAlertDialogBuilder.setTitle("Alert");
                    compatibleAlertDialogBuilder.setMessage("remove key (" + str + ") from weex storage ?");
                    compatibleAlertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!(StorageView.this.mStorageHacker == null || str == null)) {
                                StorageView.this.mStorageHacker.remove(str, new StorageHacker.OnRemoveListener() {
                                    public void onRemoved(boolean z) {
                                        if (z) {
                                            PerformanceViewAdapter.this.mStorageData.remove(i);
                                            PerformanceViewAdapter.this.notifyDataSetChanged();
                                            if (PerformanceViewAdapter.this.mContext != null) {
                                                Toast.makeText(PerformanceViewAdapter.this.mContext, "remove success", 0).show();
                                            }
                                        } else if (PerformanceViewAdapter.this.mContext != null) {
                                            Toast.makeText(PerformanceViewAdapter.this.mContext, "remove failed", 0).show();
                                        }
                                    }
                                });
                            }
                            dialogInterface.dismiss();
                        }
                    });
                    compatibleAlertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    compatibleAlertDialogBuilder.create().show();
                }
            });
            return viewHolder;
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof ViewHolder) {
                ((ViewHolder) viewHolder).bind(this.mStorageData.get(i), i % 2 != 0);
            }
        }

        public int getItemCount() {
            if (this.mStorageData == null) {
                return 0;
            }
            return this.mStorageData.size();
        }

        /* access modifiers changed from: package-private */
        public void refreshData(@NonNull List<StorageHacker.StorageInfo> list) {
            this.mStorageData.clear();
            this.mStorageData.addAll(list);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public StorageHacker.StorageInfo mCurStorageInfo;
        private TextView mKeyText;
        /* access modifiers changed from: private */
        public OnItemLongClickListener mListener;
        private TextView mTimestampText;
        private TextView mValueText;

        interface OnItemLongClickListener {
            void onItemClick(int i, String str);
        }

        ViewHolder(View view) {
            super(view);
            this.mKeyText = (TextView) view.findViewById(R.id.key);
            this.mValueText = (TextView) view.findViewById(R.id.value);
            this.mTimestampText = (TextView) view.findViewById(R.id.timestamp);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (ViewHolder.this.mListener == null) {
                        return true;
                    }
                    try {
                        int adapterPosition = ViewHolder.this.getAdapterPosition();
                        if (ViewHolder.this.mCurStorageInfo == null || ViewHolder.this.mCurStorageInfo.key == null) {
                            return true;
                        }
                        ViewHolder.this.mListener.onItemClick(adapterPosition, ViewHolder.this.mCurStorageInfo.key);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (ViewHolder.this.mCurStorageInfo != null && !TextUtils.isEmpty(ViewHolder.this.mCurStorageInfo.value)) {
                        Toast.makeText(view.getContext(), ViewHolder.this.mCurStorageInfo.value, 1).show();
                    }
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void bind(@NonNull StorageHacker.StorageInfo storageInfo, boolean z) {
            this.mCurStorageInfo = storageInfo;
            this.itemView.setBackgroundColor(z ? Color.parseColor("#E0E0E0") : -1);
            this.mValueText.setText(storageInfo.value);
            this.mKeyText.setText(storageInfo.key);
            this.mTimestampText.setText(storageInfo.timestamp);
        }

        /* access modifiers changed from: package-private */
        public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.mListener = onItemLongClickListener;
        }
    }
}
