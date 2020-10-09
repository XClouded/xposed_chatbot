package com.ali.telescope.internal.plugins.mainthreadblock;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.util.Base64;
import androidx.annotation.Keep;
import com.ali.telescope.base.event.ActivityEvent;
import com.ali.telescope.base.event.AppEvent;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.base.plugin.PluginIDContant;
import com.ali.telescope.internal.data.DataManagerProxy;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.internal.plugins.PhonePerformanceLevel;
import com.ali.telescope.util.ByteUtils;
import com.ali.telescope.util.TelescopeLog;
import com.ali.user.mobile.login.model.LoginConstant;
import com.taobao.android.tlog.protocol.model.joint.point.BackgroundJointPoint;
import dalvik.system.VMStack;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.json.JSONObject;

public class MainThreadBlockPlugin extends Plugin {
    /* access modifiers changed from: private */
    public String currentPageName = "";
    /* access modifiers changed from: private */
    public boolean isBackGround = true;
    private boolean isDestroy;
    /* access modifiers changed from: private */
    public boolean isPause;
    /* access modifiers changed from: private */
    public boolean mIsDebug;
    /* access modifiers changed from: private */
    public long mLastPauseTime;
    /* access modifiers changed from: private */
    public int mSampleLimitTime = 2000;
    /* access modifiers changed from: private */
    public int mStartTime = 1500;
    /* access modifiers changed from: private */
    public ITelescopeContext mTelescopeContext;

    public void onCreate(final Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        this.boundType = 2;
        iTelescopeContext.registerBroadcast(1, this.pluginID);
        iTelescopeContext.registerBroadcast(2, this.pluginID);
        this.mTelescopeContext = iTelescopeContext;
        if (jSONObject != null) {
            this.mStartTime = jSONObject.optInt(LoginConstant.START_TIME, 1500);
            this.mIsDebug = jSONObject.optBoolean("debug", false);
            this.mSampleLimitTime = jSONObject.optInt("sampleLimitTime", this.mSampleLimitTime);
        }
        if (PhonePerformanceLevel.getLevel() == 2) {
            this.mStartTime = (this.mStartTime * 3) / 2;
        } else if (PhonePerformanceLevel.getLevel() == 3) {
            this.mStartTime *= 2;
        }
        AnonymousClass1 r5 = new Runnable() {
            public void run() {
                try {
                    BlockMonitor.init(application, MainThreadBlockPlugin.this.mStartTime, MainThreadBlockPlugin.this.mSampleLimitTime, 5, new SampleClass(application), SampleClass.class.getDeclaredMethod("startSample", new Class[0]), SampleClass.class.getDeclaredMethod("doSample", new Class[]{Integer.TYPE, Integer.TYPE}), SampleClass.class.getDeclaredMethod("finishSample", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            r5.run();
        } else {
            Loopers.getUiHandler().post(r5);
        }
    }

    public void onResume(int i, int i2) {
        super.onResume(i, i2);
        this.isPause = false;
    }

    public void onPause(int i, int i2) {
        super.onPause(i, i2);
        this.mLastPauseTime = System.currentTimeMillis();
        this.isPause = true;
    }

    public boolean isPaused() {
        return this.isPause;
    }

    public void onEvent(int i, Event event) {
        super.onEvent(i, event);
        boolean z = true;
        if (i == 1) {
            ActivityEvent activityEvent = (ActivityEvent) event;
            if (activityEvent.subEvent == 3) {
                this.currentPageName = activityEvent.target.getClass().getName();
            }
        } else if (i == 2) {
            if (((AppEvent) event).subEvent != 1) {
                z = false;
            }
            this.isBackGround = z;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.isDestroy = true;
    }

    @Keep
    public class SampleClass {
        private static final int METHOD_ENTER = 0;
        private static final int METHOD_EXIT = 1;
        private ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream(4096);
        private Context mContext;
        private byte[] mHoleBytes = new byte[14];
        private Map<Integer, Integer> mIdToPosition = new HashMap();
        private StackTraceElement[] mLastTrace;
        private List<StackTraceElement> mMethods = new ArrayList();
        private int mNumMethodCalls;
        private int mSampleTimes;
        private byte[] mTidBytes = {1, 0};

        public SampleClass(Context context) {
            this.mContext = context;
        }

        public void startSample() {
            TelescopeLog.d("startSample = > ", new Object[0]);
            this.mLastTrace = null;
            this.mNumMethodCalls = 0;
            this.mMethods.clear();
            this.mIdToPosition.clear();
            this.mSampleTimes = 0;
            initHeader();
        }

        public void doSample(int i, int i2) {
            StackTraceElement[] threadStackTrace = VMStack.getThreadStackTrace(Looper.getMainLooper().getThread());
            System.nanoTime();
            if (this.mLastTrace == null) {
                this.mLastTrace = threadStackTrace;
                for (int length = this.mLastTrace.length - 1; length >= 0; length--) {
                    onMethodEnter(this.mLastTrace[length], i, i2);
                }
            } else {
                int length2 = this.mLastTrace.length - 1;
                int length3 = threadStackTrace.length - 1;
                while (length2 >= 0 && length3 >= 0 && isSameMethod(this.mLastTrace[length2], threadStackTrace[length3])) {
                    length2--;
                    length3--;
                }
                for (int i3 = 0; i3 <= length2; i3++) {
                    onMethodExit(this.mLastTrace[i3], i, i2);
                }
                while (length3 >= 0) {
                    onMethodEnter(threadStackTrace[length3], i, i2);
                    length3--;
                }
            }
            this.mLastTrace = threadStackTrace;
            this.mSampleTimes++;
        }

        public void finishSample(int i, int i2, int i3) {
            int i4 = i3 + 1;
            if (this.mLastTrace != null) {
                for (StackTraceElement onMethodExit : this.mLastTrace) {
                    onMethodExit(onMethodExit, i2, i4);
                }
                output(i, i2, i4);
            }
        }

        private void onMethodEnter(StackTraceElement stackTraceElement, int i, int i2) {
            methodEvent(stackTraceElement, true, i, i2);
            this.mNumMethodCalls++;
        }

        private void onMethodExit(StackTraceElement stackTraceElement, int i, int i2) {
            methodEvent(stackTraceElement, false, i, i2);
        }

        private void initHeader() {
            try {
                this.mByteArrayOutputStream.reset();
                this.mByteArrayOutputStream.write(ByteUtils.int2Bytes(1464814675));
                this.mByteArrayOutputStream.write(ByteUtils.short2Bytes(3));
                this.mByteArrayOutputStream.write(ByteUtils.short2Bytes(32));
                this.mByteArrayOutputStream.write(ByteUtils.long2Bytes(System.currentTimeMillis()));
                this.mByteArrayOutputStream.write(ByteUtils.short2Bytes(14));
                this.mByteArrayOutputStream.write(this.mHoleBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void methodEvent(StackTraceElement stackTraceElement, boolean z, int i, int i2) {
            boolean encodeTraceMethod = (encodeTraceMethod(stackTraceElement) << 2) | (!z);
            try {
                this.mByteArrayOutputStream.write(this.mTidBytes);
                this.mByteArrayOutputStream.write(ByteUtils.int2Bytes(encodeTraceMethod ? 1 : 0));
                this.mByteArrayOutputStream.write(ByteUtils.int2Bytes(i2));
                this.mByteArrayOutputStream.write(ByteUtils.int2Bytes(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private int encodeTraceMethod(StackTraceElement stackTraceElement) {
            int hashCode = (stackTraceElement.getClassName().hashCode() * stackTraceElement.getMethodName().hashCode()) + stackTraceElement.getLineNumber();
            if (!this.mIdToPosition.containsKey(Integer.valueOf(hashCode))) {
                this.mMethods.add(stackTraceElement);
                this.mIdToPosition.put(Integer.valueOf(hashCode), Integer.valueOf(this.mMethods.size() - 1));
            }
            return this.mIdToPosition.get(Integer.valueOf(hashCode)).intValue();
        }

        private boolean isSameMethod(StackTraceElement stackTraceElement, StackTraceElement stackTraceElement2) {
            return stackTraceElement.getMethodName().equals(stackTraceElement2.getMethodName()) && stackTraceElement.getClassName().equals(stackTraceElement2.getClassName()) && stackTraceElement.getLineNumber() == stackTraceElement2.getLineNumber();
        }

        private void output(int i, int i2, int i3) {
            String str;
            try {
                if (this.mSampleTimes < 10) {
                    return;
                }
                if (!MainThreadBlockPlugin.this.isPause) {
                    TelescopeLog.d("output is begin ....", new Object[0]);
                    StringBuilder sb = new StringBuilder();
                    sb.append("*extra-info\n");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("current-page=");
                    if (MainThreadBlockPlugin.this.currentPageName == null) {
                        str = "";
                    } else {
                        str = MainThreadBlockPlugin.this.currentPageName + "\n";
                    }
                    sb2.append(str);
                    sb.append(sb2.toString());
                    sb.append("msg-time=" + i + "\n");
                    sb.append("sample-times=" + this.mSampleTimes + "\n");
                    sb.append("time=" + System.currentTimeMillis() + "\n");
                    sb.append("last-pause-time=" + MainThreadBlockPlugin.this.mLastPauseTime + "\n");
                    sb.append("*extra-info-end\n");
                    sb.append("*version\n");
                    sb.append("3\n");
                    sb.append("data-file-overflow=false\n");
                    sb.append("clock=dual\n");
                    sb.append("elapsed-time-usec=" + i2 + "\n");
                    sb.append("num-method-calls=" + this.mNumMethodCalls + "\n");
                    sb.append("clock-call-overhead-nsec=0\n");
                    sb.append("vm=art\n");
                    sb.append("pid=" + Process.myPid() + "\n");
                    sb.append("*threads\n");
                    sb.append("1\tmain\n");
                    sb.append("*methods\n");
                    for (StackTraceElement next : this.mMethods) {
                        sb.append("0x" + Integer.toHexString(encodeTraceMethod(next) << 2) + "\t" + next.getClassName() + "\t" + next.getMethodName() + "#" + next.getLineNumber() + "\t()V" + "\t" + next.getFileName() + "\n");
                    }
                    sb.append("*end\n");
                    byte[] merge = ByteUtils.merge(sb.toString().getBytes(Charset.forName("utf-8")), this.mByteArrayOutputStream.toByteArray());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mContext.getExternalFilesDir((String) null), "ok.txt"));
                    fileOutputStream.write(merge);
                    fileOutputStream.close();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                    gZIPOutputStream.write(merge);
                    gZIPOutputStream.close();
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    if (byteArray != null) {
                        if (byteArray.length != 0) {
                            String encodeToString = Base64.encodeToString(byteArray, 2);
                            String access$300 = MainThreadBlockPlugin.this.isBackGround ? BackgroundJointPoint.TYPE : MainThreadBlockPlugin.this.currentPageName;
                            MainThreadBlockPlugin.this.mTelescopeContext.getBeanReport().send(new MainThreadBlockReportBean2(System.currentTimeMillis(), encodeToString, access$300));
                            if (MainThreadBlockPlugin.this.mIsDebug) {
                                DataManagerProxy.instance().putData(PluginIDContant.KEY_MAINTHREADBLOCKPLUGIN, access$300, sb.toString());
                            }
                            TelescopeLog.d("output is finish , before size : " + merge.length + " decode size : " + encodeToString.length(), new Object[0]);
                            return;
                        }
                    }
                    TelescopeLog.e("bytes compressed failed", new Object[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
