package mtopsdk.mtop.common;

import com.taobao.weex.el.parse.Operators;
import mtopsdk.mtop.domain.MtopResponse;

public class MtopCacheEvent extends MtopFinishEvent {
    public MtopCacheEvent(MtopResponse mtopResponse) {
        super(mtopResponse);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("MtopCacheEvent [seqNo=");
        sb.append(this.seqNo);
        sb.append(", mtopResponse=");
        sb.append(this.mtopResponse);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }
}
