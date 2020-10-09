package com.taobao.tao.log.godeye.core.plugin.runtime;

import com.taobao.tao.log.godeye.core.plugin.runtime.Plugin;

public class BuildInPlugin extends Plugin {
    public BuildInPlugin(Plugin.PluginData pluginData) {
        super(pluginData);
    }

    public void execute() throws Exception {
        executePluginMainClass(Class.forName(this.pluginData.getMainClass()));
    }
}
