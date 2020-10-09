package com.alibaba.fastjson.serializer;

import com.taobao.weex.el.parse.Operators;

public class SerialContext {
    public final int features;
    public final Object fieldName;
    public final Object object;
    public final SerialContext parent;

    public SerialContext(SerialContext serialContext, Object obj, Object obj2, int i) {
        this.parent = serialContext;
        this.object = obj;
        this.fieldName = obj2;
        this.features = i;
    }

    public String toString() {
        if (this.parent == null) {
            return "$";
        }
        if (this.fieldName instanceof Integer) {
            return this.parent.toString() + Operators.ARRAY_START_STR + this.fieldName + Operators.ARRAY_END_STR;
        }
        return this.parent.toString() + "." + this.fieldName;
    }
}
