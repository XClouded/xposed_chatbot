package com.ut.mini;

import android.app.Activity;
import android.net.Uri;
import com.alibaba.analytics.core.config.UTTPKBiz;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.alimama.moon.usertrack.UTHelper;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.ut.mini.internal.UTTeamWork;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UTHybridHelper {
    private static final String TAG = "UTHybridHelper";
    private static UTHybridHelper s_instance = new UTHybridHelper();

    public static UTHybridHelper getInstance() {
        return s_instance;
    }

    public void setH5Url(String str) {
        UTAnalytics.getInstance().getDefaultTracker().setH5Url(str);
    }

    public void h5UT(Map<String, String> map, Object obj) {
        Logger.d(TAG, "h5UT view", obj);
        if (map == null || map.size() == 0) {
            Logger.e("h5UT", "dataMap is empty");
            return;
        }
        String str = map.get("functype");
        if (str == null) {
            Logger.e("h5UT", "funcType is null");
            return;
        }
        String str2 = map.get("utjstype");
        if (str2 == null || str2.equals("0") || str2.equals("1")) {
            map.remove("functype");
            Date date = new Date();
            if (str.equals("2001")) {
                h5Page(date, map, obj);
                UTTeamWork.getInstance().dispatchH5JSCall(obj, map);
                return;
            }
            h5Ctrl(str, date, map);
            return;
        }
        Logger.e("h5UT", "utjstype should be 1 or 0 or null");
    }

    public void h5UT2(Map<String, String> map, Object obj) {
        Logger.d(TAG, "h5UT2 view", obj);
        if (map == null || map.size() == 0) {
            Logger.e("h5UT", "dataMap is empty");
            return;
        }
        String remove = map.remove("functype");
        if (remove == null) {
            Logger.e("h5UT", "funcType is null");
        } else if (remove.equals("page")) {
            map.remove("funcId");
            h5Page2(map, obj);
            UTTeamWork.getInstance().dispatchH5JSCall(obj, map);
        } else if (remove.equals("ctrl")) {
            h5Ctrl2(map);
        }
    }

    private void h5Ctrl2(Map<String, String> map) {
        int i;
        if (map != null && map.size() != 0) {
            try {
                i = Integer.parseInt(map.remove("funcId"));
            } catch (Throwable unused) {
                i = -1;
            }
            if (i != -1) {
                String remove = map.remove("url");
                if (remove == null || StringUtils.isEmpty(remove)) {
                    Logger.i("h5Ctrl", "pageName is null,return");
                    return;
                }
                String remove2 = map.remove("logkey");
                if (remove2 == null || StringUtils.isEmpty(remove2)) {
                    Logger.i("h5Ctrl", "logkey is null,return");
                    return;
                }
                UTOriginalCustomHitBuilder uTOriginalCustomHitBuilder = new UTOriginalCustomHitBuilder(remove, i, remove2, (String) null, (String) null, map);
                UTTracker defaultTracker = UTAnalytics.getInstance().getDefaultTracker();
                if (defaultTracker != null) {
                    defaultTracker.send(uTOriginalCustomHitBuilder.build());
                    return;
                }
                Logger.e("h5Ctrl event error", "Fatal Error,must call setRequestAuthentication method first.");
            }
        }
    }

    private void h5Page2(Map<String, String> map, Object obj) {
        Map<String, String> uTPageStateObjectSpmMap;
        Logger.d();
        if (map == null || map.size() == 0) {
            Logger.i("h5Page2", "dataMap is null or empty,return");
            return;
        }
        String remove = map.remove("url");
        if (remove == null || StringUtils.isEmpty(remove)) {
            Logger.i("h5Page2", "pageName is null,return");
            return;
        }
        String refPage = UTVariables.getInstance().getRefPage();
        int i = UTPageHitHelper.getInstance().isH52001(obj) ? 2001 : 2006;
        HashMap hashMap = new HashMap(map);
        if (2001 == i) {
            UTVariables.getInstance().setRefPage(remove);
            Map<String, String> nextPageProperties = UTPageHitHelper.getInstance().getNextPageProperties(obj);
            if (nextPageProperties != null && nextPageProperties.size() > 0) {
                hashMap.putAll(nextPageProperties);
            }
            if ((obj instanceof Activity) && (uTPageStateObjectSpmMap = getUTPageStateObjectSpmMap(obj, map, remove, map.get("_h5url"), nextPageProperties)) != null) {
                hashMap.putAll(uTPageStateObjectSpmMap);
            }
        }
        UTOriginalCustomHitBuilder uTOriginalCustomHitBuilder = new UTOriginalCustomHitBuilder(remove, i, refPage, (String) null, (String) null, hashMap);
        try {
            String tpkString = UTTPKBiz.getInstance().getTpkString(Uri.parse(map.get("_h5url")), (Map<String, String>) null);
            if (!StringUtils.isEmpty(tpkString)) {
                uTOriginalCustomHitBuilder.setProperty("_tpk", tpkString);
            }
        } catch (Throwable th) {
            Logger.e((String) null, th, new Object[0]);
        }
        UTTracker defaultTracker = UTAnalytics.getInstance().getDefaultTracker();
        if (defaultTracker != null) {
            defaultTracker.send(UTPvidHelper.processH5PagePvid(i, uTOriginalCustomHitBuilder.build()));
        } else {
            Logger.e("h5Page event error", "Fatal Error,must call setRequestAuthentication method first.");
        }
        UTPageHitHelper.getInstance().setH5Called(obj);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00c4 A[Catch:{ Exception -> 0x00ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00e4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void h5Page(java.util.Date r19, java.util.Map<java.lang.String, java.lang.String> r20, java.lang.Object r21) {
        /*
            r18 = this;
            r1 = r18
            r0 = r20
            r2 = r21
            com.alibaba.analytics.utils.Logger.d()
            if (r0 == 0) goto L_0x0107
            int r3 = r20.size()
            if (r3 != 0) goto L_0x0013
            goto L_0x0107
        L_0x0013:
            java.lang.String r3 = "urlpagename"
            java.lang.Object r3 = r0.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "url"
            java.lang.Object r4 = r0.get(r4)
            java.lang.String r4 = (java.lang.String) r4
            java.lang.String r3 = r1.getH5PageName(r3, r4)
            if (r3 == 0) goto L_0x00f9
            boolean r5 = com.alibaba.analytics.utils.StringUtils.isEmpty(r3)
            if (r5 == 0) goto L_0x0031
            goto L_0x00f9
        L_0x0031:
            java.lang.String r5 = "utjstype"
            java.lang.Object r5 = r0.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r6 = "utjstype"
            r0.remove(r6)
            r14 = 0
            if (r5 == 0) goto L_0x0059
            java.lang.String r6 = "0"
            boolean r6 = r5.equals(r6)
            if (r6 == 0) goto L_0x004a
            goto L_0x0059
        L_0x004a:
            java.lang.String r6 = "1"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x0057
            java.util.Map r5 = r1.h5PageParseArgsWithOutAplus(r0)
            goto L_0x005d
        L_0x0057:
            r11 = r14
            goto L_0x005e
        L_0x0059:
            java.util.Map r5 = r1.h5PageParseArgsWithAplus(r0)
        L_0x005d:
            r11 = r5
        L_0x005e:
            r5 = 2006(0x7d6, float:2.811E-42)
            com.ut.mini.UTPageHitHelper r6 = com.ut.mini.UTPageHitHelper.getInstance()
            boolean r6 = r6.isH52001(r2)
            r15 = 2001(0x7d1, float:2.804E-42)
            if (r6 == 0) goto L_0x006f
            r10 = 2001(0x7d1, float:2.804E-42)
            goto L_0x0071
        L_0x006f:
            r10 = 2006(0x7d6, float:2.811E-42)
        L_0x0071:
            com.ut.mini.UTVariables r5 = com.ut.mini.UTVariables.getInstance()
            java.lang.String r8 = r5.getRefPage()
            com.ut.mini.internal.UTOriginalCustomHitBuilder r9 = new com.ut.mini.internal.UTOriginalCustomHitBuilder
            r16 = 0
            r17 = 0
            r5 = r9
            r6 = r3
            r7 = r10
            r12 = r9
            r9 = r16
            r13 = r10
            r10 = r17
            r5.<init>(r6, r7, r8, r9, r10, r11)
            if (r15 != r13) goto L_0x00b2
            com.ut.mini.UTVariables r5 = com.ut.mini.UTVariables.getInstance()
            r5.setRefPage(r3)
            com.ut.mini.UTPageHitHelper r3 = com.ut.mini.UTPageHitHelper.getInstance()
            java.util.Map r3 = r3.getNextPageProperties(r2)
            if (r3 == 0) goto L_0x00a7
            int r5 = r3.size()
            if (r5 <= 0) goto L_0x00a7
            r12.setProperties(r3)
        L_0x00a7:
            boolean r5 = r2 instanceof android.app.Activity
            if (r5 == 0) goto L_0x00b2
            java.util.Map r0 = r1.getUTPageStateObjectSpmMap(r2, r0, r4, r3)
            r12.setProperties(r0)
        L_0x00b2:
            com.alibaba.analytics.core.config.UTTPKBiz r0 = com.alibaba.analytics.core.config.UTTPKBiz.getInstance()     // Catch:{ Exception -> 0x00ca }
            android.net.Uri r3 = android.net.Uri.parse(r4)     // Catch:{ Exception -> 0x00ca }
            java.lang.String r0 = r0.getTpkString(r3, r14)     // Catch:{ Exception -> 0x00ca }
            boolean r3 = com.alibaba.analytics.utils.StringUtils.isEmpty(r0)     // Catch:{ Exception -> 0x00ca }
            if (r3 != 0) goto L_0x00ce
            java.lang.String r3 = "_tpk"
            r12.setProperty(r3, r0)     // Catch:{ Exception -> 0x00ca }
            goto L_0x00ce
        L_0x00ca:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00ce:
            com.ut.mini.UTAnalytics r0 = com.ut.mini.UTAnalytics.getInstance()
            com.ut.mini.UTTracker r0 = r0.getDefaultTracker()
            if (r0 == 0) goto L_0x00e4
            java.util.Map r3 = r12.build()
            java.util.Map r3 = com.ut.mini.UTPvidHelper.processH5PagePvid(r13, r3)
            r0.send(r3)
            goto L_0x00f1
        L_0x00e4:
            java.lang.String r0 = "h5Page event error"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r4 = "Fatal Error,must call setRequestAuthentication method first."
            r5 = 0
            r3[r5] = r4
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r0, (java.lang.Object[]) r3)
        L_0x00f1:
            com.ut.mini.UTPageHitHelper r0 = com.ut.mini.UTPageHitHelper.getInstance()
            r0.setH5Called(r2)
            return
        L_0x00f9:
            r3 = 1
            r5 = 0
            java.lang.String r0 = "h5Page"
            java.lang.Object[] r2 = new java.lang.Object[r3]
            java.lang.String r3 = "pageName is null,return"
            r2[r5] = r3
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r0, (java.lang.Object[]) r2)
            return
        L_0x0107:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTHybridHelper.h5Page(java.util.Date, java.util.Map, java.lang.Object):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x008e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void h5Ctrl(java.lang.String r8, java.util.Date r9, java.util.Map<java.lang.String, java.lang.String> r10) {
        /*
            r7 = this;
            r9 = -1
            int r8 = java.lang.Integer.parseInt(r8)     // Catch:{ Throwable -> 0x0007 }
            r2 = r8
            goto L_0x0008
        L_0x0007:
            r2 = -1
        L_0x0008:
            if (r2 != r9) goto L_0x000b
            return
        L_0x000b:
            if (r10 == 0) goto L_0x00b2
            int r8 = r10.size()
            if (r8 != 0) goto L_0x0015
            goto L_0x00b2
        L_0x0015:
            java.lang.String r8 = "urlpagename"
            java.lang.Object r8 = r10.get(r8)
            java.lang.String r8 = (java.lang.String) r8
            java.lang.String r9 = "url"
            java.lang.Object r9 = r10.get(r9)
            java.lang.String r9 = (java.lang.String) r9
            java.lang.String r1 = r7.getH5PageName(r8, r9)
            r8 = 0
            r9 = 1
            if (r1 == 0) goto L_0x00a6
            boolean r0 = com.alibaba.analytics.utils.StringUtils.isEmpty(r1)
            if (r0 == 0) goto L_0x0035
            goto L_0x00a6
        L_0x0035:
            java.lang.String r0 = "logkey"
            java.lang.Object r0 = r10.get(r0)
            r3 = r0
            java.lang.String r3 = (java.lang.String) r3
            if (r3 == 0) goto L_0x009a
            boolean r0 = com.alibaba.analytics.utils.StringUtils.isEmpty(r3)
            if (r0 == 0) goto L_0x0047
            goto L_0x009a
        L_0x0047:
            r0 = 0
            java.lang.String r4 = "utjstype"
            java.lang.Object r4 = r10.get(r4)
            java.lang.String r4 = (java.lang.String) r4
            java.lang.String r5 = "utjstype"
            r10.remove(r5)
            if (r4 == 0) goto L_0x006f
            java.lang.String r5 = "0"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L_0x0060
            goto L_0x006f
        L_0x0060:
            java.lang.String r5 = "1"
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L_0x006d
            java.util.Map r10 = r7.h5CtrlParseArgsWithOutAplus(r10)
            goto L_0x0073
        L_0x006d:
            r6 = r0
            goto L_0x0074
        L_0x006f:
            java.util.Map r10 = r7.h5CtrlParseArgsWithAplus(r10)
        L_0x0073:
            r6 = r10
        L_0x0074:
            com.ut.mini.internal.UTOriginalCustomHitBuilder r10 = new com.ut.mini.internal.UTOriginalCustomHitBuilder
            r4 = 0
            r5 = 0
            r0 = r10
            r0.<init>(r1, r2, r3, r4, r5, r6)
            com.ut.mini.UTAnalytics r0 = com.ut.mini.UTAnalytics.getInstance()
            com.ut.mini.UTTracker r0 = r0.getDefaultTracker()
            if (r0 == 0) goto L_0x008e
            java.util.Map r8 = r10.build()
            r0.send(r8)
            goto L_0x0099
        L_0x008e:
            java.lang.String r10 = "h5Ctrl event error"
            java.lang.Object[] r9 = new java.lang.Object[r9]
            java.lang.String r0 = "Fatal Error,must call setRequestAuthentication method first."
            r9[r8] = r0
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r10, (java.lang.Object[]) r9)
        L_0x0099:
            return
        L_0x009a:
            java.lang.String r10 = "h5Ctrl"
            java.lang.Object[] r9 = new java.lang.Object[r9]
            java.lang.String r0 = "logkey is null,return"
            r9[r8] = r0
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r10, (java.lang.Object[]) r9)
            return
        L_0x00a6:
            java.lang.String r10 = "h5Ctrl"
            java.lang.Object[] r9 = new java.lang.Object[r9]
            java.lang.String r0 = "pageName is null,return"
            r9[r8] = r0
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r10, (java.lang.Object[]) r9)
            return
        L_0x00b2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTHybridHelper.h5Ctrl(java.lang.String, java.util.Date, java.util.Map):void");
    }

    private Map<String, String> h5PageParseArgsWithAplus(Map<String, String> map) {
        Map<String, String> argsMap;
        if (map == null || map.size() == 0) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String str = map.get("url");
        hashMap.put("_h5url", str == null ? "" : str);
        if (str != null) {
            try {
                int indexOf = str.indexOf(63);
                if (indexOf > 0 && (argsMap = JTrack.Page.getArgsMap(str.substring(0, indexOf), Uri.parse(str))) != null) {
                    hashMap.putAll(argsMap);
                }
            } catch (Throwable unused) {
            }
        }
        if (str != null) {
            Uri parse = Uri.parse(str);
            String queryParameter = parse.getQueryParameter("spm");
            if (queryParameter == null || StringUtils.isEmpty(queryParameter)) {
                hashMap.put("spm", Constants.PARAM_OUTER_SPM_NONE);
            } else {
                hashMap.put("spm", queryParameter);
            }
            String queryParameter2 = parse.getQueryParameter(UTHelper.SCM_URI_PARAMETER_KEY);
            if (queryParameter2 != null && !StringUtils.isEmpty(queryParameter2)) {
                hashMap.put(UTHelper.SCM_URI_PARAMETER_KEY, queryParameter2);
            }
            String queryParameter3 = parse.getQueryParameter("pg1stepk");
            if (queryParameter3 != null && !StringUtils.isEmpty(queryParameter3)) {
                hashMap.put("pg1stepk", queryParameter3);
            }
            if (!StringUtils.isEmpty(parse.getQueryParameter("point"))) {
                hashMap.put("issb", "1");
            }
        } else {
            hashMap.put("spm", Constants.PARAM_OUTER_SPM_NONE);
        }
        String str2 = map.get("spmcnt");
        if (str2 == null) {
            str2 = "";
        }
        hashMap.put("_spmcnt", str2);
        String str3 = map.get("spmpre");
        if (str3 == null) {
            str3 = "";
        }
        hashMap.put("_spmpre", str3);
        String str4 = map.get("lzsid");
        if (str4 == null) {
            str4 = "";
        }
        hashMap.put("_lzsid", str4);
        String str5 = map.get("extendargs");
        if (str5 == null) {
            str5 = "";
        }
        hashMap.put("_h5ea", str5);
        String str6 = map.get("cna");
        if (str6 == null) {
            str6 = "";
        }
        hashMap.put("_cna", str6);
        hashMap.put("_ish5", "1");
        return hashMap;
    }

    private Map<String, String> h5PageParseArgsWithOutAplus(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String str = map.get("url");
        if (str == null) {
            str = "";
        }
        hashMap.put("_h5url", str);
        String str2 = map.get("extendargs");
        if (str2 == null) {
            str2 = "";
        }
        hashMap.put("_h5ea", str2);
        hashMap.put("_ish5", "1");
        return hashMap;
    }

    private Map<String, String> h5CtrlParseArgsWithAplus(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String str = map.get("logkeyargs");
        if (str == null) {
            str = "";
        }
        hashMap.put("_lka", str);
        String str2 = map.get("cna");
        if (str2 == null) {
            str2 = "";
        }
        hashMap.put("_cna", str2);
        String str3 = map.get("extendargs");
        if (str3 == null) {
            str3 = "";
        }
        hashMap.put("_h5ea", str3);
        hashMap.put("_ish5", "1");
        return hashMap;
    }

    private Map<String, String> h5CtrlParseArgsWithOutAplus(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String str = map.get("extendargs");
        if (str == null) {
            str = "";
        }
        hashMap.put("_h5ea", str);
        hashMap.put("_ish5", "1");
        return hashMap;
    }

    private String getH5PageName(String str, String str2) {
        if (str != null && !StringUtils.isEmpty(str)) {
            return str;
        }
        if (StringUtils.isEmpty(str2)) {
            return "";
        }
        int indexOf = str2.indexOf("?");
        if (indexOf == -1) {
            return str2;
        }
        return str2.substring(0, indexOf);
    }

    private Map<String, String> getUTPageStateObjectSpmMap(Object obj, Map<String, String> map, String str, Map<String, String> map2) {
        return getUTPageStateObjectSpmMap(obj, map, str, "", map2, false);
    }

    private Map<String, String> getUTPageStateObjectSpmMap(Object obj, Map<String, String> map, String str, String str2, Map<String, String> map2) {
        return getUTPageStateObjectSpmMap(obj, map, str, str2, map2, true);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:7|8|9|(6:11|(1:13)|14|(1:16)|17|(2:19|20))|21|22|(6:24|(1:26)|27|(1:29)|30|(2:32|33))|34) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x007e */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x03d1  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x03dc  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x03e7  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x03f2  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x03fd  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x0408  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0084 A[Catch:{ Exception -> 0x00af }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0103  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0216  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x02e4  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x03bb  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x03c6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<java.lang.String, java.lang.String> getUTPageStateObjectSpmMap(java.lang.Object r10, java.util.Map<java.lang.String, java.lang.String> r11, java.lang.String r12, java.lang.String r13, java.util.Map<java.lang.String, java.lang.String> r14, boolean r15) {
        /*
            r9 = this;
            com.ut.mini.UTPageHitHelper r0 = com.ut.mini.UTPageHitHelper.getInstance()
            com.ut.mini.UTPageHitHelper$UTPageStateObject r0 = r0.getOrNewUTPageStateObject(r10)
            r1 = 0
            if (r0 != 0) goto L_0x000c
            return r1
        L_0x000c:
            java.lang.String r2 = ""
            java.lang.String r3 = ""
            if (r14 == 0) goto L_0x0023
            java.lang.String r2 = "utparam-url"
            java.lang.Object r2 = r14.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            java.lang.String r3 = "utparam-cnt"
            java.lang.Object r14 = r14.get(r3)
            r3 = r14
            java.lang.String r3 = (java.lang.String) r3
        L_0x0023:
            if (r15 == 0) goto L_0x00b1
            java.lang.String r14 = "spm-cnt"
            java.lang.Object r14 = r11.get(r14)
            java.lang.String r14 = (java.lang.String) r14
            java.lang.String r1 = "utparam-cnt"
            java.lang.Object r1 = r11.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            java.lang.String r4 = "spm-url"
            java.lang.Object r4 = r11.get(r4)
            java.lang.String r4 = (java.lang.String) r4
            java.lang.String r5 = "utparam-url"
            java.lang.Object r5 = r11.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r6 = "scm"
            java.lang.Object r6 = r11.get(r6)
            java.lang.String r6 = (java.lang.String) r6
            boolean r7 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Exception -> 0x007e }
            if (r7 != 0) goto L_0x007e
            android.net.Uri r13 = android.net.Uri.parse(r13)     // Catch:{ Exception -> 0x007e }
            boolean r7 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Exception -> 0x007e }
            if (r7 == 0) goto L_0x0064
            java.lang.String r7 = "spm"
            java.lang.String r7 = r13.getQueryParameter(r7)     // Catch:{ Exception -> 0x007e }
            r4 = r7
        L_0x0064:
            boolean r7 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x007e }
            if (r7 == 0) goto L_0x0071
            java.lang.String r7 = "utparam"
            java.lang.String r7 = r13.getQueryParameter(r7)     // Catch:{ Exception -> 0x007e }
            r5 = r7
        L_0x0071:
            boolean r7 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x007e }
            if (r7 == 0) goto L_0x007e
            java.lang.String r7 = "scm"
            java.lang.String r13 = r13.getQueryParameter(r7)     // Catch:{ Exception -> 0x007e }
            r6 = r13
        L_0x007e:
            boolean r13 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Exception -> 0x00af }
            if (r13 != 0) goto L_0x00af
            android.net.Uri r12 = android.net.Uri.parse(r12)     // Catch:{ Exception -> 0x00af }
            boolean r13 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Exception -> 0x00af }
            if (r13 == 0) goto L_0x0095
            java.lang.String r13 = "spm"
            java.lang.String r13 = r12.getQueryParameter(r13)     // Catch:{ Exception -> 0x00af }
            r4 = r13
        L_0x0095:
            boolean r13 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x00af }
            if (r13 == 0) goto L_0x00a2
            java.lang.String r13 = "utparam"
            java.lang.String r13 = r12.getQueryParameter(r13)     // Catch:{ Exception -> 0x00af }
            r5 = r13
        L_0x00a2:
            boolean r13 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x00af }
            if (r13 == 0) goto L_0x00af
            java.lang.String r13 = "scm"
            java.lang.String r12 = r12.getQueryParameter(r13)     // Catch:{ Exception -> 0x00af }
            r6 = r12
        L_0x00af:
            r13 = r1
            goto L_0x00df
        L_0x00b1:
            java.lang.String r13 = "spmcnt"
            java.lang.Object r13 = r11.get(r13)
            r14 = r13
            java.lang.String r14 = (java.lang.String) r14
            java.lang.String r13 = "utparamcnt"
            java.lang.Object r13 = r11.get(r13)
            java.lang.String r13 = (java.lang.String) r13
            android.net.Uri r12 = android.net.Uri.parse(r12)     // Catch:{ Exception -> 0x00dc }
            java.lang.String r4 = "spm"
            java.lang.String r4 = r12.getQueryParameter(r4)     // Catch:{ Exception -> 0x00dc }
            java.lang.String r5 = "utparam"
            java.lang.String r5 = r12.getQueryParameter(r5)     // Catch:{ Exception -> 0x00da }
            java.lang.String r6 = "scm"
            java.lang.String r12 = r12.getQueryParameter(r6)     // Catch:{ Exception -> 0x00de }
            r6 = r12
            goto L_0x00df
        L_0x00da:
            r5 = r1
            goto L_0x00de
        L_0x00dc:
            r4 = r1
            r5 = r4
        L_0x00de:
            r6 = r1
        L_0x00df:
            java.lang.String r12 = r0.mSpmUrl
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 == 0) goto L_0x00e9
            r0.mSpmUrl = r4
        L_0x00e9:
            java.lang.String r12 = r0.mUtparamUrl
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 == 0) goto L_0x00f3
            r0.mUtparamUrl = r5
        L_0x00f3:
            java.lang.String r12 = r0.mScmUrl
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 == 0) goto L_0x00fd
            r0.mScmUrl = r6
        L_0x00fd:
            boolean r12 = r0.mIsH5Page
            r1 = 0
            r7 = 1
            if (r12 != 0) goto L_0x0212
            r0.mSpmCnt = r14
            r0.mSpmUrl = r4
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r12 = r12.getLastCacheKey()
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 != 0) goto L_0x011f
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r12 = r12.getLastCacheKeySpmUrl()
            r0.mSpmPre = r12
        L_0x011f:
            r0.mScmUrl = r6
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r12 = r12.getLastCacheKey()
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 != 0) goto L_0x0139
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r12 = r12.getLastCacheKeyScmUrl()
            r0.mScmPre = r12
        L_0x0139:
            r0.mIsBack = r7
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r13 = r12.refreshUtParam(r13, r3)
            r0.mUtparamCnt = r13
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r12 = r12.refreshUtParam(r5, r2)
            com.ut.mini.UTPageHitHelper r2 = com.ut.mini.UTPageHitHelper.getInstance()
            com.ut.mini.UTPageHitHelper r3 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r3 = r3.getLastCacheKeyUtParamCnt()
            java.lang.String r5 = r2.refreshUtParam(r12, r3)
            r0.mUtparamUrl = r5
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r12 = r12.getLastCacheKeyUtParam()
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 != 0) goto L_0x0177
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r12 = r12.getLastCacheKeyUtParam()
            r0.mUtparamPre = r12
        L_0x0177:
            java.lang.String r10 = r9._getPageEventObjectCacheKey(r10)
            com.ut.mini.UTPageHitHelper r12 = com.ut.mini.UTPageHitHelper.getInstance()
            r12.setLastCacheKey(r10)
            com.ut.mini.UTPageHitHelper r10 = com.ut.mini.UTPageHitHelper.getInstance()
            r10.setLastCacheKeySpmUrl(r4)
            com.ut.mini.UTPageHitHelper r10 = com.ut.mini.UTPageHitHelper.getInstance()
            r10.setLastCacheKeyScmUrl(r6)
            com.ut.mini.UTPageHitHelper r10 = com.ut.mini.UTPageHitHelper.getInstance()
            r10.setLastCacheKeyUtParam(r5)
            com.ut.mini.UTPageHitHelper r10 = com.ut.mini.UTPageHitHelper.getInstance()
            r10.setLastCacheKeyUtParamCnt(r13)
            java.lang.String r10 = "h5Page"
            java.lang.Object[] r12 = new java.lang.Object[r7]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "mLastCacheKey:"
            r2.append(r3)
            com.ut.mini.UTPageHitHelper r3 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r3 = r3.getLastCacheKey()
            r2.append(r3)
            java.lang.String r3 = ",mLastCacheKeySpmUrl:"
            r2.append(r3)
            com.ut.mini.UTPageHitHelper r3 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r3 = r3.getLastCacheKeySpmUrl()
            r2.append(r3)
            java.lang.String r3 = ",mLastCacheKeyUtParam:"
            r2.append(r3)
            com.ut.mini.UTPageHitHelper r3 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r3 = r3.getLastCacheKeyUtParam()
            r2.append(r3)
            java.lang.String r3 = ",mLastCacheKeyUtParamCnt:"
            r2.append(r3)
            com.ut.mini.UTPageHitHelper r3 = com.ut.mini.UTPageHitHelper.getInstance()
            java.lang.String r3 = r3.getLastCacheKeyUtParamCnt()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r12[r1] = r2
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r10, (java.lang.Object[]) r12)
            java.lang.String r10 = "h5Page"
            java.lang.Object[] r12 = new java.lang.Object[r7]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UTHybridHelper lPageStateObject:"
            r2.append(r3)
            java.util.Map r3 = r0.getPageStatMap(r1)
            java.lang.String r3 = r3.toString()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r12[r1] = r2
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r10, (java.lang.Object[]) r12)
        L_0x0212:
            r0.mIsH5Page = r7
            if (r15 == 0) goto L_0x02e4
            java.lang.String r10 = "spm-pre"
            java.lang.Object r10 = r11.get(r10)
            java.lang.String r10 = (java.lang.String) r10
            java.lang.String r12 = "h5Page"
            java.lang.Object[] r15 = new java.lang.Object[r7]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UTHybridHelper spm-pre:"
            r2.append(r3)
            r2.append(r10)
            java.lang.String r2 = r2.toString()
            r15[r1] = r2
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r12, (java.lang.Object[]) r15)
            boolean r12 = android.text.TextUtils.isEmpty(r10)
            if (r12 == 0) goto L_0x025a
            java.lang.String r10 = r0.mSpmPre
            java.lang.String r12 = "h5Page"
            java.lang.Object[] r15 = new java.lang.Object[r7]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UTHybridHelper mSpmPre:"
            r2.append(r3)
            r2.append(r10)
            java.lang.String r2 = r2.toString()
            r15[r1] = r2
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r12, (java.lang.Object[]) r15)
        L_0x025a:
            java.lang.String r12 = "scm-pre"
            java.lang.Object r12 = r11.get(r12)
            java.lang.String r12 = (java.lang.String) r12
            java.lang.String r15 = "h5Page"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r8 = "UTHybridHelper scm-pre:"
            r3.append(r8)
            r3.append(r12)
            java.lang.String r3 = r3.toString()
            r2[r1] = r3
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r15, (java.lang.Object[]) r2)
            boolean r15 = android.text.TextUtils.isEmpty(r12)
            if (r15 == 0) goto L_0x029e
            java.lang.String r12 = r0.mScmPre
            java.lang.String r15 = "h5Page"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r8 = "UTHybridHelper mScmPre:"
            r3.append(r8)
            r3.append(r12)
            java.lang.String r3 = r3.toString()
            r2[r1] = r3
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r15, (java.lang.Object[]) r2)
        L_0x029e:
            java.lang.String r15 = "utparam-pre"
            java.lang.Object r11 = r11.get(r15)
            java.lang.String r11 = (java.lang.String) r11
            java.lang.String r15 = "h5Page"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r8 = "UTHybridHelper utparam-pre:"
            r3.append(r8)
            r3.append(r11)
            java.lang.String r3 = r3.toString()
            r2[r1] = r3
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r15, (java.lang.Object[]) r2)
            boolean r15 = android.text.TextUtils.isEmpty(r11)
            if (r15 == 0) goto L_0x03b0
            java.lang.String r11 = r0.mUtparamPre
            java.lang.String r15 = "h5Page"
            java.lang.Object[] r0 = new java.lang.Object[r7]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UTHybridHelper mUtparamPre:"
            r2.append(r3)
            r2.append(r11)
            java.lang.String r2 = r2.toString()
            r0[r1] = r2
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r15, (java.lang.Object[]) r0)
            goto L_0x03b0
        L_0x02e4:
            java.lang.String r10 = "spmpre"
            java.lang.Object r10 = r11.get(r10)
            java.lang.String r10 = (java.lang.String) r10
            java.lang.String r12 = "h5Page"
            java.lang.Object[] r15 = new java.lang.Object[r7]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UTHybridHelper _spmpre:"
            r2.append(r3)
            r2.append(r10)
            java.lang.String r2 = r2.toString()
            r15[r1] = r2
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r12, (java.lang.Object[]) r15)
            boolean r12 = android.text.TextUtils.isEmpty(r10)
            if (r12 == 0) goto L_0x0328
            java.lang.String r10 = r0.mSpmPre
            java.lang.String r12 = "h5Page"
            java.lang.Object[] r15 = new java.lang.Object[r7]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UTHybridHelper mSpmPre:"
            r2.append(r3)
            r2.append(r10)
            java.lang.String r2 = r2.toString()
            r15[r1] = r2
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r12, (java.lang.Object[]) r15)
        L_0x0328:
            java.lang.String r12 = "scmpre"
            java.lang.Object r12 = r11.get(r12)
            java.lang.String r12 = (java.lang.String) r12
            java.lang.String r15 = "h5Page"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r8 = "UTHybridHelper _scmpre:"
            r3.append(r8)
            r3.append(r12)
            java.lang.String r3 = r3.toString()
            r2[r1] = r3
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r15, (java.lang.Object[]) r2)
            boolean r15 = android.text.TextUtils.isEmpty(r12)
            if (r15 == 0) goto L_0x036c
            java.lang.String r12 = r0.mScmPre
            java.lang.String r15 = "h5Page"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r8 = "UTHybridHelper mScmPre:"
            r3.append(r8)
            r3.append(r12)
            java.lang.String r3 = r3.toString()
            r2[r1] = r3
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r15, (java.lang.Object[]) r2)
        L_0x036c:
            java.lang.String r15 = "utparampre"
            java.lang.Object r11 = r11.get(r15)
            java.lang.String r11 = (java.lang.String) r11
            java.lang.String r15 = "h5Page"
            java.lang.Object[] r2 = new java.lang.Object[r7]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r8 = "UTHybridHelper _utparampre:"
            r3.append(r8)
            r3.append(r11)
            java.lang.String r3 = r3.toString()
            r2[r1] = r3
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r15, (java.lang.Object[]) r2)
            boolean r15 = android.text.TextUtils.isEmpty(r11)
            if (r15 == 0) goto L_0x03b0
            java.lang.String r11 = r0.mUtparamPre
            java.lang.String r15 = "h5Page"
            java.lang.Object[] r0 = new java.lang.Object[r7]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UTHybridHelper mUtparamPre:"
            r2.append(r3)
            r2.append(r11)
            java.lang.String r2 = r2.toString()
            r0[r1] = r2
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r15, (java.lang.Object[]) r0)
        L_0x03b0:
            java.util.HashMap r15 = new java.util.HashMap
            r15.<init>()
            boolean r0 = android.text.TextUtils.isEmpty(r14)
            if (r0 != 0) goto L_0x03c0
            java.lang.String r0 = "spm-cnt"
            r15.put(r0, r14)
        L_0x03c0:
            boolean r14 = android.text.TextUtils.isEmpty(r4)
            if (r14 != 0) goto L_0x03cb
            java.lang.String r14 = "spm-url"
            r15.put(r14, r4)
        L_0x03cb:
            boolean r14 = android.text.TextUtils.isEmpty(r10)
            if (r14 != 0) goto L_0x03d6
            java.lang.String r14 = "spm-pre"
            r15.put(r14, r10)
        L_0x03d6:
            boolean r10 = android.text.TextUtils.isEmpty(r6)
            if (r10 != 0) goto L_0x03e1
            java.lang.String r10 = "scm"
            r15.put(r10, r6)
        L_0x03e1:
            boolean r10 = android.text.TextUtils.isEmpty(r12)
            if (r10 != 0) goto L_0x03ec
            java.lang.String r10 = "scm-pre"
            r15.put(r10, r12)
        L_0x03ec:
            boolean r10 = android.text.TextUtils.isEmpty(r13)
            if (r10 != 0) goto L_0x03f7
            java.lang.String r10 = "utparam-cnt"
            r15.put(r10, r13)
        L_0x03f7:
            boolean r10 = android.text.TextUtils.isEmpty(r5)
            if (r10 != 0) goto L_0x0402
            java.lang.String r10 = "utparam-url"
            r15.put(r10, r5)
        L_0x0402:
            boolean r10 = android.text.TextUtils.isEmpty(r11)
            if (r10 != 0) goto L_0x040d
            java.lang.String r10 = "utparam-pre"
            r15.put(r10, r11)
        L_0x040d:
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTHybridHelper.getUTPageStateObjectSpmMap(java.lang.Object, java.util.Map, java.lang.String, java.lang.String, java.util.Map, boolean):java.util.Map");
    }

    private String _getPageEventObjectCacheKey(Object obj) {
        String str;
        if (obj instanceof String) {
            str = (String) obj;
        } else {
            str = obj.getClass().getSimpleName();
        }
        int hashCode = obj.hashCode();
        return str + hashCode;
    }
}
