package com.alibaba.fastjson.parser;

public class ThrowableDeserializer extends JavaBeanDeserializer {
    public ThrowableDeserializer(ParserConfig parserConfig, Class<?> cls) {
        super(parserConfig, cls, cls);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v2, resolved type: java.lang.reflect.Type} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v1, resolved type: java.lang.reflect.Constructor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v2, resolved type: java.lang.reflect.Constructor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v3, resolved type: java.lang.reflect.Constructor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v3, resolved type: java.lang.reflect.Constructor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v4, resolved type: java.lang.reflect.Constructor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v15, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v16, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v6, resolved type: java.lang.reflect.Constructor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v12, resolved type: java.lang.reflect.Constructor} */
    /* JADX WARNING: type inference failed for: r7v17 */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x012d, code lost:
        r12 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x012d, code lost:
        r12 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0030, code lost:
        if (java.lang.Throwable.class.isAssignableFrom(r2) != false) goto L_0x0034;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0187  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r18, java.lang.reflect.Type r19, java.lang.Object r20) {
        /*
            r17 = this;
            r1 = r17
            r0 = r18
            r2 = r19
            com.alibaba.fastjson.parser.JSONLexer r3 = r0.lexer
            int r4 = r3.token
            r5 = 8
            r6 = 0
            if (r4 != r5) goto L_0x0013
            r3.nextToken()
            return r6
        L_0x0013:
            int r4 = r0.resolveStatus
            r7 = 2
            r8 = 0
            if (r4 != r7) goto L_0x001c
            r0.resolveStatus = r8
            goto L_0x0022
        L_0x001c:
            int r4 = r3.token
            r9 = 12
            if (r4 != r9) goto L_0x01bd
        L_0x0022:
            if (r2 == 0) goto L_0x0033
            boolean r4 = r2 instanceof java.lang.Class
            if (r4 == 0) goto L_0x0033
            java.lang.Class r2 = (java.lang.Class) r2
            java.lang.Class<java.lang.Throwable> r4 = java.lang.Throwable.class
            boolean r4 = r4.isAssignableFrom(r2)
            if (r4 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r2 = r6
        L_0x0034:
            r9 = r2
            r2 = r6
            r4 = r2
            r10 = r4
            r11 = r10
        L_0x0039:
            com.alibaba.fastjson.parser.SymbolTable r12 = r0.symbolTable
            java.lang.String r12 = r3.scanSymbol(r12)
            r13 = 13
            r14 = 16
            if (r12 != 0) goto L_0x0053
            int r15 = r3.token
            if (r15 != r13) goto L_0x004e
            r3.nextToken(r14)
            goto L_0x00d9
        L_0x004e:
            int r15 = r3.token
            if (r15 != r14) goto L_0x0053
            goto L_0x0039
        L_0x0053:
            r15 = 58
            r3.nextTokenWithChar(r15)
            java.lang.String r15 = "@type"
            boolean r15 = r15.equals(r12)
            r7 = 4
            if (r15 == 0) goto L_0x007e
            int r9 = r3.token
            if (r9 != r7) goto L_0x0076
            java.lang.String r7 = r3.stringVal()
            com.alibaba.fastjson.parser.ParserConfig r9 = r0.config
            java.lang.ClassLoader r9 = r9.defaultClassLoader
            java.lang.Class r7 = com.alibaba.fastjson.util.TypeUtils.loadClass(r7, r9, r8)
            r3.nextToken(r14)
            r9 = r7
            goto L_0x00d2
        L_0x0076:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r2 = "syntax error"
            r0.<init>(r2)
            throw r0
        L_0x007e:
            java.lang.String r15 = "message"
            boolean r15 = r15.equals(r12)
            if (r15 == 0) goto L_0x00a1
            int r10 = r3.token
            if (r10 != r5) goto L_0x008c
            r7 = r6
            goto L_0x0094
        L_0x008c:
            int r10 = r3.token
            if (r10 != r7) goto L_0x0099
            java.lang.String r7 = r3.stringVal()
        L_0x0094:
            r3.nextToken()
            r10 = r7
            goto L_0x00d2
        L_0x0099:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r2 = "syntax error"
            r0.<init>(r2)
            throw r0
        L_0x00a1:
            java.lang.String r7 = "cause"
            boolean r7 = r7.equals(r12)
            if (r7 == 0) goto L_0x00b2
            java.lang.String r4 = "cause"
            java.lang.Object r4 = r1.deserialze(r0, r6, r4)
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            goto L_0x00d2
        L_0x00b2:
            java.lang.String r7 = "stackTrace"
            boolean r7 = r7.equals(r12)
            if (r7 == 0) goto L_0x00c4
            java.lang.Class<java.lang.StackTraceElement[]> r7 = java.lang.StackTraceElement[].class
            java.lang.Object r7 = r0.parseObject(r7)
            java.lang.StackTraceElement[] r7 = (java.lang.StackTraceElement[]) r7
            r11 = r7
            goto L_0x00d2
        L_0x00c4:
            if (r2 != 0) goto L_0x00cb
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>()
        L_0x00cb:
            java.lang.Object r7 = r18.parse()
            r2.put(r12, r7)
        L_0x00d2:
            int r7 = r3.token
            if (r7 != r13) goto L_0x01b9
            r3.nextToken(r14)
        L_0x00d9:
            if (r9 != 0) goto L_0x00e2
            java.lang.Exception r3 = new java.lang.Exception
            r3.<init>(r10, r4)
            goto L_0x0168
        L_0x00e2:
            java.lang.reflect.Constructor[] r3 = r9.getConstructors()     // Catch:{ Exception -> 0x01b0 }
            int r5 = r3.length     // Catch:{ Exception -> 0x01b0 }
            r12 = r6
            r13 = r12
            r14 = r13
            r7 = 0
        L_0x00eb:
            r15 = 1
            if (r7 >= r5) goto L_0x0131
            r16 = r3[r7]     // Catch:{ Exception -> 0x01b0 }
            java.lang.Class[] r6 = r16.getParameterTypes()     // Catch:{ Exception -> 0x01b0 }
            int r6 = r6.length     // Catch:{ Exception -> 0x01b0 }
            if (r6 != 0) goto L_0x00fa
            r14 = r16
            goto L_0x012d
        L_0x00fa:
            java.lang.Class[] r6 = r16.getParameterTypes()     // Catch:{ Exception -> 0x01b0 }
            int r6 = r6.length     // Catch:{ Exception -> 0x01b0 }
            if (r6 != r15) goto L_0x010e
            java.lang.Class[] r6 = r16.getParameterTypes()     // Catch:{ Exception -> 0x01b0 }
            r6 = r6[r8]     // Catch:{ Exception -> 0x01b0 }
            java.lang.Class<java.lang.String> r15 = java.lang.String.class
            if (r6 != r15) goto L_0x010e
            r13 = r16
            goto L_0x012d
        L_0x010e:
            java.lang.Class[] r6 = r16.getParameterTypes()     // Catch:{ Exception -> 0x01b0 }
            int r6 = r6.length     // Catch:{ Exception -> 0x01b0 }
            r15 = 2
            if (r6 != r15) goto L_0x012d
            java.lang.Class[] r6 = r16.getParameterTypes()     // Catch:{ Exception -> 0x01b0 }
            r6 = r6[r8]     // Catch:{ Exception -> 0x01b0 }
            java.lang.Class<java.lang.String> r15 = java.lang.String.class
            if (r6 != r15) goto L_0x012d
            java.lang.Class[] r6 = r16.getParameterTypes()     // Catch:{ Exception -> 0x01b0 }
            r15 = 1
            r6 = r6[r15]     // Catch:{ Exception -> 0x01b0 }
            java.lang.Class<java.lang.Throwable> r15 = java.lang.Throwable.class
            if (r6 != r15) goto L_0x012d
            r12 = r16
        L_0x012d:
            int r7 = r7 + 1
            r6 = 0
            goto L_0x00eb
        L_0x0131:
            if (r12 == 0) goto L_0x0143
            r6 = 2
            java.lang.Object[] r3 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x01b0 }
            r3[r8] = r10     // Catch:{ Exception -> 0x01b0 }
            r5 = 1
            r3[r5] = r4     // Catch:{ Exception -> 0x01b0 }
            java.lang.Object r3 = r12.newInstance(r3)     // Catch:{ Exception -> 0x01b0 }
            r6 = r3
            java.lang.Throwable r6 = (java.lang.Throwable) r6     // Catch:{ Exception -> 0x01b0 }
            goto L_0x015f
        L_0x0143:
            if (r13 == 0) goto L_0x0152
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x01b0 }
            r3[r8] = r10     // Catch:{ Exception -> 0x01b0 }
            java.lang.Object r3 = r13.newInstance(r3)     // Catch:{ Exception -> 0x01b0 }
            r6 = r3
            java.lang.Throwable r6 = (java.lang.Throwable) r6     // Catch:{ Exception -> 0x01b0 }
            goto L_0x015f
        L_0x0152:
            if (r14 == 0) goto L_0x015e
            java.lang.Object[] r3 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x01b0 }
            java.lang.Object r3 = r14.newInstance(r3)     // Catch:{ Exception -> 0x01b0 }
            r6 = r3
            java.lang.Throwable r6 = (java.lang.Throwable) r6     // Catch:{ Exception -> 0x01b0 }
            goto L_0x015f
        L_0x015e:
            r6 = 0
        L_0x015f:
            if (r6 != 0) goto L_0x0167
            java.lang.Exception r3 = new java.lang.Exception     // Catch:{ Exception -> 0x01b0 }
            r3.<init>(r10, r4)     // Catch:{ Exception -> 0x01b0 }
            goto L_0x0168
        L_0x0167:
            r3 = r6
        L_0x0168:
            if (r11 == 0) goto L_0x016d
            r3.setStackTrace(r11)
        L_0x016d:
            if (r2 == 0) goto L_0x01af
            if (r9 == 0) goto L_0x0184
            java.lang.Class r4 = r1.clazz
            if (r9 != r4) goto L_0x0177
            r0 = r1
            goto L_0x0185
        L_0x0177:
            com.alibaba.fastjson.parser.ParserConfig r0 = r0.config
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r0 = r0.getDeserializer(r9)
            boolean r4 = r0 instanceof com.alibaba.fastjson.parser.JavaBeanDeserializer
            if (r4 == 0) goto L_0x0184
            com.alibaba.fastjson.parser.JavaBeanDeserializer r0 = (com.alibaba.fastjson.parser.JavaBeanDeserializer) r0
            goto L_0x0185
        L_0x0184:
            r0 = 0
        L_0x0185:
            if (r0 == 0) goto L_0x01af
            java.util.Set r2 = r2.entrySet()
            java.util.Iterator r2 = r2.iterator()
        L_0x018f:
            boolean r4 = r2.hasNext()
            if (r4 == 0) goto L_0x01af
            java.lang.Object r4 = r2.next()
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4
            java.lang.Object r5 = r4.getKey()
            java.lang.String r5 = (java.lang.String) r5
            java.lang.Object r4 = r4.getValue()
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r5 = r0.getFieldDeserializer(r5)
            if (r5 == 0) goto L_0x018f
            r5.setValue((java.lang.Object) r3, (java.lang.Object) r4)
            goto L_0x018f
        L_0x01af:
            return r3
        L_0x01b0:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException
            java.lang.String r3 = "create instance error"
            r2.<init>(r3, r0)
            throw r2
        L_0x01b9:
            r6 = 0
            r7 = 2
            goto L_0x0039
        L_0x01bd:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r2 = "syntax error"
            r0.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.ThrowableDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }
}
