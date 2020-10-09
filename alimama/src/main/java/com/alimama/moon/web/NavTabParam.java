package com.alimama.moon.web;

import androidx.annotation.Nullable;

public class NavTabParam {
    public int defaultSelect;
    @Nullable
    public Tab[] items;

    public static class Tab {
        public String title;
        public String url;
    }
}
