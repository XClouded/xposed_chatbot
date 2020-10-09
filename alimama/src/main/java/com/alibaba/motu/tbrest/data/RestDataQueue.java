package com.alibaba.motu.tbrest.data;

public class RestDataQueue<T> {
    private int count = 0;
    private final Object[] elements;
    private final int maxSize;
    private int next = 0;

    public RestDataQueue(int i) {
        this.elements = new Object[i];
        this.maxSize = i;
    }

    public T push(T t) {
        T t2 = this.elements[this.next];
        this.elements[this.next] = t;
        int i = this.next + 1;
        this.next = i;
        this.next = i % this.maxSize;
        if (this.count < this.maxSize) {
            this.count++;
        }
        return t2;
    }

    public T poll() {
        if (isEmpty()) {
            return null;
        }
        int i = ((this.next - this.count) + this.maxSize) % this.maxSize;
        T t = this.elements[i];
        this.elements[i] = null;
        this.count--;
        return t;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return this.elements[((this.next - this.count) + this.maxSize) % this.maxSize];
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean isFull() {
        return this.count == this.maxSize;
    }
}
