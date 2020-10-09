package kotlin.collections;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequenceScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlin.collections.SlidingWindowKt$windowedIterator$1", f = "SlidingWindow.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4}, l = {33, 39, 46, 52, 55}, m = "invokeSuspend", n = {"$this$iterator", "gap", "buffer", "skip", "e", "$this$iterator", "gap", "buffer", "skip", "$this$iterator", "gap", "buffer", "e", "$this$iterator", "gap", "buffer", "$this$iterator", "gap", "buffer"}, s = {"L$0", "I$0", "L$1", "I$1", "L$2", "L$0", "I$0", "L$1", "I$1", "L$0", "I$0", "L$1", "L$2", "L$0", "I$0", "L$1", "L$0", "I$0", "L$1"})
/* compiled from: SlidingWindow.kt */
final class SlidingWindowKt$windowedIterator$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super List<? extends T>>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Iterator $iterator;
    final /* synthetic */ boolean $partialWindows;
    final /* synthetic */ boolean $reuseBuffer;
    final /* synthetic */ int $size;
    final /* synthetic */ int $step;
    int I$0;
    int I$1;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private SequenceScope p$;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SlidingWindowKt$windowedIterator$1(int i, int i2, Iterator it, boolean z, boolean z2, Continuation continuation) {
        super(2, continuation);
        this.$step = i;
        this.$size = i2;
        this.$iterator = it;
        this.$reuseBuffer = z;
        this.$partialWindows = z2;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        SlidingWindowKt$windowedIterator$1 slidingWindowKt$windowedIterator$1 = new SlidingWindowKt$windowedIterator$1(this.$step, this.$size, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
        slidingWindowKt$windowedIterator$1.p$ = (SequenceScope) obj;
        return slidingWindowKt$windowedIterator$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((SlidingWindowKt$windowedIterator$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0086, code lost:
        if (r1.hasNext() == false) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0088, code lost:
        r7 = r1.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x008c, code lost:
        if (r4 <= 0) goto L_0x0091;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x008e, code lost:
        r4 = r4 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0091, code lost:
        r3.add(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x009a, code lost:
        if (r3.size() != r12.$size) goto L_0x0082;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x009c, code lost:
        r12.L$0 = r5;
        r12.I$0 = r0;
        r12.L$1 = r3;
        r12.I$1 = r4;
        r12.L$2 = r7;
        r12.L$3 = r1;
        r12.label = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00ae, code lost:
        if (r5.yield(r3, r12) != r6) goto L_0x00b1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00b0, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00b3, code lost:
        if (r12.$reuseBuffer == false) goto L_0x00b9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00b5, code lost:
        r3.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00b9, code lost:
        r3 = new java.util.ArrayList(r12.$size);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00c0, code lost:
        r4 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00ca, code lost:
        if ((!r3.isEmpty()) == false) goto L_0x0188;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00ce, code lost:
        if (r12.$partialWindows != false) goto L_0x00d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00d6, code lost:
        if (r3.size() != r12.$size) goto L_0x0188;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00d8, code lost:
        r12.L$0 = r5;
        r12.I$0 = r0;
        r12.L$1 = r3;
        r12.I$1 = r4;
        r12.label = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e7, code lost:
        if (r5.yield(r3, r12) != r6) goto L_0x0188;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e9, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00fc, code lost:
        if (r1.hasNext() == false) goto L_0x0137;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00fe, code lost:
        r6 = r1.next();
        r3.add(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0109, code lost:
        if (r3.isFull() == false) goto L_0x00f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x010d, code lost:
        if (r12.$reuseBuffer == false) goto L_0x0113;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x010f, code lost:
        r7 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0113, code lost:
        r7 = new java.util.ArrayList(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x011d, code lost:
        r12.L$0 = r5;
        r12.I$0 = r4;
        r12.L$1 = r3;
        r12.L$2 = r6;
        r12.L$3 = r1;
        r12.label = 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x012e, code lost:
        if (r5.yield(r7, r12) != r0) goto L_0x0131;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0130, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0131, code lost:
        r3.removeFirst(r12.$step);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0139, code lost:
        if (r12.$partialWindows == false) goto L_0x0188;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x013b, code lost:
        r1 = r3;
        r3 = r4;
        r4 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0144, code lost:
        if (r1.size() <= r12.$step) goto L_0x016e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0148, code lost:
        if (r12.$reuseBuffer == false) goto L_0x014e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x014a, code lost:
        r5 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x014e, code lost:
        r5 = new java.util.ArrayList(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0158, code lost:
        r12.L$0 = r4;
        r12.I$0 = r3;
        r12.L$1 = r1;
        r12.label = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0165, code lost:
        if (r4.yield(r5, r12) != r0) goto L_0x0168;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0167, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0168, code lost:
        r1.removeFirst(r12.$step);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0176, code lost:
        if ((true ^ r1.isEmpty()) == false) goto L_0x0188;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0178, code lost:
        r12.L$0 = r4;
        r12.I$0 = r3;
        r12.L$1 = r1;
        r12.label = 5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0185, code lost:
        if (r4.yield(r1, r12) != r0) goto L_0x0188;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0187, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x018a, code lost:
        return kotlin.Unit.INSTANCE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0043, code lost:
        r0 = r11.I$0;
        r0 = (kotlin.sequences.SequenceScope) r11.L$0;
        kotlin.ResultKt.throwOnFailure(r12);
     */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r12) {
        /*
            r11 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r11.label
            r2 = 1
            switch(r1) {
                case 0: goto L_0x0067;
                case 1: goto L_0x004e;
                case 2: goto L_0x003d;
                case 3: goto L_0x0027;
                case 4: goto L_0x0017;
                case 5: goto L_0x0012;
                default: goto L_0x000a;
            }
        L_0x000a:
            java.lang.IllegalStateException r12 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r12.<init>(r0)
            throw r12
        L_0x0012:
            java.lang.Object r0 = r11.L$1
            kotlin.collections.RingBuffer r0 = (kotlin.collections.RingBuffer) r0
            goto L_0x0043
        L_0x0017:
            java.lang.Object r1 = r11.L$1
            kotlin.collections.RingBuffer r1 = (kotlin.collections.RingBuffer) r1
            int r3 = r11.I$0
            java.lang.Object r4 = r11.L$0
            kotlin.sequences.SequenceScope r4 = (kotlin.sequences.SequenceScope) r4
            kotlin.ResultKt.throwOnFailure(r12)
            r12 = r11
            goto L_0x0168
        L_0x0027:
            java.lang.Object r1 = r11.L$3
            java.util.Iterator r1 = (java.util.Iterator) r1
            java.lang.Object r3 = r11.L$2
            java.lang.Object r3 = r11.L$1
            kotlin.collections.RingBuffer r3 = (kotlin.collections.RingBuffer) r3
            int r4 = r11.I$0
            java.lang.Object r5 = r11.L$0
            kotlin.sequences.SequenceScope r5 = (kotlin.sequences.SequenceScope) r5
            kotlin.ResultKt.throwOnFailure(r12)
            r12 = r11
            goto L_0x0131
        L_0x003d:
            int r0 = r11.I$1
            java.lang.Object r0 = r11.L$1
            java.util.ArrayList r0 = (java.util.ArrayList) r0
        L_0x0043:
            int r0 = r11.I$0
            java.lang.Object r0 = r11.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r12)
            goto L_0x0188
        L_0x004e:
            java.lang.Object r1 = r11.L$3
            java.util.Iterator r1 = (java.util.Iterator) r1
            java.lang.Object r3 = r11.L$2
            int r3 = r11.I$1
            java.lang.Object r3 = r11.L$1
            java.util.ArrayList r3 = (java.util.ArrayList) r3
            int r4 = r11.I$0
            java.lang.Object r5 = r11.L$0
            kotlin.sequences.SequenceScope r5 = (kotlin.sequences.SequenceScope) r5
            kotlin.ResultKt.throwOnFailure(r12)
            r12 = r11
            r6 = r0
            r0 = r4
            goto L_0x00b1
        L_0x0067:
            kotlin.ResultKt.throwOnFailure(r12)
            kotlin.sequences.SequenceScope r12 = r11.p$
            int r1 = r11.$step
            int r3 = r11.$size
            int r1 = r1 - r3
            if (r1 < 0) goto L_0x00ea
            java.util.ArrayList r3 = new java.util.ArrayList
            int r4 = r11.$size
            r3.<init>(r4)
            r4 = 0
            java.util.Iterator r5 = r11.$iterator
            r6 = r0
            r0 = r1
            r1 = r5
            r5 = r12
            r12 = r11
        L_0x0082:
            boolean r7 = r1.hasNext()
            if (r7 == 0) goto L_0x00c2
            java.lang.Object r7 = r1.next()
            if (r4 <= 0) goto L_0x0091
            int r4 = r4 + -1
            goto L_0x0082
        L_0x0091:
            r3.add(r7)
            int r8 = r3.size()
            int r9 = r12.$size
            if (r8 != r9) goto L_0x0082
            r12.L$0 = r5
            r12.I$0 = r0
            r12.L$1 = r3
            r12.I$1 = r4
            r12.L$2 = r7
            r12.L$3 = r1
            r12.label = r2
            java.lang.Object r4 = r5.yield(r3, r12)
            if (r4 != r6) goto L_0x00b1
            return r6
        L_0x00b1:
            boolean r4 = r12.$reuseBuffer
            if (r4 == 0) goto L_0x00b9
            r3.clear()
            goto L_0x00c0
        L_0x00b9:
            java.util.ArrayList r3 = new java.util.ArrayList
            int r4 = r12.$size
            r3.<init>(r4)
        L_0x00c0:
            r4 = r0
            goto L_0x0082
        L_0x00c2:
            r1 = r3
            java.util.Collection r1 = (java.util.Collection) r1
            boolean r1 = r1.isEmpty()
            r1 = r1 ^ r2
            if (r1 == 0) goto L_0x0188
            boolean r1 = r12.$partialWindows
            if (r1 != 0) goto L_0x00d8
            int r1 = r3.size()
            int r2 = r12.$size
            if (r1 != r2) goto L_0x0188
        L_0x00d8:
            r12.L$0 = r5
            r12.I$0 = r0
            r12.L$1 = r3
            r12.I$1 = r4
            r0 = 2
            r12.label = r0
            java.lang.Object r12 = r5.yield(r3, r12)
            if (r12 != r6) goto L_0x0188
            return r6
        L_0x00ea:
            kotlin.collections.RingBuffer r3 = new kotlin.collections.RingBuffer
            int r4 = r11.$size
            r3.<init>(r4)
            java.util.Iterator r4 = r11.$iterator
            r5 = r12
            r12 = r11
            r10 = r4
            r4 = r1
            r1 = r10
        L_0x00f8:
            boolean r6 = r1.hasNext()
            if (r6 == 0) goto L_0x0137
            java.lang.Object r6 = r1.next()
            r3.add(r6)
            boolean r7 = r3.isFull()
            if (r7 == 0) goto L_0x00f8
            boolean r7 = r12.$reuseBuffer
            if (r7 == 0) goto L_0x0113
            r7 = r3
            java.util.List r7 = (java.util.List) r7
            goto L_0x011d
        L_0x0113:
            java.util.ArrayList r7 = new java.util.ArrayList
            r8 = r3
            java.util.Collection r8 = (java.util.Collection) r8
            r7.<init>(r8)
            java.util.List r7 = (java.util.List) r7
        L_0x011d:
            r12.L$0 = r5
            r12.I$0 = r4
            r12.L$1 = r3
            r12.L$2 = r6
            r12.L$3 = r1
            r6 = 3
            r12.label = r6
            java.lang.Object r6 = r5.yield(r7, r12)
            if (r6 != r0) goto L_0x0131
            return r0
        L_0x0131:
            int r6 = r12.$step
            r3.removeFirst(r6)
            goto L_0x00f8
        L_0x0137:
            boolean r1 = r12.$partialWindows
            if (r1 == 0) goto L_0x0188
            r1 = r3
            r3 = r4
            r4 = r5
        L_0x013e:
            int r5 = r1.size()
            int r6 = r12.$step
            if (r5 <= r6) goto L_0x016e
            boolean r5 = r12.$reuseBuffer
            if (r5 == 0) goto L_0x014e
            r5 = r1
            java.util.List r5 = (java.util.List) r5
            goto L_0x0158
        L_0x014e:
            java.util.ArrayList r5 = new java.util.ArrayList
            r6 = r1
            java.util.Collection r6 = (java.util.Collection) r6
            r5.<init>(r6)
            java.util.List r5 = (java.util.List) r5
        L_0x0158:
            r12.L$0 = r4
            r12.I$0 = r3
            r12.L$1 = r1
            r6 = 4
            r12.label = r6
            java.lang.Object r5 = r4.yield(r5, r12)
            if (r5 != r0) goto L_0x0168
            return r0
        L_0x0168:
            int r5 = r12.$step
            r1.removeFirst(r5)
            goto L_0x013e
        L_0x016e:
            r5 = r1
            java.util.Collection r5 = (java.util.Collection) r5
            boolean r5 = r5.isEmpty()
            r2 = r2 ^ r5
            if (r2 == 0) goto L_0x0188
            r12.L$0 = r4
            r12.I$0 = r3
            r12.L$1 = r1
            r2 = 5
            r12.label = r2
            java.lang.Object r12 = r4.yield(r1, r12)
            if (r12 != r0) goto L_0x0188
            return r0
        L_0x0188:
            kotlin.Unit r12 = kotlin.Unit.INSTANCE
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.SlidingWindowKt$windowedIterator$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
