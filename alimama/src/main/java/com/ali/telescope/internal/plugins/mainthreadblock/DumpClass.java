package com.ali.telescope.internal.plugins.mainthreadblock;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import com.ali.telescope.util.ByteUtils;
import com.ali.telescope.util.TelescopeLog;
import dalvik.system.VMStack;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DumpClass {
    private static final int METHOD_ENTER = 0;
    private static final int METHOD_EXIT = 1;
    private ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream(4096);
    private Context mContext;
    private byte[] mHoleBytes = new byte[14];
    private Map<Integer, Integer> mIdToPosition = new HashMap();
    private StackTraceElement[] mLastTrace;
    private List<StackTraceElement> mMethods = new ArrayList();
    private int mNumMethodCalls;
    private byte[] mTidBytes = {1, 0};

    public DumpClass(Context context) {
        this.mContext = context;
    }

    public void startSample() {
        TelescopeLog.d("startSample = > ", new Object[0]);
        this.mLastTrace = null;
        this.mNumMethodCalls = 0;
        this.mMethods.clear();
        this.mIdToPosition.clear();
        this.mByteArrayOutputStream.reset();
        initHeader();
    }

    public void doSample(int i, int i2) {
        StackTraceElement[] threadStackTrace = VMStack.getThreadStackTrace(Looper.getMainLooper().getThread());
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
    }

    public void finishSample(int i, int i2, int i3) {
        int i4 = i3 + 1;
        TelescopeLog.d("finishSample = > wallClockDiff: " + i2 + " cpuClockDiff: " + i4, new Object[0]);
        if (this.mLastTrace != null) {
            for (StackTraceElement onMethodExit : this.mLastTrace) {
                onMethodExit(onMethodExit, i2, i4);
            }
            output(i2, i4);
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

    private void output(int i, int i2) {
        try {
            TelescopeLog.d("output is begin ....", new Object[0]);
            StringBuilder sb = new StringBuilder();
            sb.append("*version\n");
            sb.append("3\n");
            sb.append("data-file-overflow=false\n");
            sb.append("clock=dual\n");
            sb.append("elapsed-time-usec=" + i + "\n");
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
            File externalFilesDir = this.mContext.getExternalFilesDir((String) null);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(externalFilesDir, System.currentTimeMillis() + ".trace"));
            fileOutputStream.write(sb.toString().getBytes(Charset.forName("utf-8")));
            fileOutputStream.write(this.mByteArrayOutputStream.toByteArray());
            fileOutputStream.close();
            TelescopeLog.d("output is finish ....", new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
