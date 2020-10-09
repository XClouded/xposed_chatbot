package com.taobao.phenix.cache.memory;

public class MemOnlyFailedException extends Exception {
    public MemOnlyFailedException() {
        super("no memory cache , MemCache cannot conduct final result at memOnly=true");
    }
}
