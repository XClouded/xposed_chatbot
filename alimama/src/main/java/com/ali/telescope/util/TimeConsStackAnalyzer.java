package com.ali.telescope.util;

import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class TimeConsStackAnalyzer {
    private long mLastTime;
    private LinkedList<String> mRecords = new LinkedList<>();
    private Node mRootNode = obtainNode(ProtocolConst.KEY_ROOT);
    private LinkedList<Node> mStack = new LinkedList<>();

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v11, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v0, resolved type: com.ali.telescope.util.TimeConsStackAnalyzer$Node} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void appendStack(java.lang.StackTraceElement[] r13, long r14) {
        /*
            r12 = this;
            if (r13 != 0) goto L_0x0003
            return
        L_0x0003:
            java.util.LinkedList<java.lang.String> r0 = r12.mRecords
            java.lang.String r1 = makeStackTrace(r13)
            r0.addLast(r1)
            long r0 = r12.mLastTime
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x0016
            r12.mLastTime = r14
        L_0x0016:
            long r0 = r12.mLastTime
            long r0 = r14 - r0
            r12.mLastTime = r14
            com.ali.telescope.util.TimeConsStackAnalyzer$Node r14 = r12.mRootNode
            long r2 = r14.cost
            long r2 = r2 + r0
            r14.cost = r2
            java.util.LinkedList<com.ali.telescope.util.TimeConsStackAnalyzer$Node> r14 = r12.mStack
            boolean r14 = r14.isEmpty()
            r15 = 0
            if (r14 == 0) goto L_0x0041
            int r14 = r13.length
        L_0x002d:
            if (r15 >= r14) goto L_0x00c0
            r0 = r13[r15]
            java.util.LinkedList<com.ali.telescope.util.TimeConsStackAnalyzer$Node> r1 = r12.mStack
            java.lang.String r0 = r12.generateProcName(r0)
            com.ali.telescope.util.TimeConsStackAnalyzer$Node r0 = obtainNode(r0)
            r1.addFirst(r0)
            int r15 = r15 + 1
            goto L_0x002d
        L_0x0041:
            r14 = 0
            java.util.LinkedList r2 = new java.util.LinkedList
            java.util.LinkedList<com.ali.telescope.util.TimeConsStackAnalyzer$Node> r3 = r12.mStack
            r2.<init>(r3)
            java.util.Iterator r8 = r2.iterator()
            int r2 = r13.length
            r9 = 1
            int r2 = r2 - r9
            r3 = r14
            r14 = r2
        L_0x0052:
            if (r14 < 0) goto L_0x00a6
            r2 = r13[r14]
            java.lang.String r10 = r12.generateProcName(r2)
            if (r15 != 0) goto L_0x009a
            boolean r2 = r8.hasNext()
            if (r2 == 0) goto L_0x008f
            java.lang.Object r2 = r8.next()
            r11 = r2
            com.ali.telescope.util.TimeConsStackAnalyzer$Node r11 = (com.ali.telescope.util.TimeConsStackAnalyzer.Node) r11
            java.lang.String r2 = r11.proc
            boolean r2 = r2.equals(r10)
            if (r2 == 0) goto L_0x0077
            long r2 = r11.cost
            long r2 = r2 + r0
            r11.cost = r2
            goto L_0x008d
        L_0x0077:
            java.util.LinkedList<com.ali.telescope.util.TimeConsStackAnalyzer$Node> r15 = r12.mStack
            r15.remove(r11)
            r2 = r12
            r4 = r11
            r5 = r8
            r6 = r0
            r2.foldStack(r3, r4, r5, r6)
            java.util.LinkedList<com.ali.telescope.util.TimeConsStackAnalyzer$Node> r15 = r12.mStack
            com.ali.telescope.util.TimeConsStackAnalyzer$Node r2 = obtainNode(r10)
            r15.addLast(r2)
            r15 = 1
        L_0x008d:
            r3 = r11
            goto L_0x00a3
        L_0x008f:
            java.util.LinkedList<com.ali.telescope.util.TimeConsStackAnalyzer$Node> r15 = r12.mStack
            com.ali.telescope.util.TimeConsStackAnalyzer$Node r2 = obtainNode(r10)
            r15.addLast(r2)
            r15 = 1
            goto L_0x00a3
        L_0x009a:
            java.util.LinkedList<com.ali.telescope.util.TimeConsStackAnalyzer$Node> r2 = r12.mStack
            com.ali.telescope.util.TimeConsStackAnalyzer$Node r4 = obtainNode(r10)
            r2.addLast(r4)
        L_0x00a3:
            int r14 = r14 + -1
            goto L_0x0052
        L_0x00a6:
            if (r15 != 0) goto L_0x00c0
            boolean r13 = r8.hasNext()
            if (r13 == 0) goto L_0x00c0
            java.lang.Object r13 = r8.next()
            r4 = r13
            com.ali.telescope.util.TimeConsStackAnalyzer$Node r4 = (com.ali.telescope.util.TimeConsStackAnalyzer.Node) r4
            java.util.LinkedList<com.ali.telescope.util.TimeConsStackAnalyzer$Node> r13 = r12.mStack
            r13.remove(r4)
            r2 = r12
            r5 = r8
            r6 = r0
            r2.foldStack(r3, r4, r5, r6)
        L_0x00c0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.util.TimeConsStackAnalyzer.appendStack(java.lang.StackTraceElement[], long):void");
    }

    public JSONObject export() {
        if (!this.mStack.isEmpty()) {
            Iterator it = this.mStack.iterator();
            it.remove();
            foldStack((Node) null, (Node) it.next(), it, 0);
        }
        if (this.mRootNode.subs.size() == 0) {
            return null;
        }
        return this.mRootNode.export(0, (String) null);
    }

    public void reset() {
        this.mLastTime = 0;
        this.mRootNode.subs.clear();
        this.mRootNode.cost = 0;
        this.mStack.clear();
        this.mRecords.clear();
    }

    private void foldStack(Node node, Node node2, Iterator<Node> it, long j) {
        if (node == null) {
            node = this.mRootNode;
        }
        node.subs.addLast(node2);
        node2.cost += j;
        while (it.hasNext()) {
            Node next = it.next();
            next.cost += j;
            this.mStack.remove(next);
            node2.subs.addLast(next);
            node2 = next;
        }
    }

    private String generateProcName(StackTraceElement stackTraceElement) {
        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + " at:" + stackTraceElement.getLineNumber();
    }

    public static final class Node {
        public long cost;
        String proc;
        LinkedList<Node> subs = new LinkedList<>();

        public JSONObject export(long j, String str) {
            String str2;
            if (str == null) {
                str2 = this.proc;
            } else {
                str2 = this.proc + "\n" + str;
            }
            if (str == null) {
                j = this.cost;
            }
            this.cost = j;
            if (this.subs.size() == 1) {
                return this.subs.getFirst().export(this.cost, str2);
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("cost", this.cost);
                jSONObject.put("proc", str2);
                if (!this.subs.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    Iterator it = this.subs.iterator();
                    while (it.hasNext()) {
                        jSONArray.put(((Node) it.next()).export(0, (String) null));
                    }
                    jSONObject.put("sub_procs", jSONArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jSONObject;
        }
    }

    private static Node obtainNode(String str) {
        Node node = new Node();
        node.cost = 0;
        node.proc = str;
        return node;
    }

    public static String makeStackTrace(StackTraceElement[] stackTraceElementArr) {
        if (stackTraceElementArr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTraceElementArr) {
            if (stackTraceElement != null) {
                sb.append(stackTraceElement.getClassName());
                sb.append(".");
                sb.append(stackTraceElement.getMethodName());
                sb.append(" at:");
                sb.append(stackTraceElement.getLineNumber());
                sb.append(10);
            }
        }
        return sb.toString();
    }
}
