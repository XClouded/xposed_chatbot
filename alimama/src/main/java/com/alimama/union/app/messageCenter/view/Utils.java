package com.alimama.union.app.messageCenter.view;

import androidx.fragment.app.FragmentTransaction;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.union.app.messageCenter.model.AlertMessage;
import com.alimama.union.app.messageCenter.model.JSMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) Utils.class);

    public static void showAlert(BaseActivity baseActivity, AlertMessage alertMessage) throws Exception {
        JSMessage jSMessage = new JSMessage();
        jSMessage.setTitle(alertMessage.getTitle());
        jSMessage.setMessage(alertMessage.getContent());
        jSMessage.setButton(alertMessage.getAction());
        jSMessage.setUrl(alertMessage.getActionUrl());
        jSMessage.setMsgId(alertMessage.getId());
        jSMessage.setMsgType(alertMessage.getMsgType());
        showAlert(baseActivity, jSMessage);
    }

    public static void showAlert(BaseActivity baseActivity, JSMessage jSMessage) throws Exception {
        FragmentTransaction beginTransaction = baseActivity.getSupportFragmentManager().beginTransaction();
        if (baseActivity.getSupportFragmentManager().findFragmentByTag("JSMessageDialog") == null) {
            beginTransaction.addToBackStack((String) null);
            JSMessageDialog.newInstance(JSON.toJSONString(jSMessage)).show(beginTransaction, "JSMessageDialog");
        }
    }
}
