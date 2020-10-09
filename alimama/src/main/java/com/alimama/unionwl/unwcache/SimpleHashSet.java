package com.alimama.unionwl.unwcache;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class SimpleHashSet<T> extends AbstractSet<T> implements Set<T>, Cloneable {
    private static final SimpleHashSetEntry[] EMPTY_TABLE = new SimpleHashSetEntry[2];
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final int MINIMUM_CAPACITY = 4;
    /* access modifiers changed from: private */
    public SimpleHashSetEntry<T> mEntryForNull;
    transient int mSize;
    transient SimpleHashSetEntry<T>[] mTable;
    private transient int threshold;

    public static int roundUpToPowerOfTwo(int i) {
        int i2 = i - 1;
        int i3 = i2 | (i2 >>> 1);
        int i4 = i3 | (i3 >>> 2);
        int i5 = i4 | (i4 >>> 4);
        int i6 = i5 | (i5 >>> 8);
        return (i6 | (i6 >>> 16)) + 1;
    }

    public SimpleHashSet() {
        this.mTable = EMPTY_TABLE;
        this.threshold = -1;
    }

    public SimpleHashSet(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Capacity: " + i);
        } else if (i == 0) {
            this.mTable = EMPTY_TABLE;
            this.threshold = -1;
        } else {
            int i2 = 1073741824;
            if (i < 4) {
                i2 = 4;
            } else if (i <= 1073741824) {
                i2 = roundUpToPowerOfTwo(i);
            }
            makeTable(i2);
        }
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public SimpleHashSet(Collection<? extends T> collection) {
        this(collection.size() < 6 ? 11 : collection.size() * 2);
        for (Object add : collection) {
            add(add);
        }
    }

    public static int secondaryHash(Object obj) {
        int hashCode = obj.hashCode();
        int i = hashCode ^ ((hashCode >>> 20) ^ (hashCode >>> 12));
        return i ^ ((i >>> 7) ^ (i >>> 4));
    }

    public Iterator<T> iterator() {
        return new HashSetIterator();
    }

    public int size() {
        return this.mSize;
    }

    public boolean remove(Object obj) {
        SimpleHashSetEntry<T> simpleHashSetEntry = null;
        if (obj != null) {
            int secondaryHash = secondaryHash(obj);
            SimpleHashSetEntry<T>[] simpleHashSetEntryArr = this.mTable;
            int length = (simpleHashSetEntryArr.length - 1) & secondaryHash;
            SimpleHashSetEntry<T> simpleHashSetEntry2 = simpleHashSetEntryArr[length];
            while (true) {
                SimpleHashSetEntry<T> simpleHashSetEntry3 = simpleHashSetEntry2;
                SimpleHashSetEntry<T> simpleHashSetEntry4 = simpleHashSetEntry;
                simpleHashSetEntry = simpleHashSetEntry3;
                if (simpleHashSetEntry == null) {
                    return false;
                }
                if (simpleHashSetEntry.mHash != secondaryHash || !obj.equals(simpleHashSetEntry.mKey)) {
                    simpleHashSetEntry2 = simpleHashSetEntry.mNext;
                } else {
                    if (simpleHashSetEntry4 == null) {
                        simpleHashSetEntryArr[length] = simpleHashSetEntry.mNext;
                    } else {
                        SimpleHashSetEntry unused = simpleHashSetEntry4.mNext = simpleHashSetEntry.mNext;
                    }
                    this.mSize--;
                    return true;
                }
            }
        } else if (this.mEntryForNull == null) {
            return false;
        } else {
            this.mEntryForNull = null;
            this.mSize--;
            return true;
        }
    }

    public boolean add(T t) {
        if (t != null) {
            int secondaryHash = secondaryHash(t);
            SimpleHashSetEntry<T>[] simpleHashSetEntryArr = this.mTable;
            int length = (simpleHashSetEntryArr.length - 1) & secondaryHash;
            for (SimpleHashSetEntry<T> simpleHashSetEntry = simpleHashSetEntryArr[length]; simpleHashSetEntry != null; simpleHashSetEntry = simpleHashSetEntry.mNext) {
                if (simpleHashSetEntry.mKey == t || (simpleHashSetEntry.mHash == secondaryHash && simpleHashSetEntry.mKey.equals(t))) {
                    return false;
                }
            }
            int i = this.mSize;
            this.mSize = i + 1;
            if (i > this.threshold) {
                simpleHashSetEntryArr = doubleCapacity();
                length = secondaryHash & (simpleHashSetEntryArr.length - 1);
            }
            simpleHashSetEntryArr[length] = new SimpleHashSetEntry<>(secondaryHash, t);
            return true;
        } else if (this.mEntryForNull != null) {
            return false;
        } else {
            this.mSize++;
            this.mEntryForNull = new SimpleHashSetEntry<>(0, (Object) null);
            return true;
        }
    }

    private SimpleHashSetEntry<T>[] makeTable(int i) {
        SimpleHashSetEntry<T>[] simpleHashSetEntryArr = (SimpleHashSetEntry[]) new SimpleHashSetEntry[i];
        this.mTable = simpleHashSetEntryArr;
        this.threshold = (i >> 1) + (i >> 2);
        return simpleHashSetEntryArr;
    }

    private SimpleHashSetEntry<T>[] doubleCapacity() {
        SimpleHashSetEntry<T>[] simpleHashSetEntryArr = this.mTable;
        int length = simpleHashSetEntryArr.length;
        if (length == 1073741824) {
            return simpleHashSetEntryArr;
        }
        SimpleHashSetEntry<T>[] makeTable = makeTable(length * 2);
        if (this.mSize == 0) {
            return makeTable;
        }
        for (int i = 0; i < length; i++) {
            SimpleHashSetEntry<T> simpleHashSetEntry = simpleHashSetEntryArr[i];
            if (simpleHashSetEntry != null) {
                int access$200 = simpleHashSetEntry.mHash & length;
                makeTable[i | access$200] = simpleHashSetEntry;
                SimpleHashSetEntry<T> simpleHashSetEntry2 = simpleHashSetEntry;
                SimpleHashSetEntry<T> simpleHashSetEntry3 = null;
                for (SimpleHashSetEntry<T> access$100 = simpleHashSetEntry.mNext; access$100 != null; access$100 = access$100.mNext) {
                    int access$2002 = access$100.mHash & length;
                    if (access$2002 != access$200) {
                        if (simpleHashSetEntry3 == null) {
                            makeTable[i | access$2002] = access$100;
                        } else {
                            SimpleHashSetEntry unused = simpleHashSetEntry3.mNext = access$100;
                        }
                        simpleHashSetEntry3 = simpleHashSetEntry2;
                        access$200 = access$2002;
                    }
                    simpleHashSetEntry2 = access$100;
                }
                if (simpleHashSetEntry3 != null) {
                    SimpleHashSetEntry unused2 = simpleHashSetEntry3.mNext = null;
                }
            }
        }
        return makeTable;
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return this.mEntryForNull != null;
        }
        int secondaryHash = secondaryHash(obj);
        SimpleHashSetEntry<T>[] simpleHashSetEntryArr = this.mTable;
        for (SimpleHashSetEntry<T> simpleHashSetEntry = simpleHashSetEntryArr[(simpleHashSetEntryArr.length - 1) & secondaryHash]; simpleHashSetEntry != null; simpleHashSetEntry = simpleHashSetEntry.mNext) {
            if (simpleHashSetEntry.mKey == obj || (simpleHashSetEntry.mHash == secondaryHash && simpleHashSetEntry.mKey.equals(obj))) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        if (this.mSize != 0) {
            Arrays.fill(this.mTable, (Object) null);
            this.mEntryForNull = null;
            this.mSize = 0;
        }
    }

    public Object clone() {
        try {
            SimpleHashSet simpleHashSet = (SimpleHashSet) super.clone();
            simpleHashSet.mEntryForNull = null;
            simpleHashSet.makeTable(this.mTable.length);
            simpleHashSet.mSize = 0;
            Iterator it = iterator();
            while (it.hasNext()) {
                simpleHashSet.add(it.next());
            }
            return simpleHashSet;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    private static class SimpleHashSetEntry<T> {
        /* access modifiers changed from: private */
        public int mHash;
        /* access modifiers changed from: private */
        public T mKey;
        /* access modifiers changed from: private */
        public SimpleHashSetEntry<T> mNext;

        private SimpleHashSetEntry(int i, T t) {
            this.mHash = i;
            this.mKey = t;
        }
    }

    private class HashSetIterator implements Iterator<T> {
        SimpleHashSetEntry<T> lastEntryReturned;
        SimpleHashSetEntry<T> nextEntry;
        int nextIndex;

        private HashSetIterator() {
            this.nextEntry = SimpleHashSet.this.mEntryForNull;
            if (SimpleHashSet.this.mEntryForNull == null) {
                SimpleHashSetEntry<T>[] simpleHashSetEntryArr = SimpleHashSet.this.mTable;
                SimpleHashSetEntry<T> simpleHashSetEntry = null;
                while (simpleHashSetEntry == null && this.nextIndex < simpleHashSetEntryArr.length) {
                    int i = this.nextIndex;
                    this.nextIndex = i + 1;
                    simpleHashSetEntry = simpleHashSetEntryArr[i];
                }
                this.nextEntry = simpleHashSetEntry;
            }
        }

        public boolean hasNext() {
            return this.nextEntry != null;
        }

        public T next() {
            if (this.nextEntry != null) {
                SimpleHashSetEntry<T> simpleHashSetEntry = this.nextEntry;
                SimpleHashSetEntry<T>[] simpleHashSetEntryArr = SimpleHashSet.this.mTable;
                SimpleHashSetEntry<T> access$100 = simpleHashSetEntry.mNext;
                while (access$100 == null && this.nextIndex < simpleHashSetEntryArr.length) {
                    int i = this.nextIndex;
                    this.nextIndex = i + 1;
                    access$100 = simpleHashSetEntryArr[i];
                }
                this.nextEntry = access$100;
                this.lastEntryReturned = simpleHashSetEntry;
                return simpleHashSetEntry.mKey;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (this.lastEntryReturned != null) {
                SimpleHashSet.this.remove(this.lastEntryReturned.mKey);
                this.lastEntryReturned = null;
                return;
            }
            throw new IllegalStateException();
        }
    }
}
