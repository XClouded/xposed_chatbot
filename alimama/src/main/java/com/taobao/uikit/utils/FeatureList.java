package com.taobao.uikit.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.uikit.base.R;
import com.taobao.uikit.feature.features.AbsFeature;
import com.taobao.uikit.feature.features.FeatureFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FeatureList<T extends View> extends ArrayList<AbsFeature<? super T>> implements Comparator<AbsFeature<? super T>>, IFeatureList<T> {
    private static final long serialVersionUID = 5539018560951385305L;
    private T mHost;

    public FeatureList(T t) {
        this.mHost = t;
    }

    public boolean add(AbsFeature<? super T> absFeature) {
        int size = size();
        int i = 0;
        while (i < size) {
            AbsFeature absFeature2 = (AbsFeature) get(i);
            if (!TextUtils.equals(absFeature2.getClass().getName(), absFeature.getClass().getName())) {
                i++;
            } else {
                throw new RuntimeException(absFeature2.getClass().getName() + " already add to this view");
            }
        }
        boolean add = super.add(absFeature);
        Collections.sort(this, this);
        return add;
    }

    public int compare(AbsFeature<? super T> absFeature, AbsFeature<? super T> absFeature2) {
        return FeatureFactory.getFeaturePriority(absFeature.getClass().getName()) - FeatureFactory.getFeaturePriority(absFeature2.getClass().getName());
    }

    public void init(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FeatureNameSpace);
        if (obtainStyledAttributes != null) {
            ArrayList creator = FeatureFactory.creator(this.mHost.getContext(), obtainStyledAttributes);
            int size = creator.size();
            for (int i2 = 0; i2 < size; i2++) {
                AbsFeature absFeature = (AbsFeature) creator.get(i2);
                addFeature(absFeature);
                absFeature.constructor(context, attributeSet, i);
            }
            obtainStyledAttributes.recycle();
        }
    }

    public boolean addFeature(AbsFeature<? super T> absFeature) {
        if (absFeature == null) {
            return false;
        }
        absFeature.setHost(this.mHost);
        return add(absFeature);
    }

    public AbsFeature<? super T> findFeature(Class<? extends AbsFeature<? super T>> cls) {
        int size = size();
        for (int i = 0; i < size; i++) {
            AbsFeature<? super T> absFeature = (AbsFeature) get(i);
            if (absFeature.getClass() == cls) {
                return absFeature;
            }
        }
        return null;
    }

    public boolean removeFeature(Class<? extends AbsFeature<? super T>> cls) {
        int size = size();
        for (int i = 0; i < size; i++) {
            AbsFeature absFeature = (AbsFeature) get(i);
            if (absFeature.getClass() == cls) {
                return remove(absFeature);
            }
        }
        return false;
    }

    public void clearFeatures() {
        clear();
        this.mHost.requestLayout();
    }
}
