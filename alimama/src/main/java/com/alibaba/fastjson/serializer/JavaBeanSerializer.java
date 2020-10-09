package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.PropertyNamingStrategy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JavaBeanSerializer implements ObjectSerializer {
    private static final char[] false_chars = {'f', 'a', 'l', 's', 'e'};
    private static final char[] true_chars = {'t', 'r', 'u', 'e'};
    protected int features;
    private final FieldSerializer[] getters;
    private final FieldSerializer[] sortedGetters;
    protected final String typeKey;
    protected final String typeName;

    public JavaBeanSerializer(Class<?> cls) {
        this(cls, (PropertyNamingStrategy) null);
    }

    public JavaBeanSerializer(Class<?> cls, PropertyNamingStrategy propertyNamingStrategy) {
        this(cls, cls.getModifiers(), (Map<String, String>) null, false, true, true, true, propertyNamingStrategy);
    }

    public JavaBeanSerializer(Class<?> cls, String... strArr) {
        this(cls, cls.getModifiers(), map(strArr), false, true, true, true, (PropertyNamingStrategy) null);
    }

    private static Map<String, String> map(String... strArr) {
        HashMap hashMap = new HashMap();
        for (String str : strArr) {
            hashMap.put(str, str);
        }
        return hashMap;
    }

    /* JADX WARNING: type inference failed for: r16v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0086, code lost:
        r6 = r3.naming();
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public JavaBeanSerializer(java.lang.Class<?> r16, int r17, java.util.Map<java.lang.String, java.lang.String> r18, boolean r19, boolean r20, boolean r21, boolean r22, com.alibaba.fastjson.PropertyNamingStrategy r23) {
        /*
            r15 = this;
            r0 = r15
            r15.<init>()
            r1 = 0
            r0.features = r1
            r2 = 0
            if (r20 == 0) goto L_0x0015
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r3 = com.alibaba.fastjson.annotation.JSONType.class
            r13 = r16
            java.lang.annotation.Annotation r3 = r13.getAnnotation(r3)
            com.alibaba.fastjson.annotation.JSONType r3 = (com.alibaba.fastjson.annotation.JSONType) r3
            goto L_0x0018
        L_0x0015:
            r13 = r16
            r3 = r2
        L_0x0018:
            if (r3 == 0) goto L_0x0093
            com.alibaba.fastjson.serializer.SerializerFeature[] r4 = r3.serialzeFeatures()
            int r4 = com.alibaba.fastjson.serializer.SerializerFeature.of(r4)
            r0.features = r4
            java.lang.String r4 = r3.typeName()
            int r5 = r4.length()
            if (r5 != 0) goto L_0x0031
            r5 = r2
            r8 = r5
            goto L_0x0084
        L_0x0031:
            java.lang.Class r5 = r16.getSuperclass()
            r6 = r2
        L_0x0036:
            if (r5 == 0) goto L_0x0057
            java.lang.Class<java.lang.Object> r7 = java.lang.Object.class
            if (r5 == r7) goto L_0x0057
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r7 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r7 = r5.getAnnotation(r7)
            com.alibaba.fastjson.annotation.JSONType r7 = (com.alibaba.fastjson.annotation.JSONType) r7
            if (r7 != 0) goto L_0x0047
            goto L_0x0057
        L_0x0047:
            java.lang.String r6 = r7.typeKey()
            int r7 = r6.length()
            if (r7 == 0) goto L_0x0052
            goto L_0x0057
        L_0x0052:
            java.lang.Class r5 = r5.getSuperclass()
            goto L_0x0036
        L_0x0057:
            java.lang.Class[] r5 = r16.getInterfaces()
            int r7 = r5.length
            r8 = r6
            r6 = 0
        L_0x005e:
            if (r6 >= r7) goto L_0x007a
            r9 = r5[r6]
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r10 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r9 = r9.getAnnotation(r10)
            com.alibaba.fastjson.annotation.JSONType r9 = (com.alibaba.fastjson.annotation.JSONType) r9
            if (r9 == 0) goto L_0x0077
            java.lang.String r8 = r9.typeKey()
            int r9 = r8.length()
            if (r9 == 0) goto L_0x0077
            goto L_0x007a
        L_0x0077:
            int r6 = r6 + 1
            goto L_0x005e
        L_0x007a:
            if (r8 == 0) goto L_0x0083
            int r5 = r8.length()
            if (r5 != 0) goto L_0x0083
            r8 = r2
        L_0x0083:
            r5 = r4
        L_0x0084:
            if (r23 != 0) goto L_0x0090
            com.alibaba.fastjson.PropertyNamingStrategy r6 = r3.naming()
            com.alibaba.fastjson.PropertyNamingStrategy r7 = com.alibaba.fastjson.PropertyNamingStrategy.CamelCase
            if (r6 == r7) goto L_0x0090
            r14 = r6
            goto L_0x0097
        L_0x0090:
            r14 = r23
            goto L_0x0097
        L_0x0093:
            r14 = r23
            r5 = r2
            r8 = r5
        L_0x0097:
            r0.typeName = r5
            r0.typeKey = r8
            r9 = 0
            r4 = r16
            r5 = r17
            r6 = r19
            r7 = r3
            r8 = r18
            r10 = r21
            r11 = r22
            r12 = r14
            java.util.List r4 = com.alibaba.fastjson.util.TypeUtils.computeGetters(r4, r5, r6, r7, r8, r9, r10, r11, r12)
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            java.util.Iterator r4 = r4.iterator()
        L_0x00b7:
            boolean r6 = r4.hasNext()
            if (r6 == 0) goto L_0x00cc
            java.lang.Object r6 = r4.next()
            com.alibaba.fastjson.util.FieldInfo r6 = (com.alibaba.fastjson.util.FieldInfo) r6
            com.alibaba.fastjson.serializer.FieldSerializer r7 = new com.alibaba.fastjson.serializer.FieldSerializer
            r7.<init>(r6)
            r5.add(r7)
            goto L_0x00b7
        L_0x00cc:
            int r4 = r5.size()
            com.alibaba.fastjson.serializer.FieldSerializer[] r4 = new com.alibaba.fastjson.serializer.FieldSerializer[r4]
            java.lang.Object[] r4 = r5.toArray(r4)
            com.alibaba.fastjson.serializer.FieldSerializer[] r4 = (com.alibaba.fastjson.serializer.FieldSerializer[]) r4
            r0.getters = r4
            if (r3 == 0) goto L_0x00e0
            java.lang.String[] r2 = r3.orders()
        L_0x00e0:
            if (r2 == 0) goto L_0x0125
            int r2 = r2.length
            if (r2 == 0) goto L_0x0125
            r9 = 1
            r4 = r16
            r5 = r17
            r6 = r19
            r7 = r3
            r8 = r18
            r10 = r21
            r11 = r22
            r12 = r14
            java.util.List r1 = com.alibaba.fastjson.util.TypeUtils.computeGetters(r4, r5, r6, r7, r8, r9, r10, r11, r12)
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.util.Iterator r1 = r1.iterator()
        L_0x0101:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x0116
            java.lang.Object r3 = r1.next()
            com.alibaba.fastjson.util.FieldInfo r3 = (com.alibaba.fastjson.util.FieldInfo) r3
            com.alibaba.fastjson.serializer.FieldSerializer r4 = new com.alibaba.fastjson.serializer.FieldSerializer
            r4.<init>(r3)
            r2.add(r4)
            goto L_0x0101
        L_0x0116:
            int r1 = r2.size()
            com.alibaba.fastjson.serializer.FieldSerializer[] r1 = new com.alibaba.fastjson.serializer.FieldSerializer[r1]
            java.lang.Object[] r1 = r2.toArray(r1)
            com.alibaba.fastjson.serializer.FieldSerializer[] r1 = (com.alibaba.fastjson.serializer.FieldSerializer[]) r1
            r0.sortedGetters = r1
            goto L_0x0144
        L_0x0125:
            com.alibaba.fastjson.serializer.FieldSerializer[] r2 = r0.getters
            int r2 = r2.length
            com.alibaba.fastjson.serializer.FieldSerializer[] r2 = new com.alibaba.fastjson.serializer.FieldSerializer[r2]
            com.alibaba.fastjson.serializer.FieldSerializer[] r3 = r0.getters
            com.alibaba.fastjson.serializer.FieldSerializer[] r4 = r0.getters
            int r4 = r4.length
            java.lang.System.arraycopy(r3, r1, r2, r1, r4)
            java.util.Arrays.sort(r2)
            com.alibaba.fastjson.serializer.FieldSerializer[] r1 = r0.getters
            boolean r1 = java.util.Arrays.equals(r2, r1)
            if (r1 == 0) goto L_0x0142
            com.alibaba.fastjson.serializer.FieldSerializer[] r1 = r0.getters
            r0.sortedGetters = r1
            goto L_0x0144
        L_0x0142:
            r0.sortedGetters = r2
        L_0x0144:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JavaBeanSerializer.<init>(java.lang.Class, int, java.util.Map, boolean, boolean, boolean, boolean, com.alibaba.fastjson.PropertyNamingStrategy):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:320:0x0423, code lost:
        if (((java.lang.Boolean) r3).booleanValue() == false) goto L_0x0425;
     */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0148 A[SYNTHETIC, Splitter:B:105:0x0148] */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x015d A[Catch:{ Exception -> 0x064a, all -> 0x0646 }] */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x015f A[Catch:{ Exception -> 0x064a, all -> 0x0646 }] */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x0169 A[Catch:{ Exception -> 0x064a, all -> 0x0646 }] */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x016b A[Catch:{ Exception -> 0x064a, all -> 0x0646 }] */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x017e A[SYNTHETIC, Splitter:B:125:0x017e] */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x01d5 A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x01d6 A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x022e A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x025d A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x0279 A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x0280 A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x0286 A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:206:0x02b8 A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }, LOOP:4: B:204:0x02b2->B:206:0x02b8, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:380:0x04fe A[Catch:{ Exception -> 0x05d8, all -> 0x05d2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:432:0x05e7  */
    /* JADX WARNING: Removed duplicated region for block: B:443:0x060c A[SYNTHETIC, Splitter:B:443:0x060c] */
    /* JADX WARNING: Removed duplicated region for block: B:450:0x0625 A[SYNTHETIC, Splitter:B:450:0x0625] */
    /* JADX WARNING: Removed duplicated region for block: B:472:0x0654 A[SYNTHETIC, Splitter:B:472:0x0654] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00f7 A[Catch:{ Exception -> 0x00a5, all -> 0x00a0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x011b  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x011e  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0123 A[SYNTHETIC, Splitter:B:93:0x0123] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x013c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void write(com.alibaba.fastjson.serializer.JSONSerializer r39, java.lang.Object r40, java.lang.Object r41, java.lang.reflect.Type r42) throws java.io.IOException {
        /*
            r38 = this;
            r1 = r38
            r2 = r39
            r3 = r40
            r4 = r41
            r5 = r42
            com.alibaba.fastjson.serializer.SerializeWriter r6 = r2.out
            if (r3 != 0) goto L_0x0012
            r6.writeNull()
            return
        L_0x0012:
            com.alibaba.fastjson.serializer.SerialContext r7 = r2.context
            if (r7 == 0) goto L_0x0021
            com.alibaba.fastjson.serializer.SerialContext r7 = r2.context
            int r7 = r7.features
            com.alibaba.fastjson.serializer.SerializerFeature r8 = com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect
            int r8 = r8.mask
            r7 = r7 & r8
            if (r7 != 0) goto L_0x0031
        L_0x0021:
            java.util.IdentityHashMap<java.lang.Object, com.alibaba.fastjson.serializer.SerialContext> r7 = r2.references
            if (r7 == 0) goto L_0x0031
            java.util.IdentityHashMap<java.lang.Object, com.alibaba.fastjson.serializer.SerialContext> r7 = r2.references
            boolean r7 = r7.containsKey(r3)
            if (r7 == 0) goto L_0x0031
            r39.writeReference(r40)
            return
        L_0x0031:
            int r7 = r6.features
            com.alibaba.fastjson.serializer.SerializerFeature r8 = com.alibaba.fastjson.serializer.SerializerFeature.SortField
            int r8 = r8.mask
            r7 = r7 & r8
            if (r7 == 0) goto L_0x003d
            com.alibaba.fastjson.serializer.FieldSerializer[] r7 = r1.sortedGetters
            goto L_0x003f
        L_0x003d:
            com.alibaba.fastjson.serializer.FieldSerializer[] r7 = r1.getters
        L_0x003f:
            com.alibaba.fastjson.serializer.SerialContext r8 = r2.context
            int r9 = r6.features
            com.alibaba.fastjson.serializer.SerializerFeature r10 = com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect
            int r10 = r10.mask
            r9 = r9 & r10
            if (r9 != 0) goto L_0x0065
            com.alibaba.fastjson.serializer.SerialContext r9 = new com.alibaba.fastjson.serializer.SerialContext
            int r10 = r1.features
            r9.<init>(r8, r3, r4, r10)
            r2.context = r9
            java.util.IdentityHashMap<java.lang.Object, com.alibaba.fastjson.serializer.SerialContext> r9 = r2.references
            if (r9 != 0) goto L_0x005e
            java.util.IdentityHashMap r9 = new java.util.IdentityHashMap
            r9.<init>()
            r2.references = r9
        L_0x005e:
            java.util.IdentityHashMap<java.lang.Object, com.alibaba.fastjson.serializer.SerialContext> r9 = r2.references
            com.alibaba.fastjson.serializer.SerialContext r10 = r2.context
            r9.put(r3, r10)
        L_0x0065:
            int r9 = r1.features
            com.alibaba.fastjson.serializer.SerializerFeature r10 = com.alibaba.fastjson.serializer.SerializerFeature.BeanToArray
            int r10 = r10.mask
            r9 = r9 & r10
            r10 = 0
            r11 = 1
            if (r9 != 0) goto L_0x007c
            int r9 = r6.features
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.BeanToArray
            int r12 = r12.mask
            r9 = r9 & r12
            if (r9 == 0) goto L_0x007a
            goto L_0x007c
        L_0x007a:
            r9 = 0
            goto L_0x007d
        L_0x007c:
            r9 = 1
        L_0x007d:
            if (r9 == 0) goto L_0x0082
            r12 = 91
            goto L_0x0084
        L_0x0082:
            r12 = 123(0x7b, float:1.72E-43)
        L_0x0084:
            if (r9 == 0) goto L_0x0089
            r13 = 93
            goto L_0x008b
        L_0x0089:
            r13 = 125(0x7d, float:1.75E-43)
        L_0x008b:
            int r14 = r6.count     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            int r14 = r14 + r11
            char[] r15 = r6.buf     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            int r15 = r15.length     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            if (r14 <= r15) goto L_0x00aa
            java.io.Writer r15 = r6.writer     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            if (r15 != 0) goto L_0x009b
            r6.expandCapacity(r14)     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            goto L_0x00aa
        L_0x009b:
            r6.flush()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            r14 = 1
            goto L_0x00aa
        L_0x00a0:
            r0 = move-exception
            r4 = r0
            r3 = r8
            goto L_0x0670
        L_0x00a5:
            r0 = move-exception
            r4 = r0
            r3 = r8
            goto L_0x064d
        L_0x00aa:
            char[] r15 = r6.buf     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            int r11 = r6.count     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            r15[r11] = r12     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            r6.count = r14     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            int r11 = r7.length     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            if (r11 <= 0) goto L_0x00c4
            int r11 = r6.features     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            int r12 = r12.mask     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            r11 = r11 & r12
            if (r11 == 0) goto L_0x00c4
            r39.incrementIndent()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            r39.println()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
        L_0x00c4:
            int r11 = r1.features     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            int r12 = r12.mask     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            r11 = r11 & r12
            if (r11 != 0) goto L_0x00ee
            int r11 = r6.features     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            int r12 = r12.mask     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            r11 = r11 & r12
            if (r11 == 0) goto L_0x00ec
            if (r5 != 0) goto L_0x00ee
            int r11 = r6.features     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.NotWriteRootClassName     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            int r12 = r12.mask     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            r11 = r11 & r12
            if (r11 == 0) goto L_0x00ee
            com.alibaba.fastjson.serializer.SerialContext r11 = r2.context     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            if (r11 == 0) goto L_0x00ec
            com.alibaba.fastjson.serializer.SerialContext r11 = r2.context     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            com.alibaba.fastjson.serializer.SerialContext r11 = r11.parent     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            if (r11 == 0) goto L_0x00ec
            goto L_0x00ee
        L_0x00ec:
            r11 = 0
            goto L_0x00ef
        L_0x00ee:
            r11 = 1
        L_0x00ef:
            if (r11 == 0) goto L_0x0116
            java.lang.Class r11 = r40.getClass()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            if (r11 == r5) goto L_0x0116
            java.lang.String r5 = r1.typeKey     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            if (r5 == 0) goto L_0x00fe
            java.lang.String r5 = r1.typeKey     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            goto L_0x0102
        L_0x00fe:
            com.alibaba.fastjson.serializer.SerializeConfig r5 = r2.config     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            java.lang.String r5 = r5.typeKey     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
        L_0x0102:
            r6.writeFieldName(r5, r10)     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            java.lang.String r5 = r1.typeName     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            if (r5 != 0) goto L_0x0111
            java.lang.Class r5 = r40.getClass()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            java.lang.String r5 = r5.getName()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
        L_0x0111:
            r2.write((java.lang.String) r5)     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            r5 = 1
            goto L_0x0117
        L_0x0116:
            r5 = 0
        L_0x0117:
            r11 = 44
            if (r5 == 0) goto L_0x011e
            r5 = 44
            goto L_0x011f
        L_0x011e:
            r5 = 0
        L_0x011f:
            java.util.List<com.alibaba.fastjson.serializer.BeforeFilter> r12 = r2.beforeFilters     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            if (r12 == 0) goto L_0x013a
            java.util.List<com.alibaba.fastjson.serializer.BeforeFilter> r12 = r2.beforeFilters     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            java.util.Iterator r12 = r12.iterator()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
        L_0x0129:
            boolean r14 = r12.hasNext()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            if (r14 == 0) goto L_0x013a
            java.lang.Object r14 = r12.next()     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            com.alibaba.fastjson.serializer.BeforeFilter r14 = (com.alibaba.fastjson.serializer.BeforeFilter) r14     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            char r5 = r14.writeBefore(r2, r3, r5)     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            goto L_0x0129
        L_0x013a:
            if (r5 != r11) goto L_0x013e
            r5 = 1
            goto L_0x013f
        L_0x013e:
            r5 = 0
        L_0x013f:
            int r12 = r6.features     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            com.alibaba.fastjson.serializer.SerializerFeature r14 = com.alibaba.fastjson.serializer.SerializerFeature.QuoteFieldNames     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            int r14 = r14.mask     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            r12 = r12 & r14
            if (r12 == 0) goto L_0x0153
            int r12 = r6.features     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            com.alibaba.fastjson.serializer.SerializerFeature r14 = com.alibaba.fastjson.serializer.SerializerFeature.UseSingleQuotes     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            int r14 = r14.mask     // Catch:{ Exception -> 0x00a5, all -> 0x00a0 }
            r12 = r12 & r14
            if (r12 != 0) goto L_0x0153
            r12 = 1
            goto L_0x0154
        L_0x0153:
            r12 = 0
        L_0x0154:
            int r14 = r6.features     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            com.alibaba.fastjson.serializer.SerializerFeature r15 = com.alibaba.fastjson.serializer.SerializerFeature.UseSingleQuotes     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            int r15 = r15.mask     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            r14 = r14 & r15
            if (r14 == 0) goto L_0x015f
            r14 = 1
            goto L_0x0160
        L_0x015f:
            r14 = 0
        L_0x0160:
            int r15 = r6.features     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            com.alibaba.fastjson.serializer.SerializerFeature r11 = com.alibaba.fastjson.serializer.SerializerFeature.NotWriteDefaultValue     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            int r11 = r11.mask     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            r11 = r11 & r15
            if (r11 == 0) goto L_0x016b
            r11 = 1
            goto L_0x016c
        L_0x016b:
            r11 = 0
        L_0x016c:
            java.util.List<com.alibaba.fastjson.serializer.PropertyFilter> r15 = r2.propertyFilters     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            java.util.List<com.alibaba.fastjson.serializer.NameFilter> r10 = r2.nameFilters     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            r17 = r5
            java.util.List<com.alibaba.fastjson.serializer.ValueFilter> r5 = r2.valueFilters     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            java.util.List<com.alibaba.fastjson.serializer.PropertyPreFilter> r4 = r2.propertyPreFilters     // Catch:{ Exception -> 0x064a, all -> 0x0646 }
            r18 = r8
            r19 = r13
            r8 = 0
        L_0x017b:
            int r13 = r7.length     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            if (r8 >= r13) goto L_0x05de
            r13 = r7[r8]     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r20 = r7
            com.alibaba.fastjson.util.FieldInfo r7 = r13.fieldInfo     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r21 = r8
            java.lang.Class<?> r8 = r7.fieldClass     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r22 = r14
            java.lang.String r14 = r7.name     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r23 = r12
            int r12 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r24 = r11
            com.alibaba.fastjson.serializer.SerializerFeature r11 = com.alibaba.fastjson.serializer.SerializerFeature.SkipTransientField     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r11 = r11.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r11 = r11 & r12
            if (r11 == 0) goto L_0x01ab
            java.lang.reflect.Field r11 = r7.field     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r11 == 0) goto L_0x01ab
            boolean r11 = r7.fieldTransient     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r11 == 0) goto L_0x01ab
        L_0x01a1:
            r30 = r4
            r36 = r5
            r33 = r10
            r32 = r15
            goto L_0x0425
        L_0x01ab:
            java.lang.String r11 = r1.typeKey     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r11 == 0) goto L_0x01b8
            java.lang.String r11 = r1.typeKey     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            boolean r11 = r11.equals(r14)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r11 == 0) goto L_0x01b8
            goto L_0x01a1
        L_0x01b8:
            if (r4 == 0) goto L_0x01d2
            java.util.Iterator r11 = r4.iterator()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x01be:
            boolean r12 = r11.hasNext()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r12 == 0) goto L_0x01d2
            java.lang.Object r12 = r11.next()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.PropertyPreFilter r12 = (com.alibaba.fastjson.serializer.PropertyPreFilter) r12     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            boolean r12 = r12.apply(r2, r3, r14)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r12 != 0) goto L_0x01be
            r11 = 0
            goto L_0x01d3
        L_0x01d2:
            r11 = 1
        L_0x01d3:
            if (r11 != 0) goto L_0x01d6
            goto L_0x01a1
        L_0x01d6:
            r11 = 0
            r25 = 0
            boolean r12 = r7.fieldAccess     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r12 == 0) goto L_0x021e
            java.lang.Class r12 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r12) goto L_0x01f1
            java.lang.reflect.Field r12 = r7.field     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r12 = r12.getInt(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r29 = r11
            r27 = r25
            r11 = 1
        L_0x01ec:
            r25 = 0
            r26 = 0
            goto L_0x022c
        L_0x01f1:
            java.lang.Class r12 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r12) goto L_0x0202
            java.lang.reflect.Field r12 = r7.field     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            long r25 = r12.getLong(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r29 = r11
            r27 = r25
            r11 = 1
            r12 = 0
            goto L_0x01ec
        L_0x0202:
            java.lang.Class r12 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r12) goto L_0x0217
            java.lang.reflect.Field r12 = r7.field     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            boolean r12 = r12.getBoolean(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r29 = r11
            r27 = r25
            r11 = 1
            r26 = 0
            r25 = r12
            r12 = 0
            goto L_0x022c
        L_0x0217:
            java.lang.reflect.Field r11 = r7.field     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            java.lang.Object r11 = r11.get(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0222
        L_0x021e:
            java.lang.Object r11 = r13.getPropertyValue(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x0222:
            r29 = r11
            r27 = r25
            r11 = 0
            r12 = 0
            r25 = 0
            r26 = 1
        L_0x022c:
            if (r15 == 0) goto L_0x0279
            if (r11 == 0) goto L_0x024f
            r30 = r4
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x023d
            java.lang.Integer r4 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x023a:
            r26 = 1
            goto L_0x0253
        L_0x023d:
            java.lang.Class r4 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x0246
            java.lang.Long r4 = java.lang.Long.valueOf(r27)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x023a
        L_0x0246:
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x0251
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r25)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x023a
        L_0x024f:
            r30 = r4
        L_0x0251:
            r4 = r29
        L_0x0253:
            java.util.Iterator r29 = r15.iterator()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x0257:
            boolean r31 = r29.hasNext()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r31 == 0) goto L_0x0274
            java.lang.Object r31 = r29.next()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r32 = r15
            r15 = r31
            com.alibaba.fastjson.serializer.PropertyFilter r15 = (com.alibaba.fastjson.serializer.PropertyFilter) r15     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            boolean r15 = r15.apply(r3, r14, r4)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r15 != 0) goto L_0x0271
            r29 = r4
            r4 = 0
            goto L_0x027e
        L_0x0271:
            r15 = r32
            goto L_0x0257
        L_0x0274:
            r32 = r15
            r29 = r4
            goto L_0x027d
        L_0x0279:
            r30 = r4
            r32 = r15
        L_0x027d:
            r4 = 1
        L_0x027e:
            if (r4 != 0) goto L_0x0286
            r36 = r5
            r33 = r10
            goto L_0x0425
        L_0x0286:
            if (r10 == 0) goto L_0x02cc
            if (r11 == 0) goto L_0x02a9
            if (r26 != 0) goto L_0x02a9
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x0297
            java.lang.Integer r4 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x0294:
            r26 = 1
            goto L_0x02ab
        L_0x0297:
            java.lang.Class r4 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x02a0
            java.lang.Long r4 = java.lang.Long.valueOf(r27)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0294
        L_0x02a0:
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x02a9
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r25)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0294
        L_0x02a9:
            r4 = r29
        L_0x02ab:
            java.util.Iterator r15 = r10.iterator()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r33 = r10
            r10 = r14
        L_0x02b2:
            boolean r29 = r15.hasNext()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r29 == 0) goto L_0x02c9
            java.lang.Object r29 = r15.next()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r34 = r15
            r15 = r29
            com.alibaba.fastjson.serializer.NameFilter r15 = (com.alibaba.fastjson.serializer.NameFilter) r15     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            java.lang.String r10 = r15.process(r3, r10, r4)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r15 = r34
            goto L_0x02b2
        L_0x02c9:
            r29 = r4
            goto L_0x02cf
        L_0x02cc:
            r33 = r10
            r10 = r14
        L_0x02cf:
            if (r5 == 0) goto L_0x0314
            if (r11 == 0) goto L_0x02f2
            if (r26 != 0) goto L_0x02f2
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x02e0
            java.lang.Integer r29 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x02dd:
            r26 = 1
            goto L_0x02f2
        L_0x02e0:
            java.lang.Class r4 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x02e9
            java.lang.Long r29 = java.lang.Long.valueOf(r27)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x02dd
        L_0x02e9:
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r4) goto L_0x02f2
            java.lang.Boolean r29 = java.lang.Boolean.valueOf(r25)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x02dd
        L_0x02f2:
            java.util.Iterator r4 = r5.iterator()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r15 = r29
        L_0x02f8:
            boolean r31 = r4.hasNext()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r31 == 0) goto L_0x030f
            java.lang.Object r31 = r4.next()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r35 = r4
            r4 = r31
            com.alibaba.fastjson.serializer.ValueFilter r4 = (com.alibaba.fastjson.serializer.ValueFilter) r4     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            java.lang.Object r15 = r4.process(r3, r14, r15)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r4 = r35
            goto L_0x02f8
        L_0x030f:
            r4 = r29
            r29 = r15
            goto L_0x0316
        L_0x0314:
            r4 = r29
        L_0x0316:
            if (r26 == 0) goto L_0x03e2
            if (r29 != 0) goto L_0x03e2
            int r15 = r7.serialzeFeatures     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r36 = r5
            int r5 = r1.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r5 = r5 | r15
            int r15 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r5 = r5 | r15
            java.lang.Class<java.lang.Boolean> r15 = java.lang.Boolean.class
            if (r8 != r15) goto L_0x0352
            com.alibaba.fastjson.serializer.SerializerFeature r15 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullBooleanAsFalse     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r15 = r15.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r3 = com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r3 = r3.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 | r15
            if (r9 != 0) goto L_0x0340
            r31 = r5 & r3
            if (r31 != 0) goto L_0x0340
            r37 = r7
            int r7 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 & r7
            if (r3 != 0) goto L_0x0342
            goto L_0x0425
        L_0x0340:
            r37 = r7
        L_0x0342:
            r3 = r5 & r15
            if (r3 != 0) goto L_0x034b
            int r3 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 & r15
            if (r3 == 0) goto L_0x03e6
        L_0x034b:
            r3 = 0
            java.lang.Boolean r29 = java.lang.Boolean.valueOf(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x03e6
        L_0x0352:
            r37 = r7
            java.lang.Class<java.lang.String> r3 = java.lang.String.class
            if (r8 != r3) goto L_0x037a
            com.alibaba.fastjson.serializer.SerializerFeature r3 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r3 = r3.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r7 = com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r7 = r7.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = r7 | r3
            if (r9 != 0) goto L_0x036e
            r15 = r5 & r7
            if (r15 != 0) goto L_0x036e
            int r15 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = r7 & r15
            if (r7 != 0) goto L_0x036e
            goto L_0x0425
        L_0x036e:
            r5 = r5 & r3
            if (r5 != 0) goto L_0x0376
            int r5 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 & r5
            if (r3 == 0) goto L_0x03e6
        L_0x0376:
            java.lang.String r29 = ""
            goto L_0x03e6
        L_0x037a:
            java.lang.Class<java.lang.Number> r3 = java.lang.Number.class
            boolean r3 = r3.isAssignableFrom(r8)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r3 == 0) goto L_0x03a6
            com.alibaba.fastjson.serializer.SerializerFeature r3 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r3 = r3.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r7 = com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r7 = r7.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = r7 | r3
            if (r9 != 0) goto L_0x0398
            r15 = r5 & r7
            if (r15 != 0) goto L_0x0398
            int r15 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = r7 & r15
            if (r7 != 0) goto L_0x0398
            goto L_0x0425
        L_0x0398:
            r5 = r5 & r3
            if (r5 != 0) goto L_0x03a0
            int r5 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 & r5
            if (r3 == 0) goto L_0x03e6
        L_0x03a0:
            r3 = 0
            java.lang.Integer r29 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x03e6
        L_0x03a6:
            java.lang.Class<java.util.Collection> r3 = java.util.Collection.class
            boolean r3 = r3.isAssignableFrom(r8)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r3 == 0) goto L_0x03d3
            com.alibaba.fastjson.serializer.SerializerFeature r3 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r3 = r3.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r7 = com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r7 = r7.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = r7 | r3
            if (r9 != 0) goto L_0x03c4
            r15 = r5 & r7
            if (r15 != 0) goto L_0x03c4
            int r15 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = r7 & r15
            if (r7 != 0) goto L_0x03c4
            goto L_0x0425
        L_0x03c4:
            r5 = r5 & r3
            if (r5 != 0) goto L_0x03cc
            int r5 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 & r5
            if (r3 == 0) goto L_0x03e6
        L_0x03cc:
            java.util.List r3 = java.util.Collections.emptyList()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r29 = r3
            goto L_0x03e6
        L_0x03d3:
            if (r9 != 0) goto L_0x03e6
            boolean r3 = r13.writeNull     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r3 != 0) goto L_0x03e6
            com.alibaba.fastjson.serializer.SerializerFeature r3 = com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            boolean r3 = r6.isEnabled(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r3 != 0) goto L_0x03e6
            goto L_0x0425
        L_0x03e2:
            r36 = r5
            r37 = r7
        L_0x03e6:
            r3 = r29
            if (r26 == 0) goto L_0x042a
            if (r3 == 0) goto L_0x042a
            if (r24 == 0) goto L_0x042a
            java.lang.Class r5 = java.lang.Byte.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 == r5) goto L_0x0406
            java.lang.Class r5 = java.lang.Short.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 == r5) goto L_0x0406
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 == r5) goto L_0x0406
            java.lang.Class r5 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 == r5) goto L_0x0406
            java.lang.Class r5 = java.lang.Float.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 == r5) goto L_0x0406
            java.lang.Class r5 = java.lang.Double.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r5) goto L_0x0414
        L_0x0406:
            boolean r5 = r3 instanceof java.lang.Number     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r5 == 0) goto L_0x0414
            r5 = r3
            java.lang.Number r5 = (java.lang.Number) r5     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            byte r5 = r5.byteValue()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r5 != 0) goto L_0x0414
            goto L_0x0425
        L_0x0414:
            java.lang.Class r5 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r5) goto L_0x042a
            boolean r5 = r3 instanceof java.lang.Boolean     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r5 == 0) goto L_0x042a
            r5 = r3
            java.lang.Boolean r5 = (java.lang.Boolean) r5     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            boolean r5 = r5.booleanValue()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r5 != 0) goto L_0x042a
        L_0x0425:
            r4 = 0
            r16 = 44
            goto L_0x05bc
        L_0x042a:
            if (r17 == 0) goto L_0x0458
            int r5 = r6.count     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = 1
            int r5 = r5 + r7
            char[] r7 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r7 = r7.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r5 <= r7) goto L_0x0441
            java.io.Writer r7 = r6.writer     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r7 != 0) goto L_0x043d
            r6.expandCapacity(r5)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0441
        L_0x043d:
            r6.flush()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r5 = 1
        L_0x0441:
            char[] r7 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r15 = r6.count     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r16 = 44
            r7[r15] = r16     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r6.count = r5     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r7 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r7 = r7.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r5 = r5 & r7
            if (r5 == 0) goto L_0x045a
            r39.println()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x045a
        L_0x0458:
            r16 = 44
        L_0x045a:
            if (r10 == r14) goto L_0x0468
            if (r9 != 0) goto L_0x0462
            r4 = 1
            r6.writeFieldName(r10, r4)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x0462:
            r2.write((java.lang.Object) r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x0465:
            r4 = 0
            goto L_0x05ba
        L_0x0468:
            if (r4 == r3) goto L_0x0473
            if (r9 != 0) goto L_0x046f
            r13.writePrefix(r2)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x046f:
            r2.write((java.lang.Object) r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x0473:
            if (r9 != 0) goto L_0x04ba
            if (r23 == 0) goto L_0x04b7
            char[] r4 = r13.name_chars     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r4.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r7 = r6.count     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r7 = r7 + r5
            char[] r10 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r10 = r10.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r7 <= r10) goto L_0x04aa
            java.io.Writer r10 = r6.writer     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r10 != 0) goto L_0x048a
            r6.expandCapacity(r7)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x04aa
        L_0x048a:
            r7 = r5
            r5 = 0
        L_0x048c:
            char[] r10 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r10 = r10.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r14 = r6.count     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r10 = r10 - r14
            char[] r14 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r15 = r6.count     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            java.lang.System.arraycopy(r4, r5, r14, r15, r10)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            char[] r14 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r14 = r14.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r6.count = r14     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r6.flush()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r7 = r7 - r10
            int r5 = r5 + r10
            char[] r10 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r10 = r10.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r7 > r10) goto L_0x048c
            r10 = r7
            goto L_0x04ad
        L_0x04aa:
            r10 = r7
            r7 = r5
            r5 = 0
        L_0x04ad:
            char[] r14 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r15 = r6.count     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            java.lang.System.arraycopy(r4, r5, r14, r15, r7)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r6.count = r10     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x04ba
        L_0x04b7:
            r13.writePrefix(r2)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x04ba:
            if (r11 == 0) goto L_0x0538
            if (r26 != 0) goto L_0x0538
            java.lang.Class r3 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r3) goto L_0x050b
            r3 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r12 != r3) goto L_0x04cc
            java.lang.String r3 = "-2147483648"
            r6.write((java.lang.String) r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x04cc:
            if (r12 >= 0) goto L_0x04d0
            int r3 = -r12
            goto L_0x04d1
        L_0x04d0:
            r3 = r12
        L_0x04d1:
            r4 = 0
        L_0x04d2:
            int[] r5 = com.alibaba.fastjson.serializer.SerializeWriter.sizeTable     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r5 = r5[r4]     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r3 > r5) goto L_0x0508
            int r4 = r4 + 1
            if (r12 >= 0) goto L_0x04de
            int r4 = r4 + 1
        L_0x04de:
            int r3 = r6.count     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r3 = r3 + r4
            char[] r5 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r5.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r3 <= r5) goto L_0x04fb
            java.io.Writer r5 = r6.writer     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r5 != 0) goto L_0x04ee
            r6.expandCapacity(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x04fb
        L_0x04ee:
            char[] r5 = new char[r4]     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            long r7 = (long) r12     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializeWriter.getChars(r7, r4, r5)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r4 = r5.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = 0
            r6.write((char[]) r5, (int) r7, (int) r4)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r4 = 1
            goto L_0x04fc
        L_0x04fb:
            r4 = 0
        L_0x04fc:
            if (r4 != 0) goto L_0x0465
            long r4 = (long) r12     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            char[] r7 = r6.buf     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializeWriter.getChars(r4, r3, r7)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r6.count = r3     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x0508:
            int r4 = r4 + 1
            goto L_0x04d2
        L_0x050b:
            java.lang.Class r3 = java.lang.Long.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r3) goto L_0x0518
            com.alibaba.fastjson.serializer.SerializeWriter r3 = r2.out     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r4 = r27
            r3.writeLong(r4)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x0518:
            java.lang.Class r3 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r8 != r3) goto L_0x0465
            if (r25 == 0) goto L_0x052b
            com.alibaba.fastjson.serializer.SerializeWriter r3 = r2.out     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            char[] r4 = true_chars     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            char[] r5 = true_chars     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r5.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = 0
            r3.write((char[]) r4, (int) r7, (int) r5)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x052b:
            com.alibaba.fastjson.serializer.SerializeWriter r3 = r2.out     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            char[] r4 = false_chars     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            char[] r5 = false_chars     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r5.length     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = 0
            r3.write((char[]) r4, (int) r7, (int) r5)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x0538:
            if (r9 != 0) goto L_0x05b6
            java.lang.Class<java.lang.String> r4 = java.lang.String.class
            if (r8 != r4) goto L_0x0572
            int r4 = r13.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r1.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r4 = r4 | r5
            if (r3 != 0) goto L_0x0562
            int r3 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r5 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r5.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 & r5
            if (r3 != 0) goto L_0x055b
            com.alibaba.fastjson.serializer.SerializerFeature r3 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r3 = r3.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 & r4
            if (r3 == 0) goto L_0x0556
            goto L_0x055b
        L_0x0556:
            r6.writeNull()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x055b:
            java.lang.String r3 = ""
            r6.writeString(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x0562:
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r22 == 0) goto L_0x056b
            r6.writeStringWithSingleQuote(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x056b:
            r4 = 1
            r5 = 0
            r6.writeStringWithDoubleQuote(r3, r5, r4)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x0572:
            r4 = r37
            boolean r4 = r4.isEnum     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r4 == 0) goto L_0x05b1
            if (r3 == 0) goto L_0x05ac
            int r4 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r5 = com.alibaba.fastjson.serializer.SerializerFeature.WriteEnumUsingToString     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r5.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r4 = r4 & r5
            if (r4 == 0) goto L_0x05a1
            java.lang.Enum r3 = (java.lang.Enum) r3     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r4 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r5 = com.alibaba.fastjson.serializer.SerializerFeature.UseSingleQuotes     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r5 = r5.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r4 = r4 & r5
            if (r4 == 0) goto L_0x0594
            r4 = 1
            goto L_0x0595
        L_0x0594:
            r4 = 0
        L_0x0595:
            if (r4 == 0) goto L_0x059c
            r6.writeStringWithSingleQuote(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0465
        L_0x059c:
            r4 = 0
            r6.writeStringWithDoubleQuote(r3, r4, r4)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x05ba
        L_0x05a1:
            r4 = 0
            java.lang.Enum r3 = (java.lang.Enum) r3     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r3 = r3.ordinal()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r6.writeInt(r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x05ba
        L_0x05ac:
            r4 = 0
            r6.writeNull()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x05ba
        L_0x05b1:
            r4 = 0
            r13.writeValue(r2, r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x05ba
        L_0x05b6:
            r4 = 0
            r13.writeValue(r2, r3)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x05ba:
            r17 = 1
        L_0x05bc:
            int r8 = r21 + 1
            r7 = r20
            r14 = r22
            r12 = r23
            r11 = r24
            r4 = r30
            r15 = r32
            r10 = r33
            r5 = r36
            r3 = r40
            goto L_0x017b
        L_0x05d2:
            r0 = move-exception
            r4 = r0
            r3 = r18
            goto L_0x0670
        L_0x05d8:
            r0 = move-exception
            r4 = r0
            r3 = r18
            goto L_0x064d
        L_0x05de:
            r20 = r7
            r4 = 0
            r16 = 44
            java.util.List<com.alibaba.fastjson.serializer.AfterFilter> r3 = r2.afterFilters     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            if (r3 == 0) goto L_0x0607
            if (r17 == 0) goto L_0x05ea
            goto L_0x05ec
        L_0x05ea:
            r16 = 0
        L_0x05ec:
            java.util.List<com.alibaba.fastjson.serializer.AfterFilter> r3 = r2.afterFilters     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r4 = r16
        L_0x05f4:
            boolean r5 = r3.hasNext()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r5 == 0) goto L_0x0607
            java.lang.Object r5 = r3.next()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.AfterFilter r5 = (com.alibaba.fastjson.serializer.AfterFilter) r5     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r7 = r40
            char r4 = r5.writeAfter(r2, r7, r4)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x05f4
        L_0x0607:
            r7 = r20
            int r3 = r7.length     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            if (r3 <= 0) goto L_0x061b
            int r3 = r6.features     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            int r4 = r4.mask     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r3 = r3 & r4
            if (r3 == 0) goto L_0x061b
            r39.decrementIdent()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r39.println()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
        L_0x061b:
            int r3 = r6.count     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            r7 = 1
            int r11 = r3 + 1
            char[] r3 = r6.buf     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            int r3 = r3.length     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            if (r11 <= r3) goto L_0x0631
            java.io.Writer r3 = r6.writer     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            if (r3 != 0) goto L_0x062d
            r6.expandCapacity(r11)     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            goto L_0x0631
        L_0x062d:
            r6.flush()     // Catch:{ Exception -> 0x05d8, all -> 0x05d2 }
            r11 = 1
        L_0x0631:
            char[] r3 = r6.buf     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            int r4 = r6.count     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            r3[r4] = r19     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            r6.count = r11     // Catch:{ Exception -> 0x0642, all -> 0x063e }
            r3 = r18
            r2.context = r3
            return
        L_0x063e:
            r0 = move-exception
            r3 = r18
            goto L_0x0648
        L_0x0642:
            r0 = move-exception
            r3 = r18
            goto L_0x064c
        L_0x0646:
            r0 = move-exception
            r3 = r8
        L_0x0648:
            r4 = r0
            goto L_0x0670
        L_0x064a:
            r0 = move-exception
            r3 = r8
        L_0x064c:
            r4 = r0
        L_0x064d:
            java.lang.String r5 = "write javaBean error, fastjson version 1.1.71"
            r6 = r41
            if (r6 == 0) goto L_0x0668
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x066e }
            r7.<init>()     // Catch:{ all -> 0x066e }
            r7.append(r5)     // Catch:{ all -> 0x066e }
            java.lang.String r5 = ", fieldName : "
            r7.append(r5)     // Catch:{ all -> 0x066e }
            r7.append(r6)     // Catch:{ all -> 0x066e }
            java.lang.String r5 = r7.toString()     // Catch:{ all -> 0x066e }
        L_0x0668:
            com.alibaba.fastjson.JSONException r6 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x066e }
            r6.<init>(r5, r4)     // Catch:{ all -> 0x066e }
            throw r6     // Catch:{ all -> 0x066e }
        L_0x066e:
            r0 = move-exception
            goto L_0x0648
        L_0x0670:
            r2.context = r3
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JavaBeanSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type):void");
    }

    public Map<String, Object> getFieldValuesMap(Object obj) throws Exception {
        LinkedHashMap linkedHashMap = new LinkedHashMap(this.sortedGetters.length);
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            linkedHashMap.put(fieldSerializer.fieldInfo.name, fieldSerializer.getPropertyValue(obj));
        }
        return linkedHashMap;
    }
}
