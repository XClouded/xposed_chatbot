package com.taobao.tao.log.godeye.protocol.control;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Define<T> {
    List<Entry<T>> DEFINE = new CopyOnWriteArrayList();

    public static class Entry<T> {
        String opCode;
        T value;

        public static <T> Entry<T> build(String str, T t) {
            return new Entry<>(str, t);
        }

        private Entry(String str, T t) {
            this.opCode = str;
            this.value = t;
        }

        public String getOpCode() {
            return this.opCode;
        }

        public T getValue() {
            return this.value;
        }
    }

    public void register(Entry<T> entry) {
        if (entry == null) {
            throw new NullPointerException("entry");
        } else if (entry.getValue() != null) {
            for (Entry next : this.DEFINE) {
                if (next.getOpCode().equals(entry.getOpCode())) {
                    throw new IllegalArgumentException(String.format("same command exist. OpCode:%s vlaue class:%s", new Object[]{entry.getOpCode(), entry.getValue().getClass()}));
                } else if (next.getValue().equals(entry.getValue())) {
                    throw new IllegalArgumentException(String.format("same value exist. OpCode:%s vlaue class:%s", new Object[]{entry.getOpCode(), entry.getValue().getClass()}));
                }
            }
            this.DEFINE.add(entry);
        } else {
            throw new NullPointerException("value");
        }
    }

    public void unRegister(Entry<T> entry) {
        if (entry != null) {
            for (Entry<T> next : this.DEFINE) {
                if (entry == next || next.getOpCode().equals(entry.getOpCode())) {
                    this.DEFINE.remove(next);
                }
            }
            return;
        }
        throw new NullPointerException("entry");
    }

    public T getValue(String str) {
        for (Entry next : this.DEFINE) {
            if (next.getOpCode().equals(str)) {
                return next.getValue();
            }
        }
        return null;
    }

    public Entry<T> getDefine(T t) {
        for (Entry<T> next : this.DEFINE) {
            if (next.getValue().equals(t)) {
                return next;
            }
        }
        return null;
    }
}
