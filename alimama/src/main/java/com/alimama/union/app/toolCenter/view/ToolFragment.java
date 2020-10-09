package com.alimama.union.app.toolCenter.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.moon.ui.IBottomNavFragment;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.union.app.toolCenter.adapter.ToolFragmentAdapter;
import com.alimama.union.app.toolCenter.data.HomeToolFragDataModel;
import com.alimama.union.app.toolCenter.data.HomeToolFragDataResult;
import com.alimama.union.app.toolCenter.data.ToolItemBean;
import com.alimama.union.app.toolCenter.data.ToolTabDataEvent;
import com.alimama.union.app.views.ISViewContainer;
import com.alimama.unionwl.utils.LocalDisplay;
import com.google.gson.Gson;
import com.ut.mini.UTAnalytics;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

public class ToolFragment extends BaseFragment implements IBottomNavFragment, ISViewContainer.IViewContainerRefreshListener {
    public static final String PAGE_NAME = "union/union_tools";
    private static final String SPM_CNT = "a21wq.12106853";
    private ToolFragmentAdapter mAdapter;
    private ISViewContainer mContainer;
    private HomeToolFragDataModel mHomeToolFragDataModel;
    private List<ToolItemBean> mList = new ArrayList();
    private RecyclerView mRecyclerView;

    public static ToolFragment newInstance() {
        return new ToolFragment();
    }

    public void willBeDisplayed() {
        UTHelper.sendControlHit(PAGE_NAME, "showTool");
        UnionLensUtil.generatePrepvid();
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), PAGE_NAME);
    }

    public void willBeHidden() {
        UTHelper.sendControlHit(PAGE_NAME, "hiddenTool");
        SpmProcessor.pageDisappear(getActivity(), SPM_CNT);
    }

    public void refresh() {
        if (this.mHomeToolFragDataModel != null) {
            this.mHomeToolFragDataModel.sendRequest();
        }
        if (this.mContainer != null) {
            this.mContainer.showLoading();
        }
    }

    public String currFragmentTitle() {
        return getResources().getString(R.string.tab_tool_title);
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.layout_tool, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View view) {
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.tool_recycler_view);
        this.mContainer = (ISViewContainer) view.findViewById(R.id.tool_layout_container);
        this.mContainer.setContainerRefreshListener(this);
        this.mAdapter = new ToolFragmentAdapter(getActivity());
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        this.mRecyclerView.addItemDecoration(new SimpleItemDecoration(LocalDisplay.dp2px(12.0f)));
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    @Subscribe
    public void onEventMainThread(ToolTabDataEvent toolTabDataEvent) {
        if (!toolTabDataEvent.isSuccess) {
            this.mContainer.onDataLoadError();
            return;
        }
        this.mContainer.onDataLoaded();
        if (toolTabDataEvent.isSuccess) {
            HomeToolFragDataResult homeToolFragDataResult = toolTabDataEvent.dataResult;
            if (homeToolFragDataResult.toolItems == null || homeToolFragDataResult.toolItems.isEmpty()) {
                this.mContainer.onDataEmpty();
            } else if (this.mList.size() <= 0) {
                this.mList.clear();
                this.mList.addAll(homeToolFragDataResult.toolItems);
                this.mAdapter.setData(this.mList);
            } else if (!compare(homeToolFragDataResult.toolItems)) {
                this.mList.clear();
                this.mList.addAll(homeToolFragDataResult.toolItems);
                this.mAdapter.setData(this.mList);
            }
        }
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z) {
            this.mHomeToolFragDataModel = new HomeToolFragDataModel();
            this.mHomeToolFragDataModel.sendRequest();
            if (this.mContainer != null && this.mList.isEmpty()) {
                this.mContainer.showLoading();
            }
        }
    }

    public void refreshPage() {
        if (this.mHomeToolFragDataModel != null) {
            this.mHomeToolFragDataModel.sendRequest();
        }
        if (this.mContainer != null) {
            this.mContainer.showLoading();
        }
    }

    private boolean compare(List<ToolItemBean> list) {
        Gson gson = new Gson();
        return TextUtils.equals(gson.toJson((Object) this.mList), gson.toJson((Object) list));
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
