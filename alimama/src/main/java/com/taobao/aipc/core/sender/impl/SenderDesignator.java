package com.taobao.aipc.core.sender.impl;

import com.taobao.aipc.core.sender.Sender;
import com.taobao.aipc.core.wrapper.ObjectWrapper;

public class SenderDesignator {
    public static final int TYPE_GET_INSTANCE = 1;
    public static final int TYPE_GET_UTILITY_CLASS = 2;
    public static final int TYPE_INVOKE_METHOD = 3;
    public static final int TYPE_NEW_INSTANCE = 0;

    public static Sender getPostOffice(int i, ObjectWrapper objectWrapper) {
        switch (i) {
            case 0:
                return new InstanceCreatingSender(objectWrapper);
            case 1:
                return new InstanceGettingSender(objectWrapper);
            case 2:
                return new UtilityGettingSender(objectWrapper);
            case 3:
                return new ObjectSender(objectWrapper);
            default:
                throw new IllegalArgumentException("Type " + i + " is not supported.");
        }
    }
}
