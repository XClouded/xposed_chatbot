package com.taobao.weex.bridge;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.UiThread;
import androidx.collection.ArrayMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.Script;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.adapter.IWXJscProcessManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.bridge.WXValidateProcessor;
import com.taobao.weex.common.IWXBridge;
import com.taobao.weex.common.IWXDebugConfig;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXJSExceptionInfo;
import com.taobao.weex.common.WXRefreshData;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.jsEngine.JSEngine;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.ui.WXComponentRegistry;
import com.taobao.weex.ui.WXRenderManager;
import com.taobao.weex.ui.action.ActionReloadPage;
import com.taobao.weex.ui.action.GraphicActionAddChildToRichtext;
import com.taobao.weex.ui.action.GraphicActionAddElement;
import com.taobao.weex.ui.action.GraphicActionAddEvent;
import com.taobao.weex.ui.action.GraphicActionAppendTreeCreateFinish;
import com.taobao.weex.ui.action.GraphicActionCreateBody;
import com.taobao.weex.ui.action.GraphicActionCreateFinish;
import com.taobao.weex.ui.action.GraphicActionLayout;
import com.taobao.weex.ui.action.GraphicActionMoveElement;
import com.taobao.weex.ui.action.GraphicActionRefreshFinish;
import com.taobao.weex.ui.action.GraphicActionRemoveChildFromRichtext;
import com.taobao.weex.ui.action.GraphicActionRemoveElement;
import com.taobao.weex.ui.action.GraphicActionRemoveEvent;
import com.taobao.weex.ui.action.GraphicActionRenderSuccess;
import com.taobao.weex.ui.action.GraphicActionUpdateAttr;
import com.taobao.weex.ui.action.GraphicActionUpdateRichtextAttr;
import com.taobao.weex.ui.action.GraphicActionUpdateRichtextStyle;
import com.taobao.weex.ui.action.GraphicActionUpdateStyle;
import com.taobao.weex.ui.action.GraphicPosition;
import com.taobao.weex.ui.action.GraphicSize;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.module.WXDomModule;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXFileUtils;
import com.taobao.weex.utils.WXJsonUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXWsonJSONSwitch;
import com.taobao.weex.utils.batch.BactchExecutor;
import com.taobao.weex.utils.batch.Interceptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class WXBridgeManager implements Handler.Callback, BactchExecutor {
    public static final String ARGS = "args";
    private static final boolean BRIDGE_LOG_SWITCH = false;
    private static final String BUNDLE_TYPE = "bundleType";
    public static final String COMPONENT = "component";
    private static final int CRASHREINIT = 50;
    private static String GLOBAL_CONFIG_KEY = "global_switch_config";
    public static final String INITLOGFILE = "/jsserver_start.log";
    private static final int INIT_FRAMEWORK_OK = 1;
    public static final String KEY_ARGS = "args";
    public static final String KEY_METHOD = "method";
    public static final String KEY_PARAMS = "params";
    private static long LOW_MEM_VALUE = 120;
    public static final String METHD_COMPONENT_HOOK_SYNC = "componentHook";
    public static final String METHD_FIRE_EVENT_SYNC = "fireEventSync";
    public static final String METHOD = "method";
    public static final String METHOD_CALLBACK = "callback";
    public static final String METHOD_CALL_JS = "callJS";
    public static final String METHOD_CREATE_INSTANCE = "createInstance";
    public static final String METHOD_CREATE_INSTANCE_CONTEXT = "createInstanceContext";
    public static final String METHOD_CREATE_PAGE_WITH_CONTENT = "CreatePageWithContent";
    public static final String METHOD_DESTROY_INSTANCE = "destroyInstance";
    public static final String METHOD_FIRE_EVENT = "fireEvent";
    private static final String METHOD_JSFM_NOT_INIT_IN_EAGLE_MODE = "JsfmNotInitInEagleMode";
    public static final String METHOD_NOTIFY_SERIALIZE_CODE_CACHE = "notifySerializeCodeCache";
    public static final String METHOD_NOTIFY_TRIM_MEMORY = "notifyTrimMemory";
    private static final String METHOD_POST_TASK_TO_MSG_LOOP = "PostTaskToMsgLoop";
    public static final String METHOD_REFRESH_INSTANCE = "refreshInstance";
    public static final String METHOD_REGISTER_COMPONENTS = "registerComponents";
    public static final String METHOD_REGISTER_MODULES = "registerModules";
    public static final String METHOD_SET_TIMEOUT = "setTimeoutCallback";
    public static final String METHOD_UPDATE_COMPONENT_WITH_DATA = "UpdateComponentData";
    public static final String MODULE = "module";
    private static final String NON_CALLBACK = "-1";
    public static final String OPTIONS = "options";
    public static final String REF = "ref";
    private static final String RENDER_STRATEGY = "renderStrategy";
    private static final String UNDEFINED = "undefined";
    private static Class clazz_debugProxy = null;
    private static String crashUrl = null;
    /* access modifiers changed from: private */
    public static String globalConfig = "none";
    private static volatile boolean isJsEngineMultiThreadEnable = false;
    /* access modifiers changed from: private */
    public static volatile boolean isSandBoxContext = true;
    private static boolean isUseSingleProcess = false;
    private static long lastCrashTime = 0;
    static volatile WXBridgeManager mBridgeManager = null;
    private static volatile boolean mInit = false;
    private static String mRaxApi = null;
    private static Map<String, String> mWeexCoreEnvOptions = new HashMap();
    public static volatile int reInitCount = 1;
    private static volatile int sInitFrameWorkCount;
    public static StringBuilder sInitFrameWorkMsg = new StringBuilder();
    public static long sInitFrameWorkTimeOrigin;
    private HashSet<String> mDestroyedInstanceId = new HashSet<>();
    private WXParams mInitParams;
    private Interceptor mInterceptor;
    Handler mJSHandler;
    private WXThread mJSThread;
    private StringBuilder mLodBuilder = new StringBuilder(50);
    private boolean mMock = false;
    /* access modifiers changed from: private */
    public WXHashMap<String, ArrayList<WXHashMap<String, Object>>> mNextTickTasks = new WXHashMap<>();
    /* access modifiers changed from: private */
    public List<Map<String, Object>> mRegisterComponentFailList = new ArrayList(8);
    /* access modifiers changed from: private */
    public List<Map<String, Object>> mRegisterModuleFailList = new ArrayList(8);
    /* access modifiers changed from: private */
    public List<String> mRegisterServiceFailList = new ArrayList(8);
    /* access modifiers changed from: private */
    public IWXBridge mWXBridge;
    private Object mWxDebugProxy;

    public enum BundType {
        Vue,
        Rax,
        Others
    }

    public static class TimerInfo {
        public String callbackId;
        public String instanceId;
        public long time;
    }

    private void mock(String str) {
    }

    @Deprecated
    public void notifyTrimMemory() {
    }

    private WXBridgeManager() {
        initWXBridge(WXEnvironment.sRemoteDebugMode);
        this.mJSThread = new WXThread("WeexJSBridgeThread", (Handler.Callback) this);
        this.mJSHandler = this.mJSThread.getHandler();
    }

    public static WXBridgeManager getInstance() {
        if (mBridgeManager == null) {
            synchronized (WXBridgeManager.class) {
                if (mBridgeManager == null) {
                    mBridgeManager = new WXBridgeManager();
                }
            }
        }
        return mBridgeManager;
    }

    public void setUseSingleProcess(boolean z) {
        if (z != isUseSingleProcess) {
            isUseSingleProcess = z;
        }
    }

    public void onInteractionTimeUpdate(final String str) {
        post(new Runnable() {
            public void run() {
                if (WXBridgeManager.this.mWXBridge instanceof WXBridge) {
                    ((WXBridge) WXBridgeManager.this.mWXBridge).nativeOnInteractionTimeUpdate(str);
                }
            }
        });
    }

    public boolean jsEngineMultiThreadEnable() {
        return isJsEngineMultiThreadEnable;
    }

    public void checkJsEngineMultiThread() {
        IWXJscProcessManager wXJscProcessManager = WXSDKManager.getInstance().getWXJscProcessManager();
        boolean enableBackupThread = wXJscProcessManager != null ? wXJscProcessManager.enableBackupThread() : false;
        if (enableBackupThread != isJsEngineMultiThreadEnable) {
            isJsEngineMultiThreadEnable = enableBackupThread;
            if (!isJSFrameworkInit()) {
                return;
            }
            if (isJSThread()) {
                WXSDKEngine.reload();
            } else {
                post(new Runnable() {
                    public void run() {
                        WXSDKEngine.reload();
                    }
                });
            }
        }
    }

    public void setSandBoxContext(boolean z) {
        String str;
        if (z != isSandBoxContext) {
            isSandBoxContext = z;
            if (isJSThread()) {
                setJSFrameworkInit(false);
                WXModuleManager.resetAllModuleState();
                if (!isSandBoxContext) {
                    str = WXFileUtils.loadAsset("main.js", WXEnvironment.getApplication());
                } else {
                    str = WXFileUtils.loadAsset("weex-main-jsfm.js", WXEnvironment.getApplication());
                }
                initFramework(str);
                WXServiceManager.reload();
                WXModuleManager.reload();
                WXComponentRegistry.reload();
                return;
            }
            post(new Runnable() {
                public void run() {
                    String str;
                    WXBridgeManager.this.setJSFrameworkInit(false);
                    WXModuleManager.resetAllModuleState();
                    if (!WXBridgeManager.isSandBoxContext) {
                        str = WXFileUtils.loadAsset("main.js", WXEnvironment.getApplication());
                    } else {
                        str = WXFileUtils.loadAsset("weex-main-jsfm.js", WXEnvironment.getApplication());
                    }
                    WXBridgeManager.this.initFramework(str);
                    WXServiceManager.reload();
                    WXModuleManager.reload();
                    WXComponentRegistry.reload();
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public boolean isJSFrameworkInit() {
        return mInit;
    }

    /* access modifiers changed from: private */
    public void setJSFrameworkInit(boolean z) {
        mInit = z;
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recoreJsfmInitHistory("setJsfmInitFlag:" + z);
        if (z) {
            onJsFrameWorkInitSuccees();
            JSEngine.getInstance().engineInitFinished();
        }
    }

    private void initWXBridge(boolean z) {
        Method method;
        Constructor constructor;
        Method method2;
        if (z && WXEnvironment.isApkDebugable()) {
            WXEnvironment.sDebugServerConnectable = true;
        }
        if (WXEnvironment.sDebugServerConnectable) {
            WXEnvironment.isApkDebugable();
            if (WXEnvironment.getApplication() != null) {
                try {
                    if (clazz_debugProxy == null) {
                        clazz_debugProxy = Class.forName("com.taobao.weex.devtools.debug.DebugServerProxy");
                    }
                    if (!(clazz_debugProxy == null || (constructor = clazz_debugProxy.getConstructor(new Class[]{Context.class, IWXDebugConfig.class})) == null)) {
                        this.mWxDebugProxy = constructor.newInstance(new Object[]{WXEnvironment.getApplication(), new IWXDebugConfig() {
                            public WXBridgeManager getWXJSManager() {
                                return WXBridgeManager.this;
                            }

                            public WXDebugJsBridge getWXDebugJsBridge() {
                                return new WXDebugJsBridge();
                            }
                        }});
                        if (!(this.mWxDebugProxy == null || (method2 = clazz_debugProxy.getMethod("start", new Class[0])) == null)) {
                            method2.invoke(this.mWxDebugProxy, new Object[0]);
                        }
                    }
                } catch (Throwable unused) {
                }
                WXServiceManager.execAllCacheJsService();
            } else {
                WXLogUtils.e("WXBridgeManager", "WXEnvironment.sApplication is null, skip init Inspector");
            }
        }
        if (!z || this.mWxDebugProxy == null) {
            this.mWXBridge = new WXBridge();
            return;
        }
        try {
            if (clazz_debugProxy == null) {
                clazz_debugProxy = Class.forName("com.taobao.weex.devtools.debug.DebugServerProxy");
            }
            if (clazz_debugProxy != null && (method = clazz_debugProxy.getMethod("getWXBridge", new Class[0])) != null) {
                this.mWXBridge = (IWXBridge) method.invoke(this.mWxDebugProxy, new Object[0]);
            }
        } catch (Throwable unused2) {
        }
    }

    public String dumpIpcPageInfo() {
        return this.mWXBridge instanceof WXBridge ? ((WXBridge) this.mWXBridge).nativeDumpIpcPageQueueInfo() : "";
    }

    public boolean isRebootExceedLimit() {
        return reInitCount > 50;
    }

    public void stopRemoteDebug() {
        Method method;
        if (this.mWxDebugProxy != null) {
            try {
                if (clazz_debugProxy == null) {
                    clazz_debugProxy = Class.forName("com.taobao.weex.devtools.debug.DebugServerProxy");
                }
                if (clazz_debugProxy != null && (method = clazz_debugProxy.getMethod("stop", new Class[]{Boolean.TYPE})) != null) {
                    method.invoke(this.mWxDebugProxy, new Object[]{true});
                }
            } catch (Throwable unused) {
            }
        }
    }

    public Object callModuleMethod(String str, String str2, String str3, JSONArray jSONArray) {
        return callModuleMethod(str, str2, str3, jSONArray, (JSONObject) null);
    }

    public Object callModuleMethod(String str, String str2, String str3, JSONArray jSONArray, JSONObject jSONObject) {
        WXSDKInstance.ModuleInterceptResult moduleIntercept;
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return null;
        }
        if (sDKInstance.hasModuleIntercept(str2) && (moduleIntercept = sDKInstance.moduleIntercept(str2, str3, jSONArray, jSONObject)) != null && moduleIntercept.mIntercepted) {
            return moduleIntercept.mResult;
        }
        if (!sDKInstance.isNeedValidate() || WXSDKManager.getInstance().getValidateProcessor() == null) {
            try {
                return WXModuleManager.callModuleMethod(str, str2, str3, jSONArray);
            } catch (NumberFormatException unused) {
                ArrayMap arrayMap = new ArrayMap();
                arrayMap.put("moduleName", str2);
                arrayMap.put("methodName", str3);
                arrayMap.put("args", jSONArray.toJSONString());
                WXLogUtils.e("[WXBridgeManager] callNative : numberFormatException when parsing string to numbers in args", arrayMap.toString());
                return null;
            }
        } else {
            WXValidateProcessor.WXModuleValidateResult onModuleValidate = WXSDKManager.getInstance().getValidateProcessor().onModuleValidate(sDKInstance, str2, str3, jSONArray, jSONObject);
            if (onModuleValidate == null) {
                return null;
            }
            if (onModuleValidate.isSuccess) {
                return WXModuleManager.callModuleMethod(str, str2, str3, jSONArray);
            }
            JSONObject jSONObject2 = onModuleValidate.validateInfo;
            if (jSONObject2 != null) {
                WXLogUtils.e("[WXBridgeManager] module validate fail. >>> " + jSONObject2.toJSONString());
            }
            return jSONObject2;
        }
    }

    public void restart() {
        setJSFrameworkInit(false);
        WXModuleManager.resetAllModuleState();
        initWXBridge(WXEnvironment.sRemoteDebugMode);
        this.mWXBridge.resetWXBridge(WXEnvironment.sRemoteDebugMode);
    }

    public synchronized void setStackTopInstance(final String str) {
        post(new Runnable() {
            public void run() {
                WXBridgeManager.this.mNextTickTasks.setStackTopInstance(str);
            }
        }, str, (WXSDKInstance) null, (String) null);
    }

    public void post(Runnable runnable) {
        postWithName(runnable, (WXSDKInstance) null, (String) null);
    }

    public void postWithName(Runnable runnable, WXSDKInstance wXSDKInstance, String str) {
        Runnable secure = WXThread.secure(runnable, wXSDKInstance, str);
        if ((this.mInterceptor == null || !this.mInterceptor.take(secure)) && this.mJSHandler != null) {
            this.mJSHandler.post(secure);
        }
    }

    public void setInterceptor(Interceptor interceptor) {
        this.mInterceptor = interceptor;
    }

    public void post(Runnable runnable, Object obj, WXSDKInstance wXSDKInstance, String str) {
        if (this.mJSHandler != null) {
            Message obtain = Message.obtain(this.mJSHandler, WXThread.secure(runnable, wXSDKInstance, str));
            obtain.obj = obj;
            obtain.sendToTarget();
        }
    }

    public void post(Runnable runnable, Object obj) {
        post(runnable, obj, (WXSDKInstance) null, (String) null);
    }

    public void postDelay(Runnable runnable, long j) {
        if (this.mJSHandler != null) {
            this.mJSHandler.postDelayed(WXThread.secure(runnable), j);
        }
    }

    /* access modifiers changed from: package-private */
    public void setTimeout(String str, String str2) {
        Message obtain = Message.obtain();
        obtain.what = 1;
        TimerInfo timerInfo = new TimerInfo();
        timerInfo.callbackId = str;
        timerInfo.time = (long) Float.parseFloat(str2);
        obtain.obj = timerInfo;
        this.mJSHandler.sendMessageDelayed(obtain, timerInfo.time);
    }

    public void sendMessageDelayed(Message message, long j) {
        if (message != null && this.mJSHandler != null && this.mJSThread != null && this.mJSThread.isWXThreadAlive() && this.mJSThread.getLooper() != null) {
            this.mJSHandler.sendMessageDelayed(message, j);
        }
    }

    public void removeMessage(int i, Object obj) {
        if (this.mJSHandler != null && this.mJSThread != null && this.mJSThread.isWXThreadAlive() && this.mJSThread.getLooper() != null) {
            this.mJSHandler.removeMessages(i, obj);
        }
    }

    public Object callNativeModule(String str, String str2, String str3, JSONArray jSONArray, Object obj) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            WXLogUtils.d("[WXBridgeManager] call callNativeModule arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callNativeModule", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        try {
            if (WXDomModule.WXDOM.equals(str2)) {
                return WXModuleManager.getDomModule(str).callDomMethod(str3, jSONArray, new long[0]);
            }
            return callModuleMethod(str, str2, str3, jSONArray);
        } catch (Exception e) {
            String str4 = "[WXBridgeManager] callNative exception: " + WXLogUtils.getStackTrace(e);
            WXLogUtils.e(str4);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callNativeModule", str4, (Map<String, String>) null);
            return null;
        }
    }

    public Object callNativeModule(String str, String str2, String str3, JSONArray jSONArray, JSONObject jSONObject) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            WXLogUtils.d("[WXBridgeManager] call callNativeModule arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callNativeModule", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        try {
            if (!WXDomModule.WXDOM.equals(str2)) {
                return callModuleMethod(str, str2, str3, jSONArray, jSONObject);
            }
            WXDomModule domModule = WXModuleManager.getDomModule(str);
            if (domModule != null) {
                return domModule.callDomMethod(str3, jSONArray, new long[0]);
            }
            WXModuleManager.createDomModule(WXSDKManager.getInstance().getSDKInstance(str));
            return null;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callNativeModule exception: " + WXLogUtils.getStackTrace(e));
        }
    }

    public Object callNativeComponent(String str, String str2, String str3, JSONArray jSONArray, Object obj) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            WXLogUtils.d("[WXBridgeManager] call callNativeComponent arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callNativeComponent", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        try {
            WXDomModule domModule = WXModuleManager.getDomModule(str);
            if (domModule != null) {
                domModule.invokeMethod(str2, str3, jSONArray);
            } else {
                WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
                if (sDKInstance == null || !sDKInstance.isDestroy()) {
                    WXLogUtils.e("WXBridgeManager", "callNativeComponent exception :null == dom ,method:" + str3);
                }
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callNativeComponent exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callNativeComponent", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        return null;
    }

    public int callNative(String str, JSONArray jSONArray, String str2) {
        int i;
        String str3 = str;
        JSONArray jSONArray2 = jSONArray;
        String str4 = str2;
        if (TextUtils.isEmpty(str) || jSONArray2 == null) {
            WXLogUtils.d("[WXBridgeManager] call callNative arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str3, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callNative", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str3)) {
            return -1;
        }
        long nanoTime = System.nanoTime() - System.nanoTime();
        if (jSONArray2 != null && jSONArray.size() > 0) {
            int size = jSONArray.size();
            int i2 = 0;
            while (i2 < size) {
                try {
                    JSONObject jSONObject = (JSONObject) jSONArray2.get(i2);
                    if (!(jSONObject == null || WXSDKManager.getInstance().getSDKInstance(str3) == null)) {
                        Object obj = jSONObject.get("module");
                        if (obj == null) {
                            i = i2;
                            if (jSONObject.get(COMPONENT) != null) {
                                WXDomModule domModule = WXModuleManager.getDomModule(str);
                                WXStateRecord instance = WXStateRecord.getInstance();
                                instance.recordAction(str3, "callDomMethod:" + str3 + "," + jSONObject.get("method"));
                                domModule.invokeMethod((String) jSONObject.get("ref"), (String) jSONObject.get("method"), (JSONArray) jSONObject.get("args"));
                            } else {
                                throw new IllegalArgumentException("unknown callNative");
                            }
                        } else if (WXDomModule.WXDOM.equals(obj)) {
                            WXModuleManager.getDomModule(str).callDomMethod(jSONObject, nanoTime);
                        } else {
                            JSONObject jSONObject2 = jSONObject.getJSONObject("options");
                            WXStateRecord instance2 = WXStateRecord.getInstance();
                            instance2.recordAction(str3, "callModuleMethod:" + str3 + "," + obj + "," + jSONObject.get("method"));
                            i = i2;
                            callModuleMethod(str, (String) obj, (String) jSONObject.get("method"), (JSONArray) jSONObject.get("args"), jSONObject2);
                        }
                        i2 = i + 1;
                    }
                    i = i2;
                    i2 = i + 1;
                } catch (Exception e) {
                    WXLogUtils.e("[WXBridgeManager] callNative exception: ", (Throwable) e);
                    WXExceptionUtils.commitCriticalExceptionRT(str3, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callNative", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
                }
            }
        }
        if ("undefined".equals(str4) || "-1".equals(str4)) {
            return 0;
        }
        getNextTick(str3, str4);
        return 1;
    }

    public int callUpdateFinish(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.d("[WXBridgeManager] call callUpdateFinish arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateFinish", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKManager.getInstance().getSDKInstance(str);
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateFinish exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateFinish", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        if (str2 == null || str2.isEmpty() || "undefined".equals(str2) || "-1".equals(str2)) {
            return 0;
        }
        getNextTick(str, str2);
        return 1;
    }

    public int callRefreshFinish(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.d("[WXBridgeManager] call callRefreshFinish arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRefreshFinish", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, new GraphicActionRefreshFinish(sDKInstance));
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRefreshFinish exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRefreshFinish", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        if ("undefined".equals(str2) || "-1".equals(str2)) {
            return 0;
        }
        getNextTick(str, str2);
        return 1;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:59:0x0181 */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x008b A[Catch:{ Exception -> 0x019a }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x008e A[Catch:{ Exception -> 0x019a }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00d6 A[Catch:{ Exception -> 0x019a }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00f3 A[Catch:{ Exception -> 0x019a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int callReportCrashReloadPage(java.lang.String r14, java.lang.String r15) {
        /*
            r13 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r15)
            r1 = 0
            r2 = 0
            com.taobao.weex.WXSDKManager r3 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x019a }
            com.taobao.weex.WXSDKInstance r3 = r3.getSDKInstance(r14)     // Catch:{ Exception -> 0x019a }
            r4 = 1
            if (r3 == 0) goto L_0x001a
            java.lang.String r5 = r3.getBundleUrl()     // Catch:{ Exception -> 0x019a }
            r3.setHasException(r4)     // Catch:{ Exception -> 0x019a }
            r10 = r5
            goto L_0x001b
        L_0x001a:
            r10 = r2
        L_0x001b:
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ Exception -> 0x019a }
            r5 = 2
            r3.<init>(r5)     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = "weexCoreThreadStackTrace:"
            com.taobao.weex.bridge.WXBridgeManager r6 = getInstance()     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = r6.getWeexCoreThreadStackTrace()     // Catch:{ Exception -> 0x019a }
            r3.put(r5, r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = "wxStateInfo"
            com.taobao.weex.performance.WXStateRecord r6 = com.taobao.weex.performance.WXStateRecord.getInstance()     // Catch:{ Exception -> 0x019a }
            java.util.Map r6 = r6.getStateInfo()     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x019a }
            r3.put(r5, r6)     // Catch:{ Exception -> 0x019a }
            if (r0 != 0) goto L_0x0096
            android.app.Application r5 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Throwable -> 0x0075 }
            if (r5 == 0) goto L_0x0081
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0075 }
            r5.<init>()     // Catch:{ Throwable -> 0x0075 }
            com.taobao.weex.bridge.WXParams r6 = r13.mInitParams     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r6 = r6.getCrashFilePath()     // Catch:{ Throwable -> 0x0075 }
            r5.append(r6)     // Catch:{ Throwable -> 0x0075 }
            r5.append(r15)     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0075 }
            java.lang.String r15 = "jsengine"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0073 }
            r6.<init>()     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r7 = "callReportCrashReloadPage crashFile:"
            r6.append(r7)     // Catch:{ Throwable -> 0x0073 }
            r6.append(r5)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0073 }
            android.util.Log.d(r15, r6)     // Catch:{ Throwable -> 0x0073 }
            goto L_0x0080
        L_0x0073:
            r15 = move-exception
            goto L_0x0079
        L_0x0075:
            r5 = move-exception
            r12 = r5
            r5 = r15
            r15 = r12
        L_0x0079:
            java.lang.String r15 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r15)     // Catch:{ Exception -> 0x019a }
            com.taobao.weex.utils.WXLogUtils.e(r15)     // Catch:{ Exception -> 0x019a }
        L_0x0080:
            r15 = r5
        L_0x0081:
            com.taobao.weex.performance.WXStateRecord r5 = com.taobao.weex.performance.WXStateRecord.getInstance()     // Catch:{ Exception -> 0x019a }
            boolean r6 = android.text.TextUtils.isEmpty(r14)     // Catch:{ Exception -> 0x019a }
            if (r6 == 0) goto L_0x008e
            java.lang.String r6 = "null"
            goto L_0x008f
        L_0x008e:
            r6 = r14
        L_0x008f:
            r5.onJSCCrash(r6)     // Catch:{ Exception -> 0x019a }
            r13.callReportCrash(r15, r14, r10, r3)     // Catch:{ Exception -> 0x019a }
            goto L_0x00b3
        L_0x0096:
            com.taobao.weex.performance.WXStateRecord r15 = com.taobao.weex.performance.WXStateRecord.getInstance()     // Catch:{ Exception -> 0x019a }
            boolean r5 = android.text.TextUtils.isEmpty(r14)     // Catch:{ Exception -> 0x019a }
            if (r5 == 0) goto L_0x00a3
            java.lang.String r5 = "null"
            goto L_0x00a4
        L_0x00a3:
            r5 = r14
        L_0x00a4:
            r15.onJSEngineReload(r5)     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = "jsBridge"
            com.taobao.weex.common.WXErrorCode r7 = com.taobao.weex.common.WXErrorCode.WX_ERR_RELOAD_PAGE     // Catch:{ Exception -> 0x019a }
            java.lang.String r8 = "reboot jsc Engine"
            r5 = r13
            r9 = r14
            r11 = r3
            r5.commitJscCrashAlarmMonitor(r6, r7, r8, r9, r10, r11)     // Catch:{ Exception -> 0x019a }
        L_0x00b3:
            com.taobao.weex.jsEngine.JSEngine r15 = com.taobao.weex.jsEngine.JSEngine.getInstance()     // Catch:{ Exception -> 0x019a }
            r15.engineCrashed()     // Catch:{ Exception -> 0x019a }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019a }
            r15.<init>()     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = "reInitCount:"
            r15.append(r5)     // Catch:{ Exception -> 0x019a }
            int r5 = reInitCount     // Catch:{ Exception -> 0x019a }
            r15.append(r5)     // Catch:{ Exception -> 0x019a }
            java.lang.String r15 = r15.toString()     // Catch:{ Exception -> 0x019a }
            com.taobao.weex.utils.WXLogUtils.e(r15)     // Catch:{ Exception -> 0x019a }
            int r15 = reInitCount     // Catch:{ Exception -> 0x019a }
            r5 = 50
            if (r15 <= r5) goto L_0x00f3
            java.lang.String r15 = "jsEngine"
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_ERR_RELOAD_PAGE_EXCEED_LIMIT     // Catch:{ Exception -> 0x019a }
            java.lang.String r4 = "callReportCrashReloadPage"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019a }
            r5.<init>()     // Catch:{ Exception -> 0x019a }
            java.lang.String r6 = "reInitCount:"
            r5.append(r6)     // Catch:{ Exception -> 0x019a }
            int r6 = reInitCount     // Catch:{ Exception -> 0x019a }
            r5.append(r6)     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x019a }
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r15, r0, r4, r5, r3)     // Catch:{ Exception -> 0x019a }
            return r1
        L_0x00f3:
            int r15 = reInitCount     // Catch:{ Exception -> 0x019a }
            int r15 = r15 + r4
            reInitCount = r15     // Catch:{ Exception -> 0x019a }
            if (r0 == 0) goto L_0x012f
            java.lang.String r15 = "restartNewJSThread"
            java.lang.String r3 = "android_weex_ext_config"
            boolean r15 = com.taobao.weex.utils.FeatureSwitches.isOpenWithConfig(r15, r3, r15, r1)     // Catch:{ Exception -> 0x019a }
            if (r15 == 0) goto L_0x012f
            com.taobao.weex.common.WXThread r15 = r13.mJSThread     // Catch:{ Exception -> 0x019a }
            r15.quit()     // Catch:{ Exception -> 0x019a }
            r13.mJSThread = r2     // Catch:{ Exception -> 0x019a }
            r13.mJSHandler = r2     // Catch:{ Exception -> 0x019a }
            com.taobao.weex.common.WXThread r15 = new com.taobao.weex.common.WXThread     // Catch:{ Exception -> 0x019a }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019a }
            r3.<init>()     // Catch:{ Exception -> 0x019a }
            java.lang.String r5 = "WeexJSBridgeThread"
            r3.append(r5)     // Catch:{ Exception -> 0x019a }
            int r5 = reInitCount     // Catch:{ Exception -> 0x019a }
            r3.append(r5)     // Catch:{ Exception -> 0x019a }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x019a }
            r15.<init>((java.lang.String) r3, (android.os.Handler.Callback) r13)     // Catch:{ Exception -> 0x019a }
            r13.mJSThread = r15     // Catch:{ Exception -> 0x019a }
            com.taobao.weex.common.WXThread r15 = r13.mJSThread     // Catch:{ Exception -> 0x019a }
            android.os.Handler r15 = r15.getHandler()     // Catch:{ Exception -> 0x019a }
            r13.mJSHandler = r15     // Catch:{ Exception -> 0x019a }
        L_0x012f:
            java.lang.String r15 = "weexEnableProcessNotify"
            java.lang.String r3 = "android_weex_ext_config"
            boolean r15 = com.taobao.weex.utils.FeatureSwitches.isOpenWithConfig(r15, r3, r15, r4)     // Catch:{ Throwable -> 0x0181 }
            if (r15 == 0) goto L_0x0181
            com.taobao.weex.WXSDKManager r15 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Throwable -> 0x0181 }
            java.util.Map r15 = r15.getAllInstanceMap()     // Catch:{ Throwable -> 0x0181 }
            boolean r3 = r15 instanceof java.util.HashMap     // Catch:{ Throwable -> 0x0181 }
            if (r3 == 0) goto L_0x0181
            java.util.Set r15 = r15.entrySet()     // Catch:{ Throwable -> 0x0181 }
            java.util.Iterator r15 = r15.iterator()     // Catch:{ Throwable -> 0x0181 }
        L_0x014d:
            boolean r3 = r15.hasNext()     // Catch:{ Throwable -> 0x0181 }
            if (r3 == 0) goto L_0x0181
            java.lang.Object r3 = r15.next()     // Catch:{ Throwable -> 0x0181 }
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ Throwable -> 0x0181 }
            if (r3 == 0) goto L_0x014d
            java.lang.Object r4 = r3.getValue()     // Catch:{ Throwable -> 0x0181 }
            if (r4 == 0) goto L_0x014d
            java.lang.Object r4 = r3.getValue()     // Catch:{ Throwable -> 0x0181 }
            com.taobao.weex.WXSDKInstance r4 = (com.taobao.weex.WXSDKInstance) r4     // Catch:{ Throwable -> 0x0181 }
            com.taobao.weex.WXSDKInstance$WXProcessNotify r4 = r4.getWxProcessNotify()     // Catch:{ Throwable -> 0x0181 }
            if (r4 == 0) goto L_0x014d
            java.lang.Object r3 = r3.getValue()     // Catch:{ Throwable -> 0x0181 }
            com.taobao.weex.WXSDKInstance r3 = (com.taobao.weex.WXSDKInstance) r3     // Catch:{ Throwable -> 0x0181 }
            com.taobao.weex.WXSDKInstance$WXProcessNotify r3 = r3.getWxProcessNotify()     // Catch:{ Throwable -> 0x0181 }
            if (r0 == 0) goto L_0x017d
            r3.reboot()     // Catch:{ Throwable -> 0x0181 }
            goto L_0x014d
        L_0x017d:
            r3.crashed()     // Catch:{ Throwable -> 0x0181 }
            goto L_0x014d
        L_0x0181:
            r13.setJSFrameworkInit(r1)     // Catch:{ Exception -> 0x019a }
            com.taobao.weex.bridge.WXModuleManager.resetAllModuleState()     // Catch:{ Exception -> 0x019a }
            java.lang.String r15 = ""
            r13.initScriptsFramework(r15)     // Catch:{ Exception -> 0x019a }
            java.util.HashSet<java.lang.String> r15 = r13.mDestroyedInstanceId     // Catch:{ Exception -> 0x019a }
            if (r15 == 0) goto L_0x01a0
            java.util.HashSet<java.lang.String> r15 = r13.mDestroyedInstanceId     // Catch:{ Exception -> 0x019a }
            boolean r15 = r15.contains(r14)     // Catch:{ Exception -> 0x019a }
            if (r15 == 0) goto L_0x01a0
            r14 = -1
            return r14
        L_0x019a:
            r15 = move-exception
            java.lang.String r0 = "[WXBridgeManager] callReportCrashReloadPage exception: "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r15)
        L_0x01a0:
            com.taobao.weex.WXSDKManager r15 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x01c3 }
            com.taobao.weex.WXSDKInstance r15 = r15.getSDKInstance(r14)     // Catch:{ Exception -> 0x01c3 }
            if (r15 == 0) goto L_0x01d4
            com.taobao.weex.WXSDKManager r15 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x01c3 }
            com.taobao.weex.WXSDKInstance r15 = r15.getSDKInstance(r14)     // Catch:{ Exception -> 0x01c3 }
            java.lang.String r15 = r15.getBundleUrl()     // Catch:{ Exception -> 0x01c3 }
            boolean r15 = r13.shouldReloadCurrentInstance(r15)     // Catch:{ Exception -> 0x01c3 }
            com.taobao.weex.ui.action.ActionReloadPage r0 = new com.taobao.weex.ui.action.ActionReloadPage     // Catch:{ Exception -> 0x01c3 }
            r0.<init>(r14, r15)     // Catch:{ Exception -> 0x01c3 }
            r0.executeAction()     // Catch:{ Exception -> 0x01c3 }
            goto L_0x01d4
        L_0x01c3:
            r15 = move-exception
            java.lang.String r0 = "[WXBridgeManager] callReloadPage exception: "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r15)
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE
            java.lang.String r3 = "callReportCrashReloadPage"
            java.lang.String r15 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r15)
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r14, r0, r3, r15, r2)
        L_0x01d4:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.callReportCrashReloadPage(java.lang.String, java.lang.String):int");
    }

    public boolean shouldReloadCurrentInstance(String str) {
        Uri parse;
        long currentTimeMillis = System.currentTimeMillis();
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter != null) {
            boolean parseBoolean = Boolean.parseBoolean(wxConfigAdapter.getConfig("android_weex_ext_config", "check_biz_url", "true"));
            WXLogUtils.e("check_biz_url : " + parseBoolean);
            if (parseBoolean && !TextUtils.isEmpty(str) && (parse = Uri.parse(str)) != null) {
                str = parse.buildUpon().clearQuery().build().toString();
            }
        }
        if (crashUrl == null || ((crashUrl != null && !crashUrl.equals(str)) || currentTimeMillis - lastCrashTime > 15000)) {
            crashUrl = str;
            lastCrashTime = currentTimeMillis;
            return true;
        }
        lastCrashTime = currentTimeMillis;
        return false;
    }

    public void callReportCrash(String str, String str2, String str3, Map<String, String> map) {
        final String str4 = str + "." + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        File file = new File(str);
        File file2 = new File(str4);
        if (file.exists()) {
            file.renameTo(file2);
        }
        final String str5 = str2;
        final String str6 = str3;
        final Map<String, String> map2 = map;
        new Thread(new Runnable() {
            public void run() {
                try {
                    File file = new File(str4);
                    if (file.exists()) {
                        if (file.length() > 0) {
                            StringBuilder sb = new StringBuilder();
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(str4));
                            while (true) {
                                String readLine = bufferedReader.readLine();
                                if (readLine == null) {
                                    break;
                                } else if (!"".equals(readLine)) {
                                    sb.append(readLine + "\n");
                                }
                            }
                            WXBridgeManager.this.commitJscCrashAlarmMonitor(IWXUserTrackAdapter.JS_BRIDGE, WXErrorCode.WX_ERR_JSC_CRASH, sb.toString(), str5, str6, map2);
                            bufferedReader.close();
                        } else {
                            WXLogUtils.e("[WXBridgeManager] callReportCrash crash file is empty");
                        }
                        if (!WXEnvironment.isApkDebugable()) {
                            file.delete();
                        }
                    }
                } catch (Exception e) {
                    WXLogUtils.e(WXLogUtils.getStackTrace(e));
                } catch (Throwable th) {
                    WXLogUtils.e("[WXBridgeManager] callReportCrash exception: ", th);
                }
            }
        }).start();
    }

    private void getNextTick(String str, String str2) {
        addJSTask("callback", str, str2, "{}");
        sendMessage(str, 6);
    }

    private void getNextTick(String str) {
        addJSTask("callback", str, "", "{}");
        sendMessage(str, 6);
    }

    public String syncExecJsOnInstanceWithResult(String str, String str2, int i) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AnonymousClass7 r1 = new EventResult() {
            public void onCallback(Object obj) {
                super.onCallback(obj);
                countDownLatch.countDown();
            }
        };
        try {
            execJSOnInstance(r1, str, str2, i);
            countDownLatch.await(100, TimeUnit.MILLISECONDS);
            return r1.getResult() != null ? r1.getResult().toString() : "";
        } catch (Throwable th) {
            WXLogUtils.e("syncCallExecJsOnInstance", th);
            return "";
        }
    }

    public void loadJsBundleInPreInitMode(final String str, final String str2) {
        post(new Runnable() {
            public void run() {
                String unused = WXBridgeManager.this.invokeExecJSOnInstance(str, str2, -1);
                WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
                if (wXSDKInstance != null && wXSDKInstance.isPreInitMode()) {
                    wXSDKInstance.getApmForInstance().onStage(WXInstanceApm.KEY_PAGE_STAGES_LOAD_BUNDLE_END);
                    wXSDKInstance.getApmForInstance().onStageWithTime(WXInstanceApm.KEY_PAGE_STAGES_END_EXCUTE_BUNDLE, WXUtils.getFixUnixTime() + 600);
                }
            }
        });
    }

    public EventResult syncCallJSEventWithResult(String str, String str2, List<Object> list, Object... objArr) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AnonymousClass9 r1 = new EventResult() {
            public void onCallback(Object obj) {
                super.onCallback(obj);
                countDownLatch.countDown();
            }
        };
        try {
            asyncCallJSEventWithResult(r1, str, str2, list, objArr);
            countDownLatch.await(100, TimeUnit.MILLISECONDS);
            return r1;
        } catch (Exception e) {
            WXLogUtils.e("syncCallJSEventWithResult", (Throwable) e);
            return r1;
        }
    }

    public void asyncCallJSEventVoidResult(String str, String str2, List<Object> list, Object... objArr) {
        final Object[] objArr2 = objArr;
        final List<Object> list2 = list;
        final String str3 = str;
        final String str4 = str2;
        post(new Runnable() {
            public void run() {
                try {
                    if (objArr2 == null) {
                        return;
                    }
                    if (objArr2.length != 0) {
                        ArrayList arrayList = new ArrayList();
                        for (Object add : objArr2) {
                            arrayList.add(add);
                        }
                        if (list2 != null) {
                            ArrayMap arrayMap = new ArrayMap(4);
                            arrayMap.put("params", list2);
                            arrayList.add(arrayMap);
                        }
                        WXHashMap wXHashMap = new WXHashMap();
                        wXHashMap.put("method", str3);
                        wXHashMap.put("args", arrayList);
                        WXJSObject[] wXJSObjectArr = {new WXJSObject(2, str4), WXWsonJSONSwitch.toWsonOrJsonWXJSObject(new Object[]{wXHashMap})};
                        WXBridgeManager.this.invokeExecJS(String.valueOf(str4), (String) null, WXBridgeManager.METHOD_CALL_JS, wXJSObjectArr, true);
                        wXJSObjectArr[0] = null;
                    }
                } catch (Exception e) {
                    WXLogUtils.e("asyncCallJSEventVoidResult", (Throwable) e);
                }
            }
        });
    }

    private void asyncCallJSEventWithResult(EventResult eventResult, String str, String str2, List<Object> list, Object... objArr) {
        final Object[] objArr2 = objArr;
        final List<Object> list2 = list;
        final String str3 = str;
        final String str4 = str2;
        final EventResult eventResult2 = eventResult;
        post(new Runnable() {
            public void run() {
                try {
                    if (objArr2 == null) {
                        return;
                    }
                    if (objArr2.length != 0) {
                        ArrayList arrayList = new ArrayList();
                        for (Object add : objArr2) {
                            arrayList.add(add);
                        }
                        if (list2 != null) {
                            ArrayMap arrayMap = new ArrayMap(4);
                            arrayMap.put("params", list2);
                            arrayList.add(arrayMap);
                        }
                        WXHashMap wXHashMap = new WXHashMap();
                        wXHashMap.put("method", str3);
                        wXHashMap.put("args", arrayList);
                        WXJSObject[] wXJSObjectArr = {new WXJSObject(2, str4), WXWsonJSONSwitch.toWsonOrJsonWXJSObject(new Object[]{wXHashMap})};
                        WXBridgeManager.this.invokeExecJSWithCallback(String.valueOf(str4), (String) null, WXBridgeManager.METHOD_CALL_JS, wXJSObjectArr, eventResult2 != null ? new ResultCallback<byte[]>() {
                            public void onReceiveResult(byte[] bArr) {
                                JSONArray jSONArray = (JSONArray) WXWsonJSONSwitch.parseWsonOrJSON(bArr);
                                if (jSONArray != null && jSONArray.size() > 0) {
                                    eventResult2.onCallback(jSONArray.get(0));
                                }
                            }
                        } : null, true);
                        wXJSObjectArr[0] = null;
                    }
                } catch (Exception e) {
                    WXLogUtils.e("asyncCallJSEventWithResult", (Throwable) e);
                }
            }
        });
    }

    private void addJSEventTask(String str, String str2, List<Object> list, Object... objArr) {
        final Object[] objArr2 = objArr;
        final List<Object> list2 = list;
        final String str3 = str;
        final String str4 = str2;
        post(new Runnable() {
            public void run() {
                if (objArr2 != null && objArr2.length != 0) {
                    ArrayList arrayList = new ArrayList();
                    for (Object add : objArr2) {
                        arrayList.add(add);
                    }
                    if (list2 != null) {
                        ArrayMap arrayMap = new ArrayMap(4);
                        arrayMap.put("params", list2);
                        arrayList.add(arrayMap);
                    }
                    WXHashMap wXHashMap = new WXHashMap();
                    wXHashMap.put("method", str3);
                    wXHashMap.put("args", arrayList);
                    if (WXBridgeManager.this.mNextTickTasks.get(str4) == null) {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(wXHashMap);
                        WXBridgeManager.this.mNextTickTasks.put(str4, arrayList2);
                        return;
                    }
                    ((ArrayList) WXBridgeManager.this.mNextTickTasks.get(str4)).add(wXHashMap);
                }
            }
        });
    }

    private void addJSTask(String str, String str2, Object... objArr) {
        addJSEventTask(str, str2, (List<Object>) null, objArr);
    }

    private void sendMessage(String str, int i) {
        Message obtain = Message.obtain(this.mJSHandler);
        obtain.obj = str;
        obtain.what = i;
        obtain.sendToTarget();
    }

    public synchronized void initScriptsFramework(String str) {
        Message obtainMessage = this.mJSHandler.obtainMessage();
        obtainMessage.obj = str;
        obtainMessage.what = 7;
        obtainMessage.setTarget(this.mJSHandler);
        obtainMessage.sendToTarget();
    }

    @Deprecated
    public void fireEvent(String str, String str2, String str3, Map<String, Object> map) {
        fireEvent(str, str2, str3, map, (Map<String, Object>) null);
    }

    @Deprecated
    public void fireEvent(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        fireEventOnNode(str, str2, str3, map, map2);
    }

    public void fireEventOnNode(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        fireEventOnNode(str, str2, str3, map, map2, (List<Object>) null, (EventResult) null);
    }

    public void fireEventOnNode(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2, List<Object> list) {
        fireEventOnNode(str, str2, str3, map, map2, list, (EventResult) null);
    }

    public void fireEventOnNode(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2, List<Object> list, EventResult eventResult) {
        String str4 = str;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3) && this.mJSHandler != null) {
            if (checkMainThread()) {
                WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
                if (wXSDKInstance != null && wXSDKInstance.isUsingEaglePlugin()) {
                    int isSupportFireEvent = wXSDKInstance.getEaglePlugin().isSupportFireEvent(str);
                    if (isSupportFireEvent != 0) {
                        fireEventOnDataRenderNode(wXSDKInstance.getEaglePlugin(), str, str2, str3, map, map2);
                        if (isSupportFireEvent == 1) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                if (eventResult == null) {
                    addJSEventTask(METHOD_FIRE_EVENT, str, list, str2, str3, map, map2);
                    sendMessage(str, 6);
                    return;
                }
                List<Object> list2 = list;
                asyncCallJSEventWithResult(eventResult, METHD_FIRE_EVENT_SYNC, str, list, str2, str3, map, map2);
                return;
            }
            throw new WXRuntimeException("fireEvent must be called by main thread");
        }
    }

    private void fireEventOnDataRenderNode(WXEaglePlugin wXEaglePlugin, String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        final String str4 = str;
        final Map<String, Object> map3 = map;
        final WXEaglePlugin wXEaglePlugin2 = wXEaglePlugin;
        final String str5 = str2;
        final String str6 = str3;
        final Map<String, Object> map4 = map2;
        this.mJSHandler.postDelayed(WXThread.secure((Runnable) new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:13:0x004c A[Catch:{ Throwable -> 0x006d }] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r8 = this;
                    long r0 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x006d }
                    boolean r2 = com.taobao.weex.WXEnvironment.isApkDebugable()     // Catch:{ Throwable -> 0x006d }
                    if (r2 == 0) goto L_0x002a
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x006d }
                    r2.<init>()     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r3 = "fireEventOnDataRenderNode >>>> instanceId:"
                    r2.append(r3)     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r3 = r2     // Catch:{ Throwable -> 0x006d }
                    r2.append(r3)     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r3 = ", data:"
                    r2.append(r3)     // Catch:{ Throwable -> 0x006d }
                    java.util.Map r3 = r3     // Catch:{ Throwable -> 0x006d }
                    r2.append(r3)     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x006d }
                    com.taobao.weex.utils.WXLogUtils.d(r2)     // Catch:{ Throwable -> 0x006d }
                L_0x002a:
                    com.taobao.weex.bridge.WXEaglePlugin r2 = r4     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r3 = r2     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r4 = r5     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r5 = r6     // Catch:{ Throwable -> 0x006d }
                    java.util.Map r6 = r3     // Catch:{ Throwable -> 0x006d }
                    if (r6 == 0) goto L_0x0046
                    java.util.Map r6 = r3     // Catch:{ Throwable -> 0x006d }
                    boolean r6 = r6.isEmpty()     // Catch:{ Throwable -> 0x006d }
                    if (r6 == 0) goto L_0x003f
                    goto L_0x0046
                L_0x003f:
                    java.util.Map r6 = r3     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r6 = com.alibaba.fastjson.JSON.toJSONString(r6)     // Catch:{ Throwable -> 0x006d }
                    goto L_0x0048
                L_0x0046:
                    java.lang.String r6 = "{}"
                L_0x0048:
                    java.util.Map r7 = r7     // Catch:{ Throwable -> 0x006d }
                    if (r7 == 0) goto L_0x005c
                    java.util.Map r7 = r7     // Catch:{ Throwable -> 0x006d }
                    boolean r7 = r7.isEmpty()     // Catch:{ Throwable -> 0x006d }
                    if (r7 == 0) goto L_0x0055
                    goto L_0x005c
                L_0x0055:
                    java.util.Map r7 = r7     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r7 = com.alibaba.fastjson.JSON.toJSONString(r7)     // Catch:{ Throwable -> 0x006d }
                    goto L_0x005e
                L_0x005c:
                    java.lang.String r7 = "{}"
                L_0x005e:
                    r2.fireEvent(r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x006d }
                    java.lang.String r2 = "fireEventOnDataRenderNode"
                    long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x006d }
                    r5 = 0
                    long r3 = r3 - r0
                    com.taobao.weex.utils.WXLogUtils.renderPerformanceLog(r2, r3)     // Catch:{ Throwable -> 0x006d }
                    goto L_0x0090
                L_0x006d:
                    r0 = move-exception
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r2 = "[WXBridgeManager] fireEventOnDataRenderNode "
                    r1.append(r2)
                    java.lang.String r0 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r0)
                    r1.append(r0)
                    java.lang.String r0 = r1.toString()
                    java.lang.String r1 = r2
                    com.taobao.weex.common.WXErrorCode r2 = com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE
                    java.lang.String r3 = "fireEventOnDataRenderNode"
                    r4 = 0
                    com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r1, r2, r3, r0, r4)
                    com.taobao.weex.utils.WXLogUtils.e(r0)
                L_0x0090:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.AnonymousClass13.run():void");
            }
        }), 0);
    }

    private boolean checkMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @Deprecated
    public void callback(String str, String str2, String str3) {
        callback(str, str2, str3, false);
    }

    @Deprecated
    public void callback(String str, String str2, Map<String, Object> map) {
        callback(str, str2, map, false);
    }

    @Deprecated
    public void callback(String str, String str2, Object obj, boolean z) {
        callbackJavascript(str, str2, obj, z);
    }

    /* access modifiers changed from: package-private */
    public void callbackJavascript(String str, String str2, Object obj, boolean z) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && this.mJSHandler != null) {
            WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
            if (wXSDKInstance != null && wXSDKInstance.isUsingEaglePlugin()) {
                int isSupportJSCallback = wXSDKInstance.getEaglePlugin().isSupportJSCallback(str);
                if (isSupportJSCallback != 0) {
                    callbackJavascriptOnDataRender(wXSDKInstance.getEaglePlugin(), str, str2, obj, z);
                    if (isSupportJSCallback == 1) {
                        return;
                    }
                } else {
                    return;
                }
            }
            addJSTask("callback", str, str2, obj, Boolean.valueOf(z));
            sendMessage(str, 6);
        }
    }

    /* access modifiers changed from: package-private */
    public void callbackJavascriptOnDataRender(WXEaglePlugin wXEaglePlugin, String str, String str2, Object obj, boolean z) {
        final Object obj2 = obj;
        final String str3 = str;
        final WXEaglePlugin wXEaglePlugin2 = wXEaglePlugin;
        final String str4 = str2;
        final boolean z2 = z;
        this.mJSHandler.postDelayed(WXThread.secure((Runnable) new Runnable() {
            public void run() {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    String jSONString = JSON.toJSONString(obj2);
                    if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.d("callbackJavascriptOnDataRender >>>> instanceId:" + str3 + ", data:" + jSONString);
                    }
                    wXEaglePlugin2.invokeJSCallback(str3, str4, jSONString, z2);
                    WXLogUtils.renderPerformanceLog("callbackJavascriptOnDataRender", System.currentTimeMillis() - currentTimeMillis);
                } catch (Throwable th) {
                    String str = "[WXBridgeManager] callbackJavascriptOnDataRender " + WXLogUtils.getStackTrace(th);
                    WXExceptionUtils.commitCriticalExceptionRT(str3, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callbackJavascriptOnDataRender", str, (Map<String, String>) null);
                    WXLogUtils.e(str);
                }
            }
        }), 0);
    }

    public void refreshInstance(final String str, final WXRefreshData wXRefreshData) {
        if (!TextUtils.isEmpty(str) && wXRefreshData != null) {
            this.mJSHandler.postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    WXBridgeManager.this.invokeRefreshInstance(str, wXRefreshData);
                }
            }), 0);
        }
    }

    /* access modifiers changed from: private */
    public void invokeRefreshInstance(String str, WXRefreshData wXRefreshData) {
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
                System.currentTimeMillis();
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("refreshInstance >>>> instanceId:" + str + ", data:" + wXRefreshData.data + ", isDirty:" + wXRefreshData.isDirty);
                }
                if (!wXRefreshData.isDirty) {
                    this.mWXBridge.refreshInstance(str, (String) null, METHOD_REFRESH_INSTANCE, new WXJSObject[]{new WXJSObject(2, str), new WXJSObject(3, wXRefreshData.data == null ? "{}" : wXRefreshData.data)});
                    return;
                }
                return;
            }
            if (sDKInstance != null) {
                sDKInstance.onRenderError(WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode(), WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorMsg() + "invokeRefreshInstance FAILED for JSFrameworkInit FAILED, intance will invoke instance.onRenderError");
            }
            WXLogUtils.e("[WXBridgeManager] invokeRefreshInstance: framework.js uninitialized.");
        } catch (Throwable th) {
            String str2 = "[WXBridgeManager] invokeRefreshInstance " + WXLogUtils.getStackTrace(th);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "invokeRefreshInstance", str2, (Map<String, String>) null);
            WXLogUtils.e(str2);
        }
    }

    public void commitJscCrashAlarmMonitor(String str, WXErrorCode wXErrorCode, String str2, String str3, String str4, Map<String, String> map) {
        if (!TextUtils.isEmpty(str) && wXErrorCode != null) {
            Log.d("ReportCrash", " commitJscCrashAlarmMonitor errMsg " + str2);
            HashMap hashMap = new HashMap();
            hashMap.put("jscCrashStack", str2);
            if (map != null) {
                hashMap.putAll(map);
            }
            IWXJSExceptionAdapter iWXJSExceptionAdapter = WXSDKManager.getInstance().getIWXJSExceptionAdapter();
            if (iWXJSExceptionAdapter != null) {
                WXJSExceptionInfo wXJSExceptionInfo = new WXJSExceptionInfo(str3, str4, wXErrorCode, "callReportCrash", "weex core process crash and restart exception", hashMap);
                iWXJSExceptionAdapter.onJSException(wXJSExceptionInfo);
                WXLogUtils.e(wXJSExceptionInfo.toString());
            }
        }
    }

    private boolean isSkipFrameworkInit(String str) {
        return isSkipFrameworkInit(WXSDKManager.getInstance().getSDKInstance(str));
    }

    private boolean isSkipFrameworkInit(WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance == null) {
            return false;
        }
        return wXSDKInstance.skipFrameworkInit();
    }

    public void createInstance(String str, String str2, Map<String, Object> map, String str3) {
        createInstance(str, new Script(str2), map, str3);
    }

    public void setLogLevel(final int i, final boolean z) {
        post(new Runnable() {
            public void run() {
                if (WXBridgeManager.this.mWXBridge != null) {
                    WXBridgeManager.this.mWXBridge.setLogType((float) i, z);
                }
            }
        });
    }

    public void createInstance(String str, Script script, Map<String, Object> map, String str2) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            WXLogUtils.e("WXBridgeManager", "createInstance failed, SDKInstance does not exist");
            return;
        }
        boolean z = true;
        if (TextUtils.isEmpty(str) || script == null || script.isEmpty() || this.mJSHandler == null) {
            String errorCode = WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode();
            sDKInstance.onRenderError(errorCode, WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorMsg() + " instanceId==" + str + " template ==" + script + " mJSHandler== " + this.mJSHandler.toString());
            WXInstanceApm apmForInstance = sDKInstance.getApmForInstance();
            StringBuilder sb = new StringBuilder();
            sb.append("createInstance failed return; ");
            sb.append(TextUtils.isEmpty(str));
            sb.append(",");
            sb.append(script.isEmpty());
            sb.append(",");
            if (this.mJSHandler != null) {
                z = false;
            }
            sb.append(z);
            apmForInstance.onStage(sb.toString());
        } else if (isSkipFrameworkInit(str) || isJSFrameworkInit() || reInitCount != 1 || WXEnvironment.sDebugServerConnectable) {
            WXModuleManager.createDomModule(sDKInstance);
            sDKInstance.getApmForInstance().onStage(WXInstanceApm.KEY_PAGE_STAGES_LOAD_BUNDLE_START);
            final WXSDKInstance wXSDKInstance = sDKInstance;
            final String str3 = str;
            final Script script2 = script;
            final Map<String, Object> map2 = map;
            final String str4 = str2;
            post(new Runnable() {
                public void run() {
                    wXSDKInstance.getApmForInstance().onStage("wxLoadBundleStartOnJsThread");
                    long currentTimeMillis = System.currentTimeMillis();
                    WXBridgeManager.this.mWXBridge.setPageArgument(str3, "renderTimeOrigin", String.valueOf(wXSDKInstance.getWXPerformance().renderTimeOrigin));
                    WXBridgeManager.this.mWXBridge.setInstanceRenderType(wXSDKInstance.getInstanceId(), wXSDKInstance.getRenderType());
                    WXBridgeManager.this.invokeCreateInstance(wXSDKInstance, script2, map2, str4);
                    wXSDKInstance.getWXPerformance().callCreateInstanceTime = System.currentTimeMillis() - currentTimeMillis;
                    wXSDKInstance.getWXPerformance().communicateTime = wXSDKInstance.getWXPerformance().callCreateInstanceTime;
                }
            }, str, sDKInstance, METHOD_CREATE_INSTANCE);
        } else {
            String errorCode2 = WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode();
            sDKInstance.onRenderError(errorCode2, WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorMsg() + " isJSFrameworkInit==" + isJSFrameworkInit() + " reInitCount == 1");
            sDKInstance.getApmForInstance().onStage("createInstance failed jsfm isn't init return;");
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.initFramework("");
                }
            }, str, sDKInstance, "initFrameworkInCreateInstance");
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0225 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x025a A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0095 A[SYNTHETIC, Splitter:B:27:0x0095] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00aa A[Catch:{ Throwable -> 0x00ff }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00f3 A[Catch:{ Throwable -> 0x00ff }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0121 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0132 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x014d A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0159 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x015e A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0161 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x016d A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0170 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0181 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0184 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x018d A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01a2 A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x01cf A[Catch:{ Throwable -> 0x037d }] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01d9 A[Catch:{ Throwable -> 0x037d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invokeCreateInstance(@androidx.annotation.NonNull com.taobao.weex.WXSDKInstance r17, com.taobao.weex.Script r18, java.util.Map<java.lang.String, java.lang.Object> r19, java.lang.String r20) {
        /*
            r16 = this;
            r7 = r16
            r8 = r17
            boolean r0 = r16.isSkipFrameworkInit((com.taobao.weex.WXSDKInstance) r17)
            if (r0 != 0) goto L_0x000f
            java.lang.String r0 = ""
            r7.initFramework(r0)
        L_0x000f:
            boolean r0 = r7.mMock
            if (r0 == 0) goto L_0x001c
            java.lang.String r0 = r17.getInstanceId()
            r7.mock(r0)
            goto L_0x03f7
        L_0x001c:
            boolean r0 = r16.isSkipFrameworkInit((com.taobao.weex.WXSDKInstance) r17)
            if (r0 != 0) goto L_0x0046
            boolean r0 = r16.isJSFrameworkInit()
            if (r0 != 0) goto L_0x0046
            java.lang.String r0 = "[WXBridgeManager] invokeCreateInstance: framework.js uninitialized."
            com.taobao.weex.common.WXErrorCode r1 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED
            java.lang.String r1 = r1.getErrorCode()
            com.taobao.weex.common.WXErrorCode r2 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED
            java.lang.String r2 = r2.getErrorMsg()
            r8.onRenderError(r1, r2)
            com.taobao.weex.utils.WXLogUtils.e(r0)
            com.taobao.weex.performance.WXInstanceApm r0 = r17.getApmForInstance()
            java.lang.String r1 = "framework.js uninitialized and return"
            r0.onStage(r1)
            return
        L_0x0046:
            com.taobao.weex.bridge.WXModuleManager.registerWhenCreateInstance()
            com.taobao.weex.bridge.WXBridgeManager$BundType r1 = com.taobao.weex.bridge.WXBridgeManager.BundType.Others     // Catch:{ Throwable -> 0x037d }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x008a }
            java.lang.String r0 = r17.getBundleUrl()     // Catch:{ Throwable -> 0x008a }
            java.lang.String r4 = r18.getContent()     // Catch:{ Throwable -> 0x008a }
            com.taobao.weex.bridge.WXBridgeManager$BundType r4 = r7.getBundleType(r0, r4)     // Catch:{ Throwable -> 0x008a }
            boolean r0 = com.taobao.weex.WXEnvironment.isOpenDebugLog()     // Catch:{ Throwable -> 0x0088 }
            if (r0 == 0) goto L_0x0093
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0088 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0088 }
            r5.<init>()     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r6 = "end getBundleType type:"
            r5.append(r6)     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r6 = r4.toString()     // Catch:{ Throwable -> 0x0088 }
            r5.append(r6)     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r6 = " time:"
            r5.append(r6)     // Catch:{ Throwable -> 0x0088 }
            r6 = 0
            long r0 = r0 - r2
            r5.append(r0)     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r0 = r5.toString()     // Catch:{ Throwable -> 0x0088 }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ Throwable -> 0x0088 }
            goto L_0x0093
        L_0x0088:
            r0 = move-exception
            goto L_0x008c
        L_0x008a:
            r0 = move-exception
            r4 = r1
        L_0x008c:
            java.lang.String r0 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r0)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ Throwable -> 0x037d }
        L_0x0093:
            if (r19 != 0) goto L_0x00a0
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Throwable -> 0x009c }
            r0.<init>()     // Catch:{ Throwable -> 0x009c }
            r1 = r0
            goto L_0x00a2
        L_0x009c:
            r0 = move-exception
            r1 = r19
            goto L_0x0100
        L_0x00a0:
            r1 = r19
        L_0x00a2:
            java.lang.String r0 = "bundleType"
            java.lang.Object r0 = r1.get(r0)     // Catch:{ Throwable -> 0x00ff }
            if (r0 != 0) goto L_0x00eb
            com.taobao.weex.bridge.WXBridgeManager$BundType r0 = com.taobao.weex.bridge.WXBridgeManager.BundType.Vue     // Catch:{ Throwable -> 0x00ff }
            if (r4 != r0) goto L_0x00b6
            java.lang.String r0 = "bundleType"
            java.lang.String r2 = "Vue"
            r1.put(r0, r2)     // Catch:{ Throwable -> 0x00ff }
            goto L_0x00c9
        L_0x00b6:
            com.taobao.weex.bridge.WXBridgeManager$BundType r0 = com.taobao.weex.bridge.WXBridgeManager.BundType.Rax     // Catch:{ Throwable -> 0x00ff }
            if (r4 != r0) goto L_0x00c2
            java.lang.String r0 = "bundleType"
            java.lang.String r2 = "Rax"
            r1.put(r0, r2)     // Catch:{ Throwable -> 0x00ff }
            goto L_0x00c9
        L_0x00c2:
            java.lang.String r0 = "bundleType"
            java.lang.String r2 = "Others"
            r1.put(r0, r2)     // Catch:{ Throwable -> 0x00ff }
        L_0x00c9:
            java.lang.String r0 = "bundleType"
            java.lang.Object r0 = r1.get(r0)     // Catch:{ Throwable -> 0x00ff }
            boolean r2 = r0 instanceof java.lang.String     // Catch:{ Throwable -> 0x00ff }
            if (r2 == 0) goto L_0x00e0
            java.lang.String r2 = "Others"
            r3 = r0
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x00ff }
            boolean r2 = r2.equalsIgnoreCase(r3)     // Catch:{ Throwable -> 0x00ff }
            if (r2 == 0) goto L_0x00e0
            java.lang.String r0 = "other"
        L_0x00e0:
            if (r0 == 0) goto L_0x00eb
            com.taobao.weex.performance.WXInstanceApm r2 = r17.getApmForInstance()     // Catch:{ Throwable -> 0x00ff }
            java.lang.String r3 = "wxBundleType"
            r2.addProperty(r3, r0)     // Catch:{ Throwable -> 0x00ff }
        L_0x00eb:
            java.lang.String r0 = "env"
            java.lang.Object r0 = r1.get(r0)     // Catch:{ Throwable -> 0x00ff }
            if (r0 != 0) goto L_0x0107
            java.lang.String r0 = "env"
            com.taobao.weex.bridge.WXParams r2 = r7.mInitParams     // Catch:{ Throwable -> 0x00ff }
            java.util.Map r2 = r2.toMap()     // Catch:{ Throwable -> 0x00ff }
            r1.put(r0, r2)     // Catch:{ Throwable -> 0x00ff }
            goto L_0x0107
        L_0x00ff:
            r0 = move-exception
        L_0x0100:
            java.lang.String r0 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r0)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ Throwable -> 0x037d }
        L_0x0107:
            r8.bundleType = r4     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.WXEnvironment.isApkDebugable()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.bridge.WXJSObject r0 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = r17.getInstanceId()     // Catch:{ Throwable -> 0x037d }
            r3 = 2
            r0.<init>(r3, r2)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = r18.getContent()     // Catch:{ Throwable -> 0x037d }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x037d }
            r5 = 1
            if (r2 == 0) goto L_0x0132
            com.taobao.weex.bridge.WXJSObject r2 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            byte[] r6 = r18.getBinary()     // Catch:{ Throwable -> 0x037d }
            r2.<init>(r3, r6)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.bridge.WXJSObject r6 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            java.lang.String r9 = "binary"
            r6.<init>(r3, r9)     // Catch:{ Throwable -> 0x037d }
            goto L_0x0142
        L_0x0132:
            com.taobao.weex.bridge.WXJSObject r2 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            java.lang.String r6 = r18.getContent()     // Catch:{ Throwable -> 0x037d }
            r2.<init>(r3, r6)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.bridge.WXJSObject r6 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            java.lang.String r9 = "string"
            r6.<init>(r5, r9)     // Catch:{ Throwable -> 0x037d }
        L_0x0142:
            r9 = 0
            if (r1 == 0) goto L_0x0159
            java.lang.String r10 = "extraOption"
            boolean r10 = r1.containsKey(r10)     // Catch:{ Throwable -> 0x037d }
            if (r10 == 0) goto L_0x0159
            java.lang.String r10 = "extraOption"
            java.lang.Object r10 = r1.get(r10)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r11 = "extraOption"
            r1.remove(r11)     // Catch:{ Throwable -> 0x037d }
            goto L_0x015a
        L_0x0159:
            r10 = r9
        L_0x015a:
            com.taobao.weex.bridge.WXJSObject r11 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            if (r10 != 0) goto L_0x0161
            java.lang.String r10 = "{}"
            goto L_0x0165
        L_0x0161:
            java.lang.String r10 = com.taobao.weex.utils.WXJsonUtils.fromObjectToJSONString(r10)     // Catch:{ Throwable -> 0x037d }
        L_0x0165:
            r12 = 3
            r11.<init>(r12, r10)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.bridge.WXJSObject r10 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            if (r1 != 0) goto L_0x0170
            java.lang.String r1 = "{}"
            goto L_0x0174
        L_0x0170:
            java.lang.String r1 = com.taobao.weex.utils.WXJsonUtils.fromObjectToJSONString(r1)     // Catch:{ Throwable -> 0x037d }
        L_0x0174:
            r10.<init>(r12, r1)     // Catch:{ Throwable -> 0x037d }
            boolean r1 = isSandBoxContext     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.bridge.WXJSObject r1 = r7.optionObjConvert(r1, r4, r10)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.bridge.WXJSObject r10 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            if (r20 != 0) goto L_0x0184
            java.lang.String r13 = "{}"
            goto L_0x0186
        L_0x0184:
            r13 = r20
        L_0x0186:
            r10.<init>(r12, r13)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.bridge.WXBridgeManager$BundType r13 = com.taobao.weex.bridge.WXBridgeManager.BundType.Rax     // Catch:{ Throwable -> 0x037d }
            if (r4 == r13) goto L_0x019e
            com.taobao.weex.common.WXRenderStrategy r13 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r14 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Throwable -> 0x037d }
            if (r13 != r14) goto L_0x0196
            goto L_0x019e
        L_0x0196:
            com.taobao.weex.bridge.WXJSObject r13 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            java.lang.String r14 = ""
            r13.<init>(r3, r14)     // Catch:{ Throwable -> 0x037d }
            goto L_0x01c9
        L_0x019e:
            java.lang.String r13 = mRaxApi     // Catch:{ Throwable -> 0x037d }
            if (r13 != 0) goto L_0x01c2
            com.taobao.weex.adapter.IWXJsFileLoaderAdapter r13 = com.taobao.weex.WXSDKEngine.getIWXJsFileLoaderAdapter()     // Catch:{ Throwable -> 0x037d }
            if (r13 == 0) goto L_0x01ae
            java.lang.String r13 = r13.loadRaxApi()     // Catch:{ Throwable -> 0x037d }
            mRaxApi = r13     // Catch:{ Throwable -> 0x037d }
        L_0x01ae:
            java.lang.String r13 = mRaxApi     // Catch:{ Throwable -> 0x037d }
            boolean r13 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Throwable -> 0x037d }
            if (r13 == 0) goto L_0x01c2
            java.lang.String r13 = "weex-rax-api.js"
            android.app.Application r14 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r13 = com.taobao.weex.utils.WXFileUtils.loadAsset(r13, r14)     // Catch:{ Throwable -> 0x037d }
            mRaxApi = r13     // Catch:{ Throwable -> 0x037d }
        L_0x01c2:
            com.taobao.weex.bridge.WXJSObject r13 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            java.lang.String r14 = mRaxApi     // Catch:{ Throwable -> 0x037d }
            r13.<init>(r3, r14)     // Catch:{ Throwable -> 0x037d }
        L_0x01c9:
            boolean r14 = r17.isUsingEaglePlugin()     // Catch:{ Throwable -> 0x037d }
            if (r14 == 0) goto L_0x01d9
            com.taobao.weex.bridge.WXJSObject r9 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            java.lang.String r14 = r17.getEaglePluginName()     // Catch:{ Throwable -> 0x037d }
            r9.<init>(r3, r14)     // Catch:{ Throwable -> 0x037d }
            goto L_0x0205
        L_0x01d9:
            com.taobao.weex.common.WXRenderStrategy r14 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r15 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ Throwable -> 0x037d }
            if (r14 == r15) goto L_0x01fe
            com.taobao.weex.common.WXRenderStrategy r14 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r15 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Throwable -> 0x037d }
            if (r14 != r15) goto L_0x01ea
            goto L_0x01fe
        L_0x01ea:
            com.taobao.weex.common.WXRenderStrategy r14 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r15 = com.taobao.weex.common.WXRenderStrategy.JSON_RENDER     // Catch:{ Throwable -> 0x037d }
            if (r14 != r15) goto L_0x0205
            com.taobao.weex.bridge.WXJSObject r9 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r14 = com.taobao.weex.common.WXRenderStrategy.JSON_RENDER     // Catch:{ Throwable -> 0x037d }
            java.lang.String r14 = r14.getFlag()     // Catch:{ Throwable -> 0x037d }
            r9.<init>(r3, r14)     // Catch:{ Throwable -> 0x037d }
            goto L_0x0205
        L_0x01fe:
            com.taobao.weex.bridge.WXJSObject r9 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Throwable -> 0x037d }
            java.lang.String r14 = "DATA_RENDER"
            r9.<init>(r3, r14)     // Catch:{ Throwable -> 0x037d }
        L_0x0205:
            r14 = 7
            com.taobao.weex.bridge.WXJSObject[] r14 = new com.taobao.weex.bridge.WXJSObject[r14]     // Catch:{ Throwable -> 0x037d }
            r15 = 0
            r14[r15] = r0     // Catch:{ Throwable -> 0x037d }
            r14[r5] = r2     // Catch:{ Throwable -> 0x037d }
            r14[r3] = r1     // Catch:{ Throwable -> 0x037d }
            r14[r12] = r10     // Catch:{ Throwable -> 0x037d }
            r0 = 4
            r14[r0] = r13     // Catch:{ Throwable -> 0x037d }
            r0 = 5
            r14[r0] = r9     // Catch:{ Throwable -> 0x037d }
            r0 = 6
            r14[r0] = r11     // Catch:{ Throwable -> 0x037d }
            java.lang.String r0 = r18.getContent()     // Catch:{ Throwable -> 0x037d }
            r8.setTemplate(r0)     // Catch:{ Throwable -> 0x037d }
            boolean r0 = isSandBoxContext     // Catch:{ Throwable -> 0x037d }
            if (r0 != 0) goto L_0x025a
            com.taobao.weex.performance.WXInstanceApm r0 = r17.getApmForInstance()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "!isSandBoxContext,and excute"
            r0.onStage(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x037d }
            r0.<init>()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "Instance "
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = r17.getInstanceId()     // Catch:{ Throwable -> 0x037d }
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = " Did Not Render in SandBox Mode"
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = r17.getInstanceId()     // Catch:{ Throwable -> 0x037d }
            r3 = 0
            java.lang.String r4 = "createInstance"
            r6 = 0
            r1 = r16
            r5 = r14
            r1.invokeExecJS(r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x037d }
            return
        L_0x025a:
            com.taobao.weex.bridge.WXBridgeManager$BundType r0 = com.taobao.weex.bridge.WXBridgeManager.BundType.Vue     // Catch:{ Throwable -> 0x037d }
            if (r4 == r0) goto L_0x02ce
            com.taobao.weex.bridge.WXBridgeManager$BundType r0 = com.taobao.weex.bridge.WXBridgeManager.BundType.Rax     // Catch:{ Throwable -> 0x037d }
            if (r4 == r0) goto L_0x02ce
            boolean r0 = r17.isUsingEaglePlugin()     // Catch:{ Throwable -> 0x037d }
            if (r0 != 0) goto L_0x02ce
            com.taobao.weex.common.WXRenderStrategy r0 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r1 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ Throwable -> 0x037d }
            if (r0 == r1) goto L_0x02ce
            com.taobao.weex.common.WXRenderStrategy r0 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r1 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Throwable -> 0x037d }
            if (r0 == r1) goto L_0x02ce
            com.taobao.weex.common.WXRenderStrategy r0 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r1 = com.taobao.weex.common.WXRenderStrategy.JSON_RENDER     // Catch:{ Throwable -> 0x037d }
            if (r0 != r1) goto L_0x0281
            goto L_0x02ce
        L_0x0281:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x037d }
            r0.<init>()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "Instance "
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = r17.getInstanceId()     // Catch:{ Throwable -> 0x037d }
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "Did not Render in SandBox Mode And Render Type is "
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            r0.append(r4)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = " Render Strategy is "
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r1 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.utils.WXLogUtils.d(r0)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.performance.WXInstanceApm r0 = r17.getApmForInstance()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "StartInvokeExecJSBadBundleType"
            r0.onStage(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = r17.getInstanceId()     // Catch:{ Throwable -> 0x037d }
            r3 = 0
            java.lang.String r4 = "createInstance"
            r6 = 0
            r1 = r16
            r5 = r14
            r1.invokeExecJS(r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.performance.WXInstanceApm r0 = r17.getApmForInstance()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "wxEndLoadBundle"
            r0.onStage(r1)     // Catch:{ Throwable -> 0x037d }
            return
        L_0x02ce:
            com.taobao.weex.performance.WXInstanceApm r0 = r17.getApmForInstance()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "wxBeforeInvokeCreateInstanceContext"
            r0.onStage(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x037d }
            r0.<init>()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "Instance "
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = r17.getInstanceId()     // Catch:{ Throwable -> 0x037d }
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = " Render in SandBox Mode And Render Type is "
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            r0.append(r4)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = " Render Strategy is "
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXRenderStrategy r1 = r17.getRenderStrategy()     // Catch:{ Throwable -> 0x037d }
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.utils.WXLogUtils.d(r0)     // Catch:{ Throwable -> 0x037d }
            int r0 = r14.length     // Catch:{ Throwable -> 0x037d }
            int r0 = r0 + r5
            java.lang.Object[] r0 = java.util.Arrays.copyOf(r14, r0)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.bridge.WXJSObject[] r0 = (com.taobao.weex.bridge.WXJSObject[]) r0     // Catch:{ Throwable -> 0x037d }
            int r1 = r0.length     // Catch:{ Throwable -> 0x037d }
            int r1 = r1 - r5
            r0[r1] = r6     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = r17.getInstanceId()     // Catch:{ Throwable -> 0x037d }
            r3 = 0
            java.lang.String r4 = "createInstanceContext"
            r6 = 0
            r1 = r16
            r5 = r0
            int r0 = r1.invokeCreateInstanceContext(r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.performance.WXInstanceApm r1 = r17.getApmForInstance()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = "wxEndLoadBundle"
            r1.onStage(r2)     // Catch:{ Throwable -> 0x037d }
            if (r0 != 0) goto L_0x037c
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x037d }
            r0.<init>()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = "[WXBridgeManager] invokeCreateInstance : "
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = r17.getTemplateInfo()     // Catch:{ Throwable -> 0x037d }
            r0.append(r1)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x037d }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x037d }
            r1.<init>()     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = "Instance "
            r1.append(r2)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = r17.getInstanceId()     // Catch:{ Throwable -> 0x037d }
            r1.append(r2)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r2 = "Render error : "
            r1.append(r2)     // Catch:{ Throwable -> 0x037d }
            r1.append(r0)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.utils.WXLogUtils.e(r1)     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXErrorCode r1 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED     // Catch:{ Throwable -> 0x037d }
            java.lang.String r1 = r1.getErrorCode()     // Catch:{ Throwable -> 0x037d }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x037d }
            r2.<init>()     // Catch:{ Throwable -> 0x037d }
            com.taobao.weex.common.WXErrorCode r3 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED     // Catch:{ Throwable -> 0x037d }
            java.lang.String r3 = r3.getErrorMsg()     // Catch:{ Throwable -> 0x037d }
            r2.append(r3)     // Catch:{ Throwable -> 0x037d }
            r2.append(r0)     // Catch:{ Throwable -> 0x037d }
            java.lang.String r0 = r2.toString()     // Catch:{ Throwable -> 0x037d }
            r8.onRenderError(r1, r0)     // Catch:{ Throwable -> 0x037d }
        L_0x037c:
            return
        L_0x037d:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "[WXBridgeManager] invokeCreateInstance "
            r1.append(r2)
            java.lang.Throwable r2 = r0.getCause()
            r1.append(r2)
            java.lang.String r2 = r17.getTemplateInfo()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.taobao.weex.performance.WXInstanceApm r2 = r17.getApmForInstance()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "createInstance error :"
            r3.append(r4)
            java.lang.String r0 = r0.toString()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2.onStage(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "Instance "
            r0.append(r2)
            java.lang.String r2 = r17.getInstanceId()
            r0.append(r2)
            java.lang.String r2 = "Render error : "
            r0.append(r2)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.taobao.weex.utils.WXLogUtils.e(r0)
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED
            java.lang.String r0 = r0.getErrorCode()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            com.taobao.weex.common.WXErrorCode r3 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED
            java.lang.String r3 = r3.getErrorMsg()
            r2.append(r3)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            r8.onRenderError(r0, r2)
            com.taobao.weex.utils.WXLogUtils.e(r1)
        L_0x03f7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.invokeCreateInstance(com.taobao.weex.WXSDKInstance, com.taobao.weex.Script, java.util.Map, java.lang.String):void");
    }

    public WXJSObject optionObjConvert(boolean z, BundType bundType, WXJSObject wXJSObject) {
        JSONObject jSONObject;
        if (!z) {
            return wXJSObject;
        }
        try {
            JSONObject parseObject = JSON.parseObject(wXJSObject.data.toString());
            JSONObject jSONObject2 = parseObject.getJSONObject("env");
            if (!(jSONObject2 == null || (jSONObject = jSONObject2.getJSONObject("options")) == null)) {
                for (String next : jSONObject.keySet()) {
                    jSONObject2.put(next, (Object) jSONObject.getString(next));
                }
            }
            return new WXJSObject(3, parseObject.toString());
        } catch (Throwable th) {
            WXLogUtils.e(WXLogUtils.getStackTrace(th));
            return wXJSObject;
        }
    }

    public BundType getBundleType(String str, String str2) {
        if (str != null) {
            try {
                String queryParameter = Uri.parse(str).getQueryParameter(BUNDLE_TYPE);
                if (!"Vue".equals(queryParameter)) {
                    if (!"vue".equals(queryParameter)) {
                        if ("Rax".equals(queryParameter) || "rax".equals(queryParameter)) {
                            return BundType.Rax;
                        }
                    }
                }
                return BundType.Vue;
            } catch (Throwable th) {
                WXLogUtils.e(WXLogUtils.getStackTrace(th));
                return BundType.Others;
            }
        }
        if (str2 != null) {
            int indexOf = str2.indexOf("//");
            String string = JSONObject.parseObject(str2.substring(indexOf + 2, str2.indexOf("\n", indexOf))).getString("framework");
            if ("vue".equalsIgnoreCase(string)) {
                return BundType.Vue;
            }
            if ("rax".equalsIgnoreCase(string)) {
                return BundType.Rax;
            }
            if (Pattern.compile("(use)(\\s+)(weex:vue)", 2).matcher(str2).find()) {
                return BundType.Vue;
            }
            if (Pattern.compile("(use)(\\s+)(weex:rax)", 2).matcher(str2).find()) {
                return BundType.Rax;
            }
        }
        return BundType.Others;
    }

    public void destroyInstance(final String str) {
        if (this.mJSHandler != null && !TextUtils.isEmpty(str)) {
            if (this.mDestroyedInstanceId != null) {
                this.mDestroyedInstanceId.add(str);
            }
            this.mJSHandler.removeCallbacksAndMessages(str);
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.removeTaskByInstance(str);
                    WXBridgeManager.this.invokeDestroyInstance(str);
                }
            }, str, (WXSDKInstance) null, METHOD_DESTROY_INSTANCE);
        }
    }

    /* access modifiers changed from: private */
    public void removeTaskByInstance(String str) {
        this.mNextTickTasks.removeFromMapAndStack(str);
    }

    /* access modifiers changed from: private */
    public void invokeDestroyInstance(String str) {
        try {
            WXEnvironment.isApkDebugable();
            WXJSObject[] wXJSObjectArr = {new WXJSObject(2, str)};
            if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
                invokeDestoryInstance(str, (String) null, METHOD_DESTROY_INSTANCE, wXJSObjectArr, true);
            }
        } catch (Throwable th) {
            String str2 = "[WXBridgeManager] invokeDestroyInstance " + th.getCause();
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "invokeDestroyInstance", str2, (Map<String, String>) null);
            WXLogUtils.e(str2);
        }
    }

    public boolean handleMessage(Message message) {
        if (message == null) {
            return false;
        }
        int i = message.what;
        if (i == 1) {
            TimerInfo timerInfo = (TimerInfo) message.obj;
            if (timerInfo != null) {
                invokeExecJS("", (String) null, METHOD_SET_TIMEOUT, new WXJSObject[]{new WXJSObject(2, timerInfo.callbackId)});
            }
        } else if (i != 13) {
            switch (i) {
                case 6:
                    invokeCallJSBatch(message);
                    break;
                case 7:
                    invokeInitFramework(message);
                    break;
            }
        } else if (message.obj != null) {
            this.mWXBridge.takeHeapSnapshot((String) message.obj);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void invokeExecJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        invokeExecJS(str, str2, str3, wXJSObjectArr, true);
    }

    public void invokeExecJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, boolean z) {
        String str4 = str;
        WXEnvironment.isOpenDebugLog();
        long currentTimeMillis = System.currentTimeMillis();
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str4);
        if (sDKInstance != null && sDKInstance.isUsingEaglePlugin()) {
            final WXEaglePlugin eaglePlugin = sDKInstance.getEaglePlugin();
            int isSupportInvokeExecJS = eaglePlugin.isSupportInvokeExecJS(str4);
            if (isSupportInvokeExecJS != 0) {
                final String str5 = str;
                final String str6 = str2;
                final String str7 = str3;
                final WXJSObject[] wXJSObjectArr2 = wXJSObjectArr;
                WXThread.secure(new Runnable() {
                    public void run() {
                        eaglePlugin.invokeExecJS(str5, str6, str7, wXJSObjectArr2);
                    }
                }, sDKInstance, "ExecJsEagle").run();
                if (isSupportInvokeExecJS == 1) {
                    if (sDKInstance != null) {
                        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                        sDKInstance.getApmForInstance().updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_CALL_JS_NUM, 1.0d);
                        sDKInstance.getApmForInstance().updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_CALL_JS_TIME, (double) currentTimeMillis2);
                        sDKInstance.callJsTime(currentTimeMillis2);
                        return;
                    }
                    return;
                }
            } else {
                return;
            }
        }
        final String str8 = str;
        final String str9 = str2;
        final String str10 = str3;
        final WXJSObject[] wXJSObjectArr3 = wXJSObjectArr;
        WXThread.secure(new Runnable() {
            public void run() {
                WXBridgeManager.this.mWXBridge.execJS(str8, str9, str10, wXJSObjectArr3);
            }
        }, sDKInstance, "ExecJs").run();
        if (sDKInstance != null) {
            long currentTimeMillis3 = System.currentTimeMillis() - currentTimeMillis;
            sDKInstance.getApmForInstance().updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_CALL_JS_NUM, 1.0d);
            sDKInstance.getApmForInstance().updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_CALL_JS_TIME, (double) currentTimeMillis3);
            sDKInstance.callJsTime(currentTimeMillis3);
        }
    }

    private Pair<Pair<String, Object>, Boolean> extractCallbackArgs(String str) {
        try {
            JSONObject jSONObject = JSON.parseArray(str).getJSONObject(0);
            JSONArray jSONArray = jSONObject.getJSONArray("args");
            if (jSONArray.size() == 3 && "callback".equals(jSONObject.getString("method"))) {
                return new Pair<>(new Pair(jSONArray.getString(0), jSONArray.getJSONObject(1)), Boolean.valueOf(jSONArray.getBooleanValue(2)));
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public int invokeCreateInstanceContext(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, boolean z) {
        WXLogUtils.d("invokeCreateInstanceContext instanceId:" + str + " function:" + str3 + String.format(" isJSFrameworkInit：%b", new Object[]{Boolean.valueOf(isJSFrameworkInit())}));
        StringBuilder sb = this.mLodBuilder;
        sb.append("createInstanceContext >>>> instanceId:");
        sb.append(str);
        sb.append("function:");
        sb.append(str3);
        if (z) {
            StringBuilder sb2 = this.mLodBuilder;
            sb2.append(" tasks:");
            sb2.append(WXJsonUtils.fromObjectToJSONString(wXJSObjectArr));
        }
        WXLogUtils.d(this.mLodBuilder.substring(0));
        this.mLodBuilder.setLength(0);
        return this.mWXBridge.createInstanceContext(str, str2, str3, wXJSObjectArr);
    }

    public void invokeDestoryInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, boolean z) {
        StringBuilder sb = this.mLodBuilder;
        sb.append("callJS >>>> instanceId:");
        sb.append(str);
        sb.append("function:");
        sb.append(str3);
        if (z) {
            StringBuilder sb2 = this.mLodBuilder;
            sb2.append(" tasks:");
            sb2.append(WXJsonUtils.fromObjectToJSONString(wXJSObjectArr));
        }
        WXLogUtils.d(this.mLodBuilder.substring(0));
        this.mLodBuilder.setLength(0);
        this.mWXBridge.removeInstanceRenderType(str);
        this.mWXBridge.destoryInstance(str, str2, str3, wXJSObjectArr);
    }

    private void execJSOnInstance(EventResult eventResult, String str, String str2, int i) {
        final String str3 = str;
        final String str4 = str2;
        final int i2 = i;
        final EventResult eventResult2 = eventResult;
        post(new Runnable() {
            public void run() {
                eventResult2.onCallback(WXBridgeManager.this.invokeExecJSOnInstance(str3, str4, i2));
            }
        });
    }

    /* access modifiers changed from: private */
    public String invokeExecJSOnInstance(String str, String str2, int i) {
        StringBuilder sb = this.mLodBuilder;
        sb.append("execJSOnInstance >>>> instanceId:");
        sb.append(str);
        WXLogUtils.d(this.mLodBuilder.substring(0));
        this.mLodBuilder.setLength(0);
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            return this.mWXBridge.execJSOnInstance(str, str2, i);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void invokeExecJSWithCallback(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, ResultCallback resultCallback, boolean z) {
        WXEnvironment.isOpenDebugLog();
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.execJSWithCallback(str, str2, str3, wXJSObjectArr, resultCallback);
        }
    }

    @NonNull
    public static String argsToJSON(WXJSObject[] wXJSObjectArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        for (WXJSObject fromObjectToJSONString : wXJSObjectArr) {
            sb.append(WXWsonJSONSwitch.fromObjectToJSONString(fromObjectToJSONString));
            sb.append(",");
        }
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void invokeInitFramework(android.os.Message r6) {
        /*
            r5 = this;
            java.lang.String r0 = ""
            java.lang.Object r1 = r6.obj
            if (r1 == 0) goto L_0x000b
            java.lang.Object r6 = r6.obj
            r0 = r6
            java.lang.String r0 = (java.lang.String) r0
        L_0x000b:
            android.app.Application r6 = com.taobao.weex.WXEnvironment.getApplication()
            long r1 = com.taobao.weex.utils.WXUtils.getAvailMemory(r6)
            long r3 = LOW_MEM_VALUE
            int r6 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r6 <= 0) goto L_0x001c
            r5.initFramework(r0)
        L_0x001c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.invokeInitFramework(android.os.Message):void");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x010c, code lost:
        if (android.os.Build.VERSION.SDK_INT < 16) goto L_0x0118;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initFramework(java.lang.String r11) {
        /*
            r10 = this;
            com.taobao.weex.utils.tools.LogDetail r0 = new com.taobao.weex.utils.tools.LogDetail
            r0.<init>()
            java.lang.String r1 = "initFramework"
            r0.name(r1)
            r0.taskStart()
            boolean r1 = com.taobao.weex.WXSDKEngine.isSoInitialized()
            if (r1 == 0) goto L_0x01e8
            boolean r1 = r10.isJSFrameworkInit()
            if (r1 != 0) goto L_0x01e8
            long r1 = java.lang.System.currentTimeMillis()
            sInitFrameWorkTimeOrigin = r1
            boolean r1 = android.text.TextUtils.isEmpty(r11)
            if (r1 == 0) goto L_0x007b
            java.lang.String r1 = "weex JS framework from assets"
            com.taobao.weex.utils.WXLogUtils.d(r1)
            com.taobao.weex.utils.tools.LogDetail r1 = new com.taobao.weex.utils.tools.LogDetail
            r1.<init>()
            java.lang.String r2 = "loadJSFramework"
            r1.name(r2)
            r1.taskStart()
            com.taobao.weex.adapter.IWXJsFileLoaderAdapter r2 = com.taobao.weex.WXSDKEngine.getIWXJsFileLoaderAdapter()
            boolean r3 = isSandBoxContext
            if (r3 != 0) goto L_0x0056
            if (r2 == 0) goto L_0x0045
            java.lang.String r11 = r2.loadJsFramework()
        L_0x0045:
            boolean r2 = android.text.TextUtils.isEmpty(r11)
            if (r2 == 0) goto L_0x006c
            java.lang.String r11 = "main.js"
            android.app.Application r2 = com.taobao.weex.WXEnvironment.getApplication()
            java.lang.String r11 = com.taobao.weex.utils.WXFileUtils.loadAsset(r11, r2)
            goto L_0x006c
        L_0x0056:
            if (r2 == 0) goto L_0x005c
            java.lang.String r11 = r2.loadJsFrameworkForSandBox()
        L_0x005c:
            boolean r2 = android.text.TextUtils.isEmpty(r11)
            if (r2 == 0) goto L_0x006c
            java.lang.String r11 = "weex-main-jsfm.js"
            android.app.Application r2 = com.taobao.weex.WXEnvironment.getApplication()
            java.lang.String r11 = com.taobao.weex.utils.WXFileUtils.loadAsset(r11, r2)
        L_0x006c:
            java.lang.StringBuilder r2 = sInitFrameWorkMsg
            java.lang.String r3 = "| weex JS framework from assets, isSandBoxContext: "
            r2.append(r3)
            boolean r3 = isSandBoxContext
            r2.append(r3)
            r1.taskEnd()
        L_0x007b:
            boolean r1 = android.text.TextUtils.isEmpty(r11)
            r2 = 0
            if (r1 == 0) goto L_0x0097
            r10.setJSFrameworkInit(r2)
            java.lang.StringBuilder r11 = sInitFrameWorkMsg
            java.lang.String r0 = "| framework isEmpty "
            r11.append(r0)
            com.taobao.weex.common.WXErrorCode r11 = com.taobao.weex.common.WXErrorCode.WX_ERR_JS_FRAMEWORK
            java.lang.String r0 = "initFramework"
            java.lang.String r1 = "framework is empty!! "
            r2 = 0
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r2, r11, r0, r1, r2)
            return
        L_0x0097:
            r1 = 1
            com.taobao.weex.WXSDKManager r3 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.IWXStatisticsListener r3 = r3.getWXStatisticsListener()     // Catch:{ Throwable -> 0x01ca }
            if (r3 == 0) goto L_0x00e8
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.WXSDKManager r5 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.IWXStatisticsListener r5 = r5.getWXStatisticsListener()     // Catch:{ Throwable -> 0x01ca }
            r5.onJsFrameworkStart()     // Catch:{ Throwable -> 0x01ca }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01ca }
            r7 = 0
            long r5 = r5 - r3
            com.taobao.weex.WXEnvironment.sJSFMStartListenerTime = r5     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.WXSDKManager r3 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x00e0 }
            com.taobao.weex.adapter.IWXUserTrackAdapter r4 = r3.getIWXUserTrackAdapter()     // Catch:{ Exception -> 0x00e0 }
            if (r4 == 0) goto L_0x00e8
            java.util.HashMap r9 = new java.util.HashMap     // Catch:{ Exception -> 0x00e0 }
            r9.<init>(r1)     // Catch:{ Exception -> 0x00e0 }
            java.lang.String r3 = "time"
            long r5 = com.taobao.weex.WXEnvironment.sJSFMStartListenerTime     // Catch:{ Exception -> 0x00e0 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x00e0 }
            r9.put(r3, r5)     // Catch:{ Exception -> 0x00e0 }
            android.app.Application r5 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Exception -> 0x00e0 }
            java.lang.String r6 = "sJSFMStartListener"
            java.lang.String r7 = "counter"
            r8 = 0
            r4.commit(r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x00e0 }
            goto L_0x00e8
        L_0x00e0:
            r3 = move-exception
            java.lang.String r3 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r3)     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.utils.WXLogUtils.e(r3)     // Catch:{ Throwable -> 0x01ca }
        L_0x00e8:
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r5 = ""
            android.app.Application r6 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Exception -> 0x0100 }
            android.content.Context r6 = r6.getApplicationContext()     // Catch:{ Exception -> 0x0100 }
            java.io.File r6 = r6.getCacheDir()     // Catch:{ Exception -> 0x0100 }
            java.lang.String r6 = r6.getPath()     // Catch:{ Exception -> 0x0100 }
            r5 = r6
            goto L_0x0108
        L_0x0100:
            r6 = move-exception
            java.lang.String r6 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r6)     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.utils.WXLogUtils.e(r6)     // Catch:{ Throwable -> 0x01ca }
        L_0x0108:
            int r6 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x010f }
            r7 = 16
            if (r6 >= r7) goto L_0x0117
            goto L_0x0118
        L_0x010f:
            r2 = move-exception
            java.lang.String r2 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r2)     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.utils.WXLogUtils.e(r2)     // Catch:{ Throwable -> 0x01ca }
        L_0x0117:
            r2 = 1
        L_0x0118:
            java.lang.StringBuilder r6 = sInitFrameWorkMsg     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r7 = " | pieSupport:"
            r6.append(r7)     // Catch:{ Throwable -> 0x01ca }
            r6.append(r2)     // Catch:{ Throwable -> 0x01ca }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01ca }
            r6.<init>()     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r7 = "[WXBridgeManager] initFrameworkEnv crashFile:"
            r6.append(r7)     // Catch:{ Throwable -> 0x01ca }
            r6.append(r5)     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r7 = " pieSupport:"
            r6.append(r7)     // Catch:{ Throwable -> 0x01ca }
            r6.append(r2)     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.utils.WXLogUtils.d(r6)     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.utils.tools.LogDetail r6 = new com.taobao.weex.utils.tools.LogDetail     // Catch:{ Throwable -> 0x01ca }
            r6.<init>()     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r7 = "native initFrameworkEnv"
            r6.name(r7)     // Catch:{ Throwable -> 0x01ca }
            r6.taskStart()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.common.IWXBridge r7 = r10.mWXBridge     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.bridge.WXParams r8 = r10.assembleDefaultOptions()     // Catch:{ Throwable -> 0x01ca }
            int r11 = r7.initFrameworkEnv(r11, r8, r5, r2)     // Catch:{ Throwable -> 0x01ca }
            if (r11 != r1) goto L_0x01ae
            r6.taskEnd()     // Catch:{ Throwable -> 0x01ca }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01ca }
            r11 = 0
            long r5 = r5 - r3
            com.taobao.weex.WXEnvironment.sJSLibInitTime = r5     // Catch:{ Throwable -> 0x01ca }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01ca }
            long r4 = com.taobao.weex.WXEnvironment.sSDKInitStart     // Catch:{ Throwable -> 0x01ca }
            r11 = 0
            long r2 = r2 - r4
            com.taobao.weex.WXEnvironment.sSDKInitTime = r2     // Catch:{ Throwable -> 0x01ca }
            r10.setJSFrameworkInit(r1)     // Catch:{ Throwable -> 0x01ca }
            r0.taskEnd()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.WXSDKManager r11 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.IWXStatisticsListener r11 = r11.getWXStatisticsListener()     // Catch:{ Throwable -> 0x01ca }
            if (r11 == 0) goto L_0x0187
            com.taobao.weex.WXSDKManager r11 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.IWXStatisticsListener r11 = r11.getWXStatisticsListener()     // Catch:{ Throwable -> 0x01ca }
            r11.onJsFrameworkReady()     // Catch:{ Throwable -> 0x01ca }
        L_0x0187:
            r10.execRegisterFailTask()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.WXEnvironment.JsFrameworkInit = r1     // Catch:{ Throwable -> 0x01ca }
            int r11 = sInitFrameWorkCount     // Catch:{ Throwable -> 0x01ca }
            int r11 = r11 + r1
            sInitFrameWorkCount = r11     // Catch:{ Throwable -> 0x01ca }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01ca }
            r11.<init>()     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r0 = "initFrameWorkCount :"
            r11.append(r0)     // Catch:{ Throwable -> 0x01ca }
            int r0 = sInitFrameWorkCount     // Catch:{ Throwable -> 0x01ca }
            r11.append(r0)     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r11 = r11.toString()     // Catch:{ Throwable -> 0x01ca }
            com.taobao.weex.utils.WXLogUtils.e(r11)     // Catch:{ Throwable -> 0x01ca }
            r10.registerDomModule()     // Catch:{ Throwable -> 0x01ca }
            r10.trackComponentAndModulesTime()     // Catch:{ Throwable -> 0x01ca }
            goto L_0x01e8
        L_0x01ae:
            java.lang.StringBuilder r11 = sInitFrameWorkMsg     // Catch:{ Throwable -> 0x01ca }
            java.lang.String r0 = " | ExecuteJavaScript fail, reInitCount"
            r11.append(r0)     // Catch:{ Throwable -> 0x01ca }
            int r0 = reInitCount     // Catch:{ Throwable -> 0x01ca }
            r11.append(r0)     // Catch:{ Throwable -> 0x01ca }
            int r11 = reInitCount     // Catch:{ Throwable -> 0x01ca }
            if (r11 <= r1) goto L_0x01c4
            java.lang.String r11 = "[WXBridgeManager] invokeReInitFramework  ExecuteJavaScript fail"
            com.taobao.weex.utils.WXLogUtils.e(r11)     // Catch:{ Throwable -> 0x01ca }
            goto L_0x01e8
        L_0x01c4:
            java.lang.String r11 = "[WXBridgeManager] invokeInitFramework  ExecuteJavaScript fail"
            com.taobao.weex.utils.WXLogUtils.e(r11)     // Catch:{ Throwable -> 0x01ca }
            goto L_0x01e8
        L_0x01ca:
            r11 = move-exception
            java.lang.StringBuilder r0 = sInitFrameWorkMsg
            java.lang.String r2 = " | invokeInitFramework exception "
            r0.append(r2)
            java.lang.String r2 = r11.toString()
            r0.append(r2)
            int r0 = reInitCount
            if (r0 <= r1) goto L_0x01e3
            java.lang.String r0 = "[WXBridgeManager] invokeInitFramework "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r11)
            goto L_0x01e8
        L_0x01e3:
            java.lang.String r0 = "[WXBridgeManager] invokeInitFramework "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r11)
        L_0x01e8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.initFramework(java.lang.String):void");
    }

    private void trackComponentAndModulesTime() {
        post(new Runnable() {
            public void run() {
                WXEnvironment.sComponentsAndModulesReadyTime = System.currentTimeMillis() - WXEnvironment.sSDKInitStart;
            }
        });
    }

    private void invokeCallJSBatch(Message message) {
        if (!this.mNextTickTasks.isEmpty() && isJSFrameworkInit()) {
            try {
                Object obj = message.obj;
                Stack<String> instanceStack = this.mNextTickTasks.getInstanceStack();
                int size = instanceStack.size() - 1;
                ArrayList<WXHashMap<String, Object>> arrayList = null;
                while (true) {
                    if (size >= 0) {
                        obj = instanceStack.get(size);
                        arrayList = this.mNextTickTasks.remove(obj);
                        if (arrayList != null && !arrayList.isEmpty()) {
                            break;
                        }
                        size--;
                    } else {
                        break;
                    }
                }
                if (arrayList != null) {
                    invokeExecJS(String.valueOf(obj), (String) null, METHOD_CALL_JS, new WXJSObject[]{new WXJSObject(2, obj), WXWsonJSONSwitch.toWsonOrJsonWXJSObject(arrayList.toArray())});
                }
            } catch (Throwable th) {
                WXLogUtils.e("WXBridgeManager", th);
                WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_ERR_JS_FRAMEWORK, "invokeCallJSBatch", "invokeCallJSBatch#" + WXLogUtils.getStackTrace(th), (Map<String, String>) null);
            }
            if (!this.mNextTickTasks.isEmpty()) {
                this.mJSHandler.sendEmptyMessage(6);
            }
        } else if (!isJSFrameworkInit()) {
            WXLogUtils.e("[WXBridgeManager] invokeCallJSBatch: framework.js uninitialized!!  message:" + message.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0180  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.taobao.weex.bridge.WXParams assembleDefaultOptions() {
        /*
            r7 = this;
            r7.checkJsEngineMultiThread()
            java.util.Map r0 = com.taobao.weex.WXEnvironment.getConfig()
            com.taobao.weex.bridge.WXParams r1 = new com.taobao.weex.bridge.WXParams
            r1.<init>()
            java.lang.String r2 = "os"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setPlatform(r2)
            java.lang.String r2 = "cacheDir"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setCacheDir(r2)
            java.lang.String r2 = "sysVersion"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setOsVersion(r2)
            java.lang.String r2 = "appVersion"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setAppVersion(r2)
            java.lang.String r2 = "weexVersion"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setWeexVersion(r2)
            java.lang.String r2 = "sysModel"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setDeviceModel(r2)
            java.lang.String r2 = "infoCollect"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setShouldInfoCollect(r2)
            java.lang.String r2 = "logLevel"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setLogLevel(r2)
            java.lang.String r2 = "layoutDirection"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r1.setLayoutDirection(r2)
            boolean r2 = isUseSingleProcess
            if (r2 == 0) goto L_0x0076
            java.lang.String r2 = "true"
            goto L_0x0078
        L_0x0076:
            java.lang.String r2 = "false"
        L_0x0078:
            r1.setUseSingleProcess(r2)
            android.app.Application r2 = com.taobao.weex.WXEnvironment.getApplication()
            android.content.Context r2 = r2.getApplicationContext()
            java.lang.String r2 = com.taobao.weex.WXEnvironment.getCrashFilePath(r2)
            r1.setCrashFilePath(r2)
            java.lang.String r2 = com.taobao.weex.WXEnvironment.CORE_JSB_SO_PATH
            r1.setLibJsbPath(r2)
            java.lang.String r2 = com.taobao.weex.WXEnvironment.getLibJssRealPath()
            r1.setLibJssPath(r2)
            java.lang.String r2 = com.taobao.weex.WXEnvironment.getLibJssIcuPath()
            r1.setLibIcuPath(r2)
            java.lang.String r2 = com.taobao.weex.WXEnvironment.getLibLdPath()
            r1.setLibLdPath(r2)
            java.lang.String r2 = com.taobao.weex.WXEnvironment.getLibJScRealPath()
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 == 0) goto L_0x00b1
            java.lang.String r2 = ""
            goto L_0x00ba
        L_0x00b1:
            java.io.File r3 = new java.io.File
            r3.<init>(r2)
            java.lang.String r2 = r3.getParent()
        L_0x00ba:
            r1.setLibJscPath(r2)
            java.lang.String r2 = "appName"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x00ce
            r1.setAppName(r2)
        L_0x00ce:
            java.lang.String r2 = "deviceWidth"
            java.lang.Object r2 = r0.get(r2)
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x00e7
            android.app.Application r2 = com.taobao.weex.WXEnvironment.sApplication
            int r2 = com.taobao.weex.utils.WXViewUtils.getScreenWidth(r2)
            java.lang.String r2 = java.lang.String.valueOf(r2)
            goto L_0x00ef
        L_0x00e7:
            java.lang.String r2 = "deviceWidth"
            java.lang.Object r2 = r0.get(r2)
            java.lang.String r2 = (java.lang.String) r2
        L_0x00ef:
            r1.setDeviceWidth(r2)
            java.lang.String r2 = "deviceHeight"
            java.lang.Object r2 = r0.get(r2)
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x010b
            android.app.Application r0 = com.taobao.weex.WXEnvironment.sApplication
            int r0 = com.taobao.weex.utils.WXViewUtils.getScreenHeight((android.content.Context) r0)
            java.lang.String r0 = java.lang.String.valueOf(r0)
            goto L_0x0113
        L_0x010b:
            java.lang.String r2 = "deviceHeight"
            java.lang.Object r0 = r0.get(r2)
            java.lang.String r0 = (java.lang.String) r0
        L_0x0113:
            r1.setDeviceHeight(r0)
            java.util.Map r0 = com.taobao.weex.WXEnvironment.getCustomOptions()
            java.lang.String r2 = "enableBackupThread"
            boolean r3 = r7.jsEngineMultiThreadEnable()
            java.lang.String r3 = java.lang.String.valueOf(r3)
            r0.put(r2, r3)
            com.taobao.weex.WXSDKManager r2 = com.taobao.weex.WXSDKManager.getInstance()
            com.taobao.weex.adapter.IWXJscProcessManager r2 = r2.getWXJscProcessManager()
            if (r2 == 0) goto L_0x013e
            java.lang.String r3 = "enableBackupThreadCache"
            boolean r2 = r2.enableBackUpThreadCache()
            java.lang.String r2 = java.lang.String.valueOf(r2)
            r0.put(r3, r2)
        L_0x013e:
            boolean r2 = com.taobao.weex.WXEnvironment.sUseRunTimeApi
            if (r2 != 0) goto L_0x0149
            java.lang.String r2 = "__enable_native_promise__"
            java.lang.String r3 = "true"
            r0.put(r2, r3)
        L_0x0149:
            java.lang.String r2 = "true"
            com.taobao.weex.WXSDKManager r3 = com.taobao.weex.WXSDKManager.getInstance()
            com.taobao.weex.adapter.IWXConfigAdapter r3 = r3.getWxConfigAdapter()
            if (r3 == 0) goto L_0x017e
            java.lang.String r4 = "white_screen_fix_open"
            boolean r4 = r3.checkMode(r4)     // Catch:{ Exception -> 0x017a }
            if (r4 == 0) goto L_0x0164
            r3 = 1
            com.taobao.weex.WXEnvironment.isWsFixMode = r3     // Catch:{ Exception -> 0x017a }
            java.lang.String r3 = "true"
        L_0x0162:
            r2 = r3
            goto L_0x017e
        L_0x0164:
            java.lang.String r4 = "wxapm"
            java.lang.String r5 = "enableAlarmSignal"
            java.lang.String r3 = r3.getConfigWhenInit(r4, r5, r2)     // Catch:{ Exception -> 0x017a }
            java.lang.String r2 = "true"
            boolean r2 = r2.equalsIgnoreCase(r3)     // Catch:{ Exception -> 0x0175 }
            com.taobao.weex.WXEnvironment.isWsFixMode = r2     // Catch:{ Exception -> 0x0175 }
            goto L_0x0162
        L_0x0175:
            r2 = move-exception
            r6 = r3
            r3 = r2
            r2 = r6
            goto L_0x017b
        L_0x017a:
            r3 = move-exception
        L_0x017b:
            r3.printStackTrace()
        L_0x017e:
            if (r2 == 0) goto L_0x0185
            java.lang.String r3 = "enableAlarmSignal"
            r0.put(r3, r2)
        L_0x0185:
            java.lang.String r3 = "weex"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "enableAlarmSignal:"
            r4.append(r5)
            r4.append(r2)
            java.lang.String r2 = r4.toString()
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.String) r2)
            r1.setOptions(r0)
            com.taobao.weex.WXSDKManager r0 = com.taobao.weex.WXSDKManager.getInstance()
            boolean r0 = r0.needInitV8()
            r1.setNeedInitV8(r0)
            r7.mInitParams = r1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.assembleDefaultOptions():com.taobao.weex.bridge.WXParams");
    }

    public WXParams getInitParams() {
        return this.mInitParams;
    }

    private void execRegisterFailTask() {
        if (this.mRegisterModuleFailList.size() > 0) {
            ArrayList arrayList = new ArrayList();
            int size = this.mRegisterModuleFailList.size();
            for (int i = 0; i < size; i++) {
                invokeRegisterModules(this.mRegisterModuleFailList.get(i), arrayList);
            }
            this.mRegisterModuleFailList.clear();
            if (arrayList.size() > 0) {
                this.mRegisterModuleFailList.addAll(arrayList);
            }
        }
        if (this.mRegisterComponentFailList.size() > 0) {
            ArrayList arrayList2 = new ArrayList();
            invokeRegisterComponents(this.mRegisterComponentFailList, arrayList2);
            this.mRegisterComponentFailList.clear();
            if (arrayList2.size() > 0) {
                this.mRegisterComponentFailList.addAll(arrayList2);
            }
        }
        if (this.mRegisterServiceFailList.size() > 0) {
            ArrayList arrayList3 = new ArrayList();
            for (String invokeExecJSService : this.mRegisterServiceFailList) {
                invokeExecJSService(invokeExecJSService, arrayList3);
            }
            this.mRegisterServiceFailList.clear();
            if (arrayList3.size() > 0) {
                this.mRegisterServiceFailList.addAll(arrayList3);
            }
        }
    }

    public void registerModules(final Map<String, Object> map) {
        if (map != null && map.size() != 0) {
            if (isJSThread()) {
                invokeRegisterModules(map, this.mRegisterModuleFailList);
            } else {
                post(new Runnable() {
                    public void run() {
                        WXBridgeManager.this.invokeRegisterModules(map, WXBridgeManager.this.mRegisterModuleFailList);
                    }
                });
            }
        }
    }

    public void registerComponents(final List<Map<String, Object>> list) {
        if (this.mJSHandler != null && list != null && list.size() != 0) {
            AnonymousClass25 r0 = new Runnable() {
                public void run() {
                    WXBridgeManager.this.invokeRegisterComponents(list, WXBridgeManager.this.mRegisterComponentFailList);
                }
            };
            if (!isJSThread() || !isJSFrameworkInit()) {
                post(r0);
            } else {
                r0.run();
            }
        }
    }

    public void execJSService(final String str) {
        postWithName(new Runnable() {
            public void run() {
                WXBridgeManager.this.invokeExecJSService(str, WXBridgeManager.this.mRegisterServiceFailList);
            }
        }, (WXSDKInstance) null, "execJSService");
    }

    /* access modifiers changed from: private */
    public void invokeExecJSService(String str, List<String> list) {
        try {
            if (!isJSFrameworkInit()) {
                WXLogUtils.e("[WXBridgeManager] invoke execJSService: framework.js uninitialized.");
                list.add(str);
                return;
            }
            this.mWXBridge.execJSService(str);
        } catch (Throwable th) {
            WXLogUtils.e("[WXBridgeManager] invokeRegisterService:", th);
            HashMap hashMap = new HashMap();
            hashMap.put("inputParams", str + "||" + list.toString());
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_INVOKE_JSSERVICE_EXECUTE;
            WXExceptionUtils.commitCriticalExceptionRT("invokeExecJSService", wXErrorCode, "invokeExecJSService", WXErrorCode.WX_KEY_EXCEPTION_INVOKE_JSSERVICE_EXECUTE.getErrorMsg() + "[WXBridgeManager] invokeRegisterService:" + WXLogUtils.getStackTrace(th), hashMap);
        }
    }

    public boolean isJSThread() {
        return this.mJSThread != null && this.mJSThread.getId() == Thread.currentThread().getId();
    }

    /* access modifiers changed from: private */
    public void invokeRegisterModules(Map<String, Object> map, List<Map<String, Object>> list) {
        String str;
        if (map == null || !isJSFrameworkInit()) {
            if (!isJSFrameworkInit()) {
                WXLogUtils.d("[WXinvokeRegisterModulesBridgeManager] invokeRegisterModules: framework.js uninitialized.");
            }
            list.add(map);
            return;
        }
        try {
            str = this.mWXBridge.execJS("", (String) null, METHOD_REGISTER_MODULES, new WXJSObject[]{WXWsonJSONSwitch.toWsonOrJsonWXJSObject(map)}) == 0 ? "execJS error" : null;
            for (String next : map.keySet()) {
                if (next != null) {
                    WXModuleManager.resetModuleState(next, true);
                }
            }
        } catch (Throwable th) {
            str = WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_MODULES.getErrorMsg() + " \n " + th.getMessage() + map.entrySet().toString();
        }
        if (!TextUtils.isEmpty(str)) {
            WXLogUtils.e("[WXBridgeManager] invokeRegisterModules:", str);
            WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_MODULES, "invokeRegisterModules", str, (Map<String, String>) null);
        }
    }

    /* access modifiers changed from: private */
    public void invokeRegisterComponents(List<Map<String, Object>> list, List<Map<String, Object>> list2) {
        String str;
        if (list == list2) {
            throw new RuntimeException("Fail receiver should not use source.");
        } else if (!isJSFrameworkInit()) {
            for (Map<String, Object> add : list) {
                list2.add(add);
            }
        } else if (list != null) {
            WXJSObject[] wXJSObjectArr = {WXWsonJSONSwitch.toWsonOrJsonWXJSObject(list)};
            try {
                str = this.mWXBridge.execJS("", (String) null, METHOD_REGISTER_COMPONENTS, wXJSObjectArr) == 0 ? "execJS error" : null;
            } catch (Throwable th) {
                str = WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_COMPONENT + wXJSObjectArr.toString() + WXLogUtils.getStackTrace(th);
            }
            if (!TextUtils.isEmpty(str)) {
                WXLogUtils.e("[WXBridgeManager] invokeRegisterComponents ", str);
                WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_COMPONENT, METHOD_REGISTER_COMPONENTS, str, (Map<String, String>) null);
            }
        }
    }

    public void destroy() {
        if (this.mJSThread != null) {
            this.mJSThread.quit();
        }
        mBridgeManager = null;
        if (this.mDestroyedInstanceId != null) {
            this.mDestroyedInstanceId.clear();
        }
    }

    public void reportJSException(String str, String str2, String str3) {
        WXSDKInstance sDKInstance;
        WXLogUtils.e("reportJSException >>>> instanceId:" + str + ", exception function:" + str2 + ", exception:" + str3);
        WXErrorCode wXErrorCode = WXErrorCode.WX_ERR_JS_EXECUTE;
        if (!(str == null || (sDKInstance = WXSDKManager.getInstance().getSDKInstance(str)) == null)) {
            sDKInstance.setHasException(true);
            str3 = str3 + "\n getTemplateInfo==" + sDKInstance.getTemplateInfo();
            if (METHOD_CREATE_INSTANCE.equals(str2) || !sDKInstance.isContentMd5Match()) {
                try {
                    if (isSkipFrameworkInit(str) || !isJSFrameworkInit() || reInitCount <= 1 || reInitCount >= 10 || sDKInstance.isNeedReLoad()) {
                        sDKInstance.onRenderError(WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode(), WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorMsg() + ", exception function:" + str2 + ", exception:" + str3 + ", extInitTime:" + (System.currentTimeMillis() - sInitFrameWorkTimeOrigin) + "ms" + ", extInitErrorMsg:" + sInitFrameWorkMsg.toString());
                        if (!WXEnvironment.sInAliWeex) {
                            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_JS_CREATE_INSTANCE, str2, str3, (Map<String, String>) null);
                            return;
                        }
                        return;
                    }
                    new ActionReloadPage(str, true).executeAction();
                    sDKInstance.setNeedLoad(true);
                    return;
                } catch (Exception e) {
                    WXLogUtils.e(WXLogUtils.getStackTrace(e));
                }
            }
            if (METHOD_CREATE_INSTANCE.equals(str2) && !sDKInstance.getApmForInstance().hasAddView) {
                wXErrorCode = WXErrorCode.WX_RENDER_ERR_JS_CREATE_INSTANCE;
            } else if (METHOD_CREATE_INSTANCE_CONTEXT.equals(str2) && !sDKInstance.getApmForInstance().hasAddView) {
                wXErrorCode = WXErrorCode.WX_RENDER_ERR_JS_CREATE_INSTANCE_CONTEXT;
            } else if ((METHOD_UPDATE_COMPONENT_WITH_DATA.equals(str2) || METHOD_CREATE_PAGE_WITH_CONTENT.equals(str2) || METHOD_POST_TASK_TO_MSG_LOOP.equals(str2) || METHOD_JSFM_NOT_INIT_IN_EAGLE_MODE.equals(str2)) && !sDKInstance.getApmForInstance().hasAddView) {
                wXErrorCode = WXErrorCode.WX_DEGRAD_EAGLE_RENDER_ERROR;
            }
            sDKInstance.onJSException(wXErrorCode.getErrorCode(), str2, str3);
        }
        doReportJSException(str, str2, wXErrorCode, str3);
    }

    private void doReportJSException(String str, String str2, WXErrorCode wXErrorCode, String str3) {
        String str4;
        String str5;
        WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
        if (WXSDKManager.getInstance().getIWXJSExceptionAdapter() != null) {
            if (TextUtils.isEmpty(str)) {
                str = "instanceIdisNull";
            }
            if (wXSDKInstance == null && IWXUserTrackAdapter.INIT_FRAMEWORK.equals(str2)) {
                try {
                    if (WXEnvironment.getApplication() != null) {
                        try {
                            File file = new File(WXEnvironment.getApplication().getApplicationContext().getCacheDir().getPath() + INITLOGFILE);
                            if (file.exists()) {
                                if (file.length() > 0) {
                                    StringBuilder sb = new StringBuilder();
                                    try {
                                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                                        while (true) {
                                            String readLine = bufferedReader.readLine();
                                            if (readLine == null) {
                                                break;
                                            }
                                            sb.append(readLine + "\n");
                                        }
                                        str4 = sb.toString();
                                        try {
                                            bufferedReader.close();
                                        } catch (Exception e) {
                                            Exception exc = e;
                                            str5 = str4;
                                            e = exc;
                                        }
                                    } catch (Exception e2) {
                                        e = e2;
                                        str5 = null;
                                        try {
                                            WXLogUtils.e(WXLogUtils.getStackTrace(e));
                                            str4 = str5;
                                            file.delete();
                                        } catch (Throwable th) {
                                            th = th;
                                            try {
                                                WXLogUtils.e(WXLogUtils.getStackTrace(th));
                                            } catch (Throwable th2) {
                                                th = th2;
                                            }
                                            str4 = str5;
                                            str3 = str3 + "\n" + str4;
                                            WXLogUtils.e("reportJSException:" + str3);
                                            WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
                                        }
                                        str3 = str3 + "\n" + str4;
                                        WXLogUtils.e("reportJSException:" + str3);
                                        WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
                                    }
                                } else {
                                    str4 = null;
                                }
                                try {
                                    file.delete();
                                } catch (Throwable th3) {
                                    str5 = str4;
                                    th = th3;
                                }
                                str3 = str3 + "\n" + str4;
                                WXLogUtils.e("reportJSException:" + str3);
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            str5 = null;
                            WXLogUtils.e(WXLogUtils.getStackTrace(th));
                            str4 = str5;
                            str3 = str3 + "\n" + str4;
                            WXLogUtils.e("reportJSException:" + str3);
                            WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
                        }
                    }
                    str4 = null;
                } catch (Throwable th5) {
                    th = th5;
                    str5 = null;
                    WXLogUtils.e(WXLogUtils.getStackTrace(th));
                    str4 = str5;
                    str3 = str3 + "\n" + str4;
                    WXLogUtils.e("reportJSException:" + str3);
                    WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
                }
                str3 = str3 + "\n" + str4;
                WXLogUtils.e("reportJSException:" + str3);
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
        }
    }

    private void registerDomModule() throws WXException {
        HashMap hashMap = new HashMap();
        hashMap.put(WXDomModule.WXDOM, WXDomModule.METHODS);
        registerModules(hashMap);
    }

    public static void updateGlobalConfig(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "none";
        }
        if (!TextUtils.equals(str, globalConfig)) {
            globalConfig = str;
            WXEnvironment.addCustomOptions(GLOBAL_CONFIG_KEY, globalConfig);
            AnonymousClass27 r1 = new Runnable() {
                public void run() {
                    if (WXBridgeManager.mBridgeManager != null && WXBridgeManager.mBridgeManager.isJSFrameworkInit() && (WXBridgeManager.mBridgeManager.mWXBridge instanceof WXBridge)) {
                        ((WXBridge) WXBridgeManager.mBridgeManager.mWXBridge).nativeUpdateGlobalConfig(WXBridgeManager.globalConfig);
                    }
                    if (WXBridgeManager.globalConfig.contains(WXWsonJSONSwitch.WSON_OFF)) {
                        WXWsonJSONSwitch.USE_WSON = false;
                    } else {
                        WXWsonJSONSwitch.USE_WSON = true;
                    }
                }
            };
            if (mBridgeManager == null || !mBridgeManager.isJSFrameworkInit()) {
                r1.run();
            } else {
                mBridgeManager.post(r1);
            }
        }
    }

    @Nullable
    public Looper getJSLooper() {
        if (this.mJSThread != null) {
            return this.mJSThread.getLooper();
        }
        return null;
    }

    public void notifySerializeCodeCache() {
        post(new Runnable() {
            public void run() {
                if (WXBridgeManager.this.isJSFrameworkInit()) {
                    WXBridgeManager.this.invokeExecJS("", (String) null, WXBridgeManager.METHOD_NOTIFY_SERIALIZE_CODE_CACHE, new WXJSObject[0]);
                }
            }
        });
    }

    public void takeJSHeapSnapshot(String str) {
        Message obtainMessage = this.mJSHandler.obtainMessage();
        obtainMessage.obj = str;
        obtainMessage.what = 13;
        obtainMessage.setTarget(this.mJSHandler);
        obtainMessage.sendToTarget();
    }

    public int callCreateBody(String str, String str2, String str3, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3) {
        String str4 = str;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            WXLogUtils.d("[WXBridgeManager] call callCreateBody arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callCreateBody", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionCreateBody graphicActionCreateBody = new GraphicActionCreateBody(sDKInstance, str3, str2, hashMap, hashMap2, hashSet, fArr, fArr2, fArr3);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionCreateBody.getPageId(), graphicActionCreateBody);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callCreateBody exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callCreateBody", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callAddElement(String str, String str2, String str3, int i, String str4, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3, boolean z) {
        String str5 = str;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callAddElement arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str5, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callAddElement", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str5)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str5);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionAddElement graphicActionAddElement = r4;
            GraphicActionAddElement graphicActionAddElement2 = new GraphicActionAddElement(sDKInstance, str3, str2, str4, i, hashMap, hashMap2, hashSet, fArr, fArr2, fArr3);
            if (z) {
                sDKInstance.addInActiveAddElementAction(str3, graphicActionAddElement);
                return 1;
            }
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str5, graphicActionAddElement);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callAddElement exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str5, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callAddElement", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callRemoveElement(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callRemoveElement arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRemoveElement", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionRemoveElement graphicActionRemoveElement = new GraphicActionRemoveElement(sDKInstance, str2);
            if (sDKInstance.getInActiveAddElementAction(str2) != null) {
                sDKInstance.removeInActiveAddElmentAction(str2);
                return 1;
            }
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionRemoveElement.getPageId(), graphicActionRemoveElement);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRemoveElement exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRemoveElement", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callMoveElement(String str, String str2, String str3, int i) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callMoveElement arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callMoveElement", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionMoveElement graphicActionMoveElement = new GraphicActionMoveElement(sDKInstance, str2, str3, i);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionMoveElement.getPageId(), graphicActionMoveElement);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callMoveElement exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callMoveElement", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callAddEvent(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callAddEvent arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callAddEvent", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                new GraphicActionAddEvent(sDKInstance, str2, str3).executeActionOnRender();
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callAddEvent exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callAddEvent", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        getNextTick(str);
        return 1;
    }

    public int callRemoveEvent(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callRemoveEvent arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRemoveEvent", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                new GraphicActionRemoveEvent(sDKInstance, str2, str3).executeActionOnRender();
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRemoveEvent exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRemoveEvent", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        getNextTick(str);
        return 1;
    }

    public int callUpdateStyle(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2, HashMap<String, String> hashMap3, HashMap<String, String> hashMap4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callUpdateStyle arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateStyle", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionUpdateStyle graphicActionUpdateStyle = new GraphicActionUpdateStyle(sDKInstance, str2, hashMap, hashMap2, hashMap3, hashMap4);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionUpdateStyle.getPageId(), graphicActionUpdateStyle);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateStyle exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateStyle", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callUpdateAttrs(String str, String str2, HashMap<String, String> hashMap) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callUpdateAttrs arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateAttrs", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionUpdateAttr graphicActionUpdateAttr = new GraphicActionUpdateAttr(sDKInstance, str2, hashMap);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionUpdateAttr.getPageId(), graphicActionUpdateAttr);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateAttrs exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateAttrs", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    private void setExceedGPULimitComponentsInfo(String str, String str2, GraphicSize graphicSize) {
        float openGLRenderLimitValue = (float) WXRenderManager.getOpenGLRenderLimitValue();
        if (openGLRenderLimitValue <= 0.0f) {
            return;
        }
        if (graphicSize.getHeight() > openGLRenderLimitValue || graphicSize.getWidth() > openGLRenderLimitValue) {
            JSONObject jSONObject = new JSONObject();
            WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(str, str2);
            jSONObject.put("GPU limit", (Object) String.valueOf(openGLRenderLimitValue));
            jSONObject.put("component.width", (Object) String.valueOf(graphicSize.getWidth()));
            jSONObject.put("component.height", (Object) String.valueOf(graphicSize.getHeight()));
            if (wXComponent.getComponentType() != null && !wXComponent.getComponentType().isEmpty()) {
                jSONObject.put("component.type", (Object) wXComponent.getComponentType());
            }
            if (wXComponent.getStyles() != null && !wXComponent.getStyles().isEmpty()) {
                jSONObject.put("component.style", (Object) wXComponent.getStyles().toString());
            }
            if (wXComponent.getAttrs() != null && !wXComponent.getAttrs().isEmpty()) {
                jSONObject.put("component.attr", (Object) wXComponent.getAttrs().toString());
            }
            if (wXComponent.getEvents() != null && !wXComponent.getEvents().isEmpty()) {
                jSONObject.put("component.event", (Object) wXComponent.getEvents().toString());
            }
            if (wXComponent.getMargin() != null) {
                jSONObject.put("component.margin", (Object) wXComponent.getMargin().toString());
            }
            if (wXComponent.getPadding() != null) {
                jSONObject.put("component.padding", (Object) wXComponent.getPadding().toString());
            }
            if (wXComponent.getBorder() != null) {
                jSONObject.put("component.border", (Object) wXComponent.getBorder().toString());
            }
            WXSDKManager.getInstance().getSDKInstance(str).setComponentsInfoExceedGPULimit(jSONObject);
        }
    }

    public int callAddChildToRichtext(String str, String str2, String str3, String str4, String str5, HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        String str6 = str;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callAddChildToRichtext arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callAddChildToRichtext", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionAddChildToRichtext graphicActionAddChildToRichtext = new GraphicActionAddChildToRichtext(sDKInstance, str2, str3, str4, str5, hashMap, hashMap2);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionAddChildToRichtext.getPageId(), graphicActionAddChildToRichtext);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callAddChildToRichtext exception: ", WXLogUtils.getStackTrace(e));
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callAddChildToRichtext", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callRemoveChildFromRichtext(String str, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callRemoveChildFromRichtext arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRemoveChildFromRichtext", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionRemoveChildFromRichtext graphicActionRemoveChildFromRichtext = new GraphicActionRemoveChildFromRichtext(sDKInstance, str2, str3, str4);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionRemoveChildFromRichtext.getPageId(), graphicActionRemoveChildFromRichtext);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRemoveChildFromRichtext exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRemoveChildFromRichtext", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callUpdateRichtextStyle(String str, String str2, HashMap<String, String> hashMap, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callUpdateRichtextStyle arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateRichtextStyle", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionUpdateRichtextStyle graphicActionUpdateRichtextStyle = new GraphicActionUpdateRichtextStyle(sDKInstance, str2, hashMap, str3, str4);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionUpdateRichtextStyle.getPageId(), graphicActionUpdateRichtextStyle);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateRichtextStyle exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateRichtextStyle", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callUpdateRichtextChildAttr(String str, String str2, HashMap<String, String> hashMap, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callUpdateRichtextChildAttr arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateRichtextChildAttr", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionUpdateRichtextAttr graphicActionUpdateRichtextAttr = new GraphicActionUpdateRichtextAttr(sDKInstance, str2, hashMap, str3, str4);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionUpdateRichtextAttr.getPageId(), graphicActionUpdateRichtextAttr);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateRichtextChildAttr exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateRichtextChildAttr", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callLayout(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, boolean z, int i7) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callLayout arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callLayout", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicSize graphicSize = new GraphicSize((float) i6, (float) i5);
            GraphicPosition graphicPosition = new GraphicPosition((float) i3, (float) i, (float) i4, (float) i2);
            setExceedGPULimitComponentsInfo(str, str2, graphicSize);
            GraphicActionAddElement inActiveAddElementAction = sDKInstance.getInActiveAddElementAction(str2);
            if (inActiveAddElementAction != null) {
                inActiveAddElementAction.setRTL(z);
                inActiveAddElementAction.setSize(graphicSize);
                inActiveAddElementAction.setPosition(graphicPosition);
                if (!TextUtils.equals(str2, WXComponent.ROOT)) {
                    inActiveAddElementAction.setIndex(i7);
                }
                WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, inActiveAddElementAction);
                sDKInstance.removeInActiveAddElmentAction(str2);
                return 1;
            }
            GraphicActionLayout graphicActionLayout = new GraphicActionLayout(sDKInstance, str2, graphicPosition, graphicSize, z);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionLayout.getPageId(), graphicActionLayout);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callLayout exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callLayout", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callAppendTreeCreateFinish(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            WXLogUtils.d("[WXBridgeManager] call callAppendTreeCreateFinish arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callAppendTreeCreateFinish", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, new GraphicActionAppendTreeCreateFinish(WXSDKManager.getInstance().getSDKInstance(str), str2));
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callAppendTreeCreateFinish exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callAppendTreeCreateFinish", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callCreateFinish(String str) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.d("[WXBridgeManager] call callCreateFinish arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callCreateFinish", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            long currentTimeMillis = System.currentTimeMillis();
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            sDKInstance.getApmForInstance().onStage("callCreateFinish");
            sDKInstance.firstScreenCreateInstanceTime(currentTimeMillis);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, new GraphicActionCreateFinish(sDKInstance));
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callCreateFinish exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callCreateFinish", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callRenderSuccess(String str) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.d("[WXBridgeManager] call callRenderSuccess arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRenderSuccess", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        if (this.mDestroyedInstanceId != null && this.mDestroyedInstanceId.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            sDKInstance.getApmForInstance().onStage("callRenderSuccess");
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, new GraphicActionRenderSuccess(sDKInstance));
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRenderSuccess exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRenderSuccess", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public ContentBoxMeasurement getMeasurementFunc(String str, long j) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance != null) {
            return sDKInstance.getContentBoxMeasurement(j);
        }
        return null;
    }

    public void bindMeasurementToRenderObject(long j) {
        if (isJSFrameworkInit()) {
            this.mWXBridge.bindMeasurementToRenderObject(j);
        }
    }

    @UiThread
    public boolean notifyLayout(String str) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            return this.mWXBridge.notifyLayout(str);
        }
        return false;
    }

    @UiThread
    public void forceLayout(String str) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.forceLayout(str);
        }
    }

    public void onInstanceClose(String str) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.onInstanceClose(str);
        }
    }

    public void setDefaultRootSize(String str, float f, float f2, boolean z, boolean z2) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setDefaultHeightAndWidthIntoRootDom(str, f, f2, z, z2);
        }
    }

    public void setRenderContentWrapContentToCore(boolean z, String str) {
        if (isJSFrameworkInit()) {
            this.mWXBridge.setRenderContainerWrapContent(z, str);
        }
    }

    public void setStyleWidth(String str, String str2, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setStyleWidth(str, str2, f);
        }
    }

    public void setStyleHeight(String str, String str2, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setStyleHeight(str, str2, f);
        }
    }

    public long[] getFirstScreenRenderTime(String str) {
        if (isJSFrameworkInit()) {
            return this.mWXBridge.getFirstScreenRenderTime(str);
        }
        return new long[]{0, 0, 0};
    }

    public long[] getRenderFinishTime(String str) {
        if (isJSFrameworkInit()) {
            return this.mWXBridge.getRenderFinishTime(str);
        }
        return new long[]{0, 0, 0};
    }

    public void setDeviceDisplay(String str, float f, float f2, float f3) {
        final String str2 = str;
        final float f4 = f;
        final float f5 = f2;
        final float f6 = f3;
        post(new Runnable() {
            public void run() {
                WXBridgeManager.this.mWXBridge.setDeviceDisplay(str2, f4, f5, f6);
            }
        });
    }

    public void updateInitDeviceParams(final String str, final String str2, final String str3, final String str4) {
        if (isJSFrameworkInit()) {
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.mWXBridge.updateInitFrameworkParams("deviceWidth", str, "deviceWidth");
                }
            });
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.mWXBridge.updateInitFrameworkParams("deviceHeight", str2, "deviceHeight");
                }
            });
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.mWXBridge.updateInitFrameworkParams("scale", str3, "scale");
                }
            });
            if (str4 != null) {
                post(new Runnable() {
                    public void run() {
                        WXBridgeManager.this.mWXBridge.updateInitFrameworkParams(WXConfig.androidStatusBarHeight, str4, WXConfig.androidStatusBarHeight);
                    }
                });
            }
        }
    }

    public void setMargin(String str, String str2, CSSShorthand.EDGE edge, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setMargin(str, str2, edge, f);
        }
    }

    public void setPadding(String str, String str2, CSSShorthand.EDGE edge, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setPadding(str, str2, edge, f);
        }
    }

    public void setPosition(String str, String str2, CSSShorthand.EDGE edge, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setPosition(str, str2, edge, f);
        }
    }

    public void markDirty(String str, String str2, boolean z) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.markDirty(str, str2, z);
        }
    }

    public void setViewPortWidth(String str, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setViewPortWidth(str, f);
        }
    }

    public void setPageArgument(String str, String str2, String str3) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setPageArgument(str, str2, str3);
        }
    }

    public void reloadPageLayout(String str) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.reloadPageLayout(str);
        }
    }

    public void setDeviceDisplayOfPage(String str, float f, float f2) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setDeviceDisplayOfPage(str, f, f2);
        }
    }

    public int callHasTransitionPros(String str, String str2, HashMap<String, String> hashMap) {
        WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(str, str2);
        if (wXComponent == null || wXComponent.getTransition() == null || wXComponent.getTransition().getProperties() == null) {
            return -1;
        }
        for (String containsKey : wXComponent.getTransition().getProperties()) {
            if (hashMap.containsKey(containsKey)) {
                return 1;
            }
        }
        return 0;
    }

    public void registerCoreEnv(String str, String str2) {
        if (isJSFrameworkInit()) {
            this.mWXBridge.registerCoreEnv(str, str2);
        } else {
            mWeexCoreEnvOptions.put(str, str2);
        }
    }

    private void onJsFrameWorkInitSuccees() {
        for (Map.Entry next : mWeexCoreEnvOptions.entrySet()) {
            this.mWXBridge.registerCoreEnv((String) next.getKey(), (String) next.getValue());
        }
        mWeexCoreEnvOptions.clear();
    }

    public String getWeexCoreThreadStackTrace() {
        if (this.mJSThread == null) {
            return "null == mJSThread";
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(String.format("Thread Name: '%s'\n", new Object[]{this.mJSThread.getName()}));
            sb.append(String.format(Locale.ENGLISH, "\"%s\" prio=%d tid=%d %s\n", new Object[]{this.mJSThread.getName(), Integer.valueOf(this.mJSThread.getPriority()), Long.valueOf(this.mJSThread.getId()), this.mJSThread.getState()}));
            StackTraceElement[] stackTrace = this.mJSThread.getStackTrace();
            int length = stackTrace.length;
            for (int i = 0; i < length; i++) {
                sb.append(String.format("\tat %s\n", new Object[]{stackTrace[i].toString()}));
            }
        } catch (Exception e) {
            Log.e("weex", "getJSThreadStackTrace error:", e);
        }
        return sb.toString();
    }
}
