package com.alimama.unionwl.unwcache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class JsonData {
    private static final JSONArray EMPTY_JSON_ARRAY = new JSONArray();
    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();
    private static final String EMPTY_STRING = "";
    private Object mJson;

    public interface JsonConverter<T> {
        T convert(JsonData jsonData);
    }

    public static JsonData newMap() {
        return create((Object) new HashMap());
    }

    public static JsonData newList() {
        return create((Object) new ArrayList());
    }

    public static JsonData create(String str) {
        Object obj;
        if (str != null && str.length() >= 0) {
            try {
                obj = new JSONTokener(str).nextValue();
            } catch (Exception unused) {
            }
            return create(obj);
        }
        obj = null;
        return create(obj);
    }

    public static JsonData create(Object obj) {
        JsonData jsonData = new JsonData();
        if ((obj instanceof JSONArray) || (obj instanceof JSONObject)) {
            jsonData.mJson = obj;
        }
        if (obj instanceof Map) {
            jsonData.mJson = new JSONObject((Map) obj);
        }
        if (obj instanceof Collection) {
            jsonData.mJson = new JSONArray((Collection) obj);
        }
        return jsonData;
    }

    public Object getRawData() {
        return this.mJson;
    }

    public JsonData optJson(String str) {
        return create(this.mJson instanceof JSONObject ? ((JSONObject) this.mJson).opt(str) : null);
    }

    public JsonData optJson(int i) {
        return create(this.mJson instanceof JSONArray ? ((JSONArray) this.mJson).opt(i) : null);
    }

    public String optString(String str) {
        return optMapOrNew().optString(str);
    }

    public String optString(String str, String str2) {
        return optMapOrNew().optString(str, str2);
    }

    public String optString(int i) {
        return optArrayOrNew().optString(i);
    }

    public String optString(int i, String str) {
        return optArrayOrNew().optString(i, str);
    }

    public int optInt(String str) {
        return optMapOrNew().optInt(str);
    }

    public long optLong(String str) {
        return optMapOrNew().optLong(str);
    }

    public int optInt(String str, int i) {
        return optMapOrNew().optInt(str, i);
    }

    public int optInt(int i) {
        return optArrayOrNew().optInt(i);
    }

    public int optInt(int i, int i2) {
        return optArrayOrNew().optInt(i, i2);
    }

    public boolean optBoolean(String str) {
        return optMapOrNew().optBoolean(str);
    }

    public boolean optBoolean(String str, boolean z) {
        return optMapOrNew().optBoolean(str, z);
    }

    public boolean optBoolean(int i) {
        return optArrayOrNew().optBoolean(i);
    }

    public boolean optBoolean(int i, boolean z) {
        return optArrayOrNew().optBoolean(i, z);
    }

    public double optDouble(String str) {
        return optMapOrNew().optDouble(str);
    }

    public double optDouble(String str, double d) {
        return optMapOrNew().optDouble(str, d);
    }

    public double optDouble(int i) {
        return optArrayOrNew().optDouble(i);
    }

    public double optDouble(int i, double d) {
        return optArrayOrNew().optDouble(i, d);
    }

    public boolean has(String str) {
        return optMapOrNew().has(str);
    }

    public boolean has(int i) {
        return optArrayOrNew().length() > i;
    }

    public JSONObject optMapOrNew() {
        if (this.mJson instanceof JSONObject) {
            return (JSONObject) this.mJson;
        }
        return EMPTY_JSON_OBJECT;
    }

    private Object valueForPut(Object obj) {
        return obj instanceof JsonData ? ((JsonData) obj).getRawData() : obj;
    }

    public void put(String str, Object obj) {
        if (this.mJson instanceof JSONObject) {
            try {
                ((JSONObject) this.mJson).put(str, valueForPut(obj));
            } catch (JSONException unused) {
            }
        }
    }

    public void put(Object obj) {
        if (this.mJson instanceof JSONArray) {
            ((JSONArray) this.mJson).put(valueForPut(obj));
        }
    }

    public void put(int i, Object obj) {
        if (this.mJson instanceof JSONArray) {
            try {
                ((JSONArray) this.mJson).put(i, valueForPut(obj));
            } catch (JSONException unused) {
            }
        }
    }

    public JsonData editMap(int i) {
        if (has(i)) {
            return optJson(i);
        }
        JsonData newMap = newMap();
        put(i, (Object) newMap);
        return newMap;
    }

    public JsonData editMap() {
        JsonData newMap = newMap();
        put(newMap);
        return newMap;
    }

    public JsonData editMap(String str) {
        if (has(str)) {
            return optJson(str);
        }
        JsonData newMap = newMap();
        put(str, (Object) newMap);
        return newMap;
    }

    public JsonData editList(String str) {
        if (has(str)) {
            return optJson(str);
        }
        JsonData newList = newList();
        put(str, (Object) newList);
        return newList;
    }

    public JsonData editList(int i) {
        if (has(i)) {
            return optJson(i);
        }
        JsonData newList = newList();
        put(i, (Object) newList);
        return newList;
    }

    public JsonData editList() {
        JsonData newList = newList();
        put(newList);
        return newList;
    }

    public JSONArray optArrayOrNew() {
        if (this.mJson instanceof JSONArray) {
            return (JSONArray) this.mJson;
        }
        return EMPTY_JSON_ARRAY;
    }

    public int length() {
        if (this.mJson instanceof JSONArray) {
            return ((JSONArray) this.mJson).length();
        }
        if (this.mJson instanceof JSONObject) {
            return ((JSONObject) this.mJson).length();
        }
        return 0;
    }

    public Iterator<String> keys() {
        return optMapOrNew().keys();
    }

    public String toString() {
        if (this.mJson instanceof JSONArray) {
            return ((JSONArray) this.mJson).toString();
        }
        return this.mJson instanceof JSONObject ? ((JSONObject) this.mJson).toString() : "";
    }

    public ArrayList<JsonData> toArrayList() {
        ArrayList<JsonData> arrayList = new ArrayList<>();
        if (this.mJson instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray) this.mJson;
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(i, create(jSONArray.opt(i)));
            }
        } else if (this.mJson instanceof JSONObject) {
            JSONObject jSONObject = (JSONObject) this.mJson;
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                arrayList.add(create(jSONObject.opt(keys.next())));
            }
        }
        return arrayList;
    }

    public <T> ArrayList<T> asList(JsonConverter<T> jsonConverter) {
        ArrayList<T> arrayList = new ArrayList<>();
        if (this.mJson instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray) this.mJson;
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jsonConverter.convert(create(jSONArray.opt(i))));
            }
        } else if (this.mJson instanceof JSONObject) {
            JSONObject jSONObject = (JSONObject) this.mJson;
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                arrayList.add(jsonConverter.convert(create(jSONObject.opt(keys.next()))));
            }
        }
        return arrayList;
    }

    public <T> ArrayList<T> asList() {
        ArrayList<T> arrayList = new ArrayList<>();
        if (this.mJson instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray) this.mJson;
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.opt(i));
            }
        } else if (this.mJson instanceof JSONObject) {
            JSONObject jSONObject = (JSONObject) this.mJson;
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                arrayList.add(jSONObject.opt(keys.next()));
            }
        }
        return arrayList;
    }
}
