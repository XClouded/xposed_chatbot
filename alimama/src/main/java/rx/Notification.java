package rx;

import com.taobao.weex.el.parse.Operators;

public final class Notification<T> {
    private static final Notification<Void> ON_COMPLETED = new Notification<>(Kind.OnCompleted, (Object) null, (Throwable) null);
    private final Kind kind;
    private final Throwable throwable;
    private final T value;

    public enum Kind {
        OnNext,
        OnError,
        OnCompleted
    }

    public static <T> Notification<T> createOnNext(T t) {
        return new Notification<>(Kind.OnNext, t, (Throwable) null);
    }

    public static <T> Notification<T> createOnError(Throwable th) {
        return new Notification<>(Kind.OnError, (Object) null, th);
    }

    public static <T> Notification<T> createOnCompleted() {
        return ON_COMPLETED;
    }

    public static <T> Notification<T> createOnCompleted(Class<T> cls) {
        return ON_COMPLETED;
    }

    private Notification(Kind kind2, T t, Throwable th) {
        this.value = t;
        this.throwable = th;
        this.kind = kind2;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public T getValue() {
        return this.value;
    }

    public boolean hasValue() {
        return isOnNext() && this.value != null;
    }

    public boolean hasThrowable() {
        return isOnError() && this.throwable != null;
    }

    public Kind getKind() {
        return this.kind;
    }

    public boolean isOnError() {
        return getKind() == Kind.OnError;
    }

    public boolean isOnCompleted() {
        return getKind() == Kind.OnCompleted;
    }

    public boolean isOnNext() {
        return getKind() == Kind.OnNext;
    }

    public void accept(Observer<? super T> observer) {
        if (isOnNext()) {
            observer.onNext(getValue());
        } else if (isOnCompleted()) {
            observer.onCompleted();
        } else if (isOnError()) {
            observer.onError(getThrowable());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(Operators.ARRAY_START_STR);
        sb.append(super.toString());
        sb.append(Operators.SPACE_STR);
        sb.append(getKind());
        if (hasValue()) {
            sb.append(Operators.SPACE_STR);
            sb.append(getValue());
        }
        if (hasThrowable()) {
            sb.append(Operators.SPACE_STR);
            sb.append(getThrowable().getMessage());
        }
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    public int hashCode() {
        int hashCode = getKind().hashCode();
        if (hasValue()) {
            hashCode = (hashCode * 31) + getValue().hashCode();
        }
        return hasThrowable() ? (hashCode * 31) + getThrowable().hashCode() : hashCode;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Notification notification = (Notification) obj;
        if (notification.getKind() != getKind()) {
            return false;
        }
        if (!hasValue() || getValue().equals(notification.getValue())) {
            return !hasThrowable() || getThrowable().equals(notification.getThrowable());
        }
        return false;
    }
}
