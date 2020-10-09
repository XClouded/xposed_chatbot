package com.taobao.weex.devtools.inspector.protocol.module;

import com.taobao.weex.devtools.common.ListUtil;
import com.taobao.weex.devtools.common.LogUtil;
import com.taobao.weex.devtools.common.Util;
import com.taobao.weex.devtools.inspector.elements.Document;
import com.taobao.weex.devtools.inspector.elements.Origin;
import com.taobao.weex.devtools.inspector.elements.StyleAccumulator;
import com.taobao.weex.devtools.inspector.elements.W3CStyleConstants;
import com.taobao.weex.devtools.inspector.helper.ChromePeerManager;
import com.taobao.weex.devtools.inspector.helper.PeersRegisteredListener;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcPeer;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcResult;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsDomain;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsMethod;
import com.taobao.weex.devtools.json.ObjectMapper;
import com.taobao.weex.devtools.json.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class CSS implements ChromeDevtoolsDomain {
    /* access modifiers changed from: private */
    public static final HashMap<String, String> sProperties = new HashMap<>();
    /* access modifiers changed from: private */
    public final Document mDocument;
    private final ObjectMapper mObjectMapper = new ObjectMapper();
    private final ChromePeerManager mPeerManager = new ChromePeerManager();

    private static class PseudoIdMatches {
        @JsonProperty(required = true)
        public List<RuleMatch> matches = new ArrayList();
        @JsonProperty(required = true)
        public int pseudoId;
    }

    @ChromeDevtoolsMethod
    public void disable(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    @ChromeDevtoolsMethod
    public void enable(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
    }

    public CSS(Document document) {
        this.mDocument = (Document) Util.throwIfNull(document);
        this.mPeerManager.setListener(new PeerManagerListener());
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult getComputedStyleForNode(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        final GetComputedStyleForNodeRequest getComputedStyleForNodeRequest = (GetComputedStyleForNodeRequest) this.mObjectMapper.convertValue(jSONObject, GetComputedStyleForNodeRequest.class);
        final GetComputedStyleForNodeResult getComputedStyleForNodeResult = new GetComputedStyleForNodeResult();
        getComputedStyleForNodeResult.computedStyle = new ArrayList();
        this.mDocument.postAndWait((Runnable) new Runnable() {
            public void run() {
                Object elementForNodeId = CSS.this.mDocument.getElementForNodeId(getComputedStyleForNodeRequest.nodeId);
                if (elementForNodeId == null) {
                    LogUtil.e("Tried to get the style of an element that does not exist, using nodeid=" + getComputedStyleForNodeRequest.nodeId);
                    return;
                }
                CSS.this.mockStyleProperty(getComputedStyleForNodeResult.computedStyle, CSS.sProperties);
                CSS.this.mDocument.getElementStyles(elementForNodeId, new StyleAccumulator() {
                    public void store(String str, String str2, boolean z) {
                        if (!z) {
                            CSSComputedStyleProperty cSSComputedStyleProperty = new CSSComputedStyleProperty();
                            if (!str.startsWith(W3CStyleConstants.V_PREFIX)) {
                                cSSComputedStyleProperty.name = str;
                                cSSComputedStyleProperty.value = str2;
                                getComputedStyleForNodeResult.computedStyle.add(cSSComputedStyleProperty);
                            }
                        }
                    }
                });
            }
        });
        return getComputedStyleForNodeResult;
    }

    static {
        sProperties.put("width", "");
        sProperties.put("height", "");
        sProperties.put(W3CStyleConstants.PADDING_LEFT, "");
        sProperties.put(W3CStyleConstants.PADDING_TOP, "");
        sProperties.put(W3CStyleConstants.PADDING_RIGHT, "");
        sProperties.put(W3CStyleConstants.PADDING_BOTTOM, "");
        sProperties.put(W3CStyleConstants.BORDER_LEFT_WIDTH, "");
        sProperties.put(W3CStyleConstants.BORDER_TOP_WIDTH, "");
        sProperties.put(W3CStyleConstants.BORDER_RIGHT_WIDTH, "");
        sProperties.put(W3CStyleConstants.BORDER_BOTTOM_WIDTH, "");
        sProperties.put(W3CStyleConstants.MARGIN_LEFT, "");
        sProperties.put(W3CStyleConstants.MARGIN_TOP, "");
        sProperties.put(W3CStyleConstants.MARGIN_RIGHT, "");
        sProperties.put(W3CStyleConstants.MARGIN_BOTTOM, "");
        sProperties.put("left", "");
        sProperties.put("top", "");
        sProperties.put("right", "");
        sProperties.put("bottom", "");
    }

    /* access modifiers changed from: private */
    public void mockStyleProperty(List<CSSComputedStyleProperty> list, HashMap<String, String> hashMap) {
        for (Map.Entry next : hashMap.entrySet()) {
            addStyleProperty(list, (String) next.getKey(), (String) next.getValue());
        }
    }

    private void addStyleProperty(List<CSSComputedStyleProperty> list, String str, String str2) {
        CSSComputedStyleProperty cSSComputedStyleProperty = new CSSComputedStyleProperty();
        cSSComputedStyleProperty.name = str;
        cSSComputedStyleProperty.value = str2;
        list.add(cSSComputedStyleProperty);
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult getMatchedStylesForNode(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        final GetMatchedStylesForNodeRequest getMatchedStylesForNodeRequest = (GetMatchedStylesForNodeRequest) this.mObjectMapper.convertValue(jSONObject, GetMatchedStylesForNodeRequest.class);
        GetMatchedStylesForNodeResult getMatchedStylesForNodeResult = new GetMatchedStylesForNodeResult();
        ArrayList arrayList = new ArrayList();
        final RuleMatch ruleMatch = new RuleMatch();
        initMatch(ruleMatch, "local");
        arrayList.add(ruleMatch);
        final RuleMatch ruleMatch2 = new RuleMatch();
        if (!DOM.isNativeMode()) {
            initMatch(ruleMatch2, "virtual");
            arrayList.add(ruleMatch2);
        }
        getMatchedStylesForNodeResult.matchedCSSRules = arrayList;
        this.mDocument.postAndWait((Runnable) new Runnable() {
            public void run() {
                Object elementForNodeId = CSS.this.mDocument.getElementForNodeId(getMatchedStylesForNodeRequest.nodeId);
                if (elementForNodeId == null) {
                    LogUtil.w("Failed to get style of an element that does not exist, nodeid=" + getMatchedStylesForNodeRequest.nodeId);
                    return;
                }
                CSS.this.mDocument.getElementStyles(elementForNodeId, new StyleAccumulator() {
                    public void store(String str, String str2, boolean z) {
                        if (!z) {
                            CSSProperty cSSProperty = new CSSProperty();
                            if (!str.startsWith(W3CStyleConstants.V_PREFIX)) {
                                cSSProperty.name = str;
                                cSSProperty.value = str2;
                                ruleMatch.rule.style.cssProperties.add(cSSProperty);
                            } else if (!DOM.isNativeMode()) {
                                CSSProperty cSSProperty2 = new CSSProperty();
                                cSSProperty2.name = str;
                                cSSProperty2.value = str2;
                                ruleMatch2.rule.style.cssProperties.add(cSSProperty2);
                            }
                        }
                    }
                });
            }
        });
        getMatchedStylesForNodeResult.inherited = Collections.emptyList();
        getMatchedStylesForNodeResult.pseudoElements = Collections.emptyList();
        return getMatchedStylesForNodeResult;
    }

    private final class PeerManagerListener extends PeersRegisteredListener {
        private PeerManagerListener() {
        }

        /* access modifiers changed from: protected */
        public synchronized void onFirstPeerRegistered() {
            CSS.this.mDocument.addRef();
        }

        /* access modifiers changed from: protected */
        public synchronized void onLastPeerUnregistered() {
            CSS.this.mDocument.release();
        }
    }

    private static class CSSComputedStyleProperty {
        @JsonProperty(required = true)
        public String name;
        @JsonProperty(required = true)
        public String value;

        private CSSComputedStyleProperty() {
        }
    }

    private static class RuleMatch {
        @JsonProperty
        public List<Integer> matchingSelectors;
        @JsonProperty
        public CSSRule rule;

        private RuleMatch() {
        }
    }

    private static class SelectorList {
        @JsonProperty
        public List<Selector> selectors;
        @JsonProperty
        public String text;

        private SelectorList() {
        }
    }

    private static class SourceRange {
        @JsonProperty(required = true)
        public int endColumn;
        @JsonProperty(required = true)
        public int endLine;
        @JsonProperty(required = true)
        public int startColumn;
        @JsonProperty(required = true)
        public int startLine;

        private SourceRange() {
        }
    }

    private static class Selector {
        @JsonProperty
        public SourceRange range;
        @JsonProperty(required = true)
        public String text;

        private Selector() {
        }
    }

    private static class CSSRule {
        @JsonProperty
        public Origin origin;
        @JsonProperty(required = true)
        public SelectorList selectorList;
        @JsonProperty
        public CSSStyle style;
        @JsonProperty
        public String styleSheetId;

        private CSSRule() {
        }
    }

    private static class CSSStyle {
        @JsonProperty(required = true)
        public List<CSSProperty> cssProperties;
        @JsonProperty
        public String cssText;
        @JsonProperty
        public SourceRange range;
        @JsonProperty
        public List<ShorthandEntry> shorthandEntries;
        @JsonProperty
        public String styleSheetId;

        private CSSStyle() {
        }
    }

    private static class ShorthandEntry {
        @JsonProperty
        public Boolean important;
        @JsonProperty(required = true)
        public String name;
        @JsonProperty(required = true)
        public String value;

        private ShorthandEntry() {
        }
    }

    private static class CSSProperty {
        @JsonProperty
        public Boolean disabled;
        @JsonProperty
        public Boolean implicit;
        @JsonProperty
        public Boolean important;
        @JsonProperty(required = true)
        public String name;
        @JsonProperty
        public Boolean parsedOk;
        @JsonProperty
        public SourceRange range;
        @JsonProperty
        public String text;
        @JsonProperty(required = true)
        public String value;

        private CSSProperty() {
        }
    }

    private static class GetComputedStyleForNodeRequest {
        @JsonProperty(required = true)
        public int nodeId;

        private GetComputedStyleForNodeRequest() {
        }
    }

    private static class InheritedStyleEntry {
        @JsonProperty(required = true)
        public CSSStyle inlineStyle;
        @JsonProperty(required = true)
        public List<RuleMatch> matchedCSSRules;

        private InheritedStyleEntry() {
        }
    }

    private static class GetComputedStyleForNodeResult implements JsonRpcResult {
        @JsonProperty(required = true)
        public List<CSSComputedStyleProperty> computedStyle;

        private GetComputedStyleForNodeResult() {
        }
    }

    private static class GetMatchedStylesForNodeRequest {
        @JsonProperty
        public Boolean excludeInherited;
        @JsonProperty
        public Boolean excludePseudo;
        @JsonProperty(required = true)
        public int nodeId;

        private GetMatchedStylesForNodeRequest() {
        }
    }

    private static class GetMatchedStylesForNodeResult implements JsonRpcResult {
        @JsonProperty
        public List<InheritedStyleEntry> inherited;
        @JsonProperty
        public List<RuleMatch> matchedCSSRules;
        @JsonProperty
        public List<PseudoIdMatches> pseudoElements;

        private GetMatchedStylesForNodeResult() {
        }
    }

    private void initMatch(RuleMatch ruleMatch, String str) {
        ruleMatch.matchingSelectors = ListUtil.newImmutableList(0);
        Selector selector = new Selector();
        selector.text = str;
        CSSRule cSSRule = new CSSRule();
        cSSRule.origin = Origin.REGULAR;
        cSSRule.selectorList = new SelectorList();
        cSSRule.selectorList.selectors = ListUtil.newImmutableList(selector);
        cSSRule.style = new CSSStyle();
        cSSRule.style.cssProperties = new ArrayList();
        ruleMatch.rule = cSSRule;
        cSSRule.style.shorthandEntries = Collections.emptyList();
    }
}
