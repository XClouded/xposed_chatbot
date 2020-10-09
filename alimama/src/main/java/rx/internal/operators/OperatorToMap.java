package rx.internal.operators;

import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;

public final class OperatorToMap<T, K, V> implements Observable.Operator<Map<K, V>, T> {
    /* access modifiers changed from: private */
    public final Func1<? super T, ? extends K> keySelector;
    /* access modifiers changed from: private */
    public final Func0<? extends Map<K, V>> mapFactory;
    /* access modifiers changed from: private */
    public final Func1<? super T, ? extends V> valueSelector;

    public static final class DefaultToMapFactory<K, V> implements Func0<Map<K, V>> {
        public Map<K, V> call() {
            return new HashMap();
        }
    }

    public OperatorToMap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12) {
        this(func1, func12, new DefaultToMapFactory());
    }

    public OperatorToMap(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends V> func12, Func0<? extends Map<K, V>> func0) {
        this.keySelector = func1;
        this.valueSelector = func12;
        this.mapFactory = func0;
    }

    public Subscriber<? super T> call(final Subscriber<? super Map<K, V>> subscriber) {
        return new Subscriber<T>(subscriber) {
            private Map<K, V> map = ((Map) OperatorToMap.this.mapFactory.call());

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T t) {
                this.map.put(OperatorToMap.this.keySelector.call(t), OperatorToMap.this.valueSelector.call(t));
            }

            public void onError(Throwable th) {
                this.map = null;
                subscriber.onError(th);
            }

            public void onCompleted() {
                Map<K, V> map2 = this.map;
                this.map = null;
                subscriber.onNext(map2);
                subscriber.onCompleted();
            }
        };
    }
}
