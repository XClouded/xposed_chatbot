package com.taobao.android.ultron.datamodel.cache.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Entry {
    public static final String[] ID_PROJECTION = {"_id"};
    @Column("_id")
    public long id = 0;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Column {
        String defaultValue() default "";

        boolean fullText() default false;

        boolean indexed() default false;

        String value();
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Table {
        String value();
    }

    public interface Columns {
        public static final String ID = "_id";
    }

    public void clear() {
        this.id = 0;
    }
}
