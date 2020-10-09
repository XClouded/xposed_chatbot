package alimama.com.unwetaologger.base;

import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.List;

public class UNWLogTracer {
    private int step = 0;
    private List<TraceItem> stepList = new ArrayList();

    public void append(String str, String str2) {
        this.stepList.add(new TraceItem(this.step, str, str2));
        this.step++;
    }

    public List<TraceItem> getStepList() {
        return this.stepList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        for (int i = 0; i < this.stepList.size(); i++) {
            sb.append(this.stepList.get(i).toString());
            sb.append(",");
        }
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    public static class TraceItem {
        public String condition;
        public String msg;
        public int step;

        public TraceItem(int i, String str, String str2) {
            this.step = i;
            this.condition = str;
            this.msg = str2;
        }

        public String toString() {
            return "{\"step\":\"" + this.step + "\"," + "\"condition\":" + "\"" + this.condition + "\"," + "\"msg\":" + "\"" + this.msg + "\"}";
        }
    }
}
