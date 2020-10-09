package anetwork.channel.cache;

import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anetwork.channel.cache.Cache;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import mtopsdk.common.util.HttpHeaderConstant;

public class CacheHelper {
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final DateFormat STANDARD_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);

    static {
        STANDARD_FORMAT.setTimeZone(GMT);
    }

    public static String toGMTDate(long j) {
        return STANDARD_FORMAT.format(new Date(j));
    }

    private static long parseGMTDate(String str) {
        if (str.length() == 0) {
            return 0;
        }
        try {
            ParsePosition parsePosition = new ParsePosition(0);
            Date parse = STANDARD_FORMAT.parse(str, parsePosition);
            if (parsePosition.getIndex() == str.length()) {
                return parse.getTime();
            }
        } catch (Exception unused) {
        }
        return 0;
    }

    public static Cache.Entry parseCacheHeaders(Map<String, List<String>> map) {
        long j;
        long j2;
        Map<String, List<String>> map2 = map;
        long currentTimeMillis = System.currentTimeMillis();
        String singleHeaderFieldByKey = HttpHelper.getSingleHeaderFieldByKey(map2, HttpConstant.CACHE_CONTROL);
        boolean z = true;
        int i = 0;
        if (singleHeaderFieldByKey != null) {
            String[] split = singleHeaderFieldByKey.split(",");
            j = 0;
            while (true) {
                if (i >= split.length) {
                    break;
                }
                String trim = split[i].trim();
                if (trim.equals("no-store")) {
                    return null;
                }
                if (trim.equals(HttpHeaderConstant.NO_CACHE)) {
                    j = 0;
                    break;
                }
                if (trim.startsWith("max-age=")) {
                    try {
                        j = Long.parseLong(trim.substring(8));
                    } catch (Exception unused) {
                    }
                }
                i++;
            }
        } else {
            j = 0;
            z = false;
        }
        String singleHeaderFieldByKey2 = HttpHelper.getSingleHeaderFieldByKey(map2, HttpHeaderConstant.DATE);
        long parseGMTDate = singleHeaderFieldByKey2 != null ? parseGMTDate(singleHeaderFieldByKey2) : 0;
        String singleHeaderFieldByKey3 = HttpHelper.getSingleHeaderFieldByKey(map2, "Expires");
        long parseGMTDate2 = singleHeaderFieldByKey3 != null ? parseGMTDate(singleHeaderFieldByKey3) : 0;
        String singleHeaderFieldByKey4 = HttpHelper.getSingleHeaderFieldByKey(map2, "Last-Modified");
        long parseGMTDate3 = singleHeaderFieldByKey4 != null ? parseGMTDate(singleHeaderFieldByKey4) : 0;
        String singleHeaderFieldByKey5 = HttpHelper.getSingleHeaderFieldByKey(map2, "ETag");
        if (z) {
            currentTimeMillis += j * 1000;
        } else if (parseGMTDate <= 0 || parseGMTDate2 < parseGMTDate) {
            j2 = parseGMTDate3;
            if (j2 <= 0) {
                currentTimeMillis = 0;
            }
            if (currentTimeMillis != 0 && singleHeaderFieldByKey5 == null) {
                return null;
            }
            Cache.Entry entry = new Cache.Entry();
            entry.etag = singleHeaderFieldByKey5;
            entry.ttl = currentTimeMillis;
            entry.serverDate = parseGMTDate;
            entry.lastModified = j2;
            entry.responseHeaders = map2;
            return entry;
        } else {
            currentTimeMillis += parseGMTDate2 - parseGMTDate;
        }
        j2 = parseGMTDate3;
        if (currentTimeMillis != 0) {
        }
        Cache.Entry entry2 = new Cache.Entry();
        entry2.etag = singleHeaderFieldByKey5;
        entry2.ttl = currentTimeMillis;
        entry2.serverDate = parseGMTDate;
        entry2.lastModified = j2;
        entry2.responseHeaders = map2;
        return entry2;
    }
}
