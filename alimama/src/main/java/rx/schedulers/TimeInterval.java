package rx.schedulers;

import com.taobao.weex.el.parse.Operators;

public class TimeInterval<T> {
    private final long intervalInMilliseconds;
    private final T value;

    public TimeInterval(long j, T t) {
        this.value = t;
        this.intervalInMilliseconds = j;
    }

    public long getIntervalInMilliseconds() {
        return this.intervalInMilliseconds;
    }

    public T getValue() {
        return this.value;
    }

    public int hashCode() {
        return ((((int) (this.intervalInMilliseconds ^ (this.intervalInMilliseconds >>> 32))) + 31) * 31) + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TimeInterval timeInterval = (TimeInterval) obj;
        if (this.intervalInMilliseconds != timeInterval.intervalInMilliseconds) {
            return false;
        }
        if (this.value == null) {
            if (timeInterval.value != null) {
                return false;
            }
        } else if (!this.value.equals(timeInterval.value)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "TimeInterval [intervalInMilliseconds=" + this.intervalInMilliseconds + ", value=" + this.value + Operators.ARRAY_END_STR;
    }
}
