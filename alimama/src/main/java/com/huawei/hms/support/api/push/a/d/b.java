package com.huawei.hms.support.api.push.a.d;

import android.content.Context;
import com.huawei.hms.support.log.a;
import java.lang.reflect.Field;

/* compiled from: ResourceLoader */
public class b {
    public static int a(Context context, String str, String str2) {
        try {
            int identifier = context.getResources().getIdentifier(str2, str, context.getPackageName());
            if (identifier == 0) {
                Field field = Class.forName(context.getPackageName() + ".R$" + str).getField(str2);
                identifier = Integer.parseInt(field.get(field.getName()).toString());
                if (identifier == 0) {
                    a.b("ResourceLoader", "Error-resourceType=" + str + "--resourceName=" + str2 + "--resourceId =" + identifier);
                }
            }
            return identifier;
        } catch (ClassNotFoundException e) {
            a.a("ResourceLoader", "!!!! ResourceLoader: ClassNotFoundException-resourceType=" + str + "--resourceName=" + str2, (Throwable) e);
            return 0;
        } catch (NoSuchFieldException e2) {
            a.a("ResourceLoader", "!!!! ResourceLoader: NoSuchFieldException-resourceType=" + str + "--resourceName=" + str2, (Throwable) e2);
            return 0;
        } catch (NumberFormatException e3) {
            a.a("ResourceLoader", "!!!! ResourceLoader: NumberFormatException-resourceType=" + str + "--resourceName=" + str2, (Throwable) e3);
            return 0;
        } catch (IllegalAccessException e4) {
            a.a("ResourceLoader", "!!!! ResourceLoader: IllegalAccessException-resourceType=" + str + "--resourceName=" + str2, (Throwable) e4);
            return 0;
        } catch (IllegalArgumentException e5) {
            a.a("ResourceLoader", "!!!! ResourceLoader: IllegalArgumentException-resourceType=" + str + "--resourceName=" + str2, (Throwable) e5);
            return 0;
        }
    }

    public static int a(Context context, String str) {
        return a(context, "layout", str);
    }

    public static int b(Context context, String str) {
        return a(context, "id", str);
    }
}
