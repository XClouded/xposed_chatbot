package com.alimama.union.app.pagerouter;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IABTest;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.interfaces.IOrange;
import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unwbase.interfaces.ISharedPreference;
import alimama.com.unwbase.interfaces.ITlog;
import alimama.com.unwbase.interfaces.IUTAction;
import alimama.com.unwbaseimpl.EtaoABTest;
import alimama.com.unwbaseimpl.UNWOrange;
import alimama.com.unwbaseimpl.UNWTlog;
import alimama.com.unwbaseimpl.UWNUTImpl;
import androidx.annotation.NonNull;
import com.alimama.moon.init.InitConstants;

public class MoonComponentManager {
    public static final String APPBEGINRUN = "appbeginrun";
    private static MoonComponentManager sInstance = new MoonComponentManager();

    public static MoonComponentManager getInstance() {
        return sInstance;
    }

    @NonNull
    public IUTAction getUt() {
        if (UNWManager.getInstance().getService(IUTAction.class) == null) {
            UNWManager.getInstance().registerService(IUTAction.class, new UWNUTImpl());
        }
        return (IUTAction) UNWManager.getInstance().getService(IUTAction.class);
    }

    @NonNull
    public IEtaoLogger getEtaoLogger() {
        return UNWManager.getInstance().getLogger();
    }

    @NonNull
    public IOrange getOrange() {
        if (UNWManager.getInstance().getService(IOrange.class) == null) {
            UNWManager.getInstance().registerService(IOrange.class, new UNWOrange());
        }
        return (IOrange) UNWManager.getInstance().getService(IOrange.class);
    }

    @NonNull
    public IABTest getAB() {
        if (UNWManager.getInstance().getService(IABTest.class) == null) {
            UNWManager.getInstance().registerService(IABTest.class, new EtaoABTest());
        }
        return (IABTest) UNWManager.getInstance().getService(IABTest.class);
    }

    @NonNull
    public ITlog getTlog() {
        if (UNWManager.getInstance().getService(ITlog.class) == null) {
            UNWManager.getInstance().registerService(ITlog.class, new UNWTlog("moon", InitConstants.Tlog.rsaPublishKey));
        }
        return (ITlog) UNWManager.getInstance().getService(ITlog.class);
    }

    @NonNull
    public ISharedPreference getSharePreference() {
        return (ISharedPreference) UNWManager.getInstance().getService(ISharedPreference.class);
    }

    @NonNull
    public IAppEnvironment getEnvironment() {
        return (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
    }

    @NonNull
    public IRouter getPageRouter() {
        if (UNWManager.getInstance().getService(IRouter.class) != null) {
            return (IRouter) UNWManager.getInstance().getService(IRouter.class);
        }
        return new EmptyRouter();
    }
}
