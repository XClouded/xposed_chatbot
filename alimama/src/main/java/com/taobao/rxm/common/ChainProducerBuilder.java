package com.taobao.rxm.common;

import com.taobao.rxm.produce.ChainProducer;
import com.taobao.rxm.produce.Producer;
import com.taobao.rxm.request.RequestContext;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Type;

public class ChainProducerBuilder<OUT, CONTEXT extends RequestContext> {
    private final boolean mEnableGenericTypeCheck;
    private final Producer<OUT, CONTEXT> mHeadProducer;
    private ChainProducer mTailProducer;

    public <NEXT_OUT extends Releasable> ChainProducerBuilder(ChainProducer<OUT, NEXT_OUT, CONTEXT> chainProducer, boolean z) {
        Preconditions.checkNotNull(chainProducer);
        this.mEnableGenericTypeCheck = z;
        if (this.mEnableGenericTypeCheck && chainProducer.maySkipResultConsume() && chainProducer.getOutType() != chainProducer.getNextOutType()) {
            throwConsumeTypeError(chainProducer.getName());
        }
        this.mHeadProducer = chainProducer;
        this.mTailProducer = chainProducer;
    }

    public void throwConsumeTypeError(String str) {
        throw new IllegalArgumentException(str + " skip to consume new result, require OUT class must equal NEXT_OUT class");
    }

    public static <O, NEXT_O extends Releasable, CONTEXT extends RequestContext> ChainProducerBuilder<O, CONTEXT> newBuilderWithHead(ChainProducer<O, NEXT_O, CONTEXT> chainProducer) {
        return newBuilderWithHead(chainProducer, true);
    }

    public static <O, NEXT_O extends Releasable, CONTEXT extends RequestContext> ChainProducerBuilder<O, CONTEXT> newBuilderWithHead(ChainProducer<O, NEXT_O, CONTEXT> chainProducer, boolean z) {
        return new ChainProducerBuilder<>(chainProducer, z);
    }

    public <NEXT_O, NN_O extends Releasable> ChainProducerBuilder<OUT, CONTEXT> next(ChainProducer<NEXT_O, NN_O, CONTEXT> chainProducer) {
        Preconditions.checkNotNull(chainProducer);
        if (this.mEnableGenericTypeCheck) {
            Type outType = chainProducer.getOutType();
            if (chainProducer.maySkipResultConsume() && outType != chainProducer.getNextOutType()) {
                throwConsumeTypeError(chainProducer.getName());
            }
            Type nextOutType = this.mTailProducer.getNextOutType();
            if (nextOutType != outType) {
                throw new RuntimeException("NEXT_OUT " + nextOutType + " of last producer(" + this.mTailProducer.getClass().getSimpleName() + ") not equal OUT " + outType + " of next producer(" + chainProducer.getClass().getSimpleName() + Operators.BRACKET_END_STR);
            }
        }
        this.mTailProducer = this.mTailProducer.setNextProducer(chainProducer);
        return this;
    }

    public Producer<OUT, CONTEXT> build() {
        return this.mHeadProducer;
    }
}
