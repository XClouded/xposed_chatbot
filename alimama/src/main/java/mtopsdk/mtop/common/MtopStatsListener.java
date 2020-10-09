package mtopsdk.mtop.common;

import java.util.Map;

public interface MtopStatsListener {
    void onStats(Map<String, String> map);
}
