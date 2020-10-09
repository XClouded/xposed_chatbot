package com.taobao.weex.analyzer.core.debug;

public class RouteFactory {
    private static IRoute sRouteImpl;

    private RouteFactory() {
    }

    public static IRoute create() {
        if (sRouteImpl == null) {
            sRouteImpl = new DefaultRouteImpl();
        }
        return sRouteImpl;
    }

    public static void setRouteImpl(IRoute iRoute) {
        sRouteImpl = iRoute;
    }
}
