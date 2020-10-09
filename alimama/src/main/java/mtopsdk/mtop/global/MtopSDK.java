package mtopsdk.mtop.global;

import android.content.Context;
import mtopsdk.mtop.intf.Mtop;

@Deprecated
public class MtopSDK {
    @Deprecated
    public static void checkMtopSDKInit() {
        Mtop.instance((Context) null).checkMtopSDKInit();
    }

    @Deprecated
    public static void setLogSwitch(boolean z) {
        Mtop.instance((Context) null).logSwitch(z);
    }
}
