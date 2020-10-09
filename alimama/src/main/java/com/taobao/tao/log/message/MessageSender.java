package com.taobao.tao.log.message;

public interface MessageSender {
    SenderInfo getSenderInfo();

    void init(MessageInfo messageInfo);

    MessageReponse pullMsg(MessageInfo messageInfo);

    MessageReponse sendMsg(MessageInfo messageInfo);

    MessageReponse sendStartUp(MessageInfo messageInfo);
}
