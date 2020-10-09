package rx.internal.util;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import rx.Subscription;
import rx.functions.Func1;

public final class IndexedRingBuffer<E> implements Subscription {
    private static final ObjectPool<IndexedRingBuffer<?>> POOL = new ObjectPool<IndexedRingBuffer<?>>() {
        /* access modifiers changed from: protected */
        public IndexedRingBuffer<?> createObject() {
            return new IndexedRingBuffer<>();
        }
    };
    static final int SIZE = _size;
    static int _size;
    private final ElementSection<E> elements;
    final AtomicInteger index;
    private final IndexSection removed;
    final AtomicInteger removedIndex;

    public boolean isUnsubscribed() {
        return false;
    }

    static {
        _size = 256;
        if (PlatformDependent.isAndroid()) {
            _size = 8;
        }
        String property = System.getProperty("rx.indexed-ring-buffer.size");
        if (property != null) {
            try {
                _size = Integer.parseInt(property);
            } catch (Exception e) {
                PrintStream printStream = System.err;
                printStream.println("Failed to set 'rx.indexed-ring-buffer.size' with value " + property + " => " + e.getMessage());
            }
        }
    }

    public static final <T> IndexedRingBuffer<T> getInstance() {
        return POOL.borrowObject();
    }

    public void releaseToPool() {
        int i = this.index.get();
        ElementSection<E> elementSection = this.elements;
        int i2 = 0;
        loop0:
        while (elementSection != null) {
            int i3 = i2;
            int i4 = 0;
            while (i4 < SIZE) {
                if (i3 >= i) {
                    break loop0;
                }
                elementSection.array.set(i4, (Object) null);
                i4++;
                i3++;
            }
            elementSection = (ElementSection) elementSection.next.get();
            i2 = i3;
        }
        this.index.set(0);
        this.removedIndex.set(0);
        POOL.returnObject(this);
    }

    public void unsubscribe() {
        releaseToPool();
    }

    private IndexedRingBuffer() {
        this.elements = new ElementSection<>();
        this.removed = new IndexSection();
        this.index = new AtomicInteger();
        this.removedIndex = new AtomicInteger();
    }

    public int add(E e) {
        int indexForAdd = getIndexForAdd();
        if (indexForAdd < SIZE) {
            this.elements.array.set(indexForAdd, e);
            return indexForAdd;
        }
        getElementSection(indexForAdd).array.set(indexForAdd % SIZE, e);
        return indexForAdd;
    }

    public E remove(int i) {
        E e;
        if (i < SIZE) {
            e = this.elements.array.getAndSet(i, (Object) null);
        } else {
            e = getElementSection(i).array.getAndSet(i % SIZE, (Object) null);
        }
        pushRemovedIndex(i);
        return e;
    }

    private IndexSection getIndexSection(int i) {
        if (i < SIZE) {
            return this.removed;
        }
        int i2 = i / SIZE;
        IndexSection indexSection = this.removed;
        for (int i3 = 0; i3 < i2; i3++) {
            indexSection = indexSection.getNext();
        }
        return indexSection;
    }

    private ElementSection<E> getElementSection(int i) {
        if (i < SIZE) {
            return this.elements;
        }
        int i2 = i / SIZE;
        ElementSection<E> elementSection = this.elements;
        for (int i3 = 0; i3 < i2; i3++) {
            elementSection = elementSection.getNext();
        }
        return elementSection;
    }

    private synchronized int getIndexForAdd() {
        int i;
        int indexFromPreviouslyRemoved = getIndexFromPreviouslyRemoved();
        if (indexFromPreviouslyRemoved >= 0) {
            if (indexFromPreviouslyRemoved < SIZE) {
                i = this.removed.getAndSet(indexFromPreviouslyRemoved, -1);
            } else {
                i = getIndexSection(indexFromPreviouslyRemoved).getAndSet(indexFromPreviouslyRemoved % SIZE, -1);
            }
            if (i == this.index.get()) {
                this.index.getAndIncrement();
            }
        } else {
            i = this.index.getAndIncrement();
        }
        return i;
    }

    private synchronized int getIndexFromPreviouslyRemoved() {
        int i;
        int i2;
        do {
            i = this.removedIndex.get();
            if (i <= 0) {
                return -1;
            }
            i2 = i - 1;
        } while (!this.removedIndex.compareAndSet(i, i2));
        return i2;
    }

    private synchronized void pushRemovedIndex(int i) {
        int andIncrement = this.removedIndex.getAndIncrement();
        if (andIncrement < SIZE) {
            this.removed.set(andIncrement, i);
        } else {
            getIndexSection(andIncrement).set(andIncrement % SIZE, i);
        }
    }

    public int forEach(Func1<? super E, Boolean> func1) {
        return forEach(func1, 0);
    }

    public int forEach(Func1<? super E, Boolean> func1, int i) {
        int forEach = forEach(func1, i, this.index.get());
        if (i > 0 && forEach == this.index.get()) {
            return forEach(func1, 0, i);
        }
        if (forEach == this.index.get()) {
            return 0;
        }
        return forEach;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: rx.internal.util.IndexedRingBuffer$ElementSection<E>} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int forEach(rx.functions.Func1<? super E, java.lang.Boolean> r6, int r7, int r8) {
        /*
            r5 = this;
            java.util.concurrent.atomic.AtomicInteger r0 = r5.index
            int r0 = r0.get()
            rx.internal.util.IndexedRingBuffer$ElementSection<E> r1 = r5.elements
            int r2 = SIZE
            if (r7 < r2) goto L_0x0018
            rx.internal.util.IndexedRingBuffer$ElementSection r1 = r5.getElementSection(r7)
            int r2 = SIZE
            int r2 = r7 % r2
            r4 = r2
            r2 = r7
            r7 = r4
            goto L_0x0019
        L_0x0018:
            r2 = r7
        L_0x0019:
            if (r1 == 0) goto L_0x004e
        L_0x001b:
            int r3 = SIZE
            if (r7 >= r3) goto L_0x0041
            if (r2 >= r0) goto L_0x004e
            if (r2 < r8) goto L_0x0024
            goto L_0x004e
        L_0x0024:
            java.util.concurrent.atomic.AtomicReferenceArray r3 = r1.array
            java.lang.Object r3 = r3.get(r7)
            if (r3 != 0) goto L_0x002f
            goto L_0x003c
        L_0x002f:
            java.lang.Object r3 = r6.call(r3)
            java.lang.Boolean r3 = (java.lang.Boolean) r3
            boolean r3 = r3.booleanValue()
            if (r3 != 0) goto L_0x003c
            return r2
        L_0x003c:
            int r7 = r7 + 1
            int r2 = r2 + 1
            goto L_0x001b
        L_0x0041:
            java.util.concurrent.atomic.AtomicReference r7 = r1.next
            java.lang.Object r7 = r7.get()
            r1 = r7
            rx.internal.util.IndexedRingBuffer$ElementSection r1 = (rx.internal.util.IndexedRingBuffer.ElementSection) r1
            r7 = 0
            goto L_0x0019
        L_0x004e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.IndexedRingBuffer.forEach(rx.functions.Func1, int, int):int");
    }

    private static class ElementSection<E> {
        /* access modifiers changed from: private */
        public final AtomicReferenceArray<E> array;
        /* access modifiers changed from: private */
        public final AtomicReference<ElementSection<E>> next;

        private ElementSection() {
            this.array = new AtomicReferenceArray<>(IndexedRingBuffer.SIZE);
            this.next = new AtomicReference<>();
        }

        /* access modifiers changed from: package-private */
        public ElementSection<E> getNext() {
            if (this.next.get() != null) {
                return this.next.get();
            }
            ElementSection<E> elementSection = new ElementSection<>();
            if (this.next.compareAndSet((Object) null, elementSection)) {
                return elementSection;
            }
            return this.next.get();
        }
    }

    private static class IndexSection {
        private final AtomicReference<IndexSection> _next;
        private final AtomicIntegerArray unsafeArray;

        private IndexSection() {
            this.unsafeArray = new AtomicIntegerArray(IndexedRingBuffer.SIZE);
            this._next = new AtomicReference<>();
        }

        public int getAndSet(int i, int i2) {
            return this.unsafeArray.getAndSet(i, i2);
        }

        public void set(int i, int i2) {
            this.unsafeArray.set(i, i2);
        }

        /* access modifiers changed from: package-private */
        public IndexSection getNext() {
            if (this._next.get() != null) {
                return this._next.get();
            }
            IndexSection indexSection = new IndexSection();
            if (this._next.compareAndSet((Object) null, indexSection)) {
                return indexSection;
            }
            return this._next.get();
        }
    }
}
