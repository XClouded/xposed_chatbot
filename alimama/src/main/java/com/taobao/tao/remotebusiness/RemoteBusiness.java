package com.taobao.tao.remotebusiness;

import android.content.Context;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.intf.Mtop;

@Deprecated
public class RemoteBusiness extends MtopBusiness {
    protected RemoteBusiness(Mtop mtop, IMTOPDataObject iMTOPDataObject, String str) {
        super(mtop, iMTOPDataObject, str);
    }

    protected RemoteBusiness(Mtop mtop, MtopRequest mtopRequest, String str) {
        super(mtop, mtopRequest, str);
    }

    @Deprecated
    public static void init(Context context, String str) {
        Mtop.instance(context, str);
    }

    public static RemoteBusiness build(IMTOPDataObject iMTOPDataObject, String str) {
        return new RemoteBusiness(Mtop.instance((Context) null, str), iMTOPDataObject, str);
    }

    public static RemoteBusiness build(IMTOPDataObject iMTOPDataObject) {
        return build(iMTOPDataObject, (String) null);
    }

    public static RemoteBusiness build(MtopRequest mtopRequest, String str) {
        return new RemoteBusiness(Mtop.instance((Context) null, str), mtopRequest, str);
    }

    public static RemoteBusiness build(MtopRequest mtopRequest) {
        return build(mtopRequest, (String) null);
    }

    @Deprecated
    public static RemoteBusiness build(Context context, IMTOPDataObject iMTOPDataObject, String str) {
        init(context, str);
        return build(iMTOPDataObject, str);
    }

    @Deprecated
    public static RemoteBusiness build(Context context, MtopRequest mtopRequest, String str) {
        init(context, str);
        return build(mtopRequest, str);
    }

    @Deprecated
    public RemoteBusiness setErrorNotifyAfterCache(boolean z) {
        return (RemoteBusiness) super.setErrorNotifyAfterCache(z);
    }

    @Deprecated
    public RemoteBusiness registeListener(MtopListener mtopListener) {
        return (RemoteBusiness) super.registerListener(mtopListener);
    }

    @Deprecated
    public RemoteBusiness registeListener(IRemoteListener iRemoteListener) {
        return (RemoteBusiness) super.registerListener(iRemoteListener);
    }

    public RemoteBusiness retryTime(int i) {
        return (RemoteBusiness) super.retryTime(i);
    }

    @Deprecated
    public RemoteBusiness showLoginUI(boolean z) {
        return (RemoteBusiness) super.showLoginUI(z);
    }

    @Deprecated
    public RemoteBusiness setBizId(int i) {
        return (RemoteBusiness) super.setBizId(i);
    }

    @Deprecated
    public RemoteBusiness addListener(MtopListener mtopListener) {
        return (RemoteBusiness) super.addListener(mtopListener);
    }

    @Deprecated
    public void setErrorNotifyNeedAfterCache(boolean z) {
        super.setErrorNotifyAfterCache(z);
    }

    @Deprecated
    public RemoteBusiness reqContext(Object obj) {
        return (RemoteBusiness) super.reqContext(obj);
    }
}
