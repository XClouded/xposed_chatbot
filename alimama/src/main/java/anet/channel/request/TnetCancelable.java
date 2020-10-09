package anet.channel.request;

import anet.channel.util.ALog;
import org.android.spdy.SpdyErrorException;
import org.android.spdy.SpdySession;

public class TnetCancelable implements Cancelable {
    public static final TnetCancelable NULL = new TnetCancelable((SpdySession) null, 0, (String) null);
    private final String seq;
    private final SpdySession spdySession;
    private final int streamId;

    public TnetCancelable(SpdySession spdySession2, int i, String str) {
        this.spdySession = spdySession2;
        this.streamId = i;
        this.seq = str;
    }

    public void cancel() {
        try {
            if (this.spdySession != null && this.streamId != 0) {
                ALog.i("awcn.TnetCancelable", "cancel tnet request", this.seq, "streamId", Integer.valueOf(this.streamId));
                this.spdySession.streamReset((long) this.streamId, 5);
            }
        } catch (SpdyErrorException e) {
            ALog.e("awcn.TnetCancelable", "request cancel failed.", this.seq, e, "errorCode", Integer.valueOf(e.SpdyErrorGetCode()));
        }
    }
}
