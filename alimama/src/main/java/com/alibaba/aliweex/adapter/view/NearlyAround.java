package com.alibaba.aliweex.adapter.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.aliweex.R;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.List;

public class NearlyAround {
    private Context context;
    private String huichang_cache;
    private List<NearlyAroundItem> itemList;
    private LinearLayout linearLayout;
    /* access modifiers changed from: private */
    public OnNearlyItemClickListener listener;
    private ViewGroup mRootView;
    private List<NearlyAroundItem> nearlyList;
    private TextView titleTv;

    public interface OnNearlyItemClickListener {
        void OnNearlyItemClick(NearlyAroundItem nearlyAroundItem);
    }

    public NearlyAround(Context context2) {
        this.context = context2;
        init();
    }

    public View getRootView() {
        return this.mRootView;
    }

    private void init() {
        this.nearlyList = new ArrayList();
        this.mRootView = (ViewGroup) LayoutInflater.from(this.context).inflate(R.layout.huichang_nearlyaround_layout, (ViewGroup) null);
        if (this.mRootView != null) {
            this.titleTv = (TextView) this.mRootView.findViewById(R.id.nearlyaround_title);
            this.linearLayout = (LinearLayout) this.mRootView.findViewById(R.id.nearlyaround_linear);
        }
    }

    @SuppressLint({"NewApi"})
    public void updataList() {
        this.linearLayout.removeAllViews();
        this.huichang_cache = PreferenceManager.getDefaultSharedPreferences(this.context).getString("huichang_footstep_cache", "");
        Log.d("huichang_footstep_cache", this.huichang_cache);
        if (TextUtils.isEmpty(this.huichang_cache)) {
            this.mRootView.findViewById(R.id.nearlyaround_title1).setVisibility(0);
            return;
        }
        try {
            this.itemList = JSON.parseArray(this.huichang_cache, NearlyAroundItem.class);
        } catch (Exception unused) {
        }
        if (this.itemList == null || this.itemList.size() <= 0) {
            this.mRootView.findViewById(R.id.nearlyaround_title1).setVisibility(0);
            return;
        }
        this.mRootView.findViewById(R.id.nearlyaround_title1).setVisibility(8);
        this.nearlyList.clear();
        this.nearlyList.addAll(this.itemList);
        int size = this.itemList.size();
        for (int i = 0; i < size; i++) {
            NearlyAroundItem nearlyAroundItem = this.itemList.get(i);
            TextView textView = new TextView(this.context);
            textView.setText(nearlyAroundItem.getTitle());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.setMargins(WXViewUtils.dip2px(12.0f), 0, 0, 0);
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(Color.parseColor("#fffef0"));
            textView.setTextSize(16.0f);
            textView.setBackgroundDrawable(this.context.getResources().getDrawable(R.drawable.huichang_nearlyaround_tv_bg));
            textView.setTag(nearlyAroundItem);
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (NearlyAround.this.listener != null) {
                        NearlyAround.this.listener.OnNearlyItemClick((NearlyAroundItem) view.getTag());
                    }
                }
            });
            this.linearLayout.addView(textView);
        }
    }

    public void setOnNearlyItemClickListener(OnNearlyItemClickListener onNearlyItemClickListener) {
        this.listener = onNearlyItemClickListener;
    }

    public void setTitle(String str) {
        this.titleTv.setText(str);
    }
}
