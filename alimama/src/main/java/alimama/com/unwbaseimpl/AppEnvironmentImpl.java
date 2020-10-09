package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import anetwork.channel.util.RequestConstant;
import com.taobao.android.dinamic.DinamicConstant;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.json.JSONObject;

public class AppEnvironmentImpl implements IAppEnvironment {
    private static final String CONF_TTID = "ttid";
    private static final int ENV_DAILY = 3;
    private static final int ENV_PRE = 2;
    private static final int ENV_PROD = 1;
    private static final int ENV_UNKNOWN = 0;
    private static final String KEY_TTID = "ttid_value";
    private final int ENDHDR = 22;
    private final String KEY = "506d2c604963d3a9b2794ba64801024e";
    private String mChannelId;
    private String mTTid;
    private int sEnv = 0;

    public void init() {
        initAllId(UNWManager.getInstance().application);
    }

    public String getTTid() {
        if (TextUtils.isEmpty(this.mTTid)) {
            initAllId(UNWManager.getInstance().application);
        }
        return this.mTTid;
    }

    public String getChannelId() {
        if (TextUtils.isEmpty(this.mChannelId)) {
            initAllId(UNWManager.getInstance().application);
        }
        return this.mChannelId;
    }

    private void initAllId(Application application) {
        this.mChannelId = getTtidFromApk(application);
        if (!TextUtils.isEmpty(this.mChannelId)) {
            this.mTTid = this.mChannelId + DinamicConstant.DINAMIC_PREFIX_AT + "etao_android_" + UNWManager.getInstance().mVersionName;
        }
    }

    private String getTtidFromApk(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ttid", 0);
        String string = sharedPreferences.getString(KEY_TTID, "");
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        String channelIdFromApk = getChannelIdFromApk(context);
        if (TextUtils.isEmpty(channelIdFromApk)) {
            channelIdFromApk = "701234";
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(KEY_TTID, channelIdFromApk);
        edit.apply();
        return channelIdFromApk;
    }

    private String getChannelIdFromApk(Context context) {
        if (!TextUtils.isEmpty(this.mChannelId)) {
            return this.mChannelId;
        }
        String str = "";
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("META-INF/channel");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = resourceAsStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            str = new String(byteArrayOutputStream.toByteArray());
        } catch (Exception unused) {
        }
        return TextUtils.isEmpty(str) ? "701234" : str;
    }

    private String getChannelIDFromComment(Context context) {
        try {
            return decode(new JSONObject(getComment(context.getPackageManager().getPackageInfo(context.getPackageName(), 128).applicationInfo.sourceDir)).optString("t"), "506d2c604963d3a9b2794ba64801024e");
        } catch (Exception unused) {
            return "";
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0094, code lost:
        if (r1 != null) goto L_0x0058;
     */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x008f A[SYNTHETIC, Splitter:B:33:0x008f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getComment(java.lang.String r9) {
        /*
            r8 = this;
            r0 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0093, all -> 0x008b }
            java.lang.String r2 = "r"
            r1.<init>(r9, r2)     // Catch:{ Exception -> 0x0093, all -> 0x008b }
            long r2 = r1.length()     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r4 = 22
            long r2 = r2 - r4
            r4 = 0
            int r9 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r9 < 0) goto L_0x006c
            r6 = 65536(0x10000, double:3.2379E-319)
            long r6 = r2 - r6
            int r9 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r9 >= 0) goto L_0x001f
            goto L_0x0020
        L_0x001f:
            r4 = r6
        L_0x0020:
            r1.seek(r2)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            int r9 = r1.readInt()     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            int r9 = java.lang.Integer.reverseBytes(r9)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r6 = 101010256(0x6054b50, float:2.506985E-35)
            if (r9 != r6) goto L_0x005c
            r9 = 18
            byte[] r9 = new byte[r9]     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r1.readFully(r9)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r2 = 16
            byte r2 = r9[r2]     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r2 = r2 & 255(0xff, float:3.57E-43)
            r3 = 17
            byte r9 = r9[r3]     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r9 = r9 & 255(0xff, float:3.57E-43)
            int r9 = r9 << 8
            r9 = r9 | r2
            if (r9 <= 0) goto L_0x0058
            byte[] r9 = new byte[r9]     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r1.readFully(r9)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            java.lang.String r2 = new java.lang.String     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            java.lang.String r3 = "UTF-8"
            r2.<init>(r9, r3)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r1.close()     // Catch:{ IOException -> 0x0057 }
        L_0x0057:
            return r2
        L_0x0058:
            r1.close()     // Catch:{ IOException -> 0x0097 }
            goto L_0x0097
        L_0x005c:
            r6 = 1
            long r2 = r2 - r6
            int r9 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r9 < 0) goto L_0x0064
            goto L_0x0020
        L_0x0064:
            java.util.zip.ZipException r9 = new java.util.zip.ZipException     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            java.lang.String r2 = "EOCD not found; not a zip file?"
            r9.<init>(r2)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            throw r9     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
        L_0x006c:
            java.util.zip.ZipException r9 = new java.util.zip.ZipException     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r2.<init>()     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            java.lang.String r3 = "File too short to be a zip file: "
            r2.append(r3)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            long r3 = r1.length()     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r2.append(r3)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            r9.<init>(r2)     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
            throw r9     // Catch:{ Exception -> 0x0089, all -> 0x0087 }
        L_0x0087:
            r9 = move-exception
            goto L_0x008d
        L_0x0089:
            goto L_0x0094
        L_0x008b:
            r9 = move-exception
            r1 = r0
        L_0x008d:
            if (r1 == 0) goto L_0x0092
            r1.close()     // Catch:{ IOException -> 0x0092 }
        L_0x0092:
            throw r9
        L_0x0093:
            r1 = r0
        L_0x0094:
            if (r1 == 0) goto L_0x0097
            goto L_0x0058
        L_0x0097:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwbaseimpl.AppEnvironmentImpl.getComment(java.lang.String):java.lang.String");
    }

    private static int getStringOnNum(String str, String str2, int i) {
        int[] iArr = new int[i];
        String str3 = str;
        for (int i2 = 0; i2 < i; i2++) {
            int indexOf = str3.indexOf(str2);
            if (indexOf <= 0) {
                return -1;
            }
            iArr[i2] = indexOf;
            str3 = str3.substring(indexOf + 1);
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            i3 += iArr[i4];
        }
        return i3 + (str2.length() * (i - 1));
    }

    private String decode(String str, String str2) {
        return new String(xorWithKey(Base64.decode(str, 0), str2.getBytes()));
    }

    private byte[] xorWithKey(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr3[i] = (byte) (bArr[i] ^ bArr2[i % bArr2.length]);
        }
        return bArr3;
    }

    public void setEnv(String str) {
        if (UNWManager.getInstance().getDebuggable()) {
            String deserializeEnv = deserializeEnv();
            if (!TextUtils.isEmpty(deserializeEnv)) {
                str = deserializeEnv;
            }
        }
        if (!TextUtils.isEmpty(str)) {
            if (TextUtils.equals("prod", str)) {
                this.sEnv = 1;
            } else if (TextUtils.equals(RequestConstant.ENV_PRE, str)) {
                this.sEnv = 2;
            } else if (TextUtils.equals("daily", str)) {
                this.sEnv = 3;
            }
        }
        if (this.sEnv == 0) {
            throw new IllegalArgumentException("env should be [prod / pre / daily / monkey], please check the value in is_config.xml");
        }
    }

    public void serializeEnv(String str) {
        UNWManager.getInstance().getSharedPreference().putString("env_for_test_purpose", "env_for_test", str).apply();
    }

    public String deserializeEnv() {
        return UNWManager.getInstance().getSharedPreference().getString("env_for_test_purpose", "env_for_test", "");
    }

    public int getEnv() {
        return this.sEnv;
    }

    public boolean isProd() {
        return this.sEnv == 1;
    }

    public boolean isPre() {
        return this.sEnv == 2;
    }

    public boolean isDaily() {
        return this.sEnv == 3;
    }
}
