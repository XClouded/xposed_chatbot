package com.taobao.aipc.core.receiver;

import com.taobao.aipc.core.receiver.impl.InstanceCreatingReceiver;
import com.taobao.aipc.core.receiver.impl.InstanceGettingReceiver;
import com.taobao.aipc.core.receiver.impl.ObjectReceiver;
import com.taobao.aipc.core.receiver.impl.UtilityGettingReceiver;
import com.taobao.aipc.core.receiver.impl.UtilityReceiver;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.exception.IPCException;

public class ReceiverDesignator {
    public static Receiver getReceiver(ObjectWrapper objectWrapper) throws IPCException {
        int type = objectWrapper.getType();
        switch (type) {
            case 0:
                return new InstanceCreatingReceiver(objectWrapper);
            case 1:
                return new InstanceGettingReceiver(objectWrapper);
            case 2:
                return new UtilityGettingReceiver(objectWrapper);
            case 3:
                return new ObjectReceiver(objectWrapper);
            case 4:
                return new UtilityReceiver(objectWrapper);
            default:
                throw new IPCException(4, "Type " + type + " is not supported.");
        }
    }
}
