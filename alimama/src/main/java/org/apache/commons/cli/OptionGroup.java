package org.apache.commons.cli;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OptionGroup implements Serializable {
    private static final long serialVersionUID = 1;
    private Map optionMap = new HashMap();
    private boolean required;
    private String selected;

    public OptionGroup addOption(Option option) {
        this.optionMap.put(option.getKey(), option);
        return this;
    }

    public Collection getNames() {
        return this.optionMap.keySet();
    }

    public Collection getOptions() {
        return this.optionMap.values();
    }

    public void setSelected(Option option) throws AlreadySelectedException {
        if (this.selected == null || this.selected.equals(option.getOpt())) {
            this.selected = option.getOpt();
            return;
        }
        throw new AlreadySelectedException(this, option);
    }

    public String getSelected() {
        return this.selected;
    }

    public void setRequired(boolean z) {
        this.required = z;
    }

    public boolean isRequired() {
        return this.required;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = getOptions().iterator();
        stringBuffer.append(Operators.ARRAY_START_STR);
        while (it.hasNext()) {
            Option option = (Option) it.next();
            if (option.getOpt() != null) {
                stringBuffer.append("-");
                stringBuffer.append(option.getOpt());
            } else {
                stringBuffer.append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
                stringBuffer.append(option.getLongOpt());
            }
            stringBuffer.append(Operators.SPACE_STR);
            stringBuffer.append(option.getDescription());
            if (it.hasNext()) {
                stringBuffer.append(AVFSCacheConstants.COMMA_SEP);
            }
        }
        stringBuffer.append(Operators.ARRAY_END_STR);
        return stringBuffer.toString();
    }
}
