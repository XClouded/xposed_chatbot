package com.ali.user.mobile.ui.widget;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.ui.R;
import java.util.List;

public class BottomMenuFragment extends DialogFragment {
    private List<MenuItem> menuItems;

    public List<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(List<MenuItem> list) {
        this.menuItems = list;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getDialog().requestWindowFeature(1);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().getWindow().setWindowAnimations(R.style.AliUserMenuAnimation);
        View inflate = layoutInflater.inflate(R.layout.aliuser_fragment_bottom_menu, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.aliuser_cancel_tv)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!TextUtils.isEmpty(BottomMenuFragment.this.getTag())) {
                    UserTrackAdapter.sendControlUT(BottomMenuFragment.this.getTag(), "Button-Cancel");
                }
                BottomMenuFragment.this.dismiss();
            }
        });
        ((ListView) inflate.findViewById(R.id.aliuser_menu_lv)).setAdapter(new MenuItemAdapter(getActivity().getBaseContext(), this.menuItems));
        return inflate;
    }

    public void onStart() {
        super.onStart();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getDialog().getWindow().setLayout(displayMetrics.widthPixels, getDialog().getWindow().getAttributes().height);
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.gravity = 80;
        getDialog().getWindow().setAttributes(attributes);
    }

    public void onStop() {
        getView().setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.aliuser_menu_out));
        super.onStop();
    }

    public void show(FragmentManager fragmentManager, String str) {
        try {
            super.show(fragmentManager, str);
        } catch (IllegalStateException unused) {
        }
    }
}
