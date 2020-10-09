package com.alibaba.ut.page;

import android.app.Activity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class PageObjectMgr {
    public static String mPageDisAppearEventType;
    public static Stack<VirtualPageObject> mPageObjectStack = new Stack<>();

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00df  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.ut.page.VirtualPageObject getPageObject(android.content.Context r5, java.util.Map<java.lang.String, java.lang.String> r6) {
        /*
            r0 = 1
            r1 = 0
            r2 = 0
            if (r6 == 0) goto L_0x0168
            int r3 = r6.size()
            if (r3 <= 0) goto L_0x0168
            java.lang.String r3 = "caseName"
            java.lang.Object r3 = r6.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "isSPA"
            java.lang.Object r6 = r6.get(r4)     // Catch:{ Throwable -> 0x002a }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Throwable -> 0x002a }
            boolean r4 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x002a }
            if (r4 != 0) goto L_0x002a
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)     // Catch:{ Throwable -> 0x002a }
            boolean r6 = r6.booleanValue()     // Catch:{ Throwable -> 0x002a }
            goto L_0x002b
        L_0x002a:
            r6 = 0
        L_0x002b:
            if (r6 == 0) goto L_0x00df
            java.lang.String r4 = "isBack"
            boolean r4 = r4.equalsIgnoreCase(r3)
            if (r4 == 0) goto L_0x008c
            boolean r5 = isBackFromNativeOrBackground()
            if (r5 == 0) goto L_0x0058
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x004d
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            java.lang.Object r5 = r5.peek()
            com.alibaba.ut.page.VirtualPageObject r5 = (com.alibaba.ut.page.VirtualPageObject) r5
            goto L_0x0169
        L_0x004d:
            java.lang.Object[] r5 = new java.lang.Object[r0]
            java.lang.String r6 = "isBack but mPageObjectStack.isEmpty()"
            r5[r1] = r6
            com.alibaba.ut.utils.Logger.w(r2, r5)
            goto L_0x0168
        L_0x0058:
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x0066
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            r5.pop()
            goto L_0x006f
        L_0x0066:
            java.lang.Object[] r5 = new java.lang.Object[r0]
            java.lang.String r6 = "isBack but mPageObjectStack.isEmpty()"
            r5[r1] = r6
            com.alibaba.ut.utils.Logger.w(r2, r5)
        L_0x006f:
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x0081
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            java.lang.Object r5 = r5.peek()
            com.alibaba.ut.page.VirtualPageObject r5 = (com.alibaba.ut.page.VirtualPageObject) r5
            goto L_0x0169
        L_0x0081:
            java.lang.Object[] r5 = new java.lang.Object[r0]
            java.lang.String r6 = "isBack but mPageObjectStack.isEmpty()"
            r5[r1] = r6
            com.alibaba.ut.utils.Logger.w(r2, r5)
            goto L_0x0168
        L_0x008c:
            java.lang.String r4 = "isRefresh"
            boolean r4 = r4.equalsIgnoreCase(r3)
            if (r4 == 0) goto L_0x00b1
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x00a6
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            java.lang.Object r5 = r5.peek()
            com.alibaba.ut.page.VirtualPageObject r5 = (com.alibaba.ut.page.VirtualPageObject) r5
            goto L_0x0169
        L_0x00a6:
            java.lang.Object[] r5 = new java.lang.Object[r0]
            java.lang.String r6 = "isRefresh but mPageObjectStack.isEmpty()"
            r5[r1] = r6
            com.alibaba.ut.utils.Logger.w(r2, r5)
            goto L_0x0168
        L_0x00b1:
            java.lang.String r4 = "isForward"
            boolean r3 = r4.equalsIgnoreCase(r3)
            if (r3 == 0) goto L_0x0168
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r3 = mPageObjectStack
            boolean r3 = r3.isEmpty()
            if (r3 != 0) goto L_0x00ca
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r3 = mPageObjectStack
            java.lang.Object r3 = r3.peek()
            com.alibaba.ut.page.VirtualPageObject r3 = (com.alibaba.ut.page.VirtualPageObject) r3
            goto L_0x00cb
        L_0x00ca:
            r3 = r2
        L_0x00cb:
            if (r3 == 0) goto L_0x00d2
            boolean r4 = r3.isSPA
            if (r4 == 0) goto L_0x00d2
            goto L_0x00dc
        L_0x00d2:
            com.alibaba.ut.page.VirtualPageObject r3 = new com.alibaba.ut.page.VirtualPageObject
            r3.<init>(r6, r5)
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            r5.push(r3)
        L_0x00dc:
            r5 = r3
            goto L_0x0169
        L_0x00df:
            java.lang.String r6 = "isBack"
            boolean r6 = r6.equalsIgnoreCase(r3)
            if (r6 == 0) goto L_0x0131
            boolean r5 = isBackFromNativeOrBackground()
            if (r5 == 0) goto L_0x0109
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x00ff
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            java.lang.Object r5 = r5.peek()
            com.alibaba.ut.page.VirtualPageObject r5 = (com.alibaba.ut.page.VirtualPageObject) r5
            goto L_0x0169
        L_0x00ff:
            java.lang.Object[] r5 = new java.lang.Object[r0]
            java.lang.String r6 = "isBack but mPageObjectStack.isEmpty()"
            r5[r1] = r6
            com.alibaba.ut.utils.Logger.w(r2, r5)
            goto L_0x0168
        L_0x0109:
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x0116
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            r5.pop()
        L_0x0116:
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x0127
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            java.lang.Object r5 = r5.peek()
            com.alibaba.ut.page.VirtualPageObject r5 = (com.alibaba.ut.page.VirtualPageObject) r5
            goto L_0x0169
        L_0x0127:
            java.lang.Object[] r5 = new java.lang.Object[r0]
            java.lang.String r6 = "isBack but mPageObjectStack.isEmpty()"
            r5[r1] = r6
            com.alibaba.ut.utils.Logger.w(r2, r5)
            goto L_0x0168
        L_0x0131:
            java.lang.String r6 = "isRefresh"
            boolean r6 = r6.equalsIgnoreCase(r3)
            if (r6 == 0) goto L_0x0154
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x014a
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            java.lang.Object r5 = r5.peek()
            com.alibaba.ut.page.VirtualPageObject r5 = (com.alibaba.ut.page.VirtualPageObject) r5
            goto L_0x0169
        L_0x014a:
            java.lang.Object[] r5 = new java.lang.Object[r0]
            java.lang.String r6 = "isRefresh but mPageObjectStack.isEmpty()"
            r5[r1] = r6
            com.alibaba.ut.utils.Logger.w(r2, r5)
            goto L_0x0168
        L_0x0154:
            java.lang.String r6 = "isForward"
            boolean r6 = r6.equalsIgnoreCase(r3)
            if (r6 == 0) goto L_0x0168
            com.alibaba.ut.page.VirtualPageObject r6 = new com.alibaba.ut.page.VirtualPageObject
            r6.<init>(r1, r5)
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r5 = mPageObjectStack
            r5.push(r6)
            r5 = r6
            goto L_0x0169
        L_0x0168:
            r5 = r2
        L_0x0169:
            r6 = 4
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.String r3 = "page"
            r6[r1] = r3
            r6[r0] = r5
            r0 = 2
            java.lang.String r1 = "stack"
            r6[r0] = r1
            r0 = 3
            java.util.Stack<com.alibaba.ut.page.VirtualPageObject> r1 = mPageObjectStack
            r6[r0] = r1
            com.alibaba.ut.utils.Logger.e(r2, r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.page.PageObjectMgr.getPageObject(android.content.Context, java.util.Map):com.alibaba.ut.page.VirtualPageObject");
    }

    private static boolean isBackFromNativeOrBackground() {
        return "webViewPageHide".equalsIgnoreCase(mPageDisAppearEventType);
    }

    public static void clearPageObject(VirtualPageObject virtualPageObject) {
        if (virtualPageObject != null) {
            mPageObjectStack.remove(virtualPageObject);
        }
    }

    public static List<VirtualPageObject> getPageObject(Activity activity) {
        ArrayList arrayList = new ArrayList();
        Iterator it = mPageObjectStack.iterator();
        while (it.hasNext()) {
            VirtualPageObject virtualPageObject = (VirtualPageObject) it.next();
            if (virtualPageObject.mDelegateActivityHashcode == activity.hashCode()) {
                arrayList.add(virtualPageObject);
            }
        }
        return arrayList;
    }
}
