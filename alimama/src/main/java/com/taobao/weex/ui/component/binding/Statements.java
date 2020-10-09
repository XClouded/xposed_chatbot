package com.taobao.weex.ui.component.binding;

import android.os.Looper;
import androidx.collection.ArrayMap;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.dom.binding.ELUtils;
import com.taobao.weex.dom.binding.WXStatement;
import com.taobao.weex.el.parse.ArrayStack;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.el.parse.Token;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.action.GraphicPosition;
import com.taobao.weex.ui.action.GraphicSize;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentFactory;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.component.list.template.CellDataManager;
import com.taobao.weex.ui.component.list.template.CellRenderContext;
import com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList;
import com.taobao.weex.ui.component.list.template.jni.NativeRenderObjectUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Statements {
    private static final ThreadLocal<Map<String, Object>> dynamicLocal = new ThreadLocal<>();

    public static void parseStatementsToken(WXComponent wXComponent) {
        if (wXComponent.getBasicComponentData().isRenderPtrEmpty()) {
            wXComponent.getBasicComponentData().setRenderObjectPr(wXComponent.getRenderObjectPtr());
        }
        if (wXComponent.getBasicComponentData() != null) {
            BasicComponentData basicComponentData = wXComponent.getBasicComponentData();
            basicComponentData.getAttrs().parseStatements();
            basicComponentData.getStyles().parseStatements();
            basicComponentData.getEvents().parseStatements();
        }
        if (wXComponent instanceof WXVContainer) {
            WXVContainer wXVContainer = (WXVContainer) wXComponent;
            int childCount = wXVContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                parseStatementsToken(wXVContainer.getChild(i));
            }
        }
    }

    public static void initLazyComponent(WXComponent wXComponent, WXVContainer wXVContainer) {
        if (wXComponent.isLazy() || wXComponent.getHostView() == null) {
            wXComponent.lazy(false);
            if (wXVContainer != null) {
                wXVContainer.createChildViewAt(wXVContainer.indexOf(wXComponent));
            } else {
                wXComponent.createView();
            }
            wXComponent.applyLayoutAndEvent(wXComponent);
            wXComponent.bindData(wXComponent);
        }
    }

    public static WXComponent copyComponentTree(WXComponent wXComponent) {
        return copyComponentTree(wXComponent, wXComponent.getParent());
    }

    private static final WXComponent copyComponentTree(WXComponent wXComponent, WXVContainer wXVContainer) {
        BasicComponentData basicComponentData;
        try {
            basicComponentData = wXComponent.getBasicComponentData().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            basicComponentData = null;
        }
        WXComponent newInstance = WXComponentFactory.newInstance(wXComponent.getInstance(), wXVContainer, basicComponentData);
        GraphicPosition layoutPosition = wXComponent.getLayoutPosition();
        GraphicSize layoutSize = wXComponent.getLayoutSize();
        newInstance.updateDemission(layoutPosition.getTop(), layoutPosition.getBottom(), layoutPosition.getLeft(), layoutPosition.getRight(), layoutSize.getHeight(), layoutSize.getWidth());
        newInstance.updateExtra(wXComponent.getExtra());
        if (wXComponent instanceof WXVContainer) {
            WXVContainer wXVContainer2 = (WXVContainer) wXComponent;
            WXVContainer wXVContainer3 = (WXVContainer) newInstance;
            int childCount = wXVContainer2.getChildCount();
            for (int i = 0; i < childCount; i++) {
                WXComponent child = wXVContainer2.getChild(i);
                if (child != null) {
                    WXComponent copyComponentTree = copyComponentTree(child, wXVContainer3);
                    wXVContainer3.addChild(copyComponentTree);
                    NativeRenderObjectUtils.nativeAddChildRenderObject(wXVContainer3.getRenderObjectPtr(), copyComponentTree.getRenderObjectPtr());
                }
            }
        }
        if (wXComponent.isWaste()) {
            newInstance.setWaste(true);
        }
        return newInstance;
    }

    public static final List<WXComponent> doRender(WXComponent wXComponent, CellRenderContext cellRenderContext) {
        ArrayList arrayList = new ArrayList(4);
        try {
            doRenderComponent(wXComponent, cellRenderContext, arrayList);
        } catch (Exception e) {
            WXLogUtils.e("WeexStatementRender", (Throwable) e);
        }
        return arrayList;
    }

    public static final void doInitCompontent(List<WXComponent> list) {
        if (list != null && list.size() != 0) {
            for (WXComponent next : list) {
                if (next.getParent() != null) {
                    WXVContainer parent = next.getParent();
                    int indexOf = parent.indexOf(next);
                    if (indexOf >= 0) {
                        parent.createChildViewAt(indexOf);
                        next.applyLayoutAndEvent(next);
                        next.bindData(next);
                    } else {
                        throw new IllegalArgumentException("render node cann't find");
                    }
                } else {
                    throw new IllegalArgumentException("render node parent cann't find");
                }
            }
        }
    }

    private static final int doRenderComponent(WXComponent wXComponent, CellRenderContext cellRenderContext, List<WXComponent> list) {
        int i;
        Map map;
        Collection collection;
        int i2;
        Object obj;
        WXComponent wXComponent2;
        Token token;
        Iterator it;
        String str;
        WXComponent wXComponent3 = wXComponent;
        CellRenderContext cellRenderContext2 = cellRenderContext;
        List<WXComponent> list2 = list;
        WXVContainer parent = wXComponent.getParent();
        WXStatement statement = wXComponent.getAttrs().getStatement();
        if (statement != null) {
            Token token2 = statement.get(WXStatement.WX_IF) instanceof Token ? (Token) statement.get(WXStatement.WX_IF) : null;
            JSONObject jSONObject = statement.get(WXStatement.WX_FOR) instanceof JSONObject ? (JSONObject) statement.get(WXStatement.WX_FOR) : null;
            boolean z = false;
            if (jSONObject != null) {
                int indexOf = parent.indexOf(wXComponent3);
                if (jSONObject.get(WXStatement.WX_FOR_LIST) instanceof Token) {
                    Token token3 = (Token) jSONObject.get(WXStatement.WX_FOR_LIST);
                    String string = jSONObject.getString(WXStatement.WX_FOR_INDEX);
                    String string2 = jSONObject.getString(WXStatement.WX_FOR_ITEM);
                    Object execute = token3 != null ? token3.execute(cellRenderContext2.stack) : null;
                    boolean z2 = execute instanceof List;
                    if (z2 || (execute instanceof Map)) {
                        if (z2) {
                            collection = (List) execute;
                            map = null;
                        } else {
                            Map map2 = (Map) execute;
                            map = map2;
                            collection = map2.keySet();
                        }
                        HashMap hashMap = new HashMap();
                        Iterator it2 = collection.iterator();
                        int i3 = 0;
                        while (it2.hasNext()) {
                            Object next = it2.next();
                            if (map == null) {
                                Integer valueOf = Integer.valueOf(i3);
                                i2 = i3 + 1;
                                obj = next;
                                next = valueOf;
                            } else {
                                i2 = i3;
                                obj = map.get(next);
                            }
                            if (string != null) {
                                hashMap.put(string, next);
                            }
                            if (string2 != null) {
                                hashMap.put(string2, obj);
                            } else {
                                cellRenderContext2.stack.push(obj);
                            }
                            if (hashMap.size() > 0) {
                                cellRenderContext2.stack.push(hashMap);
                            }
                            if (token2 == null || Operators.isTrue(token2.execute(cellRenderContext2.stack))) {
                                if (indexOf < parent.getChildCount()) {
                                    wXComponent2 = parent.getChild(indexOf);
                                    if (!isCreateFromNodeStatement(wXComponent2, wXComponent3)) {
                                        wXComponent2 = null;
                                    }
                                    if (wXComponent2 != null && wXComponent2.isWaste()) {
                                        wXComponent2.setWaste(z);
                                    }
                                } else {
                                    wXComponent2 = null;
                                }
                                if (wXComponent2 == null) {
                                    long currentTimeMillis = System.currentTimeMillis();
                                    WXComponent copyComponentTree = copyComponentTree(wXComponent3, parent);
                                    copyComponentTree.setWaste(z);
                                    if (copyComponentTree.getAttrs().getStatement() != null) {
                                        copyComponentTree.getAttrs().getStatement().remove(WXStatement.WX_FOR);
                                        copyComponentTree.getAttrs().getStatement().remove(WXStatement.WX_IF);
                                    }
                                    parent.addChild(copyComponentTree, indexOf);
                                    it = it2;
                                    str = string;
                                    token = token2;
                                    NativeRenderObjectUtils.nativeAddChildRenderObject(parent.getRenderObjectPtr(), copyComponentTree.getRenderObjectPtr());
                                    list2.add(copyComponentTree);
                                    if (WXEnvironment.isApkDebugable()) {
                                        WXLogUtils.d(WXRecyclerTemplateList.TAG, Thread.currentThread().getName() + copyComponentTree.getRef() + copyComponentTree.getComponentType() + "statements copy component tree used " + (System.currentTimeMillis() - currentTimeMillis));
                                    }
                                    wXComponent2 = copyComponentTree;
                                } else {
                                    token = token2;
                                    it = it2;
                                    str = string;
                                }
                                doBindingAttrsEventAndRenderChildNode(wXComponent2, cellRenderContext2, list2);
                                indexOf++;
                                if (hashMap.size() > 0) {
                                    cellRenderContext2.stack.push(hashMap);
                                }
                                if (string2 == null) {
                                    cellRenderContext2.stack.pop();
                                }
                                i3 = i2;
                                string = str;
                                it2 = it;
                                token2 = token;
                                z = false;
                            } else {
                                i3 = i2;
                            }
                        }
                    }
                } else {
                    WXLogUtils.e(WXRecyclerTemplateList.TAG, jSONObject.toJSONString() + " not call vfor block, for pre compile");
                }
                while (indexOf < parent.getChildCount()) {
                    WXComponent child = parent.getChild(indexOf);
                    if (!isCreateFromNodeStatement(child, wXComponent3)) {
                        break;
                    }
                    child.setWaste(true);
                    indexOf++;
                }
                return indexOf - parent.indexOf(wXComponent3);
            }
            Token token4 = token2;
            if (token4 != null) {
                if (!Operators.isTrue(token4.execute(cellRenderContext2.stack))) {
                    wXComponent3.setWaste(true);
                    return 1;
                }
                i = 1;
                wXComponent3.setWaste(false);
                doBindingAttrsEventAndRenderChildNode(wXComponent, cellRenderContext, list);
                return i;
            }
        }
        i = 1;
        doBindingAttrsEventAndRenderChildNode(wXComponent, cellRenderContext, list);
        return i;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v4, resolved type: java.util.Map<java.lang.String, java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v3, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v5, resolved type: java.util.Map<java.lang.String, java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v4, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v17, resolved type: java.util.Map<java.lang.String, java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v18, resolved type: java.util.Map<java.lang.String, java.lang.Object>} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void doBindingAttrsEventAndRenderChildNode(com.taobao.weex.ui.component.WXComponent r18, com.taobao.weex.ui.component.list.template.CellRenderContext r19, java.util.List<com.taobao.weex.ui.component.WXComponent> r20) {
        /*
            r0 = r18
            r1 = r19
            com.taobao.weex.dom.WXAttr r2 = r18.getAttrs()
            com.taobao.weex.el.parse.ArrayStack r3 = r1.stack
            java.lang.String r4 = "@isComponentRoot"
            java.lang.Object r4 = r2.get(r4)
            r6 = 3
            r7 = 2
            r8 = 1
            r9 = 0
            r10 = 0
            if (r4 == 0) goto L_0x017b
            java.lang.String r4 = "@isComponentRoot"
            java.lang.Object r4 = r2.get(r4)
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r10)
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r4, r11)
            boolean r4 = r4.booleanValue()
            if (r4 == 0) goto L_0x017b
            java.lang.String r4 = "@componentProps"
            java.lang.Object r4 = r2.get(r4)
            if (r4 == 0) goto L_0x017b
            java.lang.String r4 = "@componentProps"
            java.lang.Object r4 = r2.get(r4)
            boolean r4 = com.taobao.weex.dom.binding.JSONUtils.isJSON((java.lang.Object) r4)
            if (r4 == 0) goto L_0x017b
            java.lang.String r4 = "@templateId"
            java.lang.Object r4 = r2.get(r4)
            java.lang.String r4 = (java.lang.String) r4
            boolean r11 = android.text.TextUtils.isEmpty(r4)
            if (r11 != 0) goto L_0x015c
            com.taobao.weex.ui.component.list.template.CellRenderState r11 = r19.getRenderState()
            java.util.Map r11 = r11.getVirtualComponentIds()
            java.lang.String r12 = r18.getViewTreeKey()
            java.lang.Object r11 = r11.get(r12)
            java.lang.String r11 = (java.lang.String) r11
            r12 = 5
            if (r11 != 0) goto L_0x00e2
            com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList r11 = r1.templateList
            java.lang.String r11 = r11.getRef()
            java.lang.String r13 = r18.getViewTreeKey()
            com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList r14 = r1.templateList
            int r15 = r1.position
            long r14 = r14.getItemId(r15)
            java.lang.String r11 = com.taobao.weex.ui.component.list.template.CellDataManager.createVirtualComponentId(r11, r13, r14)
            java.lang.String r13 = "@componentProps"
            java.lang.Object r13 = r2.get(r13)
            com.alibaba.fastjson.JSONObject r13 = com.taobao.weex.dom.binding.JSONUtils.toJSON(r13)
            com.taobao.weex.el.parse.ArrayStack r14 = r1.stack
            java.util.Map r13 = renderProps(r13, r14)
            com.taobao.weex.bridge.WXBridgeManager r14 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
            java.lang.String r15 = "componentHook"
            java.lang.String r5 = r18.getInstanceId()
            java.lang.Object[] r12 = new java.lang.Object[r12]
            r12[r10] = r4
            java.lang.String r4 = "lifecycle"
            r12[r8] = r4
            java.lang.String r4 = "create"
            r12[r7] = r4
            java.lang.Object[] r4 = new java.lang.Object[r7]
            r4[r10] = r11
            r4[r8] = r13
            r12[r6] = r4
            r4 = 4
            r12[r4] = r9
            com.taobao.weex.bridge.EventResult r4 = r14.syncCallJSEventWithResult(r15, r5, r9, r12)
            if (r4 == 0) goto L_0x00c6
            java.lang.Object r5 = r4.getResult()
            if (r5 == 0) goto L_0x00c6
            java.lang.Object r5 = r4.getResult()
            boolean r5 = r5 instanceof java.util.Map
            if (r5 == 0) goto L_0x00c6
            java.lang.Object r4 = r4.getResult()
            java.util.Map r4 = (java.util.Map) r4
            r13.putAll(r4)
        L_0x00c6:
            com.taobao.weex.ui.component.list.template.CellRenderState r4 = r19.getRenderState()
            java.util.Map r4 = r4.getVirtualComponentIds()
            java.lang.String r5 = r18.getViewTreeKey()
            r4.put(r5, r11)
            com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList r4 = r1.templateList
            com.taobao.weex.ui.component.list.template.CellDataManager r4 = r4.getCellDataManager()
            int r5 = r1.position
            r4.createVirtualComponentData(r5, r11, r13)
            r4 = 1
            goto L_0x0151
        L_0x00e2:
            com.taobao.weex.ui.component.list.template.CellRenderState r4 = r19.getRenderState()
            java.util.Map r4 = r4.getVirtualComponentDatas()
            java.lang.Object r13 = r4.get(r11)
            com.taobao.weex.ui.component.list.template.CellRenderState r4 = r19.getRenderState()
            boolean r4 = r4.isHasDataUpdate()
            if (r4 == 0) goto L_0x0150
            java.lang.String r4 = "@componentProps"
            java.lang.Object r4 = r2.get(r4)
            com.alibaba.fastjson.JSONObject r4 = (com.alibaba.fastjson.JSONObject) r4
            com.taobao.weex.el.parse.ArrayStack r5 = r1.stack
            java.util.Map r4 = renderProps(r4, r5)
            com.taobao.weex.bridge.WXBridgeManager r5 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
            java.lang.String r14 = "componentHook"
            java.lang.String r15 = r18.getInstanceId()
            java.lang.Object[] r12 = new java.lang.Object[r12]
            r12[r10] = r11
            java.lang.String r17 = "lifecycle"
            r12[r8] = r17
            java.lang.String r17 = "syncState"
            r12[r7] = r17
            java.lang.Object[] r9 = new java.lang.Object[r7]
            r9[r10] = r11
            r9[r8] = r4
            r12[r6] = r9
            r9 = 0
            r16 = 4
            r12[r16] = r9
            com.taobao.weex.bridge.EventResult r5 = r5.syncCallJSEventWithResult(r14, r15, r9, r12)
            if (r5 == 0) goto L_0x0150
            java.lang.Object r9 = r5.getResult()
            if (r9 == 0) goto L_0x0150
            java.lang.Object r9 = r5.getResult()
            boolean r9 = r9 instanceof java.util.Map
            if (r9 == 0) goto L_0x0150
            java.lang.Object r5 = r5.getResult()
            java.util.Map r5 = (java.util.Map) r5
            r4.putAll(r5)
            com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList r5 = r1.templateList
            com.taobao.weex.ui.component.list.template.CellDataManager r5 = r5.getCellDataManager()
            r5.updateVirtualComponentData(r11, r4)
            r13 = r4
        L_0x0150:
            r4 = 0
        L_0x0151:
            com.taobao.weex.dom.WXAttr r5 = r18.getAttrs()
            java.lang.String r9 = "@virtualComponentId"
            r5.put((java.lang.String) r9, (java.lang.Object) r11)
            r9 = r11
            goto L_0x016c
        L_0x015c:
            java.lang.String r4 = "@componentProps"
            java.lang.Object r4 = r2.get(r4)
            com.alibaba.fastjson.JSONObject r4 = (com.alibaba.fastjson.JSONObject) r4
            com.taobao.weex.el.parse.ArrayStack r5 = r1.stack
            java.util.Map r13 = renderProps(r4, r5)
            r4 = 0
            r9 = 0
        L_0x016c:
            com.taobao.weex.el.parse.ArrayStack r5 = new com.taobao.weex.el.parse.ArrayStack
            r5.<init>()
            r1.stack = r5
            if (r13 == 0) goto L_0x017d
            com.taobao.weex.el.parse.ArrayStack r5 = r1.stack
            r5.push(r13)
            goto L_0x017d
        L_0x017b:
            r4 = 0
            r9 = 0
        L_0x017d:
            com.taobao.weex.dom.binding.WXStatement r5 = r2.getStatement()
            if (r5 == 0) goto L_0x018e
            com.taobao.weex.dom.binding.WXStatement r2 = r2.getStatement()
            java.lang.String r5 = "[[once]]"
            java.lang.Object r2 = r2.get(r5)
            goto L_0x018f
        L_0x018e:
            r2 = 0
        L_0x018f:
            if (r2 == 0) goto L_0x01bc
            com.taobao.weex.ui.component.list.template.CellRenderState r2 = r19.getRenderState()
            java.util.Map r2 = r2.getOnceComponentStates()
            java.lang.String r5 = r18.getViewTreeKey()
            java.lang.Object r2 = r2.get(r5)
            com.taobao.weex.el.parse.ArrayStack r2 = (com.taobao.weex.el.parse.ArrayStack) r2
            if (r2 != 0) goto L_0x01ba
            com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList r2 = r1.templateList
            com.taobao.weex.el.parse.ArrayStack r2 = r2.copyStack(r1, r3)
            com.taobao.weex.ui.component.list.template.CellRenderState r5 = r19.getRenderState()
            java.util.Map r5 = r5.getOnceComponentStates()
            java.lang.String r11 = r18.getViewTreeKey()
            r5.put(r11, r2)
        L_0x01ba:
            r1.stack = r2
        L_0x01bc:
            doRenderBindingAttrsAndEvent(r18, r19)
            boolean r2 = r0 instanceof com.taobao.weex.ui.component.WXVContainer
            if (r2 == 0) goto L_0x01e4
            boolean r2 = r18.isWaste()
            if (r2 == 0) goto L_0x01ce
            boolean r2 = r0 instanceof com.taobao.weex.ui.component.list.WXCell
            if (r2 != 0) goto L_0x01ce
            return
        L_0x01ce:
            r2 = r0
            com.taobao.weex.ui.component.WXVContainer r2 = (com.taobao.weex.ui.component.WXVContainer) r2
            r5 = 0
        L_0x01d2:
            int r11 = r2.getChildCount()
            if (r5 >= r11) goto L_0x01e4
            com.taobao.weex.ui.component.WXComponent r11 = r2.getChild(r5)
            r12 = r20
            int r11 = doRenderComponent(r11, r1, r12)
            int r5 = r5 + r11
            goto L_0x01d2
        L_0x01e4:
            com.taobao.weex.el.parse.ArrayStack r2 = r1.stack
            if (r3 == r2) goto L_0x01ea
            r1.stack = r3
        L_0x01ea:
            if (r4 == 0) goto L_0x021b
            if (r9 == 0) goto L_0x021b
            com.taobao.weex.bridge.WXBridgeManager r2 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
            java.lang.String r3 = "componentHook"
            java.lang.String r4 = r18.getInstanceId()
            r5 = 4
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r5[r10] = r9
            java.lang.String r9 = "lifecycle"
            r5[r8] = r9
            java.lang.String r9 = "attach"
            r5[r7] = r9
            java.lang.Object[] r7 = new java.lang.Object[r8]
            com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList r8 = r1.templateList
            java.lang.String r8 = r8.getRef()
            int r1 = r1.position
            java.util.Map r0 = com.taobao.weex.ui.component.list.template.TemplateDom.findAllComponentRefs(r8, r1, r0)
            r7[r10] = r0
            r5[r6] = r7
            r0 = 0
            r2.asyncCallJSEventVoidResult(r3, r4, r0, r5)
        L_0x021b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.binding.Statements.doBindingAttrsEventAndRenderChildNode(com.taobao.weex.ui.component.WXComponent, com.taobao.weex.ui.component.list.template.CellRenderContext, java.util.List):void");
    }

    private static boolean isCreateFromNodeStatement(WXComponent wXComponent, WXComponent wXComponent2) {
        return wXComponent.getRef() != null && wXComponent.getRef().equals(wXComponent2.getRef());
    }

    private static void doRenderBindingAttrsAndEvent(WXComponent wXComponent, CellRenderContext cellRenderContext) {
        ArrayStack arrayStack = cellRenderContext.stack;
        wXComponent.setWaste(false);
        WXAttr attrs = wXComponent.getAttrs();
        if (!(attrs == null || attrs.getBindingAttrs() == null || attrs.getBindingAttrs().size() <= 0)) {
            Map<String, Object> renderBindingAttrs = renderBindingAttrs(wXComponent.getAttrs().getBindingAttrs(), arrayStack);
            Iterator<Map.Entry<String, Object>> it = renderBindingAttrs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry next = it.next();
                Object value = next.getValue();
                Object obj = attrs.get((String) next.getKey());
                if (value == null) {
                    if (obj == null) {
                        it.remove();
                    }
                } else if (value.equals(obj)) {
                    it.remove();
                }
            }
            if (renderBindingAttrs.size() > 0) {
                if (renderBindingAttrs.size() != 1 || renderBindingAttrs.get("src") == null || !(wXComponent instanceof WXImage)) {
                    wXComponent.nativeUpdateAttrs(renderBindingAttrs);
                } else {
                    wXComponent.getAttrs().put("src", renderBindingAttrs.get("src"));
                }
                if (isMainThread()) {
                    wXComponent.updateProperties(renderBindingAttrs);
                }
                renderBindingAttrs.clear();
            }
        }
        WXStyle styles = wXComponent.getStyles();
        if (!(styles == null || styles.getBindingStyle() == null)) {
            Map<String, Object> renderBindingAttrs2 = renderBindingAttrs(styles.getBindingStyle(), arrayStack);
            Iterator<Map.Entry<String, Object>> it2 = renderBindingAttrs2.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry next2 = it2.next();
                Object value2 = next2.getValue();
                Object obj2 = styles.get((String) next2.getKey());
                if (value2 == null) {
                    if (obj2 == null) {
                        it2.remove();
                    }
                } else if (value2.equals(obj2)) {
                    it2.remove();
                }
            }
            if (renderBindingAttrs2.size() > 0) {
                wXComponent.updateNativeStyles(renderBindingAttrs2);
                if (isMainThread()) {
                    wXComponent.updateProperties(renderBindingAttrs2);
                }
            }
        }
        WXEvent events = wXComponent.getEvents();
        if (events != null && events.getEventBindingArgs() != null) {
            for (Map.Entry entry : events.getEventBindingArgs().entrySet()) {
                List<Object> bindingEventArgs = getBindingEventArgs(arrayStack, entry.getValue());
                if (bindingEventArgs != null) {
                    events.putEventBindingArgsValue((String) entry.getKey(), bindingEventArgs);
                }
            }
        }
    }

    public static Map<String, Object> renderBindingAttrs(ArrayMap arrayMap, ArrayStack arrayStack) {
        Set<Map.Entry> entrySet = arrayMap.entrySet();
        Map<String, Object> map = dynamicLocal.get();
        if (map == null) {
            map = new HashMap<>();
            dynamicLocal.set(map);
        }
        if (map.size() > 0) {
            map.clear();
        }
        for (Map.Entry entry : entrySet) {
            Object value = entry.getValue();
            String str = (String) entry.getKey();
            if (value instanceof JSONObject) {
                JSONObject jSONObject = (JSONObject) value;
                if (jSONObject.get(ELUtils.BINDING) instanceof Token) {
                    map.put(str, ((Token) jSONObject.get(ELUtils.BINDING)).execute(arrayStack));
                }
            }
            if (value instanceof JSONArray) {
                JSONArray jSONArray = (JSONArray) value;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < jSONArray.size(); i++) {
                    Object obj = jSONArray.get(i);
                    if (obj instanceof CharSequence) {
                        sb.append(obj);
                    } else if (obj instanceof JSONObject) {
                        JSONObject jSONObject2 = (JSONObject) obj;
                        if (jSONObject2.get(ELUtils.BINDING) instanceof Token) {
                            Object execute = ((Token) jSONObject2.get(ELUtils.BINDING)).execute(arrayStack);
                            if (execute == null) {
                                execute = "";
                            }
                            sb.append(execute);
                        }
                    }
                }
                String sb2 = sb.toString();
                if (sb2.length() > 256 && WXEnvironment.isApkDebugable()) {
                    WXLogUtils.w(WXRecyclerTemplateList.TAG, " warn too big string " + sb2);
                }
                map.put(str, sb2);
            }
        }
        return map;
    }

    public static Map<String, Object> renderProps(JSONObject jSONObject, ArrayStack arrayStack) {
        Set<Map.Entry<String, Object>> entrySet = jSONObject.entrySet();
        ArrayMap arrayMap = new ArrayMap(4);
        for (Map.Entry next : entrySet) {
            Object value = next.getValue();
            String str = (String) next.getKey();
            if (value instanceof JSONObject) {
                JSONObject jSONObject2 = (JSONObject) value;
                if (jSONObject2.get(ELUtils.BINDING) instanceof Token) {
                    arrayMap.put(str, ((Token) jSONObject2.get(ELUtils.BINDING)).execute(arrayStack));
                }
            }
            arrayMap.put(str, value);
        }
        return arrayMap;
    }

    public static List<Object> getBindingEventArgs(ArrayStack arrayStack, Object obj) {
        ArrayList arrayList = new ArrayList(4);
        if (obj instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray) obj;
            for (int i = 0; i < jSONArray.size(); i++) {
                Object obj2 = jSONArray.get(i);
                if (obj2 instanceof JSONObject) {
                    JSONObject jSONObject = (JSONObject) obj2;
                    if (jSONObject.get(ELUtils.BINDING) instanceof Token) {
                        arrayList.add(((Token) jSONObject.get(ELUtils.BINDING)).execute(arrayStack));
                    }
                }
                arrayList.add(obj2);
            }
        } else if (obj instanceof JSONObject) {
            JSONObject jSONObject2 = (JSONObject) obj;
            if (jSONObject2.get(ELUtils.BINDING) instanceof Token) {
                arrayList.add(((Token) jSONObject2.get(ELUtils.BINDING)).execute(arrayStack));
            } else {
                arrayList.add(obj.toString());
            }
        } else {
            arrayList.add(obj.toString());
        }
        return arrayList;
    }

    private static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static String getComponentId(WXComponent wXComponent) {
        if ((wXComponent instanceof WXCell) || wXComponent == null) {
            return null;
        }
        WXAttr attrs = wXComponent.getAttrs();
        if (attrs.get(ELUtils.IS_COMPONENT_ROOT) == null || !WXUtils.getBoolean(attrs.get(ELUtils.IS_COMPONENT_ROOT), false).booleanValue() || attrs.get(ELUtils.COMPONENT_PROPS) == null || !(attrs.get(ELUtils.COMPONENT_PROPS) instanceof JSONObject)) {
            return getComponentId(wXComponent.getParent());
        }
        Object obj = attrs.get(CellDataManager.VIRTUAL_COMPONENT_ID);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
