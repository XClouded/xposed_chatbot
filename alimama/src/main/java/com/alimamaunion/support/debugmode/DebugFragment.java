package com.alimamaunion.support.debugmode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class DebugFragment extends Fragment {
    private List<DebugItemData> dataList = new ArrayList();
    private DebugRecyclerAdapter mAdapter;

    public static DebugFragment newInstance(List<DebugItemData> list) {
        DebugFragment debugFragment = new DebugFragment();
        debugFragment.dataList.addAll(list);
        return debugFragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return new View(layoutInflater.getContext());
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }
}
