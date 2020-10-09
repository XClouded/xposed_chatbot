package anet.channel.util;

import anet.channel.request.Request;
import anet.channel.thread.ThreadPoolExecutorFactory;
import com.alibaba.android.update.UpdateUtils;
import java.util.HashMap;
import java.util.Map;

public class RequestPriorityTable {
    private static Map<String, Integer> extPriorityMap = new HashMap();

    static {
        extPriorityMap.put("tpatch", 3);
        extPriorityMap.put("so", 3);
        extPriorityMap.put("json", 3);
        extPriorityMap.put("html", 4);
        extPriorityMap.put("htm", 4);
        extPriorityMap.put("css", 5);
        extPriorityMap.put("js", 5);
        extPriorityMap.put("webp", 6);
        extPriorityMap.put("png", 6);
        extPriorityMap.put("jpg", 6);
        extPriorityMap.put("do", 6);
        extPriorityMap.put("zip", Integer.valueOf(ThreadPoolExecutorFactory.Priority.LOW));
        extPriorityMap.put("bin", Integer.valueOf(ThreadPoolExecutorFactory.Priority.LOW));
        extPriorityMap.put(UpdateUtils.SUFFIX_APK, Integer.valueOf(ThreadPoolExecutorFactory.Priority.LOW));
    }

    public static int lookup(Request request) {
        Integer num;
        if (request == null) {
            throw new NullPointerException("url is null!");
        } else if (request.getHeaders().containsKey("x-pv")) {
            return 1;
        } else {
            String trySolveFileExtFromUrlPath = HttpHelper.trySolveFileExtFromUrlPath(request.getHttpUrl().path());
            if (trySolveFileExtFromUrlPath == null || (num = extPriorityMap.get(trySolveFileExtFromUrlPath)) == null) {
                return 6;
            }
            return num.intValue();
        }
    }
}
