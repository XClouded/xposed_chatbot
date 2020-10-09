package com.taobao.weex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.uikit.feature.features.FeatureFactory;
import com.taobao.weex.adapter.IDrawableLoader;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.adapter.IWXJscProcessManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.adapter.URIAdapter;
import com.taobao.weex.appfram.websocket.IWebSocketAdapter;
import com.taobao.weex.bridge.EventResult;
import com.taobao.weex.bridge.NativeInvokeHelper;
import com.taobao.weex.bridge.SimpleJSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXEaglePlugin;
import com.taobao.weex.bridge.WXModuleManager;
import com.taobao.weex.bridge.WXParams;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.OnWXScrollListener;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.common.WXRefreshData;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.http.WXHttpUtil;
import com.taobao.weex.instance.InstanceOnFireEventInterceptor;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.performance.WhiteScreenUtils;
import com.taobao.weex.render.WXAbstractRenderContainer;
import com.taobao.weex.tracing.WXTracing;
import com.taobao.weex.ui.action.GraphicActionAddElement;
import com.taobao.weex.ui.component.NestedContainer;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXEmbed;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.view.WXScrollView;
import com.taobao.weex.utils.FeatureSwitches;
import com.taobao.weex.utils.Trace;
import com.taobao.weex.utils.WXDeviceUtils;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXFileUtils;
import com.taobao.weex.utils.WXJsonUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXReflectionUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import com.taobao.weex.utils.cache.RegisterCache;
import com.taobao.weex.utils.tools.LogDetail;
import com.taobao.weex.utils.tools.TimeCalculator;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class WXSDKInstance implements IWXActivityStateListener, View.OnLayoutChangeListener {
    public static String ACTION_DEBUG_INSTANCE_REFRESH = "DEBUG_INSTANCE_REFRESH";
    public static String ACTION_INSTANCE_RELOAD = "INSTANCE_RELOAD";
    public static final String BUNDLE_URL = "bundleUrl";
    private static final String SOURCE_TEMPLATE_BASE64_MD5 = "templateSourceBase64MD5";
    public static String requestUrl = "requestUrl";
    static int sScreenHeight = -1;
    public WXBridgeManager.BundType bundleType;
    private List<JSONObject> componentsInfoExceedGPULimit;
    /* access modifiers changed from: private */
    public boolean createInstanceHeartBeat;
    private boolean enableFullScreenHeight;
    private boolean enableLayerType;
    /* access modifiers changed from: private */
    public boolean hasError;
    /* access modifiers changed from: private */
    public boolean hasException;
    public PriorityQueue<WXEmbed> hiddenEmbeds;
    /* access modifiers changed from: private */
    public Map<String, GraphicActionAddElement> inactiveAddElementAction;
    private boolean isCommit;
    /* access modifiers changed from: private */
    public boolean isDestroy;
    public boolean isNewFsEnd;
    private boolean isPreDownLoad;
    private boolean isPreInit;
    /* access modifiers changed from: private */
    public volatile boolean isPreRenderMode;
    /* access modifiers changed from: private */
    public boolean isRenderSuccess;
    private boolean isViewDisAppear;
    private WXInstanceApm mApmForInstance;
    private boolean mAutoAdjustDeviceWidth;
    private String mBundleUrl;
    private ComponentObserver mComponentObserver;
    private Map<String, String> mContainerInfo;
    private Map<Long, ContentBoxMeasurement> mContentBoxMeasurements;
    Context mContext;
    private boolean mCreateInstance;
    private boolean mCurrentGround;
    private CustomFontNetworkHandler mCustomFontNetworkHandler;
    private boolean mDisableSkipFrameworkInit;
    private volatile WXEaglePlugin mEaglePlugin;
    private String mEaglePluginName;
    public boolean mEnd;
    public int mExecJSTraceId;
    private boolean mFixMultiThreadBug;
    @NonNull
    private FlatGUIContext mFlatGUIContext;
    private WXGlobalEventReceiver mGlobalEventReceiver;
    private HashMap<String, List<String>> mGlobalEvents;
    public boolean mHasCreateFinish;
    private WXHttpListener mHttpListener;
    private ImageNetworkHandler mImageNetworkHandler;
    /* access modifiers changed from: private */
    public final String mInstanceId;
    private List<InstanceOnFireEventInterceptor> mInstanceOnFireEventInterceptorList;
    private int mInstanceViewPortWidth;
    private WXRefreshData mLastRefreshData;
    private List<String> mLayerOverFlowListeners;
    private int mMaxDeepLayer;
    private ConcurrentHashMap<String, ModuleIntercept> mModuleInterceptMap;
    private NativeInvokeHelper mNativeInvokeHelper;
    private boolean mNeedReLoad;
    private boolean mNeedValidate;
    private NestedInstanceInterceptor mNestedInstanceInterceptor;
    private boolean mPageDirty;
    private WXSDKInstance mParentInstance;
    private long mRefreshStartTime;
    private WXAbstractRenderContainer mRenderContainer;
    /* access modifiers changed from: private */
    public IWXRenderListener mRenderListener;
    public long mRenderStartNanos;
    public long mRenderStartTime;
    private WXRenderStrategy mRenderStrategy;
    private String mRenderType;
    private boolean mRendered;
    private WXComponent mRootComp;
    private ScrollView mScrollView;
    /* access modifiers changed from: private */
    public IWXStatisticsListener mStatisticsListener;
    private StreamNetworkHandler mStreamNetworkHandler;
    public TimeCalculator mTimeCalculator;
    private boolean mUseScroller;
    private IWXUserTrackAdapter mUserTrackAdapter;
    private Map<String, Serializable> mUserTrackParams;
    private List<OnInstanceVisibleListener> mVisibleListeners;
    private List<ActionBarHandler> mWXActionbarHandlers;
    private List<OnBackPressedHandler> mWXBackPressedHandlers;
    private List<OnActivityResultHandler> mWXOnActivityResultHandlers;
    private WXPerformance mWXPerformance;
    private List<OnWXScrollListener> mWXScrollListeners;
    private WXScrollView.WXScrollViewListener mWXScrollViewListener;
    private WXProcessNotify mWxProcessNotify;
    private int maxHiddenEmbedsNum;
    public long[] measureTimes;
    public String[] mwxDims;
    public Map<String, List<String>> responseHeaders;
    public WeakReference<String> templateRef;
    private boolean trackComponent;

    public interface ActionBarHandler {
        boolean onSupportNavigateUp();
    }

    public interface CustomFontNetworkHandler {
        String fetchLocal(String str);
    }

    public interface ImageNetworkHandler {
        String fetchLocal(String str);
    }

    public interface ModuleInterceptCallback {
        ModuleInterceptResult CallModuleMethod(String str, String str2, JSONArray jSONArray, JSONObject jSONObject);
    }

    public interface NestedInstanceInterceptor {
        void onCreateNestInstance(WXSDKInstance wXSDKInstance, NestedContainer nestedContainer);
    }

    public interface OnBackPressedHandler {
        boolean onBackPressed();
    }

    public interface OnInstanceVisibleListener {
        void onAppear();

        void onDisappear();
    }

    public interface StreamNetworkHandler {
        String fetchLocal(String str);
    }

    public interface WXProcessNotify {
        void crashed();

        void reboot();
    }

    public void onLayoutChange(View view) {
    }

    @Deprecated
    public void registerActivityStateListener(IWXActivityStateListener iWXActivityStateListener) {
    }

    @Deprecated
    public void setIWXUserTrackAdapter(IWXUserTrackAdapter iWXUserTrackAdapter) {
    }

    public List<JSONObject> getComponentsExceedGPULimit() {
        return this.componentsInfoExceedGPULimit;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void setComponentsInfoExceedGPULimit(JSONObject jSONObject) {
        if (jSONObject != null && !jSONObject.isEmpty()) {
            this.componentsInfoExceedGPULimit.add(jSONObject);
        }
    }

    public List<String> getLayerOverFlowListeners() {
        return this.mLayerOverFlowListeners;
    }

    public void addLayerOverFlowListener(String str) {
        if (this.mLayerOverFlowListeners == null) {
            this.mLayerOverFlowListeners = new ArrayList();
        }
        this.mLayerOverFlowListeners.add(str);
    }

    public void removeLayerOverFlowListener(String str) {
        if (this.mLayerOverFlowListeners != null) {
            this.mLayerOverFlowListeners.remove(str);
        }
    }

    public ImageNetworkHandler getImageNetworkHandler() {
        return this.mImageNetworkHandler;
    }

    public void setImageNetworkHandler(ImageNetworkHandler imageNetworkHandler) {
        this.mImageNetworkHandler = imageNetworkHandler;
    }

    public StreamNetworkHandler getStreamNetworkHandler() {
        return this.mStreamNetworkHandler;
    }

    public void setStreamNetworkHandler(StreamNetworkHandler streamNetworkHandler) {
        this.mStreamNetworkHandler = streamNetworkHandler;
    }

    public CustomFontNetworkHandler getCustomFontNetworkHandler() {
        return this.mCustomFontNetworkHandler;
    }

    public void setCustomFontNetworkHandler(CustomFontNetworkHandler customFontNetworkHandler) {
        this.mCustomFontNetworkHandler = customFontNetworkHandler;
    }

    public static abstract class OnActivityResultHandler {
        private String id;

        public abstract boolean onActivityResult(int i, int i2, Intent intent, String str);

        public OnActivityResultHandler(String str) {
            this.id = str;
        }

        public boolean onActivityResult(int i, int i2, Intent intent) {
            return onActivityResult(i, i2, intent, this.id);
        }
    }

    public void setUseSingleProcess(boolean z) {
        WXBridgeManager.getInstance().setUseSingleProcess(z);
    }

    public void setUseSandBox(boolean z) {
        WXBridgeManager.getInstance().setSandBoxContext(z);
    }

    public int getMaxHiddenEmbedsNum() {
        return this.maxHiddenEmbedsNum;
    }

    public void setMaxHiddenEmbedsNum(int i) {
        this.maxHiddenEmbedsNum = i;
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void addInActiveAddElementAction(String str, GraphicActionAddElement graphicActionAddElement) {
        this.inactiveAddElementAction.put(str, graphicActionAddElement);
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void removeInActiveAddElmentAction(String str) {
        this.inactiveAddElementAction.remove(str);
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public GraphicActionAddElement getInActiveAddElementAction(String str) {
        return this.inactiveAddElementAction.get(str);
    }

    public void setRenderContainer(RenderContainer renderContainer) {
        setWXAbstractRenderContainer(renderContainer);
    }

    public void setWXAbstractRenderContainer(WXAbstractRenderContainer wXAbstractRenderContainer) {
        if (wXAbstractRenderContainer != null) {
            wXAbstractRenderContainer.setSDKInstance(this);
            wXAbstractRenderContainer.addOnLayoutChangeListener(this);
        }
        this.mRenderContainer = wXAbstractRenderContainer;
        if (this.mRenderContainer == null || this.mRenderContainer.getLayoutParams() == null || this.mRenderContainer.getLayoutParams().width != -2) {
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    WXBridgeManager.getInstance().setRenderContentWrapContentToCore(false, WXSDKInstance.this.getInstanceId());
                }
            });
        } else {
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    WXBridgeManager.getInstance().setRenderContentWrapContentToCore(true, WXSDKInstance.this.getInstanceId());
                }
            });
        }
    }

    public boolean isTrackComponent() {
        return this.trackComponent;
    }

    public void setTrackComponent(boolean z) {
        this.trackComponent = z;
    }

    public boolean isLayerTypeEnabled() {
        return this.enableLayerType;
    }

    public void enableLayerType(boolean z) {
        this.enableLayerType = z;
    }

    @NonNull
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public FlatGUIContext getFlatUIContext() {
        return this.mFlatGUIContext;
    }

    public boolean isNeedValidate() {
        return this.mNeedValidate;
    }

    public boolean isNeedReLoad() {
        return this.mNeedReLoad;
    }

    public void setNeedLoad(boolean z) {
        this.mNeedReLoad = z;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void setEnableFullScreenHeight(boolean z) {
        this.enableFullScreenHeight = z;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public boolean isFullScreenHeightEnabled() {
        return this.enableFullScreenHeight;
    }

    public boolean isUseScroller() {
        return this.mUseScroller;
    }

    public void setUseScroller(boolean z) {
        this.mUseScroller = z;
    }

    public void setInstanceViewPortWidth(int i) {
        setInstanceViewPortWidth(i, false);
    }

    public void setInstanceViewPortWidth(int i, boolean z) {
        this.mInstanceViewPortWidth = i;
        this.mAutoAdjustDeviceWidth = false;
        if (!z) {
            WXBridgeManager.getInstance().setViewPortWidth(getInstanceId(), (float) this.mInstanceViewPortWidth);
        }
    }

    public void resetDeviceDisplayOfPage() {
        WXBridgeManager.getInstance().setDeviceDisplayOfPage(getInstanceId(), (float) WXViewUtils.getScreenWidth(getContext()), (float) WXViewUtils.getScreenHeight(getContext()));
    }

    public void setPageKeepRawCssStyles() {
        WXBridgeManager.getInstance().setPageArgument(getInstanceId(), "reserveCssStyles", "true");
    }

    public void reloadPageLayout() {
        WXBridgeManager.getInstance().reloadPageLayout(getInstanceId());
    }

    public void setAutoAdjustDeviceWidth(boolean z) {
        this.mAutoAdjustDeviceWidth = z;
    }

    public boolean isAutoAdjustDeviceWidth() {
        return this.mAutoAdjustDeviceWidth;
    }

    private void setDeviceDisplay(float f, float f2, float f3) {
        WXBridgeManager.getInstance().setDeviceDisplay(getInstanceId(), f, f2, f3);
    }

    public int getInstanceViewPortWidth() {
        return this.mInstanceViewPortWidth;
    }

    public WXSDKInstance(Context context) {
        this.mEnd = false;
        this.mHasCreateFinish = false;
        this.mBundleUrl = "";
        this.isDestroy = false;
        this.hasException = false;
        this.hasError = false;
        this.isRenderSuccess = false;
        this.createInstanceHeartBeat = false;
        this.isCommit = false;
        this.mGlobalEventReceiver = null;
        this.enableLayerType = true;
        this.mNeedValidate = false;
        this.mNeedReLoad = false;
        this.mUseScroller = false;
        this.mInstanceViewPortWidth = FeatureFactory.PRIORITY_ABOVE_NORMAL;
        this.enableFullScreenHeight = false;
        this.mFlatGUIContext = new FlatGUIContext();
        this.isNewFsEnd = false;
        this.componentsInfoExceedGPULimit = new LinkedList();
        this.mExecJSTraceId = WXTracing.nextId();
        this.isViewDisAppear = false;
        this.mwxDims = new String[5];
        this.measureTimes = new long[5];
        this.responseHeaders = new HashMap();
        this.mRenderStrategy = WXRenderStrategy.APPEND_ASYNC;
        this.mDisableSkipFrameworkInit = false;
        this.mRenderType = "platform";
        this.mPageDirty = true;
        this.mFixMultiThreadBug = false;
        this.mAutoAdjustDeviceWidth = WXEnvironment.AUTO_ADJUST_ENV_DEVICE_WIDTH;
        this.mCurrentGround = false;
        this.inactiveAddElementAction = new ArrayMap();
        this.mContentBoxMeasurements = new ArrayMap();
        this.maxHiddenEmbedsNum = -1;
        this.mVisibleListeners = new ArrayList();
        this.isPreInit = false;
        this.isPreDownLoad = false;
        this.mHttpListener = null;
        this.mCreateInstance = true;
        this.mGlobalEvents = new HashMap<>();
        this.mModuleInterceptMap = new ConcurrentHashMap<>();
        this.mInstanceId = WXSDKManager.getInstance().generateInstanceId();
        init(context);
    }

    public WXSDKInstance() {
        this.mEnd = false;
        this.mHasCreateFinish = false;
        this.mBundleUrl = "";
        this.isDestroy = false;
        this.hasException = false;
        this.hasError = false;
        this.isRenderSuccess = false;
        this.createInstanceHeartBeat = false;
        this.isCommit = false;
        this.mGlobalEventReceiver = null;
        this.enableLayerType = true;
        this.mNeedValidate = false;
        this.mNeedReLoad = false;
        this.mUseScroller = false;
        this.mInstanceViewPortWidth = FeatureFactory.PRIORITY_ABOVE_NORMAL;
        this.enableFullScreenHeight = false;
        this.mFlatGUIContext = new FlatGUIContext();
        this.isNewFsEnd = false;
        this.componentsInfoExceedGPULimit = new LinkedList();
        this.mExecJSTraceId = WXTracing.nextId();
        this.isViewDisAppear = false;
        this.mwxDims = new String[5];
        this.measureTimes = new long[5];
        this.responseHeaders = new HashMap();
        this.mRenderStrategy = WXRenderStrategy.APPEND_ASYNC;
        this.mDisableSkipFrameworkInit = false;
        this.mRenderType = "platform";
        this.mPageDirty = true;
        this.mFixMultiThreadBug = false;
        this.mAutoAdjustDeviceWidth = WXEnvironment.AUTO_ADJUST_ENV_DEVICE_WIDTH;
        this.mCurrentGround = false;
        this.inactiveAddElementAction = new ArrayMap();
        this.mContentBoxMeasurements = new ArrayMap();
        this.maxHiddenEmbedsNum = -1;
        this.mVisibleListeners = new ArrayList();
        this.isPreInit = false;
        this.isPreDownLoad = false;
        this.mHttpListener = null;
        this.mCreateInstance = true;
        this.mGlobalEvents = new HashMap<>();
        this.mModuleInterceptMap = new ConcurrentHashMap<>();
        this.mInstanceId = WXSDKManager.getInstance().generateInstanceId();
        this.mWXPerformance = new WXPerformance(this.mInstanceId);
        this.mApmForInstance = new WXInstanceApm(this.mInstanceId);
        WXSDKManager.getInstance().getAllInstanceMap().put(this.mInstanceId, this);
        this.mTimeCalculator = new TimeCalculator(this);
        initFixMultiThreadFlag();
    }

    private void initFixMultiThreadFlag() {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter != null) {
            this.mFixMultiThreadBug = Boolean.parseBoolean(wxConfigAdapter.getConfig("android_weex_ext_config", "fixMultiThreadBug", "true"));
        }
    }

    @RestrictTo({RestrictTo.Scope.TESTS})
    WXSDKInstance(Context context, String str) {
        this.mEnd = false;
        this.mHasCreateFinish = false;
        this.mBundleUrl = "";
        this.isDestroy = false;
        this.hasException = false;
        this.hasError = false;
        this.isRenderSuccess = false;
        this.createInstanceHeartBeat = false;
        this.isCommit = false;
        this.mGlobalEventReceiver = null;
        this.enableLayerType = true;
        this.mNeedValidate = false;
        this.mNeedReLoad = false;
        this.mUseScroller = false;
        this.mInstanceViewPortWidth = FeatureFactory.PRIORITY_ABOVE_NORMAL;
        this.enableFullScreenHeight = false;
        this.mFlatGUIContext = new FlatGUIContext();
        this.isNewFsEnd = false;
        this.componentsInfoExceedGPULimit = new LinkedList();
        this.mExecJSTraceId = WXTracing.nextId();
        this.isViewDisAppear = false;
        this.mwxDims = new String[5];
        this.measureTimes = new long[5];
        this.responseHeaders = new HashMap();
        this.mRenderStrategy = WXRenderStrategy.APPEND_ASYNC;
        this.mDisableSkipFrameworkInit = false;
        this.mRenderType = "platform";
        this.mPageDirty = true;
        this.mFixMultiThreadBug = false;
        this.mAutoAdjustDeviceWidth = WXEnvironment.AUTO_ADJUST_ENV_DEVICE_WIDTH;
        this.mCurrentGround = false;
        this.inactiveAddElementAction = new ArrayMap();
        this.mContentBoxMeasurements = new ArrayMap();
        this.maxHiddenEmbedsNum = -1;
        this.mVisibleListeners = new ArrayList();
        this.isPreInit = false;
        this.isPreDownLoad = false;
        this.mHttpListener = null;
        this.mCreateInstance = true;
        this.mGlobalEvents = new HashMap<>();
        this.mModuleInterceptMap = new ConcurrentHashMap<>();
        this.mInstanceId = str;
        init(context);
    }

    public WXComponent getRootComponent() {
        return this.mRootComp;
    }

    public void setNestedInstanceInterceptor(NestedInstanceInterceptor nestedInstanceInterceptor) {
        this.mNestedInstanceInterceptor = nestedInstanceInterceptor;
    }

    public final WXSDKInstance createNestedInstance(NestedContainer nestedContainer) {
        WXSDKInstance newNestedInstance = newNestedInstance();
        if (this.mNestedInstanceInterceptor != null) {
            this.mNestedInstanceInterceptor.onCreateNestInstance(newNestedInstance, nestedContainer);
        }
        if (newNestedInstance != null) {
            newNestedInstance.setComponentObserver(getComponentObserver());
        }
        return newNestedInstance;
    }

    /* access modifiers changed from: protected */
    public WXSDKInstance newNestedInstance() {
        return new WXSDKInstance(this.mContext);
    }

    public boolean isHasException() {
        return this.hasException;
    }

    public void setHasException(boolean z) {
        this.hasException = z;
    }

    public void createInstanceFuncHeartBeat() {
        WXLogUtils.d("createInstanceFuncHeartBeat: " + this.mInstanceId);
        this.createInstanceHeartBeat = true;
        getApmForInstance().onStage(WXInstanceApm.KEY_PAGE_STAGES_END_EXCUTE_BUNDLE);
    }

    public void addOnInstanceVisibleListener(OnInstanceVisibleListener onInstanceVisibleListener) {
        this.mVisibleListeners.add(onInstanceVisibleListener);
    }

    public void removeOnInstanceVisibleListener(OnInstanceVisibleListener onInstanceVisibleListener) {
        this.mVisibleListeners.remove(onInstanceVisibleListener);
    }

    public void init(Context context) {
        initFixMultiThreadFlag();
        RegisterCache.getInstance().idle(true);
        this.mContext = context;
        this.mContainerInfo = new HashMap(4);
        this.mNativeInvokeHelper = new NativeInvokeHelper(this.mInstanceId);
        if (this.mWXPerformance == null) {
            this.mWXPerformance = new WXPerformance(this.mInstanceId);
        }
        if (this.mApmForInstance == null) {
            this.mApmForInstance = new WXInstanceApm(this.mInstanceId);
        }
        this.mWXPerformance.WXSDKVersion = WXEnvironment.WXSDK_VERSION;
        this.mWXPerformance.JSLibInitTime = WXEnvironment.sJSLibInitTime;
        this.mUserTrackAdapter = WXSDKManager.getInstance().getIWXUserTrackAdapter();
        WXSDKManager.getInstance().getAllInstanceMap().put(this.mInstanceId, this);
        this.mContainerInfo.put(WXInstanceApm.KEY_PAGE_PROPERTIES_CONTAINER_NAME, context instanceof Activity ? context.getClass().getSimpleName() : "unKnowContainer");
        this.mContainerInfo.put(WXInstanceApm.KEY_PAGE_PROPERTIES_INSTANCE_TYPE, "page");
        this.mDisableSkipFrameworkInit = isDisableSkipFrameworkInDataRender();
        if (this.mTimeCalculator == null) {
            this.mTimeCalculator = new TimeCalculator(this);
        }
    }

    public void setComponentObserver(ComponentObserver componentObserver) {
        this.mComponentObserver = componentObserver;
    }

    public ComponentObserver getComponentObserver() {
        return this.mComponentObserver;
    }

    public NativeInvokeHelper getNativeInvokeHelper() {
        return this.mNativeInvokeHelper;
    }

    @Deprecated
    public void setBizType(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mWXPerformance.bizType = str;
        }
    }

    public ScrollView getScrollView() {
        return this.mScrollView;
    }

    public void setRootScrollView(ScrollView scrollView) {
        this.mScrollView = scrollView;
        if (this.mWXScrollViewListener != null && (this.mScrollView instanceof WXScrollView)) {
            ((WXScrollView) this.mScrollView).addScrollViewListener(this.mWXScrollViewListener);
        }
    }

    @Deprecated
    public void registerScrollViewListener(WXScrollView.WXScrollViewListener wXScrollViewListener) {
        this.mWXScrollViewListener = wXScrollViewListener;
    }

    @Deprecated
    public WXScrollView.WXScrollViewListener getScrollViewListener() {
        return this.mWXScrollViewListener;
    }

    public void setContainerInfo(String str, String str2) {
        this.mContainerInfo.put(str, str2);
    }

    public Map<String, String> getContainerInfo() {
        return this.mContainerInfo;
    }

    public void render(String str, Map<String, Object> map, String str2) {
        render(str, map, str2, WXRenderStrategy.APPEND_ASYNC);
    }

    @Deprecated
    public void render(String str, Map<String, Object> map, String str2, WXRenderStrategy wXRenderStrategy) {
        render("default", str, map, str2, wXRenderStrategy);
    }

    public void render(String str, String str2, Map<String, Object> map, String str3, WXRenderStrategy wXRenderStrategy) {
        render(str, new Script(str2), map, str3, wXRenderStrategy);
    }

    public void render(String str, Script script, Map<String, Object> map, String str2, WXRenderStrategy wXRenderStrategy) {
        this.mWXPerformance.beforeInstanceRender(this.mInstanceId);
        if (!WXEnvironment.isApkDebugable() || !"default".equals(str)) {
            renderInternal(str, script, map, str2, wXRenderStrategy);
        } else if (getUIContext() != null) {
            new AlertDialog.Builder(getUIContext()).setTitle("Error: Missing pageName").setMessage("We highly recommend you to set pageName. Call\nWXSDKInstance#render(String pageName, String template, Map<String, Object> options, String jsonInitData, WXRenderStrategy flag)\nto fix it.").show();
        }
    }

    public void render(String str, byte[] bArr, Map<String, Object> map, String str2) {
        render(str, new Script(bArr), map, str2, WXRenderStrategy.DATA_RENDER_BINARY);
    }

    private void ensureRenderArchor() {
        if (this.mRenderContainer == null && getContext() != null) {
            setRenderContainer(new RenderContainer(getContext()));
            this.mRenderContainer.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            this.mRenderContainer.setBackgroundColor(0);
            this.mRenderContainer.setSDKInstance(this);
            this.mRenderContainer.addOnLayoutChangeListener(this);
        }
    }

    private void renderInternal(String str, String str2, Map<String, Object> map, String str3, WXRenderStrategy wXRenderStrategy) {
        if (!this.mRendered && !TextUtils.isEmpty(str2)) {
            renderInternal(str, new Script(str2), map, str3, wXRenderStrategy);
        }
    }

    public boolean isPreInitMode() {
        return this.isPreInit;
    }

    public boolean isPreDownLoad() {
        return this.isPreDownLoad;
    }

    public void onInstanceReady() {
        WXLogUtils.d("test->", "onInstanceReady");
        this.mApmForInstance.onStage(WXInstanceApm.KEY_PAGE_STAGES_CONTAINER_READY);
        if (this.isPreInit || this.isPreDownLoad) {
            this.mApmForInstance.onInstanceReady(this.isPreDownLoad);
            if (this.isPreDownLoad) {
                this.mHttpListener.onInstanceReady();
            }
        }
    }

    public void preInit(String str, String str2, Map<String, Object> map, String str3, WXRenderStrategy wXRenderStrategy) {
        this.isPreInit = true;
        setRenderStrategy(wXRenderStrategy);
        if (map == null) {
            map = new HashMap<>();
        }
        this.mApmForInstance.isReady = false;
        WXSDKManager.getInstance().createInstance(this, new Script(str2), map, str3);
    }

    private void setRenderStrategy(WXRenderStrategy wXRenderStrategy) {
        this.mRenderStrategy = wXRenderStrategy;
        setEaglePlugin(WXEaglePluginManager.getPluginName(this.mRenderStrategy));
    }

    private void setEaglePlugin(String str) {
        this.mEaglePluginName = str;
        this.mEaglePlugin = WXEaglePluginManager.getInstance().getPlugin(this.mEaglePluginName);
    }

    public void preDownLoad(String str, Map<String, Object> map, String str2, WXRenderStrategy wXRenderStrategy) {
        this.isPreDownLoad = true;
        setRenderStrategy(wXRenderStrategy);
        this.mApmForInstance.isReady = false;
        renderByUrl(str, str, map, str2, wXRenderStrategy);
    }

    private void renderInternal(String str, Script script, Map<String, Object> map, String str2, WXRenderStrategy wXRenderStrategy) {
        if (!this.mRendered && script != null && !script.isEmpty()) {
            LogDetail createLogDetail = this.mTimeCalculator.createLogDetail("renderInternal");
            setRenderStrategy(wXRenderStrategy);
            if (!this.mApmForInstance.hasInit()) {
                this.mApmForInstance.doInit();
            }
            this.mApmForInstance.setPageName(str);
            this.mApmForInstance.onStage(WXInstanceApm.KEY_PAGE_STAGES_RENDER_ORGIGIN);
            this.mApmForInstance.doDelayCollectData();
            this.mWXPerformance.pageName = TextUtils.isEmpty(str) ? "defaultBundleUrl" : str;
            if (TextUtils.isEmpty(this.mBundleUrl)) {
                this.mBundleUrl = this.mWXPerformance.pageName;
            }
            if (WXTracing.isAvailable()) {
                WXTracing.TraceEvent newEvent = WXTracing.newEvent("executeBundleJS", this.mInstanceId, -1);
                newEvent.traceId = this.mExecJSTraceId;
                newEvent.iid = this.mInstanceId;
                newEvent.tname = "JSThread";
                newEvent.ph = "B";
                newEvent.submit();
                this.mRenderStartNanos = System.nanoTime();
            }
            ensureRenderArchor();
            if (map == null) {
                map = new HashMap<>();
            }
            Map<String, Object> map2 = map;
            if (!WXEnvironment.sDynamicMode || TextUtils.isEmpty(WXEnvironment.sDynamicUrl) || map2.get("dynamicMode") != null) {
                new TimeCalculator(this);
                this.mWXPerformance.JSTemplateSize = (double) (((float) script.length()) / 1024.0f);
                this.mApmForInstance.addStats(WXInstanceApm.KEY_PAGE_STATS_BUNDLE_SIZE, this.mWXPerformance.JSTemplateSize);
                this.mRenderStartTime = System.currentTimeMillis();
                WXSDKManager.getInstance().setCrashInfo(WXEnvironment.WEEX_CURRENT_KEY, str);
                if (this.mAutoAdjustDeviceWidth && WXDeviceUtils.isAutoResize(this.mContext)) {
                    if (WXEnvironment.AUTO_UPDATE_APPLICATION_SCREEN_SIZE) {
                        WXViewUtils.updateApplicationScreen(this.mContext);
                    }
                    WXParams initParams = WXBridgeManager.getInstance().getInitParams();
                    if (initParams != null && !TextUtils.equals(initParams.getDeviceWidth(), String.valueOf(WXViewUtils.getScreenWidth(this.mContext)))) {
                        initParams.setDeviceWidth(String.valueOf(WXViewUtils.getScreenWidth(this.mContext)));
                        initParams.setDeviceHeight(String.valueOf(WXViewUtils.getScreenHeight(this.mContext)));
                        float f = WXEnvironment.sApplication.getResources().getDisplayMetrics().density;
                        WXEnvironment.addCustomOptions("scale", Float.toString(f));
                        String str3 = null;
                        if (WXViewUtils.getStatusBarHeight(this.mContext) > 0) {
                            str3 = String.valueOf(WXViewUtils.getStatusBarHeight(this.mContext));
                        }
                        WXBridgeManager.getInstance().updateInitDeviceParams(initParams.getDeviceWidth(), initParams.getDeviceHeight(), Float.toString(f), str3);
                        setDeviceDisplay((float) WXViewUtils.getScreenWidth(this.mContext), (float) WXViewUtils.getScreenHeight(this.mContext), WXViewUtils.getScreenDensity(this.mContext));
                    }
                }
                createLogDetail.taskStart();
                if (isPreInitMode()) {
                    getApmForInstance().onStage(WXInstanceApm.KEY_PAGE_STAGES_LOAD_BUNDLE_START);
                    WXBridgeManager.getInstance().loadJsBundleInPreInitMode(getInstanceId(), script.getContent());
                } else {
                    WXSDKManager.getInstance().createInstance(this, script, map2, str2);
                }
                createLogDetail.taskEnd();
                this.mRendered = true;
                final IWXJscProcessManager wXJscProcessManager = WXSDKManager.getInstance().getWXJscProcessManager();
                if (wXJscProcessManager != null && wXJscProcessManager.shouldReboot()) {
                    WXSDKManager.getInstance().postOnUiThread(new Runnable() {
                        public void run() {
                            WXSDKInstance.this.checkWhiteScreen();
                            if (!WXSDKInstance.this.isDestroy && !WXSDKInstance.this.hasException && !WXSDKInstance.this.isRenderSuccess) {
                                if ((!WXSDKInstance.this.hasError || !FeatureSwitches.isOpenWithConfig("checkWeexRenderError", "android_weex_ext_config", "checkWeexRenderError", false)) && !WXSDKInstance.this.isDataRender()) {
                                    View containerView = WXSDKInstance.this.getContainerView();
                                    if ((containerView instanceof ViewGroup) && ((ViewGroup) containerView).getChildCount() == 0) {
                                        if (wXJscProcessManager.withException(WXSDKInstance.this)) {
                                            WXSDKInstance.this.onJSException(String.valueOf(WXErrorCode.WX_ERR_RELOAD_PAGE), "white screen", "No View in Container");
                                        }
                                        if (!WXSDKInstance.this.createInstanceHeartBeat) {
                                            WXBridgeManager.getInstance().callReportCrashReloadPage(WXSDKInstance.this.mInstanceId, (String) null);
                                            WXLogUtils.e("callReportCrashReloadPage with jsc reboot");
                                        }
                                    }
                                }
                            }
                        }
                    }, wXJscProcessManager.rebootTimeout());
                    return;
                }
                return;
            }
            map2.put("dynamicMode", "true");
            renderByUrl(str, WXEnvironment.sDynamicUrl, map2, str2, wXRenderStrategy);
        }
    }

    /* access modifiers changed from: private */
    public void checkWhiteScreen() {
        if (!this.isDestroy && WhiteScreenUtils.doWhiteScreenCheck() && WhiteScreenUtils.isWhiteScreen(this)) {
            WXErrorCode wXErrorCode = this.createInstanceHeartBeat ? WXErrorCode.WX_ERROR_WHITE_SCREEN : WXErrorCode.WHITE_SCREEN_RESPONSE_TIMEOUT;
            if (WXBridgeManager.getInstance().isRebootExceedLimit()) {
                wXErrorCode = WXErrorCode.WHITE_SCREEN_REBOOT_EXCEED_LIMIT;
            }
            HashMap hashMap = new HashMap(1);
            String takeViewTreeSnapShot = WhiteScreenUtils.takeViewTreeSnapShot(this);
            if (takeViewTreeSnapShot == null) {
                takeViewTreeSnapShot = "null viewTreeMsg";
            }
            hashMap.put("viewTree", takeViewTreeSnapShot);
            hashMap.put("weexCoreThreadStackTrace", WXBridgeManager.getInstance().getWeexCoreThreadStackTrace());
            for (Map.Entry next : WXStateRecord.getInstance().getStateInfo().entrySet()) {
                hashMap.put(next.getKey(), next.getValue());
            }
            WXExceptionUtils.commitCriticalExceptionRT(getInstanceId(), wXErrorCode, "checkEmptyScreen", wXErrorCode.getErrorMsg(), hashMap);
        }
    }

    public boolean skipFrameworkInit() {
        return getEaglePlugin() != null && getEaglePlugin().isSkipFrameworkInit(getInstanceId()) && !this.mDisableSkipFrameworkInit;
    }

    /* access modifiers changed from: private */
    public boolean isDataRender() {
        return getEaglePlugin() != null;
    }

    private String filterUrlByEaglePlugin(String str) {
        Pair<String, WXEaglePlugin> filterUrl = WXEaglePluginManager.getInstance().filterUrl(str);
        if (filterUrl == null) {
            return null;
        }
        String str2 = (String) filterUrl.first;
        this.mEaglePlugin = (WXEaglePlugin) filterUrl.second;
        this.mEaglePluginName = this.mEaglePlugin.getPluginName();
        this.mRenderStrategy = WXEaglePluginManager.getRenderStrategyByPlugin(this.mEaglePluginName);
        return str2;
    }

    private void renderByUrlInternal(String str, String str2, Map<String, Object> map, String str3, WXRenderStrategy wXRenderStrategy) {
        LogDetail createLogDetail = this.mTimeCalculator.createLogDetail("renderByUrlInternal");
        createLogDetail.taskStart();
        ensureRenderArchor();
        String wrapPageName = wrapPageName(str, str2);
        this.mBundleUrl = str2;
        setRenderStrategy(wXRenderStrategy);
        if (WXSDKManager.getInstance().getValidateProcessor() != null) {
            this.mNeedValidate = WXSDKManager.getInstance().getValidateProcessor().needValidate(this.mBundleUrl);
        }
        String filterUrlByEaglePlugin = filterUrlByEaglePlugin(str2);
        if (filterUrlByEaglePlugin != null) {
            wXRenderStrategy = this.mRenderStrategy;
        } else {
            filterUrlByEaglePlugin = str2;
        }
        WXRenderStrategy wXRenderStrategy2 = wXRenderStrategy;
        if (map == null) {
            map = new HashMap<>();
        }
        Map<String, Object> map2 = map;
        if (!map2.containsKey("bundleUrl")) {
            map2.put("bundleUrl", filterUrlByEaglePlugin);
        }
        getWXPerformance().pageName = wrapPageName;
        this.mApmForInstance.doInit();
        this.mApmForInstance.setPageName(wrapPageName);
        Uri parse = Uri.parse(filterUrlByEaglePlugin);
        if (parse == null || !TextUtils.equals(parse.getScheme(), "file")) {
            IWXHttpAdapter iWXHttpAdapter = WXSDKManager.getInstance().getIWXHttpAdapter();
            WXRequest wXRequest = new WXRequest();
            wXRequest.url = rewriteUri(Uri.parse(filterUrlByEaglePlugin), URIAdapter.BUNDLE).toString();
            if (!TextUtils.isEmpty(wXRequest.url)) {
                requestUrl = wXRequest.url;
            } else {
                requestUrl = wrapPageName;
            }
            if (wXRequest.paramMap == null) {
                wXRequest.paramMap = new HashMap();
            }
            wXRequest.instanceId = getInstanceId();
            wXRequest.paramMap.put("user-agent", WXHttpUtil.assembleUserAgent(this.mContext, WXEnvironment.getConfig()));
            wXRequest.paramMap.put("isBundleRequest", "true");
            this.mHttpListener = new WXHttpListener(this, wrapPageName, map2, str3, wXRenderStrategy2, System.currentTimeMillis());
            this.mHttpListener.isPreDownLoadMode = this.isPreDownLoad;
            this.mHttpListener.setSDKInstance(this);
            this.mApmForInstance.onStage(WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_START);
            iWXHttpAdapter.sendRequest(wXRequest, this.mHttpListener);
            createLogDetail.taskEnd();
            return;
        }
        this.mApmForInstance.onStage(WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_START);
        String loadFileOrAsset = WXFileUtils.loadFileOrAsset(assembleFilePath(parse), this.mContext);
        this.mApmForInstance.onStage(WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_END);
        render(wrapPageName, loadFileOrAsset, map2, str3, wXRenderStrategy2);
    }

    @Deprecated
    public void render(String str, String str2, Map<String, Object> map, String str3, int i, int i2, WXRenderStrategy wXRenderStrategy) {
        render(str, str2, map, str3, wXRenderStrategy);
    }

    public void render(String str) {
        render("default", str, (Map<String, Object>) null, (String) null, this.mRenderStrategy);
    }

    @Deprecated
    public void render(String str, int i, int i2) {
        render(str);
    }

    @Deprecated
    public void renderByUrl(String str, String str2, Map<String, Object> map, String str3, int i, int i2, WXRenderStrategy wXRenderStrategy) {
        renderByUrl(str, str2, map, str3, wXRenderStrategy);
    }

    public void renderByUrl(String str, String str2, Map<String, Object> map, String str3, WXRenderStrategy wXRenderStrategy) {
        renderByUrlInternal(str, str2, map, str3, wXRenderStrategy);
    }

    private String wrapPageName(String str, String str2) {
        if (!TextUtils.equals(str, "default")) {
            return str;
        }
        WXExceptionUtils.degradeUrl = str2;
        try {
            Uri parse = Uri.parse(str2);
            if (parse != null) {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme(parse.getScheme());
                builder.authority(parse.getAuthority());
                builder.path(parse.getPath());
                return builder.toString();
            }
        } catch (Exception unused) {
        }
        return str2;
    }

    private String assembleFilePath(Uri uri) {
        return (uri == null || uri.getPath() == null) ? "" : uri.getPath().replaceFirst("/", "");
    }

    public void reloadPage(boolean z) {
        WXSDKEngine.reload();
        if (!z) {
            IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
            if (wxConfigAdapter != null) {
                boolean parseBoolean = Boolean.parseBoolean(wxConfigAdapter.getConfig("android_weex_ext_config", "degrade_to_h5_if_not_reload", "true"));
                WXLogUtils.e("degrade : " + parseBoolean);
                if (parseBoolean) {
                    onJSException(String.valueOf(WXErrorCode.WX_ERR_RELOAD_PAGE.getErrorCode()), "Do not reloadPage", "Do not reloadPage degradeToH5");
                    WXLogUtils.e("Do not reloadPage degradeToH5");
                }
            }
        } else if (this.mContext != null) {
            Intent intent = new Intent();
            intent.setAction(ACTION_INSTANCE_RELOAD);
            intent.putExtra("url", this.mBundleUrl);
            this.mContext.sendBroadcast(intent);
        }
    }

    public void refreshInstance(Map<String, Object> map) {
        if (map != null) {
            refreshInstance(WXJsonUtils.fromObjectToJSONString(map));
        }
    }

    public void refreshInstance(String str) {
        if (str != null) {
            this.mRefreshStartTime = System.currentTimeMillis();
            if (this.mLastRefreshData != null) {
                this.mLastRefreshData.isDirty = true;
            }
            this.mLastRefreshData = new WXRefreshData(str, false);
            WXSDKManager.getInstance().refreshInstance(this.mInstanceId, this.mLastRefreshData);
        }
    }

    public WXRenderStrategy getRenderStrategy() {
        return this.mRenderStrategy;
    }

    public Context getUIContext() {
        return this.mContext;
    }

    public String getInstanceId() {
        return this.mInstanceId;
    }

    public Context getContext() {
        return this.mContext;
    }

    public int getWeexHeight() {
        if (this.mRenderContainer == null) {
            return 0;
        }
        return this.mRenderContainer.getHeight();
    }

    public int getWeexWidth() {
        if (this.mRenderContainer == null) {
            return 0;
        }
        return this.mRenderContainer.getWidth();
    }

    public IWXImgLoaderAdapter getImgLoaderAdapter() {
        return WXSDKManager.getInstance().getIWXImgLoaderAdapter();
    }

    public IDrawableLoader getDrawableLoader() {
        return WXSDKManager.getInstance().getDrawableLoader();
    }

    public URIAdapter getURIAdapter() {
        return WXSDKManager.getInstance().getURIAdapter();
    }

    public Uri rewriteUri(Uri uri, String str) {
        return getURIAdapter().rewrite(this, str, uri);
    }

    public IWXHttpAdapter getWXHttpAdapter() {
        return WXSDKManager.getInstance().getIWXHttpAdapter();
    }

    public IWXStatisticsListener getWXStatisticsListener() {
        return this.mStatisticsListener;
    }

    @Nullable
    public IWebSocketAdapter getWXWebSocketAdapter() {
        return WXSDKManager.getInstance().getIWXWebSocketAdapter();
    }

    @Deprecated
    public void reloadImages() {
        if (this.mScrollView != null) {
        }
    }

    public boolean isPreRenderMode() {
        return this.isPreRenderMode;
    }

    public void setPreRenderMode(final boolean z) {
        WXSDKManager.getInstance().getWXRenderManager().postOnUiThread((Runnable) new Runnable() {
            public void run() {
                boolean unused = WXSDKInstance.this.isPreRenderMode = z;
            }
        }, 0);
    }

    public void setContext(@NonNull Context context) {
        this.mContext = context;
    }

    public void registerRenderListener(IWXRenderListener iWXRenderListener) {
        this.mRenderListener = iWXRenderListener;
    }

    public void registerStatisticsListener(IWXStatisticsListener iWXStatisticsListener) {
        this.mStatisticsListener = iWXStatisticsListener;
    }

    public void setRenderStartTime(long j) {
        this.mRenderStartTime = j;
    }

    public void onActivityCreate() {
        WXModuleManager.onActivityCreate(getInstanceId());
        if (this.mRootComp != null) {
            this.mRootComp.onActivityCreate();
        } else if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.w("Warning :Component tree has not build completely,onActivityCreate can not be call!");
        }
        this.mGlobalEventReceiver = new WXGlobalEventReceiver(this);
        try {
            getContext().registerReceiver(this.mGlobalEventReceiver, new IntentFilter(WXGlobalEventReceiver.EVENT_ACTION));
        } catch (Throwable th) {
            WXLogUtils.e(th.getMessage());
            this.mGlobalEventReceiver = null;
        }
    }

    public void onActivityStart() {
        WXModuleManager.onActivityStart(getInstanceId());
        if (this.mRootComp != null) {
            this.mRootComp.onActivityStart();
        } else if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.w("Warning :Component tree has not build completely,onActivityStart can not be call!");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        WXModuleManager.onCreateOptionsMenu(getInstanceId(), menu);
        if (this.mRootComp != null) {
            this.mRootComp.onCreateOptionsMenu(menu);
            return true;
        } else if (!WXEnvironment.isApkDebugable()) {
            return true;
        } else {
            WXLogUtils.w("Warning :Component tree has not build completely,onActivityStart can not be call!");
            return true;
        }
    }

    public void onActivityPause() {
        onViewDisappear();
        if (!this.isCommit) {
            if (this.mUseScroller) {
                this.mWXPerformance.useScroller = 1;
            }
            this.mWXPerformance.maxDeepViewLayer = getMaxDeepLayer();
            this.mWXPerformance.wxDims = this.mwxDims;
            this.mWXPerformance.measureTimes = this.measureTimes;
            if (this.mUserTrackAdapter != null) {
                this.mUserTrackAdapter.commit(this.mContext, (String) null, "load", this.mWXPerformance, getUserTrackParams());
            }
            this.isCommit = true;
        }
        WXModuleManager.onActivityPause(getInstanceId());
        if (this.mRootComp != null) {
            this.mRootComp.onActivityPause();
        } else if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.w("Warning :Component tree has not build completely,onActivityPause can not be call!");
        }
        if (!this.mCurrentGround) {
            WXLogUtils.i("Application to be in the backround");
            Intent intent = new Intent(WXGlobalEventReceiver.EVENT_ACTION);
            intent.putExtra(WXGlobalEventReceiver.EVENT_NAME, Constants.Event.PAUSE_EVENT);
            intent.putExtra(WXGlobalEventReceiver.EVENT_WX_INSTANCEID, getInstanceId());
            if (this.mContext != null) {
                this.mContext.sendBroadcast(intent);
            } else {
                try {
                    WXEnvironment.getApplication().sendBroadcast(intent);
                } catch (Exception e) {
                    WXLogUtils.e("weex", (Throwable) e);
                }
            }
            this.mCurrentGround = true;
        }
        if ((WXEnvironment.isApkDebugable() || WXEnvironment.isPerf()) && this.mApmForInstance != null) {
            WXLogUtils.e("PerformanceData " + this.mApmForInstance.toPerfString());
        }
    }

    public void onActivityResume() {
        WXModuleManager.onActivityResume(getInstanceId());
        if (this.mRootComp != null) {
            this.mRootComp.onActivityResume();
        } else if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.w("Warning :Component tree has not build completely, onActivityResume can not be call!");
        }
        if (this.mCurrentGround) {
            WXLogUtils.i("Application  to be in the foreground");
            Intent intent = new Intent(WXGlobalEventReceiver.EVENT_ACTION);
            intent.putExtra(WXGlobalEventReceiver.EVENT_NAME, Constants.Event.RESUME_EVENT);
            intent.putExtra(WXGlobalEventReceiver.EVENT_WX_INSTANCEID, getInstanceId());
            if (this.mContext != null) {
                this.mContext.sendBroadcast(intent);
            } else {
                WXEnvironment.getApplication().sendBroadcast(intent);
            }
            this.mCurrentGround = false;
        }
        onViewAppear();
    }

    public void onActivityStop() {
        WXModuleManager.onActivityStop(getInstanceId());
        if (this.mRootComp != null) {
            this.mRootComp.onActivityStop();
        } else if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.w("Warning :Component tree has not build completely, onActivityStop can not be call!");
        }
    }

    public void onActivityDestroy() {
        WXModuleManager.onActivityDestroy(getInstanceId());
        if (this.mRootComp != null) {
            this.mRootComp.onActivityDestroy();
        } else if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.w("Warning :Component tree has not build completely, onActivityDestroy can not be call!");
        }
        this.mTimeCalculator.println();
        destroy();
    }

    public boolean onActivityBack() {
        WXModuleManager.onActivityBack(getInstanceId());
        if (this.mRootComp != null) {
            return this.mRootComp.onActivityBack();
        }
        if (!WXEnvironment.isApkDebugable()) {
            return false;
        }
        WXLogUtils.w("Warning :Component tree has not build completely, onActivityBack can not be call!");
        return false;
    }

    public boolean onSupportNavigateUp() {
        if (this.mWXActionbarHandlers == null) {
            return false;
        }
        for (ActionBarHandler onSupportNavigateUp : this.mWXActionbarHandlers) {
            if (onSupportNavigateUp.onSupportNavigateUp()) {
                return true;
            }
        }
        return false;
    }

    public boolean onBackPressed() {
        if (this.mWXBackPressedHandlers != null) {
            for (OnBackPressedHandler onBackPressed : this.mWXBackPressedHandlers) {
                if (onBackPressed.onBackPressed()) {
                    return true;
                }
            }
        }
        WXComponent rootComponent = getRootComponent();
        if (rootComponent == null) {
            return false;
        }
        WXEvent events = rootComponent.getEvents();
        if (events.contains(Constants.Event.NATIVE_BACK) && WXUtils.getBoolean(rootComponent.fireEventWait(Constants.Event.NATIVE_BACK, (Map<String, Object>) null).getResult(), false).booleanValue()) {
            return true;
        }
        boolean contains = events.contains(Constants.Event.CLICKBACKITEM);
        if (contains) {
            fireEvent(rootComponent.getRef(), Constants.Event.CLICKBACKITEM, (Map<String, Object>) null, (Map<String, Object>) null);
        }
        return contains;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002e A[LOOP:0: B:11:0x002e->B:14:0x003e, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r3, int r4, android.content.Intent r5) {
        /*
            r2 = this;
            java.lang.String r0 = r2.getInstanceId()
            com.taobao.weex.bridge.WXModuleManager.onActivityResult(r0, r3, r4, r5)
            com.taobao.weex.ui.component.WXComponent r0 = r2.mRootComp
            if (r0 == 0) goto L_0x0011
            com.taobao.weex.ui.component.WXComponent r0 = r2.mRootComp
            r0.onActivityResult(r3, r4, r5)
            goto L_0x001c
        L_0x0011:
            boolean r0 = com.taobao.weex.WXEnvironment.isApkDebugable()
            if (r0 == 0) goto L_0x001c
            java.lang.String r0 = "Warning :Component tree has not build completely, onActivityResult can not be call!"
            com.taobao.weex.utils.WXLogUtils.w(r0)
        L_0x001c:
            java.util.List<com.taobao.weex.WXSDKInstance$OnActivityResultHandler> r0 = r2.mWXOnActivityResultHandlers
            if (r0 == 0) goto L_0x0040
            java.util.List<com.taobao.weex.WXSDKInstance$OnActivityResultHandler> r0 = r2.mWXOnActivityResultHandlers
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0040
            java.util.List<com.taobao.weex.WXSDKInstance$OnActivityResultHandler> r0 = r2.mWXOnActivityResultHandlers
            java.util.Iterator r0 = r0.iterator()
        L_0x002e:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0040
            java.lang.Object r1 = r0.next()
            com.taobao.weex.WXSDKInstance$OnActivityResultHandler r1 = (com.taobao.weex.WXSDKInstance.OnActivityResultHandler) r1
            boolean r1 = r1.onActivityResult(r3, r4, r5)
            if (r1 == 0) goto L_0x002e
        L_0x0040:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXSDKInstance.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        WXModuleManager.onRequestPermissionsResult(getInstanceId(), i, strArr, iArr);
        if (this.mRootComp != null) {
            this.mRootComp.onRequestPermissionsResult(i, strArr, iArr);
        } else if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.w("Warning :Component tree has not build completely, onRequestPermissionsResult can not be call!");
        }
    }

    public void onViewDisappear() {
        this.isViewDisAppear = false;
        this.mApmForInstance.onDisAppear();
        WXComponent rootComponent = getRootComponent();
        if (rootComponent != null) {
            fireEvent(rootComponent.getRef(), Constants.Event.VIEWDISAPPEAR, (Map<String, Object>) null, (Map<String, Object>) null);
            for (OnInstanceVisibleListener onDisappear : this.mVisibleListeners) {
                onDisappear.onDisappear();
            }
        }
    }

    public boolean isViewDisAppear() {
        return this.isViewDisAppear;
    }

    public void onViewAppear() {
        this.isViewDisAppear = true;
        this.mApmForInstance.onAppear();
        WXComponent rootComponent = getRootComponent();
        if (rootComponent != null) {
            fireEvent(rootComponent.getRef(), Constants.Event.VIEWAPPEAR, (Map<String, Object>) null, (Map<String, Object>) null);
            for (OnInstanceVisibleListener onAppear : this.mVisibleListeners) {
                onAppear.onAppear();
            }
        }
    }

    public void onCreateFinish() {
        if (!this.mHasCreateFinish && this.mContext != null) {
            onViewAppear();
            WXAbstractRenderContainer wXAbstractRenderContainer = this.mRenderContainer;
            if (this.mRenderListener != null) {
                this.mRenderListener.onViewCreated(this, wXAbstractRenderContainer);
            }
            if (this.mStatisticsListener != null) {
                this.mStatisticsListener.onFirstView();
            }
        }
    }

    public void onUpdateFinish() {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("Instance onUpdateSuccess");
        }
    }

    public void runOnUiThread(Runnable runnable) {
        WXSDKManager.getInstance().postOnUiThread(runnable, 0);
    }

    public void onRenderSuccess(int i, int i2) {
        this.isRenderSuccess = true;
        if (!this.isNewFsEnd) {
            getApmForInstance().arriveNewFsRenderTime();
        }
        if (!getApmForInstance().stageMap.containsKey(WXInstanceApm.KEY_PAGE_STAGES_INTERACTION)) {
            getApmForInstance().arriveInteraction(getRootComponent());
        }
        long currentTimeMillis = System.currentTimeMillis() - this.mRenderStartTime;
        long[] renderFinishTime = WXBridgeManager.getInstance().getRenderFinishTime(getInstanceId());
        this.mWXPerformance.callBridgeTime = renderFinishTime[0];
        this.mWXPerformance.cssLayoutTime = renderFinishTime[1];
        this.mWXPerformance.parseJsonTime = renderFinishTime[2];
        this.mWXPerformance.totalTime = (double) currentTimeMillis;
        if (((double) this.mWXPerformance.screenRenderTime) < 0.001d) {
            this.mWXPerformance.screenRenderTime = currentTimeMillis;
        }
        if (!(this.mRenderListener == null || this.mContext == null)) {
            this.mRenderListener.onRenderSuccess(this, i, i2);
            if (this.mUserTrackAdapter != null) {
                WXPerformance wXPerformance = new WXPerformance(this.mInstanceId);
                wXPerformance.errCode = WXErrorCode.WX_SUCCESS.getErrorCode();
                wXPerformance.args = getBundleUrl();
                this.mUserTrackAdapter.commit(this.mContext, (String) null, IWXUserTrackAdapter.JS_BRIDGE, wXPerformance, getUserTrackParams());
            }
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d(WXLogUtils.WEEX_PERF_TAG, this.mWXPerformance.toString());
            }
        }
        if (WXEnvironment.isPerf()) {
            WXLogUtils.e(WXLogUtils.WEEX_PERF_TAG, this.mWXPerformance.getPerfData());
        }
    }

    public void onRefreshSuccess(int i, int i2) {
        if (this.mRenderListener != null && this.mContext != null) {
            this.mRenderListener.onRefreshSuccess(this, i, i2);
        }
    }

    public void onChangeElement(WXComponent wXComponent, boolean z) {
        if (!isDestroy() && this.mRenderContainer != null && this.mWXPerformance != null && wXComponent != null && !wXComponent.isIgnoreInteraction && !this.mRenderContainer.hasConsumeEvent()) {
            long currentTimeMillis = System.currentTimeMillis();
            if (!this.mHasCreateFinish || currentTimeMillis - this.mWXPerformance.renderTimeOrigin <= 8000) {
                if (wXComponent.mIsAddElementToTree) {
                    getWXPerformance().localInteractionViewAddCount++;
                    if (!z) {
                        getWXPerformance().interactionViewAddLimitCount++;
                    }
                    wXComponent.mIsAddElementToTree = false;
                }
                if (!z) {
                    this.mApmForInstance.arriveInteraction(wXComponent);
                }
            }
        }
    }

    public void onRenderError(final String str, final String str2) {
        this.hasError = true;
        WXStateRecord instance = WXStateRecord.getInstance();
        String instanceId = getInstanceId();
        instance.recordException(instanceId, "onRenderError," + str + "," + str2);
        if (this.mRenderListener != null && this.mContext != null) {
            WXLogUtils.e("onRenderError " + str + "," + str2);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (WXSDKInstance.this.mRenderListener != null && WXSDKInstance.this.mContext != null) {
                        WXSDKInstance.this.mRenderListener.onException(WXSDKInstance.this, str, str2);
                    }
                }
            });
        }
    }

    public void onJSException(final String str, final String str2, final String str3) {
        WXStateRecord instance = WXStateRecord.getInstance();
        String instanceId = getInstanceId();
        instance.recordException(instanceId, "onJSException," + str + "," + str2 + "|" + str3);
        this.hasException = true;
        if (this.mRenderListener != null && this.mContext != null) {
            WXLogUtils.e("onJSException " + str + "," + str3);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (WXSDKInstance.this.mRenderListener != null && WXSDKInstance.this.mContext != null) {
                        WXSDKInstance.this.mRenderListener.onException(WXSDKInstance.this, str, str2 + str3);
                    }
                }
            });
        }
    }

    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i != i5 || i2 != i6 || i3 != i7 || i4 != i8) {
            onLayoutChange(view);
        }
    }

    public void firstScreenCreateInstanceTime(long j) {
        if (this.mCreateInstance) {
            this.mWXPerformance.firstScreenJSFExecuteTime = j - this.mRenderStartTime;
            this.mCreateInstance = false;
        }
    }

    public void callJsTime(long j) {
        if (!this.mEnd) {
            this.mWXPerformance.fsCallJsTotalTime += j;
            this.mWXPerformance.fsCallJsTotalNum++;
        }
    }

    public void onComponentCreate(WXComponent wXComponent, long j) {
        this.mWXPerformance.mActionAddElementCount++;
        WXPerformance wXPerformance = this.mWXPerformance;
        wXPerformance.mActionAddElementSumTime = (int) (((long) wXPerformance.mActionAddElementSumTime) + j);
        if (!this.mEnd) {
            WXPerformance wXPerformance2 = this.mWXPerformance;
            wXPerformance2.fsComponentCreateTime = (int) (((long) wXPerformance2.fsComponentCreateTime) + j);
            this.mWXPerformance.fsComponentCount++;
        }
        this.mWXPerformance.componentCount++;
        this.mWXPerformance.componentCreateTime += j;
    }

    public void callActionAddElementTime(long j) {
        WXPerformance wXPerformance = this.mWXPerformance;
        wXPerformance.mActionAddElementSumTime = (int) (((long) wXPerformance.mActionAddElementSumTime) + j);
    }

    public void onOldFsRenderTimeLogic() {
        if (!this.mEnd) {
            this.mEnd = true;
            if (!(this.mStatisticsListener == null || this.mContext == null)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (WXSDKInstance.this.mStatisticsListener != null && WXSDKInstance.this.mContext != null) {
                            Trace.beginSection("onFirstScreen");
                            WXSDKInstance.this.mStatisticsListener.onFirstScreen();
                            Trace.endSection();
                        }
                    }
                });
            }
            this.mApmForInstance.arriveFSRenderTime();
            this.mWXPerformance.fsRenderTime = System.currentTimeMillis();
            this.mWXPerformance.screenRenderTime = System.currentTimeMillis() - this.mRenderStartTime;
        }
    }

    public WXSDKInstance getParentInstance() {
        return this.mParentInstance;
    }

    public void setParentInstance(WXSDKInstance wXSDKInstance) {
        this.mParentInstance = wXSDKInstance;
    }

    private void destroyView(View view) {
        try {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    destroyView(viewGroup.getChildAt(i));
                }
                viewGroup.removeViews(0, ((ViewGroup) view).getChildCount());
                WXReflectionUtils.setValue(view, "mChildrenCount", 0);
            }
            if (view instanceof Destroyable) {
                ((Destroyable) view).destroy();
            }
        } catch (Exception e) {
            WXLogUtils.e("WXSDKInstance destroyView Exception: ", (Throwable) e);
        }
    }

    public synchronized void destroy() {
        if (!isDestroy()) {
            if (this.mParentInstance != null) {
                this.mParentInstance = null;
            }
            this.mApmForInstance.onEnd();
            if (this.mRendered) {
                WXSDKManager.getInstance().destroyInstance(this.mInstanceId);
            }
            try {
                if (this.mGlobalEventReceiver != null) {
                    getContext().unregisterReceiver(this.mGlobalEventReceiver);
                    this.mGlobalEventReceiver = null;
                }
            } catch (IllegalArgumentException e) {
                WXLogUtils.w(WXLogUtils.getStackTrace(e));
            }
            if (this.mRootComp != null) {
                this.mRootComp.destroy();
                this.mRootComp = null;
            }
            if (this.mRenderContainer != null) {
                destroyView(this.mRenderContainer);
            }
            if (this.mGlobalEvents != null) {
                this.mGlobalEvents.clear();
            }
            if (this.mComponentObserver != null) {
                this.mComponentObserver = null;
            }
            if (this.mLayerOverFlowListeners != null) {
                this.mLayerOverFlowListeners.clear();
            }
            if (this.mWXOnActivityResultHandlers != null && !this.mWXOnActivityResultHandlers.isEmpty()) {
                this.mWXOnActivityResultHandlers.clear();
            }
            if (this.mWXBackPressedHandlers != null && !this.mWXBackPressedHandlers.isEmpty()) {
                this.mWXBackPressedHandlers.clear();
            }
            if (this.mWXActionbarHandlers != null && !this.mWXActionbarHandlers.isEmpty()) {
                this.mWXActionbarHandlers.clear();
            }
            getFlatUIContext().destroy();
            this.mFlatGUIContext = null;
            this.mInstanceOnFireEventInterceptorList = null;
            this.mWXScrollListeners = null;
            this.mWXActionbarHandlers = null;
            this.mWXBackPressedHandlers = null;
            this.mRenderContainer = null;
            this.mNestedInstanceInterceptor = null;
            this.mUserTrackAdapter = null;
            this.mScrollView = null;
            this.mContext = null;
            this.mRenderListener = null;
            this.isDestroy = true;
            this.mStatisticsListener = null;
            if (this.responseHeaders != null) {
                this.responseHeaders.clear();
            }
            if (this.templateRef != null) {
                this.templateRef = null;
            }
            if (this.mContentBoxMeasurements != null) {
                this.mContentBoxMeasurements.clear();
            }
            if (!this.mModuleInterceptMap.isEmpty()) {
                this.mModuleInterceptMap.clear();
            }
            this.mWXPerformance.afterInstanceDestroy(this.mInstanceId);
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    WXBridgeManager.getInstance().onInstanceClose(WXSDKInstance.this.getInstanceId());
                    WXSDKInstance.this.inactiveAddElementAction.clear();
                }
            });
            WXBridgeManager.getInstance().postDelay(new Runnable() {
                public void run() {
                    WXSDKManager.getInstance().getAllInstanceMap().remove(WXSDKInstance.this.mInstanceId);
                }
            }, 1000);
        }
    }

    public boolean isDestroy() {
        return this.isDestroy;
    }

    @Nullable
    public String getBundleUrl() {
        return this.mBundleUrl;
    }

    public View getRootView() {
        if (this.mRootComp == null) {
            return null;
        }
        return this.mRootComp.getRealView();
    }

    public View getContainerView() {
        return this.mRenderContainer;
    }

    @Deprecated
    public void setBundleUrl(String str) {
        this.mBundleUrl = str;
        if (WXSDKManager.getInstance().getValidateProcessor() != null) {
            this.mNeedValidate = WXSDKManager.getInstance().getValidateProcessor().needValidate(this.mBundleUrl);
        }
    }

    public void onRootCreated(WXComponent wXComponent) {
        this.mRootComp = wXComponent;
        this.mRootComp.mDeepInComponentTree = 1;
        this.mRenderContainer.addView(wXComponent.getHostView());
        setSize(this.mRenderContainer.getWidth(), this.mRenderContainer.getHeight());
    }

    public void moveFixedView(View view) {
        if (this.mRenderContainer != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup == null) {
                this.mRenderContainer.addView(view);
            } else if (viewGroup != this.mRenderContainer) {
                viewGroup.removeView(view);
                this.mRenderContainer.addView(view);
            }
        }
    }

    public void removeFixedView(View view) {
        if (this.mRenderContainer != null) {
            this.mRenderContainer.removeView(view);
        }
    }

    public int getRenderContainerPaddingLeft() {
        if (this.mRenderContainer != null) {
            return this.mRenderContainer.getPaddingLeft();
        }
        return 0;
    }

    public int getRenderContainerPaddingRight() {
        if (this.mRenderContainer != null) {
            return this.mRenderContainer.getPaddingRight();
        }
        return 0;
    }

    public int getRenderContainerPaddingTop() {
        if (this.mRenderContainer != null) {
            return this.mRenderContainer.getPaddingTop();
        }
        return 0;
    }

    public synchronized List<OnWXScrollListener> getWXScrollListeners() {
        return this.mWXScrollListeners;
    }

    public synchronized void registerOnWXScrollListener(OnWXScrollListener onWXScrollListener) {
        if (this.mWXScrollListeners == null) {
            this.mWXScrollListeners = new ArrayList();
        }
        this.mWXScrollListeners.add(onWXScrollListener);
    }

    public synchronized void registerActionbarHandler(ActionBarHandler actionBarHandler) {
        if (actionBarHandler != null) {
            if (this.mWXActionbarHandlers == null) {
                this.mWXActionbarHandlers = new ArrayList();
            }
            this.mWXActionbarHandlers.add(actionBarHandler);
        }
    }

    public synchronized void unRegisterActionbarHandler(ActionBarHandler actionBarHandler) {
        if (!(this.mWXActionbarHandlers == null || actionBarHandler == null)) {
            this.mWXActionbarHandlers.remove(actionBarHandler);
        }
    }

    public synchronized void unRegisterOnActivityResultHandler(OnActivityResultHandler onActivityResultHandler) {
        if (!(this.mWXOnActivityResultHandlers == null || onActivityResultHandler == null)) {
            this.mWXOnActivityResultHandlers.remove(onActivityResultHandler);
        }
    }

    public synchronized void registerOnActivityResultHandler(OnActivityResultHandler onActivityResultHandler) {
        if (onActivityResultHandler != null) {
            if (this.mWXOnActivityResultHandlers == null) {
                this.mWXOnActivityResultHandlers = new ArrayList();
            }
            this.mWXOnActivityResultHandlers.add(onActivityResultHandler);
        }
    }

    public synchronized void registerBackPressedHandler(OnBackPressedHandler onBackPressedHandler) {
        if (onBackPressedHandler != null) {
            if (this.mWXBackPressedHandlers == null) {
                this.mWXBackPressedHandlers = new ArrayList();
            }
            this.mWXBackPressedHandlers.add(onBackPressedHandler);
        }
    }

    public synchronized void unRegisterBackPressedHandler(OnBackPressedHandler onBackPressedHandler) {
        if (!(this.mWXBackPressedHandlers == null || onBackPressedHandler == null)) {
            this.mWXBackPressedHandlers.remove(onBackPressedHandler);
        }
    }

    public void setSize(int i, int i2) {
        if (i > 0) {
            if (((i2 > 0) && (!this.isDestroy)) && this.mRendered && this.mRenderContainer != null) {
                if (sScreenHeight < 0) {
                    sScreenHeight = WXViewUtils.getScreenHeight(getContext());
                }
                if (sScreenHeight > 0) {
                    double d = (double) i2;
                    double d2 = (double) sScreenHeight;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    double d3 = (d / d2) * 100.0d;
                    if (d3 > 100.0d) {
                        d3 = 100.0d;
                    }
                    getApmForInstance().addStats(WXInstanceApm.KEY_PAGE_STATS_BODY_RATIO, d3);
                }
                ViewGroup.LayoutParams layoutParams = this.mRenderContainer.getLayoutParams();
                if (layoutParams != null) {
                    final float f = (float) i;
                    final float f2 = (float) i2;
                    if (!(this.mRenderContainer.getWidth() == i && this.mRenderContainer.getHeight() == i2)) {
                        layoutParams.width = i;
                        layoutParams.height = i2;
                        this.mRenderContainer.setLayoutParams(layoutParams);
                    }
                    if (this.mRootComp != null && layoutParams != null) {
                        final boolean z = layoutParams.width == -2;
                        final boolean z2 = layoutParams.height == -2;
                        WXBridgeManager.getInstance().post(new Runnable() {
                            public void run() {
                                WXBridgeManager.getInstance().setDefaultRootSize(WXSDKInstance.this.getInstanceId(), f, f2, z, z2);
                            }
                        });
                    }
                }
            }
        }
    }

    public void fireGlobalEventCallback(String str, Map<String, Object> map) {
        List<String> list = this.mGlobalEvents.get(str);
        if (list != null) {
            for (String callback : list) {
                WXSDKManager.getInstance().callback(this.mInstanceId, callback, map, true);
            }
        }
    }

    public void fireEvent(String str, String str2, Map<String, Object> map, Map<String, Object> map2, List<Object> list) {
        fireEvent(str, str2, map, map2, list, (EventResult) null);
    }

    public void fireEvent(String str, String str2, Map<String, Object> map, Map<String, Object> map2, List<Object> list, EventResult eventResult) {
        onInterceptInstanceEvent(getInstanceId(), str, str2, map, map2);
        if (this.mWXPerformance != null && this.mWXPerformance.fsCallEventTotalNum < Integer.MAX_VALUE) {
            this.mWXPerformance.fsCallEventTotalNum++;
        }
        this.mApmForInstance.updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_CALL_EVENT_NUM, 1.0d);
        WXBridgeManager.getInstance().fireEventOnNode(getInstanceId(), str, str2, map, map2, list, eventResult);
    }

    public void fireEvent(String str, String str2, Map<String, Object> map, Map<String, Object> map2) {
        fireEvent(str, str2, map, map2, (List<Object>) null);
    }

    public void fireEvent(String str, String str2, Map<String, Object> map) {
        fireEvent(str, str2, map, (Map<String, Object>) null);
    }

    public void fireEvent(String str, String str2) {
        fireEvent(str, str2, new HashMap());
    }

    /* access modifiers changed from: protected */
    public void addEventListener(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            List list = this.mGlobalEvents.get(str);
            if (list == null) {
                list = new ArrayList();
                this.mGlobalEvents.put(str, list);
            }
            list.add(str2);
        }
    }

    /* access modifiers changed from: protected */
    public void removeEventListener(String str, String str2) {
        List list;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && (list = this.mGlobalEvents.get(str)) != null) {
            list.remove(str2);
        }
    }

    /* access modifiers changed from: protected */
    public void removeEventListener(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mGlobalEvents.remove(str);
        }
    }

    public void fireModuleEvent(String str, WXModule wXModule, Map<String, Object> map) {
        if (!TextUtils.isEmpty(str) && wXModule != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("type", str);
            hashMap.put("module", wXModule.getModuleName());
            hashMap.put("data", map);
            List<String> eventCallbacks = wXModule.getEventCallbacks(str);
            if (eventCallbacks != null) {
                for (String next : eventCallbacks) {
                    SimpleJSCallback simpleJSCallback = new SimpleJSCallback(this.mInstanceId, next);
                    if (wXModule.isOnce(next)) {
                        simpleJSCallback.invoke(hashMap);
                    } else {
                        simpleJSCallback.invokeAndKeepAlive(hashMap);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r1 = r2.getEventCallbacks(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkModuleEventRegistered(java.lang.String r1, com.taobao.weex.common.WXModule r2) {
        /*
            r0 = this;
            if (r2 == 0) goto L_0x0010
            java.util.List r1 = r2.getEventCallbacks(r1)
            if (r1 == 0) goto L_0x0010
            int r1 = r1.size()
            if (r1 <= 0) goto L_0x0010
            r1 = 1
            return r1
        L_0x0010:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXSDKInstance.checkModuleEventRegistered(java.lang.String, com.taobao.weex.common.WXModule):boolean");
    }

    public WXPerformance getWXPerformance() {
        return this.mWXPerformance;
    }

    public WXInstanceApm getApmForInstance() {
        return this.mApmForInstance;
    }

    public Map<String, Serializable> getUserTrackParams() {
        return this.mUserTrackParams;
    }

    public void addUserTrackParameter(String str, Serializable serializable) {
        if (this.mUserTrackParams == null) {
            this.mUserTrackParams = new ConcurrentHashMap();
        }
        this.mUserTrackParams.put(str, serializable);
    }

    public void clearUserTrackParameters() {
        if (this.mUserTrackParams != null) {
            this.mUserTrackParams.clear();
        }
    }

    public void removeUserTrackParameter(String str) {
        if (this.mUserTrackParams != null) {
            this.mUserTrackParams.remove(str);
        }
    }

    public int getMaxDeepLayer() {
        return this.mMaxDeepLayer;
    }

    public void setMaxDeepLayer(int i) {
        this.mMaxDeepLayer = i;
        this.mApmForInstance.updateMaxStats(WXInstanceApm.KEY_PAGE_STATS_MAX_DEEP_VIEW, (double) i);
    }

    public void setMaxDomDeep(int i) {
        this.mApmForInstance.updateMaxStats(WXInstanceApm.KEY_PAGE_STATS_MAX_DEEP_DOM, (double) i);
        if (this.mWXPerformance != null && this.mWXPerformance.maxDeepVDomLayer <= i) {
            this.mWXPerformance.maxDeepVDomLayer = i;
        }
    }

    public void onHttpStart() {
        if (!this.mEnd) {
            this.mWXPerformance.fsRequestNum++;
        }
    }

    public String getTemplateInfo() {
        String template = getTemplate();
        if (template == null) {
            return " template md5 null ,httpHeader:" + JSONObject.toJSONString(this.responseHeaders);
        } else if (TextUtils.isEmpty(template)) {
            return " template md5  length 0 ,httpHeader" + JSONObject.toJSONString(this.responseHeaders);
        } else {
            try {
                byte[] bytes = template.getBytes("UTF-8");
                String md5 = WXFileUtils.md5(bytes);
                String base64Md5 = WXFileUtils.base64Md5(bytes);
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                arrayList.add(md5);
                arrayList2.add(base64Md5);
                this.responseHeaders.put("templateSourceMD5", arrayList);
                this.responseHeaders.put(SOURCE_TEMPLATE_BASE64_MD5, arrayList2);
                return " template md5 " + md5 + " length " + bytes.length + " base64 md5 " + base64Md5 + " response header " + JSONObject.toJSONString(this.responseHeaders);
            } catch (Exception unused) {
                return "template md5 getBytes error";
            }
        }
    }

    public boolean isContentMd5Match() {
        if (this.responseHeaders == null) {
            return true;
        }
        List list = this.responseHeaders.get("Content-Md5");
        if (list == null) {
            list = this.responseHeaders.get("content-md5");
        }
        if (list == null || list.size() <= 0) {
            return true;
        }
        String str = (String) list.get(0);
        List list2 = this.responseHeaders.get(SOURCE_TEMPLATE_BASE64_MD5);
        if (list2 == null) {
            getTemplateInfo();
            list2 = this.responseHeaders.get(SOURCE_TEMPLATE_BASE64_MD5);
        }
        if (list2 == null || list2.size() == 0) {
            return true;
        }
        return str.equals(list2.get(0));
    }

    public String getTemplate() {
        if (this.templateRef == null) {
            return null;
        }
        return (String) this.templateRef.get();
    }

    public void setTemplate(String str) {
        this.templateRef = new WeakReference<>(str);
    }

    public void OnVSync() {
        if (this.mFixMultiThreadBug) {
            if (this.mPageDirty) {
                WXBridgeManager.getInstance().post(new Runnable() {
                    public void run() {
                        if (WXBridgeManager.getInstance().notifyLayout(WXSDKInstance.this.getInstanceId())) {
                            WXBridgeManager.getInstance().forceLayout(WXSDKInstance.this.getInstanceId());
                        }
                    }
                });
            }
        } else if (WXBridgeManager.getInstance().notifyLayout(getInstanceId())) {
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    WXBridgeManager.getInstance().forceLayout(WXSDKInstance.this.getInstanceId());
                }
            });
        }
    }

    public void addContentBoxMeasurement(long j, ContentBoxMeasurement contentBoxMeasurement) {
        this.mContentBoxMeasurements.put(Long.valueOf(j), contentBoxMeasurement);
    }

    public ContentBoxMeasurement getContentBoxMeasurement(long j) {
        return this.mContentBoxMeasurements.get(Long.valueOf(j));
    }

    private void onInterceptInstanceEvent(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        if (this.mInstanceOnFireEventInterceptorList != null) {
            for (InstanceOnFireEventInterceptor onInterceptFireEvent : this.mInstanceOnFireEventInterceptorList) {
                onInterceptFireEvent.onInterceptFireEvent(str, str2, str3, map, map2);
            }
        }
    }

    public List<InstanceOnFireEventInterceptor> getInstanceOnFireEventInterceptorList() {
        if (this.mInstanceOnFireEventInterceptorList == null) {
            this.mInstanceOnFireEventInterceptorList = new ArrayList();
        }
        return this.mInstanceOnFireEventInterceptorList;
    }

    public void addInstanceOnFireEventInterceptor(InstanceOnFireEventInterceptor instanceOnFireEventInterceptor) {
        if (instanceOnFireEventInterceptor != null && !getInstanceOnFireEventInterceptorList().contains(instanceOnFireEventInterceptor)) {
            getInstanceOnFireEventInterceptorList().add(instanceOnFireEventInterceptor);
        }
    }

    public String getRenderType() {
        return this.mRenderType;
    }

    public void setRenderType(String str) {
        this.mRenderType = str;
    }

    private static boolean isDisableSkipFrameworkInDataRender() {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter == null) {
            return false;
        }
        return "true".equals(wxConfigAdapter.getConfig("wxeagle", "disable_skip_framework_init", "false"));
    }

    public boolean isPageDirty() {
        return this.mPageDirty;
    }

    public void setPageDirty(boolean z) {
        this.mPageDirty = z;
    }

    public WXEaglePlugin getEaglePlugin() {
        return this.mEaglePlugin;
    }

    public String getEaglePluginName() {
        return this.mEaglePluginName;
    }

    public boolean isUsingEaglePlugin() {
        return this.mEaglePlugin != null;
    }

    public ModuleInterceptResult moduleIntercept(String str, String str2, JSONArray jSONArray, JSONObject jSONObject) {
        ModuleIntercept moduleIntercept = this.mModuleInterceptMap.get(str);
        if (moduleIntercept == null || moduleIntercept.callback == null) {
            return null;
        }
        return moduleIntercept.callback.CallModuleMethod(str, str2, jSONArray, jSONObject);
    }

    public boolean hasModuleIntercept(String str) {
        return this.mModuleInterceptMap.containsKey(str);
    }

    public void registerModuleIntercept(String str, ModuleInterceptCallback moduleInterceptCallback) {
        if (!TextUtils.isEmpty(str) && moduleInterceptCallback != null) {
            this.mModuleInterceptMap.put(str, new ModuleIntercept(str, moduleInterceptCallback));
        }
    }

    public void unRegisterModuleIntercept(String str) {
        this.mModuleInterceptMap.remove(str);
    }

    public static class ModuleInterceptResult {
        public boolean mIntercepted = false;
        public Object mResult = null;

        public ModuleInterceptResult(boolean z, Object obj) {
            this.mIntercepted = z;
            this.mResult = obj;
        }
    }

    public WXProcessNotify getWxProcessNotify() {
        return this.mWxProcessNotify;
    }

    public void registerProcessNotify(WXProcessNotify wXProcessNotify) {
        this.mWxProcessNotify = wXProcessNotify;
    }

    public void unRegisterProcessNotify() {
        this.mWxProcessNotify = null;
    }

    private static class ModuleIntercept {
        public ModuleInterceptCallback callback;
        public String moduleName;

        public ModuleIntercept(String str, ModuleInterceptCallback moduleInterceptCallback) {
            this.moduleName = str;
            this.callback = moduleInterceptCallback;
        }
    }
}
