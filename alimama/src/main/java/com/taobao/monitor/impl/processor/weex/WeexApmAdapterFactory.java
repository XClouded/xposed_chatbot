package com.taobao.monitor.impl.processor.weex;

import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.performance.IApmAdapterFactory;
import com.taobao.monitor.performance.IWXApmAdapter;
import com.taobao.weex.performance.WXInstanceApm;
import java.util.Map;

public class WeexApmAdapterFactory implements IApmAdapterFactory {
    private IWXApmAdapter DEFAULT = new IWXApmAdapter() {
        public void addBiz(String str, Map<String, Object> map) {
        }

        public void addBizAbTest(String str, Map<String, Object> map) {
        }

        public void addBizStage(String str, Map<String, Object> map) {
        }

        public void addProperty(String str, Object obj) {
        }

        public void addStatistic(String str, double d) {
        }

        public void onEnd() {
        }

        public void onEvent(String str, Object obj) {
        }

        public void onStage(String str, long j) {
        }

        public void onStart() {
        }

        public void onStart(String str) {
        }

        public void onStop() {
        }
    };

    public IWXApmAdapter createApmAdapter() {
        return createApmAdapterByType(WXInstanceApm.WEEX_PAGE_TOPIC);
    }

    public IWXApmAdapter createApmAdapterByType(String str) {
        return new WeexProcessorProxy(DynamicConstants.needWeex ? new WeexProcessor(str) : this.DEFAULT);
    }

    private static class WeexProcessorProxy implements IWXApmAdapter {
        /* access modifiers changed from: private */
        public final IWXApmAdapter proxy;

        private WeexProcessorProxy(IWXApmAdapter iWXApmAdapter) {
            this.proxy = iWXApmAdapter;
        }

        public void onStart(final String str) {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.onStart(str);
                }
            });
        }

        public void onEnd() {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.onEnd();
                }
            });
        }

        public void onEvent(final String str, final Object obj) {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.onEvent(str, obj);
                }
            });
        }

        public void onStage(final String str, final long j) {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.onStage(str, j);
                }
            });
        }

        public void addProperty(final String str, final Object obj) {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.addProperty(str, obj);
                }
            });
        }

        public void addStatistic(final String str, final double d) {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.addStatistic(str, d);
                }
            });
        }

        public void addBiz(final String str, final Map<String, Object> map) {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.addBiz(str, map);
                }
            });
        }

        public void addBizAbTest(final String str, final Map<String, Object> map) {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.addBizAbTest(str, map);
                }
            });
        }

        public void addBizStage(final String str, final Map<String, Object> map) {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.addBizStage(str, map);
                }
            });
        }

        public void onStart() {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.onStart();
                }
            });
        }

        public void onStop() {
            async(new Runnable() {
                public void run() {
                    WeexProcessorProxy.this.proxy.onStop();
                }
            });
        }

        private void async(Runnable runnable) {
            Global.instance().handler().post(runnable);
        }
    }
}
