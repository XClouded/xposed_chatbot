package alimama.com.unwnetwork;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.IMtop;
import alimama.com.unwbase.interfaces.IRxRequestManager;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;
import org.json.JSONObject;

public class RxRequestManager implements IRxRequestManager {
    private MtopRequestManagerListener mListener;
    private Mtop mMtop;
    private String mTtid;

    public void init() {
    }

    public RxRequestManager(MtopRequestManagerListener mtopRequestManagerListener) {
        IMtop iMtop = (IMtop) UNWManager.getInstance().getService(IMtop.class);
        checkIsNull(iMtop, true);
        checkIsNull(iMtop.getMtop(), true);
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        checkIsNull(iAppEnvironment, true);
        this.mMtop = iMtop.getMtop();
        this.mTtid = iAppEnvironment.getTTid();
        this.mListener = mtopRequestManagerListener;
    }

    public RxResponse doSyncHttpRequest(RxHttpRequest rxHttpRequest) {
        if (!rxHttpRequest.isPost()) {
            return doHttpGetRequest(rxHttpRequest);
        }
        return null;
    }

    private RxResponse doHttpGetRequest(RxHttpRequest rxHttpRequest) {
        RxResponse rxResponse = new RxResponse();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(rxHttpRequest.getUrl()).openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            byte[] streamData = getStreamData(new BufferedInputStream(httpURLConnection.getInputStream()));
            rxResponse.isReqSuccess = true;
            rxResponse.oriData = streamData;
            return rxResponse;
        } catch (Exception | MalformedURLException unused) {
            rxResponse.isReqSuccess = false;
            return rxResponse;
        }
    }

    public RxResponse doSyncRequest(RxMtopRequest rxMtopRequest) {
        checkIsNull(this.mMtop, true);
        RxResponse rxResponse = new RxResponse();
        ApiInfo apiInfo = rxMtopRequest.getApiInfo();
        if (apiInfo == null) {
            rxResponse.isReqSuccess = false;
            return rxResponse;
        }
        MtopRequest mtopRequest = new MtopRequest();
        MtopBuilder build = this.mMtop.build(mtopRequest, this.mTtid);
        if (rxMtopRequest.isEnablePost()) {
            build.reqMethod(MethodEnum.POST);
        }
        if (apiInfo.useWua()) {
            build.useWua();
        }
        mtopRequest.setApiName(apiInfo.getAPIName());
        mtopRequest.setVersion(apiInfo.getVersion());
        mtopRequest.setNeedSession(apiInfo.needSession());
        mtopRequest.setNeedEcode(apiInfo.needECode());
        mtopRequest.setData(new JSONObject(rxMtopRequest.getParams()).toString());
        build.retryTime(rxMtopRequest.getRetryTimes());
        build.setConnectionTimeoutMilliSecond(5000);
        build.setSocketTimeoutMilliSecond(5000);
        MtopResponse syncRequest = build.syncRequest();
        String retMsg = syncRequest.getRetMsg();
        if (syncRequest.isApiSuccess()) {
            rxResponse.oriData = syncRequest.getBytedata();
            rxResponse.isReqSuccess = true;
            rxResponse.retCode = syncRequest.getRetCode();
            rxResponse.msg = retMsg;
            if (this.mListener != null) {
                this.mListener.onSuccess(syncRequest);
            }
            return rxResponse;
        }
        if (syncRequest.isSessionInvalid()) {
            if (this.mListener != null) {
                this.mListener.onSessionValid(syncRequest);
            }
        } else if (syncRequest.isNetworkError()) {
            if (this.mListener != null) {
                this.mListener.onNetworkError(syncRequest);
            }
        } else if ((syncRequest.isSystemError() || syncRequest.isExpiredRequest()) && this.mListener != null) {
            this.mListener.onOtherFailed(syncRequest);
        }
        rxResponse.oriData = syncRequest.getBytedata();
        rxResponse.isReqSuccess = false;
        rxResponse.result = "";
        rxResponse.msg = retMsg;
        rxResponse.retCode = syncRequest.getRetCode();
        return rxResponse;
    }

    private byte[] getStreamData(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (Exception unused) {
            return new byte[0];
        }
    }

    private boolean checkIsNull(Object obj, boolean z) {
        boolean z2 = obj == null;
        if (!z2 || !z) {
            return z2;
        }
        throw new IllegalArgumentException(obj.getClass().getSimpleName() + "should not be null");
    }
}
