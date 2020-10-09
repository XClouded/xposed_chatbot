package com.alibaba.android.enhance.svg.parser;

import androidx.annotation.NonNull;
import com.alibaba.android.enhance.svg.parser.LLFunctionParser;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleFunctionParser<V> extends LLFunctionParser<String, List<V>> {

    public interface FlatMapper<V> {
        V map(String str);
    }

    public interface NonUniformMapper<V> {
        List<V> map(List<String> list);
    }

    public SimpleFunctionParser(@NonNull String str, @NonNull final FlatMapper<V> flatMapper) {
        super(str, new LLFunctionParser.Mapper<String, List<V>>() {
            public Map<String, List<V>> map(String str, List<String> list) {
                HashMap hashMap = new HashMap();
                LinkedList linkedList = new LinkedList();
                for (String map : list) {
                    linkedList.add(FlatMapper.this.map(map));
                }
                hashMap.put(str, linkedList);
                return hashMap;
            }
        });
    }

    public SimpleFunctionParser(@NonNull String str, @NonNull final NonUniformMapper<V> nonUniformMapper) {
        super(str, new LLFunctionParser.Mapper<String, List<V>>() {
            public Map<String, List<V>> map(String str, List<String> list) {
                HashMap hashMap = new HashMap();
                hashMap.put(str, NonUniformMapper.this.map(list));
                return hashMap;
            }
        });
    }
}
