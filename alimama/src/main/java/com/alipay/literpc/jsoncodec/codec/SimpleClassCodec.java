package com.alipay.literpc.jsoncodec.codec;

import com.alipay.literpc.jsoncodec.util.ClassUtil;
import java.lang.reflect.Type;

public class SimpleClassCodec implements ObjectDeserializer, ObjectSerializer {
    public Object deserialize(Object obj, Type type) throws Exception {
        return obj;
    }

    public Object serialize(Object obj) throws Exception {
        return obj;
    }

    public boolean match(Class<?> cls) {
        return ClassUtil.isSimpleType(cls);
    }
}
