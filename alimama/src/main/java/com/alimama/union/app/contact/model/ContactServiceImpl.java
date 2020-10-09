package com.alimama.union.app.contact.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.config.MoonConfigCenter;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactServiceImpl implements ContactService {
    private Logger logger = LoggerFactory.getLogger((Class<?>) ContactServiceImpl.class);

    public List<Contact> queryLocalContacts(Context context, int i) {
        ArrayList arrayList = new ArrayList();
        Cursor query = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{"_id", "display_name", "data1"}, "mimetype = 'vnd.android.cursor.item/phone_v2' AND has_phone_number = 1", (String[]) null, "sort_key");
        if (query == null) {
            this.logger.error("query contacts return null cursor!!!");
            return arrayList;
        }
        while (query.moveToNext() && arrayList.size() < i) {
            String string = query.getString(query.getColumnIndex("display_name"));
            String formatPhoneNumber = formatPhoneNumber(query.getString(query.getColumnIndex("data1")));
            if (Pattern.matches(MoonConfigCenter.getPhoneNumberPattern(), formatPhoneNumber)) {
                Contact contact = new Contact();
                contact.setName(string);
                contact.setPhone(formatPhoneNumber);
                arrayList.add(contact);
                this.logger.info("contact: {}", (Object) JSON.toJSONString(contact));
            }
        }
        query.close();
        return arrayList;
    }

    public void sendSms(Context context, BatchSms batchSms) {
        Intent intent = new Intent("android.intent.action.SENDTO");
        String join = TextUtils.join(",", batchSms.getPhones().toArray());
        intent.setData(Uri.parse("smsto:" + join));
        intent.putExtra("sms_body", batchSms.getText());
        context.startActivity(intent);
    }

    private String formatPhoneNumber(String str) {
        char[] charArray = str.toCharArray();
        char[] cArr = new char[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (Character.isDigit(c)) {
                cArr[i] = c;
            } else {
                cArr[i] = ' ';
            }
        }
        return new String(cArr).replace(Operators.SPACE_STR, "");
    }
}
