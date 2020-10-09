package com.ali.protodb.lsdb;

public class KeyIterator implements Iterator<Key> {
    private int index = 0;
    private final String[] keys;

    public KeyIterator(String[] strArr) {
        this.keys = strArr;
        this.index = 0;
    }

    public Key next() {
        if (this.keys == null || this.index >= this.keys.length) {
            return null;
        }
        String[] strArr = this.keys;
        int i = this.index;
        this.index = i + 1;
        return new Key(strArr[i]);
    }
}
