package com.facebook.stetho.dumpapp;

import alimama.com.unwetaologger.base.UNWLogger;
import com.taobao.weex.ui.component.WXBasicComponentType;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class GlobalOptions {
    public final Option optionHelp = new Option("h", "help", false, "Print this help");
    public final Option optionListPlugins = new Option("l", WXBasicComponentType.LIST, false, "List available plugins");
    public final Option optionProcess = new Option("p", UNWLogger.LOG_VALUE_TYPE_PROCESS, true, "Specify target process");
    public final Options options = new Options();

    public GlobalOptions() {
        this.options.addOption(this.optionHelp);
        this.options.addOption(this.optionListPlugins);
        this.options.addOption(this.optionProcess);
    }
}
