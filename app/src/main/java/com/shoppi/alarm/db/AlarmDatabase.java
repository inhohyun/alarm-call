package com.shoppi.alarm.db;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Alarm.class}, version = 2, exportSchema = false) //변경사항에대해 관리하려면 버전을 명시해줘야 함,
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao(); // 아까 만든 AlarmDao의 추상메소드

    private static volatile AlarmDatabase INSTANCE;

    //싱글톤
    public static AlarmDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AlarmDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AlarmDatabase.class, "alarm_table") // 여기의 데이터베이스를 가져와서 리사이클러뷰에 띄움, 이름 달라서 참조 못하는 버그 해결
                            //fallbackToDestructiveMigration() : db의 스키마를 변경해야할때 version을 바꾸기 위한 메소드 
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    //db 객체 제거
    public static void destroyInstance() {
        INSTANCE = null;
    }

}
