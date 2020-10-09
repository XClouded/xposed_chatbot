package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;

public final class OperatorToMultimap<T, K, V> implements Observable.Operator<Map<K, Collection<V>>, T> {
    /* access modifiers changed from: private */
    public final Func1<? super K, ? extends Collection<V>> collectionFactory;
    /* access modifiers changed from: private */
    public final Func1<? super T, ? extends K> keySelector;
    /* access modifiers changed from: private */
    public final Func0<? extends Map<K, Collection<V>>> mapFactory;
    /* access modifiers changed from: private */
    public final Func1<? super T, ? extends V> valueSelector;

    public static final class DefaultToMultimapFactory<K, V> implements Func0<Map<K, Collection<V>>> {
        public Map<K, Collection<V>> call() {
            return new HashMap();
        }
    }

    public static final class DefaultMultimapCollectionFactory<K, V> implements Func1<K, Collection<V>> {
        public Collection<V> call(K k) {
            return new ArrayList();
        }
    }

    public OperatorToMultimap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12) {
        this(func1, func12, new DefaultToMultimapFactory(), new DefaultMultimapCollectionFactory());
    }

    public OperatorToMultimap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12, Func0<? extends Map<K, Collection<V>>> func0) {
        this(func1, func12, func0, new DefaultMultimapCollectionFactory());
    }

    public OperatorToMultimap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12, Func0<? extends Map<K, Collection<V>>> func0, Func1<? super K, ? extends Collection<V>> func13) {
        this.keySelector = func1;
        this.valueSelector = func12;
        this.mapFactory = func0;
        this.collectionFactory = func13;
    }

    public Subscriber<? super T> call(final Subscriber<? super Map<K, Collection<V>>> subscriber) {
        return new Subscriber<T>(subscriber) {
            private Map<K, Collection<V>> map = ((Map) OperatorToMultimap.this.mapFactory.call());

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T t) {
                Object call = OperatorToMultimap.this.keySelector.call(t);
                Object call2 = OperatorToMultimap.this.valueSelector.call(t);
                Collection collection = this.map.get(call);
                if (collection == null) {
                    collection = (Collection) OperatorToMultimap.this.collectionFactory.call(call);
                    this.map.put(call, collection);
                }
                collection.add(call2);
            }

            public void onError(Throwable th) {
                this.map = null;
                subscriber.onError(th);
            }

            public void onCompleted() {
                Map<K, Collection<V>> map2 = this.map;
                this.map = null;
                subscriber.onNext(map2);
                subscriber.onCompleted();
            }
        };
    }
}
