package com.facebook.imagepipeline.request;

import com.facebook.datasource.DataSource;

public interface DataSourceWithImageRequest<T> extends DataSource<T> {
    ImageRequest getImageRequest();
}
