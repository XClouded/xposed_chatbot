package com.alimama.moon.features.home.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.ui.mask.MasterApprenticeGuideActivity;

public class HomeApprenticeAreaViewHolder extends HomeCommonItemViewHolder {
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        showUserGuide(layoutInflater.getContext());
        return super.createView(layoutInflater, viewGroup);
    }

    public void showUserGuide(Context context) {
        if (context != null && ((SettingManager) BeanContext.get(SettingManager.class)).isMasterApprenticeUserGuideFirst()) {
            ((SettingManager) BeanContext.get(SettingManager.class)).setMasterApprenticeUserGuideFirst(false);
            context.startActivity(new Intent(context, MasterApprenticeGuideActivity.class));
        }
    }
}
