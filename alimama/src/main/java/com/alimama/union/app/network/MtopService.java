package com.alimama.union.app.network;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IMtop;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alimama.moon.BuildConfig;
import com.alimama.moon.network.MtopException;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.util.MtopConvert;

@Singleton
public final class MtopService implements IWebService {
    private final Context appContext;
    private Mtop mtop;

    @Inject
    public MtopService(@Named("appContext") Context context) {
        this.appContext = context;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0049  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initMtopSDK() {
        /*
            r5 = this;
            com.alimama.union.app.configproperties.EnvHelper r0 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r0 = r0.getCurrentApiEnv()
            int r1 = r0.hashCode()
            r2 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r3 = 2
            r4 = 0
            if (r1 == r2) goto L_0x0032
            r2 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r1 == r2) goto L_0x0028
            r2 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r1 == r2) goto L_0x001e
            goto L_0x003c
        L_0x001e:
            java.lang.String r1 = "debug"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            r0 = 2
            goto L_0x003d
        L_0x0028:
            java.lang.String r1 = "prepare"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            r0 = 0
            goto L_0x003d
        L_0x0032:
            java.lang.String r1 = "online"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            r0 = 1
            goto L_0x003d
        L_0x003c:
            r0 = -1
        L_0x003d:
            switch(r0) {
                case 0: goto L_0x0049;
                case 1: goto L_0x0046;
                case 2: goto L_0x0043;
                default: goto L_0x0040;
            }
        L_0x0040:
            mtopsdk.mtop.domain.EnvModeEnum r0 = mtopsdk.mtop.domain.EnvModeEnum.ONLINE
            goto L_0x004b
        L_0x0043:
            mtopsdk.mtop.domain.EnvModeEnum r0 = mtopsdk.mtop.domain.EnvModeEnum.TEST
            goto L_0x004b
        L_0x0046:
            mtopsdk.mtop.domain.EnvModeEnum r0 = mtopsdk.mtop.domain.EnvModeEnum.ONLINE
            goto L_0x004b
        L_0x0049:
            mtopsdk.mtop.domain.EnvModeEnum r0 = mtopsdk.mtop.domain.EnvModeEnum.PREPARE
        L_0x004b:
            java.lang.String r1 = "INNER"
            mtopsdk.mtop.intf.MtopSetting.setAppKeyIndex(r1, r4, r3)
            java.lang.String r1 = "INNER"
            java.lang.String r2 = "7.3.4"
            mtopsdk.mtop.intf.MtopSetting.setAppVersion(r1, r2)
            java.lang.String r1 = "INNER"
            android.content.Context r2 = r5.appContext
            java.lang.String r3 = "10002089@moon_android_7.3.4"
            mtopsdk.mtop.intf.Mtop r1 = mtopsdk.mtop.intf.Mtop.instance(r1, r2, r3)
            r5.mtop = r1
            mtopsdk.mtop.intf.Mtop r1 = r5.mtop
            r1.switchEnvMode(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.network.MtopService.initMtopSDK():void");
    }

    public Mtop getMtop() {
        IMtop iMtop = (IMtop) UNWManager.getInstance().getService(IMtop.class);
        if (iMtop != null) {
            return iMtop.getMtop();
        }
        return null;
    }

    public <T extends BaseOutDo> T get(IMTOPDataObject iMTOPDataObject, Class<T> cls) throws MtopException {
        IMtop iMtop = (IMtop) UNWManager.getInstance().getService(IMtop.class);
        if (iMtop == null) {
            return null;
        }
        MtopResponse syncRequest = iMtop.getMtop().build(iMTOPDataObject, BuildConfig.TTID).reqMethod(MethodEnum.GET).syncRequest();
        if (syncRequest.isApiSuccess()) {
            return (BaseOutDo) MtopConvert.convertJsonToOutputDO(syncRequest.getBytedata(), cls);
        }
        throw new MtopException(syncRequest.getRetCode(), syncRequest.getRetMsg());
    }

    public <T extends BaseOutDo> T get(IMTOPDataObject iMTOPDataObject, Type type) throws MtopException {
        IMtop iMtop = (IMtop) UNWManager.getInstance().getService(IMtop.class);
        if (iMtop == null) {
            return null;
        }
        MtopResponse syncRequest = iMtop.getMtop().build(iMTOPDataObject, BuildConfig.TTID).reqMethod(MethodEnum.GET).syncRequest();
        if (syncRequest.isApiSuccess()) {
            try {
                return (BaseOutDo) JSON.parseObject(syncRequest.getBytedata(), type, new Feature[0]);
            } catch (Exception e) {
                throw new MtopException("JSONException", "JSONException: " + e.getMessage());
            }
        } else {
            throw new MtopException(syncRequest.getRetCode(), syncRequest.getRetMsg());
        }
    }

    public <T extends BaseOutDo> T post(IMTOPDataObject iMTOPDataObject, Class<T> cls) throws MtopException {
        IMtop iMtop = (IMtop) UNWManager.getInstance().getService(IMtop.class);
        if (iMtop == null) {
            return null;
        }
        MtopResponse syncRequest = iMtop.getMtop().build(iMTOPDataObject, BuildConfig.TTID).reqMethod(MethodEnum.POST).syncRequest();
        if (syncRequest.isApiSuccess()) {
            return (BaseOutDo) MtopConvert.convertJsonToOutputDO(syncRequest.getBytedata(), cls);
        }
        throw new MtopException(syncRequest.getRetCode(), syncRequest.getRetMsg());
    }
}
