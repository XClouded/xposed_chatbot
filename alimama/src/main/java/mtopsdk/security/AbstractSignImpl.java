package mtopsdk.security;

import androidx.annotation.NonNull;
import java.util.HashMap;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.global.MtopConfig;

public abstract class AbstractSignImpl implements ISign {
    EnvModeEnum envMode = null;
    MtopConfig mtopConfig = null;

    public String getAvmpSign(String str, String str2, int i) {
        return null;
    }

    public String getMiniWua(HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        return null;
    }

    public String getSecBodyDataEx(String str, String str2, String str3, HashMap<String, String> hashMap, int i) {
        return null;
    }

    public String getSign(HashMap<String, String> hashMap, String str) {
        return null;
    }

    public HashMap<String, String> getUnifiedSign(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, String str, String str2, boolean z) {
        return null;
    }

    public String getWua(HashMap<String, String> hashMap, String str) {
        return null;
    }

    public void init(@NonNull MtopConfig mtopConfig2) {
        this.mtopConfig = mtopConfig2;
        if (this.mtopConfig != null) {
            this.envMode = this.mtopConfig.envMode;
        }
    }

    /* access modifiers changed from: package-private */
    public int getEnv() {
        if (this.envMode == null) {
            return 0;
        }
        switch (this.envMode) {
            case ONLINE:
                return 0;
            case PREPARE:
                return 1;
            case TEST:
            case TEST_SANDBOX:
                return 2;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public String getAuthCode() {
        return this.mtopConfig != null ? this.mtopConfig.authCode : "";
    }

    /* access modifiers changed from: package-private */
    public String getInstanceId() {
        return this.mtopConfig != null ? this.mtopConfig.instanceId : "";
    }
}
