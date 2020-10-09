package com.alimama.union.app.messageCenter.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AlertMessageDao {
    @Query("select * from alertMessage where taobaoNumId=:taobaoNumId and msgType=:msgType and CAST(strftime('%s','now') AS INTEGER)<(createTime+expireDay*60*60*24) order by createTime desc limit 1")
    AlertMessage getLastedMessage(Long l, Integer num);

    @Query("select * from alertMessage where taobaoNumId=:taobaoNumId and read=1 and CAST(strftime('%s','now') AS INTEGER)<(createTime+expireDay*60*60*24) order by createTime desc limit 1")
    LiveData<AlertMessage> getLastedUnreadMessageAsync(Long l);

    @Query("select * from alertMessage where taobaoNumId=:taobaoNumId and msgType=:msgType and id=:msgId order by createTime desc limit 1")
    AlertMessage getMessageById(Long l, Integer num, Long l2);

    @Insert(onConflict = 1)
    void insert(AlertMessage alertMessage);

    @Insert(onConflict = 1)
    void insert(List<AlertMessage> list);

    @Update(onConflict = 1)
    void update(AlertMessage alertMessage);
}
