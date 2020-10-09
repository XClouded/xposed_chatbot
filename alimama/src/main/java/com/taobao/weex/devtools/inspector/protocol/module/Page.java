package com.taobao.weex.devtools.inspector.protocol.module;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.taobao.weex.devtools.common.ProcessUtil;
import com.taobao.weex.devtools.inspector.domstorage.SharedPreferencesHelper;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcException;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcPeer;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcResult;
import com.taobao.weex.devtools.inspector.jsonrpc.PendingRequestCallback;
import com.taobao.weex.devtools.inspector.jsonrpc.protocol.JsonRpcError;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsDomain;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsMethod;
import com.taobao.weex.devtools.inspector.protocol.module.Console;
import com.taobao.weex.devtools.inspector.screencast.ScreencastDispatcher;
import com.taobao.weex.devtools.json.ObjectMapper;
import com.taobao.weex.devtools.json.annotation.JsonProperty;
import com.taobao.weex.devtools.json.annotation.JsonValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class Page implements ChromeDevtoolsDomain {
    private final Context mContext;
    private final ObjectMapper mObjectMapper = new ObjectMapper();
    @Nullable
    private ScreencastDispatcher mScreencastDispatcher;

    public static class ScreencastFrameEvent {
        @JsonProperty(required = true)
        public String data;
        @JsonProperty(required = true)
        public ScreencastFrameEventMetadata metadata;
    }

    public static class ScreencastFrameEventMetadata {
        @JsonProperty(required = true)
        public int deviceHeight;
        @JsonProperty(required = true)
        public int deviceWidth;
        @JsonProperty(required = true)
        public int offsetTop;
        @JsonProperty(required = true)
        public float pageScaleFactor;
        @JsonProperty(required = true)
        public int scrollOffsetX;
        @JsonProperty(required = true)
        public int scrollOffsetY;
    }

    public static class StartScreencastRequest {
        @JsonProperty
        public String format;
        @JsonProperty
        public int maxHeight;
        @JsonProperty
        public int maxWidth;
        @JsonProperty
        public int quality;
    }

    @ChromeDevtoolsMethod
    public void clearDeviceOrientationOverride(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    @ChromeDevtoolsMethod
    public void clearGeolocationOverride(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    @ChromeDevtoolsMethod
    public void disable(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    @ChromeDevtoolsMethod
    public void screencastFrameAck(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    @ChromeDevtoolsMethod
    public void setDeviceMetricsOverride(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    @ChromeDevtoolsMethod
    public void setEmulatedMedia(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    @ChromeDevtoolsMethod
    public void setShowViewportSizeOnResize(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    @ChromeDevtoolsMethod
    public void setTouchEmulationEnabled(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    public Page(Context context) {
        this.mContext = context;
    }

    @ChromeDevtoolsMethod
    public void enable(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        notifyExecutionContexts(jsonRpcPeer);
        sendWelcomeMessage(jsonRpcPeer);
    }

    private void notifyExecutionContexts(JsonRpcPeer jsonRpcPeer) {
        ExecutionContextDescription executionContextDescription = new ExecutionContextDescription();
        executionContextDescription.frameId = "1";
        executionContextDescription.id = 1;
        ExecutionContextCreatedParams executionContextCreatedParams = new ExecutionContextCreatedParams();
        executionContextCreatedParams.context = executionContextDescription;
        jsonRpcPeer.invokeMethod("Runtime.executionContextCreated", executionContextCreatedParams, (PendingRequestCallback) null);
    }

    private void sendWelcomeMessage(JsonRpcPeer jsonRpcPeer) {
        Console.ConsoleMessage consoleMessage = new Console.ConsoleMessage();
        consoleMessage.source = Console.MessageSource.JAVASCRIPT;
        consoleMessage.level = Console.MessageLevel.LOG;
        consoleMessage.text = "                                  _               _                 _      \n                                 | |             | |               | |     \n __      __ ___   ___ __  __   __| |  ___ __   __| |_  ___    ___  | | ___ \n \\ \\ /\\ / // _ \\ / _ \\\\ \\/ /  / _` | / _ \\\\ \\ / /| __|/ _ \\  / _ \\ | |/ __|\n  \\ V  V /|  __/|  __/ >  <  | (_| ||  __/ \\ V / | |_| (_) || (_) || |\\__ \\\n   \\_/\\_/  \\___| \\___|/_/\\_\\  \\__,_| \\___|  \\_/   \\__|\\___/  \\___/ |_||___/\n                                                                           \n                                                                           \n   Welcome to WEEX! Attached to " + ProcessUtil.getProcessName() + "\n";
        Console.MessageAddedRequest messageAddedRequest = new Console.MessageAddedRequest();
        messageAddedRequest.message = consoleMessage;
        jsonRpcPeer.invokeMethod("Console.messageAdded", messageAddedRequest, (PendingRequestCallback) null);
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult getResourceContent(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) throws JsonRpcException {
        try {
            String string = jSONObject.getString("frameId");
            jSONObject.getString("url");
            return readResourceContent(string, "1");
        } catch (IOException e) {
            throw new JsonRpcException(new JsonRpcError(JsonRpcError.ErrorCode.INTERNAL_ERROR, e.toString(), (JSONObject) null));
        } catch (JSONException e2) {
            throw new JsonRpcException(new JsonRpcError(JsonRpcError.ErrorCode.INTERNAL_ERROR, e2.toString(), (JSONObject) null));
        }
    }

    private getResourceContentResponse readResourceContent(String str, String str2) throws IOException, JsonRpcException {
        return new getResourceContentResponse();
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult getResourceTree(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        Iterator<String> it = SharedPreferencesHelper.getSharedPreferenceTags(this.mContext).iterator();
        FrameResourceTree createSimpleFrameResourceTree = createSimpleFrameResourceTree("1", (String) null, UMLLCons.FEATURE_TYPE_WEEX, it.hasNext() ? it.next() : "");
        if (createSimpleFrameResourceTree.childFrames == null) {
            createSimpleFrameResourceTree.childFrames = new ArrayList();
        }
        int i = 1;
        while (it.hasNext()) {
            StringBuilder sb = new StringBuilder();
            sb.append("1.");
            int i2 = i + 1;
            sb.append(i);
            String sb2 = sb.toString();
            createSimpleFrameResourceTree.childFrames.add(createSimpleFrameResourceTree(sb2, "1", "Child #" + sb2, it.next()));
            i = i2;
        }
        GetResourceTreeParams getResourceTreeParams = new GetResourceTreeParams();
        getResourceTreeParams.frameTree = createSimpleFrameResourceTree;
        return getResourceTreeParams;
    }

    private static FrameResourceTree createSimpleFrameResourceTree(String str, String str2, String str3, String str4) {
        Frame frame = new Frame();
        frame.id = str;
        frame.parentId = str2;
        frame.loaderId = "1";
        frame.name = str3;
        frame.url = "";
        frame.securityOrigin = str4;
        frame.mimeType = "text/plain";
        FrameResourceTree frameResourceTree = new FrameResourceTree();
        frameResourceTree.frame = frame;
        frameResourceTree.resources = Collections.emptyList();
        frameResourceTree.childFrames = null;
        return frameResourceTree;
    }

    private static FrameResourceTree createMockFrameResourceTree(String str, String str2, String str3, String str4) {
        Frame frame = new Frame();
        frame.id = str;
        frame.parentId = str2;
        frame.loaderId = "2";
        frame.name = str3;
        frame.url = "http://www.sina.com";
        frame.securityOrigin = str4;
        frame.mimeType = "text/plain";
        FrameResourceTree frameResourceTree = new FrameResourceTree();
        frameResourceTree.frame = frame;
        frameResourceTree.resources = createMockResource();
        frameResourceTree.childFrames = null;
        Log.v("FrameResourceTree", "frame : , name : " + frame.name + ", mimeType : " + frame.mimeType);
        return frameResourceTree;
    }

    private static List<Resource> createMockResource() {
        ArrayList arrayList = new ArrayList();
        Resource resource = new Resource();
        resource.url = "http://apod.nasa.gov/apod/ap160601.html";
        resource.type = ResourceType.DOCUMENT;
        resource.mimeType = "text/HTML";
        Resource resource2 = new Resource();
        resource2.url = "http://30.10.216.161:12580/examples/build/hello.js";
        resource2.type = ResourceType.SCRIPT;
        resource2.mimeType = "text/javascript";
        arrayList.add(resource);
        arrayList.add(resource2);
        return arrayList;
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult canScreencast(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        return new SimpleBooleanResult(true);
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult hasTouchInputs(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        return new SimpleBooleanResult(false);
    }

    @ChromeDevtoolsMethod
    public void startScreencast(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        StartScreencastRequest startScreencastRequest = (StartScreencastRequest) this.mObjectMapper.convertValue(jSONObject, StartScreencastRequest.class);
        if (this.mScreencastDispatcher == null) {
            this.mScreencastDispatcher = new ScreencastDispatcher();
            this.mScreencastDispatcher.startScreencast(jsonRpcPeer, startScreencastRequest);
        }
    }

    @ChromeDevtoolsMethod
    public void stopScreencast(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        if (this.mScreencastDispatcher != null) {
            this.mScreencastDispatcher.stopScreencast();
            this.mScreencastDispatcher = null;
        }
    }

    private static class getResourceContentResponse implements JsonRpcResult {
        @JsonProperty(required = true)
        public boolean base64Encoded;
        @JsonProperty(required = true)
        public String content;

        private getResourceContentResponse() {
        }
    }

    private static class GetResourceTreeParams implements JsonRpcResult {
        @JsonProperty(required = true)
        public FrameResourceTree frameTree;

        private GetResourceTreeParams() {
        }
    }

    private static class FrameResourceTree {
        @JsonProperty
        public List<FrameResourceTree> childFrames;
        @JsonProperty(required = true)
        public Frame frame;
        @JsonProperty(required = true)
        public List<Resource> resources;

        private FrameResourceTree() {
        }
    }

    private static class Frame {
        @JsonProperty(required = true)
        public String id;
        @JsonProperty(required = true)
        public String loaderId;
        @JsonProperty(required = true)
        public String mimeType;
        @JsonProperty
        public String name;
        @JsonProperty
        public String parentId;
        @JsonProperty(required = true)
        public String securityOrigin;
        @JsonProperty(required = true)
        public String url;

        private Frame() {
        }
    }

    private static class Resource {
        @JsonProperty
        public boolean canceled;
        @JsonProperty
        public boolean failed;
        @JsonProperty(required = true)
        public String mimeType;
        @JsonProperty(required = true)
        public ResourceType type;
        @JsonProperty(required = true)
        public String url;

        private Resource() {
        }
    }

    public enum ResourceType {
        DOCUMENT("Document"),
        STYLESHEET("Stylesheet"),
        IMAGE("Image"),
        FONT("Font"),
        SCRIPT("Script"),
        XHR("XHR"),
        WEBSOCKET("WebSocket"),
        OTHER("Other");
        
        private final String mProtocolValue;

        private ResourceType(String str) {
            this.mProtocolValue = str;
        }

        @JsonValue
        public String getProtocolValue() {
            return this.mProtocolValue;
        }
    }

    private static class ExecutionContextCreatedParams {
        @JsonProperty(required = true)
        public ExecutionContextDescription context;

        private ExecutionContextCreatedParams() {
        }
    }

    private static class ExecutionContextDescription {
        @JsonProperty(required = true)
        public String frameId;
        @JsonProperty(required = true)
        public int id;

        private ExecutionContextDescription() {
        }
    }
}
