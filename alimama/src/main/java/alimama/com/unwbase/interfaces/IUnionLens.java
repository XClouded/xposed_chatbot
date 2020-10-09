package alimama.com.unwbase.interfaces;

import java.util.Map;

public interface IUnionLens extends IInitAction {
    String appendNaUnionLens(Map<String, String> map);

    String appendUrlUnionLens(String str);

    String appendUtUnionLens();

    String getRecoveryId();

    String getUrlParameter(String str, String str2);

    boolean isInit();

    boolean isUnionLensReport();

    String replaceUrl(String str, String str2, String str3);

    void setReportSwitch(boolean z);
}
