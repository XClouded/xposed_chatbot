package com.alibaba.ut.abtest.internal;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.ut.abtest.UTABEnvironment;
import com.alibaba.ut.abtest.UTABMethod;
import com.alibaba.ut.abtest.bucketing.decision.DecisionService;
import com.alibaba.ut.abtest.bucketing.decision.DecisionServiceImpl;
import com.alibaba.ut.abtest.bucketing.expression.ExpressionService;
import com.alibaba.ut.abtest.bucketing.expression.ExpressionServiceImpl;
import com.alibaba.ut.abtest.bucketing.feature.FeatureService;
import com.alibaba.ut.abtest.bucketing.feature.FeatureServiceImpl;
import com.alibaba.ut.abtest.config.ConfigService;
import com.alibaba.ut.abtest.config.ConfigServiceImpl;
import com.alibaba.ut.abtest.event.EventService;
import com.alibaba.ut.abtest.event.EventServiceImpl;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.debug.DebugService;
import com.alibaba.ut.abtest.internal.debug.DebugServiceImpl;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.Preferences;
import com.alibaba.ut.abtest.internal.util.StringUtils;
import com.alibaba.ut.abtest.internal.util.Utils;
import com.alibaba.ut.abtest.multiprocess.MultiProcessService;
import com.alibaba.ut.abtest.multiprocess.MultiProcessServiceImpl;
import com.alibaba.ut.abtest.pipeline.PipelineService;
import com.alibaba.ut.abtest.pipeline.PipelineServiceImpl;
import com.alibaba.ut.abtest.push.PushService;
import com.alibaba.ut.abtest.push.PushServiceImpl;
import com.alibaba.ut.abtest.push.UTABPushConfiguration;
import com.alibaba.ut.abtest.track.TrackService;
import com.alibaba.ut.abtest.track.TrackServiceImpl;

public final class ABContext {
    private static final String TAG = "ABContext";
    private static ABContext instance;
    private ConfigService configService;
    private Context context;
    private volatile UTABMethod currentApiMethod;
    private boolean debugMode;
    private DebugService debugService;
    private DecisionService decisionService;
    private UTABEnvironment environment;
    private EventService eventService;
    private ExpressionService expressionService;
    private FeatureService featureService;
    private boolean multiProcessEnable;
    private MultiProcessService multiProcessService;
    private PipelineService pipelineService;
    private PushService pushService;
    private TrackService trackService;
    private String userId;
    private String userNick;

    private ABContext() {
    }

    public static synchronized ABContext getInstance() {
        ABContext aBContext;
        synchronized (ABContext.class) {
            if (instance == null) {
                instance = new ABContext();
            }
            aBContext = instance;
        }
        return aBContext;
    }

    public void initialize(Context context2) {
        this.context = context2;
        this.userId = Preferences.getInstance().getString("uid", (String) null);
        this.userNick = Preferences.getInstance().getString(ABConstants.Preference.USER_NICK, (String) null);
        LogUtils.logD(TAG, "获取本地存储用户信息. userId=" + this.userId + ", userNick=" + this.userNick);
    }

    public Context getContext() {
        if (this.context == null) {
            return Utils.getApplication();
        }
        return this.context;
    }

    public boolean isDebugMode() {
        return this.debugMode;
    }

    public void setDebugMode(boolean z) {
        this.debugMode = z;
    }

    public UTABMethod getCurrentApiMethod() {
        return this.currentApiMethod;
    }

    public void setCurrentApiMethod(UTABMethod uTABMethod) {
        LogUtils.logD(TAG, "setCurrentApiMethod, apiMethod=" + uTABMethod + ", currentApiMethod=" + this.currentApiMethod);
        if (this.currentApiMethod == null || this.currentApiMethod != uTABMethod) {
            if (uTABMethod == UTABMethod.Push) {
                this.currentApiMethod = UTABMethod.Push;
                if (!getPushService().initialize(new UTABPushConfiguration.Builder().create())) {
                    this.currentApiMethod = UTABMethod.Pull;
                }
            } else {
                this.currentApiMethod = UTABMethod.Pull;
            }
            if (this.currentApiMethod == UTABMethod.Pull) {
                getPushService().destory();
            }
        }
    }

    public void setMultiProcessEnable(boolean z) {
        this.multiProcessEnable = z;
    }

    public boolean isMultiProcessEnable() {
        return this.multiProcessEnable;
    }

    public ExpressionService getExpressionService() {
        if (this.expressionService == null) {
            synchronized (this) {
                if (this.expressionService == null) {
                    this.expressionService = new ExpressionServiceImpl();
                }
            }
        }
        return this.expressionService;
    }

    public DecisionService getDecisionService() {
        if (this.decisionService == null) {
            synchronized (this) {
                if (this.decisionService == null) {
                    this.decisionService = new DecisionServiceImpl();
                }
            }
        }
        return this.decisionService;
    }

    public ConfigService getConfigService() {
        if (this.configService == null) {
            synchronized (this) {
                if (this.configService == null) {
                    this.configService = new ConfigServiceImpl();
                }
            }
        }
        return this.configService;
    }

    public TrackService getTrackService() {
        if (this.trackService == null) {
            synchronized (this) {
                if (this.trackService == null) {
                    this.trackService = new TrackServiceImpl();
                }
            }
        }
        return this.trackService;
    }

    public PipelineService getPipelineService() {
        if (this.pipelineService == null) {
            synchronized (this) {
                if (this.pipelineService == null) {
                    this.pipelineService = new PipelineServiceImpl();
                }
            }
        }
        return this.pipelineService;
    }

    public PushService getPushService() {
        if (this.pushService == null) {
            synchronized (this) {
                if (this.pushService == null) {
                    this.pushService = new PushServiceImpl();
                }
            }
        }
        return this.pushService;
    }

    public DebugService getDebugService() {
        if (this.debugService == null) {
            synchronized (this) {
                if (this.debugService == null) {
                    this.debugService = new DebugServiceImpl();
                }
            }
        }
        return this.debugService;
    }

    public EventService getEventService() {
        if (this.eventService == null) {
            synchronized (this) {
                if (this.eventService == null) {
                    this.eventService = new EventServiceImpl();
                }
            }
        }
        return this.eventService;
    }

    public FeatureService getFeatureService() {
        if (this.featureService == null) {
            synchronized (this) {
                if (this.featureService == null) {
                    this.featureService = new FeatureServiceImpl();
                }
            }
        }
        return this.featureService;
    }

    public MultiProcessService getMultiProcessService() {
        if (this.multiProcessService == null) {
            synchronized (this) {
                if (this.multiProcessService == null) {
                    this.multiProcessService = new MultiProcessServiceImpl();
                }
            }
        }
        return this.multiProcessService;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getLongUserId() {
        return Preferences.getInstance().getString(ABConstants.Preference.USER_LONG_ID, (String) null);
    }

    public void setUserId(String str) {
        this.userId = StringUtils.emptyToNull(str);
        Preferences.getInstance().putStringAsync("uid", this.userId);
        if (!TextUtils.isEmpty(str)) {
            Preferences.getInstance().putStringAsync(ABConstants.Preference.USER_LONG_ID, this.userId);
        }
    }

    public String getUserNick() {
        return this.userNick;
    }

    public void setUserNick(String str) {
        this.userNick = StringUtils.emptyToNull(str);
        Preferences.getInstance().putStringAsync(ABConstants.Preference.USER_NICK, this.userNick);
        if (!TextUtils.isEmpty(str)) {
            Preferences.getInstance().putStringAsync(ABConstants.Preference.USER_LONG_NICK, this.userNick);
        }
    }

    public UTABEnvironment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(UTABEnvironment uTABEnvironment) {
        this.environment = uTABEnvironment;
    }
}
