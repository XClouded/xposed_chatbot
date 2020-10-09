package com.alibaba.taffy.core.util.lang;

import com.alibaba.taffy.core.collection.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ListUtil {
    public static <T> List<T> defaultIfNull(List<T> list, List<T> list2) {
        return list == null ? list2 : list;
    }

    public static <E> boolean isEmpty(List<E> list) {
        return list == null || list.isEmpty();
    }

    public static <E> boolean isNotEmpty(List<E> list) {
        return !isEmpty(list);
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    public static <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public static <E> List<E> union(List<? extends E>... listArr) {
        ArrayList arrayList = new ArrayList();
        for (List<? extends E> addAll : listArr) {
            arrayList.addAll(addAll);
        }
        return arrayList;
    }

    public static <E> List<E> sum(List<? extends E>... listArr) {
        ArrayList arrayList = new ArrayList();
        for (List<? extends E> it : listArr) {
            for (Object next : it) {
                if (!arrayList.contains(next)) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    public static <E> List<E> intersection(List<? extends E>... listArr) {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        for (List<? extends E> it : listArr) {
            for (Object next : it) {
                if (!hashSet.contains(next) || arrayList.contains(next)) {
                    hashSet.add(next);
                } else {
                    arrayList.add(next);
                    hashSet.remove(next);
                }
            }
        }
        hashSet.clear();
        return arrayList;
    }

    public static <E> List<E> select(Collection<? extends E> collection, Predicate<? super E> predicate) {
        return (List) CollectionUtil.select(collection, predicate, new ArrayList(collection.size()));
    }

    public static <E> List<E> selectRejected(Collection<? extends E> collection, Predicate<? super E> predicate) {
        return (List) CollectionUtil.selectRejected(collection, predicate, new ArrayList(collection.size()));
    }

    public static boolean isEqual(Collection<?> collection, Collection<?> collection2) {
        if (collection == collection2) {
            return true;
        }
        if (collection == null || collection2 == null || collection.size() != collection2.size()) {
            return false;
        }
        Iterator<?> it = collection.iterator();
        Iterator<?> it2 = collection2.iterator();
        while (it.hasNext() && it2.hasNext()) {
            Object next = it.next();
            Object next2 = it2.next();
            if (next == null) {
                if (next2 == null) {
                }
            } else if (!next.equals(next2)) {
            }
            return false;
        }
        if (it.hasNext() || it2.hasNext()) {
            return false;
        }
        return true;
    }

    public static int hashCode(Collection<?> collection) {
        int i;
        if (collection == null) {
            return 0;
        }
        int i2 = 1;
        for (Object next : collection) {
            int i3 = i2 * 31;
            if (next == null) {
                i = 0;
            } else {
                i = next.hashCode();
            }
            i2 = i3 + i;
        }
        return i2;
    }

    public static <T> int hashCode(List<T>... listArr) {
        int i;
        if (listArr == null || listArr.length == 0) {
            return 0;
        }
        int i2 = 1;
        for (List<T> list : listArr) {
            int i3 = i2 * 31;
            if (list == null) {
                i = 0;
            } else {
                i = list.hashCode();
            }
            i2 = i3 + i;
        }
        return i2;
    }

    public static <E> int indexOf(List<E> list, Predicate<E> predicate) {
        if (list == null || predicate == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (predicate.evaluate(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [com.alibaba.taffy.core.collection.Predicate<? super T>, com.alibaba.taffy.core.collection.Predicate] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T find(java.lang.Iterable<T> r0, com.alibaba.taffy.core.collection.Predicate<? super T> r1) {
        /*
            java.lang.Object r0 = com.alibaba.taffy.core.util.lang.CollectionUtil.find(r0, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.lang.ListUtil.find(java.lang.Iterable, com.alibaba.taffy.core.collection.Predicate):java.lang.Object");
    }

    public static <T> List<T> copy(List<? extends T> list, List<T> list2) {
        Collections.copy(list2, list);
        return list2;
    }

    public static <T> List<T> copy(List<? extends T> list) {
        return new ArrayList(list);
    }
}
