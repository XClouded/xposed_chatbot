package com.alimama.union.app.contact.model;

import android.content.Context;
import java.util.List;

public interface ContactService {
    List<Contact> queryLocalContacts(Context context, int i);

    void sendSms(Context context, BatchSms batchSms);
}
