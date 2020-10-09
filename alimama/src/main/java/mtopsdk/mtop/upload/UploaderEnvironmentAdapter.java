package mtopsdk.mtop.upload;

import android.content.Context;
import com.uploader.portal.UploaderEnvironmentImpl;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.global.SDKConfig;
import mtopsdk.xstate.XState;

class UploaderEnvironmentAdapter extends UploaderEnvironmentImpl {
    public UploaderEnvironmentAdapter(Context context) {
        super(context);
        setAuthCode(SDKConfig.getInstance().getGlobalAuthCode());
    }

    public int getEnvironment() {
        EnvModeEnum globalEnvMode = SDKConfig.getInstance().getGlobalEnvMode();
        if (globalEnvMode != null) {
            switch (globalEnvMode) {
                case ONLINE:
                    return 0;
                case PREPARE:
                    return 1;
                case TEST:
                case TEST_SANDBOX:
                    return 2;
            }
        }
        return super.getEnvironment();
    }

    public String getAppKey() {
        return SDKConfig.getInstance().getGlobalAppKey();
    }

    public String getUserId() {
        return XState.getUserId();
    }

    public String getAppVersion() {
        return SDKConfig.getInstance().getGlobalAppVersion();
    }
}
