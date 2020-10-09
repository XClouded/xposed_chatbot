package mtopsdk.mtop.intf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ReqSource {
    public static final int H5 = 1;
    public static final int NATIVE = 0;
    public static final int WEEX = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Definition {
    }
}
