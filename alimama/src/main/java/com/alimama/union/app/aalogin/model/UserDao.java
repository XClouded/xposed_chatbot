package com.alimama.union.app.aalogin.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("select * from user where userId=:userId")
    User getUserById(String str);

    @Query("select * from user where userId=:userId")
    LiveData<User> getUserByIdAsync(String str);

    @Insert(onConflict = 1)
    void insertUser(User user);
}
