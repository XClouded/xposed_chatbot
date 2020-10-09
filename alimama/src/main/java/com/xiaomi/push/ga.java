package com.xiaomi.push;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ga implements ge {
    private String a;

    /* renamed from: a  reason: collision with other field name */
    private List<ga> f396a = null;

    /* renamed from: a  reason: collision with other field name */
    private String[] f397a = null;
    private String b;

    /* renamed from: b  reason: collision with other field name */
    private String[] f398b = null;
    private String c;

    public ga(String str, String str2, String[] strArr, String[] strArr2) {
        this.a = str;
        this.b = str2;
        this.f397a = strArr;
        this.f398b = strArr2;
    }

    public ga(String str, String str2, String[] strArr, String[] strArr2, String str3, List<ga> list) {
        this.a = str;
        this.b = str2;
        this.f397a = strArr;
        this.f398b = strArr2;
        this.c = str3;
        this.f396a = list;
    }

    public static ga a(Bundle bundle) {
        ArrayList arrayList;
        String string = bundle.getString("ext_ele_name");
        String string2 = bundle.getString("ext_ns");
        String string3 = bundle.getString("ext_text");
        Bundle bundle2 = bundle.getBundle("attributes");
        Set<String> keySet = bundle2.keySet();
        String[] strArr = new String[keySet.size()];
        String[] strArr2 = new String[keySet.size()];
        int i = 0;
        for (String str : keySet) {
            strArr[i] = str;
            strArr2[i] = bundle2.getString(str);
            i++;
        }
        if (bundle.containsKey("children")) {
            Parcelable[] parcelableArray = bundle.getParcelableArray("children");
            ArrayList arrayList2 = new ArrayList(parcelableArray.length);
            for (Parcelable parcelable : parcelableArray) {
                arrayList2.add(a((Bundle) parcelable));
            }
            arrayList = arrayList2;
        } else {
            arrayList = null;
        }
        return new ga(string, string2, strArr, strArr2, string3, arrayList);
    }

    public static Parcelable[] a(List<ga> list) {
        return a((ga[]) list.toArray(new ga[list.size()]));
    }

    public static Parcelable[] a(ga[] gaVarArr) {
        if (gaVarArr == null) {
            return null;
        }
        Parcelable[] parcelableArr = new Parcelable[gaVarArr.length];
        for (int i = 0; i < gaVarArr.length; i++) {
            parcelableArr[i] = gaVarArr[i].a();
        }
        return parcelableArr;
    }

    public Bundle a() {
        Bundle bundle = new Bundle();
        bundle.putString("ext_ele_name", this.a);
        bundle.putString("ext_ns", this.b);
        bundle.putString("ext_text", this.c);
        Bundle bundle2 = new Bundle();
        if (this.f397a != null && this.f397a.length > 0) {
            for (int i = 0; i < this.f397a.length; i++) {
                bundle2.putString(this.f397a[i], this.f398b[i]);
            }
        }
        bundle.putBundle("attributes", bundle2);
        if (this.f396a != null && this.f396a.size() > 0) {
            bundle.putParcelableArray("children", a(this.f396a));
        }
        return bundle;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Parcelable m327a() {
        return a();
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m328a() {
        return this.a;
    }

    public String a(String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        } else if (this.f397a == null) {
            return null;
        } else {
            for (int i = 0; i < this.f397a.length; i++) {
                if (str.equals(this.f397a[i])) {
                    return this.f398b[i];
                }
            }
            return null;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m329a(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = go.a(str);
        }
        this.c = str;
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return !TextUtils.isEmpty(this.c) ? go.b(this.c) : this.c;
    }

    public String d() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.L);
        sb.append(this.a);
        if (!TextUtils.isEmpty(this.b)) {
            sb.append(Operators.SPACE_STR);
            sb.append("xmlns=");
            sb.append("\"");
            sb.append(this.b);
            sb.append("\"");
        }
        if (this.f397a != null && this.f397a.length > 0) {
            for (int i = 0; i < this.f397a.length; i++) {
                if (!TextUtils.isEmpty(this.f398b[i])) {
                    sb.append(Operators.SPACE_STR);
                    sb.append(this.f397a[i]);
                    sb.append("=\"");
                    sb.append(go.a(this.f398b[i]));
                    sb.append("\"");
                }
            }
        }
        if (!TextUtils.isEmpty(this.c)) {
            sb.append(Operators.G);
            sb.append(this.c);
        } else if (this.f396a == null || this.f396a.size() <= 0) {
            str = "/>";
            sb.append(str);
            return sb.toString();
        } else {
            sb.append(Operators.G);
            for (ga d : this.f396a) {
                sb.append(d.d());
            }
        }
        sb.append("</");
        sb.append(this.a);
        str = Operators.G;
        sb.append(str);
        return sb.toString();
    }

    public String toString() {
        return d();
    }
}
