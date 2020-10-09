package com.vivo.push;

import android.content.Intent;
import com.vivo.push.c.ab;

public interface IPushClientFactory {
    ab createReceiveTask(y yVar);

    y createReceiverCommand(Intent intent);

    v createTask(y yVar);
}
