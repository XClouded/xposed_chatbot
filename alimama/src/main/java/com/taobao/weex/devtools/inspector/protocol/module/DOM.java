package com.taobao.weex.devtools.inspector.protocol.module;

import android.graphics.Color;
import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.common.ArrayListAccumulator;
import com.taobao.weex.devtools.common.LogUtil;
import com.taobao.weex.devtools.common.UncheckedCallable;
import com.taobao.weex.devtools.common.Util;
import com.taobao.weex.devtools.inspector.elements.Document;
import com.taobao.weex.devtools.inspector.elements.DocumentView;
import com.taobao.weex.devtools.inspector.elements.ElementInfo;
import com.taobao.weex.devtools.inspector.elements.NodeDescriptor;
import com.taobao.weex.devtools.inspector.elements.NodeType;
import com.taobao.weex.devtools.inspector.elements.StyleAccumulator;
import com.taobao.weex.devtools.inspector.helper.ChromePeerManager;
import com.taobao.weex.devtools.inspector.helper.PeersRegisteredListener;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcException;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcPeer;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcResult;
import com.taobao.weex.devtools.inspector.jsonrpc.protocol.JsonRpcError;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsDomain;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsMethod;
import com.taobao.weex.devtools.inspector.protocol.module.Runtime;
import com.taobao.weex.devtools.json.ObjectMapper;
import com.taobao.weex.devtools.json.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import org.json.JSONObject;

public class DOM implements ChromeDevtoolsDomain {
    private static boolean sNativeMode = true;
    private ChildNodeInsertedEvent mCachedChildNodeInsertedEvent;
    private ChildNodeRemovedEvent mCachedChildNodeRemovedEvent;
    /* access modifiers changed from: private */
    public final Document mDocument;
    /* access modifiers changed from: private */
    public final DocumentUpdateListener mListener;
    private final ObjectMapper mObjectMapper = new ObjectMapper();
    /* access modifiers changed from: private */
    public final ChromePeerManager mPeerManager;
    private final AtomicInteger mResultCounter;
    /* access modifiers changed from: private */
    public final Map<String, List<Integer>> mSearchResults;

    public static final class GetBoxModelResponse implements JsonRpcResult {
        @JsonProperty(required = true)
        public BoxModel model;
    }

    public DOM(Document document) {
        this.mDocument = (Document) Util.throwIfNull(document);
        this.mSearchResults = Collections.synchronizedMap(new HashMap());
        this.mResultCounter = new AtomicInteger(0);
        this.mPeerManager = new ChromePeerManager();
        this.mPeerManager.setListener(new PeerManagerListener());
        this.mListener = new DocumentUpdateListener();
    }

    public static void setNativeMode(boolean z) {
        sNativeMode = z;
    }

    public static boolean isNativeMode() {
        return sNativeMode;
    }

    @ChromeDevtoolsMethod
    public void enable(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        this.mPeerManager.addPeer(jsonRpcPeer);
    }

    @ChromeDevtoolsMethod
    public void disable(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        this.mPeerManager.removePeer(jsonRpcPeer);
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult getDocument(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        GetDocumentResponse getDocumentResponse = new GetDocumentResponse();
        getDocumentResponse.root = (Node) this.mDocument.postAndWait(new UncheckedCallable<Node>() {
            public Node call() {
                return DOM.this.createNodeForElement(DOM.this.mDocument.getRootElement(), DOM.this.mDocument.getDocumentView(), (Accumulator<Object>) null);
            }
        });
        return getDocumentResponse;
    }

    @ChromeDevtoolsMethod
    public void highlightNode(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        final HighlightNodeRequest highlightNodeRequest = (HighlightNodeRequest) this.mObjectMapper.convertValue(jSONObject, HighlightNodeRequest.class);
        if (highlightNodeRequest.nodeId == null) {
            LogUtil.w("DOM.highlightNode was not given a nodeId; JS objectId is not supported");
            return;
        }
        final RGBAColor rGBAColor = highlightNodeRequest.highlightConfig.contentColor;
        if (rGBAColor == null) {
            LogUtil.w("DOM.highlightNode was not given a color to highlight with");
        } else {
            this.mDocument.postAndWait((Runnable) new Runnable() {
                public void run() {
                    Object elementForNodeId = DOM.this.mDocument.getElementForNodeId(highlightNodeRequest.nodeId.intValue());
                    if (elementForNodeId != null) {
                        DOM.this.mDocument.highlightElement(elementForNodeId, rGBAColor.getColor());
                    }
                }
            });
        }
    }

    @ChromeDevtoolsMethod
    public void hideHighlight(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        this.mDocument.postAndWait((Runnable) new Runnable() {
            public void run() {
                DOM.this.mDocument.hideHighlight();
            }
        });
    }

    @ChromeDevtoolsMethod
    public ResolveNodeResponse resolveNode(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) throws JsonRpcException {
        final ResolveNodeRequest resolveNodeRequest = (ResolveNodeRequest) this.mObjectMapper.convertValue(jSONObject, ResolveNodeRequest.class);
        Object postAndWait = this.mDocument.postAndWait(new UncheckedCallable<Object>() {
            public Object call() {
                return DOM.this.mDocument.getElementForNodeId(resolveNodeRequest.nodeId);
            }
        });
        if (postAndWait != null) {
            int mapObject = Runtime.mapObject(jsonRpcPeer, postAndWait);
            Runtime.RemoteObject remoteObject = new Runtime.RemoteObject();
            remoteObject.type = Runtime.ObjectType.OBJECT;
            remoteObject.subtype = Runtime.ObjectSubType.NODE;
            remoteObject.className = postAndWait.getClass().getName();
            remoteObject.value = null;
            remoteObject.description = null;
            remoteObject.objectId = String.valueOf(mapObject);
            ResolveNodeResponse resolveNodeResponse = new ResolveNodeResponse();
            resolveNodeResponse.object = remoteObject;
            return resolveNodeResponse;
        }
        JsonRpcError.ErrorCode errorCode = JsonRpcError.ErrorCode.INVALID_PARAMS;
        throw new JsonRpcException(new JsonRpcError(errorCode, "No known nodeId=" + resolveNodeRequest.nodeId, (JSONObject) null));
    }

    @ChromeDevtoolsMethod
    public void setAttributesAsText(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        final SetAttributesAsTextRequest setAttributesAsTextRequest = (SetAttributesAsTextRequest) this.mObjectMapper.convertValue(jSONObject, SetAttributesAsTextRequest.class);
        this.mDocument.postAndWait((Runnable) new Runnable() {
            public void run() {
                Object elementForNodeId = DOM.this.mDocument.getElementForNodeId(setAttributesAsTextRequest.nodeId);
                if (elementForNodeId != null) {
                    DOM.this.mDocument.setAttributesAsText(elementForNodeId, setAttributesAsTextRequest.text);
                }
            }
        });
    }

    @ChromeDevtoolsMethod
    public void setInspectModeEnabled(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        final SetInspectModeEnabledRequest setInspectModeEnabledRequest = (SetInspectModeEnabledRequest) this.mObjectMapper.convertValue(jSONObject, SetInspectModeEnabledRequest.class);
        this.mDocument.postAndWait((Runnable) new Runnable() {
            public void run() {
                DOM.this.mDocument.setInspectModeEnabled(setInspectModeEnabledRequest.enabled);
            }
        });
    }

    @ChromeDevtoolsMethod
    public PerformSearchResponse performSearch(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        final PerformSearchRequest performSearchRequest = (PerformSearchRequest) this.mObjectMapper.convertValue(jSONObject, PerformSearchRequest.class);
        final ArrayListAccumulator arrayListAccumulator = new ArrayListAccumulator();
        this.mDocument.postAndWait((Runnable) new Runnable() {
            public void run() {
                DOM.this.mDocument.findMatchingElements(performSearchRequest.query, arrayListAccumulator);
            }
        });
        String valueOf = String.valueOf(this.mResultCounter.getAndIncrement());
        this.mSearchResults.put(valueOf, arrayListAccumulator);
        PerformSearchResponse performSearchResponse = new PerformSearchResponse();
        performSearchResponse.searchId = valueOf;
        performSearchResponse.resultCount = arrayListAccumulator.size();
        return performSearchResponse;
    }

    @ChromeDevtoolsMethod
    public GetSearchResultsResponse getSearchResults(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        GetSearchResultsRequest getSearchResultsRequest = (GetSearchResultsRequest) this.mObjectMapper.convertValue(jSONObject, GetSearchResultsRequest.class);
        if (getSearchResultsRequest.searchId == null) {
            LogUtil.w("searchId may not be null");
            return null;
        }
        List list = this.mSearchResults.get(getSearchResultsRequest.searchId);
        if (list == null) {
            LogUtil.w("\"" + getSearchResultsRequest.searchId + "\" is not a valid reference to a search result");
            return null;
        }
        List<Integer> subList = list.subList(getSearchResultsRequest.fromIndex, getSearchResultsRequest.toIndex);
        GetSearchResultsResponse getSearchResultsResponse = new GetSearchResultsResponse();
        getSearchResultsResponse.nodeIds = subList;
        return getSearchResultsResponse;
    }

    @ChromeDevtoolsMethod
    public void discardSearchResults(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        DiscardSearchResultsRequest discardSearchResultsRequest = (DiscardSearchResultsRequest) this.mObjectMapper.convertValue(jSONObject, DiscardSearchResultsRequest.class);
        if (discardSearchResultsRequest.searchId != null) {
            this.mSearchResults.remove(discardSearchResultsRequest.searchId);
        }
    }

    @ChromeDevtoolsMethod
    public GetNodeForLocationResponse getNodeForLocation(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        GetNodeForLocationResponse getNodeForLocationResponse = new GetNodeForLocationResponse();
        GetNodeForLocationRequest getNodeForLocationRequest = (GetNodeForLocationRequest) this.mObjectMapper.convertValue(jSONObject, GetNodeForLocationRequest.class);
        if (getNodeForLocationRequest.x > 0 && getNodeForLocationRequest.y > 0) {
            getNodeForLocationResponse.nodeId = Integer.valueOf(findViewByLocation(getNodeForLocationRequest.x, getNodeForLocationRequest.y));
        }
        return getNodeForLocationResponse;
    }

    public int findViewByLocation(final int i, final int i2) {
        final ArrayListAccumulator arrayListAccumulator = new ArrayListAccumulator();
        this.mDocument.postAndWait((Runnable) new Runnable() {
            public void run() {
                DOM.this.mDocument.findMatchingElements(i, i2, arrayListAccumulator);
            }
        });
        if (arrayListAccumulator.size() > 0) {
            return ((Integer) arrayListAccumulator.get(arrayListAccumulator.size() - 1)).intValue();
        }
        return 0;
    }

    @ChromeDevtoolsMethod
    public GetBoxModelResponse getBoxModel(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        GetBoxModelResponse getBoxModelResponse = new GetBoxModelResponse();
        final BoxModel boxModel = new BoxModel();
        final GetBoxModelRequest getBoxModelRequest = (GetBoxModelRequest) this.mObjectMapper.convertValue(jSONObject, GetBoxModelRequest.class);
        if (getBoxModelRequest.nodeId == null) {
            return null;
        }
        getBoxModelResponse.model = boxModel;
        this.mDocument.postAndWait((Runnable) new Runnable() {
            public void run() {
                final Object elementForNodeId = DOM.this.mDocument.getElementForNodeId(getBoxModelRequest.nodeId.intValue());
                if (elementForNodeId == null) {
                    LogUtil.w("Failed to get style of an element that does not exist, nodeid=" + getBoxModelRequest.nodeId);
                    return;
                }
                DOM.this.mDocument.getElementStyles(elementForNodeId, new StyleAccumulator() {
                    /* JADX WARNING: Removed duplicated region for block: B:12:0x002b  */
                    /* JADX WARNING: Removed duplicated region for block: B:23:0x0121  */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void store(java.lang.String r37, java.lang.String r38, boolean r39) {
                        /*
                            r36 = this;
                            r0 = r36
                            boolean r1 = com.taobao.weex.devtools.inspector.protocol.module.DOM.isNativeMode()
                            if (r1 == 0) goto L_0x0013
                            java.lang.Object r1 = r0
                            boolean r1 = r1 instanceof android.view.View
                            if (r1 == 0) goto L_0x0022
                            java.lang.Object r1 = r0
                            android.view.View r1 = (android.view.View) r1
                            goto L_0x0023
                        L_0x0013:
                            java.lang.Object r1 = r0
                            boolean r1 = r1 instanceof com.taobao.weex.ui.component.WXComponent
                            if (r1 == 0) goto L_0x0022
                            java.lang.Object r1 = r0
                            com.taobao.weex.ui.component.WXComponent r1 = (com.taobao.weex.ui.component.WXComponent) r1
                            android.view.View r1 = r1.getHostView()
                            goto L_0x0023
                        L_0x0022:
                            r1 = 0
                        L_0x0023:
                            if (r1 == 0) goto L_0x0121
                            boolean r4 = r1.isShown()
                            if (r4 == 0) goto L_0x0121
                            float r4 = com.taobao.weex.devtools.inspector.screencast.ScreencastDispatcher.getsBitmapScale()
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r5 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r5 = r0
                            int r6 = r1.getWidth()
                            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
                            r5.width = r6
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r5 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r5 = r0
                            int r6 = r1.getHeight()
                            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
                            r5.height = r6
                            boolean r5 = com.taobao.weex.devtools.inspector.protocol.module.DOM.isNativeMode()
                            if (r5 != 0) goto L_0x0095
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r5 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r5 = r0
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r6 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r6 = r0
                            java.lang.Integer r6 = r6.width
                            int r6 = r6.intValue()
                            int r6 = r6 * 750
                            int r7 = com.taobao.weex.utils.WXViewUtils.getScreenWidth()
                            int r6 = r6 / r7
                            double r6 = (double) r6
                            r8 = 4602678819172646912(0x3fe0000000000000, double:0.5)
                            java.lang.Double.isNaN(r6)
                            double r6 = r6 + r8
                            int r6 = (int) r6
                            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
                            r5.width = r6
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r5 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r5 = r0
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r6 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r6 = r0
                            java.lang.Integer r6 = r6.height
                            int r6 = r6.intValue()
                            int r6 = r6 * 750
                            int r7 = com.taobao.weex.utils.WXViewUtils.getScreenWidth()
                            int r6 = r6 / r7
                            double r6 = (double) r6
                            java.lang.Double.isNaN(r6)
                            double r6 = r6 + r8
                            int r6 = (int) r6
                            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
                            r5.height = r6
                        L_0x0095:
                            r5 = 2
                            int[] r5 = new int[r5]
                            r1.getLocationOnScreen(r5)
                            r6 = 0
                            r6 = r5[r6]
                            float r6 = (float) r6
                            float r6 = r6 * r4
                            double r6 = (double) r6
                            r8 = 1
                            r5 = r5[r8]
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            double r8 = (double) r5
                            int r5 = r1.getWidth()
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            double r10 = (double) r5
                            java.lang.Double.isNaN(r6)
                            java.lang.Double.isNaN(r10)
                            double r10 = r10 + r6
                            int r5 = r1.getHeight()
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            double r12 = (double) r5
                            java.lang.Double.isNaN(r8)
                            java.lang.Double.isNaN(r12)
                            double r12 = r12 + r8
                            int r5 = r1.getPaddingLeft()
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            double r14 = (double) r5
                            int r5 = r1.getPaddingTop()
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            double r2 = (double) r5
                            int r5 = r1.getPaddingRight()
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            r18 = r2
                            double r2 = (double) r5
                            int r5 = r1.getPaddingBottom()
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            r20 = r2
                            double r2 = (double) r5
                            boolean r5 = r1 instanceof android.view.ViewGroup
                            if (r5 == 0) goto L_0x011a
                            android.view.ViewGroup$LayoutParams r1 = r1.getLayoutParams()
                            if (r1 == 0) goto L_0x011a
                            boolean r5 = r1 instanceof android.view.ViewGroup.MarginLayoutParams
                            if (r5 == 0) goto L_0x011a
                            android.view.ViewGroup$MarginLayoutParams r1 = (android.view.ViewGroup.MarginLayoutParams) r1
                            int r5 = r1.leftMargin
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            r22 = r2
                            double r2 = (double) r5
                            int r5 = r1.topMargin
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            r24 = r2
                            double r2 = (double) r5
                            int r5 = r1.rightMargin
                            float r5 = (float) r5
                            float r5 = r5 * r4
                            r26 = r2
                            double r2 = (double) r5
                            int r1 = r1.bottomMargin
                            float r1 = (float) r1
                            float r1 = r1 * r4
                            double r4 = (double) r1
                            goto L_0x0139
                        L_0x011a:
                            r22 = r2
                            r2 = 0
                            r4 = 0
                            goto L_0x0135
                        L_0x0121:
                            r2 = 0
                            r4 = 0
                            r6 = 0
                            r8 = 0
                            r10 = 0
                            r12 = 0
                            r14 = 0
                            r18 = 0
                            r20 = 0
                            r22 = 0
                        L_0x0135:
                            r24 = 0
                            r26 = 0
                        L_0x0139:
                            java.util.ArrayList r1 = new java.util.ArrayList
                            r28 = r4
                            r4 = 8
                            r1.<init>(r4)
                            r16 = 0
                            double r30 = r6 + r16
                            java.lang.Double r5 = java.lang.Double.valueOf(r30)
                            r1.add(r5)
                            double r32 = r8 + r16
                            java.lang.Double r5 = java.lang.Double.valueOf(r32)
                            r1.add(r5)
                            double r34 = r10 - r16
                            java.lang.Double r5 = java.lang.Double.valueOf(r34)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r32)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r34)
                            r1.add(r5)
                            double r16 = r12 - r16
                            java.lang.Double r5 = java.lang.Double.valueOf(r16)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r30)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r16)
                            r1.add(r5)
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r5 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r5 = r0
                            r5.padding = r1
                            java.util.ArrayList r1 = new java.util.ArrayList
                            r1.<init>(r4)
                            double r30 = r30 + r14
                            java.lang.Double r5 = java.lang.Double.valueOf(r30)
                            r1.add(r5)
                            double r32 = r32 + r18
                            java.lang.Double r5 = java.lang.Double.valueOf(r32)
                            r1.add(r5)
                            double r34 = r34 - r20
                            java.lang.Double r5 = java.lang.Double.valueOf(r34)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r32)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r34)
                            r1.add(r5)
                            double r16 = r16 - r22
                            java.lang.Double r5 = java.lang.Double.valueOf(r16)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r30)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r16)
                            r1.add(r5)
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r5 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r5 = r0
                            r5.content = r1
                            java.util.ArrayList r1 = new java.util.ArrayList
                            r1.<init>(r4)
                            java.lang.Double r5 = java.lang.Double.valueOf(r6)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r8)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r10)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r8)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r10)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r12)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r6)
                            r1.add(r5)
                            java.lang.Double r5 = java.lang.Double.valueOf(r12)
                            r1.add(r5)
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r5 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r5 = r0
                            r5.border = r1
                            java.util.ArrayList r1 = new java.util.ArrayList
                            r1.<init>(r4)
                            double r6 = r6 - r24
                            java.lang.Double r4 = java.lang.Double.valueOf(r6)
                            r1.add(r4)
                            double r8 = r8 - r26
                            java.lang.Double r4 = java.lang.Double.valueOf(r8)
                            r1.add(r4)
                            double r10 = r10 + r2
                            java.lang.Double r2 = java.lang.Double.valueOf(r10)
                            r1.add(r2)
                            java.lang.Double r2 = java.lang.Double.valueOf(r8)
                            r1.add(r2)
                            java.lang.Double r2 = java.lang.Double.valueOf(r10)
                            r1.add(r2)
                            double r12 = r12 + r28
                            java.lang.Double r2 = java.lang.Double.valueOf(r12)
                            r1.add(r2)
                            java.lang.Double r2 = java.lang.Double.valueOf(r6)
                            r1.add(r2)
                            java.lang.Double r2 = java.lang.Double.valueOf(r12)
                            r1.add(r2)
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$9 r2 = com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.this
                            com.taobao.weex.devtools.inspector.protocol.module.DOM$BoxModel r2 = r0
                            r2.margin = r1
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.devtools.inspector.protocol.module.DOM.AnonymousClass9.AnonymousClass1.store(java.lang.String, java.lang.String, boolean):void");
                    }
                });
            }
        });
        return getBoxModelResponse;
    }

    private static class GetBoxModelRequest {
        @JsonProperty
        public Integer nodeId;

        private GetBoxModelRequest() {
        }
    }

    private static class BoxModel {
        @JsonProperty(required = true)
        public List<Double> border;
        @JsonProperty(required = true)
        public List<Double> content;
        @JsonProperty(required = true)
        public Integer height;
        @JsonProperty(required = true)
        public List<Double> margin;
        @JsonProperty(required = true)
        public List<Double> padding;
        @JsonProperty(required = true)
        public Integer width;

        private BoxModel() {
        }
    }

    /* access modifiers changed from: private */
    public Node createNodeForElement(Object obj, DocumentView documentView, @Nullable Accumulator<Object> accumulator) {
        List<Node> list;
        if (accumulator != null) {
            accumulator.store(obj);
        }
        NodeDescriptor nodeDescriptor = this.mDocument.getNodeDescriptor(obj);
        Node node = new Node();
        node.nodeId = this.mDocument.getNodeIdForElement(obj).intValue();
        node.nodeType = nodeDescriptor.getNodeType(obj);
        node.nodeName = nodeDescriptor.getNodeName(obj);
        node.localName = nodeDescriptor.getLocalName(obj);
        node.nodeValue = nodeDescriptor.getNodeValue(obj);
        Document.AttributeListAccumulator attributeListAccumulator = new Document.AttributeListAccumulator();
        nodeDescriptor.getAttributes(obj, attributeListAccumulator);
        node.attributes = attributeListAccumulator;
        ElementInfo elementInfo = documentView.getElementInfo(obj);
        if (elementInfo.children.size() == 0) {
            list = Collections.emptyList();
        } else {
            list = new ArrayList<>(elementInfo.children.size());
        }
        int size = elementInfo.children.size();
        for (int i = 0; i < size; i++) {
            list.add(createNodeForElement(elementInfo.children.get(i), documentView, accumulator));
        }
        node.children = list;
        node.childNodeCount = Integer.valueOf(list.size());
        return node;
    }

    /* access modifiers changed from: private */
    public ChildNodeInsertedEvent acquireChildNodeInsertedEvent() {
        ChildNodeInsertedEvent childNodeInsertedEvent = this.mCachedChildNodeInsertedEvent;
        if (childNodeInsertedEvent == null) {
            childNodeInsertedEvent = new ChildNodeInsertedEvent();
        }
        this.mCachedChildNodeInsertedEvent = null;
        return childNodeInsertedEvent;
    }

    /* access modifiers changed from: private */
    public void releaseChildNodeInsertedEvent(ChildNodeInsertedEvent childNodeInsertedEvent) {
        childNodeInsertedEvent.parentNodeId = -1;
        childNodeInsertedEvent.previousNodeId = -1;
        childNodeInsertedEvent.node = null;
        if (this.mCachedChildNodeInsertedEvent == null) {
            this.mCachedChildNodeInsertedEvent = childNodeInsertedEvent;
        }
    }

    /* access modifiers changed from: private */
    public ChildNodeRemovedEvent acquireChildNodeRemovedEvent() {
        ChildNodeRemovedEvent childNodeRemovedEvent = this.mCachedChildNodeRemovedEvent;
        if (childNodeRemovedEvent == null) {
            childNodeRemovedEvent = new ChildNodeRemovedEvent();
        }
        this.mCachedChildNodeRemovedEvent = null;
        return childNodeRemovedEvent;
    }

    /* access modifiers changed from: private */
    public void releaseChildNodeRemovedEvent(ChildNodeRemovedEvent childNodeRemovedEvent) {
        childNodeRemovedEvent.parentNodeId = -1;
        childNodeRemovedEvent.nodeId = -1;
        if (this.mCachedChildNodeRemovedEvent == null) {
            this.mCachedChildNodeRemovedEvent = childNodeRemovedEvent;
        }
    }

    private final class DocumentUpdateListener implements Document.UpdateListener {
        private DocumentUpdateListener() {
        }

        public void onAttributeModified(Object obj, String str, String str2) {
            AttributeModifiedEvent attributeModifiedEvent = new AttributeModifiedEvent();
            attributeModifiedEvent.nodeId = DOM.this.mDocument.getNodeIdForElement(obj).intValue();
            attributeModifiedEvent.name = str;
            attributeModifiedEvent.value = str2;
            DOM.this.mPeerManager.sendNotificationToPeers("DOM.onAttributeModified", attributeModifiedEvent);
        }

        public void onAttributeRemoved(Object obj, String str) {
            AttributeRemovedEvent attributeRemovedEvent = new AttributeRemovedEvent();
            attributeRemovedEvent.nodeId = DOM.this.mDocument.getNodeIdForElement(obj).intValue();
            attributeRemovedEvent.name = str;
            DOM.this.mPeerManager.sendNotificationToPeers("DOM.attributeRemoved", attributeRemovedEvent);
        }

        public void onInspectRequested(Object obj) {
            Integer nodeIdForElement = DOM.this.mDocument.getNodeIdForElement(obj);
            if (nodeIdForElement == null) {
                LogUtil.d("DocumentProvider.Listener.onInspectRequested() called for a non-mapped node: element=%s", obj);
                return;
            }
            InspectNodeRequestedEvent inspectNodeRequestedEvent = new InspectNodeRequestedEvent();
            inspectNodeRequestedEvent.nodeId = nodeIdForElement.intValue();
            DOM.this.mPeerManager.sendNotificationToPeers("DOM.inspectNodeRequested", inspectNodeRequestedEvent);
        }

        public void onChildNodeRemoved(int i, int i2) {
            ChildNodeRemovedEvent access$1700 = DOM.this.acquireChildNodeRemovedEvent();
            access$1700.parentNodeId = i;
            access$1700.nodeId = i2;
            DOM.this.mPeerManager.sendNotificationToPeers("DOM.childNodeRemoved", access$1700);
            DOM.this.releaseChildNodeRemovedEvent(access$1700);
        }

        public void onChildNodeInserted(DocumentView documentView, Object obj, int i, int i2, Accumulator<Object> accumulator) {
            ChildNodeInsertedEvent access$1900 = DOM.this.acquireChildNodeInsertedEvent();
            access$1900.parentNodeId = i;
            access$1900.previousNodeId = i2;
            access$1900.node = DOM.this.createNodeForElement(obj, documentView, accumulator);
            DOM.this.mPeerManager.sendNotificationToPeers("DOM.childNodeInserted", access$1900);
            DOM.this.releaseChildNodeInsertedEvent(access$1900);
        }
    }

    private final class PeerManagerListener extends PeersRegisteredListener {
        private PeerManagerListener() {
        }

        /* access modifiers changed from: protected */
        public synchronized void onFirstPeerRegistered() {
            DOM.this.mDocument.addRef();
            DOM.this.mDocument.addUpdateListener(DOM.this.mListener);
        }

        /* access modifiers changed from: protected */
        public synchronized void onLastPeerUnregistered() {
            DOM.this.mSearchResults.clear();
            DOM.this.mDocument.removeUpdateListener(DOM.this.mListener);
            DOM.this.mDocument.release();
        }
    }

    private static class GetDocumentResponse implements JsonRpcResult {
        @JsonProperty(required = true)
        public Node root;

        private GetDocumentResponse() {
        }
    }

    private static class Node implements JsonRpcResult {
        @JsonProperty
        public List<String> attributes;
        @JsonProperty
        public Integer childNodeCount;
        @JsonProperty
        public List<Node> children;
        @JsonProperty(required = true)
        public String localName;
        @JsonProperty(required = true)
        public int nodeId;
        @JsonProperty(required = true)
        public String nodeName;
        @JsonProperty(required = true)
        public NodeType nodeType;
        @JsonProperty(required = true)
        public String nodeValue;

        private Node() {
        }
    }

    private static class AttributeModifiedEvent {
        @JsonProperty(required = true)
        public String name;
        @JsonProperty(required = true)
        public int nodeId;
        @JsonProperty(required = true)
        public String value;

        private AttributeModifiedEvent() {
        }
    }

    private static class AttributeRemovedEvent {
        @JsonProperty(required = true)
        public String name;
        @JsonProperty(required = true)
        public int nodeId;

        private AttributeRemovedEvent() {
        }
    }

    private static class ChildNodeInsertedEvent {
        @JsonProperty(required = true)
        public Node node;
        @JsonProperty(required = true)
        public int parentNodeId;
        @JsonProperty(required = true)
        public int previousNodeId;

        private ChildNodeInsertedEvent() {
        }
    }

    private static class ChildNodeRemovedEvent {
        @JsonProperty(required = true)
        public int nodeId;
        @JsonProperty(required = true)
        public int parentNodeId;

        private ChildNodeRemovedEvent() {
        }
    }

    private static class HighlightNodeRequest {
        @JsonProperty(required = true)
        public HighlightConfig highlightConfig;
        @JsonProperty
        public Integer nodeId;
        @JsonProperty
        public String objectId;

        private HighlightNodeRequest() {
        }
    }

    private static class HighlightConfig {
        @JsonProperty
        public RGBAColor contentColor;

        private HighlightConfig() {
        }
    }

    private static class InspectNodeRequestedEvent {
        @JsonProperty
        public int nodeId;

        private InspectNodeRequestedEvent() {
        }
    }

    private static class SetInspectModeEnabledRequest {
        @JsonProperty(required = true)
        public boolean enabled;
        @JsonProperty
        public HighlightConfig highlightConfig;
        @JsonProperty
        public Boolean inspectShadowDOM;

        private SetInspectModeEnabledRequest() {
        }
    }

    private static class RGBAColor {
        @JsonProperty
        public Double a;
        @JsonProperty(required = true)
        public int b;
        @JsonProperty(required = true)
        public int g;
        @JsonProperty(required = true)
        public int r;

        private RGBAColor() {
        }

        public int getColor() {
            byte b2 = -1;
            if (this.a != null) {
                long round = Math.round(this.a.doubleValue() * 255.0d);
                if (round < 0) {
                    b2 = 0;
                } else if (round < 255) {
                    b2 = (byte) ((int) round);
                }
            }
            return Color.argb(b2, this.r, this.g, this.b);
        }
    }

    private static class ResolveNodeRequest {
        @JsonProperty(required = true)
        public int nodeId;
        @JsonProperty
        public String objectGroup;

        private ResolveNodeRequest() {
        }
    }

    private static class SetAttributesAsTextRequest {
        @JsonProperty(required = true)
        public int nodeId;
        @JsonProperty(required = true)
        public String text;

        private SetAttributesAsTextRequest() {
        }
    }

    private static class ResolveNodeResponse implements JsonRpcResult {
        @JsonProperty(required = true)
        public Runtime.RemoteObject object;

        private ResolveNodeResponse() {
        }
    }

    private static class PerformSearchRequest {
        @JsonProperty
        public Boolean includeUserAgentShadowDOM;
        @JsonProperty(required = true)
        public String query;

        private PerformSearchRequest() {
        }
    }

    private static class PerformSearchResponse implements JsonRpcResult {
        @JsonProperty(required = true)
        public int resultCount;
        @JsonProperty(required = true)
        public String searchId;

        private PerformSearchResponse() {
        }
    }

    private static class GetSearchResultsRequest {
        @JsonProperty(required = true)
        public int fromIndex;
        @JsonProperty(required = true)
        public String searchId;
        @JsonProperty(required = true)
        public int toIndex;

        private GetSearchResultsRequest() {
        }
    }

    private static class GetSearchResultsResponse implements JsonRpcResult {
        @JsonProperty(required = true)
        public List<Integer> nodeIds;

        private GetSearchResultsResponse() {
        }
    }

    private static class DiscardSearchResultsRequest {
        @JsonProperty(required = true)
        public String searchId;

        private DiscardSearchResultsRequest() {
        }
    }

    private static class GetNodeForLocationRequest {
        @JsonProperty(required = true)
        public int x;
        @JsonProperty(required = true)
        public int y;

        private GetNodeForLocationRequest() {
        }
    }

    private static class GetNodeForLocationResponse implements JsonRpcResult {
        @JsonProperty(required = true)
        public Integer nodeId;

        private GetNodeForLocationResponse() {
        }
    }
}
