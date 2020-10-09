package com.taobao.weex.analyzer;

import alimama.com.unwrouter.UNWRouter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.motu.crashreporter.Constants;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.core.DevOptionsConfig;
import com.taobao.weex.analyzer.core.ShakeDetector;
import com.taobao.weex.analyzer.core.cpu.CpuSampleView;
import com.taobao.weex.analyzer.core.debug.DataRepository;
import com.taobao.weex.analyzer.core.exception.JSExceptionCatcher;
import com.taobao.weex.analyzer.core.fps.FPSSampler;
import com.taobao.weex.analyzer.core.fps.FpsSampleView;
import com.taobao.weex.analyzer.core.inspector.network.NetworkInspectorView;
import com.taobao.weex.analyzer.core.inspector.view.InspectorView;
import com.taobao.weex.analyzer.core.lint.ProfileDomView;
import com.taobao.weex.analyzer.core.lint.RemoteVDomMonitor;
import com.taobao.weex.analyzer.core.logcat.LogView;
import com.taobao.weex.analyzer.core.logcat.ats.LogUploadOverlayView;
import com.taobao.weex.analyzer.core.memory.MemorySampleView;
import com.taobao.weex.analyzer.core.scalpel.ScalpelFrameLayout;
import com.taobao.weex.analyzer.core.scalpel.ScalpelViewController;
import com.taobao.weex.analyzer.core.settings.SettingsActivity;
import com.taobao.weex.analyzer.core.storage.StorageView;
import com.taobao.weex.analyzer.core.storage.WXPerfStorage;
import com.taobao.weex.analyzer.core.traffic.TrafficSampleView;
import com.taobao.weex.analyzer.core.weex.PerfSampleOverlayView;
import com.taobao.weex.analyzer.core.weex.v2.PerformanceV2Repository;
import com.taobao.weex.analyzer.core.weex.v2.WXPerformanceV2OverlayView;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.DevOption;
import com.taobao.weex.analyzer.view.EntranceView;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.IResizableView;
import com.taobao.weex.common.Constants;
import com.taobao.weex.performance.WXAnalyzerDataTransfer;
import com.uc.webview.export.extension.UCCore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeexDevOptions implements IWXDevOptions {
    public static final String EXTRA_DEVICE_ID = "deviceId";
    public static final String EXTRA_FROM = "from";
    public static final String EXTRA_WS_URL = "wsUrl";
    public static Context sApplicationContext;
    private static IWebSocket sWebSocketImpl;
    /* access modifiers changed from: private */
    public Config mConfig;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public CpuSampleView mCpuSampleView;
    private String mCurPageName;
    /* access modifiers changed from: private */
    public DevOptionsConfig mDevOptionsConfig;
    private List<DevOption> mExtraOptions = null;
    /* access modifiers changed from: private */
    public FpsSampleView mFpsSampleView;
    /* access modifiers changed from: private */
    public InspectorView mInspectorView;
    /* access modifiers changed from: private */
    public WXSDKInstance mInstance;
    /* access modifiers changed from: private */
    public LogUploadOverlayView mLogUploadOverlayView;
    /* access modifiers changed from: private */
    public LogView mLogView;
    /* access modifiers changed from: private */
    public MemorySampleView mMemorySampleView;
    /* access modifiers changed from: private */
    public NetworkInspectorView mNetworkInspectorView;
    /* access modifiers changed from: private */
    public PerfSampleOverlayView mPerfMonitorOverlayView;
    /* access modifiers changed from: private */
    public WXPerformanceV2OverlayView mPerformanceV2OverlayView;
    private PerformanceV2Repository mPerformanceV2Repository;
    /* access modifiers changed from: private */
    public ProfileDomView mProfileDomView;
    private RemoteVDomMonitor mRemoteVDomMonitor;
    /* access modifiers changed from: private */
    public ScalpelViewController mScalpelViewController;
    private ShakeDetector mShakeDetector;
    private boolean mShowDebugView = true;
    /* access modifiers changed from: private */
    public TrafficSampleView mTrafficSampleView;
    /* access modifiers changed from: private */
    public boolean shown = false;

    public WeexDevOptions(@NonNull Context context) {
        init(context, (Config) null);
    }

    public WeexDevOptions(@NonNull Context context, @Nullable Config config) {
        init(context, config);
    }

    public static void setWebSocketAdapter(IWebSocket iWebSocket) {
        sWebSocketImpl = iWebSocket;
    }

    public static IWebSocket getWebSocketImpl() {
        return sWebSocketImpl;
    }

    private Config provideDefaultConfig() {
        return new Config.Builder().enableShake(true).build();
    }

    private void init(@NonNull Context context, @Nullable Config config) {
        this.mContext = context;
        sApplicationContext = context.getApplicationContext();
        if (config == null) {
            config = provideDefaultConfig();
        }
        this.mConfig = config;
        this.mPerformanceV2Repository = PerformanceV2Repository.create(context);
        this.mPerformanceV2Repository.init();
        this.mDevOptionsConfig = DevOptionsConfig.getInstance(context);
        this.mPerfMonitorOverlayView = new PerfSampleOverlayView(context, config);
        this.mProfileDomView = new ProfileDomView(context, config);
        this.mProfileDomView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setVdomDepthEnabled(false);
                }
            }
        });
        this.mNetworkInspectorView = new NetworkInspectorView(context, config);
        this.mNetworkInspectorView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setNetworkInspectorEnabled(false);
                }
            }
        });
        this.mNetworkInspectorView.setOnSizeChangedListener(new IResizableView.OnSizeChangedListener() {
            public void onSizeChanged(int i) {
                WeexDevOptions.this.mDevOptionsConfig.setNetworkInspectorViewSize(i);
            }
        });
        this.mLogView = new LogView(context, config);
        this.mLogView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setLogOutputEnabled(false);
                }
            }
        });
        this.mLogView.setOnLogConfigChangedListener(new LogView.OnLogConfigChangedListener() {
            public void onLogLevelChanged(int i) {
                WeexDevOptions.this.mDevOptionsConfig.setLogLevel(i);
            }
        });
        this.mLogView.setOnSizeChangedListener(new IResizableView.OnSizeChangedListener() {
            public void onSizeChanged(int i) {
                WeexDevOptions.this.mDevOptionsConfig.setLogViewSize(i);
            }
        });
        this.mPerformanceV2OverlayView = new WXPerformanceV2OverlayView(context, this.mPerformanceV2Repository);
        this.mPerformanceV2OverlayView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setWeexPerformanceV2Enabled(false);
                }
            }
        });
        this.mMemorySampleView = new MemorySampleView(context, config);
        this.mMemorySampleView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setMemoryChartEnabled(false);
                }
            }
        });
        this.mCpuSampleView = new CpuSampleView(context, config);
        this.mCpuSampleView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setCpuChartEnabled(false);
                }
            }
        });
        this.mTrafficSampleView = new TrafficSampleView(context, config);
        this.mTrafficSampleView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setTrafficChartEnabled(false);
                }
            }
        });
        this.mFpsSampleView = new FpsSampleView(context, config);
        this.mFpsSampleView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setFpsChartEnabled(false);
                }
            }
        });
        this.mInspectorView = new InspectorView(context, config);
        this.mInspectorView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setViewInspectorEnabled(false);
                }
            }
        });
        this.mLogUploadOverlayView = new LogUploadOverlayView(context);
        this.mLogUploadOverlayView.setOnCloseListener(new IOverlayView.OnCloseListener() {
            public void close(IOverlayView iOverlayView) {
                if (iOverlayView != null) {
                    WeexDevOptions.this.mDevOptionsConfig.setUploadLogViewEnabled(false);
                }
            }
        });
        this.mShakeDetector = new ShakeDetector(new ShakeDetector.ShakeListener() {
            public void onShake() {
                WeexDevOptions.this.showDevOptions();
            }
        }, config);
        this.mRemoteVDomMonitor = new RemoteVDomMonitor(context);
        WXAnalyzerDataTransfer.isOpenPerformance = true;
    }

    private List<DevOption> registerDefaultOptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new DevOption("weex性能指标", R.drawable.wxt_icon_performance, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isWeexPerformanceV2Enabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setWeexPerformanceV2Enabled(false);
                    WeexDevOptions.this.mPerformanceV2OverlayView.dismiss();
                    return;
                }
                WeexDevOptions.this.mPerformanceV2OverlayView.bindInstance(WeexDevOptions.this.mInstance);
                WeexDevOptions.this.mDevOptionsConfig.setWeexPerformanceV2Enabled(true);
                WeexDevOptions.this.mPerformanceV2OverlayView.show();
            }
        }, true, true, true));
        arrayList.add(new DevOption("Weex日志上报", R.drawable.wxt_icon_upload, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isUploadLogViewEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setUploadLogViewEnabled(false);
                    WeexDevOptions.this.mLogUploadOverlayView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setUploadLogViewEnabled(true);
                WeexDevOptions.this.mLogUploadOverlayView.show();
            }
        }, true, true, false));
        arrayList.add(new DevOption("渲染性能分析", R.drawable.wxt_icon_render_analysis, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isVDomDepthEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setVdomDepthEnabled(false);
                    WeexDevOptions.this.mProfileDomView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setVdomDepthEnabled(true);
                WeexDevOptions.this.mProfileDomView.show();
                WeexDevOptions.this.mProfileDomView.bindInstance(WeexDevOptions.this.mInstance);
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_render_analysis", (Map<String, String>) null);
            }
        }, true, this.mProfileDomView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption("综合性能", R.drawable.wxt_icon_multi_performance, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isPerfCommonEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setPerfCommonEnabled(false);
                    WeexDevOptions.this.mPerfMonitorOverlayView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setPerfCommonEnabled(true);
                WeexDevOptions.this.mPerfMonitorOverlayView.show();
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_perf_overview", (Map<String, String>) null);
            }
        }, true, this.mPerfMonitorOverlayView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption("视图审查", R.drawable.wxt_icon_view_inspector, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isViewInspectorEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setViewInspectorEnabled(false);
                    WeexDevOptions.this.mInspectorView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setViewInspectorEnabled(true);
                WeexDevOptions.this.mInspectorView.show();
                WeexDevOptions.this.mInspectorView.bindInstance(WeexDevOptions.this.mInstance);
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_view_inspector", (Map<String, String>) null);
            }
        }, true, this.mInspectorView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption("网络监控", R.drawable.wxt_icon_network, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isNetworkInspectorEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setNetworkInspectorEnabled(false);
                    WeexDevOptions.this.mNetworkInspectorView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setNetworkInspectorEnabled(true);
                WeexDevOptions.this.mNetworkInspectorView.setViewSize(WeexDevOptions.this.mDevOptionsConfig.getNetworkInspectorViewSize());
                WeexDevOptions.this.mNetworkInspectorView.show();
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_network", (Map<String, String>) null);
            }
        }, true, this.mNetworkInspectorView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption("weex storage", R.drawable.wxt_icon_storage, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                new StorageView(WeexDevOptions.this.mContext, WeexDevOptions.this.mConfig).show();
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_storage", (Map<String, String>) null);
            }
        }, false, !this.mConfig.getIgnoreOptions().contains(Config.TYPE_STORAGE)));
        arrayList.add(new DevOption("3d视图", R.drawable.wxt_icon_3d_rotation, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mScalpelViewController != null) {
                    WeexDevOptions.this.mScalpelViewController.toggleScalpelEnabled();
                    UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_3d", (Map<String, String>) null);
                }
            }
        }, true, this.mScalpelViewController != null && this.mScalpelViewController.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption("日志", R.drawable.wxt_icon_log, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isLogOutputEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setLogOutputEnabled(false);
                    WeexDevOptions.this.mLogView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setLogOutputEnabled(true);
                WeexDevOptions.this.mLogView.setLogLevel(WeexDevOptions.this.mDevOptionsConfig.getLogLevel());
                WeexDevOptions.this.mLogView.setFilterName(WeexDevOptions.this.mDevOptionsConfig.getLogFilter());
                WeexDevOptions.this.mLogView.setViewSize(WeexDevOptions.this.mDevOptionsConfig.getLogViewSize());
                WeexDevOptions.this.mLogView.show();
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_logcat", (Map<String, String>) null);
            }
        }, true, this.mLogView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption("内存", R.drawable.wxt_icon_memory, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isMemoryChartEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setMemoryChartEnabled(false);
                    WeexDevOptions.this.mMemorySampleView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setMemoryChartEnabled(true);
                WeexDevOptions.this.mMemorySampleView.show();
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_memory", (Map<String, String>) null);
            }
        }, true, this.mMemorySampleView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption(Constants.CPU, R.drawable.wxt_icon_cpu, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isCPUChartEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setCpuChartEnabled(false);
                    WeexDevOptions.this.mCpuSampleView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setCpuChartEnabled(true);
                WeexDevOptions.this.mCpuSampleView.show();
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_cpu", (Map<String, String>) null);
            }
        }, true, this.mCpuSampleView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption(Config.TYPE_FPS, R.drawable.wxt_icon_fps, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (!FPSSampler.isSupported()) {
                    Toast.makeText(WeexDevOptions.this.mContext, "your device is not support.", 0).show();
                } else if (WeexDevOptions.this.mDevOptionsConfig.isFpsChartEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setFpsChartEnabled(false);
                    WeexDevOptions.this.mFpsSampleView.dismiss();
                } else {
                    WeexDevOptions.this.mDevOptionsConfig.setFpsChartEnabled(true);
                    WeexDevOptions.this.mFpsSampleView.show();
                    UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_fps", (Map<String, String>) null);
                }
            }
        }, true, this.mFpsSampleView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption("流量", R.drawable.wxt_icon_traffic, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                if (WeexDevOptions.this.mDevOptionsConfig.isTrafficChartEnabled()) {
                    WeexDevOptions.this.mDevOptionsConfig.setTrafficChartEnabled(false);
                    WeexDevOptions.this.mTrafficSampleView.dismiss();
                    return;
                }
                WeexDevOptions.this.mDevOptionsConfig.setTrafficChartEnabled(true);
                WeexDevOptions.this.mTrafficSampleView.show();
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_traffic", (Map<String, String>) null);
            }
        }, true, this.mTrafficSampleView.isPermissionGranted(this.mConfig)));
        arrayList.add(new DevOption("配置", R.drawable.wxt_icon_settings, new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                SettingsActivity.launch(WeexDevOptions.this.mContext);
                UserTrack.commit(WeexDevOptions.this.mContext, "wx_option_settings", (Map<String, String>) null);
            }
        }));
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void showDevOptions() {
        if (!this.shown && this.mContext != null) {
            if (!(this.mContext instanceof Activity) || !((Activity) this.mContext).isFinishing()) {
                EntranceView.Creator creator = new EntranceView.Creator(this.mContext);
                if (this.mExtraOptions != null && !this.mExtraOptions.isEmpty()) {
                    creator.injectOptions(this.mExtraOptions);
                }
                creator.injectOptions(registerDefaultOptions());
                EntranceView create = creator.create(this.mShowDebugView);
                create.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        boolean unused = WeexDevOptions.this.shown = false;
                    }
                });
                create.show();
                this.shown = true;
                UserTrack.commit(this.mContext, "show_weex_analyzer", (Map<String, String>) null);
            }
        }
    }

    public void registerExtraOption(@NonNull DevOption devOption) {
        if (this.mExtraOptions == null) {
            this.mExtraOptions = new ArrayList();
        }
        this.mExtraOptions.add(devOption);
    }

    public void setShowDebugView(boolean z) {
        this.mShowDebugView = z;
    }

    public void registerExtraOption(@NonNull String str, int i, @NonNull final Runnable runnable) {
        DevOption devOption = new DevOption();
        devOption.listener = new DevOption.OnOptionClickListener() {
            public void onOptionClick() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    Log.e("weex-analyzer", e.getMessage());
                }
            }
        };
        devOption.iconRes = i;
        devOption.optionName = str;
        registerExtraOption(devOption);
    }

    public void onCreate() {
        Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
        intent.putExtra("status", "create");
        intent.putExtra("type", "lifecycle");
        intent.putExtra(UNWRouter.PAGE_NAME, this.mCurPageName);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        if (this.mDevOptionsConfig.isLogOutputEnabled()) {
            this.mLogView.setLogLevel(this.mDevOptionsConfig.getLogLevel());
            this.mLogView.setFilterName(this.mDevOptionsConfig.getLogFilter());
            this.mLogView.setViewSize(this.mDevOptionsConfig.getLogViewSize());
            this.mLogView.show();
        } else {
            this.mLogView.dismiss();
        }
        if (this.mPerformanceV2Repository == null) {
            this.mPerformanceV2Repository = PerformanceV2Repository.create(this.mContext);
            this.mPerformanceV2Repository.init();
        }
    }

    public void onStart() {
        Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
        intent.putExtra("status", "start");
        intent.putExtra("type", "lifecycle");
        intent.putExtra(UNWRouter.PAGE_NAME, this.mCurPageName);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
    }

    public void onReceiveTouchEvent(MotionEvent motionEvent) {
        if (motionEvent != null && this.mInspectorView != null && this.mDevOptionsConfig.isViewInspectorEnabled()) {
            this.mInspectorView.receiveTouchEvent(motionEvent);
        }
    }

    public void onResume() {
        this.mShakeDetector.start((SensorManager) this.mContext.getSystemService("sensor"));
        if (this.mDevOptionsConfig.isPerfCommonEnabled()) {
            this.mPerfMonitorOverlayView.show();
        } else {
            this.mPerfMonitorOverlayView.dismiss();
        }
        if (this.mDevOptionsConfig.isVDomDepthEnabled()) {
            this.mProfileDomView.show();
            this.mProfileDomView.bindInstance(this.mInstance);
        } else {
            this.mProfileDomView.dismiss();
        }
        if (this.mDevOptionsConfig.isViewInspectorEnabled()) {
            this.mInspectorView.show();
            this.mInspectorView.bindInstance(this.mInstance);
        } else {
            this.mInspectorView.dismiss();
        }
        if (this.mDevOptionsConfig.isNetworkInspectorEnabled()) {
            this.mNetworkInspectorView.setViewSize(this.mDevOptionsConfig.getNetworkInspectorViewSize());
            this.mNetworkInspectorView.show();
        } else {
            this.mNetworkInspectorView.dismiss();
        }
        if (this.mDevOptionsConfig.isMemoryChartEnabled()) {
            this.mMemorySampleView.show();
        } else {
            this.mMemorySampleView.dismiss();
        }
        if (this.mDevOptionsConfig.isCPUChartEnabled()) {
            this.mCpuSampleView.show();
        } else {
            this.mCpuSampleView.dismiss();
        }
        if (this.mDevOptionsConfig.isFpsChartEnabled()) {
            this.mFpsSampleView.show();
        } else {
            this.mFpsSampleView.dismiss();
        }
        if (this.mDevOptionsConfig.isTrafficChartEnabled()) {
            this.mTrafficSampleView.show();
        } else {
            this.mTrafficSampleView.dismiss();
        }
        if (this.mDevOptionsConfig.isWeexPerformanceV2Enabled()) {
            this.mPerformanceV2OverlayView.show();
            this.mInspectorView.bindInstance(this.mInstance);
        } else {
            this.mPerformanceV2OverlayView.dismiss();
        }
        if (this.mDevOptionsConfig.isUploadLogViewEnabled()) {
            this.mLogUploadOverlayView.show();
        } else {
            this.mLogUploadOverlayView.dismiss();
        }
        if (this.mDevOptionsConfig.isLogOutputEnabled()) {
            this.mLogView.dismiss();
            this.mLogView.setLogLevel(this.mDevOptionsConfig.getLogLevel());
            this.mLogView.setFilterName(this.mDevOptionsConfig.getLogFilter());
            this.mLogView.setViewSize(this.mDevOptionsConfig.getLogViewSize());
            this.mLogView.show();
        } else {
            this.mLogView.dismiss();
        }
        if (this.mScalpelViewController != null) {
            this.mScalpelViewController.resume();
        }
        Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
        intent.putExtra("status", UCCore.EVENT_RESUME);
        intent.putExtra("type", "lifecycle");
        intent.putExtra(UNWRouter.PAGE_NAME, this.mCurPageName);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
    }

    public void onPause() {
        this.mShakeDetector.stop();
        if (this.mDevOptionsConfig.isPerfCommonEnabled()) {
            this.mPerfMonitorOverlayView.dismiss();
        }
        if (this.mDevOptionsConfig.isVDomDepthEnabled()) {
            this.mProfileDomView.dismiss();
        }
        if (this.mDevOptionsConfig.isViewInspectorEnabled()) {
            this.mInspectorView.dismiss();
        }
        if (this.mDevOptionsConfig.isNetworkInspectorEnabled()) {
            this.mNetworkInspectorView.dismiss();
        }
        if (this.mDevOptionsConfig.isLogOutputEnabled()) {
            this.mLogView.dismiss();
        }
        if (this.mDevOptionsConfig.isWeexPerformanceV2Enabled()) {
            this.mPerformanceV2OverlayView.dismiss();
        }
        if (this.mDevOptionsConfig.isUploadLogViewEnabled()) {
            this.mLogUploadOverlayView.dismiss();
        }
        if (this.mDevOptionsConfig.isMemoryChartEnabled()) {
            this.mMemorySampleView.dismiss();
        }
        if (this.mDevOptionsConfig.isFpsChartEnabled()) {
            this.mFpsSampleView.dismiss();
        }
        if (this.mDevOptionsConfig.isCPUChartEnabled()) {
            this.mCpuSampleView.dismiss();
        }
        if (this.mDevOptionsConfig.isTrafficChartEnabled()) {
            this.mTrafficSampleView.dismiss();
        }
        if (this.mScalpelViewController != null) {
            this.mScalpelViewController.pause();
        }
        Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
        intent.putExtra("status", "pause");
        intent.putExtra("type", "lifecycle");
        intent.putExtra(UNWRouter.PAGE_NAME, this.mCurPageName);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
    }

    public void onStop() {
        Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
        intent.putExtra("status", "stop");
        intent.putExtra("type", "lifecycle");
        intent.putExtra(UNWRouter.PAGE_NAME, this.mCurPageName);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
    }

    public void onDestroy() {
        if (this.mRemoteVDomMonitor != null) {
            this.mRemoteVDomMonitor.destroy();
            this.mRemoteVDomMonitor = null;
        }
        Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
        intent.putExtra("status", Constants.Event.SLOT_LIFECYCLE.DESTORY);
        intent.putExtra("type", "lifecycle");
        intent.putExtra(UNWRouter.PAGE_NAME, this.mCurPageName);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        this.mPerformanceV2Repository.destroy();
        this.mPerformanceV2Repository = null;
    }

    public void onWeexRenderSuccess(@Nullable WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance != null) {
            this.mInstance = wXSDKInstance;
            this.mCurPageName = WXPerfStorage.getInstance().savePerformance(wXSDKInstance);
            if (this.mCurPageName != null) {
                Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
                intent.putExtra(Config.TYPE_WEEX_PERFORMANCE_STATISTICS, JSON.toJSONString(WXPerfStorage.getInstance().getLatestPerformance(this.mCurPageName)));
                intent.putExtra("type", Config.TYPE_WEEX_PERFORMANCE_STATISTICS);
                LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
            }
            if (this.mRemoteVDomMonitor != null) {
                this.mRemoteVDomMonitor.monitor(wXSDKInstance);
            }
            if (this.mProfileDomView != null) {
                this.mProfileDomView.bindInstance(wXSDKInstance);
            }
            if (this.mInspectorView != null) {
                this.mInspectorView.bindInstance(wXSDKInstance);
            }
            if (this.mPerformanceV2OverlayView != null) {
                this.mPerformanceV2OverlayView.bindInstance(wXSDKInstance);
            }
        }
    }

    public void onWindmillRenderSuccess(@Nullable Object obj) {
        if (obj != null && (obj instanceof WXSDKInstance)) {
            onWeexRenderSuccess((WXSDKInstance) obj);
        }
    }

    public View onWeexViewCreated(WXSDKInstance wXSDKInstance, View view) {
        if (view == null || view.getContext() == null) {
            return null;
        }
        if (view.getParent() != null) {
            return view;
        }
        this.mScalpelViewController = new ScalpelViewController(this.mContext, this.mConfig);
        this.mScalpelViewController.setOnToggleListener(new ScalpelViewController.OnToggleListener() {
            public void onToggle(View view, boolean z) {
                Context access$600 = WeexDevOptions.this.mContext;
                StringBuilder sb = new StringBuilder();
                sb.append("3d layer is ");
                sb.append(z ? "enabled" : "disabled");
                Toast.makeText(access$600, sb.toString(), 0).show();
            }
        });
        this.mScalpelViewController.setOnDrawViewNameListener(new ScalpelFrameLayout.OnDrawViewNameListener() {
            @Nullable
            public String onDrawViewName(@NonNull View view, @NonNull String str) {
                for (String equalsIgnoreCase : DevOptionsConfig.WHITE_SCALPEL_VIEW_NAMES) {
                    if (str.equalsIgnoreCase(equalsIgnoreCase)) {
                        return str;
                    }
                }
                return null;
            }
        });
        return this.mScalpelViewController.wrapView(view);
    }

    public View onWindmillViewCreated(View view) {
        return onWeexViewCreated((WXSDKInstance) null, view);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (!SDKUtils.isEmulator() || i != 82) {
            return false;
        }
        showDevOptions();
        return true;
    }

    public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
        if (this.mDevOptionsConfig != null && this.mDevOptionsConfig.isShownJSException()) {
            try {
                JSExceptionCatcher.catchException(this.mContext, this.mDevOptionsConfig, wXSDKInstance, str, str2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onWindmillException(Object obj, String str, String str2) {
        if (obj == null) {
            onException((WXSDKInstance) null, str, str2);
        } else if (obj instanceof WXSDKInstance) {
            onException((WXSDKInstance) obj, str, str2);
        }
    }

    public void onReceiveWindmillPerformanceLog(String str) {
        if (!TextUtils.isEmpty(str)) {
            Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
            intent.putExtra(Config.TYPE_WINDMILL_PERFORMANCE_STATISTICS, str);
            intent.putExtra("type", Config.TYPE_WINDMILL_PERFORMANCE_STATISTICS);
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        }
    }
}
