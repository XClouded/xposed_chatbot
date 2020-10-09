package com.alimama.share;

import com.alimama.share.listeners.Action;
import java.util.ArrayList;
import java.util.List;

public class EtaoShare {
    private List<Action> commandList;

    private EtaoShare() {
        this.commandList = new ArrayList();
    }

    private static class SingletonInstance {
        /* access modifiers changed from: private */
        public static final EtaoShare INSTANCE = new EtaoShare();

        private SingletonInstance() {
        }
    }

    public static EtaoShare getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void giveCommand(Action action) {
        this.commandList.add(action);
    }

    public void beginExec() {
        for (Action execute : this.commandList) {
            execute.execute();
        }
        this.commandList.clear();
    }

    public void exec(Action action) {
        action.execute();
    }
}
