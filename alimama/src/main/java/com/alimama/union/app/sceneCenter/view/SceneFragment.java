package com.alimama.union.app.sceneCenter.view;

import android.os.Bundle;
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
import com.alimama.union.app.sceneCenter.data.SceneFragDataModel;
import com.alimama.union.app.sceneCenter.data.SceneTabDataModelEvent;
import com.alimama.union.app.sceneCenter.data.SceneTabDataResult;
import com.alimama.union.app.toolCenter.view.SimpleItemDecoration;
import com.alimama.union.app.views.ISViewContainer;
import com.alimama.unionwl.utils.LocalDisplay;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.alimamaunion.common.listpage.CommonRecyclerAdapter;
import com.ut.mini.UTAnalytics;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

public class SceneFragment extends BaseFragment implements IBottomNavFragment, ISViewContainer.IViewContainerRefreshListener {
    public static final String PAGE_NAME = "union/union_scene";
    private static final String SPM_CNT = "a21wq.12106862";
    private CommonRecyclerAdapter mAdapter;
    private ISViewContainer mContainer;
    private List<CommonItemInfo> mList = new ArrayList();
    private RecyclerView mRecyclerView;
    private SceneFragDataModel mSceneFragDataModel;

    public static SceneFragment newInstance() {
        return new SceneFragment();
    }

    public View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.layout_scene, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z) {
            this.mSceneFragDataModel = new SceneFragDataModel();
            this.mSceneFragDataModel.sendRequest();
            if (this.mContainer != null && this.mList.isEmpty()) {
                this.mContainer.showLoading();
            }
        }
    }

    private void initView(View view) {
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.scene_recycler_view);
        this.mContainer = (ISViewContainer) view.findViewById(R.id.scene_layout_container);
        this.mContainer.setContainerRefreshListener(this);
        this.mAdapter = new CommonRecyclerAdapter();
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new SimpleItemDecoration(LocalDisplay.dp2px(12.0f)));
    }

    public void willBeDisplayed() {
        UTHelper.sendControlHit(PAGE_NAME, "showScene");
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), PAGE_NAME);
    }

    public void willBeHidden() {
        UTHelper.sendControlHit(PAGE_NAME, "hiddenScene");
        SpmProcessor.pageDisappear(getActivity(), SPM_CNT);
    }

    public void refresh() {
        if (this.mSceneFragDataModel != null) {
            this.mSceneFragDataModel.sendRequest();
        }
        if (this.mContainer != null) {
            this.mContainer.showLoading();
        }
    }

    public String currFragmentTitle() {
        return getResources().getString(R.string.tab_scene_title);
    }

    @Subscribe
    public void onEventMainThread(SceneTabDataModelEvent sceneTabDataModelEvent) {
        if (!sceneTabDataModelEvent.isSuccess) {
            this.mContainer.onDataLoadError();
            return;
        }
        this.mContainer.onDataLoaded();
        if (sceneTabDataModelEvent.isSuccess) {
            SceneTabDataResult sceneTabDataResult = sceneTabDataModelEvent.dataResult;
            if (sceneTabDataResult.toolItems == null || sceneTabDataResult.toolItems.isEmpty()) {
                this.mContainer.onDataEmpty();
                return;
            }
            this.mList.clear();
            this.mList.addAll(sceneTabDataModelEvent.dataResult.toolItems);
            this.mAdapter.notifyResult(true, sceneTabDataModelEvent.dataResult.toolItems);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void refreshPage() {
        if (this.mSceneFragDataModel != null) {
            this.mSceneFragDataModel.sendRequest();
        }
        if (this.mContainer != null) {
            this.mContainer.showLoading();
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
