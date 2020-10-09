package com.alibaba.taffy.core.util.lang;

import com.alibaba.taffy.core.collection.Predicate;
import com.alibaba.taffy.core.collection.Transformer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CollectionUtil {
    public static <T> Collection<T> defaultIfNull(Collection<T> collection, Collection<T> collection2) {
        return collection == null ? collection2 : collection;
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <E> boolean isNotEmpty(Collection<E> collection) {
        return !isEmpty(collection);
    }

    public static <T> Collection<T> emptyCollection() {
        return Collections.emptyList();
    }

    public static <T> Collection<T> emptyIfNull(Collection<T> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }

    public static <T> Collection<T> union(Collection<? extends T>... collectionArr) {
        ArrayList arrayList = new ArrayList();
        for (Collection<? extends T> addAll : collectionArr) {
            arrayList.addAll(addAll);
        }
        return arrayList;
    }

    public static <T> Collection<T> sum(Collection<? extends T>... collectionArr) {
        ArrayList arrayList = new ArrayList();
        for (Collection<? extends T> it : collectionArr) {
            for (Object next : it) {
                if (!arrayList.contains(next)) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    public static <T> Collection<T> intersection(Collection<? extends T>... collectionArr) {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        for (Collection<? extends T> it : collectionArr) {
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

    public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
        if (iterable == null || predicate == null) {
            return null;
        }
        for (T next : iterable) {
            if (predicate.evaluate(next)) {
                return next;
            }
        }
        return null;
    }

    public static <T> int indexOf(Iterable<T> iterable, Predicate<T> predicate) {
        if (iterable == null || predicate == null) {
            return -1;
        }
        int i = 0;
        for (T evaluate : iterable) {
            if (predicate.evaluate(evaluate)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static <T> boolean filter(Iterable<T> iterable, Predicate<? super T> predicate) {
        boolean z = false;
        if (!(iterable == null || predicate == null)) {
            Iterator<T> it = iterable.iterator();
            while (it.hasNext()) {
                if (!predicate.evaluate(it.next())) {
                    it.remove();
                    z = true;
                }
            }
        }
        return z;
    }

    public static <C> void transform(Collection<C> collection, Transformer<? super C, ? extends C> transformer) {
        if (collection != null && transformer != null) {
            if (collection instanceof List) {
                ListIterator listIterator = ((List) collection).listIterator();
                while (listIterator.hasNext()) {
                    listIterator.set(transformer.transform(listIterator.next()));
                }
                return;
            }
            Collection<? extends C> collect = collect(collection, transformer);
            collection.clear();
            collection.addAll(collect);
        }
    }

    public static <I, O> Collection<O> collect(Iterable<I> iterable, Transformer<? super I, ? extends O> transformer) {
        return collect(iterable, transformer, iterable instanceof Collection ? new ArrayList(((Collection) iterable).size()) : new ArrayList());
    }

    public static <I, O, R extends Collection<? super O>> R collect(Iterable<? extends I> iterable, Transformer<? super I, ? extends O> transformer, R r) {
        return iterable != null ? collect(iterable.iterator(), transformer, r) : r;
    }

    public static <I, O, R extends Collection<? super O>> R collect(Iterator<? extends I> it, Transformer<? super I, ? extends O> transformer, R r) {
        if (!(it == null || transformer == null)) {
            while (it.hasNext()) {
                r.add(transformer.transform(it.next()));
            }
        }
        return r;
    }

    public static <O, R extends Collection<? super O>> R select(Iterable<? extends O> iterable, Predicate<? super O> predicate, R r) {
        if (!(iterable == null || predicate == null)) {
            for (Object next : iterable) {
                if (predicate.evaluate(next)) {
                    r.add(next);
                }
            }
        }
        return r;
    }

    public static <O, R extends Collection<? super O>> R selectRejected(Iterable<? extends O> iterable, Predicate<? super O> predicate, R r) {
        if (!(iterable == null || predicate == null)) {
            for (Object next : iterable) {
                if (!predicate.evaluate(next)) {
                    r.add(next);
                }
            }
        }
        return r;
    }
}
