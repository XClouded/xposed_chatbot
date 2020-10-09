package com.alibaba.aliweex.plugin;

import android.text.TextUtils;
import androidx.core.util.Pair;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpmMonitor {
    private static final String default_ab = "0.0";
    private static final String s_AUTO_SPMD = "dataAutoSpmd";
    private static final String s_AUTO_SPMD_MAX_IDX = "dataAutoSpmdMaxIdx";
    private static final String s_DATA_SPM_ANCHOR_ID = "dataSpmAnchorId";
    private static final String s_SPM_ATTR_NAME = "dataSpm";
    private static final String s_SPM_ID_ATTR_NAME = "spmId";
    private static final String s_SPM_MAX_IDX = "dataSpmMaxIdx";
    private String spm_id = "";

    public SpmMonitor(WXComponent wXComponent) {
        if (wXComponent != null) {
            this.spm_id = tryToGetAttribute(wXComponent, s_SPM_ID_ATTR_NAME);
            if (TextUtils.isEmpty(this.spm_id) || !Pattern.matches("^[\\w\\-\\.\\/]+$", this.spm_id)) {
                this.spm_id = "0.0";
            }
        }
    }

    public void doTrace(WXComponent wXComponent) {
        while (wXComponent != null) {
            String tagName = tagName(wXComponent);
            if (TextUtils.isEmpty(tagName)) {
                return;
            }
            if ("a".equals(tagName) || "A".equals(tagName) || "AREA".equals(tagName)) {
                spmSpmAnchorChk(wXComponent, false);
                return;
            } else if (!WXComponent.ROOT.equals(tagName)) {
                wXComponent = wXComponent.getParent();
            } else {
                return;
            }
        }
    }

    private void spmSpmAnchorChk(WXComponent wXComponent, boolean z) {
        String tryToGetAttribute = tryToGetAttribute(wXComponent, s_DATA_SPM_ANCHOR_ID);
        if (TextUtils.isEmpty(tryToGetAttribute) || !spmIsSPMAnchorIdMatch(tryToGetAttribute)) {
            Pair<String, WXComponent> spmSpmGetParentSPMId = spmSpmGetParentSPMId(wXComponent.getParent());
            String str = (String) spmSpmGetParentSPMId.first;
            if (TextUtils.isEmpty(str)) {
                spmDealNoneSPMLink(wXComponent, z);
                return;
            }
            spmInitSPMModule((WXComponent) spmSpmGetParentSPMId.second, str, z, false);
            spmInitSPMModule((WXComponent) spmSpmGetParentSPMId.second, str, z, true);
            return;
        }
        spmAnchorEnsureSPMIdInHref(wXComponent, tryToGetAttribute, z);
    }

    private void spmDealNoneSPMLink(WXComponent wXComponent, boolean z) {
        String str = this.spm_id;
        String tryToGetHref = tryToGetHref(wXComponent);
        String spmGetSPMIdFromHref = spmGetSPMIdFromHref(tryToGetHref);
        int i = 0;
        if (!TextUtils.isEmpty(str) && str.split("\\.").length == 2) {
            String[] strArr = new String[3];
            strArr[0] = str;
            strArr[1] = "0";
            strArr[2] = TextUtils.isEmpty(spmGetAnchor4thIdSpmD(wXComponent)) ? "0" : spmGetAnchor4thIdSpmD(wXComponent);
            String str2 = "";
            while (i < strArr.length) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(strArr[i]);
                sb.append(i == strArr.length - 1 ? "" : ".");
                str2 = sb.toString();
                i++;
            }
            spmAnchorEnsureSPMIdInHref(wXComponent, str2, z);
        } else if (!TextUtils.isEmpty(tryToGetHref) && !TextUtils.isEmpty(spmGetSPMIdFromHref)) {
            spmWriteHref(wXComponent, tryToGetHref.replaceAll("&?\\bspm=[^&#]*", "").replaceAll("&{2,}", "&").replaceFirst("\\?&", "?").replaceFirst("\\?$", "").replaceFirst("\\?#", "#"));
        }
    }

    private void spmInitSPMModule(WXComponent wXComponent, String str, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str)) {
            str = tryToGetAttribute(wXComponent, s_SPM_ATTR_NAME);
        }
        if (!TextUtils.isEmpty(str)) {
            ArrayList<WXComponent> spmGetModuleLinks = spmGetModuleLinks(wXComponent, z2);
            if (spmGetModuleLinks.size() != 0) {
                String str2 = this.spm_id;
                if (Pattern.matches("^[\\w\\-\\*]+(\\.[\\w\\-\\*\\/]+)?$", str2)) {
                    if (!str.contains(".")) {
                        if (!str2.contains(".")) {
                            str2 = str2 + ".0";
                        }
                        str = str2 + '.' + str;
                    } else if (!str.startsWith(str2)) {
                        String[] split = str2.split("\\.");
                        String[] split2 = str.split("\\.");
                        int length = split.length;
                        for (int i = 0; i < length; i++) {
                            split2[i] = split[i];
                        }
                        String str3 = str;
                        int i2 = 0;
                        while (i2 < split2.length) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str3);
                            sb.append(split2[i2]);
                            sb.append(i2 == split2.length + -1 ? "" : ".");
                            str3 = sb.toString();
                            i2++;
                        }
                        str = str3;
                    }
                }
                if (Pattern.matches("^[\\w\\-\\*]+\\.[\\w\\-\\*\\/]+\\.[\\w\\-\\*\\/]+$", str)) {
                    String str4 = z2 ? s_AUTO_SPMD_MAX_IDX : s_SPM_MAX_IDX;
                    int parseInt = parseInt(tryToGetAttribute(wXComponent, str4));
                    int size = spmGetModuleLinks.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        WXComponent wXComponent2 = spmGetModuleLinks.get(i3);
                        String tryToGetHref = tryToGetHref(wXComponent2);
                        if (z2 || !TextUtils.isEmpty(tryToGetHref)) {
                            String tryToGetAttribute = tryToGetAttribute(wXComponent2, s_DATA_SPM_ANCHOR_ID);
                            if (TextUtils.isEmpty(tryToGetAttribute) || !spmIsSPMAnchorIdMatch(tryToGetAttribute)) {
                                parseInt++;
                                String spmGetAnchor4thIdSpmD = spmGetAnchor4thIdSpmD(wXComponent2);
                                if (TextUtils.isEmpty(spmGetAnchor4thIdSpmD)) {
                                    spmGetAnchor4thIdSpmD = String.valueOf(parseInt);
                                }
                                if (z2) {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("at");
                                    sb2.append(isNumber(spmGetAnchor4thIdSpmD) ? 1000 : "");
                                    sb2.append(spmGetAnchor4thIdSpmD);
                                    spmGetAnchor4thIdSpmD = sb2.toString();
                                }
                                spmAnchorEnsureSPMIdInHref(wXComponent2, str + '.' + spmGetAnchor4thIdSpmD, z);
                            } else {
                                spmAnchorEnsureSPMIdInHref(wXComponent2, tryToGetAttribute, z);
                            }
                        }
                    }
                    setAttribute(wXComponent, str4, String.valueOf(parseInt));
                }
            }
        }
    }

    private ArrayList<WXComponent> spmGetModuleLinks(WXComponent wXComponent, boolean z) {
        boolean z2;
        ArrayList<WXComponent> arrayList = new ArrayList<>();
        ArrayList<WXComponent> elementsByTagName = getElementsByTagName(wXComponent, "a");
        int size = elementsByTagName.size();
        for (int i = 0; i < size; i++) {
            WXComponent wXComponent2 = elementsByTagName.get(i);
            WXComponent wXComponent3 = wXComponent2;
            while (true) {
                wXComponent3 = wXComponent3.getParent();
                if (wXComponent3 != null && wXComponent3 != wXComponent) {
                    if (!TextUtils.isEmpty(tryToGetAttribute(wXComponent3, s_SPM_ATTR_NAME))) {
                        z2 = true;
                        break;
                    }
                } else {
                    z2 = false;
                }
            }
            z2 = false;
            if (!z2) {
                String tryToGetAttribute = tryToGetAttribute(wXComponent2, s_AUTO_SPMD);
                if (!z && !"t".equals(tryToGetAttribute)) {
                    arrayList.add(wXComponent2);
                } else if (z && "t".equals(tryToGetAttribute)) {
                    arrayList.add(wXComponent2);
                }
            }
        }
        return arrayList;
    }

    private Pair<String, WXComponent> spmSpmGetParentSPMId(WXComponent wXComponent) {
        String str = "";
        WXComponent wXComponent2 = wXComponent;
        while (true) {
            if (wXComponent2 == null || WXComponent.ROOT.equals(wXComponent2.getRef())) {
                break;
            }
            String tryToGetAttribute = tryToGetAttribute(wXComponent2, s_SPM_ATTR_NAME);
            if (TextUtils.isEmpty(tryToGetAttribute)) {
                wXComponent2 = wXComponent2.getParent();
                if (wXComponent2 == null) {
                    break;
                }
            } else {
                wXComponent = wXComponent2;
                str = tryToGetAttribute;
                break;
            }
        }
        if (!TextUtils.isEmpty(str) && !Pattern.matches("^[\\w\\-\\.\\/]+$", str)) {
            str = "0";
        }
        return new Pair<>(str, wXComponent);
    }

    private String tryToGetHref(WXComponent wXComponent) {
        try {
            return tryToGetAttribute(wXComponent, "href").trim();
        } catch (Exception unused) {
            return "";
        }
    }

    private String spmGetSPMIdFromHref(String str) {
        Matcher matcher = Pattern.compile("&?\\bspm=([^&#]*)").matcher(str);
        if (!matcher.find() || matcher.groupCount() <= 0) {
            return "";
        }
        return matcher.group(1);
    }

    private String spmGetAnchor4thIdSpmD(WXComponent wXComponent) {
        Pair<String, String> sPMData = getSPMData();
        if (sPMData.first.equals("0") && sPMData.second.equals("0")) {
            return "0";
        }
        String tryToGetAttribute = tryToGetAttribute(wXComponent, s_SPM_ATTR_NAME);
        return (TextUtils.isEmpty(tryToGetAttribute) || !Pattern.matches("^d\\w+$", tryToGetAttribute)) ? "" : tryToGetAttribute;
    }

    private void spmAnchorEnsureSPMIdInHref(WXComponent wXComponent, String str, boolean z) {
        String str2 = this.spm_id;
        setAttribute(wXComponent, s_DATA_SPM_ANCHOR_ID, str);
        if (!TextUtils.isEmpty(str2) && !"0.0".equals(str2)) {
            String tryToGetHref = tryToGetHref(wXComponent);
            if (!z) {
                spmWriteHref(wXComponent, spmUpdateHrefWithSPMId(tryToGetHref, str));
            }
        }
    }

    private String spmUpdateHrefWithSPMId(String str, String str2) {
        String str3;
        String str4;
        if (TextUtils.isEmpty(str) || str.startsWith("tel:")) {
            return str;
        }
        Matcher matcher = Pattern.compile("&?\\bspm=[^&#]*").matcher(str);
        if (!TextUtils.isEmpty(str) && matcher.find()) {
            str = str.replaceAll("&?\\bspm=[^&#]*", "").replaceAll("&{2,}", "&").replaceFirst("\\?&", "?").replaceFirst("\\?$", "");
        }
        if (TextUtils.isEmpty(str2)) {
            return str;
        }
        String str5 = "";
        String str6 = "";
        String str7 = "&";
        int i = 0;
        if (str.contains("#")) {
            String[] split = str.split("#");
            if (split.length > 0) {
                str = split[0];
            }
            String str8 = str6;
            int i2 = 1;
            while (i2 < split.length) {
                StringBuilder sb = new StringBuilder();
                sb.append(str8);
                sb.append(split[i2]);
                sb.append(i2 == split.length - 1 ? "" : "#");
                str8 = sb.toString();
                i2++;
            }
            str6 = str8;
        }
        String[] split2 = r10.split("\\?");
        int length = split2.length - 1;
        String[] split3 = split2[0].split("//");
        split3[split3.length - 1].split("/");
        if (length > 0 && split2.length > 0) {
            str5 = split2[split2.length - 1];
            r10 = "";
            while (i < split2.length - 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(r10);
                sb2.append(split2[i]);
                sb2.append(i == (split2.length - 1) - 1 ? "" : "?");
                r10 = sb2.toString();
                i++;
            }
        }
        if (!TextUtils.isEmpty(str5) && length > 1 && str5.indexOf(38) == -1 && str5.indexOf(37) != -1) {
            str7 = "%26";
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(r10);
        sb3.append("?spm=");
        sb3.append("");
        sb3.append(str2);
        if (!TextUtils.isEmpty(str5)) {
            str3 = str7 + str5;
        } else {
            str3 = "";
        }
        sb3.append(str3);
        if (!TextUtils.isEmpty(str6)) {
            str4 = "#" + str6;
        } else {
            str4 = "";
        }
        sb3.append(str4);
        return sb3.toString();
    }

    private void spmWriteHref(WXComponent wXComponent, String str) {
        setAttribute(wXComponent, "href", str);
    }

    private String getGlobalSPMId(WXComponent wXComponent) {
        if (!TextUtils.isEmpty(this.spm_id)) {
            return this.spm_id;
        }
        String tryToGetAttribute = tryToGetAttribute(wXComponent, s_SPM_ID_ATTR_NAME);
        while (true) {
            if (wXComponent == null) {
                break;
            }
            String tryToGetAttribute2 = tryToGetAttribute(wXComponent, s_SPM_ID_ATTR_NAME);
            if (!WXComponent.ROOT.equals(wXComponent.getRef())) {
                wXComponent = wXComponent.getParent();
                if (wXComponent == null) {
                    break;
                }
            } else {
                tryToGetAttribute = tryToGetAttribute2;
                break;
            }
        }
        return (TextUtils.isEmpty(tryToGetAttribute) || Pattern.matches("^[\\w\\-\\.\\/]+$", tryToGetAttribute)) ? tryToGetAttribute : "0.0";
    }

    private Pair<String, String> getSPMData() {
        String[] split = this.spm_id.split("\\.");
        return split.length == 2 ? new Pair<>(split[0], split[1]) : new Pair<>("0", "0");
    }

    private boolean spmIsSPMAnchorIdMatch(String str) {
        String str2 = this.spm_id;
        String[] split = str.split("\\.");
        String str3 = "";
        String str4 = "";
        if (split.length > 1) {
            str3 = split[0];
            str4 = split[1];
        }
        return (str3 + '.' + str4).equals(str2);
    }

    private ArrayList<WXComponent> getElementsByTagName(WXComponent wXComponent, String str) {
        ArrayList<WXComponent> arrayList = new ArrayList<>();
        if (wXComponent instanceof WXVContainer) {
            WXVContainer wXVContainer = (WXVContainer) wXComponent;
            if (wXVContainer.getChildCount() > 0) {
                for (int i = 0; i < wXVContainer.getChildCount(); i++) {
                    WXComponent child = wXVContainer.getChild(i);
                    if (str.equals(child.getComponentType())) {
                        arrayList.add(child);
                    }
                    ArrayList<WXComponent> elementsByTagName = getElementsByTagName(child, str);
                    if (elementsByTagName.size() > 0) {
                        arrayList.addAll(elementsByTagName);
                    }
                }
            }
        }
        return arrayList;
    }

    private String tagName(WXComponent wXComponent) {
        return wXComponent.getComponentType();
    }

    private String tryToGetAttribute(WXComponent wXComponent, String str) {
        WXAttr attrs = wXComponent.getAttrs();
        if (attrs == null) {
            return null;
        }
        Object obj = attrs.get(str);
        if (obj instanceof String) {
            return (String) obj;
        }
        return null;
    }

    private void setAttribute(WXComponent wXComponent, String str, String str2) {
        wXComponent.getAttrs().put(str, (Object) str2);
    }

    private int parseInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean isNumber(String str) {
        int i;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            i = -1;
        }
        if (i >= 0) {
            return true;
        }
        return false;
    }
}
