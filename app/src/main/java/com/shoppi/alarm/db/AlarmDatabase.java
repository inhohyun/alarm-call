package com.shoppi.alarm.db;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Alarm.class}, version = 1) //변경사항에대해 관리하려면 버전을 명시해줘야 함, User라는 테이블 안에 데이터를 넣을 것임
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao(); // 아까 만든 AlarmDao의 추상메소드



}
