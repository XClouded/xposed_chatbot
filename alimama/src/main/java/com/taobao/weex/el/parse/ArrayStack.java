package com.taobao.weex.el.parse;

import java.util.ArrayList;
import java.util.List;

public class ArrayStack<T> {
    private ArrayList<T> stack = new ArrayList<>(4);

    public int size() {
        return this.stack.size();
    }

    public T pop() {
        return this.stack.remove(this.stack.size() - 1);
    }

    public void push(T t) {
        this.stack.add(t);
    }

    public T peek() {
        return this.stack.get(this.stack.size() - 1);
    }

    public T get(int i) {
        return this.stack.get(i);
    }

    public T remove(int i) {
        return this.stack.remove(i);
    }

    public void add(int i, T t) {
        this.stack.add(i, t);
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public List<T> getList() {
        return this.stack;
    }
}
