package com.taobao.android.dinamic.expressionv2;

import android.util.LruCache;
import android.util.Pair;
import android.view.View;
import androidx.annotation.Nullable;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.dinamic.DinamicEventHandler;
import com.taobao.android.dinamic.expressionv2.DinamicASTNode;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DinamicProperty;
import com.taobao.android.dinamic.view.DinamicError;
import java.util.List;

public class ExpressionProcessor implements DinamicProcessor {
    private static LruCache<String, DinamicASTNode> expressionTreeCache = new LruCache<>(32);
    private DinamicParams dinamicParams;
    private String expression;
    private String viewIdentify;

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String str) {
        this.expression = str;
    }

    public ExpressionProcessor(String str, DinamicParams dinamicParams2) {
        this.expression = str;
        this.dinamicParams = dinamicParams2;
    }

    public ExpressionProcessor(String str, String str2, DinamicParams dinamicParams2) {
        this.expression = str;
        this.viewIdentify = str2;
        this.dinamicParams = dinamicParams2;
    }

    public Object process() {
        return process(this.expression, this.viewIdentify, this.dinamicParams);
    }

    public static Object process(String str, String str2, DinamicParams dinamicParams2) {
        DinamicASTNode dinamicASTNode = DinamicConstant.ISCACHE ? expressionTreeCache.get(str) : null;
        if (dinamicASTNode == null) {
            Pair<List, List> pair = new DinamicTokenizer().tokensWithExpr(str);
            if (pair != null) {
                DinamicASTBuilder dinamicASTBuilder = new DinamicASTBuilder();
                dinamicASTBuilder.setDinamicParams(dinamicParams2);
                DinamicASTNode parseWithTokens = dinamicASTBuilder.parseWithTokens(pair);
                if (parseWithTokens != null) {
                    if (DinamicConstant.ISCACHE) {
                        expressionTreeCache.put(str, parseWithTokens);
                    }
                    Object computeValue = computeValue(parseWithTokens, str, dinamicParams2);
                    if (computeValue != null) {
                        return computeValue;
                    }
                } else {
                    DinamicLog.print("build AST Tree error!");
                }
            } else {
                DinamicLog.print("token error!");
            }
        } else {
            dinamicASTNode.bindData(dinamicParams2);
            Object computeValue2 = computeValue(dinamicASTNode, str, dinamicParams2);
            if (computeValue2 == null || computeValue2 == DinamicConstant.NL) {
                return null;
            }
            return computeValue2;
        }
        return null;
    }

    @Nullable
    private static Object computeValue(DinamicASTNode dinamicASTNode, String str, DinamicParams dinamicParams2) {
        return dinamicASTNode.evaluate();
    }

    public static void handleEvent(View view, String str, DinamicParams dinamicParams2) {
        DinamicASTNode dinamicASTNode = DinamicConstant.ISCACHE ? expressionTreeCache.get(str) : null;
        if (dinamicASTNode == null) {
            Pair<List, List> pair = new DinamicTokenizer().tokensWithExpr(str);
            if (pair != null) {
                DinamicASTBuilder dinamicASTBuilder = new DinamicASTBuilder();
                dinamicASTBuilder.setDinamicParams(dinamicParams2);
                DinamicASTNode parseWithTokens = dinamicASTBuilder.parseWithTokens(pair);
                if (parseWithTokens != null) {
                    if (DinamicConstant.ISCACHE) {
                        expressionTreeCache.put(str, parseWithTokens);
                    }
                    handleEvent_(view, parseWithTokens);
                    return;
                }
                return;
            }
            return;
        }
        dinamicASTNode.bindData(dinamicParams2);
        handleEvent_(view, dinamicASTNode);
    }

    private static void handleEvent_(View view, DinamicASTNode dinamicASTNode) {
        if (dinamicASTNode == null) {
            return;
        }
        if (dinamicASTNode.type == DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeMethod) {
            try {
                ((DinamicMethodNode) dinamicASTNode).handleEvent(view);
            } catch (ClassCastException unused) {
                DinamicLog.e(Dinamic.TAG, "root node class cast error!");
            }
        } else if (dinamicASTNode.type == DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeSerialBlock) {
            int size = dinamicASTNode.children.size();
            for (int i = 0; i < size; i++) {
                DinamicASTNode dinamicASTNode2 = dinamicASTNode.children.get(i);
                if (dinamicASTNode2.type == DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeMethod) {
                    try {
                        ((DinamicMethodNode) dinamicASTNode2).handleEvent(view);
                    } catch (ClassCastException unused2) {
                        DinamicLog.e(Dinamic.TAG, "child node class cast error!");
                        return;
                    }
                }
            }
        }
    }

    public static DinamicASTNode[] getEventArray(View view, String str, DinamicParams dinamicParams2) {
        DinamicASTNode dinamicASTNode = DinamicConstant.ISCACHE ? expressionTreeCache.get(str) : null;
        if (dinamicASTNode == null) {
            Pair<List, List> pair = new DinamicTokenizer().tokensWithExpr(str);
            if (pair != null) {
                DinamicASTBuilder dinamicASTBuilder = new DinamicASTBuilder();
                dinamicASTBuilder.setDinamicParams(dinamicParams2);
                DinamicASTNode parseWithTokens = dinamicASTBuilder.parseWithTokens(pair);
                if (parseWithTokens != null) {
                    if (DinamicConstant.ISCACHE) {
                        expressionTreeCache.put(str, parseWithTokens);
                    }
                    return getEventArray_(view, parseWithTokens);
                }
            }
            return null;
        }
        dinamicASTNode.bindData(dinamicParams2);
        return getEventArray_(view, dinamicASTNode);
    }

    private static DinamicASTNode[] getEventArray_(View view, DinamicASTNode dinamicASTNode) {
        int size;
        if (dinamicASTNode != null) {
            if (dinamicASTNode.type == DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeMethod) {
                try {
                    ((DinamicMethodNode) dinamicASTNode).evaluateMidlle();
                    return new DinamicASTNode[]{dinamicASTNode};
                } catch (ClassCastException unused) {
                    DinamicLog.e(Dinamic.TAG, "root node class cast error!");
                    return null;
                }
            } else if (dinamicASTNode.type == DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeSerialBlock && (size = dinamicASTNode.children.size()) > 0) {
                for (int i = 0; i < size; i++) {
                    DinamicASTNode dinamicASTNode2 = dinamicASTNode.children.get(i);
                    if (dinamicASTNode2 instanceof DinamicMethodNode) {
                        ((DinamicMethodNode) dinamicASTNode2).evaluateMidlle();
                    }
                }
                return (DinamicASTNode[]) dinamicASTNode.children.toArray(new DinamicASTNode[size]);
            }
        }
        return null;
    }

    public static void handlePreEvent(View view, String str, DinamicParams dinamicParams2, DinamicProperty dinamicProperty) {
        try {
            DinamicASTNode[] eventArray = getEventArray(view, str, dinamicParams2);
            if (eventArray != null && eventArray.length > 0) {
                for (int i = 0; i < eventArray.length; i++) {
                    DinamicEventHandler eventHandler = Dinamic.getEventHandler(eventArray[i].name);
                    if (eventHandler != null && (eventArray[i] instanceof DinamicMethodNode)) {
                        eventHandler.prepareBindEvent(view, ((DinamicMethodNode) eventArray[i]).getMiddle(), dinamicParams2.getOriginalData());
                    }
                }
            }
        } catch (Throwable unused) {
            dinamicParams2.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_EVENT_HANDLER_EXCEPTION, dinamicProperty.viewIdentify);
        }
    }
}
