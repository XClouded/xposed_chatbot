package com.alibaba.aliweex.plugin;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkFlow {
    private static String TAG = "WorkFlow";
    private static HandlerThread mHandlerThread;
    private static Handler mMainHandler;
    private static Handler mTaskHandler;
    private static ExecutorService sExecutor;

    public interface Action<T, R> {
        R call(T t);
    }

    public static abstract class CancelAction<T> implements Action<T, T> {
        public T call(T t) {
            return t;
        }

        /* access modifiers changed from: protected */
        public abstract boolean cancel(T t);
    }

    interface Flowable<T, R> {

        public interface OnActionCall<R> {
            void onCall(R r);
        }

        public enum RunThread {
            CURRENT,
            UI,
            SUB,
            NEW,
            SERIALTASK
        }

        Flow countFlow(CountDownLatch countDownLatch);

        FlowNode<T, R> currentThread();

        Flow flow();

        void flowToNext(T t);

        Flow getContext();

        R getResult();

        boolean hasNext();

        boolean isLooping();

        FlowNode<T, R> newThread();

        Flowable<R, ?> next();

        void onActionCall(OnActionCall<R> onActionCall);

        Flowable<?, T> prior();

        void scheduleFlow(T t);

        FlowNode<T, R> serialTask();

        FlowNode<T, R> serialTask(int i);

        <A extends Action<T, R>> Flowable<T, R> setAction(A a);

        Flowable<T, R> setContext(Flow flow);

        Flowable<T, R> setNext(Flowable<R, ?> flowable);

        Flowable<T, R> setPrior(Flowable<?, T> flowable);

        FlowNode<T, R> subThread();

        FlowNode<T, R> uiThread();
    }

    public static abstract class JudgeAction<T> implements Action<T, T> {
        public T call(T t) {
            return t;
        }

        /* access modifiers changed from: protected */
        public abstract boolean judge(T t);
    }

    /* access modifiers changed from: private */
    public static synchronized Handler getMainHandler() {
        Handler handler;
        synchronized (WorkFlow.class) {
            if (mMainHandler == null) {
                mMainHandler = new Handler(Looper.getMainLooper());
            }
            handler = mMainHandler;
        }
        return handler;
    }

    /* access modifiers changed from: private */
    public static synchronized Handler getTaskHandler() {
        Handler handler;
        synchronized (WorkFlow.class) {
            if (mHandlerThread == null) {
                mHandlerThread = new HandlerThread("workflow-ht");
                mHandlerThread.start();
                mTaskHandler = new Handler(mHandlerThread.getLooper());
            }
            handler = mTaskHandler;
        }
        return handler;
    }

    /* access modifiers changed from: private */
    public static synchronized ExecutorService getExecutor() {
        ExecutorService executorService;
        synchronized (WorkFlow.class) {
            if (sExecutor == null) {
                sExecutor = Executors.newCachedThreadPool();
            }
            executorService = sExecutor;
        }
        return executorService;
    }

    /* access modifiers changed from: private */
    public static boolean isOnUIThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static class Work<T, R> {
        private Flowable<T, R> flowable;

        public Work(Flowable<T, R> flowable2) {
            this.flowable = flowable2;
        }

        public static Work<Void, Void> make() {
            return make((Void) null);
        }

        public static <R> Work<Void, R> make(R r) {
            return make(StartNode.make(r));
        }

        public static <T> Work<?, T> make(T[] tArr) {
            return make(Arrays.asList(tArr));
        }

        public static <T> Work<?, T> make(final Iterable<T> iterable) {
            return make().loop(new Action<Void, Iterable<T>>() {
                public Iterable<T> call(Void voidR) {
                    return iterable;
                }
            });
        }

        public <N> Work<Iterable<N>, N> loop(Action<R, Iterable<N>> action) {
            return new Work<>(ArrayNode.make(createNextNode(action)));
        }

        public <N> Work<R, N> next(Action<R, N> action) {
            return new Work<>(createNextNode(action).currentThread());
        }

        public <N> Work<R, N> ui(Action<R, N> action) {
            return new Work<>(createNextNode(action).uiThread());
        }

        public <N> Work<R, N> sub(Action<R, N> action) {
            return new Work<>(createNextNode(action).subThread());
        }

        public <N> Work<R, N> newThread(Action<R, N> action) {
            return new Work<>(createNextNode(action).newThread());
        }

        public <N> Work<R, N> serialTask(Action<R, N> action) {
            return new Work<>(createNextNode(action).serialTask());
        }

        public <S, N> Work<R, ParallelMerge<N>> branch(BranchParallel<S, R, N> branchParallel) {
            return new Work<>(createNextNode(branchParallel).subThread());
        }

        public Work<R, R> judge(JudgeAction<R> judgeAction) {
            return new Work<>(JudgeNode.make(judgeAction).setPrior(this.flowable).currentThread());
        }

        public Work<R, R> cancel(CancelAction<R> cancelAction) {
            return new Work<>(CancelNode.make(cancelAction).setPrior(this.flowable).currentThread());
        }

        public Work<T, R> cancelWhen(Flow.Cancelable cancelable) {
            this.flowable.getContext().setCancelable(cancelable);
            return this;
        }

        public Work<T, R> onCancel(Flow.CancelListener cancelListener) {
            this.flowable.getContext().setCancelListener(cancelListener);
            return this;
        }

        public Work<T, R> onError(Flow.ErrorListener errorListener) {
            this.flowable.getContext().setErrorListener(errorListener);
            return this;
        }

        public Work<T, R> onComplete(Flow.CompleteListener completeListener) {
            this.flowable.getContext().setCompleteListener(completeListener);
            return this;
        }

        public Work<T, R> runOnNewThread() {
            this.flowable.newThread();
            return this;
        }

        public Work<T, R> runOnSerialTask() {
            this.flowable.serialTask();
            return this;
        }

        public Work<T, R> runOnSerialTask(int i) {
            this.flowable.serialTask(i);
            return this;
        }

        public Flow flow() {
            return this.flowable.flow();
        }

        public Flow countFlow(CountDownLatch countDownLatch) {
            return this.flowable.countFlow(countDownLatch);
        }

        private static <T, R> Work<T, R> make(Flowable<T, R> flowable2) {
            flowable2.setContext(new Flow(flowable2));
            return new Work<>(flowable2);
        }

        private <N> Flowable<R, N> createNextNode(Action<R, N> action) {
            return NextNode.make(action).setPrior(this.flowable);
        }
    }

    public static class Flow {
        CancelListener cancelListener;
        Cancelable cancelable;
        CompleteListener completeListener;
        ErrorListener errorListener;
        WorkFlowException exception;
        Flowable<?, ?> headNode;
        boolean isCanceled;
        CountDownLatch latch;
        Flowable<?, ?> tailNode;

        public interface CancelListener {
            void onCancel();
        }

        public interface Cancelable {
            boolean cancel();
        }

        public interface CompleteListener {
            void onComplete();
        }

        public interface ErrorListener {
            void onError(Throwable th);
        }

        Flow(Flowable<?, ?> flowable) {
            this.headNode = flowable;
        }

        /* access modifiers changed from: package-private */
        public Flow setTailNode(Flowable<?, ?> flowable) {
            this.tailNode = flowable;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Flow flowStart() {
            this.headNode.scheduleFlow(null);
            return this;
        }

        /* access modifiers changed from: package-private */
        public void runOnUIThread(Runnable runnable) {
            if (WorkFlow.isOnUIThread()) {
                runnable.run();
            } else {
                WorkFlow.getMainHandler().post(runnable);
            }
        }

        /* access modifiers changed from: package-private */
        public void runOnNewThread(Runnable runnable) {
            WorkFlow.getExecutor().execute(runnable);
        }

        /* access modifiers changed from: package-private */
        public void runOnSerialTask(Runnable runnable) {
            WorkFlow.getTaskHandler().post(runnable);
        }

        /* access modifiers changed from: package-private */
        public void runOnSerialTask(Runnable runnable, int i) {
            WorkFlow.getTaskHandler().postDelayed(runnable, (long) i);
        }

        /* access modifiers changed from: package-private */
        public void flowToFinal() {
            if (this.latch != null) {
                this.latch.countDown();
            }
            if (WorkFlow.isOnUIThread()) {
                handleListenEvent();
            } else {
                runOnUIThread(new Runnable() {
                    public void run() {
                        Flow.this.handleListenEvent();
                    }
                });
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isCanceled() {
            return isCanceledByHand() || isCanceledPassively();
        }

        /* access modifiers changed from: package-private */
        public boolean isCanceledPassively() {
            return this.cancelable != null && this.cancelable.cancel();
        }

        /* access modifiers changed from: package-private */
        public boolean isCanceledByHand() {
            return this.isCanceled;
        }

        /* access modifiers changed from: package-private */
        public Flow cancelFlow() {
            this.isCanceled = true;
            return this;
        }

        public Flow setException(WorkFlowException workFlowException) {
            this.exception = workFlowException;
            return this;
        }

        public void setCancelable(Cancelable cancelable2) {
            this.cancelable = cancelable2;
        }

        public void setCancelListener(CancelListener cancelListener2) {
            this.cancelListener = cancelListener2;
        }

        public void setErrorListener(ErrorListener errorListener2) {
            this.errorListener = errorListener2;
        }

        public void setCompleteListener(CompleteListener completeListener2) {
            this.completeListener = completeListener2;
        }

        public Flow setCountDownLatch(CountDownLatch countDownLatch) {
            this.latch = countDownLatch;
            return this;
        }

        /* access modifiers changed from: private */
        public void handleListenEvent() {
            if (isCanceled() && this.cancelListener != null) {
                this.cancelListener.onCancel();
            } else if (this.exception != null && this.errorListener != null) {
                this.errorListener.onError(this.exception);
            } else if (this.completeListener != null) {
                this.completeListener.onComplete();
            }
        }
    }

    private static class CancelNode<T> extends FlowNode<T, T> {
        private CancelNode() {
        }

        static <T> Flowable<T, T> make(CancelAction<T> cancelAction) {
            return new CancelNode().setAction(cancelAction);
        }

        public void flowToNext(T t) {
            if (((CancelAction) getAction()).cancel(t)) {
                getContext().cancelFlow();
                getContext().flowToFinal();
                return;
            }
            super.flowToNext(t);
        }
    }

    private static class JudgeNode<T> extends FlowNode<T, T> {
        private JudgeNode() {
        }

        static <T> Flowable<T, T> make(JudgeAction<T> judgeAction) {
            return new JudgeNode().setAction(judgeAction);
        }

        public void flowToNext(T t) {
            if (((JudgeAction) getAction()).judge(t)) {
                super.flowToNext(t);
                return;
            }
            Flowable<?, ?> findLoopNode = findLoopNode();
            if (findLoopNode != null) {
                findLoopNode.scheduleFlow(findLoopNode.prior().getResult());
            } else {
                this.context.flowToFinal();
            }
        }
    }

    private static class ArrayNode<I extends Iterable<R>, R> extends FlowNode<I, R> {
        /* access modifiers changed from: private */
        public Iterator<R> iterator;

        private ArrayNode() {
        }

        static <I extends Iterable<R>, R> Flowable<I, R> make(Flowable<?, I> flowable) {
            ArrayNode arrayNode = new ArrayNode();
            flowable.onActionCall(new Flowable.OnActionCall<I>(arrayNode) {
                final /* synthetic */ ArrayNode val$node;

                {
                    this.val$node = r1;
                }

                public void onCall(I i) {
                    Iterator unused = this.val$node.iterator = i.iterator();
                }
            });
            arrayNode.setAction(new Action<I, R>(arrayNode) {
                final /* synthetic */ ArrayNode val$node;

                {
                    this.val$node = r1;
                }

                public R call(I i) {
                    if (this.val$node.iterator.hasNext()) {
                        return this.val$node.iterator.next();
                    }
                    return null;
                }
            });
            return arrayNode.setPrior(flowable);
        }

        public boolean isLooping() {
            return this.iterator.hasNext();
        }
    }

    public static class BranchParallelMerge<T, R> extends BranchMerge<T> {
        int count;
        List<R> results = new Vector(this.count);
        List<Work<T, R>> works;

        BranchParallelMerge(List<Work<T, R>> list) {
            this.works = list;
            this.count = list.size();
        }

        /* access modifiers changed from: package-private */
        public ParallelMerge<R> call() {
            flowAndWait();
            return new ParallelMerge<>(this.results);
        }

        /* access modifiers changed from: package-private */
        public CountDownLatch createLatch() {
            return new CountDownLatch(this.count);
        }

        /* access modifiers changed from: package-private */
        public CountDownLatch branchFlow() {
            CountDownLatch createLatch = createLatch();
            for (Work<T, R> next : this.works) {
                next.next(new EndAction<R>() {
                    public void end(R r) {
                        BranchParallelMerge.this.results.add(r);
                    }
                }).countFlow(createLatch);
            }
            return createLatch;
        }
    }

    public static class ParallelMerge<R> {
        private List<R> results;

        ParallelMerge(List<R> list) {
            this.results = list;
        }

        public List<R> getResults() {
            return this.results;
        }
    }

    public static abstract class BranchMerge<T> {
        /* access modifiers changed from: package-private */
        public abstract CountDownLatch branchFlow();

        /* access modifiers changed from: package-private */
        public abstract CountDownLatch createLatch();

        /* access modifiers changed from: package-private */
        public final void flowAndWait() {
            try {
                branchFlow().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static abstract class BranchParallel<N, T, R> extends Branch<N> implements Action<T, ParallelMerge<R>> {
        List<N> data;

        public abstract R branch(int i, N n);

        public BranchParallel(List<N> list) {
            this.data = list;
        }

        public ParallelMerge<R> call(T t) {
            ArrayList arrayList = new ArrayList();
            for (final int i = 0; i < this.data.size(); i++) {
                arrayList.add(createWork(this.data.get(i)).next(new Action<N, R>() {
                    public R call(N n) {
                        return BranchParallel.this.branch(i, n);
                    }
                }));
            }
            return new BranchParallelMerge(arrayList).call();
        }
    }

    public static abstract class Branch<T> {
        /* access modifiers changed from: package-private */
        public Work<Void, T> createWork(T t) {
            return Work.make(t).runOnNewThread();
        }
    }

    static class NextNode<T, R> extends FlowNode<T, R> {
        NextNode(Action<T, R> action) {
            super(action);
        }

        static <T, R> FlowNode<T, R> make(Action<T, R> action) {
            return new NextNode(action);
        }
    }

    static class StartNode<R> extends FlowNode<Void, R> {
        StartNode(Action<Void, R> action) {
            super(action);
        }

        static <R> FlowNode<Void, R> make(final R r) {
            return new StartNode(new Action<Void, R>() {
                public R call(Void voidR) {
                    return r;
                }
            });
        }
    }

    static abstract class FlowNode<T, R> implements Flowable<T, R> {
        Action<T, R> action;
        Flowable.OnActionCall<R> actionCall;
        R actionResult;
        Flow context;
        Flowable<R, ?> next;
        Flowable<?, T> prior;
        Flowable.RunThread runThread = Flowable.RunThread.CURRENT;
        int threadTimeout = -1;

        public boolean isLooping() {
            return false;
        }

        FlowNode() {
        }

        FlowNode(Action<T, R> action2) {
            setAction(action2);
        }

        public Flowable<T, R> setContext(Flow flow) {
            this.context = flow;
            return this;
        }

        public Flow getContext() {
            return this.context;
        }

        public <A extends Action<T, R>> Flowable<T, R> setAction(A a) {
            this.action = a;
            return this;
        }

        public final <S extends Action<T, R>> S getAction() {
            return this.action;
        }

        public Flowable<R, ?> next() {
            return this.next;
        }

        public Flowable<?, T> prior() {
            return this.prior;
        }

        public Flowable<T, R> setNext(Flowable<R, ?> flowable) {
            this.next = flowable;
            return this;
        }

        public Flowable<T, R> setPrior(Flowable<?, T> flowable) {
            this.prior = flowable;
            this.prior.setNext(this);
            setContext(flowable.getContext());
            return this;
        }

        public FlowNode<T, R> uiThread() {
            this.runThread = Flowable.RunThread.UI;
            return this;
        }

        public FlowNode<T, R> subThread() {
            this.runThread = Flowable.RunThread.SUB;
            return this;
        }

        public FlowNode<T, R> newThread() {
            this.runThread = Flowable.RunThread.NEW;
            return this;
        }

        public FlowNode<T, R> currentThread() {
            this.runThread = Flowable.RunThread.CURRENT;
            return this;
        }

        public FlowNode<T, R> serialTask() {
            this.runThread = Flowable.RunThread.SERIALTASK;
            return this;
        }

        public FlowNode<T, R> serialTask(int i) {
            this.runThread = Flowable.RunThread.SERIALTASK;
            this.threadTimeout = i;
            return this;
        }

        public Flow flow() {
            return this.context.setTailNode(this).flowStart();
        }

        public Flow countFlow(CountDownLatch countDownLatch) {
            return this.context.setTailNode(this).setCountDownLatch(countDownLatch).flowStart();
        }

        public void scheduleFlow(final T t) {
            if (this.context.isCanceled()) {
                this.context.flowToFinal();
                return;
            }
            switch (this.runThread) {
                case CURRENT:
                    flowToNext(t);
                    return;
                case UI:
                    if (WorkFlow.isOnUIThread()) {
                        flowToNext(t);
                        return;
                    } else {
                        this.context.runOnUIThread(new Runnable() {
                            public void run() {
                                FlowNode.this.flowToNext(t);
                            }
                        });
                        return;
                    }
                case SUB:
                    if (WorkFlow.isOnUIThread()) {
                        this.context.runOnNewThread(new Runnable() {
                            public void run() {
                                FlowNode.this.flowToNext(t);
                            }
                        });
                        return;
                    } else {
                        flowToNext(t);
                        return;
                    }
                case NEW:
                    this.context.runOnNewThread(new Runnable() {
                        public void run() {
                            FlowNode.this.flowToNext(t);
                        }
                    });
                    return;
                case SERIALTASK:
                    if (this.threadTimeout > 0) {
                        this.context.runOnSerialTask(new Runnable() {
                            public void run() {
                                FlowNode.this.flowToNext(t);
                            }
                        }, this.threadTimeout);
                        return;
                    } else {
                        this.context.runOnSerialTask(new Runnable() {
                            public void run() {
                                FlowNode.this.flowToNext(t);
                            }
                        });
                        return;
                    }
                default:
                    flowToNext(t);
                    return;
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public void flowToNext(T t) {
            try {
                Object callThis = callThis(t);
                if (this.actionCall != null) {
                    this.actionCall.onCall(callThis);
                }
                if (hasNext()) {
                    next().scheduleFlow(callThis);
                    return;
                }
                Flowable<?, ?> findLoopNode = findLoopNode();
                if (findLoopNode != null) {
                    findLoopNode.scheduleFlow(findLoopNode.prior().getResult());
                } else {
                    this.context.flowToFinal();
                }
            } catch (Throwable th) {
                th.printStackTrace();
                if (th instanceof WorkFlowException) {
                    this.context.setException((WorkFlowException) th).flowToFinal();
                } else {
                    this.context.setException(new WorkFlowException(th)).flowToFinal();
                }
            }
        }

        public R getResult() {
            return this.actionResult;
        }

        public void onActionCall(Flowable.OnActionCall<R> onActionCall) {
            this.actionCall = onActionCall;
        }

        private R callThis(T t) {
            this.actionResult = this.action.call(t);
            return this.actionResult;
        }

        /* access modifiers changed from: package-private */
        public Flowable<?, ?> findLoopNode() {
            for (Flowable<?, ?> flowable = this; flowable != null; flowable = flowable.prior()) {
                if (flowable.isLooping()) {
                    return flowable;
                }
            }
            return null;
        }
    }

    public static abstract class EndAction<T> implements Action<T, Void> {
        public abstract void end(T t);

        public Void call(T t) {
            end(t);
            return null;
        }
    }

    static class WorkFlowException extends RuntimeException {
        WorkFlowException(Throwable th) {
            super(th);
        }

        public String toString() {
            return "WorkException{causeException=" + getCause() + "} " + super.toString();
        }
    }
}
