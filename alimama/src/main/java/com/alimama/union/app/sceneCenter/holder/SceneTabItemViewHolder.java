package com.alimama.union.app.sceneCenter.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alimama.moon.R;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.sceneCenter.item.SceneTabItem;
import com.alimama.union.app.sceneCenter.view.SceneFragment;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimamaunion.common.listpage.CommonBaseViewHolder;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;

public class SceneTabItemViewHolder implements CommonBaseViewHolder<SceneTabItem> {
    private static final String SCENE_ACTION_NAME = "click_scenename_scene_%d";
    private Context mContext;
    private TextView mDesc;
    private EtaoDraweeView mLogoImage;
    private View mRootView;
    private TextView mSubDesc;
    private EtaoDraweeView mTipImage;
    private TextView mTitle;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.mContext = viewGroup.getContext();
        this.mRootView = layoutInflater.inflate(R.layout.scene_tab_item_layout, viewGroup, false);
        this.mTitle = (TextView) this.mRootView.findViewById(R.id.scene_item_title);
        this.mDesc = (TextView) this.mRootView.findViewById(R.id.scene_item_desc);
        this.mSubDesc = (TextView) this.mRootView.findViewById(R.id.scene_item_sub_desc);
        this.mLogoImage = (EtaoDraweeView) this.mRootView.findViewById(R.id.scene_item_left_img);
        this.mTipImage = (EtaoDraweeView) this.mRootView.findViewById(R.id.scene_tip_img);
        return this.mRootView;
    }

    public void onBindViewHolder(final int i, final SceneTabItem sceneTabItem) {
        this.mTitle.setText(sceneTabItem.mTitle);
        this.mDesc.setText(sceneTabItem.mDescription);
        this.mSubDesc.setText(sceneTabItem.mSubDescription);
        String str = (String) this.mLogoImage.getTag();
        if (TextUtils.isEmpty(str)) {
            this.mLogoImage.setAnyImageUrl(sceneTabItem.mImg);
            this.mLogoImage.setTag(sceneTabItem.mImg);
        } else if (!TextUtils.equals(str, sceneTabItem.mImg)) {
            this.mLogoImage.setAnyImageUrl(sceneTabItem.mImg);
            this.mLogoImage.setTag(sceneTabItem.mImg);
        }
        if (TextUtils.isEmpty(sceneTabItem.mTagImg)) {
            this.mTipImage.setVisibility(8);
        } else {
            this.mTipImage.setVisibility(0);
            String str2 = (String) this.mTipImage.getTag();
            if (TextUtils.isEmpty(str2)) {
                calcShowArea();
                this.mTipImage.setAnyImageUrl(sceneTabItem.mTagImg);
                this.mTipImage.setTag(sceneTabItem.mTagImg);
            } else if (!TextUtils.equals(str2, sceneTabItem.mTagImg)) {
                calcShowArea();
                this.mTipImage.setAnyImageUrl(sceneTabItem.mTagImg);
                this.mTipImage.setTag(sceneTabItem.mTagImg);
            }
        }
        this.mRootView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!TextUtils.isEmpty(sceneTabItem.mUrl)) {
                    MoonComponentManager.getInstance().getPageRouter().gotoPage(sceneTabItem.mUrl);
                }
                SceneTabItemViewHolder.this.doUTAnalytics(i);
            }
        });
    }

    /* access modifiers changed from: private */
    public void doUTAnalytics(int i) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(SceneFragment.PAGE_NAME, String.format(SCENE_ACTION_NAME, new Object[]{Integer.valueOf(i)})).build());
    }

    private void calcShowArea() {
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadii(24.0f, 0.0f, 0.0f, 0.0f);
        GenericDraweeHierarchy build = new GenericDraweeHierarchyBuilder(this.mContext.getResources()).build();
        build.setRoundingParams(roundingParams);
        build.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        this.mTipImage.setHierarchy(build);
    }
}
