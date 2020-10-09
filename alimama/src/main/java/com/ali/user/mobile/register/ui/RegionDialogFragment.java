package com.ali.user.mobile.register.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.register.RegistConstants;
import com.ali.user.mobile.ui.R;
import com.alibaba.analytics.utils.MapUtils;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class RegionDialogFragment extends DialogFragment {
    private RegionAdapter mAdapter;
    private RegionInfo mCurrentRegion;
    private List<String> mLetterList;
    private HashMap<String, Integer> mLetterMap;
    /* access modifiers changed from: private */
    public List<RegionInfo> mList;
    private ListView mListView;
    /* access modifiers changed from: private */
    public RegionListener mRegionListener;

    public RegionInfo getCurrentRegion() {
        return this.mCurrentRegion;
    }

    public void setCurrentRegion(RegionInfo regionInfo) {
        this.mCurrentRegion = regionInfo;
    }

    public RegionListener getRegionListener() {
        return this.mRegionListener;
    }

    public void setRegionListener(RegionListener regionListener) {
        this.mRegionListener = regionListener;
    }

    public List<RegionInfo> getList() {
        return this.mList;
    }

    public void setList(List<RegionInfo> list) {
        this.mList = list;
    }

    public HashMap<String, Integer> getLetterMap() {
        return this.mLetterMap;
    }

    public void setLetterMap(HashMap<String, Integer> hashMap) {
        this.mLetterMap = hashMap;
    }

    public List<String> getLetterList() {
        return this.mLetterList;
    }

    public void setLetterList(List<String> list) {
        this.mLetterList = list;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, R.style.DialogTheme);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog.getWindow().setLayout(displayMetrics.widthPixels, -2);
            WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
            attributes.gravity = 80;
            getDialog().getWindow().setAttributes(attributes);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().requestWindowFeature(1);
        View inflate = layoutInflater.inflate(R.layout.aliuser_register_region_fragment, viewGroup);
        this.mListView = (ListView) inflate.findViewById(R.id.aliuser_region_listview);
        if (!(this.mListView == null || this.mAdapter == null)) {
            this.mListView.setAdapter(this.mAdapter);
            if (this.mCurrentRegion != null) {
                int selection = getSelection();
                this.mListView.setSelection(selection);
                this.mListView.setItemChecked(selection, true);
                this.mAdapter.setSelectedItem(this.mCurrentRegion);
            }
            this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    RegionInfo regionInfo = (RegionInfo) RegionDialogFragment.this.mList.get(i);
                    new Intent().putExtra(RegistConstants.REGION_INFO, regionInfo);
                    Properties properties = new Properties();
                    properties.put("position", String.valueOf(i));
                    properties.put("countryCode", regionInfo.domain);
                    UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder("List_Reg_selectCountry");
                    uTCustomHitBuilder.setProperties(MapUtils.convertPropertiesToMap(properties));
                    UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
                    RegionDialogFragment.this.dismissAllowingStateLoss();
                    if (RegionDialogFragment.this.mRegionListener != null) {
                        RegionDialogFragment.this.mRegionListener.onClick(regionInfo);
                    }
                }
            });
        }
        return inflate;
    }

    private int getSelection() {
        if (!(this.mCurrentRegion == null || TextUtils.isEmpty(this.mCurrentRegion.name) || this.mList == null)) {
            for (int i = 0; i < this.mList.size(); i++) {
                RegionInfo regionInfo = this.mList.get(i);
                if (regionInfo != null && !TextUtils.isEmpty(regionInfo.name) && regionInfo.name.equals(this.mCurrentRegion.name)) {
                    return i;
                }
            }
        }
        return 0;
    }

    public void setupAdapter(Context context) {
        this.mAdapter = new RegionAdapter(context, this.mList);
    }
}
