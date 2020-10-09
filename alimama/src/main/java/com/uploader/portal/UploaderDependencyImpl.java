package com.uploader.portal;

import android.content.Context;
import androidx.annotation.NonNull;
import com.uploader.export.IUploaderDependency;
import com.uploader.export.IUploaderEnvironment;
import com.uploader.export.IUploaderLog;
import com.uploader.export.IUploaderStatistics;
import com.uploader.export.UploaderGlobal;

public class UploaderDependencyImpl implements IUploaderDependency {
    static Context context;
    private IUploaderEnvironment environment;
    private IUploaderLog log;
    private IUploaderStatistics statistics;

    public UploaderDependencyImpl() {
        this((Context) null, new UploaderEnvironmentImpl2(UploaderGlobal.retrieveContext()), new UploaderLogImpl(), new UploaderStatisticsImpl());
    }

    public UploaderDependencyImpl(Context context2) {
        this(context2, new UploaderEnvironmentImpl2(context2), new UploaderLogImpl(), new UploaderStatisticsImpl());
    }

    public UploaderDependencyImpl(Context context2, IUploaderEnvironment iUploaderEnvironment) {
        this(context2, iUploaderEnvironment, new UploaderLogImpl(), new UploaderStatisticsImpl());
    }

    public UploaderDependencyImpl(Context context2, IUploaderEnvironment iUploaderEnvironment, IUploaderLog iUploaderLog, IUploaderStatistics iUploaderStatistics) {
        if (context2 == null) {
            context = UploaderGlobal.retrieveContext();
        } else {
            context = context2;
        }
        this.environment = iUploaderEnvironment;
        this.log = iUploaderLog;
        this.statistics = iUploaderStatistics;
    }

    public IUploaderLog getLog() {
        return this.log;
    }

    public IUploaderStatistics getStatistics() {
        return this.statistics;
    }

    @NonNull
    public IUploaderEnvironment getEnvironment() {
        return this.environment;
    }
}
