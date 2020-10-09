package rx.internal.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.SerialSubscription;

public final class OnSubscribeJoin<TLeft, TRight, TLeftDuration, TRightDuration, R> implements Observable.OnSubscribe<R> {
    final Observable<TLeft> left;
    final Func1<TLeft, Observable<TLeftDuration>> leftDurationSelector;
    final Func2<TLeft, TRight, R> resultSelector;
    final Observable<TRight> right;
    final Func1<TRight, Observable<TRightDuration>> rightDurationSelector;

    public OnSubscribeJoin(Observable<TLeft> observable, Observable<TRight> observable2, Func1<TLeft, Observable<TLeftDuration>> func1, Func1<TRight, Observable<TRightDuration>> func12, Func2<TLeft, TRight, R> func2) {
        this.left = observable;
        this.right = observable2;
        this.leftDurationSelector = func1;
        this.rightDurationSelector = func12;
        this.resultSelector = func2;
    }

    public void call(Subscriber<? super R> subscriber) {
        new ResultSink(new SerializedSubscriber(subscriber)).run();
    }

    final class ResultSink {
        final CompositeSubscription group;
        final Object guard = new Object();
        boolean leftDone;
        int leftId;
        final Map<Integer, TLeft> leftMap;
        boolean rightDone;
        int rightId;
        final Map<Integer, TRight> rightMap;
        final Subscriber<? super R> subscriber;

        public ResultSink(Subscriber<? super R> subscriber2) {
            this.subscriber = subscriber2;
            this.group = new CompositeSubscription();
            this.leftMap = new HashMap();
            this.rightMap = new HashMap();
        }

        public void run() {
            this.subscriber.add(this.group);
            LeftSubscriber leftSubscriber = new LeftSubscriber();
            RightSubscriber rightSubscriber = new RightSubscriber();
            this.group.add(leftSubscriber);
            this.group.add(rightSubscriber);
            OnSubscribeJoin.this.left.unsafeSubscribe(leftSubscriber);
            OnSubscribeJoin.this.right.unsafeSubscribe(rightSubscriber);
        }

        final class LeftSubscriber extends Subscriber<TLeft> {
            LeftSubscriber() {
            }

            /* access modifiers changed from: protected */
            public void expire(int i, Subscription subscription) {
                boolean z;
                synchronized (ResultSink.this.guard) {
                    z = ResultSink.this.leftMap.remove(Integer.valueOf(i)) != null && ResultSink.this.leftMap.isEmpty() && ResultSink.this.leftDone;
                }
                if (z) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(subscription);
            }

            public void onNext(TLeft tleft) {
                int i;
                int i2;
                synchronized (ResultSink.this.guard) {
                    ResultSink resultSink = ResultSink.this;
                    i = resultSink.leftId;
                    resultSink.leftId = i + 1;
                    ResultSink.this.leftMap.put(Integer.valueOf(i), tleft);
                    i2 = ResultSink.this.rightId;
                }
                try {
                    LeftDurationSubscriber leftDurationSubscriber = new LeftDurationSubscriber(i);
                    ResultSink.this.group.add(leftDurationSubscriber);
                    OnSubscribeJoin.this.leftDurationSelector.call(tleft).unsafeSubscribe(leftDurationSubscriber);
                    ArrayList<Object> arrayList = new ArrayList<>();
                    synchronized (ResultSink.this.guard) {
                        for (Map.Entry next : ResultSink.this.rightMap.entrySet()) {
                            if (((Integer) next.getKey()).intValue() < i2) {
                                arrayList.add(next.getValue());
                            }
                        }
                    }
                    for (Object call : arrayList) {
                        ResultSink.this.subscriber.onNext(OnSubscribeJoin.this.resultSelector.call(tleft, call));
                    }
                } catch (Throwable th) {
                    onError(th);
                }
            }

            public void onError(Throwable th) {
                ResultSink.this.subscriber.onError(th);
                ResultSink.this.subscriber.unsubscribe();
            }

            public void onCompleted() {
                boolean z;
                synchronized (ResultSink.this.guard) {
                    z = true;
                    ResultSink.this.leftDone = true;
                    if (!ResultSink.this.rightDone) {
                        if (!ResultSink.this.leftMap.isEmpty()) {
                            z = false;
                        }
                    }
                }
                if (z) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(this);
            }

            final class LeftDurationSubscriber extends Subscriber<TLeftDuration> {
                final int id;
                boolean once = true;

                public LeftDurationSubscriber(int i) {
                    this.id = i;
                }

                public void onNext(TLeftDuration tleftduration) {
                    onCompleted();
                }

                public void onError(Throwable th) {
                    LeftSubscriber.this.onError(th);
                }

                public void onCompleted() {
                    if (this.once) {
                        this.once = false;
                        LeftSubscriber.this.expire(this.id, this);
                    }
                }
            }
        }

        final class RightSubscriber extends Subscriber<TRight> {
            RightSubscriber() {
            }

            /* access modifiers changed from: package-private */
            public void expire(int i, Subscription subscription) {
                boolean z;
                synchronized (ResultSink.this.guard) {
                    z = ResultSink.this.rightMap.remove(Integer.valueOf(i)) != null && ResultSink.this.rightMap.isEmpty() && ResultSink.this.rightDone;
                }
                if (z) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(subscription);
            }

            public void onNext(TRight tright) {
                int i;
                int i2;
                synchronized (ResultSink.this.guard) {
                    ResultSink resultSink = ResultSink.this;
                    i = resultSink.rightId;
                    resultSink.rightId = i + 1;
                    ResultSink.this.rightMap.put(Integer.valueOf(i), tright);
                    i2 = ResultSink.this.leftId;
                }
                ResultSink.this.group.add(new SerialSubscription());
                try {
                    RightDurationSubscriber rightDurationSubscriber = new RightDurationSubscriber(i);
                    ResultSink.this.group.add(rightDurationSubscriber);
                    OnSubscribeJoin.this.rightDurationSelector.call(tright).unsafeSubscribe(rightDurationSubscriber);
                    ArrayList<Object> arrayList = new ArrayList<>();
                    synchronized (ResultSink.this.guard) {
                        for (Map.Entry next : ResultSink.this.leftMap.entrySet()) {
                            if (((Integer) next.getKey()).intValue() < i2) {
                                arrayList.add(next.getValue());
                            }
                        }
                    }
                    for (Object call : arrayList) {
                        ResultSink.this.subscriber.onNext(OnSubscribeJoin.this.resultSelector.call(call, tright));
                    }
                } catch (Throwable th) {
                    onError(th);
                }
            }

            public void onError(Throwable th) {
                ResultSink.this.subscriber.onError(th);
                ResultSink.this.subscriber.unsubscribe();
            }

            public void onCompleted() {
                boolean z;
                synchronized (ResultSink.this.guard) {
                    z = true;
                    ResultSink.this.rightDone = true;
                    if (!ResultSink.this.leftDone) {
                        if (!ResultSink.this.rightMap.isEmpty()) {
                            z = false;
                        }
                    }
                }
                if (z) {
                    ResultSink.this.subscriber.onCompleted();
                    ResultSink.this.subscriber.unsubscribe();
                    return;
                }
                ResultSink.this.group.remove(this);
            }

            final class RightDurationSubscriber extends Subscriber<TRightDuration> {
                final int id;
                boolean once = true;

                public RightDurationSubscriber(int i) {
                    this.id = i;
                }

                public void onNext(TRightDuration trightduration) {
                    onCompleted();
                }

                public void onError(Throwable th) {
                    RightSubscriber.this.onError(th);
                }

                public void onCompleted() {
                    if (this.once) {
                        this.once = false;
                        RightSubscriber.this.expire(this.id, this);
                    }
                }
            }
        }
    }
}
