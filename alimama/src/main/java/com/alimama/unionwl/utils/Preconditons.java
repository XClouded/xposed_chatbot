package com.alimama.unionwl.utils;

import java.util.Collection;

public class Preconditons {
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean isCollectionValid(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean checkElementIndex(int i, Collection collection) {
        return collection != null && i < collection.size();
    }
}
