package com.taobao.weex.devtools.toolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import anet.channel.util.ErrorConstant;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.ITracingAdapter;
import com.taobao.weex.devtools.adapter.WXTracingAdapter;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.inspector.R;
import com.taobao.weex.tracing.WXTracing;
import com.taobao.weex.ui.component.WXComponent;

public class EventOverviewFragment extends Fragment {
    private RecyclerView list;

    public static EventOverviewFragment getInstance(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(BindingXConstants.KEY_INSTANCE_ID, i);
        EventOverviewFragment eventOverviewFragment = new EventOverviewFragment();
        eventOverviewFragment.setArguments(bundle);
        return eventOverviewFragment;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_event_overview, viewGroup, false);
        int i = getArguments().getInt(BindingXConstants.KEY_INSTANCE_ID, -1);
        this.list = (RecyclerView) inflate.findViewById(R.id.perf_list);
        this.list.setLayoutManager(new LinearLayoutManager(getContext()));
        ITracingAdapter tracingAdapter = WXSDKManager.getInstance().getTracingAdapter();
        if (!(tracingAdapter == null || !(tracingAdapter instanceof WXTracingAdapter) || i == -1)) {
            this.list.setAdapter(new PerfListAdapter(((WXTracingAdapter) tracingAdapter).getTraceEventByInstanceId(i)));
        }
        return inflate;
    }

    private class PerfListAdapter extends RecyclerView.Adapter<ItemHolder> {
        /* access modifiers changed from: private */
        public WXTracing.TraceEvent rootEvent;

        public PerfListAdapter(WXTracing.TraceEvent traceEvent) {
            this.rootEvent = traceEvent;
        }

        public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trace_event_item, viewGroup, false));
        }

        public void onBindViewHolder(final ItemHolder itemHolder, int i) {
            final WXTracing.TraceEvent valueAt = this.rootEvent.subEvents.valueAt(i);
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (valueAt.subEvents != null) {
                        EventOverviewFragment.this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EventDetailFragment.getFragment(PerfListAdapter.this.rootEvent.traceId, valueAt.traceId)).addToBackStack(EventDetailFragment.class.getSimpleName()).commitAllowingStateLoss();
                    }
                }
            });
            if (valueAt.subEvents == null) {
                itemHolder.info.setVisibility(4);
            } else {
                itemHolder.info.setVisibility(0);
            }
            if (valueAt.ts < this.rootEvent.ts) {
                this.rootEvent.ts = valueAt.ts;
            }
            itemHolder.actionName.setText(valueAt.fname);
            TextView textView = itemHolder.actionDuration;
            textView.setText(valueAt.duration + " ms");
            final ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) itemHolder.duration.getLayoutParams();
            itemHolder.itemView.post(new Runnable() {
                public void run() {
                    double d = (double) (valueAt.ts - PerfListAdapter.this.rootEvent.ts);
                    double d2 = PerfListAdapter.this.rootEvent.duration;
                    Double.isNaN(d);
                    double d3 = d / d2;
                    double itemWidth = (double) itemHolder.getItemWidth();
                    Double.isNaN(itemWidth);
                    double d4 = valueAt.duration / PerfListAdapter.this.rootEvent.duration;
                    double itemWidth2 = (double) itemHolder.getItemWidth();
                    Double.isNaN(itemWidth2);
                    marginLayoutParams.width = (int) (d4 * itemWidth2);
                    marginLayoutParams.leftMargin = (int) (d3 * itemWidth);
                    itemHolder.duration.setLayoutParams(marginLayoutParams);
                }
            });
            if (valueAt.ref != null) {
                WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(valueAt.iid, valueAt.ref);
                if (wXComponent != null) {
                    String componentType = wXComponent.getComponentType();
                    TextView textView2 = itemHolder.compType;
                    textView2.setText(Operators.L + componentType + "/>");
                    if (wXComponent.getRealView() != null) {
                        itemHolder.viewType.setText(wXComponent.getRealView().getClass().getSimpleName());
                    }
                    if (wXComponent.isLazy()) {
                        itemHolder.compType.append(" @lazy");
                    }
                    TextView textView3 = itemHolder.compRef;
                    textView3.setText("Ref: " + wXComponent.getRef());
                    return;
                }
                return;
            }
            itemHolder.compType.setText("-");
            itemHolder.viewType.setText("-");
            itemHolder.compRef.setText("-");
        }

        public int getItemCount() {
            return this.rootEvent.subEvents.size();
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView actionDuration;
        public TextView actionName;
        public TextView compRef;
        public TextView compType;
        public View duration;
        public ImageView info;
        public LinearLayout infoContent;
        public TextView viewType;

        public ItemHolder(View view) {
            super(view);
            this.actionName = (TextView) view.findViewById(R.id.action_name);
            this.compRef = (TextView) view.findViewById(R.id.comp_ref);
            this.duration = view.findViewById(R.id.duration);
            this.infoContent = (LinearLayout) view.findViewById(R.id.info_content);
            this.actionDuration = (TextView) view.findViewById(R.id.action_duration);
            this.viewType = (TextView) view.findViewById(R.id.view_type);
            this.compType = (TextView) view.findViewById(R.id.comp_type);
            this.info = (ImageView) view.findViewById(R.id.info);
        }

        public int getItemWidth() {
            return this.infoContent.getMeasuredWidth() + ErrorConstant.ERROR_NO_NETWORK;
        }
    }
}
