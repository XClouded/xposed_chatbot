package com.taobao.android.dinamic.expressionv2;

import android.view.View;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.dinamic.DinamicEventHandler;
import com.taobao.android.dinamic.expression.parser.DinamicDataParser;
import com.taobao.android.dinamic.expression.parser.DinamicDataParserFactory;
import com.taobao.android.dinamic.expressionv2.DinamicASTNode;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import java.util.ArrayList;
import java.util.List;

public class DinamicMethodNode extends DinamicASTNode {
    private List middle;

    public List getMiddle() {
        return this.middle;
    }

    public void setMiddle(List list) {
        this.middle = list;
    }

    public DinamicASTNode.DinamicASTNodeType getType() {
        return DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeMethod;
    }

    public DinamicMethodNode() {
        this.type = DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeMethod;
    }

    public Object evaluate() {
        ArrayList arrayList = new ArrayList();
        int size = this.children.size();
        for (int i = 0; i < size; i++) {
            Object evaluate = ((DinamicASTNode) this.children.get(i)).evaluate();
            if (evaluate != null) {
                arrayList.add(evaluate);
            } else {
                arrayList.add(DinamicConstant.NL);
            }
        }
        DinamicDataParser parser = this.name != null ? DinamicDataParserFactory.getParser(this.name) : null;
        DinamicLog.print("MethodName:" + this.name);
        if (parser != null) {
            try {
                DinamicLog.print("args:" + arrayList.toString());
                return parser.evalWithArgs(arrayList, (DinamicParams) this.data);
            } catch (Throwable th) {
                DinamicLog.w("DinamicExpresstion", th, "parse express failed, parser=", parser.getClass().getName());
            }
        }
        return null;
    }

    public void handleEvent(View view) {
        DinamicLog.w("DinamicExpression handleEvent", new String[0]);
        if (this.children != null) {
            ArrayList arrayList = new ArrayList();
            int size = this.children.size();
            for (int i = 0; i < size; i++) {
                arrayList.add(((DinamicASTNode) this.children.get(i)).evaluate());
            }
            DinamicEventHandler dinamicEventHandler = null;
            if (this.name != null) {
                dinamicEventHandler = Dinamic.getEventHandler(this.name);
            }
            if (dinamicEventHandler != null) {
                try {
                    DinamicParams dinamicParams = (DinamicParams) this.data;
                    dinamicEventHandler.handleEvent(view, dinamicParams.getModule(), arrayList, dinamicParams.getOriginalData(), dinamicParams.getDinamicContext(), (ArrayList) view.getTag(DinamicTagKey.VIEW_PARAMS));
                } catch (Throwable th) {
                    DinamicLog.w("DinamicExpression", th, "parse express failed, parser=", dinamicEventHandler.getClass().getName());
                }
            }
        }
    }

    public void evaluateMidlle() {
        this.middle = new ArrayList();
        int size = this.children.size();
        for (int i = 0; i < size; i++) {
            this.middle.add(((DinamicASTNode) this.children.get(i)).evaluate());
        }
    }
}
