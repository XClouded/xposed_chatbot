package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public class OperatorGroupBy<T, K, R> implements Observable.Operator<GroupedObservable<K, R>, T> {
    private static final Func1<Object, Object> IDENTITY = new Func1<Object, Object>() {
        public Object call(Object obj) {
            return obj;
        }
    };
    /* access modifiers changed from: private */
    public static final Object NULL_KEY = new Object();
    final Func1<? super T, ? extends K> keySelector;
    final Func1<? super T, ? extends R> valueSelector;

    public OperatorGroupBy(Func1<? super T, ? extends K> func1) {
        this(func1, IDENTITY);
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends R> func12) {
        this.keySelector = func1;
        this.valueSelector = func12;
    }

    public Subscriber<? super T> call(Subscriber<? super GroupedObservable<K, R>> subscriber) {
        return new GroupBySubscriber(this.keySelector, this.valueSelector, subscriber);
    }

    static final class GroupBySubscriber<K, T, R> extends Subscriber<T> {
        static final AtomicLongFieldUpdater<GroupBySubscriber> BUFFERED_COUNT = AtomicLongFieldUpdater.newUpdater(GroupBySubscriber.class, "bufferedCount");
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> COMPLETION_EMITTED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "completionEmitted");
        private static final int MAX_QUEUE_SIZE = 1024;
        static final AtomicLongFieldUpdater<GroupBySubscriber> REQUESTED = AtomicLongFieldUpdater.newUpdater(GroupBySubscriber.class, "requested");
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> TERMINATED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "terminated");
        private static final int TERMINATED_WITH_COMPLETED = 1;
        private static final int TERMINATED_WITH_ERROR = 2;
        private static final int UNTERMINATED = 0;
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> WIP_FOR_UNSUBSCRIBE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "wipForUnsubscribe");
        private static final NotificationLite<Object> nl = NotificationLite.instance();
        volatile long bufferedCount;
        final Subscriber<? super GroupedObservable<K, R>> child;
        volatile int completionEmitted;
        final Func1<? super T, ? extends R> elementSelector;
        private final ConcurrentHashMap<Object, GroupState<K, T>> groups = new ConcurrentHashMap<>();
        final Func1<? super T, ? extends K> keySelector;
        volatile long requested;
        final GroupBySubscriber<K, T, R> self = this;
        volatile int terminated = 0;
        volatile int wipForUnsubscribe = 1;

        public GroupBySubscriber(Func1<? super T, ? extends K> func1, Func1<? super T, ? extends R> func12, Subscriber<? super GroupedObservable<K, R>> subscriber) {
            this.keySelector = func1;
            this.elementSelector = func12;
            this.child = subscriber;
            subscriber.add(Subscriptions.create(new Action0() {
                public void call() {
                    if (GroupBySubscriber.WIP_FOR_UNSUBSCRIBE_UPDATER.decrementAndGet(GroupBySubscriber.this.self) == 0) {
                        GroupBySubscriber.this.self.unsubscribe();
                    }
                }
            }));
        }

        private static class GroupState<K, T> {
            /* access modifiers changed from: private */
            public final Queue<Object> buffer;
            /* access modifiers changed from: private */
            public final AtomicLong count;
            /* access modifiers changed from: private */
            public final AtomicLong requested;
            private final Subject<T, T> s;

            private GroupState() {
                this.s = BufferUntilSubscriber.create();
                this.requested = new AtomicLong();
                this.count = new AtomicLong();
                this.buffer = new ConcurrentLinkedQueue();
            }

            public Observable<T> getObservable() {
                return this.s;
            }

            public Observer<T> getObserver() {
                return this.s;
            }
        }

        public void onStart() {
            REQUESTED.set(this, 1024);
            request(1024);
        }

        public void onCompleted() {
            if (TERMINATED_UPDATER.compareAndSet(this, 0, 1)) {
                for (GroupState<K, T> emitItem : this.groups.values()) {
                    emitItem(emitItem, nl.completed());
                }
                if (this.groups.isEmpty() && COMPLETION_EMITTED_UPDATER.compareAndSet(this, 0, 1)) {
                    this.child.onCompleted();
                }
            }
        }

        public void onError(Throwable th) {
            if (TERMINATED_UPDATER.compareAndSet(this, 0, 2)) {
                for (GroupState<K, T> emitItem : this.groups.values()) {
                    emitItem(emitItem, nl.error(th));
                }
                try {
                    this.child.onError(th);
                } finally {
                    unsubscribe();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void requestFromGroupedObservable(long j, GroupState<K, T> groupState) {
            BackpressureUtils.getAndAddRequest(groupState.requested, j);
            if (groupState.count.getAndIncrement() == 0) {
                pollQueue(groupState);
            }
        }

        private Object groupedKey(K k) {
            return k == null ? OperatorGroupBy.NULL_KEY : k;
        }

        private K getKey(Object obj) {
            if (obj == OperatorGroupBy.NULL_KEY) {
                return null;
            }
            return obj;
        }

        public void onNext(T t) {
            try {
                Object groupedKey = groupedKey(this.keySelector.call(t));
                GroupState groupState = this.groups.get(groupedKey);
                if (groupState == null) {
                    if (!this.child.isUnsubscribed()) {
                        groupState = createNewGroup(groupedKey);
                    } else {
                        return;
                    }
                }
                if (groupState != null) {
                    emitItem(groupState, nl.next(t));
                }
            } catch (Throwable th) {
                onError(OnErrorThrowable.addValueAsLastCause(th, t));
            }
        }

        private GroupState<K, T> createNewGroup(final Object obj) {
            int i;
            final GroupState<K, T> groupState = new GroupState<>();
            GroupedObservable create = GroupedObservable.create(getKey(obj), new Observable.OnSubscribe<R>() {
                public void call(final Subscriber<? super R> subscriber) {
                    subscriber.setProducer(new Producer() {
                        public void request(long j) {
                            GroupBySubscriber.this.requestFromGroupedObservable(j, groupState);
                        }
                    });
                    final AtomicBoolean atomicBoolean = new AtomicBoolean();
                    groupState.getObservable().doOnUnsubscribe(new Action0() {
                        public void call() {
                            if (atomicBoolean.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(obj);
                            }
                        }
                    }).unsafeSubscribe(new Subscriber<T>(subscriber) {
                        public void onStart() {
                        }

                        public void onCompleted() {
                            subscriber.onCompleted();
                            if (atomicBoolean.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(obj);
                            }
                        }

                        public void onError(Throwable th) {
                            subscriber.onError(th);
                            if (atomicBoolean.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(obj);
                            }
                        }

                        public void onNext(T t) {
                            try {
                                subscriber.onNext(GroupBySubscriber.this.elementSelector.call(t));
                            } catch (Throwable th) {
                                onError(OnErrorThrowable.addValueAsLastCause(th, t));
                            }
                        }
                    });
                }
            });
            do {
                i = this.wipForUnsubscribe;
                if (i <= 0) {
                    return null;
                }
            } while (!WIP_FOR_UNSUBSCRIBE_UPDATER.compareAndSet(this, i, i + 1));
            if (this.groups.putIfAbsent(obj, groupState) == null) {
                this.child.onNext(create);
                return groupState;
            }
            throw new IllegalStateException("Group already existed while creating a new one");
        }

        /* access modifiers changed from: private */
        public void cleanupGroup(Object obj) {
            GroupState remove = this.groups.remove(obj);
            if (remove != null) {
                if (!remove.buffer.isEmpty()) {
                    BUFFERED_COUNT.addAndGet(this.self, (long) (-remove.buffer.size()));
                }
                completeInner();
                requestMoreIfNecessary();
            }
        }

        private void emitItem(GroupState<K, T> groupState, Object obj) {
            Queue access$500 = groupState.buffer;
            AtomicLong access$000 = groupState.requested;
            REQUESTED.decrementAndGet(this);
            if (access$000 == null || access$000.get() <= 0 || (access$500 != null && !access$500.isEmpty())) {
                access$500.add(obj);
                BUFFERED_COUNT.incrementAndGet(this);
                if (groupState.count.getAndIncrement() == 0) {
                    pollQueue(groupState);
                }
            } else {
                nl.accept(groupState.getObserver(), obj);
                if (access$000.get() != Long.MAX_VALUE) {
                    access$000.decrementAndGet();
                }
            }
            requestMoreIfNecessary();
        }

        private void pollQueue(GroupState<K, T> groupState) {
            do {
                drainIfPossible(groupState);
                if (groupState.count.decrementAndGet() > 1) {
                    groupState.count.set(1);
                }
            } while (groupState.count.get() > 0);
        }

        private void requestMoreIfNecessary() {
            if (REQUESTED.get(this) == 0 && this.terminated == 0) {
                long j = 1024 - BUFFERED_COUNT.get(this);
                if (j > 0 && REQUESTED.compareAndSet(this, 0, j)) {
                    request(j);
                }
            }
        }

        private void drainIfPossible(GroupState<K, T> groupState) {
            Object poll;
            while (groupState.requested.get() > 0 && (poll = groupState.buffer.poll()) != null) {
                nl.accept(groupState.getObserver(), poll);
                if (groupState.requested.get() != Long.MAX_VALUE) {
                    groupState.requested.decrementAndGet();
                }
                BUFFERED_COUNT.decrementAndGet(this);
                requestMoreIfNecessary();
            }
        }

        private void completeInner() {
            if (WIP_FOR_UNSUBSCRIBE_UPDATER.decrementAndGet(this) == 0) {
                unsubscribe();
            } else if (this.groups.isEmpty() && this.terminated == 1 && COMPLETION_EMITTED_UPDATER.compareAndSet(this, 0, 1)) {
                this.child.onCompleted();
            }
        }
    }
}
