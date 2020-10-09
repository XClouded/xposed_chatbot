package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.securityjni.tools.DataContext;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.SecurityGuardParamContext;
import com.taobao.wireless.security.sdk.indiekit.IIndieKitComponent;
import com.taobao.wireless.security.sdk.securesignature.ISecureSignatureComponent;
import com.taobao.wireless.security.sdk.securesignature.SecureSignatureDefine;
import java.util.HashMap;
import java.util.TreeMap;

@Deprecated
public class SecretUtil {
    public static final String M_API = "API";
    public static final String M_DATA = "DATA";
    public static final String M_DEV = "DEV";
    public static final String M_ECODE = "ECODE";
    public static final String M_IMEI = "IMEI";
    public static final String M_IMSI = "IMSI";
    public static final String M_SSO = "SSO";
    public static final String M_TIME = "TIME";
    public static final String M_V = "V";
    private IIndieKitComponent indieKitProxy;
    private SecurityGuardManager manager;
    private ISecureSignatureComponent signProxy;

    public int validateFileSignature(String str, String str2, String str3) {
        return -1;
    }

    public SecretUtil(ContextWrapper contextWrapper) {
        this.manager = SecurityGuardManager.getInstance(contextWrapper);
        if (this.manager != null) {
            this.signProxy = this.manager.getSecureSignatureComp();
            this.indieKitProxy = this.manager.getIndieKitComp();
        }
    }

    public String signRequest(SecurityGuardParamContext securityGuardParamContext) {
        if (this.signProxy == null) {
            return null;
        }
        return this.signProxy.signRequest(securityGuardParamContext);
    }

    public String getSign(String str, String str2, String str3, String str4, String str5, String str6) {
        return getSign(str, str2, str3, str4, str5, (String) null, str6);
    }

    public String getSign(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        if (str == null || str2 == null || str3 == null || str4 == null || str7 == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("API", str);
        hashMap.put("V", str2);
        hashMap.put("IMEI", str3);
        hashMap.put("IMSI", str4);
        if (str5 != null) {
            hashMap.put("DATA", str5);
        }
        if (str6 != null) {
            hashMap.put("ECODE", str6);
        }
        hashMap.put("TIME", str7);
        return getSign(hashMap, new DataContext(0, (byte[]) null));
    }

    public String getSign(HashMap<String, String> hashMap, DataContext dataContext) {
        return getMtopSign(hashMap, dataContext);
    }

    public String getMtopSign(HashMap<String, String> hashMap, DataContext dataContext) {
        if (this.signProxy == null || hashMap == null || dataContext == null) {
            return null;
        }
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 3;
        if (dataContext.extData == null) {
            dataContext.index = dataContext.index < 0 ? 0 : dataContext.index;
            String appKeyByIndex = this.manager.getStaticDataStoreComp().getAppKeyByIndex(dataContext.index);
            if (appKeyByIndex == null || "".equals(appKeyByIndex)) {
                return null;
            }
            securityGuardParamContext.appKey = appKeyByIndex;
        } else if (dataContext.extData.length == 0) {
            return null;
        } else {
            securityGuardParamContext.appKey = new String(dataContext.extData);
        }
        return this.signProxy.signRequest(securityGuardParamContext);
    }

    public String getLoginTopToken(String str, String str2) {
        return getLoginTopToken(str, str2, new DataContext(0, (byte[]) null));
    }

    public String getLoginTopToken(String str, String str2, DataContext dataContext) {
        if (this.indieKitProxy == null || str == null || str2 == null || dataContext == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("username", str);
        hashMap.put("timestamp", str2);
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.paramMap = hashMap;
        int i = 0;
        securityGuardParamContext.requestType = 0;
        if (dataContext.extData == null) {
            if (dataContext.index >= 0) {
                i = dataContext.index;
            }
            dataContext.index = i;
            String appKeyByIndex = this.manager.getStaticDataStoreComp().getAppKeyByIndex(dataContext.index);
            if (appKeyByIndex == null || "".equals(appKeyByIndex)) {
                return null;
            }
            securityGuardParamContext.appKey = appKeyByIndex;
        } else if (dataContext.extData.length == 0) {
            return null;
        } else {
            securityGuardParamContext.appKey = new String(dataContext.extData);
        }
        return this.indieKitProxy.indieKitRequest(securityGuardParamContext);
    }

    public String getTopSign(TreeMap<String, String> treeMap) {
        return getTopSign(treeMap, new DataContext(0, (byte[]) null));
    }

    public String getTopSign(TreeMap<String, String> treeMap, DataContext dataContext) {
        if (this.signProxy == null || treeMap == null || treeMap.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder(512);
        for (String next : treeMap.keySet()) {
            String str = treeMap.get(next);
            if (str != null) {
                sb.append(next);
                sb.append(str);
            } else {
                sb.append(next);
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("INPUT", sb.toString());
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 2;
        if (dataContext.extData == null) {
            dataContext.index = dataContext.index < 0 ? 0 : dataContext.index;
            String appKeyByIndex = this.manager.getStaticDataStoreComp().getAppKeyByIndex(dataContext.index);
            if (appKeyByIndex == null || "".equals(appKeyByIndex)) {
                return null;
            }
            securityGuardParamContext.appKey = appKeyByIndex;
        } else if (dataContext.extData.length == 0) {
            return null;
        } else {
            securityGuardParamContext.appKey = new String(dataContext.extData);
        }
        return this.signProxy.signRequest(securityGuardParamContext);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getExternalSign(java.util.LinkedHashMap<java.lang.String, java.lang.String> r7, com.taobao.securityjni.tools.DataContext r8) {
        /*
            r6 = this;
            com.taobao.wireless.security.sdk.securesignature.ISecureSignatureComponent r0 = r6.signProxy
            r1 = 0
            if (r0 == 0) goto L_0x00dc
            if (r7 == 0) goto L_0x00dc
            boolean r0 = r7.isEmpty()
            if (r0 != 0) goto L_0x00dc
            if (r8 != 0) goto L_0x0011
            goto L_0x00dc
        L_0x0011:
            int r0 = r8.category
            r2 = -1
            switch(r0) {
                case 0: goto L_0x0034;
                case 1: goto L_0x002d;
                case 2: goto L_0x0026;
                case 3: goto L_0x001f;
                case 4: goto L_0x0018;
                default: goto L_0x0017;
            }
        L_0x0017:
            goto L_0x003b
        L_0x0018:
            int r0 = r8.type
            if (r0 != 0) goto L_0x003b
            r0 = 14
            goto L_0x003c
        L_0x001f:
            int r0 = r8.type
            if (r0 != 0) goto L_0x003b
            r0 = 8
            goto L_0x003c
        L_0x0026:
            int r0 = r8.type
            if (r0 != 0) goto L_0x003b
            r0 = 12
            goto L_0x003c
        L_0x002d:
            int r0 = r8.type
            if (r0 != 0) goto L_0x003b
            r0 = 11
            goto L_0x003c
        L_0x0034:
            int r0 = r8.type
            if (r0 != 0) goto L_0x003b
            r0 = 10
            goto L_0x003c
        L_0x003b:
            r0 = -1
        L_0x003c:
            if (r0 == r2) goto L_0x00db
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = 768(0x300, float:1.076E-42)
            r2.<init>(r3)
            java.util.Set r3 = r7.keySet()
            java.util.Iterator r3 = r3.iterator()
        L_0x004d:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0078
            java.lang.Object r4 = r3.next()
            java.lang.String r4 = (java.lang.String) r4
            if (r4 == 0) goto L_0x004d
            java.lang.Object r5 = r7.get(r4)
            java.lang.String r5 = (java.lang.String) r5
            if (r5 == 0) goto L_0x006f
            r2.append(r4)
            r4 = 61
            r2.append(r4)
            r2.append(r5)
            goto L_0x0072
        L_0x006f:
            r2.append(r4)
        L_0x0072:
            r4 = 38
            r2.append(r4)
            goto L_0x004d
        L_0x0078:
            int r7 = r2.length()
            r3 = 1
            if (r7 >= r3) goto L_0x0080
            return r1
        L_0x0080:
            java.util.HashMap r7 = new java.util.HashMap
            r7.<init>()
            java.lang.String r4 = "INPUT"
            int r5 = r2.length()
            int r5 = r5 - r3
            r3 = 0
            java.lang.String r2 = r2.substring(r3, r5)
            r7.put(r4, r2)
            com.taobao.wireless.security.sdk.SecurityGuardParamContext r2 = new com.taobao.wireless.security.sdk.SecurityGuardParamContext
            r2.<init>()
            r2.paramMap = r7
            r2.requestType = r0
            byte[] r7 = r8.extData
            if (r7 == 0) goto L_0x00b1
            byte[] r7 = r8.extData
            int r7 = r7.length
            if (r7 != 0) goto L_0x00a7
            return r1
        L_0x00a7:
            java.lang.String r7 = new java.lang.String
            byte[] r8 = r8.extData
            r7.<init>(r8)
            r2.appKey = r7
            goto L_0x00d3
        L_0x00b1:
            int r7 = r8.index
            if (r7 >= 0) goto L_0x00b6
            goto L_0x00b8
        L_0x00b6:
            int r3 = r8.index
        L_0x00b8:
            r8.index = r3
            com.taobao.wireless.security.sdk.SecurityGuardManager r7 = r6.manager
            com.taobao.wireless.security.sdk.staticdatastore.IStaticDataStoreComponent r7 = r7.getStaticDataStoreComp()
            int r8 = r8.index
            java.lang.String r7 = r7.getAppKeyByIndex(r8)
            if (r7 == 0) goto L_0x00da
            java.lang.String r8 = ""
            boolean r8 = r8.equals(r7)
            if (r8 == 0) goto L_0x00d1
            goto L_0x00da
        L_0x00d1:
            r2.appKey = r7
        L_0x00d3:
            com.taobao.wireless.security.sdk.securesignature.ISecureSignatureComponent r7 = r6.signProxy
            java.lang.String r1 = r7.signRequest(r2)
            goto L_0x00db
        L_0x00da:
            return r1
        L_0x00db:
            return r1
        L_0x00dc:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.securityjni.SecretUtil.getExternalSign(java.util.LinkedHashMap, com.taobao.securityjni.tools.DataContext):java.lang.String");
    }

    public String getQianNiuSign(byte[] bArr, byte[] bArr2) {
        if (this.signProxy == null || bArr == null || bArr2 == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String str = new String(bArr);
        String str2 = new String(bArr2);
        hashMap.put(SecureSignatureDefine.SG_KEY_SIGN_STR1, str);
        hashMap.put(SecureSignatureDefine.SG_KEY_SIGN_STR2, str2);
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 9;
        return this.signProxy.signRequest(securityGuardParamContext);
    }

    public String getMtopV4Sign(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, DataContext dataContext) {
        if (this.signProxy == null || dataContext == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("ECODE", str);
        hashMap.put("DATA", str2);
        hashMap.put("TIME", str3);
        hashMap.put("API", str4);
        hashMap.put("V", str5);
        hashMap.put(SecureSignatureDefine.SG_KEY_SIGN_SID, str6);
        hashMap.put(SecureSignatureDefine.SG_KEY_SIGN_TTID, str7);
        hashMap.put(SecureSignatureDefine.SG_KEY_SIGN_DEVICDEID, str8);
        hashMap.put(SecureSignatureDefine.SG_KEY_SIGN_LAT, str9);
        hashMap.put(SecureSignatureDefine.SG_KEY_SIGN_LNG, str10);
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 4;
        if (dataContext.extData == null) {
            dataContext.index = dataContext.index < 0 ? 0 : dataContext.index;
            String appKeyByIndex = this.manager.getStaticDataStoreComp().getAppKeyByIndex(dataContext.index);
            if (appKeyByIndex == null || "".equals(appKeyByIndex)) {
                return null;
            }
            securityGuardParamContext.appKey = appKeyByIndex;
        } else if (dataContext.extData.length == 0) {
            return null;
        } else {
            securityGuardParamContext.appKey = new String(dataContext.extData);
        }
        return this.signProxy.signRequest(securityGuardParamContext);
    }

    public String getMtopV4RespSign(String str, DataContext dataContext) {
        if (this.signProxy == null || dataContext == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("INPUT", str);
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 5;
        if (dataContext.extData == null) {
            dataContext.index = dataContext.index < 0 ? 0 : dataContext.index;
            String appKeyByIndex = this.manager.getStaticDataStoreComp().getAppKeyByIndex(dataContext.index);
            if (appKeyByIndex == null || "".equals(appKeyByIndex)) {
                return null;
            }
            securityGuardParamContext.appKey = appKeyByIndex;
        } else if (dataContext.extData.length == 0) {
            return null;
        } else {
            securityGuardParamContext.appKey = new String(dataContext.extData);
        }
        return this.signProxy.signRequest(securityGuardParamContext);
    }

    public String getLaiwangSign(String str, String str2, DataContext dataContext) {
        if (this.signProxy == null || dataContext == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("INPUT", str);
        hashMap.put(SecureSignatureDefine.SG_KEY_SIGN_KEY, str2);
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 7;
        if (dataContext.extData == null) {
            dataContext.index = dataContext.index < 0 ? 0 : dataContext.index;
            String appKeyByIndex = this.manager.getStaticDataStoreComp().getAppKeyByIndex(dataContext.index);
            if (appKeyByIndex == null || "".equals(appKeyByIndex)) {
                return null;
            }
            securityGuardParamContext.appKey = appKeyByIndex;
        } else if (dataContext.extData.length == 0) {
            return null;
        } else {
            securityGuardParamContext.appKey = new String(dataContext.extData);
        }
        return this.signProxy.signRequest(securityGuardParamContext);
    }

    public String indieKitRequest(SecurityGuardParamContext securityGuardParamContext) {
        if (this.indieKitProxy == null) {
            return null;
        }
        return this.indieKitProxy.indieKitRequest(securityGuardParamContext);
    }

    public int reportSusText(String str, String str2) {
        throw new UnsupportedOperationException();
    }
}
