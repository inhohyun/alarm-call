package com.shoppi.alarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shoppi.alarm.db.Alarm;
import com.shoppi.alarm.db.AlarmDao;
import com.shoppi.alarm.db.AlarmDatabase;
import com.shoppi.roomdatabase_sample.R;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlarmDao mAlarmDao; // 알람 인터페이스 전역변수로 인스턴스
    private Button save;
    private TimePicker timePicker;
    private AlarmDatabase db;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    //+버튼을 눌렀을 때 실행되는 코드
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = (TimePicker) findViewById(R.id.time_picker); // 시간 설정
        save = (Button) findViewById(R.id.save); // 맨 마지막에 저장하는 버튼


        // db저장하는 코드
        AlarmDatabase database = Room.databaseBuilder(getApplicationContext(), AlarmDatabase.class, "alarm_table")
                .fallbackToDestructiveMigration() // 스키마(db를 말함) 버전 변경 카능
                .allowMainThreadQueries() // Main Thread에서 DBdp IO(입출력)를 가능하게 함, 쿼리를 사용 가능하도록
                .build();

        mAlarmDao = database.alarmDao(); // 아까 만든 인터페이스를 전역변수로 받음, 인터페이스 객체 할당

        //저장버튼 클릭
        save.setOnClickListener(v -> {

            //저장 버튼 클릭시 현재 timepicker의 값을 저장
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int hour = timePicker.getHour(); // timepicker의 시간 값을 변수에 저장
            int minute = timePicker.getMinute(); // timepicker의 분 값은 변수에 저장

            //calendar에 저장
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }

            //알람 리시버 호출
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                Intent intent = new Intent(this, AlarmReceiver.class);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //calendar에 저장된 시간에 알람 반복
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);


                //timePicker에서 저장된 내용 가져오기
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hour, int minute) {

                    }
                });
            }

            //저장버튼 클릭시 데이터 삽입
            Alarm alarm = new Alarm(); // 객체 인스턴스 생성
            alarm.setHour(hour); // 설정한 알람 시간
            alarm.setMinute(minute); //설정한 알람 분
            mAlarmDao.Insert(alarm);

//            List<Alarm> userList = mAlarmDao.getAll(); // UserDao에서 만든 조회 쿼리를 리스트에 저장
//
//            //데이터 조회,  fori+tap하면 자동 for문 완성됨(신기하네 안드로이드 단축키인가)
//            for (int i = 0; i < userList.size(); i++) { // database 크기 만큼의 반복
//                Log.d("TEST", userList.get(i).getHour() + "\n"
//                        + userList.get(i).getMinute() + "\n"
//                        + userList.get(i).getAlarmid() + "\n");
//
//            }

            //저장 클릭시 데이터베이스에 알람 내용 저장 후 메인화면으로 전환
            Intent main_intent = new Intent(this, Maintest_Activity.class);
            startActivity(main_intent);
            Toast.makeText(MainActivity.this, "알람이 저장되었습니다.", Toast.LENGTH_LONG).show();

        });






//        //데이터 수정
//        Alarm alarm2 = new Alarm(); // 객체 인스턴스 생성
//        alarm2.setAlarmid(1); // 어떤 id 값을 참조할 것인지, 기본키 참조
//        alarm2.setHour(mHour);
//        alarm2.setMinute(mMinute);
//        mAlarmDao.setUpdateAlarm(alarm2);//userDao에 만든 setUpdateAlarm를 가져와 id(Primary key)가 참조하는 값을 위의 내용으로 수정
//
//        //데이터 삭제
//        Alarm alarm3 = new Alarm();
//        alarm3.setAlarmid(2); // 참조할 기본키
//        mAlarmDao.setDeleteAlarm(alarm3); // 참조하는 값을 삭제제
    }


}
