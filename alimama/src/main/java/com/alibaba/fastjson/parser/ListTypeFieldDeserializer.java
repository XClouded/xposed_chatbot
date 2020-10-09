package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ListTypeFieldDeserializer extends FieldDeserializer {
    private final boolean array;
    private ObjectDeserializer deserializer;
    private final Type itemType;

    public ListTypeFieldDeserializer(ParserConfig parserConfig, Class<?> cls, FieldInfo fieldInfo) {
        super(cls, fieldInfo, 14);
        Type type = fieldInfo.fieldType;
        Class<?> cls2 = fieldInfo.fieldClass;
        if (cls2.isArray()) {
            this.itemType = cls2.getComponentType();
            this.array = true;
            return;
        }
        this.itemType = TypeUtils.getCollectionItemType(type);
        this.array = false;
    }

    public void parseField(DefaultJSONParser defaultJSONParser, Object obj, Type type, Map<String, Object> map) {
        List list;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = jSONLexer.token();
        JSONArray jSONArray = null;
        if (i == 8 || (i == 4 && jSONLexer.stringVal().length() == 0)) {
            setValue(obj, (Object) null);
            defaultJSONParser.lexer.nextToken();
            return;
        }
        if (this.array) {
            jSONArray = new JSONArray();
            jSONArray.setComponentType(this.itemType);
            list = jSONArray;
        } else {
            list = new ArrayList();
        }
        ParseContext parseContext = defaultJSONParser.contex;
        defaultJSONParser.setContext(parseContext, obj, this.fieldInfo.name);
        parseArray(defaultJSONParser, type, list);
        defaultJSONParser.setContext(parseContext);
        List list2 = list;
        if (this.array) {
            Object array2 = list.toArray((Object[]) Array.newInstance((Class) this.itemType, list.size()));
            jSONArray.setRelatedArray(array2);
            list2 = array2;
        }
        if (obj == null) {
            map.put(this.fieldInfo.name, list2);
        } else {
            setValue(obj, list2);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void parseArray(com.alibaba.fastjson.parser.DefaultJSONParser r18, java.lang.reflect.Type r19, java.util.Collection r20) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r20
            java.lang.reflect.Type r4 = r0.itemType
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r5 = r0.deserializer
            boolean r6 = r2 instanceof java.lang.reflect.ParameterizedType
            r7 = 0
            r8 = 1
            if (r6 == 0) goto L_0x00ce
            boolean r6 = r4 instanceof java.lang.reflect.TypeVariable
            r9 = 0
            r10 = -1
            if (r6 == 0) goto L_0x0067
            r6 = r4
            java.lang.reflect.TypeVariable r6 = (java.lang.reflect.TypeVariable) r6
            r11 = r2
            java.lang.reflect.ParameterizedType r11 = (java.lang.reflect.ParameterizedType) r11
            java.lang.reflect.Type r12 = r11.getRawType()
            boolean r12 = r12 instanceof java.lang.Class
            if (r12 == 0) goto L_0x002c
            java.lang.reflect.Type r9 = r11.getRawType()
            java.lang.Class r9 = (java.lang.Class) r9
        L_0x002c:
            if (r9 == 0) goto L_0x004e
            java.lang.reflect.TypeVariable[] r12 = r9.getTypeParameters()
            int r12 = r12.length
            r13 = 0
        L_0x0034:
            if (r13 >= r12) goto L_0x004e
            java.lang.reflect.TypeVariable[] r14 = r9.getTypeParameters()
            r14 = r14[r13]
            java.lang.String r14 = r14.getName()
            java.lang.String r15 = r6.getName()
            boolean r14 = r14.equals(r15)
            if (r14 == 0) goto L_0x004b
            goto L_0x004f
        L_0x004b:
            int r13 = r13 + 1
            goto L_0x0034
        L_0x004e:
            r13 = -1
        L_0x004f:
            if (r13 == r10) goto L_0x010a
            java.lang.reflect.Type[] r4 = r11.getActualTypeArguments()
            r4 = r4[r13]
            java.lang.reflect.Type r6 = r0.itemType
            boolean r6 = r4.equals(r6)
            if (r6 != 0) goto L_0x010a
            com.alibaba.fastjson.parser.ParserConfig r5 = r1.config
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r5 = r5.getDeserializer(r4)
            goto L_0x010a
        L_0x0067:
            boolean r6 = r4 instanceof java.lang.reflect.ParameterizedType
            if (r6 == 0) goto L_0x010a
            r6 = r4
            java.lang.reflect.ParameterizedType r6 = (java.lang.reflect.ParameterizedType) r6
            java.lang.reflect.Type[] r11 = r6.getActualTypeArguments()
            int r12 = r11.length
            if (r12 != r8) goto L_0x010a
            r12 = r11[r7]
            boolean r12 = r12 instanceof java.lang.reflect.TypeVariable
            if (r12 == 0) goto L_0x010a
            r12 = r11[r7]
            java.lang.reflect.TypeVariable r12 = (java.lang.reflect.TypeVariable) r12
            r13 = r2
            java.lang.reflect.ParameterizedType r13 = (java.lang.reflect.ParameterizedType) r13
            java.lang.reflect.Type r14 = r13.getRawType()
            boolean r14 = r14 instanceof java.lang.Class
            if (r14 == 0) goto L_0x0090
            java.lang.reflect.Type r9 = r13.getRawType()
            java.lang.Class r9 = (java.lang.Class) r9
        L_0x0090:
            if (r9 == 0) goto L_0x00b4
            java.lang.reflect.TypeVariable[] r14 = r9.getTypeParameters()
            int r14 = r14.length
            r15 = 0
        L_0x0098:
            if (r15 >= r14) goto L_0x00b4
            java.lang.reflect.TypeVariable[] r16 = r9.getTypeParameters()
            r16 = r16[r15]
            java.lang.String r8 = r16.getName()
            java.lang.String r7 = r12.getName()
            boolean r7 = r8.equals(r7)
            if (r7 == 0) goto L_0x00af
            goto L_0x00b5
        L_0x00af:
            int r15 = r15 + 1
            r7 = 0
            r8 = 1
            goto L_0x0098
        L_0x00b4:
            r15 = -1
        L_0x00b5:
            if (r15 == r10) goto L_0x010a
            java.lang.reflect.Type[] r4 = r13.getActualTypeArguments()
            r4 = r4[r15]
            r7 = 0
            r11[r7] = r4
            com.alibaba.fastjson.util.ParameterizedTypeImpl r4 = new com.alibaba.fastjson.util.ParameterizedTypeImpl
            java.lang.reflect.Type r7 = r6.getOwnerType()
            java.lang.reflect.Type r6 = r6.getRawType()
            r4.<init>(r11, r7, r6)
            goto L_0x010a
        L_0x00ce:
            boolean r6 = r4 instanceof java.lang.reflect.TypeVariable
            if (r6 == 0) goto L_0x010a
            boolean r6 = r2 instanceof java.lang.Class
            if (r6 == 0) goto L_0x010a
            r6 = r2
            java.lang.Class r6 = (java.lang.Class) r6
            r7 = r4
            java.lang.reflect.TypeVariable r7 = (java.lang.reflect.TypeVariable) r7
            r6.getTypeParameters()
            java.lang.reflect.TypeVariable[] r8 = r6.getTypeParameters()
            int r8 = r8.length
            r9 = 0
        L_0x00e5:
            if (r9 >= r8) goto L_0x010a
            java.lang.reflect.TypeVariable[] r10 = r6.getTypeParameters()
            r10 = r10[r9]
            java.lang.String r11 = r10.getName()
            java.lang.String r12 = r7.getName()
            boolean r11 = r11.equals(r12)
            if (r11 == 0) goto L_0x0107
            java.lang.reflect.Type[] r6 = r10.getBounds()
            int r7 = r6.length
            r8 = 1
            if (r7 != r8) goto L_0x010a
            r7 = 0
            r4 = r6[r7]
            goto L_0x010a
        L_0x0107:
            int r9 = r9 + 1
            goto L_0x00e5
        L_0x010a:
            com.alibaba.fastjson.parser.JSONLexer r6 = r1.lexer
            if (r5 != 0) goto L_0x0116
            com.alibaba.fastjson.parser.ParserConfig r5 = r1.config
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r5 = r5.getDeserializer(r4)
            r0.deserializer = r5
        L_0x0116:
            int r7 = r6.token
            r8 = 12
            r9 = 14
            if (r7 == r9) goto L_0x0162
            int r7 = r6.token
            if (r7 != r8) goto L_0x012f
            r7 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf(r7)
            java.lang.Object r1 = r5.deserialze(r1, r4, r2)
            r3.add(r1)
            return
        L_0x012f:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "exepct '[', but "
            r1.append(r3)
            int r3 = r6.token
            java.lang.String r3 = com.alibaba.fastjson.parser.JSONToken.name(r3)
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            if (r2 == 0) goto L_0x015c
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r1)
            java.lang.String r1 = ", type : "
            r3.append(r1)
            r3.append(r2)
            java.lang.String r1 = r3.toString()
        L_0x015c:
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException
            r2.<init>(r1)
            throw r2
        L_0x0162:
            r7 = 0
            char r2 = r6.ch
            r10 = 15
            r11 = 34
            r12 = 123(0x7b, float:1.72E-43)
            r13 = 91
            r14 = 26
            if (r2 != r13) goto L_0x0189
            int r2 = r6.bp
            r15 = 1
            int r2 = r2 + r15
            r6.bp = r2
            int r15 = r6.len
            if (r2 < r15) goto L_0x017e
            r2 = 26
            goto L_0x0184
        L_0x017e:
            java.lang.String r15 = r6.text
            char r2 = r15.charAt(r2)
        L_0x0184:
            r6.ch = r2
            r6.token = r9
            goto L_0x01c8
        L_0x0189:
            if (r2 != r12) goto L_0x01a3
            int r2 = r6.bp
            r15 = 1
            int r2 = r2 + r15
            r6.bp = r2
            int r15 = r6.len
            if (r2 < r15) goto L_0x0198
            r2 = 26
            goto L_0x019e
        L_0x0198:
            java.lang.String r15 = r6.text
            char r2 = r15.charAt(r2)
        L_0x019e:
            r6.ch = r2
            r6.token = r8
            goto L_0x01c8
        L_0x01a3:
            if (r2 != r11) goto L_0x01a9
            r6.scanString()
            goto L_0x01c8
        L_0x01a9:
            r15 = 93
            if (r2 != r15) goto L_0x01c5
            int r2 = r6.bp
            r15 = 1
            int r2 = r2 + r15
            r6.bp = r2
            int r15 = r6.len
            if (r2 < r15) goto L_0x01ba
            r2 = 26
            goto L_0x01c0
        L_0x01ba:
            java.lang.String r15 = r6.text
            char r2 = r15.charAt(r2)
        L_0x01c0:
            r6.ch = r2
            r6.token = r10
            goto L_0x01c8
        L_0x01c5:
            r6.nextToken()
        L_0x01c8:
            int r2 = r6.token
            r15 = 16
            if (r2 != r15) goto L_0x01d2
            r6.nextToken()
            goto L_0x01c8
        L_0x01d2:
            int r2 = r6.token
            if (r2 != r10) goto L_0x01f6
            char r1 = r6.ch
            r2 = 44
            if (r1 != r2) goto L_0x01f2
            int r1 = r6.bp
            r2 = 1
            int r1 = r1 + r2
            r6.bp = r1
            int r2 = r6.len
            if (r1 < r2) goto L_0x01e7
            goto L_0x01ed
        L_0x01e7:
            java.lang.String r2 = r6.text
            char r14 = r2.charAt(r1)
        L_0x01ed:
            r6.ch = r14
            r6.token = r15
            goto L_0x01f5
        L_0x01f2:
            r6.nextToken()
        L_0x01f5:
            return
        L_0x01f6:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r7)
            java.lang.Object r2 = r5.deserialze(r1, r4, r2)
            r3.add(r2)
            int r2 = r1.resolveStatus
            r10 = 1
            if (r2 != r10) goto L_0x0209
            r1.checkListResolve(r3)
        L_0x0209:
            int r2 = r6.token
            if (r2 != r15) goto L_0x024d
            char r2 = r6.ch
            if (r2 != r13) goto L_0x0229
            int r2 = r6.bp
            int r2 = r2 + r10
            r6.bp = r2
            int r10 = r6.len
            if (r2 < r10) goto L_0x021d
            r2 = 26
            goto L_0x0223
        L_0x021d:
            java.lang.String r10 = r6.text
            char r2 = r10.charAt(r2)
        L_0x0223:
            r6.ch = r2
            r6.token = r9
            r10 = 1
            goto L_0x024d
        L_0x0229:
            if (r2 != r12) goto L_0x0243
            int r2 = r6.bp
            r10 = 1
            int r2 = r2 + r10
            r6.bp = r2
            int r15 = r6.len
            if (r2 < r15) goto L_0x0238
            r2 = 26
            goto L_0x023e
        L_0x0238:
            java.lang.String r15 = r6.text
            char r2 = r15.charAt(r2)
        L_0x023e:
            r6.ch = r2
            r6.token = r8
            goto L_0x024d
        L_0x0243:
            r10 = 1
            if (r2 != r11) goto L_0x024a
            r6.scanString()
            goto L_0x024d
        L_0x024a:
            r6.nextToken()
        L_0x024d:
            int r7 = r7 + 1
            r10 = 15
            goto L_0x01c8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.ListTypeFieldDeserializer.parseArray(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.util.Collection):void");
    }
}
