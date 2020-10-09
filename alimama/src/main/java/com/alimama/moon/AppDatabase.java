package com.alimama.moon;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.alimama.union.app.aalogin.model.User;
import com.alimama.union.app.aalogin.model.UserDao;
import com.alimama.union.app.messageCenter.model.AlertMessage;
import com.alimama.union.app.messageCenter.model.AlertMessageDao;

@Database(entities = {AlertMessage.class, User.class}, version = 8)
@TypeConverters({RoomConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AlertMessageDao alertMessageDao();

    public abstract UserDao userDao();
}
