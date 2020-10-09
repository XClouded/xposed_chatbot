package com.alimama.unwdinamicxcontainer.presenter.dxcengine;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unweventparse.constants.EventConstants;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alimama.unwdinamicxcontainer.R;
import com.alimama.unwdinamicxcontainer.adapter.EndlessRecyclerOnScrollListener;
import com.alimama.unwdinamicxcontainer.diywidget.DXUNWCouponViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.DXUNWFinalPriceViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.DXUNWPageTimerTipViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.DXUNWProgressViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.DXWNWDiscountTagViewWidgetNode;
import com.alimama.unwdinamicxcontainer.diywidget.viewcontainer.ISViewContainer;
import com.alimama.unwdinamicxcontainer.event.BaseEvent;
import com.alimama.unwdinamicxcontainer.event.IDXEvent;
import com.alimama.unwdinamicxcontainer.event.IDXEventRegister;
import com.alimama.unwdinamicxcontainer.model.dxcengine.DXCLoadMoreModel;
import com.alimama.unwdinamicxcontainer.model.dxcengine.GlobalModel;
import com.alimama.unwdinamicxcontainer.model.dxcengine.UltronageDataInvalidType;
import com.alimama.unwdinamicxcontainer.utils.DXContainerUtil;
import com.alimama.unwdinamicxcontainer.utils.GlobalModelUtil;
import com.taobao.android.dinamicx.DXAbsEventHandler;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.template.utils.DXHashUtil;
import com.taobao.android.dxcontainer.AliDXContainerDataChange;
import com.taobao.android.dxcontainer.DXContainerEngine;
import com.taobao.android.dxcontainer.DXContainerEngineConfig;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.life.EngineMainLoadMoreListener;
import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreController;
import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreStateText;
import java.util.HashMap;
import java.util.List;

public class UNWDinamicXContainerEngine implements IDXContainerEngine, IDXEventRegister {
    private static final int MAX_SPAN_SIZE = 2;
    /* access modifiers changed from: private */
    public static String TAG = "UNWDinamicXContainerEngine";
    private boolean isEmptyList;
    /* access modifiers changed from: private */
    public boolean isEnableLoadMore;
    private boolean isRegisterEmptyView;
    /* access modifiers changed from: private */
    public boolean isShowBottomView;
    private String mBizType;
    private ISViewContainer mContainer;
    private Context mContext;
    /* access modifiers changed from: private */
    public IDXContainerLoadMoreController mDXCLoadMoreState;
    private DXContainerEngine mDXContainerEngine;
    /* access modifiers changed from: private */
    public IDXContainerPresenter mDXContainerPresenter;
    /* access modifiers changed from: private */
    public DXCLoadMoreModel mDxcLoadMoreModel;
    private String mEmptyMsg;
    private GlobalModel mGlobalModel;
    /* access modifiers changed from: private */
    public HashMap<String, IDXEvent> mHashMap;
    private View mInjectEmptyView;
    private ViewGroup.LayoutParams mInjectEmptyViewLayoutParams;
    private LinearLayout mInjectErrorViewLl;
    /* access modifiers changed from: private */
    public RecyclerView.LayoutManager mLayoutManager;
    /* access modifiers changed from: private */
    public int mLoadMoreState;
    private RecyclerView mRecyclerView;
    private View mTopView;
    private FrameLayout mView;

    public enum OrderType {
        ASCENDING_ORDER,
        DESCENDING_ORDER
    }

    public UNWDinamicXContainerEngine(Context context, IDXContainerPresenter iDXContainerPresenter, String str, DXCLoadMoreModel dXCLoadMoreModel) {
        UNWManager.getInstance().getLogger().info(TAG, "UNWDinamicXContainerEngine", "constructor");
        this.mContext = context;
        this.mDXContainerPresenter = iDXContainerPresenter;
        this.mBizType = str;
        this.mDxcLoadMoreModel = dXCLoadMoreModel;
        initProperties();
        onCreateView();
        initDXContainerEngine();
        registerCommonEvents();
    }

    private void initProperties() {
        this.isEnableLoadMore = true;
        this.mLoadMoreState = 0;
        this.isShowBottomView = true;
        this.isEmptyList = false;
        this.mEmptyMsg = "";
        this.isRegisterEmptyView = false;
        this.mHashMap = new HashMap<>();
        this.mGlobalModel = new GlobalModel();
    }

    private void registerCommonEvents() {
        registerEvent(EventConstants.UT.NAME, new BaseEvent());
        registerEvent(EventConstants.pageRouter.NAME, new BaseEvent());
    }

    private void onCreateView() {
        this.mTopView = LayoutInflater.from(this.mContext).inflate(R.layout.dxc_engine_layout, (ViewGroup) null);
        this.mContainer = (ISViewContainer) this.mTopView.findViewById(R.id.dxc_view_container);
        this.mContainer.setErrorViewListener(new IDXCErrorViewListener() {
            public void clickRefreshBtn() {
                UNWDinamicXContainerEngine.this.mDXContainerPresenter.clickErrorViewRefreshBtn();
            }
        });
        this.mView = (FrameLayout) this.mTopView.findViewById(R.id.dxc_view_holder);
        this.mInjectErrorViewLl = (LinearLayout) this.mTopView.findViewById(R.id.inject_error_view_ll);
    }

    private void initDXContainerEngine() {
        this.mDXContainerEngine = new DXContainerEngine(this.mContext, new DXContainerEngineConfig.Builder(this.mBizType).withEnableDXCRootView(true).withDefaultSelectedTab(1).withIDXCLoadMoreStateText(new IDXContainerLoadMoreStateText() {
            public String getNormalText() {
                return UNWDinamicXContainerEngine.this.mDxcLoadMoreModel != null ? UNWDinamicXContainerEngine.this.mDxcLoadMoreModel.getNormalText() : "";
            }

            public String getLoadingText() {
                return UNWDinamicXContainerEngine.this.mDxcLoadMoreModel != null ? UNWDinamicXContainerEngine.this.mDxcLoadMoreModel.getLoadingText() : "";
            }

            public String getFailedText() {
                return UNWDinamicXContainerEngine.this.mDxcLoadMoreModel != null ? UNWDinamicXContainerEngine.this.mDxcLoadMoreModel.getFailedText() : "";
            }

            public String getNoMoreText() {
                return UNWDinamicXContainerEngine.this.mDxcLoadMoreModel != null ? UNWDinamicXContainerEngine.this.mDxcLoadMoreModel.getNoMoreText() : "";
            }
        }).build());
        this.mDXContainerEngine.registerDXWidget(DXWNWDiscountTagViewWidgetNode.DXWNWDISCOUNTTAGVIEW_WNWDISCOUNTTAGVIEW, new DXWNWDiscountTagViewWidgetNode.Builder());
        this.mDXContainerEngine.registerDXWidget(DXUNWCouponViewWidgetNode.DXUNWCOUPONVIEW_UNWCOUPONVIEW, new DXUNWCouponViewWidgetNode.Builder());
        this.mDXContainerEngine.registerDXWidget(DXUNWProgressViewWidgetNode.DXUNWPROGRESSVIEW_UNWPROGRESSVIEW, new DXUNWProgressViewWidgetNode.Builder());
        this.mDXContainerEngine.registerDXWidget(DXUNWPageTimerTipViewWidgetNode.DXUNWPAGETIMERTIPVIEW_UNWPAGETIMERTIPVIEW, new DXUNWPageTimerTipViewWidgetNode.Builder());
        this.mDXContainerEngine.registerDXWidget(DXUNWFinalPriceViewWidgetNode.DXUNWFINALPRICEVIEW_UNWFINALPRICEVIEW, new DXUNWFinalPriceViewWidgetNode.Builder());
        this.mDXContainerEngine.registerDXEventHandler(DXHashUtil.hash("UNWCommonClick"), new DXAbsEventHandler() {
            public void handleEvent(DXEvent dXEvent, Object[] objArr, DXRuntimeContext dXRuntimeContext) {
                try {
                    JSONArray parseArray = JSONObject.parseArray(objArr[0].toString());
                    for (int i = 0; i < parseArray.size(); i++) {
                        JSONObject jSONObject = (JSONObject) parseArray.get(i);
                        String string = jSONObject.getString("type");
                        if (UNWDinamicXContainerEngine.this.mHashMap.get(string) != null) {
                            ((IDXEvent) UNWDinamicXContainerEngine.this.mHashMap.get(string)).executeEvent(jSONObject.toJSONString());
                        }
                    }
                } catch (Exception e) {
                    UNWManager.getInstance().getLogger().error(UNWDinamicXContainerEngine.TAG, "registerDXEventHandler", e.toString());
                }
            }
        });
        this.mRecyclerView = (RecyclerView) this.mDXContainerEngine.getContentView();
        this.mLayoutManager = this.mRecyclerView.getLayoutManager();
        this.mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(this.mLayoutManager) {
            public void onLoadMore(int i) {
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i == 0) {
                    int i2 = 0;
                    if (UNWDinamicXContainerEngine.this.mLayoutManager instanceof LinearLayoutManager) {
                        i2 = ((LinearLayoutManager) UNWDinamicXContainerEngine.this.mLayoutManager).findFirstVisibleItemPosition();
                    }
                    UNWDinamicXContainerEngine.this.mDXContainerPresenter.recyclerViewScrollStateChanged(i2);
                }
            }
        });
        this.mView.addView(this.mRecyclerView);
        initLoadMore();
    }

    private void initLoadMore() {
        this.mDXContainerEngine.setPreLoadMoreListener(new EngineMainLoadMoreListener() {
            public boolean isEnableLoadMore() {
                return true;
            }

            public void onLoadMore(IDXContainerLoadMoreController iDXContainerLoadMoreController) {
                IDXContainerLoadMoreController unused = UNWDinamicXContainerEngine.this.mDXCLoadMoreState = iDXContainerLoadMoreController;
                if (UNWDinamicXContainerEngine.this.mDXCLoadMoreState != null) {
                    UNWDinamicXContainerEngine.this.mDXCLoadMoreState.setState(UNWDinamicXContainerEngine.this.mLoadMoreState);
                }
                if (UNWDinamicXContainerEngine.this.isEnableLoadMore) {
                    UNWDinamicXContainerEngine.this.mDXContainerPresenter.loadMore(iDXContainerLoadMoreController);
                }
            }

            public boolean isShowBottomView() {
                return UNWDinamicXContainerEngine.this.isShowBottomView;
            }
        });
    }

    public void enableFooterView(boolean z) {
        this.isShowBottomView = z;
    }

    public void initData(String str, String str2) {
        if (!DXContainerUtil.isUltronageDataValid(str, this.mDXContainerPresenter)) {
            UNWManager.getInstance().getLogger().error(TAG, "initData", "initData ultronage data is invalid");
            this.mDXContainerPresenter.renderFailed(str);
            return;
        }
        this.mEmptyMsg = str2;
        if (!updateGlobalModel(str)) {
            this.mDXContainerPresenter.initDataError();
            UNWManager.getInstance().getLogger().error(TAG, "initData", "initData initDataError");
        } else if (this.isEmptyList) {
            if (!this.isRegisterEmptyView) {
                showEmptyView(str2);
            } else {
                showErrorView(this.mInjectEmptyView, this.mInjectEmptyViewLayoutParams);
            }
            this.mDXContainerPresenter.isEmptyList();
            IEtaoLogger logger = UNWManager.getInstance().getLogger();
            String str3 = TAG;
            logger.info(str3, "initDataShowEmptyView", "emptyMsg: " + str2);
        } else {
            updateViewContainer(true);
            boolean initData = this.mDXContainerEngine.initData(AliDXContainerDataChange.exchange(JSON.parseObject(str)));
            if (initData) {
                UNWManager.getInstance().getLogger().success(TAG, "initData");
            } else {
                UNWManager.getInstance().getLogger().error(TAG, "initData", "initData Failed");
            }
            updateLoadMoreAfterRendered(initData, str);
        }
    }

    public void appendData(String str) {
        if (!DXContainerUtil.isUltronageDataValid(str, this.mDXContainerPresenter)) {
            UNWManager.getInstance().getLogger().error(TAG, "appendData", "appendData ultronage data is invalid");
        } else if (!updateGlobalModel(str)) {
            UNWManager.getInstance().getLogger().error(TAG, "appendData", "updateGlobalModel is false");
        } else {
            updateViewContainer(true);
            this.mContainer.onDataLoaded();
            DXContainerModel exchange = AliDXContainerDataChange.exchange(JSON.parseObject(str));
            if (this.mDXContainerEngine.getRootDXCModel() == null) {
                UNWManager.getInstance().getLogger().error(TAG, "appendDataReInitData", "rootModel is null, reInitData");
                initData(str, this.mEmptyMsg);
                return;
            }
            boolean append = this.mDXContainerEngine.append(exchange, this.mDXContainerEngine.getRootDXCModel().getId());
            if (append) {
                UNWManager.getInstance().getLogger().success(TAG, "appendData");
            } else {
                UNWManager.getInstance().getLogger().error(TAG, "appendData", "appendData Failed");
            }
            updateLoadMoreAfterRendered(append, str);
        }
    }

    public void updateData(String str, OrderType orderType, boolean z, IDXCUpdateDataListener iDXCUpdateDataListener) {
        if (!DXContainerUtil.isUltronageDataValid(str, this.mDXContainerPresenter)) {
            UNWManager.getInstance().getLogger().error(TAG, "updateData", "updateData ultronage data is invalid");
        } else if (!updateGlobalModel(str)) {
            UNWManager.getInstance().getLogger().error(TAG, "updateData", "updateGlobalModel is false");
        } else {
            updateViewContainer(true);
            this.mContainer.onDataLoaded();
            DXContainerModel rootDXCModel = this.mDXContainerEngine.getRootDXCModel();
            if (rootDXCModel == null) {
                UNWManager.getInstance().getLogger().error(TAG, "updateDataReInitData", "rootModel is null, reInitData");
                initData(str, this.mEmptyMsg);
                return;
            }
            List<DXContainerModel> children = AliDXContainerDataChange.exchange(JSON.parseObject(str)).getChildren();
            for (int i = 0; i < children.size(); i++) {
                DXContainerModel dXContainerModel = children.get(i);
                String id = dXContainerModel.getId();
                if (DXContainerUtil.isRootModelContainsSubList(rootDXCModel, id, dXContainerModel)) {
                    List<DXContainerModel> children2 = dXContainerModel.getChildren();
                    for (int i2 = 0; i2 < children2.size(); i2++) {
                        DXContainerUtil.fetchUpdateModelList(rootDXCModel, children2.get(i2), id, z);
                    }
                }
            }
            if (orderType != null) {
                DXContainerUtil.sort(rootDXCModel, orderType);
            }
            this.mDXContainerEngine.refresh();
            updateLoadMoreAfterRendered(true, str);
            if (iDXCUpdateDataListener != null) {
                iDXCUpdateDataListener.updateSuccess(this.mDXContainerEngine.getRootDXCModel());
            }
        }
    }

    public View fetchRenderedView() {
        return this.mTopView;
    }

    public ISViewContainer fetchViewContainer() {
        return this.mContainer;
    }

    public RecyclerView fetchRecyclerView() {
        return this.mRecyclerView;
    }

    public GlobalModel fetchGlobalModel() {
        return this.mGlobalModel;
    }

    public void removeData(String str) {
        if (!DXContainerUtil.isUltronageDataValid(str, this.mDXContainerPresenter)) {
            UNWManager.getInstance().getLogger().error(TAG, "removeData", "removeData ultronage data is invalid");
            return;
        }
        if (this.mDXContainerEngine.remove(AliDXContainerDataChange.exchange(JSON.parseObject(str)))) {
            UNWManager.getInstance().getLogger().success(TAG, "removeData");
        } else {
            UNWManager.getInstance().getLogger().error(TAG, "removeData", "removeData Failed");
        }
    }

    public void refresh() {
        this.mDXContainerEngine.refresh();
    }

    public void insertData(String str, int i) {
        if (!DXContainerUtil.isUltronageDataValid(str, this.mDXContainerPresenter)) {
            UNWManager.getInstance().getLogger().error(TAG, "insertData", "insertData ultronage data is invalid");
            return;
        }
        if (this.mDXContainerEngine.insert(AliDXContainerDataChange.exchange(JSON.parseObject(str)), i, this.mDXContainerEngine.getRootDXCModel().getId())) {
            UNWManager.getInstance().getLogger().success(TAG, "insertData");
        } else {
            UNWManager.getInstance().getLogger().error(TAG, "insertData", "insertData Failed");
        }
    }

    public void updateDXCLoadMoreState(int i) {
        this.mLoadMoreState = i;
        if (this.mDXCLoadMoreState != null) {
            this.mDXCLoadMoreState.setState(i);
        }
    }

    public void configLoadMoreModel(DXCLoadMoreModel dXCLoadMoreModel) {
        this.mDxcLoadMoreModel = dXCLoadMoreModel;
    }

    public void showErrorView(String str) {
        this.mContainer.onNetworkError(str, R.drawable.default_empty);
        IEtaoLogger logger = UNWManager.getInstance().getLogger();
        String str2 = TAG;
        logger.info(str2, "showErrorView", "errorMsg is: " + str);
    }

    public void showErrorView(View view) {
        updateViewContainer(false);
        this.mInjectErrorViewLl.removeAllViews();
        this.mInjectErrorViewLl.addView(view);
        UNWManager.getInstance().getLogger().info(TAG, "showErrorView", "inject error view");
    }

    public void showErrorView(View view, ViewGroup.LayoutParams layoutParams) {
        updateViewContainer(false);
        this.mInjectErrorViewLl.removeAllViews();
        this.mInjectErrorViewLl.addView(view, layoutParams);
        UNWManager.getInstance().getLogger().info(TAG, "showErrorView", "inject error view and params");
    }

    public void registerEvent(String str, IDXEvent iDXEvent) {
        this.mHashMap.put(str, iDXEvent);
    }

    private void updateViewContainer(boolean z) {
        if (z) {
            this.mContainer.setVisibility(0);
            this.mInjectErrorViewLl.setVisibility(8);
            return;
        }
        this.mContainer.setVisibility(8);
        this.mInjectErrorViewLl.setVisibility(0);
    }

    public void showEmptyView(String str) {
        this.mContainer.onEmptyData(str);
    }

    public void registerEmptyView(View view, ViewGroup.LayoutParams layoutParams) {
        this.isRegisterEmptyView = true;
        this.mInjectEmptyView = view;
        this.mInjectEmptyViewLayoutParams = layoutParams;
    }

    private void showEmptyView() {
        updateViewContainer(false);
        this.mInjectErrorViewLl.removeAllViews();
        this.mInjectErrorViewLl.addView(this.mInjectEmptyView, this.mInjectEmptyViewLayoutParams);
        UNWManager.getInstance().getLogger().info(TAG, "showEmptyView", "inject empty view and params");
    }

    private void updateLoadMoreAfterRendered(boolean z, String str) {
        if (z) {
            updateDXCLoadMoreState(0);
            this.mDXContainerPresenter.renderSuccess(this.mRecyclerView);
        } else {
            updateDXCLoadMoreState(3);
            this.mDXContainerPresenter.renderFailed(str);
        }
        if (z && !this.isEnableLoadMore) {
            updateDXCLoadMoreState(2);
        }
    }

    public boolean updateGlobalModel(String str) {
        try {
            GlobalModelUtil.updateGlobalModel(str, this.mGlobalModel);
            this.isEnableLoadMore = TextUtils.equals(this.mGlobalModel.getExtendParams().getLoadMore(), "true");
            this.isEmptyList = TextUtils.equals(this.mGlobalModel.getExtendParams().getEmptyList(), "true");
            if (!TextUtils.equals(this.mGlobalModel.getExtendParams().getUnwSuccess(), "true")) {
                this.mDXContainerPresenter.ultronageDataInValid(UltronageDataInvalidType.UNWSUCCESS_NOT_TRUE);
                UNWManager.getInstance().getLogger().error(TAG, "updateGlobalModel", "unwSuccess is not true");
                return false;
            } else if (this.isEnableLoadMore) {
                return true;
            } else {
                HashMap hashMap = new HashMap();
                hashMap.put("json", str);
                UNWManager.getInstance().getLogger().info(TAG, "updateGlobalModel", "isEnableLoadMore == false", hashMap);
                return true;
            }
        } catch (Exception e) {
            IEtaoLogger logger = UNWManager.getInstance().getLogger();
            String str2 = TAG;
            logger.error(str2, "updateGlobalModel", "Exception is: " + e.toString());
            return false;
        }
    }
}
