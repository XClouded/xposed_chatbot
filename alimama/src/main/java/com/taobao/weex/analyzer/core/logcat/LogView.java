package com.taobao.weex.analyzer.core.logcat;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.logcat.LogcatDumper;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.overlay.AbstractResizableOverlayView;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.SimpleOverlayView;
import com.taobao.weex.utils.WXLogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogView extends AbstractResizableOverlayView {
    private static final int BTN_DISABLED_COLOR = 16777215;
    private static final int BTN_ENABLED_COLOR = -1127359431;
    /* access modifiers changed from: private */
    public boolean isJSLogPanelDisplayed = true;
    /* access modifiers changed from: private */
    public boolean isSettingOpened;
    /* access modifiers changed from: private */
    public boolean isSizeMenuOpened;
    /* access modifiers changed from: private */
    public SimpleOverlayView mCollapsedView;
    /* access modifiers changed from: private */
    public OnLogConfigChangedListener mConfigChangeListener;
    /* access modifiers changed from: private */
    public View mJSLogBtn;
    /* access modifiers changed from: private */
    public DisplayLogItemView mJSLogPanel;
    /* access modifiers changed from: private */
    public int mLogLevel;
    /* access modifiers changed from: private */
    public LogcatDumper mLogcatDumper;
    /* access modifiers changed from: private */
    public View mNativeLogBtn;
    /* access modifiers changed from: private */
    public DisplayLogItemView mNativeLogPanel;
    /* access modifiers changed from: private */
    public IOverlayView.OnCloseListener mOnCloseListener;
    /* access modifiers changed from: private */
    public onStatusChangedListener mStatusChangedListener;

    public interface OnLogConfigChangedListener {
        void onLogLevelChanged(int i);
    }

    public interface onStatusChangedListener {
        void onCollapsed();

        void onExpanded();
    }

    @Deprecated
    public void setFilterName(String str) {
    }

    public LogView(Context context, Config config) {
        super(context, config);
        this.mWidth = -1;
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains("log");
    }

    public void setOnCloseListener(@Nullable IOverlayView.OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public void setOnLogConfigChangedListener(@Nullable OnLogConfigChangedListener onLogConfigChangedListener) {
        this.mConfigChangeListener = onLogConfigChangedListener;
    }

    public void setOnStatusChangedListener(@Nullable onStatusChangedListener onstatuschangedlistener) {
        this.mStatusChangedListener = onstatuschangedlistener;
    }

    public void setLogLevel(int i) {
        this.mLogLevel = i;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        View inflate = View.inflate(this.mContext, R.layout.wxt_log_view, (ViewGroup) null);
        final View findViewById = inflate.findViewById(R.id.hold);
        View findViewById2 = inflate.findViewById(R.id.clear);
        View findViewById3 = inflate.findViewById(R.id.close);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.level_group);
        RadioGroup radioGroup2 = (RadioGroup) inflate.findViewById(R.id.height_group);
        View findViewById4 = inflate.findViewById(R.id.collapse);
        final ViewGroup viewGroup = (ViewGroup) inflate.findViewById(R.id.setting_content);
        final ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(R.id.size_content);
        this.mNativeLogPanel = (DisplayLogItemView) inflate.findViewById(R.id.native_log_panel);
        this.mJSLogPanel = (DisplayLogItemView) inflate.findViewById(R.id.js_log_panel);
        this.mJSLogBtn = inflate.findViewById(R.id.btn_panel_js_log);
        this.mNativeLogBtn = inflate.findViewById(R.id.btn_panel_native_log);
        this.mJSLogBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LogView.this.mJSLogBtn.setBackgroundColor(LogView.BTN_ENABLED_COLOR);
                LogView.this.mNativeLogBtn.setBackgroundColor(16777215);
                LogView.this.mJSLogPanel.setVisibility(0);
                LogView.this.mNativeLogPanel.setVisibility(4);
                boolean unused = LogView.this.isJSLogPanelDisplayed = true;
            }
        });
        this.mNativeLogBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LogView.this.mNativeLogBtn.setBackgroundColor(LogView.BTN_ENABLED_COLOR);
                LogView.this.mJSLogBtn.setBackgroundColor(16777215);
                LogView.this.mJSLogPanel.setVisibility(4);
                LogView.this.mNativeLogPanel.setVisibility(0);
                boolean unused = LogView.this.isJSLogPanelDisplayed = false;
            }
        });
        ((TextView) inflate.findViewById(R.id.size)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean unused = LogView.this.isSizeMenuOpened = !LogView.this.isSizeMenuOpened;
                if (LogView.this.isSizeMenuOpened) {
                    viewGroup2.setVisibility(0);
                } else {
                    viewGroup2.setVisibility(8);
                }
            }
        });
        ((TextView) inflate.findViewById(R.id.settings)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean unused = LogView.this.isSettingOpened = !LogView.this.isSettingOpened;
                if (LogView.this.isSettingOpened) {
                    viewGroup.setVisibility(0);
                } else {
                    viewGroup.setVisibility(8);
                }
            }
        });
        findViewById4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LogView.this.performCollapse();
            }
        });
        ((TextView) inflate.findViewById(R.id.copy_all)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!LogView.this.isViewAttached) {
                    return;
                }
                if (LogView.this.isJSLogPanelDisplayed) {
                    if (LogView.this.mJSLogPanel.getLogAdapter() != null) {
                        try {
                            SDKUtils.copyToClipboard(view.getContext(), LogView.this.mJSLogPanel.getLogAdapter().dumpAll(), true);
                        } catch (Throwable unused) {
                            Toast.makeText(view.getContext(), "copy failed", 0).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "copy failed", 0).show();
                    }
                } else if (LogView.this.mNativeLogPanel.getLogAdapter() != null) {
                    try {
                        SDKUtils.copyToClipboard(view.getContext(), LogView.this.mNativeLogPanel.getLogAdapter().dumpAll(), true);
                    } catch (Throwable unused2) {
                        Toast.makeText(view.getContext(), "copy failed", 0).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "copy failed", 0).show();
                }
            }
        });
        if (!(this.mJSLogPanel == null || this.mNativeLogPanel == null || this.mJSLogPanel.getContentView() == null || this.mNativeLogPanel.getContentView() == null)) {
            setViewSize(this.mViewSize, this.mJSLogPanel.getContentView(), false);
            setViewSize(this.mViewSize, this.mNativeLogPanel.getContentView(), false);
            switch (this.mViewSize) {
                case 0:
                    ((RadioButton) inflate.findViewById(R.id.height_small)).setChecked(true);
                    break;
                case 1:
                    ((RadioButton) inflate.findViewById(R.id.height_medium)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton) inflate.findViewById(R.id.height_large)).setChecked(true);
                    break;
            }
            radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.height_small) {
                        int unused = LogView.this.mViewSize = 0;
                    } else if (i == R.id.height_medium) {
                        int unused2 = LogView.this.mViewSize = 1;
                    } else if (i == R.id.height_large) {
                        int unused3 = LogView.this.mViewSize = 2;
                    }
                    LogView.this.setViewSize(LogView.this.mViewSize, LogView.this.mJSLogPanel.getContentView(), true);
                    LogView.this.setViewSize(LogView.this.mViewSize, LogView.this.mNativeLogPanel.getContentView(), true);
                }
            });
        }
        switch (this.mLogLevel) {
            case 2:
                ((RadioButton) inflate.findViewById(R.id.level_v)).setChecked(true);
                break;
            case 3:
                ((RadioButton) inflate.findViewById(R.id.level_d)).setChecked(true);
                break;
            case 4:
                ((RadioButton) inflate.findViewById(R.id.level_i)).setChecked(true);
                break;
            case 5:
                ((RadioButton) inflate.findViewById(R.id.level_w)).setChecked(true);
                break;
            case 6:
                ((RadioButton) inflate.findViewById(R.id.level_e)).setChecked(true);
                break;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (LogView.this.mLogcatDumper != null) {
                    if (LogView.this.mNativeLogPanel.getLogAdapter() != null) {
                        LogView.this.mNativeLogPanel.getLogAdapter().clear();
                    }
                    int access$1500 = LogView.this.mLogLevel;
                    if (i == R.id.level_i) {
                        access$1500 = 4;
                    } else if (i == R.id.level_v) {
                        access$1500 = 2;
                    } else if (i == R.id.level_d) {
                        access$1500 = 3;
                    } else if (i == R.id.level_e) {
                        access$1500 = 6;
                    } else if (i == R.id.level_w) {
                        access$1500 = 5;
                    }
                    if (access$1500 != LogView.this.mLogLevel) {
                        int unused = LogView.this.mLogLevel = access$1500;
                        LogView.this.mLogcatDumper.setLevel(LogView.this.mLogLevel);
                        if (LogView.this.mConfigChangeListener != null) {
                            LogView.this.mConfigChangeListener.onLogLevelChanged(LogView.this.mLogLevel);
                        }
                    }
                    LogView.this.mLogcatDumper.findCachedLogByNewFilters();
                }
            }
        });
        findViewById.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (LogView.this.mNativeLogPanel.getLogAdapter() == null || !LogView.this.isViewAttached) {
                    return;
                }
                if (LogView.this.mNativeLogPanel.getLogAdapter().isHoldModeEnabled()) {
                    LogView.this.mNativeLogPanel.getLogAdapter().setHoldModeEnabled(false);
                    ((TextView) findViewById).setText("hold(off)");
                    return;
                }
                LogView.this.mNativeLogPanel.getLogAdapter().setHoldModeEnabled(true);
                ((TextView) findViewById).setText("hold(on)");
            }
        });
        findViewById2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!LogView.this.isViewAttached) {
                    return;
                }
                if (LogView.this.isJSLogPanelDisplayed) {
                    if (LogView.this.mJSLogPanel.getLogAdapter() != null) {
                        LogView.this.mJSLogPanel.getLogAdapter().clear();
                    }
                } else if (LogView.this.mNativeLogPanel.getLogAdapter() != null) {
                    LogView.this.mNativeLogPanel.getLogAdapter().clear();
                    if (LogView.this.mLogcatDumper != null) {
                        LogView.this.mLogcatDumper.clearCachedLog();
                    }
                }
            }
        });
        findViewById3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (LogView.this.isViewAttached && LogView.this.mOnCloseListener != null) {
                    LogView.this.mOnCloseListener.close(LogView.this);
                    LogView.this.dismiss();
                }
            }
        });
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onShown() {
        this.mLogcatDumper = new LogcatDumpBuilder().listener(new LogcatDumper.OnLogReceivedListener() {
            public void onReceived(@NonNull List<LogcatDumper.LogInfo> list) {
                if (LogView.this.mNativeLogPanel.getLogAdapter() != null) {
                    LogView.this.mNativeLogPanel.getLogAdapter().addLog(list);
                }
                if (LogView.this.mJSLogPanel.getLogAdapter() != null) {
                    for (LogcatDumper.LogInfo next : list) {
                        if (next.message != null && next.message.contains("jsLog")) {
                            LogView.this.mJSLogPanel.getLogAdapter().addLog(next);
                        }
                    }
                }
            }
        }).level(this.mLogLevel).enableCache(true).cacheLimit(1000).build();
        this.mLogcatDumper.beginDump();
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
        if (this.mLogcatDumper != null) {
            this.mLogcatDumper.destroy();
            this.mLogcatDumper = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.mCollapsedView != null) {
            this.mCollapsedView.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void performCollapse() {
        if (this.mStatusChangedListener != null) {
            this.mStatusChangedListener.onCollapsed();
        }
        dismiss();
        if (this.mCollapsedView == null) {
            this.mCollapsedView = new SimpleOverlayView.Builder(this.mContext, "Log").listener(new SimpleOverlayView.OnClickListener() {
                public void onClick(@NonNull IOverlayView iOverlayView) {
                    LogView.this.mCollapsedView.dismiss();
                    if (LogView.this.mStatusChangedListener != null) {
                        LogView.this.mStatusChangedListener.onExpanded();
                    }
                    LogView.this.show();
                }
            }).build();
        }
        this.mCollapsedView.show();
    }

    static class JSLogWatcherProxy implements WXLogUtils.JsLogWatcher {
        /* access modifiers changed from: private */
        public SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss : ", Locale.getDefault());
        /* access modifiers changed from: private */
        public WXLogUtils.JsLogWatcher logWatcher;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        JSLogWatcherProxy(WXLogUtils.JsLogWatcher jsLogWatcher) {
            this.logWatcher = jsLogWatcher;
        }

        public void onJsLog(final int i, final String str) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (JSLogWatcherProxy.this.logWatcher != null) {
                        WXLogUtils.JsLogWatcher access$2300 = JSLogWatcherProxy.this.logWatcher;
                        int i = i;
                        access$2300.onJsLog(i, JSLogWatcherProxy.this.formatter.format(new Date(System.currentTimeMillis())) + str);
                    }
                }
            });
        }
    }
}
