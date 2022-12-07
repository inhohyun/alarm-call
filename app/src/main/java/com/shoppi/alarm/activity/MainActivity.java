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

//알람 설정화면, 알람 울리는 역할, fragment로 수정 예정(시간 없으면 그냥 activity로)
public class MainActivity extends AppCompatActivity {
    private AlarmDao mAlarmDao; // 알람 인터페이스 전역변수로 인스턴스
    private Button save;
    private TimePicker timePicker;
    AlarmManager alarmmanager;
    Context context;
    //  PendingIntent pendingIntent;
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

        //저장버튼 클릭시
        save.setOnClickListener(new View.OnClickListener() {

            //getInstance를 사용하기위해 api 호출, 지우면 getIntance 부분에 빨간줄 뜰것임
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // calendar에 시간 셋팅
                calendar.set(java.util.Calendar.HOUR_OF_DAY, timePicker.getHour()); // 시간
                calendar.set(java.util.Calendar.MINUTE, timePicker.getMinute()); // 분


                //현재시간보다 이전이면
                if (calendar.before(android.icu.util.Calendar.getInstance())) {
                    //다음날로 설정
                    calendar.add(android.icu.util.Calendar.DATE, 1);
                }

                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                //일단 state로 값 없이 설정하는 것으로 바꿈
                //이거 값 뭐 전달되는지 물어봤었나? -> state는 key값이고 on이 value 값임/
                 intent.putExtra("state", "on"); // state 값이 on이면 알림 시작, off면 중지

                //알람 리시버 호출
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //calendar에 저장된 시간에 알람 설정/반복
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);

                //timePicker에서 저장된 내용 가져오기, 필요한가 싶긴한데 일단 둠
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hour, int minute) {

                    }
                });

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


    }

}
