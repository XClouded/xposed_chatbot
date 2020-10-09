package rx;

import rx.annotations.Experimental;
import rx.internal.util.SubscriptionList;

@Experimental
public abstract class SingleSubscriber<T> implements Subscription {
    private final SubscriptionList cs = new SubscriptionList();

    public abstract void onError(Throwable th);

    public abstract void onSuccess(T t);

    public final void add(Subscription subscription) {
        this.cs.add(subscription);
    }

    public final void unsubscribe() {
        this.cs.unsubscribe();
    }

    public final boolean isUnsubscribed() {
        return this.cs.isUnsubscribed();
    }
}
