package alimama.com.unwviewbase.pullandrefrsh;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface Orientation {
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 0;
}
