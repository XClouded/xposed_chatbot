package com.taobao.accs.data;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.net.BaseConnection;
import com.taobao.accs.ut.monitor.NetPerformanceMonitor;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.JsonUtility;
import com.taobao.accs.utl.MessageStreamBuilder;
import com.taobao.accs.utl.RomInfoCollecter;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.weex.el.parse.Operators;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
import org.json.JSONException;

public class Message implements Serializable {
    public static int CONTROL_MAX_RETRY_TIMES = 5;
    public static final int EXT_HEADER_VALUE_MAX_LEN = 1023;
    public static final int FLAG_ACK_TYPE = 32;
    public static final int FLAG_BIZ_RET = 64;
    public static final int FLAG_DATA_TYPE = 32768;
    public static final int FLAG_ERR = 4096;
    public static final int FLAG_REQ_BIT1 = 16384;
    public static final int FLAG_REQ_BIT2 = 8192;
    public static final int FLAG_RET = 2048;
    public static final String KEY_BINDAPP = "ctrl_bindapp";
    public static final String KEY_BINDSERVICE = "ctrl_bindservice";
    public static final String KEY_BINDUSER = "ctrl_binduser";
    public static final String KEY_UNBINDAPP = "ctrl_unbindapp";
    public static final String KEY_UNBINDSERVICE = "ctrl_unbindservice";
    public static final String KEY_UNBINDUSER = "ctrl_unbinduser";
    public static final int MAX_RETRY_TIMES = 3;
    private static final String TAG = "Msg";
    static long baseMessageId = 1;
    String appKey = null;
    public String appSign = null;
    String appVersion = null;
    public String bizId = null;
    String brand = null;
    public Integer command = null;
    byte compress = 0;
    public String cunstomDataId;
    byte[] data;
    public String dataId;
    short dataLength;
    public long delyTime = 0;
    Map<Integer, String> extHeader;
    short extHeaderLength;
    String exts = null;
    short flags;
    public boolean force = false;
    public URL host;
    String imei = null;
    String imsi = null;
    public boolean isAck = false;
    public boolean isCancel = false;
    String macAddress = null;
    String model = null;
    Id msgId;
    transient NetPerformanceMonitor netPerformanceMonitor;
    byte noUse = 0;
    int node;
    String notifyEnable = null;
    Integer osType = null;
    String osVersion = null;
    String packageName = null;
    public int retryTimes = 0;
    Integer sdkVersion = null;
    long sendTime;
    public String serviceId = null;
    String source;
    byte sourceLength;
    public long startSendTime;
    String tag = null;
    String target;
    byte targetLength;
    public int timeout = 40000;
    short totalLength;
    String ttid = null;
    int type = -1;
    Integer updateDevice = 0;
    public String userinfo = null;
    String venderOsName = null;
    String venderOsVersion = null;

    public static class MsgResType implements Serializable {
        public static final int INVALID = -1;
        public static final int NEED_ACK = 1;
        public static final int NO_ACK = 0;

        public static String name(int i) {
            switch (i) {
                case 0:
                    return "NO_ACK";
                case 1:
                    return "NEED_ACK";
                default:
                    return "INVALID";
            }
        }

        public static int valueOf(int i) {
            switch (i) {
                case 0:
                    return 0;
                case 1:
                    return 1;
                default:
                    return 1;
            }
        }
    }

    public static class MsgType implements Serializable {
        public static final int CONTROL = 0;
        public static final int DATA = 1;
        public static final int HANDSHAKE = 3;
        public static final int INVALID = -1;
        public static final int PING = 2;

        public static String name(int i) {
            switch (i) {
                case 0:
                    return "CONTROL";
                case 1:
                    return "DATA";
                case 2:
                    return "PING";
                case 3:
                    return "HANDSHAKE";
                default:
                    return "INVALID";
            }
        }

        public static int valueOf(int i) {
            switch (i) {
                case 0:
                    return 0;
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                default:
                    return 0;
            }
        }
    }

    public enum ReqType {
        DATA,
        ACK,
        REQ,
        RES;

        public static ReqType valueOf(int i) {
            switch (i) {
                case 0:
                    return DATA;
                case 1:
                    return ACK;
                case 2:
                    return REQ;
                case 3:
                    return RES;
                default:
                    return DATA;
            }
        }
    }

    public static class Id implements Serializable {
        private String dataId;
        private int id;

        public Id(int i, String str) {
            this.id = i;
            this.dataId = str;
        }

        public int getId() {
            return this.id;
        }

        public String getDataId() {
            return this.dataId;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Id id2 = (Id) obj;
            if (this.id == id2.getId() || this.dataId.equals(id2.getDataId())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.dataId.hashCode();
        }
    }

    private Message() {
        synchronized (Message.class) {
            this.startSendTime = System.currentTimeMillis();
            this.dataId = String.valueOf(this.startSendTime) + "." + String.valueOf(baseMessageId);
            long j = baseMessageId;
            baseMessageId = 1 + j;
            this.msgId = new Id((int) j, this.dataId);
        }
    }

    public int getNode() {
        return this.node;
    }

    public int getType() {
        return this.type;
    }

    public String getDataId() {
        return this.dataId;
    }

    public boolean isControlFrame() {
        return Constants.TARGET_CONTROL.equals(this.target);
    }

    public Id getMsgId() {
        return this.msgId;
    }

    public void setSendTime(long j) {
        this.sendTime = j;
    }

    public NetPerformanceMonitor getNetPermanceMonitor() {
        return this.netPerformanceMonitor;
    }

    public long getDelyTime() {
        return this.delyTime;
    }

    public int getRetryTimes() {
        return this.retryTimes;
    }

    private String getTag() {
        return TAG + "_" + this.tag;
    }

    public String getPackageName() {
        return this.packageName == null ? "" : this.packageName;
    }

    public boolean isTimeOut() {
        boolean z = (System.currentTimeMillis() - this.startSendTime) + this.delyTime >= ((long) this.timeout);
        if (z) {
            String tag2 = getTag();
            ALog.e(tag2, "delay time:" + this.delyTime + " beforeSendTime:" + (System.currentTimeMillis() - this.startSendTime) + " timeout" + this.timeout, new Object[0]);
        }
        return z;
    }

    public byte[] build(Context context, int i) {
        byte[] bArr;
        try {
            buildData();
        } catch (JSONException e) {
            ALog.e(getTag(), "build1", e, new Object[0]);
        } catch (UnsupportedEncodingException e2) {
            ALog.e(getTag(), "build2", e2, new Object[0]);
        }
        String str = this.data != null ? new String(this.data) : "";
        compressData();
        if (!this.isAck) {
            StringBuilder sb = new StringBuilder();
            sb.append(UtilityImpl.getDeviceId(context));
            sb.append("|");
            sb.append(this.packageName);
            sb.append("|");
            sb.append(this.serviceId == null ? "" : this.serviceId);
            sb.append("|");
            sb.append(this.userinfo == null ? "" : this.userinfo);
            this.source = sb.toString();
        }
        try {
            bArr = (this.dataId + "").getBytes("utf-8");
            this.sourceLength = (byte) this.source.getBytes("utf-8").length;
            this.targetLength = (byte) this.target.getBytes("utf-8").length;
        } catch (Exception e3) {
            e3.printStackTrace();
            ALog.e(getTag(), "build3", e3, new Object[0]);
            bArr = (this.dataId + "").getBytes();
            this.sourceLength = (byte) this.source.getBytes().length;
            this.targetLength = (byte) this.target.getBytes().length;
        }
        short extHeaderLen = getExtHeaderLen(this.extHeader);
        this.dataLength = (short) (this.targetLength + 3 + 1 + this.sourceLength + 1 + bArr.length + (this.data == null ? 0 : this.data.length) + extHeaderLen + 2);
        this.totalLength = (short) (this.dataLength + 2);
        MessageStreamBuilder messageStreamBuilder = new MessageStreamBuilder(this.totalLength + 2 + 4);
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(getTag(), "Build Message", Constants.KEY_DATA_ID, new String(bArr));
        }
        try {
            messageStreamBuilder.writeByte((byte) (this.compress | 32));
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag2 = getTag();
                ALog.d(tag2, "\tversion:2 compress:" + this.compress, new Object[0]);
            }
            if (i == 0) {
                messageStreamBuilder.writeByte(ByteCompanionObject.MIN_VALUE);
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(getTag(), "\tflag: 0x80", new Object[0]);
                }
            } else {
                messageStreamBuilder.writeByte((byte) 64);
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(getTag(), "\tflag: 0x40", new Object[0]);
                }
            }
            messageStreamBuilder.writeShort(this.totalLength);
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag3 = getTag();
                ALog.d(tag3, "\ttotalLength:" + this.totalLength, new Object[0]);
            }
            messageStreamBuilder.writeShort(this.dataLength);
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag4 = getTag();
                ALog.d(tag4, "\tdataLength:" + this.dataLength, new Object[0]);
            }
            messageStreamBuilder.writeShort(this.flags);
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag5 = getTag();
                ALog.d(tag5, "\tflags:" + Integer.toHexString(this.flags), new Object[0]);
            }
            messageStreamBuilder.writeByte(this.targetLength);
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag6 = getTag();
                ALog.d(tag6, "\ttargetLength:" + this.targetLength, new Object[0]);
            }
            messageStreamBuilder.write(this.target.getBytes("utf-8"));
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag7 = getTag();
                ALog.d(tag7, "\ttarget:" + this.target, new Object[0]);
            }
            messageStreamBuilder.writeByte(this.sourceLength);
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag8 = getTag();
                ALog.d(tag8, "\tsourceLength:" + this.sourceLength, new Object[0]);
            }
            messageStreamBuilder.write(this.source.getBytes("utf-8"));
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag9 = getTag();
                ALog.d(tag9, "\tsource:" + this.source, new Object[0]);
            }
            messageStreamBuilder.writeByte((byte) bArr.length);
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag10 = getTag();
                ALog.d(tag10, "\tdataIdLength:" + bArr.length, new Object[0]);
            }
            messageStreamBuilder.write(bArr);
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag11 = getTag();
                ALog.d(tag11, "\tdataId:" + new String(bArr), new Object[0]);
            }
            messageStreamBuilder.writeShort(extHeaderLen);
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag12 = getTag();
                ALog.d(tag12, "\textHeader len:" + extHeaderLen, new Object[0]);
            }
            if (this.extHeader != null) {
                for (Integer intValue : this.extHeader.keySet()) {
                    int intValue2 = intValue.intValue();
                    String str2 = this.extHeader.get(Integer.valueOf(intValue2));
                    if (!TextUtils.isEmpty(str2)) {
                        messageStreamBuilder.writeShort((short) ((((short) intValue2) << 10) | ((short) (str2.getBytes("utf-8").length & EXT_HEADER_VALUE_MAX_LEN))));
                        messageStreamBuilder.write(str2.getBytes("utf-8"));
                        if (ALog.isPrintLog(ALog.Level.D)) {
                            String tag13 = getTag();
                            ALog.d(tag13, "\textHeader key:" + intValue2 + " value:" + str2, new Object[0]);
                        }
                    }
                }
            }
            if (this.data != null) {
                messageStreamBuilder.write(this.data);
            }
            if (ALog.isPrintLog(ALog.Level.D)) {
                String tag14 = getTag();
                ALog.d(tag14, "\toriData:" + str, new Object[0]);
            }
            messageStreamBuilder.flush();
        } catch (IOException e4) {
            ALog.e(getTag(), "build4", e4, new Object[0]);
        }
        byte[] byteArray = messageStreamBuilder.toByteArray();
        try {
            messageStreamBuilder.close();
        } catch (IOException e5) {
            ALog.e(getTag(), "build5", e5, new Object[0]);
        }
        return byteArray;
    }

    /* access modifiers changed from: package-private */
    public short getExtHeaderLen(Map<Integer, String> map) {
        short s = 0;
        if (map != null) {
            try {
                for (Integer intValue : map.keySet()) {
                    String str = map.get(Integer.valueOf(intValue.intValue()));
                    if (!TextUtils.isEmpty(str)) {
                        s = (short) (s + ((short) (str.getBytes("utf-8").length & EXT_HEADER_VALUE_MAX_LEN)) + 2);
                    }
                }
            } catch (Exception e) {
                e.toString();
            }
        }
        return s;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0055 A[SYNTHETIC, Splitter:B:29:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x005f A[SYNTHETIC, Splitter:B:35:0x005f] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0064 A[Catch:{ Exception -> 0x0067 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void compressData() {
        /*
            r6 = this;
            r0 = 0
            byte[] r1 = r6.data     // Catch:{ Throwable -> 0x0041, all -> 0x003c }
            if (r1 != 0) goto L_0x0006
            return
        L_0x0006:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0041, all -> 0x003c }
            r1.<init>()     // Catch:{ Throwable -> 0x0041, all -> 0x003c }
            java.util.zip.GZIPOutputStream r2 = new java.util.zip.GZIPOutputStream     // Catch:{ Throwable -> 0x0037, all -> 0x0032 }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x0037, all -> 0x0032 }
            byte[] r0 = r6.data     // Catch:{ Throwable -> 0x0030 }
            r2.write(r0)     // Catch:{ Throwable -> 0x0030 }
            r2.finish()     // Catch:{ Throwable -> 0x0030 }
            byte[] r0 = r1.toByteArray()     // Catch:{ Throwable -> 0x0030 }
            if (r0 == 0) goto L_0x0029
            int r3 = r0.length     // Catch:{ Throwable -> 0x0030 }
            byte[] r4 = r6.data     // Catch:{ Throwable -> 0x0030 }
            int r4 = r4.length     // Catch:{ Throwable -> 0x0030 }
            if (r3 >= r4) goto L_0x0029
            r6.data = r0     // Catch:{ Throwable -> 0x0030 }
            r0 = 1
            r6.compress = r0     // Catch:{ Throwable -> 0x0030 }
        L_0x0029:
            r2.close()     // Catch:{ Exception -> 0x005b }
        L_0x002c:
            r1.close()     // Catch:{ Exception -> 0x005b }
            goto L_0x005b
        L_0x0030:
            r0 = move-exception
            goto L_0x0045
        L_0x0032:
            r2 = move-exception
            r5 = r2
            r2 = r0
            r0 = r5
            goto L_0x005d
        L_0x0037:
            r2 = move-exception
            r5 = r2
            r2 = r0
            r0 = r5
            goto L_0x0045
        L_0x003c:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L_0x005d
        L_0x0041:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L_0x0045:
            java.lang.String r3 = r6.getTag()     // Catch:{ all -> 0x005c }
            java.lang.String r4 = r0.toString()     // Catch:{ all -> 0x005c }
            android.util.Log.e(r3, r4)     // Catch:{ all -> 0x005c }
            r0.printStackTrace()     // Catch:{ all -> 0x005c }
            if (r2 == 0) goto L_0x0058
            r2.close()     // Catch:{ Exception -> 0x005b }
        L_0x0058:
            if (r1 == 0) goto L_0x005b
            goto L_0x002c
        L_0x005b:
            return
        L_0x005c:
            r0 = move-exception
        L_0x005d:
            if (r2 == 0) goto L_0x0062
            r2.close()     // Catch:{ Exception -> 0x0067 }
        L_0x0062:
            if (r1 == 0) goto L_0x0067
            r1.close()     // Catch:{ Exception -> 0x0067 }
        L_0x0067:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.data.Message.compressData():void");
    }

    /* access modifiers changed from: package-private */
    public void buildData() throws JSONException, UnsupportedEncodingException {
        if (this.command != null && this.command.intValue() != 100 && this.command.intValue() != 102) {
            this.data = new JsonUtility.JsonObjectBuilder().put("command", this.command.intValue() == 100 ? null : this.command).put("appKey", this.appKey).put(Constants.KEY_OS_TYPE, this.osType).put("sign", this.appSign).put("sdkVersion", this.sdkVersion).put("appVersion", this.appVersion).put("ttid", this.ttid).put(Constants.KEY_MODEL, this.model).put("brand", this.brand).put("imei", this.imei).put("imsi", this.imsi).put("os", this.osVersion).put(Constants.KEY_EXTS, this.exts).build().toString().getBytes("utf-8");
        }
    }

    /* access modifiers changed from: package-private */
    public void printByte(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(getTag(), "len:" + bArr.length, new Object[0]);
            if (bArr.length < 512) {
                for (byte b : bArr) {
                    sb.append(Integer.toHexString(b & UByte.MAX_VALUE));
                    sb.append(Operators.SPACE_STR);
                }
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(getTag(), sb.toString(), new Object[0]);
                }
            }
        }
    }

    public static Message BuildPing(boolean z, int i) {
        Message message = new Message();
        message.type = 2;
        message.command = 201;
        message.force = z;
        message.delyTime = (long) i;
        return message;
    }

    public static Message buildForeground(String str) {
        Message message = new Message();
        message.type(1, ReqType.DATA, 0);
        message.command = 100;
        message.target = Constants.TARGET_FORE;
        setControlHost(str, message);
        return message;
    }

    public static Message buildBackground(String str) {
        Message message = new Message();
        message.type(1, ReqType.DATA, 0);
        message.command = 100;
        message.target = Constants.TARGET_BACK;
        setControlHost(str, message);
        return message;
    }

    public static Message buildHandshake(String str) {
        Message message = new Message();
        message.type(3, ReqType.DATA, 1);
        message.packageName = str;
        message.target = Constants.TARGET_CONTROL;
        message.command = 200;
        return message;
    }

    @Deprecated
    public static Message buildBindApp(BaseConnection baseConnection, Context context, Intent intent) {
        return buildBindApp(baseConnection.getHost((String) null), baseConnection.mConfigTag, context, intent);
    }

    public static Message buildBindApp(String str, String str2, Context context, Intent intent) {
        Message message;
        try {
            Context context2 = context;
            String str3 = str2;
            message = buildBindApp(context2, str3, intent.getStringExtra("appKey"), intent.getStringExtra("app_sercet"), intent.getStringExtra("packageName"), intent.getStringExtra("ttid"), intent.getStringExtra("appVersion"));
            try {
                setControlHost(str, message);
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            message = null;
            ALog.e(TAG, "buildBindApp", e.getMessage());
            return message;
        }
        return message;
    }

    @Deprecated
    public static Message buildBindApp(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        return buildBindApp(context, str, str2, str3, str4, str5, str6);
    }

    public static Message buildBindApp(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
        String str7 = null;
        if (TextUtils.isEmpty(str4)) {
            return null;
        }
        Message message = new Message();
        message.node = 1;
        message.type(1, ReqType.DATA, 1);
        message.osType = 1;
        message.osVersion = Build.VERSION.SDK_INT + "";
        message.packageName = str4;
        message.target = Constants.TARGET_CONTROL;
        message.command = 1;
        message.appKey = str2;
        message.appSign = UtilityImpl.getAppsign(context, str2, str3, UtilityImpl.getDeviceId(context), str);
        message.sdkVersion = Integer.valueOf(Constants.SDK_VERSION_CODE);
        message.appVersion = str6;
        message.packageName = str4;
        message.ttid = str5;
        message.model = Build.MODEL;
        message.brand = Build.BRAND;
        message.cunstomDataId = KEY_BINDAPP;
        message.tag = str;
        message.exts = new JsonUtility.JsonObjectBuilder().put("notifyEnable", UtilityImpl.isNotificationEnabled(context)).put("romInfo", RomInfoCollecter.getCollecter().collect()).build().toString();
        UtilityImpl.saveNotificationState(context, Constants.SP_FILE_NAME, UtilityImpl.isNotificationEnabled(context));
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            message.imei = telephonyManager != null ? telephonyManager.getDeviceId() : null;
            if (telephonyManager != null) {
                str7 = telephonyManager.getSubscriberId();
            }
            message.imsi = str7;
        } catch (Throwable th) {
            ALog.w(TAG, "buildBindApp imei", th.getMessage());
        }
        return message;
    }

    @Deprecated
    public static Message buildUnbindApp(BaseConnection baseConnection, Context context, Intent intent) {
        return buildUnbindApp(baseConnection.getHost((String) null), intent);
    }

    public static Message buildUnbindApp(String str, Intent intent) {
        Message message;
        ALog.e(TAG, "buildUnbindApp1" + UtilityImpl.getStackMsg(new Exception()), new Object[0]);
        try {
            message = buildUnbindApp(str, intent.getStringExtra("packageName"));
            try {
                setControlHost(str, message);
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            message = null;
            ALog.e(TAG, "buildUnbindApp1", e.getMessage());
            return message;
        }
        return message;
    }

    @Deprecated
    public static Message buildUnbindApp(BaseConnection baseConnection, Context context, String str, String str2, String str3, String str4) {
        return buildUnbindApp(baseConnection.getHost((String) null), str);
    }

    public static Message buildUnbindApp(String str, String str2) {
        Message message;
        try {
            ALog.d(TAG, "buildUnbindApp", new Object[0]);
            if (TextUtils.isEmpty(str2)) {
                return null;
            }
            message = new Message();
            try {
                message.node = 1;
                message.type(1, ReqType.DATA, 1);
                message.packageName = str2;
                message.target = Constants.TARGET_CONTROL;
                message.command = 2;
                message.packageName = str2;
                message.sdkVersion = Integer.valueOf(Constants.SDK_VERSION_CODE);
                message.cunstomDataId = KEY_UNBINDAPP;
                setControlHost(str, message);
            } catch (Exception e) {
                e = e;
            }
            return message;
        } catch (Exception e2) {
            e = e2;
            message = null;
            ALog.e(TAG, "buildUnbindApp", e.getMessage());
            return message;
        }
    }

    @Deprecated
    public static Message buildBindService(BaseConnection baseConnection, Context context, Intent intent) {
        return buildBindService(baseConnection.getHost((String) null), baseConnection.mConfigTag, intent);
    }

    public static Message buildBindService(String str, String str2, Intent intent) {
        Message message;
        try {
            message = buildBindService(intent.getStringExtra("packageName"), intent.getStringExtra("serviceId"));
            try {
                message.tag = str2;
                setControlHost(str, message);
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            message = null;
            ALog.e(TAG, "buildBindService", e, new Object[0]);
            e.printStackTrace();
            return message;
        }
        return message;
    }

    @Deprecated
    public static Message buildBindService(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
        return buildBindService(str, str3);
    }

    public static Message buildBindService(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        Message message = new Message();
        message.node = 1;
        message.type(1, ReqType.DATA, 1);
        message.packageName = str;
        message.serviceId = str2;
        message.target = Constants.TARGET_CONTROL;
        message.command = 5;
        message.packageName = str;
        message.serviceId = str2;
        message.sdkVersion = Integer.valueOf(Constants.SDK_VERSION_CODE);
        message.cunstomDataId = KEY_BINDSERVICE;
        return message;
    }

    @Deprecated
    public static Message buildUnbindService(BaseConnection baseConnection, Context context, Intent intent) {
        return buildUnbindService(baseConnection.getHost((String) null), baseConnection.mConfigTag, intent);
    }

    public static Message buildUnbindService(String str, String str2, Intent intent) {
        Message message;
        try {
            message = buildUnbindService(intent.getStringExtra("packageName"), intent.getStringExtra("serviceId"));
            try {
                message.tag = str2;
                setControlHost(str, message);
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            message = null;
            ALog.e(TAG, "buildUnbindService", e, new Object[0]);
            e.printStackTrace();
            return message;
        }
        return message;
    }

    @Deprecated
    public static Message buildUnbindService(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
        return buildUnbindService(str, str3);
    }

    public static Message buildUnbindService(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        Message message = new Message();
        message.node = 1;
        message.type(1, ReqType.DATA, 1);
        message.packageName = str;
        message.serviceId = str2;
        message.target = Constants.TARGET_CONTROL;
        message.command = 6;
        message.packageName = str;
        message.serviceId = str2;
        message.sdkVersion = Integer.valueOf(Constants.SDK_VERSION_CODE);
        message.cunstomDataId = KEY_UNBINDSERVICE;
        return message;
    }

    @Deprecated
    public static Message buildBindUser(BaseConnection baseConnection, Context context, Intent intent) {
        return buildBindUser(baseConnection.getHost((String) null), baseConnection.mConfigTag, intent);
    }

    public static Message buildBindUser(String str, String str2, Intent intent) {
        Message message;
        try {
            message = buildBindUser(intent.getStringExtra("packageName"), intent.getStringExtra("userInfo"));
            if (message != null) {
                try {
                    message.tag = str2;
                    setControlHost(str, message);
                } catch (Exception e) {
                    e = e;
                }
            }
        } catch (Exception e2) {
            e = e2;
            message = null;
            ALog.e(TAG, "buildBindUser", e, new Object[0]);
            e.printStackTrace();
            return message;
        }
        return message;
    }

    @Deprecated
    public static Message buildBindUser(Context context, String str, String str2, String str3, String str4, String str5) {
        return buildBindUser(str, str4);
    }

    public static Message buildBindUser(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        Message message = new Message();
        message.node = 1;
        message.type(1, ReqType.DATA, 1);
        message.packageName = str;
        message.userinfo = str2;
        message.target = Constants.TARGET_CONTROL;
        message.command = 3;
        message.packageName = str;
        message.userinfo = str2;
        message.sdkVersion = Integer.valueOf(Constants.SDK_VERSION_CODE);
        message.cunstomDataId = KEY_BINDUSER;
        return message;
    }

    public static Message buildErrorReportMessage(String str, String str2, String str3, int i) {
        Message message = new Message();
        try {
            message.host = new URL(str3);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        message.target = Constants.TARGET_SERVICE_ST;
        message.type(1, ReqType.DATA, 0);
        message.command = 100;
        message.data = (0 + "|" + i + "|" + str + "|" + AdapterUtilityImpl.getDeviceId(GlobalClientInfo.getContext()) + "|" + str2).getBytes();
        return message;
    }

    @Deprecated
    public static Message buildUnbindUser(BaseConnection baseConnection, Context context, Intent intent) {
        return buildUnbindUser(baseConnection.getHost((String) null), baseConnection.mConfigTag, intent);
    }

    public static Message buildUnbindUser(String str, String str2, Intent intent) {
        Message message;
        try {
            message = buildUnbindUser(intent.getStringExtra("packageName"));
            try {
                message.tag = str2;
                setControlHost(str, message);
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            message = null;
            ALog.e(TAG, "buildUnbindUser", e, new Object[0]);
            e.printStackTrace();
            return message;
        }
        return message;
    }

    @Deprecated
    public static Message buildUnbindUser(Context context, String str, String str2, String str3, String str4, String str5) {
        return buildUnbindUser(str);
    }

    public static Message buildUnbindUser(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Message message = new Message();
        message.node = 1;
        message.type(1, ReqType.DATA, 1);
        message.packageName = str;
        message.target = Constants.TARGET_CONTROL;
        message.command = 4;
        message.sdkVersion = Integer.valueOf(Constants.SDK_VERSION_CODE);
        message.cunstomDataId = KEY_UNBINDUSER;
        return message;
    }

    @Deprecated
    public static Message buildSendData(BaseConnection baseConnection, Context context, String str, String str2, ACCSManager.AccsRequest accsRequest) {
        return buildSendData(baseConnection.getHost((String) null), baseConnection.mConfigTag, baseConnection.mConfig.getStoreId(), context, str, accsRequest, true);
    }

    @Deprecated
    public static Message buildSendData(BaseConnection baseConnection, Context context, String str, String str2, ACCSManager.AccsRequest accsRequest, boolean z) {
        return buildSendData(baseConnection.getHost((String) null), baseConnection.mConfigTag, baseConnection.mConfig.getStoreId(), context, str, accsRequest, z);
    }

    public static Message buildSendData(String str, String str2, String str3, Context context, String str4, ACCSManager.AccsRequest accsRequest) {
        return buildSendData(str, str2, str3, context, str4, accsRequest, true);
    }

    public static Message buildSendData(String str, String str2, String str3, Context context, String str4, ACCSManager.AccsRequest accsRequest, boolean z) {
        if (TextUtils.isEmpty(str4)) {
            return null;
        }
        Message message = new Message();
        message.node = 1;
        message.type(1, ReqType.DATA, 1);
        message.command = 100;
        message.packageName = str4;
        message.serviceId = accsRequest.serviceId;
        message.userinfo = accsRequest.userId;
        message.data = accsRequest.data;
        String str5 = TextUtils.isEmpty(accsRequest.targetServiceName) ? accsRequest.serviceId : accsRequest.targetServiceName;
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.TARGET_SERVICE_PRE);
        sb.append(str5);
        sb.append("|");
        sb.append(accsRequest.target == null ? "" : accsRequest.target);
        message.target = sb.toString();
        message.cunstomDataId = accsRequest.dataId;
        message.bizId = accsRequest.businessId;
        if (accsRequest.timeout > 0) {
            message.timeout = accsRequest.timeout;
        }
        if (z) {
            setUnit(str, message, accsRequest);
        } else {
            message.host = accsRequest.host;
        }
        fillExtHeader(context, message, GlobalClientInfo.getInstance(context).getSid(str2), GlobalClientInfo.getInstance(context).getUserId(str2), str3, GlobalClientInfo.mCookieSec, accsRequest.businessId, accsRequest.tag);
        message.netPerformanceMonitor = new NetPerformanceMonitor();
        message.netPerformanceMonitor.setMsgType(0);
        message.netPerformanceMonitor.setDataId(accsRequest.dataId);
        message.netPerformanceMonitor.setServiceId(accsRequest.serviceId);
        message.netPerformanceMonitor.setHost(message.host.toString());
        message.tag = str2;
        return message;
    }

    @Deprecated
    public static Message buildRequest(BaseConnection baseConnection, Context context, String str, String str2, String str3, ACCSManager.AccsRequest accsRequest, boolean z) {
        return buildRequest(context, baseConnection.getHost((String) null), baseConnection.mConfigTag, baseConnection.mConfig.getStoreId(), str, str2, accsRequest, z);
    }

    public static Message buildRequest(Context context, String str, String str2, String str3, String str4, String str5, ACCSManager.AccsRequest accsRequest, boolean z) {
        String str6 = str2;
        ACCSManager.AccsRequest accsRequest2 = accsRequest;
        if (TextUtils.isEmpty(str4)) {
            return null;
        }
        Message message = new Message();
        message.node = 1;
        message.type(1, ReqType.REQ, 1);
        message.command = 100;
        message.packageName = str4;
        message.serviceId = accsRequest2.serviceId;
        message.userinfo = accsRequest2.userId;
        message.data = accsRequest2.data;
        String str7 = TextUtils.isEmpty(accsRequest2.targetServiceName) ? accsRequest2.serviceId : accsRequest2.targetServiceName;
        StringBuilder sb = new StringBuilder();
        sb.append(str5);
        sb.append(str7);
        sb.append("|");
        sb.append(accsRequest2.target == null ? "" : accsRequest2.target);
        message.target = sb.toString();
        message.cunstomDataId = accsRequest2.dataId;
        message.bizId = accsRequest2.businessId;
        message.tag = str6;
        if (accsRequest2.timeout > 0) {
            message.timeout = accsRequest2.timeout;
        }
        if (z) {
            String str8 = str;
            setUnit(str, message, accsRequest2);
        } else {
            message.host = accsRequest2.host;
        }
        fillExtHeader(context, message, GlobalClientInfo.getInstance(context).getSid(str2), GlobalClientInfo.getInstance(context).getUserId(str2), str3, GlobalClientInfo.mCookieSec, accsRequest2.businessId, accsRequest2.tag);
        message.netPerformanceMonitor = new NetPerformanceMonitor();
        message.netPerformanceMonitor.setDataId(accsRequest2.dataId);
        message.netPerformanceMonitor.setServiceId(accsRequest2.serviceId);
        message.netPerformanceMonitor.setHost(message.host.toString());
        message.tag = str6;
        return message;
    }

    private static void setUnit(String str, Message message, ACCSManager.AccsRequest accsRequest) {
        if (accsRequest.host == null) {
            try {
                message.host = new URL(str);
            } catch (MalformedURLException e) {
                ALog.e(TAG, "setUnit", e, new Object[0]);
                e.printStackTrace();
            }
        } else {
            message.host = accsRequest.host;
        }
    }

    private static void setControlHost(String str, Message message) {
        try {
            message.host = new URL(str);
        } catch (Exception e) {
            ALog.e(TAG, "setControlHost", e, new Object[0]);
        }
    }

    @Deprecated
    public static Message buildPushAck(BaseConnection baseConnection, String str, String str2, String str3, boolean z, short s, String str4, Map<Integer, String> map) {
        return buildPushAck(baseConnection.getHost((String) null), baseConnection.mConfigTag, str, str2, str3, z, s, str4, map);
    }

    public static Message buildPushAck(String str, String str2, String str3, String str4, String str5, boolean z, short s, String str6, Map<Integer, String> map) {
        Message message = new Message();
        message.node = 1;
        message.setPushAckFlag(s, z);
        message.source = str3;
        message.target = str4;
        message.dataId = str5;
        message.isAck = true;
        message.extHeader = map;
        try {
            if (TextUtils.isEmpty(str6)) {
                message.host = new URL(str);
            } else {
                message.host = new URL(str6);
            }
            message.tag = str2;
            if (message.host == null) {
                try {
                    message.host = new URL(str);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (message.host == null) {
                try {
                    message.host = new URL(str);
                } catch (MalformedURLException e2) {
                    e2.printStackTrace();
                }
            }
            throw th;
        }
        return message;
    }

    public static Message buildParameterError(String str, int i) {
        Message message = new Message();
        message.type(1, ReqType.ACK, 0);
        message.command = Integer.valueOf(i);
        message.packageName = str;
        return message;
    }

    private static void fillExtHeader(Context context, Message message, String str, String str2, String str3, String str4, String str5, String str6) {
        if (!TextUtils.isEmpty(str5) || !TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str6) || str4 != null) {
            message.extHeader = new HashMap();
            if (str5 != null && UtilityImpl.getByteLen(str5) <= 1023) {
                message.extHeader.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_BUSINESS.ordinal()), str5);
            }
            if (str != null && UtilityImpl.getByteLen(str) <= 1023) {
                message.extHeader.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_SID.ordinal()), str);
            }
            if (str2 != null && UtilityImpl.getByteLen(str2) <= 1023) {
                message.extHeader.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_USERID.ordinal()), str2);
            }
            if (str6 != null && UtilityImpl.getByteLen(str6) <= 1023) {
                message.extHeader.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_TAG.ordinal()), str6);
            }
            if (str4 != null && UtilityImpl.getByteLen(str4) <= 1023) {
                message.extHeader.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_COOKIE.ordinal()), str4);
            }
            if (str3 != null && UtilityImpl.getByteLen(str3) <= 1023) {
                message.extHeader.put(19, str3);
            }
        }
    }

    private void type(int i, ReqType reqType, int i2) {
        this.type = i;
        if (i != 2) {
            this.flags = (short) (((((i & 1) << 4) | (reqType.ordinal() << 2)) | i2) << 11);
        }
    }

    private void setPushAckFlag(short s, boolean z) {
        this.type = 1;
        this.flags = s;
        this.flags = (short) (this.flags & -16385);
        this.flags = (short) (this.flags | 8192);
        this.flags = (short) (this.flags & -2049);
        this.flags = (short) (this.flags & -65);
        if (z) {
            this.flags = (short) (this.flags | 32);
        }
    }
}
