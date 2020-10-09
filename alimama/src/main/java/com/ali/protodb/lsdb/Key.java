package com.ali.protodb.lsdb;

public final class Key {
    private final String key;

    public Key(String str) {
        if (str != null) {
            this.key = str;
        } else {
            this.key = "";
        }
    }

    public String getStringKey() {
        return this.key;
    }
}
