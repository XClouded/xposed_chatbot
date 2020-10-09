package com.alimama.moon.di.module;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unwrouter.UNWRouter;
import alimama.com.unwrouter.interfaces.PageNeedLoginAction;
import android.content.Context;
import androidx.room.Room;
import com.alimama.moon.AppDatabase;
import com.alimama.moon.eventbus.DefaultEventBusImpl;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.ui.BottomNavActivity;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.aalogin.TaobaoLogin;
import com.alimama.union.app.contact.model.ContactService;
import com.alimama.union.app.contact.model.ContactServiceImpl;
import com.alimama.union.app.infrastructure.executor.AsyncTaskManager;
import com.alimama.union.app.infrastructure.image.capture.BitmapCapture;
import com.alimama.union.app.infrastructure.image.capture.Capture;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.infrastructure.image.save.IImageSaver;
import com.alimama.union.app.infrastructure.permission.CommonPermission;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.network.IWebService;
import com.alimama.union.app.network.MockMtopService;
import com.alimama.union.app.network.MtopService;
import com.alimama.union.app.pagerouter.MoonJumpIterceptor;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context2) {
        this.context = context2;
    }

    @Singleton
    @Provides
    @Named("appContext")
    public Context provideContext() {
        return this.context;
    }

    @Singleton
    @Provides
    public AppDatabase provideAppDatabase() {
        return Room.databaseBuilder(this.context, AppDatabase.class, "union-app").fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    public Executor providerExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Singleton
    @Provides
    public ILogin provideLogin(@Named("appContext") Context context2, @Named("mtop_service") IWebService iWebService, IEventBus iEventBus) {
        return new TaobaoLogin(context2, iWebService, iEventBus);
    }

    @Singleton
    @Provides
    @Named("mtop_service")
    public IWebService provideMtopService(@Named("appContext") Context context2) {
        return new MtopService(context2);
    }

    @Singleton
    @Provides
    @Named("mock_service")
    public IWebService provideMockService() {
        return new MockMtopService();
    }

    @Singleton
    @Provides
    public IEventBus provideEventBus() {
        return new DefaultEventBusImpl();
    }

    @Singleton
    @Provides
    public ContactService provideContactService() {
        return new ContactServiceImpl();
    }

    @Singleton
    @Provides
    @Named("READ_CONTACTS")
    public Permission provideReadContactPermission() {
        return new CommonPermission("android.permission.READ_CONTACTS", 1);
    }

    @Singleton
    @Provides
    @Named("WRITE_EXTERNAL_STORAGE")
    public Permission provideWriteExternalStoragePermission() {
        return new CommonPermission("android.permission.WRITE_EXTERNAL_STORAGE", 2);
    }

    @Singleton
    @Provides
    @Named("CAMERA")
    public Permission provideCameraAPermission() {
        return new CommonPermission("android.permission.CAMERA", 3);
    }

    @Singleton
    @Provides
    public Capture provideCapture(IImageSaver iImageSaver) {
        return new BitmapCapture(iImageSaver);
    }

    @Singleton
    @Provides
    public IImageSaver provideImageSaver() {
        return ExternalPublicStorageSaver.getInstance();
    }

    @Singleton
    @Provides
    public UNWRouter providePageRouter() {
        UNWRouter uNWRouter = new UNWRouter((PageNeedLoginAction) null, "unionApp");
        uNWRouter.setHomeActivity(BottomNavActivity.class);
        uNWRouter.setInterceptor(new MoonJumpIterceptor());
        UNWManager.getInstance().registerService(IRouter.class, uNWRouter);
        return uNWRouter;
    }

    @Singleton
    @Provides
    public AsyncTaskManager provideTaskManager() {
        return AsyncTaskManager.getInstance();
    }
}
