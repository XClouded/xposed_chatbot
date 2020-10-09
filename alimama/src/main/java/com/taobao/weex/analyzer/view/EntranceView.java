package com.taobao.weex.analyzer.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.debug.MDSDebugEntranceView;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.utils.XiaomiOverlayViewPermissionHelper;
import com.taobao.weex.analyzer.view.alert.AbstractAlertView;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.List;

public class EntranceView extends AbstractAlertView {
    private static final String TAG = "EntranceView";
    private ViewGroup mDebugContainer;
    private List<DevOption> mDevOptions;
    private RecyclerView mList;
    private boolean mShowMDS = true;

    public EntranceView(Context context) {
        super(context);
    }

    public EntranceView(Context context, boolean z) {
        super(context);
        this.mShowMDS = z;
    }

    /* access modifiers changed from: protected */
    public void onInitView(@NonNull Window window) {
        Context context = getContext();
        this.mList = (RecyclerView) window.findViewById(R.id.list);
        this.mList.setLayoutManager(new GridLayoutManager(context, 3, 1, false));
        this.mList.addItemDecoration(new Decoration(Color.parseColor("#e0e0e0"), (int) ViewUtils.dp2px(context, 1), 3));
        this.mDebugContainer = (ViewGroup) findViewById(R.id.debug_container);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EntranceView.this.dismiss();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        if (this.mDevOptions != null) {
            EntranceViewAdapter entranceViewAdapter = new EntranceViewAdapter(getContext(), this.mDevOptions);
            if (this.mShowMDS) {
                MDSDebugEntranceView mDSDebugEntranceView = new MDSDebugEntranceView(getContext());
                mDSDebugEntranceView.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) ViewUtils.dp2px(getContext(), 150)));
                this.mDebugContainer.addView(mDSDebugEntranceView);
            } else {
                this.mDebugContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, 0));
            }
            this.mList.setAdapter(entranceViewAdapter);
        }
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        super.onDismiss();
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_entrance_layout;
    }

    public void registerDevOption(@Nullable DevOption devOption) {
        if (devOption != null) {
            if (this.mDevOptions == null) {
                this.mDevOptions = new ArrayList();
            }
            this.mDevOptions.add(devOption);
        }
    }

    public void registerDevOptions(List<DevOption> list) {
        if (list != null && !list.isEmpty()) {
            if (this.mDevOptions == null) {
                this.mDevOptions = new ArrayList();
            }
            for (DevOption next : list) {
                if (next.isPermissionGranted) {
                    this.mDevOptions.add(next);
                }
            }
        }
    }

    public static class Creator {
        private Context context;
        private List<DevOption> options = new ArrayList();

        public Creator(@NonNull Context context2) {
            this.context = context2;
        }

        public Creator injectOptions(List<DevOption> list) {
            this.options.addAll(list);
            return this;
        }

        public Creator injectOption(DevOption devOption) {
            this.options.add(devOption);
            return this;
        }

        public EntranceView create(boolean z) {
            EntranceView entranceView = new EntranceView(this.context, z);
            entranceView.registerDevOptions(this.options);
            return entranceView;
        }
    }

    class EntranceViewAdapter extends RecyclerView.Adapter<ViewHolder> {
        Context mContext;
        private View mHeaderView;
        List<DevOption> mOptions;

        EntranceViewAdapter(Context context, List<DevOption> list) {
            this.mOptions = list;
            this.mContext = context;
        }

        public void setHeader(View view) {
            this.mHeaderView = view;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (this.mHeaderView != null && i == 2) {
                return new ViewHolder(this.mHeaderView, i);
            }
            return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.wxt_option_item, viewGroup, false), i);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (getItemViewType(i) != 2) {
                if (this.mHeaderView != null) {
                    viewHolder.bind(this.mOptions.get(i - 1));
                } else {
                    viewHolder.bind(this.mOptions.get(i));
                }
            }
        }

        public int getItemCount() {
            int i = this.mHeaderView != null ? 1 : 0;
            return this.mOptions != null ? i + this.mOptions.size() : i;
        }

        public int getItemViewType(int i) {
            return (this.mHeaderView != null && i == 0) ? 2 : 1;
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    public int getSpanSize(int i) {
                        if (EntranceViewAdapter.this.getItemViewType(i) == 2) {
                            return gridLayoutManager.getSpanCount();
                        }
                        return 1;
                    }
                });
            }
        }
    }

    static class ItemType {
        static final int TYPE_HEADER = 2;
        static final int TYPE_OPTION = 1;

        ItemType() {
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        DevOption curOption;
        View label;
        ImageView optionIcon;
        TextView optionName;

        ViewHolder(View view, int i) {
            super(view);
            if (i != 2) {
                this.optionName = (TextView) view.findViewById(R.id.option_name);
                this.optionIcon = (ImageView) view.findViewById(R.id.option_icon);
                this.label = view.findViewById(R.id.label);
                view.setOnClickListener(new View.OnClickListener(EntranceView.this) {
                    public void onClick(View view) {
                        if (ViewHolder.this.curOption != null && ViewHolder.this.curOption.listener != null) {
                            try {
                                if (ViewHolder.this.curOption.isOverlayView) {
                                    if (!XiaomiOverlayViewPermissionHelper.isPermissionGranted(view.getContext())) {
                                        Toast.makeText(view.getContext(), "检测到使用了小米手机，可能需要你手动开启悬浮窗权限", 1).show();
                                        XiaomiOverlayViewPermissionHelper.requestPermission(view.getContext());
                                        return;
                                    } else if (Build.VERSION.SDK_INT >= 25 && !SDKUtils.canDrawOverlays(EntranceView.this.getContext())) {
                                        WXLogUtils.d(EntranceView.TAG, "we have no permission to draw overlay views.");
                                        EntranceView.this.getContext().startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + EntranceView.this.getContext().getPackageName())));
                                        Toast.makeText(EntranceView.this.getContext(), "please granted overlay permission before use this option", 0).show();
                                        return;
                                    }
                                }
                                ViewHolder.this.curOption.listener.onOptionClick();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            EntranceView.this.dismiss();
                        }
                    }
                });
            }
        }

        /* access modifiers changed from: package-private */
        public void bind(DevOption devOption) {
            this.curOption = devOption;
            if (!TextUtils.isEmpty(devOption.optionName)) {
                this.optionName.setText(devOption.optionName);
            }
            if (devOption.iconRes != 0) {
                this.optionIcon.setImageResource(devOption.iconRes);
            }
            if (devOption.showLabel) {
                this.label.setVisibility(0);
            } else {
                this.label.setVisibility(8);
            }
        }
    }

    static class Decoration extends RecyclerView.ItemDecoration {
        int color;
        Paint paint;
        int size;
        int spanCount;

        Decoration(int i, int i2, int i3) {
            this.color = i;
            this.size = i2;
            this.spanCount = i3;
        }

        public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            super.onDraw(canvas, recyclerView, state);
            createPaint();
            int childCount = recyclerView.getChildCount();
            int i = 0;
            while (i < childCount) {
                View childAt = recyclerView.getChildAt(i);
                float x = childAt.getX();
                float x2 = childAt.getX() + ((float) childAt.getWidth());
                float y = childAt.getY();
                float height = ((float) childAt.getHeight()) + childAt.getY();
                i++;
                if (i % this.spanCount == 1) {
                    canvas.drawRect(x, y, x + ((float) this.size), height, this.paint);
                }
                canvas.drawRect(x2 - ((float) this.size), y, x2, height, this.paint);
                canvas.drawRect(x, y, x2, y + ((float) this.size), this.paint);
                if (childCount - i < this.spanCount) {
                    canvas.drawRect(x, height, x2, height + ((float) this.size), this.paint);
                }
            }
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            super.getItemOffsets(rect, view, recyclerView, state);
        }

        private void createPaint() {
            if (this.paint == null) {
                this.paint = new Paint(1);
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setStrokeWidth((float) this.size);
                this.paint.setColor(this.color);
            }
        }
    }
}
