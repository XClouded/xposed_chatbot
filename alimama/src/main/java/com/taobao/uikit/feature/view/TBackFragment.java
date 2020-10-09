package com.taobao.uikit.feature.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import com.taobao.uikit.feature.features.ImageSaveFeature;

public class TBackFragment extends Fragment {
    private ImageSaveFeature mImageSaveFeature;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.mImageSaveFeature != null) {
            this.mImageSaveFeature.onActivityResult(i, i2, intent);
        }
    }

    public void setImageSaveFeature(ImageSaveFeature imageSaveFeature) {
        this.mImageSaveFeature = imageSaveFeature;
    }
}
