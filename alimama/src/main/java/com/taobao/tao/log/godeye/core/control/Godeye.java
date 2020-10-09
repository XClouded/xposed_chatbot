package com.taobao.tao.log.godeye.core.control;

import android.app.Application;
import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.tlog.protocol.model.GodeyeInfo;
import com.taobao.tao.log.godeye.api.command.ICommandManager;
import com.taobao.tao.log.godeye.api.command.ResponseData;
import com.taobao.tao.log.godeye.api.command.TraceTask;
import com.taobao.tao.log.godeye.api.control.AbsCommandController;
import com.taobao.tao.log.godeye.api.control.IGodeye;
import com.taobao.tao.log.godeye.api.control.IGodeyeJointPointCenter;
import com.taobao.tao.log.godeye.core.GodEyeAppListener;
import com.taobao.tao.log.godeye.core.GodEyeReponse;
import com.taobao.tao.log.godeye.core.command.GodeyeCommandManager;
import com.taobao.tao.log.godeye.core.command.GodeyeRemoteCommandCenter;
import com.taobao.tao.log.godeye.core.plugin.PluginManager;
import com.taobao.tao.log.godeye.protocol.control.Define;
import com.taobao.tao.log.godeye.protocol.model.ClientEvent;
import com.taobao.tao.log.task.MethodTraceReplyTask;
import com.taobao.tao.log.upload.FileUploadListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Godeye implements IGodeye {
    public static final String GODEYE_TAG = "Godeye";
    private static volatile Godeye instance;
    public GodEyeAppListener godEyeAppListener;
    public Map<String, GodEyeReponse> godEyeReponses = new ConcurrentHashMap();
    private String mAppId;
    private String mAppVersion;
    private Application mApplication;
    private String mBuildId;
    private List<ClientEvent> mClientEventQueue = new ArrayList();
    private GodeyeCommandManager mGodeyeCommandManager;
    private GodeyeJointPointCenter mGodeyeJointPointCenter;
    private IGodeyeJointPointCenter.GodeyeJointPointCallback mGodeyeOnDemandCallback;
    private GodeyeRemoteCommandCenter mGodeyeRemoteCommandCenter;
    private boolean mInitialized = false;
    private boolean mIsDebugMode = false;
    public String utdid;

    private Godeye() {
    }

    public static Godeye sharedInstance() {
        if (instance == null) {
            instance = new Godeye();
        }
        return instance;
    }

    public void initialize(Application application, String str, String str2) {
        this.mApplication = application;
        this.mAppId = str;
        this.mAppVersion = str2;
        try {
            PluginManager.loadPlugin(application);
            if (this.mGodeyeJointPointCenter == null) {
                this.mGodeyeJointPointCenter = defaultGodeyeJointPointCenter();
            }
            commandExecuteWhenInit();
            this.mInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commandExecuteWhenInit() {
        try {
            Set<Define.Entry<AbsCommandController>> commandControllers = defaultGodeyeRemoteCommandCenter().getCommandControllers();
            if (commandControllers != null && commandControllers.size() > 0) {
                this.mIsDebugMode = true;
                if (this.mGodeyeOnDemandCallback != null) {
                    this.mGodeyeOnDemandCallback.doCallback();
                }
                for (Define.Entry next : commandControllers) {
                    TraceTask rawCommandString = sharedInstance().defaultCommandManager().getRawCommandString((AbsCommandController) next.getValue());
                    if (rawCommandString != null) {
                        defaultGodeyeRemoteCommandCenter().dispatchCommandInternal((AbsCommandController) next.getValue(), rawCommandString, true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerCommandController(AbsCommandController absCommandController) {
        defaultGodeyeRemoteCommandCenter().registerCommandController(absCommandController.opCode, absCommandController);
    }

    public boolean handleRemoteCommand(GodeyeInfo godeyeInfo) {
        if (godeyeInfo == null || godeyeInfo.commandInfo == null) {
            return false;
        }
        try {
            this.mIsDebugMode = true;
            defaultGodeyeRemoteCommandCenter().dispatchCommand(godeyeInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void response(AbsCommandController absCommandController, ResponseData responseData) {
        Log.e("xxxxxxxx", "controller.opCode:" + absCommandController.opCode);
        if (responseData != null) {
            if (responseData.extraData == null) {
                responseData.extraData = new JSONObject();
            }
            responseData.extraData.put(Constants.KEY_APP_BUILD, (Object) this.mBuildId);
            if (responseData.responseCode == 5) {
                responseData.extraData.put(Constants.KEY_STAT_DATA, (Object) getRuntimeStatData());
                responseData.extraData.put(Constants.KEY_CLIENT_EVENT_QUEUE, (Object) this.mClientEventQueue);
                PluginManager.removeAllPlugins(this.mApplication);
            }
            if (responseData.responseCode == 5) {
                try {
                    String str = absCommandController.opCode;
                    String requestId = absCommandController.getRequestId();
                    String uploadId = absCommandController.getUploadId();
                    if (str != null) {
                        MethodTraceReplyTask.execute(requestId, uploadId, responseData.extraData.getString("fileName"), responseData.extraData.getString(Constants.KEY_FILE_URL), "application/x-perf-methodtrace", responseData.tokenData.getString("ossObjectKey"), responseData.tokenData.getString("ossEndpoint"));
                    } else {
                        Log.e(GODEYE_TAG, "you need regist god eye reponse");
                    }
                } catch (Exception e) {
                    Log.e(GODEYE_TAG, e.getMessage(), e);
                }
            }
        }
    }

    public void upload(AbsCommandController absCommandController, String str, FileUploadListener fileUploadListener) {
        String str2 = absCommandController.opCode;
        String uploadId = absCommandController.getUploadId();
        if (str2 != null) {
            GodEyeReponse godEyeReponse = this.godEyeReponses.get(str2);
            if (godEyeReponse != null) {
                godEyeReponse.sendFile(uploadId, str, fileUploadListener);
            } else {
                Log.e(GODEYE_TAG, "you need regist god eye reponse");
            }
        }
    }

    public ICommandManager defaultCommandManager() {
        if (this.mGodeyeCommandManager == null) {
            this.mGodeyeCommandManager = new GodeyeCommandManager(this.mApplication);
        }
        return this.mGodeyeCommandManager;
    }

    public GodeyeJointPointCenter defaultGodeyeJointPointCenter() {
        if (this.mGodeyeJointPointCenter == null) {
            this.mGodeyeJointPointCenter = new GodeyeJointPointCenter(this.mApplication);
        }
        return this.mGodeyeJointPointCenter;
    }

    public GodeyeRemoteCommandCenter defaultGodeyeRemoteCommandCenter() {
        if (this.mGodeyeRemoteCommandCenter == null) {
            this.mGodeyeRemoteCommandCenter = new GodeyeRemoteCommandCenter();
        }
        return this.mGodeyeRemoteCommandCenter;
    }

    public Map<String, Object> getRuntimeStatData() {
        Map<String, Object> map;
        if (this.godEyeAppListener != null) {
            try {
                map = this.godEyeAppListener.getAppInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.w(GODEYE_TAG, "god eye app listener doesn't exist ");
            map = null;
        }
        return map == null ? new HashMap() : map;
    }

    public Application getApplication() {
        return this.mApplication;
    }

    public String getAppVersion() {
        return this.mAppVersion;
    }

    public void setBuildId(String str) {
        this.mBuildId = str;
    }

    public boolean isInitialized() {
        return this.mInitialized;
    }

    public boolean isDebugMode() {
        return this.mIsDebugMode;
    }

    public void addClientEvent(ClientEvent clientEvent) {
        this.mClientEventQueue.add(clientEvent);
    }

    public void setGodeyeOnDemandCallback(IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
        this.mGodeyeOnDemandCallback = godeyeJointPointCallback;
    }
}
