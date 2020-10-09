package rx.observers;

import java.io.PrintStream;
import java.util.Arrays;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorFailedException;
import rx.plugins.RxJavaPlugins;

public class SafeSubscriber<T> extends Subscriber<T> {
    private final Subscriber<? super T> actual;
    boolean done = false;

    public SafeSubscriber(Subscriber<? super T> subscriber) {
        super(subscriber);
        this.actual = subscriber;
    }

    public void onCompleted() {
        if (!this.done) {
            this.done = true;
            try {
                this.actual.onCompleted();
            } catch (Throwable th) {
                unsubscribe();
                throw th;
            }
            unsubscribe();
        }
    }

    public void onError(Throwable th) {
        Exceptions.throwIfFatal(th);
        if (!this.done) {
            this.done = true;
            _onError(th);
        }
    }

    public void onNext(T t) {
        try {
            if (!this.done) {
                this.actual.onNext(t);
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            onError(th);
        }
    }

    /* access modifiers changed from: protected */
    public void _onError(Throwable th) {
        try {
            RxJavaPlugins.getInstance().getErrorHandler().handleError(th);
        } catch (Throwable th2) {
            handlePluginException(th2);
        }
        try {
            this.actual.onError(th);
            try {
                unsubscribe();
                return;
            } catch (RuntimeException e) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            } catch (Throwable th3) {
                handlePluginException(th3);
            }
            unsubscribe();
            throw new OnErrorFailedException("Error occurred when trying to propagate error to Observer.onError", new CompositeException(Arrays.asList(new Throwable[]{th, th})));
            throw new OnErrorFailedException(e);
        } catch (Throwable th4) {
            try {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(th4);
            } catch (Throwable th5) {
                handlePluginException(th5);
            }
            throw new OnErrorFailedException("Error occurred when trying to propagate error to Observer.onError and during unsubscription.", new CompositeException(Arrays.asList(new Throwable[]{th, th, th4})));
        }
    }

    private void handlePluginException(Throwable th) {
        PrintStream printStream = System.err;
        printStream.println("RxJavaErrorHandler threw an Exception. It shouldn't. => " + th.getMessage());
        th.printStackTrace();
    }

    public Subscriber<? super T> getActual() {
        return this.actual;
    }
}
