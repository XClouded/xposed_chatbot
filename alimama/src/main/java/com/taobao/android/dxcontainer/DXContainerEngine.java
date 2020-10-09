package com.taobao.android.dxcontainer;

import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.taobao.android.dinamicx.DXAbsEventHandler;
import com.taobao.android.dinamicx.expression.parser.IDXDataParser;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;
import com.taobao.android.dxcontainer.adapter.DXContainerBaseLayoutManager;
import com.taobao.android.dxcontainer.event.DXContainerEventCallback;
import com.taobao.android.dxcontainer.layout.IDXContainerLayout;
import com.taobao.android.dxcontainer.life.EngineLifeStateListener;
import com.taobao.android.dxcontainer.life.EngineLoadMoreListener;
import com.taobao.android.dxcontainer.loadmore.DXContainerDefaultLoadMoreStateText;
import com.taobao.android.dxcontainer.loadmore.IDXContainerLoadMoreStateText;
import com.taobao.android.dxcontainer.render.IDXContainerComponentRender;
import com.taobao.android.dxcontainer.render.IDXContainerRender;
import com.taobao.android.dxcontainer.vlayout.LayoutHelper;
import com.taobao.android.dxcontainer.vlayout.layout.StaggeredGridLayoutHelper;
import java.util.List;

public class DXContainerEngine extends DXContainerBaseClass {
    private static boolean hasInitialize = false;
    /* access modifiers changed from: private */
    public IDXContainerWrapper containerWrapper;
    private ViewPager.OnPageChangeListener indicator;
    private IDXContainerInternalStickyListener internalDXCStickyListener = new IDXContainerInternalStickyListener();
    private EngineLoadMoreListener loadMoreListener;
    private SparseArray<String> loadMoreViewTexts = new SparseArray<>();
    private DXContainerMainManager mainManager;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private int setDefaultSelectedTab;
    private int tabHeight;
    private DXContainerTabManager tabManager;

    public DXContainerTabManager getTabManager() {
        return this.tabManager;
    }

    public static void initialize(@NonNull Context context, @Nullable DXContainerGlobalInitConfig dXContainerGlobalInitConfig) {
        try {
            if (!hasInitialize) {
                if (dXContainerGlobalInitConfig != null) {
                    if (dXContainerGlobalInitConfig.idxContainerAppMonitor != null) {
                        DXContainerAppMonitor.setDxcAppMonitor(dXContainerGlobalInitConfig.idxContainerAppMonitor);
                    } else {
                        DXContainerAppMonitor.setDxcAppMonitor(new IDXContainerAppMonitor() {
                            public void alarm_commitFail(String str, String str2, String str3, String str4) {
                            }

                            public void alarm_commitFail(String str, String str2, String str3, String str4, String str5) {
                            }

                            public void alarm_commitSuccess(String str, String str2) {
                            }

                            public void alarm_commitSuccess(String str, String str2, String str3) {
                            }

                            public void counter_commit(String str, String str2, double d) {
                            }

                            public void counter_commit(String str, String str2, String str3, double d) {
                            }

                            public void stat_begin(String str, String str2, String str3) {
                            }

                            public void stat_commit(String str, String str2, double d) {
                            }

                            public void stat_end(String str, String str2, String str3) {
                            }
                        });
                    }
                    if (dXContainerGlobalInitConfig.recyclerViewInterface != null) {
                        DXContainerGlobalCenter.recyclerViewInterface = dXContainerGlobalInitConfig.recyclerViewInterface;
                    } else {
                        DXContainerGlobalCenter.recyclerViewInterface = new IDXContainerRecyclerViewInterface() {
                            public boolean setRecyclerViewAttr(RecyclerView recyclerView, DXContainerRecyclerViewOption dXContainerRecyclerViewOption) {
                                return false;
                            }

                            public RecyclerView newRecyclerView(Context context, DXContainerRecyclerViewOption dXContainerRecyclerViewOption) {
                                return new RecyclerView(context);
                            }
                        };
                    }
                    DXContainerGlobalCenter.debug = dXContainerGlobalInitConfig.isDebug;
                    hasInitialize = true;
                    return;
                }
                throw new RuntimeException();
            }
        } catch (Throwable unused) {
        }
    }

    public DXContainerEngine(Context context, DXContainerEngineConfig dXContainerEngineConfig) {
        super(new DXContainerEngineContext(context, dXContainerEngineConfig));
        this.setDefaultSelectedTab = dXContainerEngineConfig.getDefaultSelectedTab();
        this.containerEngineContext.setEngine(this);
        this.containerEngineContext.init();
        this.mainManager = new DXContainerMainManager(this.containerEngineContext);
        this.tabManager = new DXContainerTabManager(this.containerEngineContext);
    }

    public IDXContainerInternalStickyListener getStickyListener() {
        return this.internalDXCStickyListener;
    }

    public void setPreLoadMoreListener(EngineLoadMoreListener engineLoadMoreListener) {
        this.loadMoreListener = engineLoadMoreListener;
        IDXContainerLoadMoreStateText stateText = getContainerEngineConfig().getStateText();
        if (stateText == null) {
            stateText = new DXContainerDefaultLoadMoreStateText();
        }
        setLoadMoreStatusTexts(stateText);
        this.mainManager.setPreLoadMoreListener(this.loadMoreListener, this.loadMoreViewTexts);
    }

    private void setLoadMoreStatusTexts(IDXContainerLoadMoreStateText iDXContainerLoadMoreStateText) {
        if (iDXContainerLoadMoreStateText != null) {
            this.loadMoreViewTexts.put(0, iDXContainerLoadMoreStateText.getNormalText());
            this.loadMoreViewTexts.put(1, iDXContainerLoadMoreStateText.getLoadingText());
            this.loadMoreViewTexts.put(3, iDXContainerLoadMoreStateText.getFailedText());
            this.loadMoreViewTexts.put(2, iDXContainerLoadMoreStateText.getNoMoreText());
        }
    }

    public boolean registerDXEventHandler(long j, DXAbsEventHandler dXAbsEventHandler) {
        return this.containerEngineContext.registerEventHandler(j, dXAbsEventHandler);
    }

    public void registerDefaultEventHandler(long j, DXContainerEventCallback dXContainerEventCallback) {
        this.containerEngineContext.registerDefaultEventHandler(j, dXContainerEventCallback);
    }

    public void registerDXWidget(long j, IDXBuilderWidgetNode iDXBuilderWidgetNode) {
        this.containerEngineContext.registerDXWidget(j, iDXBuilderWidgetNode);
    }

    public void registerRender(String str, IDXContainerRender iDXContainerRender) {
        this.containerEngineContext.registerRender(str, iDXContainerRender);
    }

    public void registerNativeComponent(String str, IDXContainerComponentRender iDXContainerComponentRender) {
        this.containerEngineContext.registerNativeComponent(str, iDXContainerComponentRender);
    }

    public void registerDXDataParser(long j, IDXDataParser iDXDataParser) {
        this.containerEngineContext.registerDXDataParser(j, iDXDataParser);
    }

    public void registerLayout(String str, IDXContainerLayout iDXContainerLayout) {
        this.containerEngineContext.registerLayout(str, iDXContainerLayout);
    }

    public boolean initData(DXContainerModel dXContainerModel) {
        boolean initData = this.mainManager.initData(dXContainerModel);
        this.tabManager.resetState();
        return initData;
    }

    public DXContainerModel getRootDXCModel() {
        return this.mainManager.getSingleCManager().getRootModel();
    }

    public void setDefaultSelectedTab(int i) {
        this.setDefaultSelectedTab = i;
    }

    public int getDefaultSelectedTab() {
        return this.setDefaultSelectedTab;
    }

    /* access modifiers changed from: package-private */
    public EngineLoadMoreListener getLoadMoreListener() {
        return this.loadMoreListener;
    }

    /* access modifiers changed from: package-private */
    public SparseArray<String> getLoadMoreViewTexts() {
        return this.loadMoreViewTexts;
    }

    public void setTabChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.pageChangeListener = onPageChangeListener;
    }

    /* access modifiers changed from: package-private */
    public ViewPager.OnPageChangeListener getTabChangeListener() {
        return this.pageChangeListener;
    }

    public void setTabIndicator(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.indicator = onPageChangeListener;
        if (this.tabManager.getViewPager() != null && this.indicator != null) {
            this.tabManager.getViewPager().setTabIndicator(this.indicator);
        }
    }

    /* access modifiers changed from: package-private */
    public ViewPager.OnPageChangeListener getTabIndicator() {
        return this.indicator;
    }

    /* access modifiers changed from: package-private */
    public void setTabViewPager(final DXContainerViewPager dXContainerViewPager) {
        if (dXContainerViewPager != null) {
            this.tabManager.setViewPager(dXContainerViewPager);
            dXContainerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int i) {
                }

                public void onPageScrolled(int i, float f, int i2) {
                }

                public void onPageSelected(int i) {
                    if (DXContainerEngine.this.containerWrapper != null) {
                        DXContainerEngine.this.containerWrapper.setCurrentChild(dXContainerViewPager.getCurrentPage(i));
                    }
                }
            });
            this.internalDXCStickyListener.setViewPager(dXContainerViewPager);
        }
    }

    public int getTabContentHeight() {
        return this.mainManager.getSingleCManager().getRecyclerViewContentHeight() - this.tabHeight;
    }

    /* access modifiers changed from: package-private */
    public IDXContainerWrapper getContainerWrapper() {
        return this.containerWrapper;
    }

    public void setTabHeight(int i) {
        ViewGroup.LayoutParams layoutParams;
        this.tabHeight = i;
        if (this.containerWrapper != null) {
            this.containerWrapper.setTopHeight(this.mainManager.getSingleCManager().getRecyclerViewPaddingTop() + i);
            if (this.tabManager.getViewPager() != null && (layoutParams = this.tabManager.getViewPager().getLayoutParams()) != null) {
                layoutParams.height = getTabContentHeight();
            }
        }
    }

    public boolean add(DXContainerModel dXContainerModel, DXContainerModel dXContainerModel2) {
        if (dXContainerModel == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, 4000, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_ADD_MODEL_NOT_EXIST);
            return false;
        } else if (dXContainerModel2 == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, 4001, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_ADD_TARGET_MODEL_NOT_EXIST);
            return false;
        } else {
            dXContainerModel2.addChild(dXContainerModel);
            dXContainerModel2.getSingleCManager().refreshAll();
            return true;
        }
    }

    public boolean add(DXContainerModel dXContainerModel, String str) {
        return add(dXContainerModel, getDXCModelByID(str));
    }

    public boolean append(DXContainerModel dXContainerModel, DXContainerModel dXContainerModel2) {
        if (dXContainerModel == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_APPEND_MODEL_NOT_EXIST, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_APPEND_MODEL_NOT_EXIST);
            return false;
        } else if (dXContainerModel.getChildren() == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_APPEND_MODEL_NO_CHILD, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_APPEND_MODEL_NO_CHILD);
            return false;
        } else if (dXContainerModel2 == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_APPEND_PARENT_MODEL_NOT_EXIST, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_APPEND_PARENT_MODEL_NOT_EXIST);
            return false;
        } else {
            for (DXContainerModel addChild : dXContainerModel.getChildren()) {
                dXContainerModel2.addChild(addChild);
            }
            dXContainerModel2.getSingleCManager().refreshAll();
            return true;
        }
    }

    public boolean append(DXContainerModel dXContainerModel, String str) {
        return append(dXContainerModel, getDXCModelByID(str));
    }

    public boolean update(DXContainerModel dXContainerModel) {
        if (dXContainerModel == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_UPDATE_MODEL_NOT_EXIST, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_UPDATE_MODEL_NOT_EXIST);
            return false;
        } else if (dXContainerModel.getEngine() == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_UPDATE_MODEL_ENGINE_NULL, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_UPDATE_MODEL_ENGINE_NULL);
            return false;
        } else {
            int positionByModelId = dXContainerModel.getEngine().getPositionByModelId(dXContainerModel.getId());
            if (positionByModelId == -1) {
                return false;
            }
            dXContainerModel.makeDirty();
            RecyclerView contentView = dXContainerModel.getSingleCManager().getContentView();
            Parcelable onSaveInstanceState = contentView.getLayoutManager().onSaveInstanceState();
            dXContainerModel.getSingleCManager().notifyItemRangeChange(positionByModelId, 1);
            contentView.getLayoutManager().onRestoreInstanceState(onSaveInstanceState);
            return true;
        }
    }

    public boolean insert(DXContainerModel dXContainerModel, int i, DXContainerModel dXContainerModel2) {
        if (dXContainerModel == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, 4002, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_INSERT_MODEL_NOT_EXIST);
            return false;
        } else if (dXContainerModel2 == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, 4003, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_INSERT_TARGET_MODEL_NOT_EXIST);
            return false;
        } else {
            dXContainerModel2.insertChildWithDXCModel(dXContainerModel, i);
            insertRefresh(i, dXContainerModel2, 1);
            return true;
        }
    }

    public boolean insert(List<DXContainerModel> list, int i, DXContainerModel dXContainerModel) {
        if (list == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_INSERT_MODELS_NOT_EXIST, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_INSERT_MODELS_NOT_EXIST);
            return false;
        } else if (dXContainerModel == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, 4003, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_INSERT_TARGET_MODEL_NOT_EXIST);
            return false;
        } else if (list.size() < 1) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_INSERT_MODELS_NO_CHILD, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_INSERT_MODELS_NO_CHILD);
            return false;
        } else {
            for (int i2 = 0; i2 < list.size(); i2++) {
                dXContainerModel.insertChildWithDXCModel(list.get(i2), i + i2);
            }
            insertRefresh(i, dXContainerModel, list.size());
            return true;
        }
    }

    public boolean insert(DXContainerModel dXContainerModel, int i, String str) {
        return insert(dXContainerModel, i, getDXCModelByID(str));
    }

    private void insertRefresh(int i, DXContainerModel dXContainerModel, int i2) {
        DXContainerSingleRVManager singleCManager = dXContainerModel.getSingleCManager();
        singleCManager.updateAllMap(true);
        List<LayoutHelper> layoutHelpers = singleCManager.getAdapter().getLayoutHelpers();
        int size = layoutHelpers.size();
        for (int i3 = 0; i3 < size; i3++) {
            LayoutHelper layoutHelper = layoutHelpers.get(i3);
            int intValue = layoutHelper.getRange().getLower().intValue();
            int intValue2 = layoutHelper.getRange().getUpper().intValue();
            if (intValue <= i && i <= intValue2 && (layoutHelper instanceof StaggeredGridLayoutHelper)) {
                ((StaggeredGridLayoutHelper) layoutHelper).spanUpdate(i - intValue);
            }
        }
        RecyclerView contentView = singleCManager.getContentView();
        Parcelable onSaveInstanceState = contentView.getLayoutManager().onSaveInstanceState();
        singleCManager.notifyItemRangeInsert(i, i2);
        contentView.getLayoutManager().onRestoreInstanceState(onSaveInstanceState);
    }

    public boolean remove(String str) {
        return remove(getDXCModelByID(str));
    }

    public boolean remove(DXContainerModel dXContainerModel) {
        if (dXContainerModel == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_REMOVE_MODEL_NOT_EXIST, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_REMOVE_MODEL_NOT_EXIST);
            return false;
        }
        int positionByModelId = dXContainerModel.getEngine().getPositionByModelId(dXContainerModel.getId());
        dXContainerModel.removeFromParent();
        DXContainerSingleRVManager singleCManager = dXContainerModel.getSingleCManager();
        singleCManager.updateAllMap(false);
        List<LayoutHelper> layoutHelpers = singleCManager.getAdapter().getLayoutHelpers();
        int size = layoutHelpers.size();
        for (int i = 0; i < size; i++) {
            LayoutHelper layoutHelper = layoutHelpers.get(i);
            int intValue = layoutHelper.getRange().getLower().intValue();
            int intValue2 = layoutHelper.getRange().getUpper().intValue();
            if (intValue <= positionByModelId && positionByModelId <= intValue2 && (layoutHelper instanceof StaggeredGridLayoutHelper)) {
                ((StaggeredGridLayoutHelper) layoutHelper).spanUpdate(positionByModelId - intValue);
            }
        }
        RecyclerView contentView = singleCManager.getContentView();
        Parcelable onSaveInstanceState = contentView.getLayoutManager().onSaveInstanceState();
        singleCManager.notifyItemRangeRemoved(positionByModelId, 1);
        contentView.getLayoutManager().onRestoreInstanceState(onSaveInstanceState);
        return true;
    }

    public void refresh() {
        this.mainManager.getSingleCManager().refreshAll();
        this.tabManager.refreshAll();
    }

    public void setContainerWrapper(IDXContainerWrapper iDXContainerWrapper) {
        if (DXContainerGlobalCenter.isDebug()) {
            DXContainerAppMonitor.logi("setContainerWrapper");
        }
        this.containerWrapper = iDXContainerWrapper;
        iDXContainerWrapper.setRoot(getContentView());
    }

    public DXContainerModel getTabRootDXCModel(int i) {
        return this.tabManager.getTabRootDXCModel(i);
    }

    public DXContainerBaseLayoutManager getTabLayoutManager(int i) {
        return this.tabManager.getTabLayoutManager(i);
    }

    public int getCurrentTabIndex() {
        if (this.tabManager.getViewPager() != null) {
            return this.tabManager.getViewPager().getCurrentItem();
        }
        DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_RENDER, DXContainerErrorConstant.DX_CONTAINER_ERROR_GET_CURRENT_TAB_INDEX_VIEW_PAGER_NULL, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_GET_CURRENT_TAB_INDEX_VIEW_PAGER_NULL);
        return -1;
    }

    public boolean setCurrentTabIndex(int i) {
        if (this.tabManager.getViewPager() == null) {
            return false;
        }
        this.tabManager.getViewPager().setCurrentItem(i);
        return true;
    }

    public List<DXContainerModel> getDXCModelByTag(String str) {
        if (!TextUtils.isEmpty(str)) {
            return getContainerEngineContext().getModelManager().getDXCModelByTag(str);
        }
        DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_GET_DXC_MODEL_BY_TAG_TAG_EMPTY, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_GET_DXC_MODEL_BY_TAG_TAG_EMPTY);
        return null;
    }

    public DXContainerModel getDXCModelByID(String str) {
        if (!TextUtils.isEmpty(str)) {
            return getContainerEngineContext().getModelManager().getDXCModelByID(str);
        }
        DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_GET_DXC_MODEL_BY_ID_ID_EMPTY, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_GET_DXC_MODEL_BY_ID_ID_EMPTY);
        return null;
    }

    public ViewGroup getContentView() {
        return this.mainManager.getSingleCManager().getContentView();
    }

    public void setEngineLifeListener(EngineLifeStateListener engineLifeStateListener) {
        this.mainManager.getSingleCManager().getAdapter().setLifeListener(engineLifeStateListener);
    }

    public void setStickyListener(DXContainerStickyListener dXContainerStickyListener) {
        this.internalDXCStickyListener.setListener(dXContainerStickyListener);
    }

    public int getPositionByModelId(String str) {
        DXContainerModel dXCModelByID = getDXCModelByID(str);
        if (dXCModelByID == null) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_GET_POSITION_BY_MODEL_ID_MODEL_NULL, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_GET_POSITION_BY_MODEL_ID_MODEL_NULL);
            return -1;
        }
        int positionByModelId = dXCModelByID.getSingleCManager().getViewTypeGenerator().getPositionByModelId(str);
        if (positionByModelId == -1) {
            DXContainerAppMonitor.trackerError(getContainerEngineConfig().getBizType(), (DXContainerModel) null, DXContainerErrorConstant.DXC_MONITOR_POINT_ENGINE_MODEL, DXContainerErrorConstant.DX_CONTAINER_ERROR_GET_POSITION_BY_MODEL_ID_POSITION_INVALID, DXContainerErrorConstant.DX_CONTAINER_ERROR_MESSAGE_GET_POSITION_BY_MODEL_ID_POSITION_INVALID);
        }
        return positionByModelId;
    }

    public void scrollToTop() {
        scrollToTop(0);
    }

    public void scrollToTop(int i) {
        this.tabManager.scrollToTop();
        scrollToPosition(0, i);
    }

    public void scrollToPosition(int i, int i2) {
        scrollToPosition(getRootDXCModel(), i, i2);
    }

    public void smoothScrollToPosition(int i, int i2, DXContainerScrollFinishedListener dXContainerScrollFinishedListener) {
        smoothScrollToPosition(getRootDXCModel(), i, i2, dXContainerScrollFinishedListener);
    }

    public void smoothScrollToPosition(DXContainerModel dXContainerModel, int i, int i2, DXContainerScrollFinishedListener dXContainerScrollFinishedListener) {
        dXContainerModel.getSingleCManager().smoothScrollToPosition(i, i2, dXContainerScrollFinishedListener);
    }

    public void scrollToPosition(String str, int i, int i2) {
        scrollToPosition(getDXCModelByID(str), i, i2);
    }

    public void scrollToPosition(DXContainerModel dXContainerModel, int i, int i2) {
        if (dXContainerModel != null) {
            dXContainerModel.getSingleCManager().scrollToPosition(i, i2);
        }
    }
}
