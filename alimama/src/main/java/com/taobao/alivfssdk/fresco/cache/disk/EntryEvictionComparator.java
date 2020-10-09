package com.taobao.alivfssdk.fresco.cache.disk;

import com.taobao.alivfssdk.fresco.cache.disk.DiskStorage;
import java.util.Comparator;

public interface EntryEvictionComparator extends Comparator<DiskStorage.Entry> {
}
