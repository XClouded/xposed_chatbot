package com.taobao.android.tlog.protocol.model.request;

import alimama.com.unweventparse.constants.EventConstants;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.tlog.protocol.builder.UploadDataBuilder;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.request.base.FileInfo;
import com.taobao.android.tlog.protocol.model.request.base.LogRequestBase;

public class BuilderHelper {
    public static RequestResult buildRequestResult(JSONObject jSONObject, JSONObject jSONObject2, String str, String str2, String str3, String str4) throws Exception {
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("type", (Object) str);
        jSONObject3.put("version", (Object) Constants.version);
        jSONObject3.put(EventConstants.Mtop.HEADERS, (Object) jSONObject2);
        jSONObject3.put("data", (Object) jSONObject);
        String buildLogUploadContent = UploadDataBuilder.buildLogUploadContent(jSONObject3.toString());
        RequestResult requestResult = new RequestResult();
        requestResult.content = buildLogUploadContent;
        requestResult.requestId = str2;
        requestResult.sessionId = str3;
        requestResult.uploadId = str4;
        return requestResult;
    }

    public static JSONArray buildFileInfos(FileInfo[] fileInfoArr) {
        JSONArray jSONArray = new JSONArray();
        for (FileInfo fileInfo : fileInfoArr) {
            JSONObject jSONObject = new JSONObject();
            if (fileInfo.fileName != null) {
                jSONObject.put("fileName", (Object) fileInfo.fileName);
            }
            if (fileInfo.absolutePath != null) {
                jSONObject.put("absolutePath", (Object) fileInfo.absolutePath);
            }
            if (fileInfo.lastModified != null) {
                jSONObject.put("lastModified", (Object) fileInfo.lastModified);
            }
            if (fileInfo.contentLength != null) {
                jSONObject.put("contentLength", (Object) fileInfo.contentLength);
            }
            if (fileInfo.contentType != null) {
                jSONObject.put("contentType", (Object) fileInfo.contentType);
            }
            if (fileInfo.contentMD5 != null) {
                jSONObject.put("contentMD5", (Object) fileInfo.contentMD5);
            }
            if (fileInfo.contentEncoding != null) {
                jSONObject.put("contentEncoding", (Object) fileInfo.contentEncoding);
            }
            jSONArray.add(jSONObject);
        }
        return jSONArray;
    }

    public static JSONObject buildRequestHeader(LogRequestBase logRequestBase, String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(Constants.appKeyName, (Object) logRequestBase.appKey);
        jSONObject.put(Constants.appIdName, (Object) logRequestBase.appId);
        jSONObject.put(Constants.deviceIdName, (Object) logRequestBase.utdid);
        jSONObject.put(Constants.sessionIdName, (Object) str2);
        jSONObject.put(Constants.requestIdName, (Object) str);
        jSONObject.put(Constants.opCodeName, (Object) logRequestBase.opCode);
        return jSONObject;
    }
}
