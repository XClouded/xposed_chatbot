package com.alimama.union.app.toolCenter.data;

import java.util.List;

public class ToolItemBean {
    public String groupName;
    public List<ChildItem> items;
    public String title;

    public static class ChildItem {
        public String img;
        public String title;
        public String url;
    }
}
