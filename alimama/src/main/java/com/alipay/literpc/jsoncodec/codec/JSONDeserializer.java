package com.alipay.literpc.jsoncodec.codec;

import com.alipay.literpc.jsoncodec.util.ClassUtil;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.alipay.JSONArray;
import org.json.alipay.JSONObject;

public class JSONDeserializer {
    static List<ObjectDeserializer> deserializers = new ArrayList();

    static {
        deserializers.add(new SimpleClassCodec());
        deserializers.add(new EnumCodec());
        deserializers.add(new DateCodec());
        deserializers.add(new MapCodec());
        deserializers.add(new SetDeserializer());
        deserializers.add(new CollectionCodec());
        deserializers.add(new ArrayCodec());
        deserializers.add(new JavaBeanCodec());
    }

    public static final Object deserialize(String str, Type type) throws Exception {
        if (str == null || str.length() == 0) {
            return null;
        }
        String trim = str.trim();
        if (trim.startsWith(Operators.ARRAY_START_STR) && trim.endsWith(Operators.ARRAY_END_STR)) {
            return deserialize0(new JSONArray(trim), type);
        }
        if (!trim.startsWith(Operators.BLOCK_START_STR) || !trim.endsWith("}")) {
            return deserialize0(trim, type);
        }
        return deserialize0(new JSONObject(trim), type);
    }

    public static final <T> T deserialize0(Object obj, Type type) throws Exception {
        T deserialize;
        for (ObjectDeserializer next : deserializers) {
            if (next.match(ClassUtil.getRawClass(type)) && (deserialize = next.deserialize(obj, type)) != null) {
                return deserialize;
            }
        }
        return null;
    }
}
