package com.alibaba.taffy.core.util.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TopologyUtil {

    public interface DependencyAdapter<T> {
        Collection<T> getDependencies(T t);
    }

    public interface DependencyMethod<T> {
        Collection<T> getDependencies();
    }

    private static class AdjacentNode<T> {
        int edges;
        T vertex;

        private AdjacentNode() {
            this.edges = 0;
        }
    }

    public static <T extends DependencyMethod<T>> List<T> sort(Collection<T> collection) {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        for (T t : collection) {
            if (!hashMap.containsKey(t)) {
                dfs(t, arrayList, hashMap, hashMap2);
            }
        }
        return arrayList;
    }

    private static <T extends DependencyMethod<T>> void dfs(T t, Collection<T> collection, Map<T, Boolean> map, Map<T, Boolean> map2) {
        if (t != null) {
            map.put(t, true);
            map2.put(t, true);
            Collection<DependencyMethod> dependencies = t.getDependencies();
            if (CollectionUtil.isNotEmpty(dependencies)) {
                for (DependencyMethod dependencyMethod : dependencies) {
                    if (!map.containsKey(dependencyMethod)) {
                        dfs(dependencyMethod, collection, map, map2);
                    } else if (map2.containsKey(dependencyMethod)) {
                        throw new IllegalArgumentException("has cycle dependencies");
                    }
                }
            }
            collection.add(t);
            map2.remove(t);
        }
    }

    public static <T> List<T> sort(Collection<T> collection, DependencyAdapter<T> dependencyAdapter) {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        for (T next : collection) {
            if (!hashMap.containsKey(next)) {
                dfs(next, dependencyAdapter, arrayList, hashMap, hashMap2);
            }
        }
        return arrayList;
    }

    private static <T> void dfs(T t, DependencyAdapter<T> dependencyAdapter, Collection<T> collection, Map<T, Boolean> map, Map<T, Boolean> map2) {
        if (t != null) {
            map.put(t, true);
            map2.put(t, true);
            Collection<T> dependencies = dependencyAdapter.getDependencies(t);
            if (CollectionUtil.isNotEmpty(dependencies)) {
                for (T next : dependencies) {
                    if (!map.containsKey(next)) {
                        dfs(next, dependencyAdapter, collection, map, map2);
                    } else if (map2.containsKey(next)) {
                        throw new IllegalArgumentException("has cycle dependencies");
                    }
                }
            }
            collection.add(t);
            map2.remove(t);
        }
    }

    public static <T extends DependencyMethod<T>> List<T> sortFast(Collection<T> collection) {
        ArrayList arrayList = new ArrayList(collection);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        int i = 0;
        for (T dependencies : collection) {
            i += kahnBuildNode(arrayList, dependencies.getDependencies(), linkedHashMap);
        }
        return kahn(arrayList, linkedHashMap, i);
    }

    private static <T extends DependencyMethod<T>> List<T> kahn(List<T> list, Map<T, AdjacentNode<T>> map, int i) {
        ArrayList arrayList = new ArrayList();
        while (CollectionUtil.isNotEmpty(list)) {
            DependencyMethod dependencyMethod = (DependencyMethod) list.remove(0);
            i += kahnProcess(dependencyMethod, arrayList, list, dependencyMethod.getDependencies(), map);
        }
        if (i == 0) {
            return arrayList;
        }
        throw new IllegalArgumentException("has cycle dependencies");
    }

    public static <T> List<T> sortFast(Collection<T> collection, DependencyAdapter<T> dependencyAdapter) {
        ArrayList arrayList = new ArrayList(collection);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        int i = 0;
        for (T dependencies : collection) {
            i += kahnBuildNode(arrayList, dependencyAdapter.getDependencies(dependencies), linkedHashMap);
        }
        return kahn(arrayList, dependencyAdapter, linkedHashMap, i);
    }

    private static <T> List<T> kahn(List<T> list, DependencyAdapter<T> dependencyAdapter, Map<T, AdjacentNode<T>> map, int i) {
        ArrayList arrayList = new ArrayList();
        while (CollectionUtil.isNotEmpty(list)) {
            T remove = list.remove(0);
            i += kahnProcess(remove, arrayList, list, dependencyAdapter.getDependencies(remove), map);
        }
        if (i == 0) {
            return arrayList;
        }
        throw new IllegalArgumentException("has cycle dependencies");
    }

    private static <T> int kahnBuildNode(Collection<T> collection, Collection<T> collection2, Map<T, AdjacentNode<T>> map) {
        int i = 0;
        if (CollectionUtil.isNotEmpty(collection2)) {
            for (T next : collection2) {
                if (next != null) {
                    AdjacentNode adjacentNode = map.get(next);
                    if (adjacentNode == null) {
                        AdjacentNode adjacentNode2 = new AdjacentNode();
                        adjacentNode2.edges = 1;
                        adjacentNode2.vertex = next;
                        map.put(next, adjacentNode2);
                    } else {
                        adjacentNode.edges++;
                    }
                    i++;
                    collection.remove(next);
                }
            }
        }
        return i;
    }

    private static <T> int kahnProcess(T t, List<T> list, Collection<T> collection, Collection<T> collection2, Map<T, AdjacentNode<T>> map) {
        int i = 0;
        list.add(0, t);
        if (CollectionUtil.isNotEmpty(collection2)) {
            for (T t2 : collection2) {
                AdjacentNode adjacentNode = map.get(t2);
                i--;
                adjacentNode.edges--;
                if (adjacentNode.edges == 0) {
                    collection.add(adjacentNode.vertex);
                }
            }
        }
        return i;
    }
}
