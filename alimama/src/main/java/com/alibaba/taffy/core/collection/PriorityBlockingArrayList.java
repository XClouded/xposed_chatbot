package com.alibaba.taffy.core.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityBlockingArrayList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    private volatile transient Object[] array;
    private Comparator<? super E> comparator;
    final transient ReentrantLock lock;

    /* access modifiers changed from: package-private */
    public final Object[] getArray() {
        return this.array;
    }

    private void setArray(Object[] objArr) {
        this.array = objArr;
    }

    public PriorityBlockingArrayList() {
        this.lock = new ReentrantLock();
        setArray(new Object[0]);
    }

    public PriorityBlockingArrayList(Comparator<? super E> comparator2) {
        this.lock = new ReentrantLock();
        this.comparator = comparator2;
        setArray(new Object[0]);
    }

    public PriorityBlockingArrayList(Collection<? extends E> collection) {
        this(collection, (Comparator) null);
    }

    public PriorityBlockingArrayList(Collection<? extends E> collection, Comparator<? super E> comparator2) {
        this.lock = new ReentrantLock();
        this.comparator = comparator2;
        if (collection.getClass() == PriorityBlockingArrayList.class) {
            setArray(((PriorityBlockingArrayList) collection).getArray());
        } else {
            addAll(collection);
        }
    }

    public PriorityBlockingArrayList(E[] eArr) {
        this(eArr, (Comparator) null);
    }

    public PriorityBlockingArrayList(E[] eArr, Comparator<? super E> comparator2) {
        this.lock = new ReentrantLock();
        this.comparator = comparator2;
        addAll(eArr);
    }

    public int size() {
        return getArray().length;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private static boolean eq(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    private static int indexOf(Object obj, Object[] objArr, int i, int i2) {
        if (obj == null) {
            while (i < i2) {
                if (objArr[i] == null) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        while (i < i2) {
            if (obj.equals(objArr[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int binarySearch(Object obj) {
        Object[] array2 = getArray();
        return binarySearch(obj, array2, 0, array2.length, this.comparator);
    }

    public int binarySearch(Object obj, int i, int i2) {
        return binarySearch(obj, getArray(), i, i2, this.comparator);
    }

    private static int binarySearch(Object obj, Object[] objArr, int i, int i2, Comparator comparator2) {
        int binarySearch = Arrays.binarySearch(objArr, i, i2, obj, comparator2);
        if (binarySearch < 0) {
            return -1;
        }
        return binarySearch;
    }

    public boolean contains(Object obj) {
        Object[] array2 = getArray();
        return indexOf(obj, array2, 0, array2.length) >= 0;
    }

    public int indexOf(Object obj) {
        Object[] array2 = getArray();
        return indexOf(obj, array2, 0, array2.length);
    }

    public int indexOf(E e, int i) {
        Object[] array2 = getArray();
        return indexOf(e, array2, i, array2.length);
    }

    public int lastIndexOf(Object obj) {
        throw new UnsupportedOperationException();
    }

    public Object clone() {
        try {
            PriorityBlockingArrayList priorityBlockingArrayList = (PriorityBlockingArrayList) super.clone();
            priorityBlockingArrayList.array = (Object[]) priorityBlockingArrayList.array.clone();
            return priorityBlockingArrayList;
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    public Object[] toArray() {
        Object[] array2 = getArray();
        return Arrays.copyOf(array2, array2.length);
    }

    public <T> T[] toArray(T[] tArr) {
        Object[] array2 = getArray();
        int length = array2.length;
        if (tArr.length < length) {
            return Arrays.copyOf(array2, length, tArr.getClass());
        }
        System.arraycopy(array2, 0, tArr, 0, length);
        if (tArr.length > length) {
            tArr[length] = null;
        }
        return tArr;
    }

    private E get(Object[] objArr, int i) {
        return objArr[i];
    }

    public E get(int i) {
        return get(getArray(), i);
    }

    public void add(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(E e) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array2 = getArray();
            int length = array2.length;
            Object[] objArr = (Object[]) Array.newInstance(array2.getClass().getComponentType(), length + 1);
            int i = 0;
            if (length > 0) {
                int binarySearch = Arrays.binarySearch(array2, e, this.comparator);
                if (binarySearch < 0) {
                    binarySearch ^= -1;
                }
                if (binarySearch == 0) {
                    System.arraycopy(array2, 0, objArr, 1, length);
                } else if (binarySearch > length) {
                    System.arraycopy(array2, 0, objArr, 0, length);
                } else {
                    System.arraycopy(array2, 0, objArr, 0, binarySearch);
                    System.arraycopy(array2, binarySearch, objArr, binarySearch + 1, length - binarySearch);
                }
                i = binarySearch;
            }
            objArr[i] = e;
            setArray(objArr);
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    public E remove(int i) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array2 = getArray();
            int length = array2.length;
            E e = get(array2, i);
            int i2 = (length - i) - 1;
            if (i2 == 0) {
                setArray(Arrays.copyOf(array2, length - 1));
            } else {
                Object[] objArr = new Object[(length - 1)];
                System.arraycopy(array2, 0, objArr, 0, i);
                System.arraycopy(array2, i + 1, objArr, i, i2);
                setArray(objArr);
            }
            return e;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean remove(Object obj) {
        Object[] array2 = getArray();
        int indexOf = indexOf(obj, array2, 0, array2.length);
        if (indexOf < 0) {
            return false;
        }
        return remove(obj, array2, indexOf);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0027, code lost:
        if (r11 < r2) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        r0.unlock();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        if (r1[r11] != r9) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0032, code lost:
        r11 = indexOf(r9, r1, r11, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0036, code lost:
        if (r11 >= 0) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0038, code lost:
        r0.unlock();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003b, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean remove(java.lang.Object r9, java.lang.Object[] r10, int r11) {
        /*
            r8 = this;
            java.util.concurrent.locks.ReentrantLock r0 = r8.lock
            r0.lock()
            java.lang.Object[] r1 = r8.getArray()     // Catch:{ all -> 0x0052 }
            int r2 = r1.length     // Catch:{ all -> 0x0052 }
            r3 = 0
            if (r10 == r1) goto L_0x003c
            int r4 = java.lang.Math.min(r11, r2)     // Catch:{ all -> 0x0052 }
            r5 = 0
        L_0x0012:
            if (r5 >= r4) goto L_0x0027
            r6 = r1[r5]     // Catch:{ all -> 0x0052 }
            r7 = r10[r5]     // Catch:{ all -> 0x0052 }
            if (r6 == r7) goto L_0x0024
            r6 = r1[r5]     // Catch:{ all -> 0x0052 }
            boolean r6 = eq(r9, r6)     // Catch:{ all -> 0x0052 }
            if (r6 == 0) goto L_0x0024
            r11 = r5
            goto L_0x003c
        L_0x0024:
            int r5 = r5 + 1
            goto L_0x0012
        L_0x0027:
            if (r11 < r2) goto L_0x002d
            r0.unlock()
            return r3
        L_0x002d:
            r10 = r1[r11]     // Catch:{ all -> 0x0052 }
            if (r10 != r9) goto L_0x0032
            goto L_0x003c
        L_0x0032:
            int r11 = indexOf(r9, r1, r11, r2)     // Catch:{ all -> 0x0052 }
            if (r11 >= 0) goto L_0x003c
            r0.unlock()
            return r3
        L_0x003c:
            int r9 = r2 + -1
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ all -> 0x0052 }
            java.lang.System.arraycopy(r1, r3, r9, r3, r11)     // Catch:{ all -> 0x0052 }
            int r10 = r11 + 1
            int r2 = r2 - r11
            r3 = 1
            int r2 = r2 - r3
            java.lang.System.arraycopy(r1, r10, r9, r11, r2)     // Catch:{ all -> 0x0052 }
            r8.setArray(r9)     // Catch:{ all -> 0x0052 }
            r0.unlock()
            return r3
        L_0x0052:
            r9 = move-exception
            r0.unlock()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.collection.PriorityBlockingArrayList.remove(java.lang.Object, java.lang.Object[], int):boolean");
    }

    /* access modifiers changed from: package-private */
    public void removeRange(int i, int i2) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array2 = getArray();
            int length = array2.length;
            if (i < 0 || i2 > length || i2 < i) {
                throw new IndexOutOfBoundsException();
            }
            int i3 = length - (i2 - i);
            int i4 = length - i2;
            if (i4 == 0) {
                setArray(Arrays.copyOf(array2, i3));
            } else {
                Object[] objArr = new Object[i3];
                System.arraycopy(array2, 0, objArr, 0, i);
                System.arraycopy(array2, i2, objArr, i, i4);
                setArray(objArr);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean addIfAbsent(E e) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array2 = getArray();
            int length = array2.length;
            int binarySearch = Arrays.binarySearch(array2, e, this.comparator);
            if (binarySearch < 0) {
                int i = binarySearch ^ -1;
                Object[] objArr = (Object[]) Array.newInstance(array2.getClass().getComponentType(), length + 1);
                if (length > 0) {
                    if (i == 0) {
                        System.arraycopy(array2, 0, objArr, 1, length);
                    } else if (i > length) {
                        System.arraycopy(array2, 0, objArr, 0, length);
                    } else {
                        System.arraycopy(array2, 0, objArr, 0, i);
                        System.arraycopy(array2, i, objArr, i + 1, length - i);
                    }
                }
                objArr[i] = e;
                setArray(objArr);
                return true;
            }
            reentrantLock.unlock();
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean containsAll(Collection<?> collection) {
        Object[] array2 = getArray();
        int length = array2.length;
        for (Object indexOf : collection) {
            if (indexOf(indexOf, array2, 0, length) < 0) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: finally extract failed */
    public boolean removeAll(Collection<?> collection) {
        if (collection != null) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                Object[] array2 = getArray();
                int length = array2.length;
                if (length != 0) {
                    Object[] objArr = new Object[length];
                    int i = 0;
                    for (Object obj : array2) {
                        if (!collection.contains(obj)) {
                            objArr[i] = obj;
                            i++;
                        }
                    }
                    if (i != length) {
                        setArray(Arrays.copyOf(objArr, i));
                        reentrantLock.unlock();
                        return true;
                    }
                }
                reentrantLock.unlock();
                return false;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        } else {
            throw new NullPointerException();
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean retainAll(Collection<?> collection) {
        if (collection != null) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                Object[] array2 = getArray();
                int length = array2.length;
                if (length != 0) {
                    Object[] objArr = new Object[length];
                    int i = 0;
                    for (Object obj : array2) {
                        if (collection.contains(obj)) {
                            objArr[i] = obj;
                            i++;
                        }
                    }
                    if (i != length) {
                        setArray(Arrays.copyOf(objArr, i));
                        reentrantLock.unlock();
                        return true;
                    }
                }
                reentrantLock.unlock();
                return false;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        } else {
            throw new NullPointerException();
        }
    }

    public E set(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public int addAllAbsent(Collection<? extends E> collection) {
        int i = 0;
        for (Object addIfAbsent : collection) {
            if (addIfAbsent(addIfAbsent)) {
                i++;
            }
        }
        return i;
    }

    public void clear() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            setArray(new Object[0]);
        } finally {
            reentrantLock.unlock();
        }
    }

    private int compare(E e, E e2, Comparator<? super E> comparator2) {
        if (comparator2 == null) {
            return ((Comparable) e).compareTo(e2);
        }
        return comparator2.compare(e, e2);
    }

    public boolean addAll(Collection<? extends E> collection) {
        Object[] array2 = collection.getClass() == PriorityBlockingArrayList.class ? ((PriorityBlockingArrayList) collection).getArray() : collection.toArray();
        if (array2.length == 0) {
            return false;
        }
        return addAll((E[]) array2);
    }

    /* JADX INFO: finally extract failed */
    public boolean addAll(E[] eArr) {
        Object[] copyOf = Arrays.copyOf(eArr, eArr.length);
        Arrays.sort(copyOf, this.comparator);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] array2 = getArray();
            int length = array2.length;
            if (length == 0 && copyOf.getClass() == Object[].class) {
                setArray(copyOf);
            } else {
                Object[] objArr = (Object[]) Array.newInstance(array2.getClass().getComponentType(), copyOf.length + length);
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                while (i < length && i2 < copyOf.length) {
                    if (compare(array2[i], copyOf[i2], this.comparator) > 0) {
                        objArr[i3] = copyOf[i2];
                        i2++;
                    } else {
                        objArr[i3] = array2[i];
                        i++;
                    }
                    i3++;
                }
                while (i < length) {
                    objArr[i3] = array2[i];
                    i3++;
                    i++;
                }
                while (i2 < copyOf.length) {
                    objArr[i3] = copyOf[i2];
                    i3++;
                    i2++;
                }
                setArray(objArr);
            }
            reentrantLock.unlock();
            return true;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Object[] array2 = getArray();
        objectOutputStream.writeInt(array2.length);
        for (Object writeObject : array2) {
            objectOutputStream.writeObject(writeObject);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        Object[] objArr = new Object[readInt];
        for (int i = 0; i < readInt; i++) {
            objArr[i] = objectInputStream.readObject();
        }
        setArray(objArr);
    }

    public String toString() {
        return Arrays.toString(getArray());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        Iterator it = ((List) obj).iterator();
        for (Object obj2 : getArray()) {
            if (!it.hasNext() || !eq(obj2, it.next())) {
                return false;
            }
        }
        return !it.hasNext();
    }

    public int hashCode() {
        int i;
        int i2 = 1;
        for (Object obj : getArray()) {
            int i3 = i2 * 31;
            if (obj == null) {
                i = 0;
            } else {
                i = obj.hashCode();
            }
            i2 = i3 + i;
        }
        return i2;
    }

    public Iterator<E> iterator() {
        return new COWIterator(getArray(), 0);
    }

    public ListIterator<E> listIterator() {
        return new COWIterator(getArray(), 0);
    }

    public ListIterator<E> listIterator(int i) {
        Object[] array2 = getArray();
        int length = array2.length;
        if (i >= 0 && i <= length) {
            return new COWIterator(array2, i);
        }
        throw new IndexOutOfBoundsException("Index: " + i);
    }

    static final class COWIterator<E> implements ListIterator<E> {
        private int cursor;
        private final Object[] snapshot;

        private COWIterator(Object[] objArr, int i) {
            this.cursor = i;
            this.snapshot = objArr;
        }

        public boolean hasNext() {
            return this.cursor < this.snapshot.length;
        }

        public boolean hasPrevious() {
            return this.cursor > 0;
        }

        public E next() {
            if (hasNext()) {
                E[] eArr = this.snapshot;
                int i = this.cursor;
                this.cursor = i + 1;
                return eArr[i];
            }
            throw new NoSuchElementException();
        }

        public E previous() {
            if (hasPrevious()) {
                E[] eArr = this.snapshot;
                int i = this.cursor - 1;
                this.cursor = i;
                return eArr[i];
            }
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return this.cursor;
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    public List<E> subList(int i, int i2) {
        throw new UnsupportedOperationException();
    }
}
