package rx.observers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import rx.Notification;
import rx.Observer;
import rx.Subscriber;
import rx.annotations.Experimental;
import rx.exceptions.CompositeException;

public class TestSubscriber<T> extends Subscriber<T> {
    private static final Observer<Object> INERT = new Observer<Object>() {
        public void onCompleted() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Object obj) {
        }
    };
    private final long initialRequest;
    private volatile Thread lastSeenThread;
    private final CountDownLatch latch;
    private final TestObserver<T> testObserver;

    @Experimental
    public TestSubscriber(long j) {
        this(INERT, j);
    }

    @Experimental
    public TestSubscriber(Observer<T> observer, long j) {
        this.latch = new CountDownLatch(1);
        if (observer != null) {
            this.testObserver = new TestObserver<>(observer);
            this.initialRequest = j;
            return;
        }
        throw new NullPointerException();
    }

    public TestSubscriber(Subscriber<T> subscriber) {
        this(subscriber, -1);
    }

    public TestSubscriber(Observer<T> observer) {
        this(observer, -1);
    }

    public TestSubscriber() {
        this(-1);
    }

    @Experimental
    public static <T> TestSubscriber<T> create() {
        return new TestSubscriber<>();
    }

    @Experimental
    public static <T> TestSubscriber<T> create(long j) {
        return new TestSubscriber<>(j);
    }

    @Experimental
    public static <T> TestSubscriber<T> create(Observer<T> observer, long j) {
        return new TestSubscriber<>(observer, j);
    }

    @Experimental
    public static <T> TestSubscriber<T> create(Subscriber<T> subscriber) {
        return new TestSubscriber<>(subscriber);
    }

    @Experimental
    public static <T> TestSubscriber<T> create(Observer<T> observer) {
        return new TestSubscriber<>(observer);
    }

    public void onStart() {
        if (this.initialRequest >= 0) {
            requestMore(this.initialRequest);
        }
    }

    public void onCompleted() {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.testObserver.onCompleted();
        } finally {
            this.latch.countDown();
        }
    }

    public List<Notification<T>> getOnCompletedEvents() {
        return this.testObserver.getOnCompletedEvents();
    }

    public void onError(Throwable th) {
        try {
            this.lastSeenThread = Thread.currentThread();
            this.testObserver.onError(th);
        } finally {
            this.latch.countDown();
        }
    }

    public List<Throwable> getOnErrorEvents() {
        return this.testObserver.getOnErrorEvents();
    }

    public void onNext(T t) {
        this.lastSeenThread = Thread.currentThread();
        this.testObserver.onNext(t);
    }

    public void requestMore(long j) {
        request(j);
    }

    public List<T> getOnNextEvents() {
        return this.testObserver.getOnNextEvents();
    }

    public void assertReceivedOnNext(List<T> list) {
        this.testObserver.assertReceivedOnNext(list);
    }

    public void assertTerminalEvent() {
        this.testObserver.assertTerminalEvent();
    }

    public void assertUnsubscribed() {
        if (!isUnsubscribed()) {
            throw new AssertionError("Not unsubscribed.");
        }
    }

    public void assertNoErrors() {
        List<Throwable> onErrorEvents = getOnErrorEvents();
        if (onErrorEvents.size() > 0) {
            AssertionError assertionError = new AssertionError("Unexpected onError events: " + getOnErrorEvents().size());
            if (onErrorEvents.size() == 1) {
                assertionError.initCause(getOnErrorEvents().get(0));
            } else {
                assertionError.initCause(new CompositeException(onErrorEvents));
            }
            throw assertionError;
        }
    }

    public void awaitTerminalEvent() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    public void awaitTerminalEvent(long j, TimeUnit timeUnit) {
        try {
            this.latch.await(j, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }

    public void awaitTerminalEventAndUnsubscribeOnTimeout(long j, TimeUnit timeUnit) {
        try {
            if (!this.latch.await(j, timeUnit)) {
                unsubscribe();
            }
        } catch (InterruptedException unused) {
            unsubscribe();
        }
    }

    public Thread getLastSeenThread() {
        return this.lastSeenThread;
    }

    @Experimental
    public void assertCompleted() {
        int size = this.testObserver.getOnCompletedEvents().size();
        if (size == 0) {
            throw new AssertionError("Not completed!");
        } else if (size > 1) {
            throw new AssertionError("Completed multiple times: " + size);
        }
    }

    @Experimental
    public void assertNotCompleted() {
        int size = this.testObserver.getOnCompletedEvents().size();
        if (size == 1) {
            throw new AssertionError("Completed!");
        } else if (size > 1) {
            throw new AssertionError("Completed multiple times: " + size);
        }
    }

    @Experimental
    public void assertError(Class<? extends Throwable> cls) {
        List<Throwable> onErrorEvents = this.testObserver.getOnErrorEvents();
        if (onErrorEvents.size() == 0) {
            throw new AssertionError("No errors");
        } else if (onErrorEvents.size() > 1) {
            AssertionError assertionError = new AssertionError("Multiple errors: " + onErrorEvents.size());
            assertionError.initCause(new CompositeException(onErrorEvents));
            throw assertionError;
        } else if (!cls.isInstance(onErrorEvents.get(0))) {
            AssertionError assertionError2 = new AssertionError("Exceptions differ; expected: " + cls + ", actual: " + onErrorEvents.get(0));
            assertionError2.initCause(onErrorEvents.get(0));
            throw assertionError2;
        }
    }

    @Experimental
    public void assertError(Throwable th) {
        List<Throwable> onErrorEvents = this.testObserver.getOnErrorEvents();
        if (onErrorEvents.size() == 0) {
            throw new AssertionError("No errors");
        } else if (onErrorEvents.size() > 1) {
            AssertionError assertionError = new AssertionError("Multiple errors: " + onErrorEvents.size());
            assertionError.initCause(new CompositeException(onErrorEvents));
            throw assertionError;
        } else if (!th.equals(onErrorEvents.get(0))) {
            AssertionError assertionError2 = new AssertionError("Exceptions differ; expected: " + th + ", actual: " + onErrorEvents.get(0));
            assertionError2.initCause(onErrorEvents.get(0));
            throw assertionError2;
        }
    }

    @Experimental
    public void assertNoTerminalEvent() {
        List<Throwable> onErrorEvents = this.testObserver.getOnErrorEvents();
        int size = this.testObserver.getOnCompletedEvents().size();
        if (onErrorEvents.size() <= 0 && size <= 0) {
            return;
        }
        if (onErrorEvents.isEmpty()) {
            throw new AssertionError("Found " + onErrorEvents.size() + " errors and " + size + " completion events instead of none");
        } else if (onErrorEvents.size() == 1) {
            AssertionError assertionError = new AssertionError("Found " + onErrorEvents.size() + " errors and " + size + " completion events instead of none");
            assertionError.initCause(onErrorEvents.get(0));
            throw assertionError;
        } else {
            AssertionError assertionError2 = new AssertionError("Found " + onErrorEvents.size() + " errors and " + size + " completion events instead of none");
            assertionError2.initCause(new CompositeException(onErrorEvents));
            throw assertionError2;
        }
    }

    @Experimental
    public void assertNoValues() {
        int size = this.testObserver.getOnNextEvents().size();
        if (size > 0) {
            throw new AssertionError("No onNext events expected yet some received: " + size);
        }
    }

    @Experimental
    public void assertValueCount(int i) {
        int size = this.testObserver.getOnNextEvents().size();
        if (size != i) {
            throw new AssertionError("Number of onNext events differ; expected: " + i + ", actual: " + size);
        }
    }

    @Experimental
    public void assertValues(T... tArr) {
        assertReceivedOnNext(Arrays.asList(tArr));
    }

    @Experimental
    public void assertValue(T t) {
        assertReceivedOnNext(Collections.singletonList(t));
    }
}
