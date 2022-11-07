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
    void setInsertAlarm(Alarm alarm);

    @Update //수정
    void setUpdateAlarm(Alarm alarm);

    @Delete // 삭제
    void setDeleteAlarm(Alarm alarm);

    //조회 쿼리
    @Query("SELECT * FROM alarm_table")// 쿼리 : 데이터베이스에 요청하는 명령문
         List<Alarm> getAlarmAll();

//    @Insert
//    void insert(Alarm alarm);
//
//
//    @Query("DELETE FROM alarm_table")
//    void deleteAll();
//
//    LiveData<List<Alarm>> getAlarms();
//
//    @Update
//    void update(Alarm alarm);

}
