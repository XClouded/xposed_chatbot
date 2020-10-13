package com.sndo.chatbot;

import android.app.Application;

import com.sndo.chatbot.http.DataConfig;

public class App extends Application {
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        
        DataConfig.DEBUG = BuildConfig.DEBUG;
        DataConfig.API_HOST = BuildConfig.API_HOST;
    }
}
