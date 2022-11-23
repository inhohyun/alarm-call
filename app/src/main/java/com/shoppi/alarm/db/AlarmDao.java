package com.shoppi.alarm.db;

//Data Access Object

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert //삽입
    void Insert(Alarm alarm);

    @Update //수정
    void Update(Alarm alarm);

    @Delete // 삭제
    void Delete(Alarm alarm);

    @Query("SELECT * FROM alarm_table")
    LiveData<List<Alarm>> getAll(); //LiveData

    //삭제 쿼리
    @Query("DELETE FROM alarm_table")
    void deleteAll();


}
