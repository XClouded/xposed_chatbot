package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class MapDeserializer implements ObjectDeserializer {
    public static MapDeserializer instance = new MapDeserializer();

    MapDeserializer() {
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        if (type == JSONObject.class && defaultJSONParser.fieldTypeResolver == null) {
            return defaultJSONParser.parseObject();
        }
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token == 8) {
            jSONLexer.nextToken(16);
            return null;
        }
        Map<?, ?> createMap = createMap(type);
        ParseContext parseContext = defaultJSONParser.contex;
        try {
            defaultJSONParser.setContext(parseContext, createMap, obj);
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type type2 = parameterizedType.getActualTypeArguments()[0];
                Type type3 = parameterizedType.getActualTypeArguments()[1];
                if (String.class == type2) {
                    return parseMap(defaultJSONParser, createMap, type3, obj);
                }
                T parseMap = parseMap(defaultJSONParser, createMap, type2, type3, obj);
                defaultJSONParser.setContext(parseContext);
                return parseMap;
            }
            T parseObject = defaultJSONParser.parseObject((Map) createMap, obj);
            defaultJSONParser.setContext(parseContext);
            return parseObject;
        } finally {
            defaultJSONParser.setContext(parseContext);
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r9 = r8.config.getDeserializer(r2);
        r0.nextToken(16);
        r8.resolveStatus = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00df, code lost:
        if (r1 == null) goto L_0x00e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00e3, code lost:
        if ((r11 instanceof java.lang.Integer) != false) goto L_0x00e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00e5, code lost:
        r8.popContext();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00e8, code lost:
        r9 = (java.util.Map) r9.deserialze(r8, r2, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00ee, code lost:
        r8.setContext(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00f1, code lost:
        return r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map parseMap(com.alibaba.fastjson.parser.DefaultJSONParser r8, java.util.Map<java.lang.String, java.lang.Object> r9, java.lang.reflect.Type r10, java.lang.Object r11) {
        /*
            com.alibaba.fastjson.parser.JSONLexer r0 = r8.lexer
            int r1 = r0.token
            r2 = 12
            if (r1 != r2) goto L_0x0152
            com.alibaba.fastjson.parser.ParseContext r1 = r8.contex
        L_0x000a:
            r0.skipWhitespace()     // Catch:{ all -> 0x014d }
            char r2 = r0.ch     // Catch:{ all -> 0x014d }
        L_0x000f:
            r3 = 44
            if (r2 != r3) goto L_0x001c
            r0.next()     // Catch:{ all -> 0x014d }
            r0.skipWhitespace()     // Catch:{ all -> 0x014d }
            char r2 = r0.ch     // Catch:{ all -> 0x014d }
            goto L_0x000f
        L_0x001c:
            r3 = 0
            r4 = 58
            r5 = 34
            r6 = 16
            if (r2 != r5) goto L_0x004e
            com.alibaba.fastjson.parser.SymbolTable r2 = r8.symbolTable     // Catch:{ all -> 0x014d }
            java.lang.String r2 = r0.scanSymbol(r2, r5)     // Catch:{ all -> 0x014d }
            r0.skipWhitespace()     // Catch:{ all -> 0x014d }
            char r7 = r0.ch     // Catch:{ all -> 0x014d }
            if (r7 != r4) goto L_0x0033
            goto L_0x0098
        L_0x0033:
            com.alibaba.fastjson.JSONException r9 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x014d }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x014d }
            r10.<init>()     // Catch:{ all -> 0x014d }
            java.lang.String r11 = "syntax error, "
            r10.append(r11)     // Catch:{ all -> 0x014d }
            java.lang.String r11 = r0.info()     // Catch:{ all -> 0x014d }
            r10.append(r11)     // Catch:{ all -> 0x014d }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x014d }
            r9.<init>(r10)     // Catch:{ all -> 0x014d }
            throw r9     // Catch:{ all -> 0x014d }
        L_0x004e:
            r7 = 125(0x7d, float:1.75E-43)
            if (r2 != r7) goto L_0x005e
            r0.next()     // Catch:{ all -> 0x014d }
            r0.sp = r3     // Catch:{ all -> 0x014d }
            r0.nextToken(r6)     // Catch:{ all -> 0x014d }
            r8.setContext(r1)
            return r9
        L_0x005e:
            r7 = 39
            if (r2 != r7) goto L_0x008b
            com.alibaba.fastjson.parser.SymbolTable r2 = r8.symbolTable     // Catch:{ all -> 0x014d }
            java.lang.String r2 = r0.scanSymbol(r2, r7)     // Catch:{ all -> 0x014d }
            r0.skipWhitespace()     // Catch:{ all -> 0x014d }
            char r7 = r0.ch     // Catch:{ all -> 0x014d }
            if (r7 != r4) goto L_0x0070
            goto L_0x0098
        L_0x0070:
            com.alibaba.fastjson.JSONException r9 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x014d }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x014d }
            r10.<init>()     // Catch:{ all -> 0x014d }
            java.lang.String r11 = "syntax error, "
            r10.append(r11)     // Catch:{ all -> 0x014d }
            java.lang.String r11 = r0.info()     // Catch:{ all -> 0x014d }
            r10.append(r11)     // Catch:{ all -> 0x014d }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x014d }
            r9.<init>(r10)     // Catch:{ all -> 0x014d }
            throw r9     // Catch:{ all -> 0x014d }
        L_0x008b:
            com.alibaba.fastjson.parser.SymbolTable r2 = r8.symbolTable     // Catch:{ all -> 0x014d }
            java.lang.String r2 = r0.scanSymbolUnQuoted(r2)     // Catch:{ all -> 0x014d }
            r0.skipWhitespace()     // Catch:{ all -> 0x014d }
            char r7 = r0.ch     // Catch:{ all -> 0x014d }
            if (r7 != r4) goto L_0x012c
        L_0x0098:
            r0.next()     // Catch:{ all -> 0x014d }
            r0.skipWhitespace()     // Catch:{ all -> 0x014d }
            char r4 = r0.ch     // Catch:{ all -> 0x014d }
            r0.sp = r3     // Catch:{ all -> 0x014d }
            java.lang.String r3 = "@type"
            r4 = 13
            r7 = 0
            if (r2 != r3) goto L_0x00f2
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x014d }
            boolean r3 = r0.isEnabled(r3)     // Catch:{ all -> 0x014d }
            if (r3 != 0) goto L_0x00f2
            com.alibaba.fastjson.parser.SymbolTable r2 = r8.symbolTable     // Catch:{ all -> 0x014d }
            java.lang.String r2 = r0.scanSymbol(r2, r5)     // Catch:{ all -> 0x014d }
            com.alibaba.fastjson.parser.ParserConfig r3 = r8.config     // Catch:{ all -> 0x014d }
            int r5 = r0.features     // Catch:{ all -> 0x014d }
            java.lang.Class r2 = r3.checkAutoType(r2, r7, r5)     // Catch:{ all -> 0x014d }
            java.lang.Class r3 = r9.getClass()     // Catch:{ all -> 0x014d }
            if (r2 != r3) goto L_0x00d3
            r0.nextToken(r6)     // Catch:{ all -> 0x014d }
            int r2 = r0.token     // Catch:{ all -> 0x014d }
            if (r2 != r4) goto L_0x000a
            r0.nextToken(r6)     // Catch:{ all -> 0x014d }
            r8.setContext(r1)
            return r9
        L_0x00d3:
            com.alibaba.fastjson.parser.ParserConfig r9 = r8.config     // Catch:{ all -> 0x014d }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r9 = r9.getDeserializer(r2)     // Catch:{ all -> 0x014d }
            r0.nextToken(r6)     // Catch:{ all -> 0x014d }
            r10 = 2
            r8.resolveStatus = r10     // Catch:{ all -> 0x014d }
            if (r1 == 0) goto L_0x00e8
            boolean r10 = r11 instanceof java.lang.Integer     // Catch:{ all -> 0x014d }
            if (r10 != 0) goto L_0x00e8
            r8.popContext()     // Catch:{ all -> 0x014d }
        L_0x00e8:
            java.lang.Object r9 = r9.deserialze(r8, r2, r11)     // Catch:{ all -> 0x014d }
            java.util.Map r9 = (java.util.Map) r9     // Catch:{ all -> 0x014d }
            r8.setContext(r1)
            return r9
        L_0x00f2:
            r0.nextToken()     // Catch:{ all -> 0x014d }
            r8.setContext(r1)     // Catch:{ all -> 0x014d }
            int r3 = r0.token     // Catch:{ all -> 0x014d }
            r5 = 8
            if (r3 != r5) goto L_0x0102
            r0.nextToken()     // Catch:{ all -> 0x014d }
            goto L_0x0106
        L_0x0102:
            java.lang.Object r7 = r8.parseObject((java.lang.reflect.Type) r10, (java.lang.Object) r2)     // Catch:{ all -> 0x014d }
        L_0x0106:
            r9.put(r2, r7)     // Catch:{ all -> 0x014d }
            int r3 = r8.resolveStatus     // Catch:{ all -> 0x014d }
            r5 = 1
            if (r3 != r5) goto L_0x0111
            r8.checkMapResolve(r9, r2)     // Catch:{ all -> 0x014d }
        L_0x0111:
            r8.setContext(r1, r7, r2)     // Catch:{ all -> 0x014d }
            int r2 = r0.token     // Catch:{ all -> 0x014d }
            r3 = 20
            if (r2 == r3) goto L_0x0128
            r3 = 15
            if (r2 != r3) goto L_0x011f
            goto L_0x0128
        L_0x011f:
            if (r2 != r4) goto L_0x000a
            r0.nextToken()     // Catch:{ all -> 0x014d }
            r8.setContext(r1)
            return r9
        L_0x0128:
            r8.setContext(r1)
            return r9
        L_0x012c:
            com.alibaba.fastjson.JSONException r9 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x014d }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x014d }
            r10.<init>()     // Catch:{ all -> 0x014d }
            java.lang.String r11 = "expect ':' at "
            r10.append(r11)     // Catch:{ all -> 0x014d }
            int r11 = r0.pos     // Catch:{ all -> 0x014d }
            r10.append(r11)     // Catch:{ all -> 0x014d }
            java.lang.String r11 = ", actual "
            r10.append(r11)     // Catch:{ all -> 0x014d }
            r10.append(r7)     // Catch:{ all -> 0x014d }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x014d }
            r9.<init>(r10)     // Catch:{ all -> 0x014d }
            throw r9     // Catch:{ all -> 0x014d }
        L_0x014d:
            r9 = move-exception
            r8.setContext(r1)
            throw r9
        L_0x0152:
            com.alibaba.fastjson.JSONException r8 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "syntax error, expect {, actual "
            r9.append(r10)
            int r10 = r0.token
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            r8.<init>(r9)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.MapDeserializer.parseMap(com.alibaba.fastjson.parser.DefaultJSONParser, java.util.Map, java.lang.reflect.Type, java.lang.Object):java.util.Map");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006d, code lost:
        r4.nextTokenWithChar(com.taobao.weex.el.parse.Operators.CONDITION_IF_MIDDLE);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0072, code lost:
        if (r4.token != 4) goto L_0x00bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0074, code lost:
        r0 = r4.stringVal();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007e, code lost:
        if ("..".equals(r0) == false) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0080, code lost:
        r12 = r8.parent.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x008b, code lost:
        if ("$".equals(r0) == false) goto L_0x0098;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x008d, code lost:
        r0 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0090, code lost:
        if (r0.parent == null) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0092, code lost:
        r0 = r0.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0095, code lost:
        r12 = r0.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0098, code lost:
        r1.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r8, r0));
        r1.resolveStatus = 1;
        r12 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a3, code lost:
        r4.nextToken(13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a8, code lost:
        if (r4.token != 13) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00aa, code lost:
        r4.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00af, code lost:
        r1.setContext(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b2, code lost:
        return r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00ba, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00d5, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref, " + com.alibaba.fastjson.parser.JSONToken.name(r9));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object parseMap(com.alibaba.fastjson.parser.DefaultJSONParser r16, java.util.Map<java.lang.Object, java.lang.Object> r17, java.lang.reflect.Type r18, java.lang.reflect.Type r19, java.lang.Object r20) {
        /*
            r1 = r16
            r0 = r17
            r2 = r18
            r3 = r19
            com.alibaba.fastjson.parser.JSONLexer r4 = r1.lexer
            int r5 = r4.token
            r6 = 16
            r7 = 12
            if (r5 == r7) goto L_0x0030
            if (r5 != r6) goto L_0x0015
            goto L_0x0030
        L_0x0015:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "syntax error, expect {, actual "
            r1.append(r2)
            java.lang.String r2 = com.alibaba.fastjson.parser.JSONToken.name(r5)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0030:
            com.alibaba.fastjson.parser.ParserConfig r5 = r1.config
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r5 = r5.getDeserializer(r2)
            com.alibaba.fastjson.parser.ParserConfig r7 = r1.config
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r7 = r7.getDeserializer(r3)
            r4.nextToken()
            com.alibaba.fastjson.parser.ParseContext r8 = r1.contex
        L_0x0041:
            int r9 = r4.token     // Catch:{ all -> 0x014a }
            r10 = 13
            if (r9 != r10) goto L_0x004e
            r4.nextToken(r6)     // Catch:{ all -> 0x014a }
            r1.setContext(r8)
            return r0
        L_0x004e:
            r11 = 58
            r13 = 1
            r14 = 4
            if (r9 != r14) goto L_0x00d6
            int r15 = r4.sp     // Catch:{ all -> 0x014a }
            if (r15 != r14) goto L_0x00d6
            java.lang.String r15 = r4.text     // Catch:{ all -> 0x014a }
            java.lang.String r12 = "$ref"
            int r6 = r4.np     // Catch:{ all -> 0x014a }
            int r6 = r6 + r13
            boolean r6 = r15.startsWith(r12, r6)     // Catch:{ all -> 0x014a }
            if (r6 == 0) goto L_0x00d6
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x014a }
            boolean r6 = r4.isEnabled(r6)     // Catch:{ all -> 0x014a }
            if (r6 != 0) goto L_0x00d6
            r4.nextTokenWithChar(r11)     // Catch:{ all -> 0x014a }
            int r0 = r4.token     // Catch:{ all -> 0x014a }
            if (r0 != r14) goto L_0x00bb
            java.lang.String r0 = r4.stringVal()     // Catch:{ all -> 0x014a }
            java.lang.String r2 = ".."
            boolean r2 = r2.equals(r0)     // Catch:{ all -> 0x014a }
            if (r2 == 0) goto L_0x0085
            com.alibaba.fastjson.parser.ParseContext r0 = r8.parent     // Catch:{ all -> 0x014a }
            java.lang.Object r12 = r0.object     // Catch:{ all -> 0x014a }
            goto L_0x00a3
        L_0x0085:
            java.lang.String r2 = "$"
            boolean r2 = r2.equals(r0)     // Catch:{ all -> 0x014a }
            if (r2 == 0) goto L_0x0098
            r0 = r8
        L_0x008e:
            com.alibaba.fastjson.parser.ParseContext r2 = r0.parent     // Catch:{ all -> 0x014a }
            if (r2 == 0) goto L_0x0095
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x014a }
            goto L_0x008e
        L_0x0095:
            java.lang.Object r12 = r0.object     // Catch:{ all -> 0x014a }
            goto L_0x00a3
        L_0x0098:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r2 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x014a }
            r2.<init>(r8, r0)     // Catch:{ all -> 0x014a }
            r1.addResolveTask(r2)     // Catch:{ all -> 0x014a }
            r1.resolveStatus = r13     // Catch:{ all -> 0x014a }
            r12 = 0
        L_0x00a3:
            r4.nextToken(r10)     // Catch:{ all -> 0x014a }
            int r0 = r4.token     // Catch:{ all -> 0x014a }
            if (r0 != r10) goto L_0x00b3
            r0 = 16
            r4.nextToken(r0)     // Catch:{ all -> 0x014a }
            r1.setContext(r8)
            return r12
        L_0x00b3:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x014a }
            java.lang.String r2 = "illegal ref"
            r0.<init>(r2)     // Catch:{ all -> 0x014a }
            throw r0     // Catch:{ all -> 0x014a }
        L_0x00bb:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x014a }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x014a }
            r2.<init>()     // Catch:{ all -> 0x014a }
            java.lang.String r3 = "illegal ref, "
            r2.append(r3)     // Catch:{ all -> 0x014a }
            java.lang.String r3 = com.alibaba.fastjson.parser.JSONToken.name(r9)     // Catch:{ all -> 0x014a }
            r2.append(r3)     // Catch:{ all -> 0x014a }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x014a }
            r0.<init>(r2)     // Catch:{ all -> 0x014a }
            throw r0     // Catch:{ all -> 0x014a }
        L_0x00d6:
            int r6 = r17.size()     // Catch:{ all -> 0x014a }
            if (r6 != 0) goto L_0x0108
            if (r9 != r14) goto L_0x0108
            java.lang.String r6 = "@type"
            java.lang.String r9 = r4.stringVal()     // Catch:{ all -> 0x014a }
            boolean r6 = r6.equals(r9)     // Catch:{ all -> 0x014a }
            if (r6 == 0) goto L_0x0108
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x014a }
            boolean r6 = r4.isEnabled(r6)     // Catch:{ all -> 0x014a }
            if (r6 != 0) goto L_0x0108
            r4.nextTokenWithChar(r11)     // Catch:{ all -> 0x014a }
            r6 = 16
            r4.nextToken(r6)     // Catch:{ all -> 0x014a }
            int r6 = r4.token     // Catch:{ all -> 0x014a }
            if (r6 != r10) goto L_0x0105
            r4.nextToken()     // Catch:{ all -> 0x014a }
            r1.setContext(r8)
            return r0
        L_0x0105:
            r4.nextToken()     // Catch:{ all -> 0x014a }
        L_0x0108:
            r6 = 0
            java.lang.Object r6 = r5.deserialze(r1, r2, r6)     // Catch:{ all -> 0x014a }
            int r9 = r4.token     // Catch:{ all -> 0x014a }
            r10 = 17
            if (r9 != r10) goto L_0x0131
            r4.nextToken()     // Catch:{ all -> 0x014a }
            java.lang.Object r9 = r7.deserialze(r1, r3, r6)     // Catch:{ all -> 0x014a }
            int r10 = r1.resolveStatus     // Catch:{ all -> 0x014a }
            if (r10 != r13) goto L_0x0121
            r1.checkMapResolve(r0, r6)     // Catch:{ all -> 0x014a }
        L_0x0121:
            r0.put(r6, r9)     // Catch:{ all -> 0x014a }
            int r6 = r4.token     // Catch:{ all -> 0x014a }
            r9 = 16
            if (r6 != r9) goto L_0x012d
            r4.nextToken()     // Catch:{ all -> 0x014a }
        L_0x012d:
            r6 = 16
            goto L_0x0041
        L_0x0131:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x014a }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x014a }
            r2.<init>()     // Catch:{ all -> 0x014a }
            java.lang.String r3 = "syntax error, expect :, actual "
            r2.append(r3)     // Catch:{ all -> 0x014a }
            int r3 = r4.token     // Catch:{ all -> 0x014a }
            r2.append(r3)     // Catch:{ all -> 0x014a }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x014a }
            r0.<init>(r2)     // Catch:{ all -> 0x014a }
            throw r0     // Catch:{ all -> 0x014a }
        L_0x014a:
            r0 = move-exception
            r1.setContext(r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.MapDeserializer.parseMap(com.alibaba.fastjson.parser.DefaultJSONParser, java.util.Map, java.lang.reflect.Type, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    public Map<?, ?> createMap(Type type) {
        if (type == Properties.class) {
            return new Properties();
        }
        if (type == Hashtable.class) {
            return new Hashtable();
        }
        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }
        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }
        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        if (type == Map.class || type == HashMap.class) {
            return new HashMap();
        }
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (type == JSONObject.class) {
            return new JSONObject();
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (EnumMap.class.equals(rawType)) {
                return new EnumMap((Class) parameterizedType.getActualTypeArguments()[0]);
            }
            return createMap(rawType);
        }
        Class cls = (Class) type;
        if (!cls.isInterface()) {
            try {
                return (Map) cls.newInstance();
            } catch (Exception e) {
                throw new JSONException("unsupport type " + type, e);
            }
        } else {
            throw new JSONException("unsupport type " + type);
        }
    }
}
