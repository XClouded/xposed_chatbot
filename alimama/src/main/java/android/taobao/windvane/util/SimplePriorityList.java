package android.taobao.windvane.util;

import android.taobao.windvane.util.SimplePriorityList.PriorityInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimplePriorityList<E extends PriorityInterface> implements List<E> {
    private List<E> cachedPriorityList = new CopyOnWriteArrayList();
    private List<E>[] list;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private int prioritySize;

    public interface PriorityInterface {
        int getPriority();
    }

    public SimplePriorityList(int i) {
        if (i > 0) {
            this.list = new ArrayList[i];
            for (int i2 = 0; i2 < i; i2++) {
                this.list[i2] = new ArrayList();
            }
            this.cachedPriorityList.clear();
            this.prioritySize = i;
            return;
        }
        throw new IllegalArgumentException("prioritySize < 0: " + i);
    }

    public void add(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(E e) {
        if (!checkPriority(e)) {
            return false;
        }
        this.lock.writeLock().lock();
        boolean add = this.list[e.getPriority()].add(e);
        this.cachedPriorityList.clear();
        this.lock.writeLock().unlock();
        return add;
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        this.lock.writeLock().lock();
        for (List<E> clear : this.list) {
            clear.clear();
        }
        this.cachedPriorityList.clear();
        this.lock.writeLock().unlock();
    }

    public boolean contains(Object obj) {
        return getCachedPriorityList().contains(obj);
    }

    public boolean containsAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public E get(int i) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Iterator<E> iterator() {
        return getCachedPriorityList().iterator();
    }

    public int lastIndexOf(Object obj) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<E> listIterator() {
        return getCachedPriorityList().listIterator();
    }

    public ListIterator<E> listIterator(int i) {
        throw new UnsupportedOperationException();
    }

    public E remove(int i) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        PriorityInterface priorityInterface = (PriorityInterface) obj;
        if (!checkPriority(priorityInterface)) {
            return false;
        }
        this.lock.writeLock().lock();
        boolean remove = this.list[priorityInterface.getPriority()].remove(priorityInterface);
        this.cachedPriorityList.clear();
        this.lock.writeLock().unlock();
        return remove;
    }

    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public E set(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return getCachedPriorityList().size();
    }

    public List<E> subList(int i, int i2) {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        return getCachedPriorityList().toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return getCachedPriorityList().toArray(tArr);
    }

    private boolean checkPriority(E e) {
        return e == null || (e.getPriority() >= 0 && e.getPriority() < this.prioritySize);
    }

    private List<E> getCachedPriorityList() {
        if (this.cachedPriorityList.isEmpty()) {
            this.lock.readLock().lock();
            for (List<E> addAll : this.list) {
                this.cachedPriorityList.addAll(addAll);
            }
            this.lock.readLock().unlock();
        }
        return this.cachedPriorityList;
    }
}
