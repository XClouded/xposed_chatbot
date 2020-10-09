package com.taobao.weex.devtools.toolbox;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.ITracingAdapter;
import com.taobao.weex.devtools.adapter.WXTracingAdapter;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.inspector.R;
import com.taobao.weex.tracing.WXTracing;
import com.taobao.weex.utils.WXViewUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailFragment extends Fragment {
    private TextView eventName;
    private TextView eventPayload;
    private WXTracing.TraceEvent rootEvent;
    private View rootView;
    private LinearLayout subEvents;

    public static EventDetailFragment getFragment(int i, int i2) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BindingXConstants.KEY_INSTANCE_ID, i);
        bundle.putInt("traceId", i2);
        eventDetailFragment.setArguments(bundle);
        return eventDetailFragment;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i = getArguments().getInt(BindingXConstants.KEY_INSTANCE_ID);
        int i2 = getArguments().getInt("traceId");
        ITracingAdapter tracingAdapter = WXSDKManager.getInstance().getTracingAdapter();
        if (tracingAdapter != null && (tracingAdapter instanceof WXTracingAdapter)) {
            this.rootEvent = ((WXTracingAdapter) tracingAdapter).getTraceEventByInstanceId(i).subEvents.get(i2);
        }
        this.rootView = layoutInflater.inflate(R.layout.fragment_event_detail, viewGroup, false);
        instantiationViews();
        this.eventName.setText(this.rootEvent.fname);
        for (int i3 = 0; i3 < this.rootEvent.subEvents.size(); i3++) {
            WXTracing.TraceEvent valueAt = this.rootEvent.subEvents.valueAt(i3);
            if (!"DomExecute".equals(valueAt.fname) && !"UIExecute".equals(valueAt.fname)) {
                EventView eventView = new EventView(getContext());
                eventView.desc.setText(valueAt.fname);
                this.subEvents.addView(eventView);
                double d = (double) (valueAt.ts - this.rootEvent.ts);
                double d2 = this.rootEvent.duration;
                Double.isNaN(d);
                double d3 = d / d2;
                double screenWidth = (double) (WXViewUtils.getScreenWidth(getContext()) - WXViewUtils.dip2px(8.0f));
                Double.isNaN(screenWidth);
                int i4 = (int) (d3 * screenWidth);
                double d4 = valueAt.duration / this.rootEvent.duration;
                double screenWidth2 = (double) WXViewUtils.getScreenWidth(getContext());
                Double.isNaN(screenWidth2);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) eventView.indicator.getLayoutParams();
                marginLayoutParams.width = ((int) (d4 * screenWidth2)) + WXViewUtils.dip2px(2.0f);
                marginLayoutParams.height = -1;
                marginLayoutParams.leftMargin = i4 - WXViewUtils.dip2px(2.0f);
                marginLayoutParams.bottomMargin = 1;
                if ("DOMThread".equals(valueAt.tname)) {
                    eventView.indicator.setBackgroundColor(Color.parseColor("#84A6E8"));
                } else if ("UIThread".equals(valueAt.tname)) {
                    eventView.indicator.setBackgroundColor(Color.parseColor("#83B86E"));
                } else {
                    eventView.indicator.setBackgroundColor(-16711681);
                }
                eventView.indicator.setLayoutParams(marginLayoutParams);
                TextView textView = eventView.duration;
                textView.setText(valueAt.duration + " ms");
            }
        }
        if (this.rootEvent.payload != null) {
            try {
                if (this.rootEvent.payload.startsWith(Operators.BLOCK_START_STR)) {
                    JSONObject jSONObject = new JSONObject(this.rootEvent.payload);
                    this.rootEvent.payload = jSONObject.toString(2);
                } else if (this.rootEvent.payload.startsWith(Operators.ARRAY_START_STR)) {
                    JSONArray jSONArray = new JSONArray(this.rootEvent.payload);
                    this.rootEvent.payload = jSONArray.toString(2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.eventPayload.setText(this.rootEvent.payload);
        }
        return this.rootView;
    }

    private void instantiationViews() {
        this.eventName = (TextView) this.rootView.findViewById(R.id.event_name);
        this.subEvents = (LinearLayout) this.rootView.findViewById(R.id.sub_events);
        this.eventPayload = (TextView) this.rootView.findViewById(R.id.event_payload);
        this.eventPayload.setTypeface(Typeface.MONOSPACE);
    }

    private static class EventView extends FrameLayout {
        TextView desc;
        TextView duration;
        View indicator;

        public EventView(@NonNull Context context) {
            super(context);
            init();
        }

        private void init() {
            this.indicator = new View(getContext());
            this.desc = new TextView(getContext());
            this.duration = new TextView(getContext());
            this.duration.setGravity(5);
            addView(this.indicator);
            addView(this.desc);
            addView(this.duration);
        }
    }
}
