package com.ali.telescope.internal.plugins.anr;

import android.app.Application;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.internal.plugins.anr.sharedpreferences.SPHook;
import org.json.JSONObject;

public class ANRPlugin extends Plugin {
    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        super.onCreate(application, iTelescopeContext, jSONObject);
        new SPHook().startHook(application);
    }
}
