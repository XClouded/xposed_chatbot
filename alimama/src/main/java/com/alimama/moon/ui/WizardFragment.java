package com.alimama.moon.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alimama.moon.R;

public class WizardFragment extends Fragment {
    public static final String EXTRA_WIZARD_IMAGE_RES = "com.alimama.moon.ui.WizardFragment.EXTRA_WIZARD_IMAGE_RES";
    @DrawableRes
    private int imageRes;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.imageRes = bundle.getInt(EXTRA_WIZARD_IMAGE_RES);
        } else {
            this.imageRes = getArguments().getInt(EXTRA_WIZARD_IMAGE_RES);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(EXTRA_WIZARD_IMAGE_RES, this.imageRes);
        super.onSaveInstanceState(bundle);
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_wizard, viewGroup, false);
        ((ImageView) inflate.findViewById(R.id.image_view)).setImageResource(this.imageRes);
        return inflate;
    }
}
