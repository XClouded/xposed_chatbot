package com.ali.alihadeviceevaluator.network;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.ali.alihadeviceevaluator.util.Base64Utils;
import com.ali.alihadeviceevaluator.util.Global;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.uc.webview.export.media.MessageID;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import org.json.JSONException;
import org.json.JSONObject;

public class RemoteDeviceManager implements IRemoteBaseListener {
    private static final String BIZ_ID_DEVICE_SCORE = "1";
    public static final String TAG = "RemoteDeviceManager";
    private DataCaptureListener dataCaptureListener;

    public interface DataCaptureListener {
        void onFailed();

        void onReceive(float f);
    }

    public RemoteDeviceManager(DataCaptureListener dataCaptureListener2) {
        this.dataCaptureListener = dataCaptureListener2;
    }

    public void fetchData(float f) {
        MtopTaobaoHaQueryRequest mtopTaobaoHaQueryRequest = new MtopTaobaoHaQueryRequest();
        mtopTaobaoHaQueryRequest.setBizid("1");
        mtopTaobaoHaQueryRequest.setDeviceModel(Build.MODEL);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("localScore", String.valueOf(f));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mtopTaobaoHaQueryRequest.setData(Base64Utils.encodeBase64File(jSONObject.toString()));
        MtopBusiness.build(Mtop.instance(Mtop.Id.INNER, (Context) Global.context), (IMTOPDataObject) mtopTaobaoHaQueryRequest).registerListener((IRemoteListener) this).startRequest(MtopTaobaoHaQueryResponse.class);
    }

    public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
        if (baseOutDo != null && (baseOutDo instanceof MtopTaobaoHaQueryResponse)) {
            try {
                this.dataCaptureListener.onReceive(Float.valueOf(((MtopTaobaoHaQueryResponse) baseOutDo).getData().score).floatValue());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void onError(int i, MtopResponse mtopResponse, Object obj) {
        Log.e(TAG, MessageID.onError + i);
        this.dataCaptureListener.onFailed();
    }

    public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
        Log.e(TAG, "onSystemError:" + i);
        this.dataCaptureListener.onFailed();
    }
}
