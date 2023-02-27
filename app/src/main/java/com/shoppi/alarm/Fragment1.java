package com.shoppi.alarm;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.shoppi.alarm.activity.SettingActivity;
import com.shoppi.alarm.db.Alarm;
import com.shoppi.alarm.db.AlarmDao;
import com.shoppi.alarm.db.AlarmDatabase;
import com.shoppi.alarm.manage.AlarmReceiver;
import com.shoppi.roomdatabase_sample.R;

//fragment에서 정보를 저장
public class Fragment1 extends Fragment {
    View view;
    private AlarmDao mAlarmDao; // 알람 인터페이스 전역변수로 인스턴스
    private Button save;
    private TimePicker timePicker;
    AlarmManager alarmmanager;
    Context context;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //fragment가 inflated 되면 fagment가 activity안에 들어가게됨+

        view = inflater.inflate(R.layout.alarmset, container, false); // 레이아웃 연결

        save =  view.findViewById(R.id.save); // 맨 마지막에 저장하는 버튼
        timePicker =  view.findViewById(R.id.time_picker); // 시간 설정

        alarmmanager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        // Calendar 객체 생성
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        // 알람리시버 intent 생성
        final Intent intent = new Intent(this.context, AlarmReceiver.class);

        // db저장하는 코드
        AlarmDatabase database = Room.databaseBuilder(context.getApplicationContext(), AlarmDatabase.class, "alarm_table")
                .fallbackToDestructiveMigration() // 스키마(db를 말함) 버전 변경 카능
                .allowMainThreadQueries() // Main Thread에서 DBdp IO(입출력)를 가능하게 함, 쿼리를 사용 가능하도록
                .build();
        mAlarmDao = database.alarmDao(); // 아까 만든 인터페이스를 전역변수로 받음, 인터페이스 객체 할당

        //저장버튼 클릭
        save.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // calendar에 시간 셋팅
                calendar.set(java.util.Calendar.HOUR_OF_DAY, timePicker.getHour()); // 시간
                calendar.set(java.util.Calendar.MINUTE, timePicker.getMinute()); // 분


                //현재시간보다 이전이면
                if (calendar.before(Calendar.getInstance())){
                    //다음날로 설정
                    calendar.add(Calendar.DATE, 1);
                }

                // 시간 가져옴
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                calendar.set(java.util.Calendar.HOUR_OF_DAY, hour);
                calendar.set(java.util.Calendar.MINUTE, minute);

                //일단 state로 값 없이 설정하는 것으로 바꿈

                //이거 값 뭐 전달되는지 물어봤었나? -> state는 key값이고 on이 value 값임/
                intent.putExtra("state", "on"); // state 값이 on이면 알림 시작, off면 중지

                //알람 리시버 호출
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
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
                Intent main_intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(main_intent);
                Toast.makeText(context, "알람이 저장되었습니다.", Toast.LENGTH_LONG).show();

            }

        });

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

}
