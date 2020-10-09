package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class JavaBeanInfo {
    final Constructor<?> creatorConstructor;
    public final String[] creatorConstructorParameters;
    final Constructor<?> defaultConstructor;
    final int defaultConstructorParameterSize;
    final Method factoryMethod;
    final FieldInfo[] fields;
    final JSONType jsonType;
    boolean ordered = false;
    public final int parserFeatures;
    final FieldInfo[] sortedFields;
    final boolean supportBeanToArray;
    public final String typeKey;
    public final long typeKeyHashCode;
    public final String typeName;

    JavaBeanInfo(Class<?> cls, Constructor<?> constructor, Constructor<?> constructor2, Method method, FieldInfo[] fieldInfoArr, FieldInfo[] fieldInfoArr2, JSONType jSONType, String[] strArr) {
        int i;
        boolean z;
        int i2 = 0;
        this.defaultConstructor = constructor;
        this.creatorConstructor = constructor2;
        this.factoryMethod = method;
        this.fields = fieldInfoArr;
        this.jsonType = jSONType;
        if (strArr == null || strArr.length != fieldInfoArr.length) {
            this.creatorConstructorParameters = strArr;
        } else {
            this.creatorConstructorParameters = null;
        }
        if (jSONType != null) {
            String typeName2 = jSONType.typeName();
            this.typeName = typeName2.length() <= 0 ? cls.getName() : typeName2;
            String typeKey2 = jSONType.typeKey();
            this.typeKey = typeKey2.length() <= 0 ? null : typeKey2;
            i = 0;
            for (Feature feature : jSONType.parseFeatures()) {
                i |= feature.mask;
            }
        } else {
            this.typeName = cls.getName();
            this.typeKey = null;
            i = 0;
        }
        if (this.typeKey == null) {
            this.typeKeyHashCode = 0;
        } else {
            this.typeKeyHashCode = TypeUtils.fnv_64_lower(this.typeKey);
        }
        this.parserFeatures = i;
        if (jSONType != null) {
            z = false;
            for (Feature feature2 : jSONType.parseFeatures()) {
                if (feature2 == Feature.SupportArrayToBean) {
                    z = true;
                }
            }
        } else {
            z = false;
        }
        this.supportBeanToArray = z;
        FieldInfo[] computeSortedFields = computeSortedFields(fieldInfoArr, fieldInfoArr2);
        this.sortedFields = Arrays.equals(fieldInfoArr, computeSortedFields) ? fieldInfoArr : computeSortedFields;
        if (constructor != null) {
            i2 = constructor.getParameterTypes().length;
        } else if (method != null) {
            i2 = method.getParameterTypes().length;
        }
        this.defaultConstructorParameterSize = i2;
    }

    private FieldInfo[] computeSortedFields(FieldInfo[] fieldInfoArr, FieldInfo[] fieldInfoArr2) {
        String[] orders;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        if (!(this.jsonType == null || (orders = this.jsonType.orders()) == null || orders.length == 0)) {
            int i = 0;
            while (true) {
                if (i >= orders.length) {
                    z = true;
                    break;
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= fieldInfoArr2.length) {
                        z4 = false;
                        break;
                    } else if (fieldInfoArr2[i2].name.equals(orders[i])) {
                        z4 = true;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (!z4) {
                    z = false;
                    break;
                }
                i++;
            }
            if (!z) {
                return fieldInfoArr2;
            }
            if (orders.length == fieldInfoArr.length) {
                int i3 = 0;
                while (true) {
                    if (i3 >= orders.length) {
                        z3 = true;
                        break;
                    } else if (!fieldInfoArr2[i3].name.equals(orders[i3])) {
                        z3 = false;
                        break;
                    } else {
                        i3++;
                    }
                }
                if (z3) {
                    return fieldInfoArr2;
                }
                FieldInfo[] fieldInfoArr3 = new FieldInfo[fieldInfoArr2.length];
                for (int i4 = 0; i4 < orders.length; i4++) {
                    int i5 = 0;
                    while (true) {
                        if (i5 >= fieldInfoArr2.length) {
                            break;
                        } else if (fieldInfoArr2[i5].name.equals(orders[i4])) {
                            fieldInfoArr3[i4] = fieldInfoArr2[i5];
                            break;
                        } else {
                            i5++;
                        }
                    }
                }
                this.ordered = true;
                return fieldInfoArr3;
            }
            FieldInfo[] fieldInfoArr4 = new FieldInfo[fieldInfoArr2.length];
            for (int i6 = 0; i6 < orders.length; i6++) {
                int i7 = 0;
                while (true) {
                    if (i7 >= fieldInfoArr2.length) {
                        break;
                    } else if (fieldInfoArr2[i7].name.equals(orders[i6])) {
                        fieldInfoArr4[i6] = fieldInfoArr2[i7];
                        break;
                    } else {
                        i7++;
                    }
                }
            }
            int length = orders.length;
            for (int i8 = 0; i8 < fieldInfoArr2.length; i8++) {
                int i9 = 0;
                while (true) {
                    if (i9 >= fieldInfoArr4.length || i9 >= length) {
                        z2 = false;
                    } else if (fieldInfoArr4[i8].equals(fieldInfoArr2[i9])) {
                        z2 = true;
                        break;
                    } else {
                        i9++;
                    }
                }
                if (!z2) {
                    fieldInfoArr4[length] = fieldInfoArr2[i8];
                    length++;
                }
            }
            this.ordered = true;
        }
        return fieldInfoArr2;
    }

    static boolean addField(List<FieldInfo> list, FieldInfo fieldInfo, boolean z) {
        if (!z) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                FieldInfo fieldInfo2 = list.get(i);
                if (fieldInfo2.name.equals(fieldInfo.name) && (!fieldInfo2.getOnly || fieldInfo.getOnly)) {
                    return false;
                }
            }
        }
        list.add(fieldInfo);
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v0, resolved type: java.lang.Class<java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.lang.Class<java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Class<java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v28, resolved type: java.lang.Class<? super java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v44, resolved type: java.lang.annotation.Annotation} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v36, resolved type: java.lang.Class<java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v37, resolved type: java.lang.Class<java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v40, resolved type: java.lang.Class<? super java.lang.Object>} */
    /* JADX WARNING: type inference failed for: r0v50, types: [java.lang.reflect.Type[]] */
    /* JADX WARNING: type inference failed for: r0v67, types: [java.lang.reflect.Type[]] */
    /* JADX WARNING: type inference failed for: r0v81, types: [java.lang.reflect.Type[]] */
    /* JADX WARNING: Code restructure failed: missing block: B:273:0x0669, code lost:
        if ((java.util.Map.class.isAssignableFrom(r5) || java.util.Collection.class.isAssignableFrom(r5)) == false) goto L_0x064a;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:255:0x05f1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.fastjson.parser.JavaBeanInfo build(java.lang.Class<?> r48, int r49, java.lang.reflect.Type r50, boolean r51, boolean r52, boolean r53, boolean r54, com.alibaba.fastjson.PropertyNamingStrategy r55) {
        /*
            r11 = r48
            r12 = r49
            r13 = r51
            r10 = r55
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
            java.lang.reflect.Constructor[] r0 = r48.getDeclaredConstructors()
            boolean r1 = com.alibaba.fastjson.util.TypeUtils.isKotlin(r48)
            r2 = r12 & 1024(0x400, float:1.435E-42)
            r17 = 0
            r7 = 1
            r6 = 0
            if (r2 != 0) goto L_0x006b
            int r3 = r0.length
            if (r3 == r7) goto L_0x0027
            if (r1 != 0) goto L_0x006b
        L_0x0027:
            java.lang.Class[] r3 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x002e }
            java.lang.reflect.Constructor r3 = r11.getDeclaredConstructor(r3)     // Catch:{ Exception -> 0x002e }
            goto L_0x0030
        L_0x002e:
            r3 = r17
        L_0x0030:
            if (r3 != 0) goto L_0x0064
            boolean r4 = r48.isMemberClass()
            if (r4 == 0) goto L_0x0064
            r4 = r12 & 8
            if (r4 != 0) goto L_0x0064
            int r4 = r0.length
            r5 = 0
        L_0x003e:
            if (r5 >= r4) goto L_0x0064
            r18 = r0[r5]
            java.lang.Class[] r6 = r18.getParameterTypes()
            r20 = r3
            int r3 = r6.length
            if (r3 != r7) goto L_0x005c
            r19 = 0
            r3 = r6[r19]
            java.lang.Class r6 = r48.getDeclaringClass()
            boolean r3 = r3.equals(r6)
            if (r3 == 0) goto L_0x005e
            r6 = r18
            goto L_0x006f
        L_0x005c:
            r19 = 0
        L_0x005e:
            int r5 = r5 + 1
            r3 = r20
            r6 = 0
            goto L_0x003e
        L_0x0064:
            r20 = r3
            r19 = 0
            r6 = r20
            goto L_0x006f
        L_0x006b:
            r19 = 0
            r6 = r17
        L_0x006f:
            r18 = 0
            if (r13 == 0) goto L_0x0078
            r10 = r17
            r15 = r10
            goto L_0x00e2
        L_0x0078:
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r4 = r11
            r5 = r17
        L_0x0080:
            if (r4 == 0) goto L_0x00d4
            java.lang.Class<java.lang.Object> r7 = java.lang.Object.class
            if (r4 == r7) goto L_0x00d4
            java.lang.reflect.Method[] r7 = r4.getDeclaredMethods()
            r22 = r5
            int r5 = r7.length
            r10 = 0
        L_0x008e:
            if (r10 >= r5) goto L_0x00ca
            r23 = r5
            r5 = r7[r10]
            r24 = r7
            int r7 = r5.getModifiers()
            r20 = r7 & 8
            if (r20 == 0) goto L_0x00b3
            java.lang.Class<com.alibaba.fastjson.annotation.JSONCreator> r7 = com.alibaba.fastjson.annotation.JSONCreator.class
            boolean r7 = r5.isAnnotationPresent(r7)
            if (r7 == 0) goto L_0x00c3
            if (r22 != 0) goto L_0x00ab
            r22 = r5
            goto L_0x00c3
        L_0x00ab:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r1 = "multi-json creator"
            r0.<init>(r1)
            throw r0
        L_0x00b3:
            r20 = r7 & 2
            if (r20 != 0) goto L_0x00c3
            r15 = r7 & 256(0x100, float:3.59E-43)
            if (r15 != 0) goto L_0x00c3
            r7 = r7 & 4
            if (r7 == 0) goto L_0x00c0
            goto L_0x00c3
        L_0x00c0:
            r3.add(r5)
        L_0x00c3:
            int r10 = r10 + 1
            r5 = r23
            r7 = r24
            goto L_0x008e
        L_0x00ca:
            java.lang.Class r4 = r4.getSuperclass()
            r5 = r22
            r7 = 1
            r10 = r55
            goto L_0x0080
        L_0x00d4:
            r22 = r5
            int r4 = r3.size()
            java.lang.reflect.Method[] r4 = new java.lang.reflect.Method[r4]
            r3.toArray(r4)
            r10 = r4
            r15 = r22
        L_0x00e2:
            java.lang.reflect.Field[] r7 = r48.getDeclaredFields()
            boolean r3 = r48.isInterface()
            if (r3 != 0) goto L_0x00f1
            if (r2 == 0) goto L_0x00ef
            goto L_0x00f1
        L_0x00ef:
            r2 = 0
            goto L_0x00f2
        L_0x00f1:
            r2 = 1
        L_0x00f2:
            if (r6 == 0) goto L_0x0105
            if (r2 == 0) goto L_0x00f7
            goto L_0x0105
        L_0x00f7:
            r22 = r7
            r28 = r10
            r37 = r15
            r23 = r17
            r15 = 1
            r7 = r6
            r10 = r8
            r8 = 0
            goto L_0x040f
        L_0x0105:
            int r3 = r0.length
            r4 = 0
        L_0x0107:
            if (r4 >= r3) goto L_0x011d
            r5 = r0[r4]
            r25 = r3
            java.lang.Class<com.alibaba.fastjson.annotation.JSONCreator> r3 = com.alibaba.fastjson.annotation.JSONCreator.class
            java.lang.annotation.Annotation r3 = r5.getAnnotation(r3)
            com.alibaba.fastjson.annotation.JSONCreator r3 = (com.alibaba.fastjson.annotation.JSONCreator) r3
            if (r3 == 0) goto L_0x0118
            goto L_0x011f
        L_0x0118:
            int r4 = r4 + 1
            r3 = r25
            goto L_0x0107
        L_0x011d:
            r5 = r17
        L_0x011f:
            if (r5 == 0) goto L_0x0202
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r5, r12)
            java.lang.Class[] r4 = r5.getParameterTypes()
            if (r54 == 0) goto L_0x0131
            java.lang.reflect.Type[] r0 = r5.getGenericParameterTypes()
            r18 = r0
            goto L_0x0133
        L_0x0131:
            r18 = r4
        L_0x0133:
            java.lang.annotation.Annotation[][] r20 = r5.getParameterAnnotations()
            r3 = 0
        L_0x0138:
            int r0 = r4.length
            if (r3 >= r0) goto L_0x01bd
            r0 = r20[r3]
            int r1 = r0.length
            r2 = 0
        L_0x013f:
            if (r2 >= r1) goto L_0x0156
            r26 = r1
            r1 = r0[r2]
            r27 = r0
            boolean r0 = r1 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r0 == 0) goto L_0x014f
            r0 = r1
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            goto L_0x0158
        L_0x014f:
            int r2 = r2 + 1
            r1 = r26
            r0 = r27
            goto L_0x013f
        L_0x0156:
            r0 = r17
        L_0x0158:
            if (r0 == 0) goto L_0x01b5
            r22 = r4[r3]
            r23 = r18[r3]
            java.lang.String r1 = r0.name()
            java.lang.reflect.Field r2 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r1, r7, r8)
            if (r2 == 0) goto L_0x016b
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r2, r12)
        L_0x016b:
            int r24 = r0.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r1 = r0.serialzeFeatures()
            int r25 = com.alibaba.fastjson.serializer.SerializerFeature.of(r1)
            com.alibaba.fastjson.util.FieldInfo r1 = new com.alibaba.fastjson.util.FieldInfo
            java.lang.String r26 = r0.name()
            r0 = r1
            r28 = r10
            r10 = r1
            r1 = r26
            r26 = r2
            r2 = r48
            r27 = r3
            r3 = r22
            r22 = r4
            r4 = r23
            r23 = r5
            r5 = r26
            r29 = r6
            r30 = r8
            r8 = 0
            r6 = r24
            r31 = r7
            r7 = r25
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            addField(r9, r10, r13)
            int r3 = r27 + 1
            r4 = r22
            r5 = r23
            r10 = r28
            r6 = r29
            r8 = r30
            r7 = r31
            r19 = 0
            goto L_0x0138
        L_0x01b5:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r1 = "illegal json creator"
            r0.<init>(r1)
            throw r0
        L_0x01bd:
            r23 = r5
            r29 = r6
            r31 = r7
            r30 = r8
            r28 = r10
            r8 = 0
            int r0 = r9.size()
            com.alibaba.fastjson.util.FieldInfo[] r0 = new com.alibaba.fastjson.util.FieldInfo[r0]
            r9.toArray(r0)
            int r1 = r0.length
            com.alibaba.fastjson.util.FieldInfo[] r1 = new com.alibaba.fastjson.util.FieldInfo[r1]
            int r2 = r0.length
            java.lang.System.arraycopy(r0, r8, r1, r8, r2)
            java.util.Arrays.sort(r1)
            if (r52 == 0) goto L_0x01e5
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r1 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r1 = r11.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONType r1 = (com.alibaba.fastjson.annotation.JSONType) r1
        L_0x01e5:
            int r1 = r0.length
            java.lang.String[] r1 = new java.lang.String[r1]
            r2 = 0
        L_0x01e9:
            int r3 = r0.length
            if (r2 >= r3) goto L_0x01f5
            r3 = r0[r2]
            java.lang.String r3 = r3.name
            r1[r2] = r3
            int r2 = r2 + 1
            goto L_0x01e9
        L_0x01f5:
            r18 = r1
            r37 = r15
            r7 = r29
            r10 = r30
            r22 = r31
            r15 = 1
            goto L_0x040f
        L_0x0202:
            r23 = r5
            r29 = r6
            r31 = r7
            r30 = r8
            r28 = r10
            r8 = 0
            if (r15 == 0) goto L_0x02ce
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r15, r12)
            java.lang.Class[] r10 = r15.getParameterTypes()
            int r0 = r10.length
            if (r0 <= 0) goto L_0x02c6
            if (r54 == 0) goto L_0x0221
            java.lang.reflect.Type[] r0 = r15.getGenericParameterTypes()
            r12 = r0
            goto L_0x0222
        L_0x0221:
            r12 = r10
        L_0x0222:
            java.lang.annotation.Annotation[][] r16 = r15.getParameterAnnotations()
            r7 = 0
        L_0x0227:
            int r0 = r10.length
            if (r7 >= r0) goto L_0x028d
            r0 = r16[r7]
            int r1 = r0.length
            r2 = 0
        L_0x022e:
            if (r2 >= r1) goto L_0x023d
            r3 = r0[r2]
            boolean r4 = r3 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r4 == 0) goto L_0x023a
            r0 = r3
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            goto L_0x023f
        L_0x023a:
            int r2 = r2 + 1
            goto L_0x022e
        L_0x023d:
            r0 = r17
        L_0x023f:
            if (r0 == 0) goto L_0x0285
            r3 = r10[r7]
            r4 = r12[r7]
            java.lang.String r1 = r0.name()
            r6 = r30
            r5 = r31
            java.lang.reflect.Field r19 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r1, r5, r6)
            int r20 = r0.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r1 = r0.serialzeFeatures()
            int r21 = com.alibaba.fastjson.serializer.SerializerFeature.of(r1)
            com.alibaba.fastjson.util.FieldInfo r2 = new com.alibaba.fastjson.util.FieldInfo
            java.lang.String r1 = r0.name()
            r0 = r2
            r8 = r2
            r2 = r48
            r32 = r10
            r10 = r5
            r5 = r19
            r33 = r10
            r10 = r6
            r6 = r20
            r19 = r7
            r7 = r21
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            addField(r9, r8, r13)
            int r7 = r19 + 1
            r30 = r10
            r10 = r32
            r31 = r33
            r8 = 0
            goto L_0x0227
        L_0x0285:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r1 = "illegal json creator"
            r0.<init>(r1)
            throw r0
        L_0x028d:
            int r0 = r9.size()
            com.alibaba.fastjson.util.FieldInfo[] r5 = new com.alibaba.fastjson.util.FieldInfo[r0]
            r9.toArray(r5)
            int r0 = r5.length
            com.alibaba.fastjson.util.FieldInfo[] r0 = new com.alibaba.fastjson.util.FieldInfo[r0]
            int r1 = r5.length
            r2 = 0
            java.lang.System.arraycopy(r5, r2, r0, r2, r1)
            java.util.Arrays.sort(r0)
            boolean r1 = java.util.Arrays.equals(r5, r0)
            if (r1 == 0) goto L_0x02a9
            r6 = r5
            goto L_0x02aa
        L_0x02a9:
            r6 = r0
        L_0x02aa:
            if (r52 == 0) goto L_0x02b6
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r0 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r0 = r11.getAnnotation(r0)
            com.alibaba.fastjson.annotation.JSONType r0 = (com.alibaba.fastjson.annotation.JSONType) r0
            r7 = r0
            goto L_0x02b8
        L_0x02b6:
            r7 = r17
        L_0x02b8:
            com.alibaba.fastjson.parser.JavaBeanInfo r9 = new com.alibaba.fastjson.parser.JavaBeanInfo
            r2 = 0
            r3 = 0
            r0 = r9
            r1 = r48
            r4 = r15
            r8 = r18
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        L_0x02c6:
            r10 = r30
            r37 = r15
            r22 = r31
            goto L_0x040c
        L_0x02ce:
            r10 = r30
            r33 = r31
            if (r2 != 0) goto L_0x0407
            if (r1 == 0) goto L_0x03f0
            int r1 = r0.length
            if (r1 <= 0) goto L_0x03f0
            java.lang.String[] r8 = com.alibaba.fastjson.util.TypeUtils.getKoltinConstructorParameters(r48)
            if (r8 == 0) goto L_0x03d9
            int r1 = r0.length
            r7 = r23
            r2 = 0
        L_0x02e3:
            if (r2 >= r1) goto L_0x030f
            r3 = r0[r2]
            java.lang.Class[] r4 = r3.getParameterTypes()
            int r5 = r4.length
            if (r5 <= 0) goto L_0x0300
            int r5 = r4.length
            r6 = 1
            int r5 = r5 - r6
            r5 = r4[r5]
            java.lang.String r5 = r5.getName()
            java.lang.String r6 = "kotlin.jvm.internal.DefaultConstructorMarker"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x0300
            goto L_0x030c
        L_0x0300:
            if (r7 == 0) goto L_0x030b
            java.lang.Class[] r5 = r7.getParameterTypes()
            int r5 = r5.length
            int r4 = r4.length
            if (r5 < r4) goto L_0x030b
            goto L_0x030c
        L_0x030b:
            r7 = r3
        L_0x030c:
            int r2 = r2 + 1
            goto L_0x02e3
        L_0x030f:
            r6 = 1
            r7.setAccessible(r6)
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r7, r12)
            java.lang.Class[] r5 = r7.getParameterTypes()
            if (r54 == 0) goto L_0x0323
            java.lang.reflect.Type[] r0 = r7.getGenericParameterTypes()
            r18 = r0
            goto L_0x0325
        L_0x0323:
            r18 = r5
        L_0x0325:
            java.lang.annotation.Annotation[][] r19 = r7.getParameterAnnotations()
            r4 = 0
        L_0x032a:
            int r0 = r5.length
            if (r4 >= r0) goto L_0x03a9
            r0 = r8[r4]
            r1 = r19[r4]
            int r2 = r1.length
            r3 = 0
        L_0x0333:
            if (r3 >= r2) goto L_0x0347
            r6 = r1[r3]
            r34 = r1
            boolean r1 = r6 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r1 == 0) goto L_0x0341
            r1 = r6
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            goto L_0x0349
        L_0x0341:
            int r3 = r3 + 1
            r1 = r34
            r6 = 1
            goto L_0x0333
        L_0x0347:
            r1 = r17
        L_0x0349:
            r3 = r5[r4]
            r6 = r18[r4]
            r35 = r5
            r2 = r33
            java.lang.reflect.Field r5 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r0, r2, r10)
            if (r5 == 0) goto L_0x0361
            if (r1 != 0) goto L_0x0361
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r5.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
        L_0x0361:
            if (r1 == 0) goto L_0x037c
            int r20 = r1.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r21 = r1.serialzeFeatures()
            int r21 = com.alibaba.fastjson.serializer.SerializerFeature.of(r21)
            java.lang.String r1 = r1.name()
            int r22 = r1.length()
            if (r22 == 0) goto L_0x037a
            r0 = r1
        L_0x037a:
            r1 = r0
            goto L_0x0381
        L_0x037c:
            r1 = r0
            r20 = 0
            r21 = 0
        L_0x0381:
            com.alibaba.fastjson.util.FieldInfo r0 = new com.alibaba.fastjson.util.FieldInfo
            r36 = r0
            r22 = r2
            r2 = r48
            r23 = r4
            r4 = r6
            r24 = r35
            r37 = r15
            r15 = 1
            r6 = r20
            r20 = r7
            r7 = r21
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            addField(r9, r0, r13)
            int r4 = r23 + 1
            r7 = r20
            r33 = r22
            r5 = r24
            r15 = r37
            r6 = 1
            goto L_0x032a
        L_0x03a9:
            r20 = r7
            r37 = r15
            r22 = r33
            r15 = 1
            int r0 = r9.size()
            com.alibaba.fastjson.util.FieldInfo[] r0 = new com.alibaba.fastjson.util.FieldInfo[r0]
            r9.toArray(r0)
            int r1 = r0.length
            com.alibaba.fastjson.util.FieldInfo[] r1 = new com.alibaba.fastjson.util.FieldInfo[r1]
            int r2 = r0.length
            r8 = 0
            java.lang.System.arraycopy(r0, r8, r1, r8, r2)
            java.util.Arrays.sort(r1)
            int r1 = r0.length
            java.lang.String[] r1 = new java.lang.String[r1]
            r2 = 0
        L_0x03c8:
            int r3 = r0.length
            if (r2 >= r3) goto L_0x03d4
            r3 = r0[r2]
            java.lang.String r3 = r3.name
            r1[r2] = r3
            int r2 = r2 + 1
            goto L_0x03c8
        L_0x03d4:
            r18 = r1
            r23 = r20
            goto L_0x040d
        L_0x03d9:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "default constructor not found. "
            r1.append(r2)
            r1.append(r11)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x03f0:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "default constructor not found. "
            r1.append(r2)
            r1.append(r11)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0407:
            r37 = r15
            r22 = r33
            r8 = 0
        L_0x040c:
            r15 = 1
        L_0x040d:
            r7 = r29
        L_0x040f:
            if (r7 == 0) goto L_0x0414
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r7, r12)
        L_0x0414:
            r5 = 4
            if (r13 != 0) goto L_0x062d
            r4 = r28
            int r3 = r4.length
            r2 = 0
        L_0x041b:
            if (r2 >= r3) goto L_0x0625
            r1 = r4[r2]
            java.lang.String r0 = r1.getName()
            int r6 = r0.length()
            if (r6 >= r5) goto L_0x0438
        L_0x0429:
            r26 = r2
            r27 = r3
            r42 = r4
            r28 = r7
            r15 = r9
            r20 = r10
            r43 = r22
            goto L_0x0613
        L_0x0438:
            java.lang.Class r6 = r1.getReturnType()
            java.lang.Class r5 = java.lang.Void.TYPE
            if (r6 == r5) goto L_0x0446
            java.lang.Class r5 = r1.getDeclaringClass()
            if (r6 != r5) goto L_0x0429
        L_0x0446:
            java.lang.Class[] r5 = r1.getParameterTypes()
            int r5 = r5.length
            if (r5 == r15) goto L_0x044e
            goto L_0x0429
        L_0x044e:
            if (r53 == 0) goto L_0x0459
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r5 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r5 = r1.getAnnotation(r5)
            com.alibaba.fastjson.annotation.JSONField r5 = (com.alibaba.fastjson.annotation.JSONField) r5
            goto L_0x045b
        L_0x0459:
            r5 = r17
        L_0x045b:
            if (r5 != 0) goto L_0x0463
            if (r53 == 0) goto L_0x0463
            com.alibaba.fastjson.annotation.JSONField r5 = com.alibaba.fastjson.util.TypeUtils.getSupperMethodAnnotation(r11, r1)
        L_0x0463:
            r20 = r5
            if (r20 == 0) goto L_0x04cf
            boolean r5 = r20.deserialize()
            if (r5 != 0) goto L_0x046e
            goto L_0x0429
        L_0x046e:
            int r6 = r20.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r5 = r20.serialzeFeatures()
            int r21 = com.alibaba.fastjson.serializer.SerializerFeature.of(r5)
            java.lang.String r5 = r20.name()
            int r5 = r5.length()
            if (r5 == 0) goto L_0x04c0
            java.lang.String r5 = r20.name()
            com.alibaba.fastjson.util.FieldInfo r0 = new com.alibaba.fastjson.util.FieldInfo
            r24 = 0
            r25 = 0
            r38 = r0
            r39 = r1
            r1 = r5
            r26 = r2
            r2 = r39
            r27 = r3
            r3 = r24
            r24 = r4
            r4 = r48
            r15 = 4
            r5 = r50
            r15 = 3
            r28 = r7
            r7 = r21
            r8 = r20
            r15 = r9
            r9 = r25
            r41 = r10
            r14 = r22
            r42 = r24
            r10 = r54
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            addField(r15, r0, r13)
            r10 = r39
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r10, r12)
            goto L_0x04e8
        L_0x04c0:
            r26 = r2
            r27 = r3
            r42 = r4
            r28 = r7
            r15 = r9
            r41 = r10
            r14 = r22
            r10 = r1
            goto L_0x04e0
        L_0x04cf:
            r26 = r2
            r27 = r3
            r42 = r4
            r28 = r7
            r15 = r9
            r41 = r10
            r14 = r22
            r10 = r1
            r6 = 0
            r21 = 0
        L_0x04e0:
            java.lang.String r1 = "set"
            boolean r1 = r0.startsWith(r1)
            if (r1 != 0) goto L_0x04ee
        L_0x04e8:
            r43 = r14
            r20 = r41
            goto L_0x0613
        L_0x04ee:
            r1 = 3
            char r2 = r0.charAt(r1)
            boolean r3 = java.lang.Character.isUpperCase(r2)
            if (r3 == 0) goto L_0x0525
            boolean r2 = com.alibaba.fastjson.util.TypeUtils.compatibleWithJavaBean
            if (r2 == 0) goto L_0x0508
            java.lang.String r0 = r0.substring(r1)
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.decapitalize(r0)
        L_0x0505:
            r9 = r41
            goto L_0x0554
        L_0x0508:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            char r3 = r0.charAt(r1)
            char r1 = java.lang.Character.toLowerCase(r3)
            r2.append(r1)
            r1 = 4
            java.lang.String r0 = r0.substring(r1)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            goto L_0x0505
        L_0x0525:
            r1 = 4
            r3 = 95
            if (r2 != r3) goto L_0x052f
            java.lang.String r0 = r0.substring(r1)
            goto L_0x0505
        L_0x052f:
            r3 = 102(0x66, float:1.43E-43)
            if (r2 != r3) goto L_0x0539
            r2 = 3
            java.lang.String r0 = r0.substring(r2)
            goto L_0x0505
        L_0x0539:
            r2 = 3
            int r3 = r0.length()
            r4 = 5
            if (r3 < r4) goto L_0x04e8
            char r3 = r0.charAt(r1)
            boolean r1 = java.lang.Character.isUpperCase(r3)
            if (r1 == 0) goto L_0x04e8
            java.lang.String r0 = r0.substring(r2)
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.decapitalize(r0)
            goto L_0x0505
        L_0x0554:
            java.lang.reflect.Field r1 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r0, r14, r9)
            if (r1 != 0) goto L_0x058b
            java.lang.Class[] r2 = r10.getParameterTypes()
            r8 = 0
            r2 = r2[r8]
            java.lang.Class r3 = java.lang.Boolean.TYPE
            if (r2 != r3) goto L_0x058c
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "is"
            r1.append(r2)
            char r2 = r0.charAt(r8)
            char r2 = java.lang.Character.toUpperCase(r2)
            r1.append(r2)
            r2 = 1
            java.lang.String r3 = r0.substring(r2)
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.reflect.Field r1 = com.alibaba.fastjson.util.TypeUtils.getField(r11, r1, r14, r9)
            goto L_0x058c
        L_0x058b:
            r8 = 0
        L_0x058c:
            r3 = r1
            if (r3 == 0) goto L_0x05e3
            if (r53 == 0) goto L_0x059c
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r3.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            r22 = r1
            goto L_0x059e
        L_0x059c:
            r22 = r17
        L_0x059e:
            if (r22 == 0) goto L_0x05e3
            int r6 = r22.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r1 = r22.serialzeFeatures()
            int r7 = com.alibaba.fastjson.serializer.SerializerFeature.of(r1)
            java.lang.String r1 = r22.name()
            int r1 = r1.length()
            if (r1 == 0) goto L_0x05d7
            java.lang.String r1 = r22.name()
            com.alibaba.fastjson.util.FieldInfo r5 = new com.alibaba.fastjson.util.FieldInfo
            r0 = r5
            r2 = r10
            r4 = r48
            r10 = r5
            r5 = r50
            r8 = r20
            r24 = r9
            r9 = r22
            r43 = r14
            r14 = r10
            r10 = r54
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            addField(r15, r14, r13)
            r20 = r24
            goto L_0x0613
        L_0x05d7:
            r24 = r9
            r43 = r14
            if (r20 != 0) goto L_0x05e0
            r8 = r22
            goto L_0x05eb
        L_0x05e0:
            r8 = r20
            goto L_0x05eb
        L_0x05e3:
            r24 = r9
            r43 = r14
            r8 = r20
            r7 = r21
        L_0x05eb:
            r20 = r24
            r14 = r55
            if (r14 == 0) goto L_0x05f5
            java.lang.String r0 = r14.translate(r0)
        L_0x05f5:
            r1 = r0
            com.alibaba.fastjson.util.FieldInfo r9 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r21 = 0
            r0 = r9
            r2 = r10
            r4 = r48
            r5 = r50
            r44 = r9
            r9 = r21
            r14 = r10
            r10 = r54
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r44
            addField(r15, r0, r13)
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r14, r12)
        L_0x0613:
            int r2 = r26 + 1
            r9 = r15
            r10 = r20
            r3 = r27
            r7 = r28
            r4 = r42
            r22 = r43
            r5 = 4
            r8 = 0
            r15 = 1
            goto L_0x041b
        L_0x0625:
            r42 = r4
            r28 = r7
            r15 = r9
            r43 = r22
            goto L_0x0634
        L_0x062d:
            r15 = r9
            r43 = r22
            r42 = r28
            r28 = r7
        L_0x0634:
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r43
            int r2 = r1.length
            r0.<init>(r2)
            int r2 = r1.length
            r3 = 0
        L_0x063e:
            if (r3 >= r2) goto L_0x067c
            r4 = r1[r3]
            int r5 = r4.getModifiers()
            r6 = r5 & 8
            if (r6 == 0) goto L_0x064d
        L_0x064a:
            r40 = 1
            goto L_0x0679
        L_0x064d:
            r5 = r5 & 16
            if (r5 == 0) goto L_0x066c
            java.lang.Class r5 = r4.getType()
            java.lang.Class<java.util.Map> r6 = java.util.Map.class
            boolean r6 = r6.isAssignableFrom(r5)
            if (r6 != 0) goto L_0x0668
            java.lang.Class<java.util.Collection> r6 = java.util.Collection.class
            boolean r5 = r6.isAssignableFrom(r5)
            if (r5 == 0) goto L_0x0666
            goto L_0x0668
        L_0x0666:
            r5 = 0
            goto L_0x0669
        L_0x0668:
            r5 = 1
        L_0x0669:
            if (r5 != 0) goto L_0x066c
            goto L_0x064a
        L_0x066c:
            int r5 = r4.getModifiers()
            r40 = 1
            r5 = r5 & 1
            if (r5 == 0) goto L_0x0679
            r0.add(r4)
        L_0x0679:
            int r3 = r3 + 1
            goto L_0x063e
        L_0x067c:
            r40 = 1
            java.lang.Class r1 = r48.getSuperclass()
        L_0x0682:
            if (r1 == 0) goto L_0x06c9
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
            if (r1 == r2) goto L_0x06c9
            java.lang.reflect.Field[] r2 = r1.getDeclaredFields()
            int r3 = r2.length
            r4 = 0
        L_0x068e:
            if (r4 >= r3) goto L_0x06c4
            r5 = r2[r4]
            int r6 = r5.getModifiers()
            r7 = r6 & 8
            if (r7 == 0) goto L_0x069b
            goto L_0x06c1
        L_0x069b:
            r7 = r6 & 16
            if (r7 == 0) goto L_0x06ba
            java.lang.Class r7 = r5.getType()
            java.lang.Class<java.util.Map> r8 = java.util.Map.class
            boolean r8 = r8.isAssignableFrom(r7)
            if (r8 != 0) goto L_0x06b6
            java.lang.Class<java.util.Collection> r8 = java.util.Collection.class
            boolean r7 = r8.isAssignableFrom(r7)
            if (r7 == 0) goto L_0x06b4
            goto L_0x06b6
        L_0x06b4:
            r7 = 0
            goto L_0x06b7
        L_0x06b6:
            r7 = 1
        L_0x06b7:
            if (r7 != 0) goto L_0x06ba
            goto L_0x06c1
        L_0x06ba:
            r6 = r6 & 1
            if (r6 == 0) goto L_0x06c1
            r0.add(r5)
        L_0x06c1:
            int r4 = r4 + 1
            goto L_0x068e
        L_0x06c4:
            java.lang.Class r1 = r1.getSuperclass()
            goto L_0x0682
        L_0x06c9:
            java.util.Iterator r14 = r0.iterator()
        L_0x06cd:
            boolean r0 = r14.hasNext()
            if (r0 == 0) goto L_0x0750
            java.lang.Object r0 = r14.next()
            r3 = r0
            java.lang.reflect.Field r3 = (java.lang.reflect.Field) r3
            java.lang.String r0 = r3.getName()
            int r1 = r15.size()
            r2 = 0
            r7 = 0
        L_0x06e4:
            if (r2 >= r1) goto L_0x06f8
            java.lang.Object r4 = r15.get(r2)
            com.alibaba.fastjson.util.FieldInfo r4 = (com.alibaba.fastjson.util.FieldInfo) r4
            java.lang.String r4 = r4.name
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x06f5
            r7 = 1
        L_0x06f5:
            int r2 = r2 + 1
            goto L_0x06e4
        L_0x06f8:
            if (r7 == 0) goto L_0x06fb
            goto L_0x06cd
        L_0x06fb:
            if (r53 == 0) goto L_0x0707
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r3.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            r9 = r1
            goto L_0x0709
        L_0x0707:
            r9 = r17
        L_0x0709:
            if (r9 == 0) goto L_0x0728
            int r1 = r9.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r2 = r9.serialzeFeatures()
            int r2 = com.alibaba.fastjson.serializer.SerializerFeature.of(r2)
            java.lang.String r4 = r9.name()
            int r4 = r4.length()
            if (r4 == 0) goto L_0x0725
            java.lang.String r0 = r9.name()
        L_0x0725:
            r6 = r1
            r7 = r2
            goto L_0x072a
        L_0x0728:
            r6 = 0
            r7 = 0
        L_0x072a:
            r10 = r55
            if (r10 == 0) goto L_0x0732
            java.lang.String r0 = r10.translate(r0)
        L_0x0732:
            r1 = r0
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r3, r12)
            com.alibaba.fastjson.util.FieldInfo r8 = new com.alibaba.fastjson.util.FieldInfo
            r2 = 0
            r20 = 0
            r0 = r8
            r4 = r48
            r5 = r50
            r45 = r8
            r8 = r20
            r10 = r54
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r45
            addField(r15, r0, r13)
            goto L_0x06cd
        L_0x0750:
            if (r13 != 0) goto L_0x0812
            r14 = r42
            int r10 = r14.length
            r9 = 0
        L_0x0756:
            if (r9 >= r10) goto L_0x0812
            r8 = r14[r9]
            java.lang.String r0 = r8.getName()
            int r1 = r0.length()
            r2 = 4
            if (r1 >= r2) goto L_0x076f
        L_0x0765:
            r20 = r9
            r22 = r10
            r21 = 3
            r25 = 4
            goto L_0x080c
        L_0x076f:
            java.lang.String r1 = "get"
            boolean r1 = r0.startsWith(r1)
            if (r1 == 0) goto L_0x0765
            r1 = 3
            char r2 = r0.charAt(r1)
            boolean r1 = java.lang.Character.isUpperCase(r2)
            if (r1 == 0) goto L_0x0765
            java.lang.Class[] r1 = r8.getParameterTypes()
            int r1 = r1.length
            if (r1 == 0) goto L_0x078a
            goto L_0x0765
        L_0x078a:
            java.lang.Class r1 = r8.getReturnType()
            java.lang.Class<java.util.Collection> r2 = java.util.Collection.class
            boolean r2 = r2.isAssignableFrom(r1)
            if (r2 != 0) goto L_0x079e
            java.lang.Class<java.util.Map> r2 = java.util.Map.class
            boolean r1 = r2.isAssignableFrom(r1)
            if (r1 == 0) goto L_0x0765
        L_0x079e:
            if (r53 == 0) goto L_0x07ab
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = r8.getAnnotation(r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            r20 = r1
            goto L_0x07ad
        L_0x07ab:
            r20 = r17
        L_0x07ad:
            if (r20 == 0) goto L_0x07bc
            java.lang.String r1 = r20.name()
            int r2 = r1.length()
            if (r2 <= 0) goto L_0x07bc
            r6 = 4
            r7 = 3
            goto L_0x07da
        L_0x07bc:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r7 = 3
            char r2 = r0.charAt(r7)
            char r2 = java.lang.Character.toLowerCase(r2)
            r1.append(r2)
            r6 = 4
            java.lang.String r0 = r0.substring(r6)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r1 = r0
        L_0x07da:
            com.alibaba.fastjson.util.FieldInfo r5 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r21 = 0
            r22 = 0
            r24 = 0
            r0 = r5
            r2 = r8
            r4 = r48
            r46 = r5
            r5 = r50
            r25 = 4
            r6 = r21
            r21 = 3
            r7 = r22
            r47 = r8
            r8 = r20
            r20 = r9
            r9 = r24
            r22 = r10
            r10 = r54
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            r0 = r46
            addField(r15, r0, r13)
            r0 = r47
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r11, r0, r12)
        L_0x080c:
            int r9 = r20 + 1
            r10 = r22
            goto L_0x0756
        L_0x0812:
            int r0 = r15.size()
            com.alibaba.fastjson.util.FieldInfo[] r5 = new com.alibaba.fastjson.util.FieldInfo[r0]
            r15.toArray(r5)
            int r0 = r5.length
            com.alibaba.fastjson.util.FieldInfo[] r6 = new com.alibaba.fastjson.util.FieldInfo[r0]
            int r0 = r5.length
            r1 = 0
            java.lang.System.arraycopy(r5, r1, r6, r1, r0)
            java.util.Arrays.sort(r6)
            if (r52 == 0) goto L_0x0832
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r0 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r0 = r11.getAnnotation(r0)
            com.alibaba.fastjson.annotation.JSONType r0 = (com.alibaba.fastjson.annotation.JSONType) r0
            r7 = r0
            goto L_0x0834
        L_0x0832:
            r7 = r17
        L_0x0834:
            com.alibaba.fastjson.parser.JavaBeanInfo r9 = new com.alibaba.fastjson.parser.JavaBeanInfo
            r0 = r9
            r1 = r48
            r2 = r28
            r3 = r23
            r4 = r37
            r8 = r18
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JavaBeanInfo.build(java.lang.Class, int, java.lang.reflect.Type, boolean, boolean, boolean, boolean, com.alibaba.fastjson.PropertyNamingStrategy):com.alibaba.fastjson.parser.JavaBeanInfo");
    }
}
