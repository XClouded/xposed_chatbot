package com.alibaba.aliweex.adapter.module;

import android.view.Menu;
import com.alibaba.aliweex.AliWXSDKInstance;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.WXError;
import com.alibaba.aliweex.adapter.INavigationBarModuleAdapter;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.utils.WXLogUtils;

public class WXNavigationBarModule extends WXModule {
    public boolean onCreateOptionsMenu(Menu menu) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            navBarAdapter.onCreateOptionsMenu(this.mWXSDKInstance, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @JSMethod
    public void show(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError show = navBarAdapter.show(this.mWXSDKInstance, jSONObject);
            if (show != null) {
                jSCallback = jSCallback2;
            }
            jSCallback.invoke(getResultData(show));
            return;
        }
        notSupported(jSCallback2);
    }

    @JSMethod
    public void hasMenu(Boolean bool, JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("show", (Object) bool);
                WXError hasMenu = navBarAdapter.hasMenu(this.mWXSDKInstance, jSONObject);
                if (hasMenu != null) {
                    jSCallback = jSCallback2;
                }
                if (jSCallback != null) {
                    jSCallback.invoke(getResultData(hasMenu));
                }
            } catch (Throwable unused) {
                if (jSCallback2 != null) {
                    notSupported(jSCallback2);
                }
            }
        } else if (jSCallback2 != null) {
            notSupported(jSCallback2);
        }
    }

    @JSMethod
    public void hide(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError hide = navBarAdapter.hide(this.mWXSDKInstance, jSONObject);
            if (hide != null) {
                jSCallback = jSCallback2;
            }
            jSCallback.invoke(getResultData(hide));
            return;
        }
        notSupported(jSCallback2);
    }

    @JSMethod
    public void setTitle(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError title = navBarAdapter.setTitle(this.mWXSDKInstance, jSONObject);
            if (title != null) {
                jSCallback = jSCallback2;
            }
            if (jSCallback != null) {
                jSCallback.invoke(getResultData(title));
                return;
            }
            return;
        }
        notSupported(jSCallback2);
    }

    @JSMethod
    public void setLeftItem(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        setMenuItem(new WXMenuItem(jSONObject, jSCallback, jSCallback2), true);
    }

    @JSMethod
    public void setRightItem(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        setMenuItem(new WXMenuItem(jSONObject, jSCallback, jSCallback2), false);
    }

    @JSMethod
    public void appendMenu(JSONObject jSONObject, final JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError moreItem = navBarAdapter.setMoreItem(this.mWXSDKInstance, jSONObject, new INavigationBarModuleAdapter.OnItemClickListener() {
                public void onClick(int i) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("index", (Object) Integer.valueOf(i));
                    jSCallback.invokeAndKeepAlive(jSONObject);
                }
            });
            JSONObject resultData = getResultData(moreItem);
            if (moreItem == null) {
                jSCallback.invokeAndKeepAlive(resultData);
            } else {
                jSCallback2.invoke(resultData);
            }
        }
    }

    @JSMethod
    public void setStyle(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError style = navBarAdapter.setStyle(this.mWXSDKInstance, jSONObject);
            if (style != null) {
                jSCallback = jSCallback2;
            }
            jSCallback.invoke(getResultData(style));
            return;
        }
        notSupported(jSCallback2);
    }

    @JSMethod
    public void setBadgeStyle(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError badgeStyle = navBarAdapter.setBadgeStyle(this.mWXSDKInstance, jSONObject);
            if (badgeStyle != null) {
                jSCallback = jSCallback2;
            }
            jSCallback.invoke(getResultData(badgeStyle));
            return;
        }
        notSupported(jSCallback2);
    }

    @JSMethod
    public void showMenu(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError showMenu = navBarAdapter.showMenu(this.mWXSDKInstance, jSONObject);
            if (showMenu != null) {
                jSCallback = jSCallback2;
            }
            jSCallback.invoke(getResultData(showMenu));
            return;
        }
        notSupported(jSCallback2);
    }

    @JSMethod
    public void getStatusBarHeight(JSCallback jSCallback) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError statusBarHeight = navBarAdapter.getStatusBarHeight(this.mWXSDKInstance);
            if (statusBarHeight != null) {
                jSCallback.invoke(statusBarHeight.result);
            } else {
                notSupported(jSCallback);
            }
        } else {
            notSupported(jSCallback);
        }
    }

    @JSMethod
    public void getHeight(JSCallback jSCallback) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            WXError height = navBarAdapter.getHeight(this.mWXSDKInstance);
            if (height != null) {
                jSCallback.invoke(height.result);
            } else {
                notSupported(jSCallback);
            }
        } else {
            notSupported(jSCallback);
        }
    }

    @JSMethod
    public void setTransparent(Boolean bool, JSCallback jSCallback, JSCallback jSCallback2) {
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("transparence", (Object) bool);
                WXError transparent = navBarAdapter.setTransparent(this.mWXSDKInstance, jSONObject);
                if (transparent != null) {
                    jSCallback = jSCallback2;
                }
                if (jSCallback != null) {
                    jSCallback.invoke(getResultData(transparent));
                }
            } catch (Throwable unused) {
                if (jSCallback2 != null) {
                    notSupported(jSCallback2);
                }
            }
        } else if (jSCallback2 != null) {
            notSupported(jSCallback2);
        }
    }

    private INavigationBarModuleAdapter getNavBarAdapter() {
        INavigationBarModuleAdapter navigationBarModuleAdapter = AliWeex.getInstance().getNavigationBarModuleAdapter();
        return (navigationBarModuleAdapter != null || !(this.mWXSDKInstance instanceof AliWXSDKInstance)) ? navigationBarModuleAdapter : ((AliWXSDKInstance) this.mWXSDKInstance).getWXNavBarAdapter();
    }

    private JSONObject getResultData(WXError wXError) {
        JSONObject jSONObject = new JSONObject();
        if (wXError != null) {
            jSONObject.put("message", (Object) wXError.message);
            jSONObject.put("result", (Object) wXError.result);
            if (wXError.options != null) {
                for (String next : wXError.options.keySet()) {
                    jSONObject.put(next, (Object) wXError.options.get(next));
                }
            }
        }
        return jSONObject;
    }

    private void notSupported(JSCallback jSCallback) {
        if (jSCallback == null) {
            WXLogUtils.e("WXNavigationBarModule", "notSupported -> failure callback is null");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("result", (Object) "WX_NOT_SUPPORTED");
        jSCallback.invoke(jSONObject);
    }

    private void setMenuItem(final WXMenuItem wXMenuItem, boolean z) {
        WXError wXError;
        INavigationBarModuleAdapter navBarAdapter = getNavBarAdapter();
        if (navBarAdapter != null) {
            if (wXMenuItem != null) {
                if (z) {
                    wXError = navBarAdapter.setLeftItem(this.mWXSDKInstance, wXMenuItem.options, new INavigationBarModuleAdapter.OnItemClickListener() {
                        public void onClick(int i) {
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.put("index", (Object) Integer.valueOf(i));
                            wXMenuItem.success.invokeAndKeepAlive(jSONObject);
                        }
                    });
                } else {
                    wXError = navBarAdapter.setRightItem(this.mWXSDKInstance, wXMenuItem.options, new INavigationBarModuleAdapter.OnItemClickListener() {
                        public void onClick(int i) {
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.put("index", (Object) Integer.valueOf(i));
                            wXMenuItem.success.invokeAndKeepAlive(jSONObject);
                        }
                    });
                }
                JSONObject resultData = getResultData(wXError);
                if (wXError == null) {
                    wXMenuItem.success.invokeAndKeepAlive(resultData);
                } else {
                    wXMenuItem.failure.invoke(resultData);
                }
            }
        } else if (wXMenuItem != null) {
            notSupported(wXMenuItem.failure);
        }
    }

    private static class WXMenuItem {
        JSCallback failure;
        JSONObject options;
        JSCallback success;

        public WXMenuItem(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
            this.options = jSONObject;
            this.success = jSCallback;
            this.failure = jSCallback2;
        }
    }
}
