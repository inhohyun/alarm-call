package com.shoppi.alarm.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shoppi.alarm.activity.Maintest_Activity;
import com.shoppi.alarm.db.Alarm;
import com.shoppi.alarm.db.AlarmDao;
import com.shoppi.alarm.db.AlarmDatabase;
import com.shoppi.alarm.manage.AlarmReceiver;
import com.shoppi.roomdatabase_sample.R;

import java.util.Calendar;

//알람 설정화면, 알람 울리는 역할
public class MainActivity extends AppCompatActivity {
    private AlarmDao mAlarmDao; // 알람 인터페이스 전역변수로 인스턴스
    private Button save;
    private TimePicker timePicker;
    AlarmManager alarmmanager;
    Context context;
    PendingIntent pendingIntent;
    //private AlarmDatabase db;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    //+버튼을 눌렀을 때 실행되는 코드
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        save = (Button) findViewById(R.id.save); // 맨 마지막에 저장하는 버튼
        timePicker = (TimePicker) findViewById(R.id.time_picker); // 시간 설정

        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Calendar 객체 생성
        final Calendar calendar = Calendar.getInstance();
        // 알람리시버 intent 생성
        final Intent intent = new Intent(this.context, AlarmReceiver.class);

        // db저장하는 코드
        AlarmDatabase database = Room.databaseBuilder(context.getApplicationContext(), AlarmDatabase.class, "alarm_table")
                .fallbackToDestructiveMigration() // 스키마(db를 말함) 버전 변경 카능
                .allowMainThreadQueries() // Main Thread에서 DBdp IO(입출력)를 가능하게 함, 쿼리를 사용 가능하도록
                .build();
        mAlarmDao = database.alarmDao(); // 아까 만든 인터페이스를 전역변수로 받음, 인터페이스 객체 할당

        save.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                //저장 버튼 클릭시 메인화면으로 돌아옴
                //일단 아래 if문은 alt enter한 것
                    // calendar에 시간 셋팅
                    calendar.set(java.util.Calendar.HOUR_OF_DAY, timePicker.getHour()); // 시간
                    calendar.set(java.util.Calendar.MINUTE, timePicker.getMinute()); // 분
//                    calendar.set(Calendar.SECOND, 0); // 초

                    //현재시간보다 이전이면
                    if (calendar.before(android.icu.util.Calendar.getInstance())){
                        //다음날로 설정
                        calendar.add(android.icu.util.Calendar.DATE, 1);
                    }
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);



                intent.putExtra("state", "on"); // state 값이 on이면 알림 시작, off면 중지
            //알람 리시버 호출
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            //calendar에 저장된 시간에 알람 설정/반복
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

                //timePicker에서 저장된 내용 가져오기
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hour, int minute) {

                    }
                });

                //Toast로 알람 시간 보여주기
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss", Locale.getDefault());
//                    Toast.makeText(context, "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_SHORT).show(); // 일단 this로 들어갈 부분을 context로 엮긴함


                Alarm alarm = new Alarm(); // 객체 인스턴스 생성
                alarm.setHour(hour); // 설정한 알람 시간
                alarm.setMinute(minute); //설정한 알람 분
                mAlarmDao.Insert(alarm);

                //    저장 클릭시 데이터베이스에 알람 내용 저장 후 메인화면으로 전환
                Intent main_intent = new Intent(context, Maintest_Activity.class);
                startActivity(main_intent);
                Toast.makeText(context, "알람이 저장되었습니다.", Toast.LENGTH_LONG).show();
            }


        });


// 전에 코드 참고용으로 남겨둠 다 하고 지울게...
//        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE); // 알람매니저 설정
//        timePicker = (TimePicker) findViewById(R.id.time_picker); // 시간 설정
//        save = (Button) findViewById(R.id.save); // 맨 마지막에 저장하는 버튼
//
//        // Calendar 객체 생성
//        final Calendar calendar = Calendar.getInstance();
//        // 알람리시버 intent 생성
//        final Intent my_intent = new Intent(this.context, AlarmReceiver.class);
//        // db저장하는 코드
//        AlarmDatabase database = Room.databaseBuilder(getApplicationContext(), AlarmDatabase.class, "alarm_table")
//                .fallbackToDestructiveMigration() // 스키마(db를 말함) 버전 변경 카능
//                .allowMainThreadQueries() // Main Thread에서 DBdp IO(입출력)를 가능하게 함, 쿼리를 사용 가능하도록
//                .build();
//
//        mAlarmDao = database.alarmDao(); // 아까 만든 인터페이스를 전역변수로 받음, 인터페이스 객체 할당
//
//        //저장버튼 클릭
//        save.setOnClickListener(v -> {
//
//            //저장 버튼 클릭시 현재 timepicker의 값을 저장
//            // calendar에 시간 셋팅
//            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
//            calendar.set(Calendar.MINUTE, timePicker.getMinute());
//
//            // 시간 가져옴
//            int hour = timePicker.getHour();
//            int minute = timePicker.getMinute();
//
//            //calendar에 저장
//            calendar.set(Calendar.HOUR_OF_DAY, hour);
//            calendar.set(Calendar.MINUTE, minute);
//
//            // reveiver에 string 값 넘겨주기->이걸 왜 넘겨주더라?->key 값으로 쓰려고
//            my_intent.putExtra("state","alarm on");
//            pendingIntent = PendingIntent.getBroadcast(context, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            //알람 리시버 호출
//            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 1, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            //calendar에 저장된 시간에 알람 반복
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY, alarmIntent);
//
//                //timePicker에서 저장된 내용 가져오기
////                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
////                    @Override
////                    public void onTimeChanged(TimePicker view, int hour, int minute) {
////
////                    }
////                });
//
//
//            //저장버튼 클릭시 데이터 삽입
//            Alarm alarm = new Alarm(); // 객체 인스턴스 생성
//            alarm.setHour(hour); // 설정한 알람 시간
//            alarm.setMinute(minute); //설정한 알람 분
//            mAlarmDao.Insert(alarm);
//
//
//
//            //저장 클릭시 데이터베이스에 알람 내용 저장 후 메인화면으로 전환
//            Intent main_intent = new Intent(this, Maintest_Activity.class);
//            startActivity(main_intent);
//            Toast.makeText(MainActivity.this, "알람이 저장되었습니다.", Toast.LENGTH_LONG).show();
//
//        });
//






    }
    //알람을 중지하는 메소드, ringactivity에서 호출
    public void stop(){
        if (this.pendingIntent == null){
            return;
        }
        //off로 바꿔서 알람리시버->service로 호출
        this.alarmmanager.cancel(this.pendingIntent);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("state", "off");
        sendBroadcast(intent);

        this.pendingIntent = null;
    }

}
