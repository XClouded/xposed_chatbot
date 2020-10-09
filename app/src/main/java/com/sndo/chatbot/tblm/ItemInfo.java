package com.sndo.chatbot.tblm;

import org.json.JSONObject;

public class ItemInfo {

    public static int COMMON_TYPE_BASE = 1000;
    public static final int DEFAULT_TYPE = 0;
    public static final int FOOT_TYPE = 9999;
    public static final int MAX_SPAN_SIZE = 20;
    public static final String TAG = "CommonItemInfo";
    private static Class[] baseParam = {String.class, Integer.TYPE, JSONObject.class};
    private static ItemInfo[] sCommonItemInfos = new ItemInfo[0];
//    public CommonBaseItem commonBaseItem;
    public int priority;
    public int spanSize;
    public String type;
    public int viewType;
}
