package com.taobao.securityjni.bcast;

import android.content.Intent;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Array;
import java.util.ArrayList;
import kotlin.UByte;

@Deprecated
public class AppStateManager {
    public static final String DNS_ACTION = "setaobao.bbox.DNS";
    public static final String EXTRA_DNS_IP = "IPAddress";
    public static final String EXTRA_DNS_LOCAL = "DNSinfolocal";
    public static final String EXTRA_DNS_NET = "DNSinfonet";
    public static final String EXTRA_RT = "RTinfo";
    public static final String EXTRA_SPITEP = "SPITEPinfo";
    private static final int IPV4_SIZE = 4;
    public static int Init = 0;
    public static final String RT_ACTION = "setaobao.bbox.RT";
    public static final int RT_VALUE_100_PERMISSION = 10;
    public static final int RT_VALUE_INVALID = -1;
    public static final int RT_VALUE_LIKELY_1 = 1;
    public static final int RT_VALUE_LIKELY_2 = 2;
    public static final int RT_VALUE_LIKELY_3 = 3;
    public static final int RT_VALUE_LIKELY_4 = 4;
    public static final int RT_VALUE_LIKELY_5 = 5;
    public static final int RT_VALUE_LIKELY_6 = 6;
    public static final int RT_VALUE_LIKELY_7 = 7;
    public static final int RT_VALUE_LIKELY_8 = 8;
    public static final int RT_VALUE_LIKELY_9 = 9;
    public static final int RT_VALUE_UNDETECTABLE = 0;
    public static final String SPITEP_ACTION = "setaobao.bbox.SPITEP";
    public static final int SPITEP_VALUE_NS_0 = 0;
    public static final int SPITEP_VALUE_NS_1 = 1;
    public static final int SPITEP_VALUE_NS_2 = 2;
    public static final int SPITEP_VALUE_NS_3 = 3;

    public static class DoaminIP {
        public byte[][] ip;
        public String name;

        private String IpToString() {
            StringBuilder sb = new StringBuilder();
            if (this.ip == null) {
                return BuildConfig.buildJavascriptFrameworkVersion;
            }
            for (int i = 0; i < this.ip.length; i++) {
                byte[] bArr = this.ip[i];
                sb.append("ip[");
                sb.append(i);
                sb.append("]=");
                if (bArr != null) {
                    for (int i2 = 0; i2 < bArr.length; i2++) {
                        sb.append(bArr[i2] & UByte.MAX_VALUE);
                        if (i2 != bArr.length - 1) {
                            sb.append(Operators.CONDITION_IF_MIDDLE);
                        }
                    }
                } else {
                    sb.append(BuildConfig.buildJavascriptFrameworkVersion);
                }
                sb.append("  ");
            }
            return sb.toString();
        }

        public String toString() {
            return "DoaminIP [name=" + this.name + ", ip=" + IpToString() + Operators.ARRAY_END_STR;
        }
    }

    public static final ArrayList<DoaminIP> parserDomainIP(Intent intent) {
        byte[] byteArrayExtra = intent.getByteArrayExtra(EXTRA_DNS_IP);
        if (byteArrayExtra == null) {
            return null;
        }
        ArrayList<DoaminIP> arrayList = new ArrayList<>();
        byte[][] bArr = null;
        int i = 0;
        while (i < byteArrayExtra.length) {
            byte b = byteArrayExtra[i] & UByte.MAX_VALUE;
            String str = new String(byteArrayExtra, i + 1, b);
            int i2 = i + b + 1;
            byte b2 = byteArrayExtra[i2] & UByte.MAX_VALUE;
            int i3 = b2 + i2;
            if (i3 > byteArrayExtra.length - 1) {
                break;
            }
            int i4 = b2 / 4;
            if (i4 > 0) {
                byte[][] bArr2 = (byte[][]) Array.newInstance(byte.class, new int[]{i4, 4});
                for (int i5 = 0; i5 < i4; i5++) {
                    System.arraycopy(byteArrayExtra, i2 + 1 + (i5 * 4), bArr2[i5], 0, 4);
                }
                bArr = bArr2;
            }
            i = i3 + 1;
            DoaminIP doaminIP = new DoaminIP();
            doaminIP.name = str;
            doaminIP.ip = bArr;
            arrayList.add(doaminIP);
        }
        return arrayList;
    }
}
