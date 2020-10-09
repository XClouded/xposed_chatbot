package com.ali.user.mobile.rpc;

import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;

public class RpcRequest {
    public String API_NAME;
    public String NAME_SPACE = "com.alibaba.havana";
    public boolean NEED_ECODE = false;
    public boolean NEED_SESSION = false;
    public String VERSION = "1.0";
    public ArrayList<String> paramNames = new ArrayList<>();
    public ArrayList<Object> paramValues = new ArrayList<>();
    public int requestSite;

    public void addParam(String str, Object obj) {
        this.paramNames.add(str);
        this.paramValues.add(obj);
    }

    public String toString() {
        return "RpcRequest [API_NAME=" + this.API_NAME + ", VERSION=" + this.VERSION + ", params=" + Operators.ARRAY_END_STR;
    }
}
