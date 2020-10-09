package rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public final class BufferUntilSubscriber<T> extends Subject<T, T> {
    /* access modifiers changed from: private */
    public static final Observer EMPTY_OBSERVER = new Observer() {
        public void onCompleted() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Object obj) {
        }
    };
    private boolean forward = false;
    final State<T> state;

    public static <T> BufferUntilSubscriber<T> create() {
        return new BufferUntilSubscriber<>(new State());
    }

    static final class State<T> {
        static final AtomicReferenceFieldUpdater<State, Observer> OBSERVER_UPDATER = AtomicReferenceFieldUpdater.newUpdater(State.class, Observer.class, "observerRef");
        final ConcurrentLinkedQueue<Object> buffer = new ConcurrentLinkedQueue<>();
        boolean emitting = false;
        Object guard = new Object();
        final NotificationLite<T> nl = NotificationLite.instance();
        volatile Observer<? super T> observerRef = null;

        State() {
        }

        /* access modifiers changed from: package-private */
        public boolean casObserverRef(Observer<? super T> observer, Observer<? super T> observer2) {
            return OBSERVER_UPDATER.compareAndSet(this, observer, observer2);
        }
    }

    static final class OnSubscribeAction<T> implements Observable.OnSubscribe<T> {
        final State<T> state;

        public OnSubscribeAction(State<T> state2) {
            this.state = state2;
        }

        public void call(Subscriber<? super T> subscriber) {
            boolean z;
            if (this.state.casObserverRef((Observer) null, subscriber)) {
                subscriber.add(Subscriptions.create(new Action0() {
                    public void call() {
                        OnSubscribeAction.this.state.observerRef = BufferUntilSubscriber.EMPTY_OBSERVER;
                    }
                }));
                synchronized (this.state.guard) {
                    z = true;
                    if (!this.state.emitting) {
                        this.state.emitting = true;
                    } else {
                        z = false;
                    }
                }
                if (z) {
                    NotificationLite instance = NotificationLite.instance();
                    while (true) {
                        Object poll = this.state.buffer.poll();
                        if (poll != null) {
                            instance.accept(this.state.observerRef, poll);
                        } else {
                            synchronized (this.state.guard) {
                                if (this.state.buffer.isEmpty()) {
                                    this.state.emitting = false;
                                    return;
                                }
                            }
                        }
                    }
                }
            } else {
                subscriber.onError(new IllegalStateException("Only one subscriber allowed!"));
            }
        }
    }

    private BufferUntilSubscriber(State<T> state2) {
        super(new OnSubscribeAction(state2));
        this.state = state2;
    }

    private void emit(Object obj) {
        synchronized (this.state.guard) {
            this.state.buffer.add(obj);
            if (this.state.observerRef != null && !this.state.emitting) {
                this.forward = true;
                this.state.emitting = true;
            }
        }
        if (this.forward) {
            while (true) {
                Object poll = this.state.buffer.poll();
                if (poll != null) {
                    this.state.nl.accept(this.state.observerRef, poll);
                } else {
                    return;
                }
            }
        }
    }

    public void onCompleted() {
        if (this.forward) {
            this.state.observerRef.onCompleted();
        } else {
            emit(this.state.nl.completed());
        }
    }

    public void onError(Throwable th) {
        if (this.forward) {
            this.state.observerRef.onError(th);
        } else {
            emit(this.state.nl.error(th));
        }
    }

    public void onNext(T t) {
        if (this.forward) {
            this.state.observerRef.onNext(t);
        } else {
            emit(this.state.nl.next(t));
        }
    }

    public boolean hasObservers() {
        boolean z;
        synchronized (this.state.guard) {
            z = this.state.observerRef != null;
        }
        return z;
    }
}
