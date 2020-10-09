package org.apache.commons.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PosixParser extends Parser {
    private Option currentOption;
    private boolean eatTheRest;
    private Options options;
    private List tokens = new ArrayList();

    private void init() {
        this.eatTheRest = false;
        this.tokens.clear();
    }

    /* access modifiers changed from: protected */
    public String[] flatten(Options options2, String[] strArr, boolean z) {
        String str;
        init();
        this.options = options2;
        Iterator it = Arrays.asList(strArr).iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (str2.startsWith(HelpFormatter.DEFAULT_LONG_OPT_PREFIX)) {
                int indexOf = str2.indexOf(61);
                if (indexOf == -1) {
                    str = str2;
                } else {
                    str = str2.substring(0, indexOf);
                }
                if (!options2.hasOption(str)) {
                    processNonOptionToken(str2, z);
                } else {
                    this.currentOption = options2.getOption(str);
                    this.tokens.add(str);
                    if (indexOf != -1) {
                        this.tokens.add(str2.substring(indexOf + 1));
                    }
                }
            } else if ("-".equals(str2)) {
                this.tokens.add(str2);
            } else if (!str2.startsWith("-")) {
                processNonOptionToken(str2, z);
            } else if (str2.length() == 2 || options2.hasOption(str2)) {
                processOptionToken(str2, z);
            } else {
                burstToken(str2, z);
            }
            gobble(it);
        }
        return (String[]) this.tokens.toArray(new String[this.tokens.size()]);
    }

    private void gobble(Iterator it) {
        if (this.eatTheRest) {
            while (it.hasNext()) {
                this.tokens.add(it.next());
            }
        }
    }

    private void processNonOptionToken(String str, boolean z) {
        if (z && (this.currentOption == null || !this.currentOption.hasArg())) {
            this.eatTheRest = true;
            this.tokens.add(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
        }
        this.tokens.add(str);
    }

    private void processOptionToken(String str, boolean z) {
        if (z && !this.options.hasOption(str)) {
            this.eatTheRest = true;
        }
        if (this.options.hasOption(str)) {
            this.currentOption = this.options.getOption(str);
        }
        this.tokens.add(str);
    }

    /* access modifiers changed from: protected */
    public void burstToken(String str, boolean z) {
        int i;
        int i2 = 1;
        while (i2 < str.length()) {
            String valueOf = String.valueOf(str.charAt(i2));
            if (this.options.hasOption(valueOf)) {
                List list = this.tokens;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("-");
                stringBuffer.append(valueOf);
                list.add(stringBuffer.toString());
                this.currentOption = this.options.getOption(valueOf);
                if (!this.currentOption.hasArg() || str.length() == (i = i2 + 1)) {
                    i2++;
                } else {
                    this.tokens.add(str.substring(i));
                    return;
                }
            } else if (z) {
                processNonOptionToken(str.substring(i2), true);
                return;
            } else {
                this.tokens.add(str);
                return;
            }
        }
    }
}
