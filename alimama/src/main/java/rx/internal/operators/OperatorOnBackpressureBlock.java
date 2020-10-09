package rx.internal.operators;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import rx.Observable;
import rx.Subscriber;
import rx.internal.util.BackpressureDrainManager;

public class OperatorOnBackpressureBlock<T> implements Observable.Operator<T, T> {
    final int max;

    public OperatorOnBackpressureBlock(int i) {
        this.max = i;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        BlockingSubscriber blockingSubscriber = new BlockingSubscriber(this.max, subscriber);
        blockingSubscriber.init();
        return blockingSubscriber;
    }

    static final class BlockingSubscriber<T> extends Subscriber<T> implements BackpressureDrainManager.BackpressureQueueCallback {
        final Subscriber<? super T> child;
        final BackpressureDrainManager manager;
        final NotificationLite<T> nl = NotificationLite.instance();
        final BlockingQueue<Object> queue;

        public BlockingSubscriber(int i, Subscriber<? super T> subscriber) {
            this.queue = new ArrayBlockingQueue(i);
            this.child = subscriber;
            this.manager = new BackpressureDrainManager(this);
        }

        /* access modifiers changed from: package-private */
        public void init() {
            this.child.add(this);
            this.child.setProducer(this.manager);
        }

        public void onNext(T t) {
            try {
                this.queue.put(this.nl.next(t));
                this.manager.drain();
            } catch (InterruptedException e) {
                if (!isUnsubscribed()) {
                    onError(e);
                }
            }
        }

        public void onError(Throwable th) {
            this.manager.terminateAndDrain(th);
        }

        public void onCompleted() {
            this.manager.terminateAndDrain();
        }

        public boolean accept(Object obj) {
            return this.nl.accept(this.child, obj);
        }

        public void complete(Throwable th) {
            if (th != null) {
                this.child.onError(th);
            } else {
                this.child.onCompleted();
            }
        }

        public Object peek() {
            return this.queue.peek();
        }

        public Object poll() {
            return this.queue.poll();
        }
    }
}
