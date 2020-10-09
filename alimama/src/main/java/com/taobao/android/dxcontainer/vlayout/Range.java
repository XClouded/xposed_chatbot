package com.taobao.android.dxcontainer.vlayout;

import android.os.Build;
import androidx.annotation.NonNull;
import java.lang.Comparable;
import java.util.Arrays;
import java.util.Objects;

public final class Range<T extends Comparable<? super T>> {
    private final T mLower;
    private final T mUpper;

    public Range(@NonNull T t, @NonNull T t2) {
        if (t == null) {
            throw new IllegalArgumentException("lower must not be null");
        } else if (t2 != null) {
            this.mLower = t;
            this.mUpper = t2;
            if (t.compareTo(t2) > 0) {
                throw new IllegalArgumentException("lower must be less than or equal to upper");
            }
        } else {
            throw new IllegalArgumentException("upper must not be null");
        }
    }

    public static <T extends Comparable<? super T>> Range<T> create(T t, T t2) {
        return new Range<>(t, t2);
    }

    public T getLower() {
        return this.mLower;
    }

    public T getUpper() {
        return this.mUpper;
    }

    public boolean contains(@NonNull T t) {
        if (t != null) {
            boolean z = t.compareTo(this.mLower) >= 0;
            boolean z2 = t.compareTo(this.mUpper) <= 0;
            if (!z || !z2) {
                return false;
            }
            return true;
        }
        throw new IllegalArgumentException("value must not be null");
    }

    public boolean contains(@NonNull Range<T> range) {
        if (range != null) {
            boolean z = range.mLower.compareTo(this.mLower) >= 0;
            boolean z2 = range.mUpper.compareTo(this.mUpper) <= 0;
            if (!z || !z2) {
                return false;
            }
            return true;
        }
        throw new IllegalArgumentException("value must not be null");
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Range)) {
            return false;
        }
        Range range = (Range) obj;
        if (!this.mLower.equals(range.mLower) || !this.mUpper.equals(range.mUpper)) {
            return false;
        }
        return true;
    }

    public T clamp(T t) {
        if (t == null) {
            throw new IllegalArgumentException("value must not be null");
        } else if (t.compareTo(this.mLower) < 0) {
            return this.mLower;
        } else {
            if (t.compareTo(this.mUpper) > 0) {
                return this.mUpper;
            }
            return t;
        }
    }

    public Range<T> intersect(Range<T> range) {
        T t;
        if (range != null) {
            int compareTo = range.mLower.compareTo(this.mLower);
            int compareTo2 = range.mUpper.compareTo(this.mUpper);
            if (compareTo <= 0 && compareTo2 >= 0) {
                return this;
            }
            if (compareTo >= 0 && compareTo2 <= 0) {
                return range;
            }
            if (compareTo <= 0) {
                t = this.mLower;
            } else {
                t = range.mLower;
            }
            return create(t, compareTo2 >= 0 ? this.mUpper : range.mUpper);
        }
        throw new IllegalArgumentException("range must not be null");
    }

    public Range<T> intersect(T t, T t2) {
        if (t == null) {
            throw new IllegalArgumentException("lower must not be null");
        } else if (t2 != null) {
            int compareTo = t.compareTo(this.mLower);
            int compareTo2 = t2.compareTo(this.mUpper);
            if (compareTo <= 0 && compareTo2 >= 0) {
                return this;
            }
            if (compareTo <= 0) {
                t = this.mLower;
            }
            if (compareTo2 >= 0) {
                t2 = this.mUpper;
            }
            return create(t, t2);
        } else {
            throw new IllegalArgumentException("upper must not be null");
        }
    }

    public Range<T> extend(Range<T> range) {
        T t;
        if (range != null) {
            int compareTo = range.mLower.compareTo(this.mLower);
            int compareTo2 = range.mUpper.compareTo(this.mUpper);
            if (compareTo <= 0 && compareTo2 >= 0) {
                return range;
            }
            if (compareTo >= 0 && compareTo2 <= 0) {
                return this;
            }
            if (compareTo >= 0) {
                t = this.mLower;
            } else {
                t = range.mLower;
            }
            return create(t, compareTo2 <= 0 ? this.mUpper : range.mUpper);
        }
        throw new IllegalArgumentException("range must not be null");
    }

    public Range<T> extend(T t, T t2) {
        if (t == null) {
            throw new IllegalArgumentException("lower must not be null");
        } else if (t2 != null) {
            int compareTo = t.compareTo(this.mLower);
            int compareTo2 = t2.compareTo(this.mUpper);
            if (compareTo >= 0 && compareTo2 <= 0) {
                return this;
            }
            if (compareTo >= 0) {
                t = this.mLower;
            }
            if (compareTo2 <= 0) {
                t2 = this.mUpper;
            }
            return create(t, t2);
        } else {
            throw new IllegalArgumentException("upper must not be null");
        }
    }

    public Range<T> extend(T t) {
        if (t != null) {
            return extend(t, t);
        }
        throw new IllegalArgumentException("value must not be null");
    }

    public String toString() {
        return String.format("[%s, %s]", new Object[]{this.mLower, this.mUpper});
    }

    public int hashCode() {
        if (Build.VERSION.SDK_INT >= 19) {
            return Objects.hash(new Object[]{this.mLower, this.mUpper});
        }
        return Arrays.hashCode(new Object[]{this.mLower, this.mUpper});
    }
}
