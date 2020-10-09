package com.alibaba.taffy.core.collection;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class PriorityBlockingArraySet<E> extends AbstractSet<E> implements Serializable {
    private final PriorityBlockingArrayList<E> al;

    public PriorityBlockingArraySet() {
        this.al = new PriorityBlockingArrayList<>();
    }

    public PriorityBlockingArraySet(Comparator<? super E> comparator) {
        this.al = new PriorityBlockingArrayList<>(comparator);
    }

    public PriorityBlockingArraySet(Collection<? extends E> collection) {
        this(collection, (Comparator) null);
    }

    public PriorityBlockingArraySet(Collection<? extends E> collection, Comparator<? super E> comparator) {
        if (collection.getClass() == PriorityBlockingArraySet.class) {
            this.al = new PriorityBlockingArrayList<>(((PriorityBlockingArraySet) collection).al, comparator);
            return;
        }
        this.al = new PriorityBlockingArrayList<>(comparator);
        this.al.addAllAbsent(collection);
    }

    public int size() {
        return this.al.size();
    }

    public boolean isEmpty() {
        return this.al.isEmpty();
    }

    public boolean contains(Object obj) {
        return this.al.contains(obj);
    }

    public Object[] toArray() {
        return this.al.toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return this.al.toArray(tArr);
    }

    public void clear() {
        this.al.clear();
    }

    public boolean remove(Object obj) {
        return this.al.remove(obj);
    }

    public boolean add(E e) {
        return this.al.addIfAbsent(e);
    }

    public boolean containsAll(Collection<?> collection) {
        return this.al.containsAll(collection);
    }

    public boolean addAll(Collection<? extends E> collection) {
        return this.al.addAllAbsent(collection) > 0;
    }

    public boolean removeAll(Collection<?> collection) {
        return this.al.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return this.al.retainAll(collection);
    }

    public Iterator<E> iterator() {
        return this.al.iterator();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Object[] array = this.al.getArray();
        int length = array.length;
        boolean[] zArr = new boolean[length];
        int i = 0;
        for (Object next : (Set) obj) {
            i++;
            if (i > length) {
                return false;
            }
            int i2 = 0;
            while (i2 < length) {
                if (zArr[i2] || !eq(next, array[i2])) {
                    i2++;
                } else {
                    zArr[i2] = true;
                }
            }
            return false;
        }
        if (i == length) {
            return true;
        }
        return false;
    }

    private static boolean eq(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }
}
