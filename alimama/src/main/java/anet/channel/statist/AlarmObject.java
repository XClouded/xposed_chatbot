package anet.channel.statist;

import com.taobao.weex.el.parse.Operators;

public class AlarmObject {
    public String arg;
    public String errorCode;
    public String errorMsg;
    public boolean isSuccess;
    public String module;
    public String modulePoint;

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("[module:");
        sb.append(this.module);
        sb.append(" modulePoint:");
        sb.append(this.modulePoint);
        sb.append(" arg:");
        sb.append(this.arg);
        sb.append(" isSuccess:");
        sb.append(this.isSuccess);
        sb.append(" errorCode:");
        sb.append(this.errorCode);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }
}
