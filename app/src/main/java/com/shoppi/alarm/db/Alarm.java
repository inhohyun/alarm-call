package com.shoppi.alarm.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm_table") // db를 편하게 사용하기위한 데이터모델 import
//db에 저장할 정보
public class Alarm {
    @PrimaryKey(autoGenerate = true) // 아이디 자동 생성, id 값이 1씩 증가하여 자동 생성됨, 기본키를 따로 저장 안해줘도 됨
    private int alarmid = 0; // 생성된 알람 시간의 고유 id 값
    private int hour, minute; // 시간, 분
    //private boolean started, recurring; // 시작

    //getter & setter 가져오거나 세팅을 하기위한 단계, alt+insert로 생성

//    public Alarm(int hour, int minute){
//        this.hour = hour;
//        this.minute = minute;
//
//    }
    public int getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(int alarmid) {
        this.alarmid = alarmid;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


//    @Override
//    public String toString(){
//        return "RecordData{" +
//                "alarmid='" + alarmid + '\'' +
//                ", hour='" + hour + '\'' +
//                ", minute='" + minute + '\''
//                + '}';
//    }
}
