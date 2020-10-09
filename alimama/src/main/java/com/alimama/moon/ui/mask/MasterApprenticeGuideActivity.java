package com.alimama.moon.ui.mask;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.alimama.moon.R;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.ui.PageRouterActivity;

public class MasterApprenticeGuideActivity extends PageRouterActivity {
    private View mGuideLayout;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_master_apprentice_guide);
        this.mGuideLayout = findViewById(R.id.master_user_guide_layout_view);
        this.mGuideLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MasterApprenticeGuideActivity.this.finish();
                ((SettingManager) BeanContext.get(SettingManager.class)).setMasterApprenticeUserGuideFirst(false);
            }
        });
    }
}
