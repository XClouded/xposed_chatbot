package com.alimama.moon.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.service.BeanContext;
import java.util.ArrayList;
import java.util.List;

public class UserGuideActivity extends BaseActivity {
    private List<Integer> GUIDE_IMAGE_RES_IDS;
    private int currentResId;
    private ImageView userGuideImageView;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_user_guide);
        this.GUIDE_IMAGE_RES_IDS = new ArrayList();
        this.GUIDE_IMAGE_RES_IDS.add(Integer.valueOf(R.drawable.step1));
        this.userGuideImageView = (ImageView) findViewById(R.id.user_guide_image_view);
        setImageResourceId(this.GUIDE_IMAGE_RES_IDS.get(0).intValue());
        this.userGuideImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserGuideActivity.this.showNextGuide();
            }
        });
        ((SettingManager) BeanContext.get(SettingManager.class)).setUserGuideFirst(false);
    }

    private void setImageResourceId(int i) {
        this.currentResId = i;
        this.userGuideImageView.setImageResource(i);
    }

    /* access modifiers changed from: private */
    public void showNextGuide() {
        int indexOf = this.GUIDE_IMAGE_RES_IDS.indexOf(Integer.valueOf(this.currentResId));
        if (indexOf == this.GUIDE_IMAGE_RES_IDS.size() - 1) {
            finish();
        } else {
            setImageResourceId(this.GUIDE_IMAGE_RES_IDS.get(indexOf + 1).intValue());
        }
    }
}
