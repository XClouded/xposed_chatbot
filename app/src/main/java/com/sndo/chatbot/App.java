package com.sndo.chatbot;

import android.app.Application;

import com.sndo.chatbot.http.DataConfig;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        DataConfig.DEBUG = BuildConfig.DEBUG;
        DataConfig.API_HOST= BuildConfig.API_HOST;
    }
}
