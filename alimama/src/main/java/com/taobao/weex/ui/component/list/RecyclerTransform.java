package com.taobao.weex.ui.component.list;

import java.util.regex.Pattern;

public class RecyclerTransform {
    private static final String TAG = "RecyclerTransform";
    public static final String TRANSFORM = "transform";
    private static final Pattern transformPattern = Pattern.compile("([a-z]+)\\(([0-9\\.]+),?([0-9\\.]+)?\\)");

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x006a A[Catch:{ NumberFormatException -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x006d A[Catch:{ NumberFormatException -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0077 A[Catch:{ NumberFormatException -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0081 A[Catch:{ NumberFormatException -> 0x00c4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0097 A[SYNTHETIC, Splitter:B:40:0x0097] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.recyclerview.widget.RecyclerView.ItemDecoration parseTransforms(int r16, java.lang.String r17) {
        /*
            r0 = r17
            if (r0 != 0) goto L_0x0006
            r0 = 0
            return r0
        L_0x0006:
            java.util.regex.Pattern r1 = transformPattern
            java.util.regex.Matcher r1 = r1.matcher(r0)
            r0 = 0
            r2 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
        L_0x0014:
            boolean r0 = r1.find()
            r3 = 1
            if (r0 == 0) goto L_0x00e2
            java.lang.String r4 = r1.group()
            java.lang.String r0 = r1.group(r3)
            r11 = -1
            int r12 = r0.hashCode()     // Catch:{ NumberFormatException -> 0x00c4 }
            r13 = -1267206133(0xffffffffb477f80b, float:-2.3093905E-7)
            r14 = 3
            r15 = 2
            if (r12 == r13) goto L_0x005c
            r13 = -925180581(0xffffffffc8dadd5b, float:-448234.84)
            if (r12 == r13) goto L_0x0052
            r13 = 109250890(0x683094a, float:4.929037E-35)
            if (r12 == r13) goto L_0x0048
            r13 = 1052832078(0x3ec0f14e, float:0.376841)
            if (r12 == r13) goto L_0x003f
            goto L_0x0066
        L_0x003f:
            java.lang.String r12 = "translate"
            boolean r0 = r0.equals(r12)     // Catch:{ NumberFormatException -> 0x00c4 }
            if (r0 == 0) goto L_0x0066
            goto L_0x0067
        L_0x0048:
            java.lang.String r3 = "scale"
            boolean r0 = r0.equals(r3)     // Catch:{ NumberFormatException -> 0x00c4 }
            if (r0 == 0) goto L_0x0066
            r3 = 0
            goto L_0x0067
        L_0x0052:
            java.lang.String r3 = "rotate"
            boolean r0 = r0.equals(r3)     // Catch:{ NumberFormatException -> 0x00c4 }
            if (r0 == 0) goto L_0x0066
            r3 = 3
            goto L_0x0067
        L_0x005c:
            java.lang.String r3 = "opacity"
            boolean r0 = r0.equals(r3)     // Catch:{ NumberFormatException -> 0x00c4 }
            if (r0 == 0) goto L_0x0066
            r3 = 2
            goto L_0x0067
        L_0x0066:
            r3 = -1
        L_0x0067:
            switch(r3) {
                case 0: goto L_0x0097;
                case 1: goto L_0x0081;
                case 2: goto L_0x0077;
                case 3: goto L_0x006d;
                default: goto L_0x006a;
            }     // Catch:{ NumberFormatException -> 0x00c4 }
        L_0x006a:
            java.lang.String r0 = "RecyclerTransform"
            goto L_0x00ae
        L_0x006d:
            java.lang.String r0 = r1.group(r15)     // Catch:{ NumberFormatException -> 0x00c4 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x00c4 }
            r8 = r0
            goto L_0x0014
        L_0x0077:
            java.lang.String r0 = r1.group(r15)     // Catch:{ NumberFormatException -> 0x00c4 }
            float r0 = java.lang.Float.parseFloat(r0)     // Catch:{ NumberFormatException -> 0x00c4 }
            r5 = r0
            goto L_0x0014
        L_0x0081:
            java.lang.String r0 = r1.group(r15)     // Catch:{ NumberFormatException -> 0x00c4 }
            int r3 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x00c4 }
            java.lang.String r0 = r1.group(r14)     // Catch:{ NumberFormatException -> 0x0094 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x0094 }
            r7 = r0
            r6 = r3
            goto L_0x0014
        L_0x0094:
            r0 = move-exception
            r6 = r3
            goto L_0x00c5
        L_0x0097:
            java.lang.String r0 = r1.group(r15)     // Catch:{ NumberFormatException -> 0x00c4 }
            float r3 = java.lang.Float.parseFloat(r0)     // Catch:{ NumberFormatException -> 0x00c4 }
            java.lang.String r0 = r1.group(r14)     // Catch:{ NumberFormatException -> 0x00ab }
            float r0 = java.lang.Float.parseFloat(r0)     // Catch:{ NumberFormatException -> 0x00ab }
            r10 = r0
            r9 = r3
            goto L_0x0014
        L_0x00ab:
            r0 = move-exception
            r9 = r3
            goto L_0x00c5
        L_0x00ae:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ NumberFormatException -> 0x00c4 }
            r3.<init>()     // Catch:{ NumberFormatException -> 0x00c4 }
            java.lang.String r11 = "Invaild transform expression:"
            r3.append(r11)     // Catch:{ NumberFormatException -> 0x00c4 }
            r3.append(r4)     // Catch:{ NumberFormatException -> 0x00c4 }
            java.lang.String r3 = r3.toString()     // Catch:{ NumberFormatException -> 0x00c4 }
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r3)     // Catch:{ NumberFormatException -> 0x00c4 }
            goto L_0x0014
        L_0x00c4:
            r0 = move-exception
        L_0x00c5:
            java.lang.String r3 = ""
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r3, (java.lang.Throwable) r0)
            java.lang.String r0 = "RecyclerTransform"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r11 = "Invaild transform expression:"
            r3.append(r11)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r3)
            goto L_0x0014
        L_0x00e2:
            com.taobao.weex.ui.view.listview.adapter.TransformItemDecoration r0 = new com.taobao.weex.ui.view.listview.adapter.TransformItemDecoration
            r1 = r16
            if (r1 != r3) goto L_0x00ea
            r4 = 1
            goto L_0x00eb
        L_0x00ea:
            r4 = 0
        L_0x00eb:
            r3 = r0
            r3.<init>(r4, r5, r6, r7, r8, r9, r10)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.list.RecyclerTransform.parseTransforms(int, java.lang.String):androidx.recyclerview.widget.RecyclerView$ItemDecoration");
    }
}
