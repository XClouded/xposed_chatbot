package com.alimama.moon.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.service.BeanContext;

public class UserGuideSingeActivity extends PageRouterActivity {
    private View mGuideLayout;
    private int mIndex;
    private ImageView mMidH5TabWizardImg;
    private TextView mSkipMaskTv;

    static /* synthetic */ int access$004(UserGuideSingeActivity userGuideSingeActivity) {
        int i = userGuideSingeActivity.mIndex + 1;
        userGuideSingeActivity.mIndex = i;
        return i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_user_guide_single);
        initData();
        this.mGuideLayout = findViewById(R.id.user_guide_layout_view);
        this.mMidH5TabWizardImg = (ImageView) findViewById(R.id.mid_h5_tab_wizard_img);
        this.mSkipMaskTv = (TextView) findViewById(R.id.skip_mask_tv);
        this.mSkipMaskTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserGuideSingeActivity.this.finish();
                ((SettingManager) BeanContext.get(SettingManager.class)).setMidTabUserGuideFirst(false);
            }
        });
        this.mGuideLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserGuideSingeActivity.this.updateMideTabWizardImg(UserGuideSingeActivity.access$004(UserGuideSingeActivity.this));
            }
        });
    }

    private void initData() {
        this.mIndex = 0;
    }

    /* access modifiers changed from: private */
    public void updateMideTabWizardImg(int i) {
        if (i == 0) {
            this.mMidH5TabWizardImg.setImageResource(R.drawable.goods_wizard1);
        } else if (i == 1) {
            this.mMidH5TabWizardImg.setImageResource(R.drawable.goods_wizard2);
        } else if (i == 2) {
            this.mMidH5TabWizardImg.setImageResource(R.drawable.goods_wizard3);
        } else {
            finish();
            ((SettingManager) BeanContext.get(SettingManager.class)).setMidTabUserGuideFirst(false);
        }
    }
}
