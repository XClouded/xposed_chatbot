package com.taobao.weex.devtools.toolbox;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXModuleManager;
import com.taobao.weex.bridge.WXParams;
import com.taobao.weex.common.TypeModuleFactory;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.devtools.debug.WXDebugConstants;
import com.taobao.weex.inspector.R;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.WXComponentRegistry;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EnvironmentFragment extends Fragment {
    ArrayAdapter arrayAdapter;
    TextView info;
    List<String> items = new ArrayList();
    ListView list;
    Spinner spinner;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_environment, viewGroup, false);
        this.spinner = (Spinner) inflate.findViewById(R.id.spinner);
        this.spinner.setAdapter(new ArrayAdapter(getContext(), 17367043, new String[]{"Modules", "Components", "Environment"}));
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                switch (i) {
                    case 0:
                        EnvironmentFragment.this.showModules();
                        return;
                    case 1:
                        EnvironmentFragment.this.showComponents();
                        return;
                    case 2:
                        EnvironmentFragment.this.showEnvironment();
                        return;
                    default:
                        return;
                }
            }
        });
        this.list = (ListView) inflate.findViewById(R.id.list);
        this.info = (TextView) inflate.findViewById(R.id.info);
        this.info.setTypeface(Typeface.MONOSPACE);
        this.arrayAdapter = new ArrayAdapter(getContext(), 17367043, this.items);
        this.list.setAdapter(this.arrayAdapter);
        showModules();
        return inflate;
    }

    /* access modifiers changed from: private */
    public void showModules() {
        final HashMap hashMap = new HashMap();
        for (Map.Entry next : EnvironmentUtil.getExistedModules(true).entrySet()) {
            hashMap.put(((String) next.getKey()) + "  [global]", next.getValue());
        }
        hashMap.putAll(EnvironmentUtil.getExistedModules(false));
        this.list.setVisibility(0);
        this.info.setText("");
        this.items.clear();
        this.items.addAll(hashMap.keySet());
        ((ArrayAdapter) this.list.getAdapter()).notifyDataSetChanged();
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                TypeModuleFactory typeModuleFactory = (TypeModuleFactory) hashMap.get(EnvironmentFragment.this.items.get(i));
                if (typeModuleFactory != null) {
                    try {
                        EnvironmentFragment.this.info.setText(new JSONArray(Arrays.asList(typeModuleFactory.getMethods())).toString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showComponents() {
        final Map<String, IFComponentHolder> existedComponents = EnvironmentUtil.getExistedComponents();
        this.list.setVisibility(0);
        this.info.setText("");
        this.items.clear();
        this.items.addAll(existedComponents.keySet());
        ((ArrayAdapter) this.list.getAdapter()).notifyDataSetChanged();
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                IFComponentHolder iFComponentHolder = (IFComponentHolder) existedComponents.get(EnvironmentFragment.this.items.get(i));
                if (iFComponentHolder != null) {
                    try {
                        EnvironmentFragment.this.info.setText(new JSONArray(Arrays.asList(iFComponentHolder.getMethods())).toString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showEnvironment() {
        this.list.setVisibility(8);
        WXParams initParams = WXBridgeManager.getInstance().getInitParams();
        Map<String, String> config = WXEnvironment.getConfig();
        config.put("platform", initParams.getPlatform());
        config.put(WXDebugConstants.ENV_OS_VERSION, initParams.getOsVersion());
        config.put("appVersion", initParams.getAppVersion());
        config.put("weexVersion", initParams.getWeexVersion());
        config.put(WXDebugConstants.ENV_DEVICE_MODEL, initParams.getDeviceModel());
        config.put("appName", initParams.getAppName());
        config.put("deviceWidth", initParams.getDeviceWidth());
        config.put("deviceHeight", initParams.getDeviceHeight());
        config.put("shouldInfoCollect", initParams.getShouldInfoCollect());
        config.putAll(WXEnvironment.getCustomOptions());
        try {
            this.info.setText(new JSONObject(config).toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class EnvironmentUtil {
        private EnvironmentUtil() {
        }

        public static Map<String, TypeModuleFactory> getExistedModules(boolean z) {
            HashMap hashMap = new HashMap();
            if (z) {
                try {
                    Field declaredField = WXModuleManager.class.getDeclaredField("sGlobalModuleMap");
                    if (declaredField != null) {
                        declaredField.setAccessible(true);
                        Map map = (Map) declaredField.get((Object) null);
                        for (Object next : map.keySet()) {
                            Object obj = map.get(next);
                            if (obj instanceof WXModule) {
                                hashMap.put((String) next, new TypeModuleFactory(obj.getClass()));
                            }
                        }
                    }
                } catch (Throwable unused) {
                }
            } else {
                Field declaredField2 = WXModuleManager.class.getDeclaredField("sModuleFactoryMap");
                if (declaredField2 != null) {
                    declaredField2.setAccessible(true);
                    return (Map) declaredField2.get((Object) null);
                }
            }
            return hashMap;
        }

        public static Map<String, IFComponentHolder> getExistedComponents() {
            try {
                Field declaredField = WXComponentRegistry.class.getDeclaredField("sTypeComponentMap");
                declaredField.setAccessible(true);
                return (Map) declaredField.get((Object) null);
            } catch (Throwable th) {
                th.printStackTrace();
                return new HashMap();
            }
        }
    }
}
