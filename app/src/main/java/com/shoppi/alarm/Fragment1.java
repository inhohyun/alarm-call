package com.shoppi.alarm;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.shoppi.alarm.activity.Maintest_Activity;
import com.shoppi.alarm.db.Alarm;
import com.shoppi.alarm.db.AlarmDao;
import com.shoppi.alarm.db.AlarmDatabase;
import com.shoppi.alarm.manage.AlarmReceiver;
import com.shoppi.roomdatabase_sample.R;

//fragment에서 정보를 저장
public class Fragment1 extends Fragment {
    private AlarmDao mAlarmDao; // 알람 인터페이스 전역변수로 인스턴스
    View view;
    Context context;
    AlarmManager alarmanager;
    private Button save;
    private TimePicker timePicker;
    private PendingIntent pendingIntent; // 백그라운드 intent

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.activity_main, container, false); // 레이아웃 연결

        save = (Button) view.findViewById(R.id.save); // 맨 마지막에 저장하는 버튼
        timePicker = (TimePicker) view.findViewById(R.id.time_picker); // 시간 설정

        this.alarmanager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = null;
                //저장 버튼 클릭시 메인화면으로 돌아옴
                //일단 아래 if문은 alt enter한 것->이 부분이 문제..
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    calendar = Calendar.getInstance();
                    // calendar에 시간 셋팅
                    calendar.set(java.util.Calendar.HOUR_OF_DAY, timePicker.getHour()); // 시간
                    calendar.set(java.util.Calendar.MINUTE, timePicker.getMinute()); // 분
//                    calendar.set(Calendar.SECOND, 0); // 초
                    //현재시간보다 이전이면
                    if (calendar.before(Calendar.getInstance())){
                        //다음날로 설정
                        calendar.add(Calendar.DATE, 1);
                    }


                    //알람설정
                    alarmanager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    // 시간 가져옴
                    int hour = timePicker.getHour();
                    int minute = timePicker.getMinute();

                    // db저장하는 코드
                    AlarmDatabase database = Room.databaseBuilder(context.getApplicationContext(), AlarmDatabase.class, "alarm_table")
                            .fallbackToDestructiveMigration() // 스키마(db를 말함) 버전 변경 카능
                            .allowMainThreadQueries() // Main Thread에서 DBdp IO(입출력)를 가능하게 함, 쿼리를 사용 가능하도록
                            .build();
                    mAlarmDao = database.alarmDao(); // 아까 만든 인터페이스를 전역변수로 받음, 인터페이스 객체 할당

//         fragment에서context를 이용하려면 getActivity 함수 사용

                    Alarm alarm = new Alarm(); // 객체 인스턴스 생성
                    alarm.setHour(hour); // 설정한 알람 시간
                    alarm.setMinute(minute); //설정한 알람 분
                    mAlarmDao.Insert(alarm);
                }
                    //Receiver 설정->receiver 호출하는데 문제가 있음
                    Intent intent = new Intent(context, AlarmReceiver.class); // context 맞음?
                    intent.putExtra("state", "on"); // state 값이 on이면 알림 시작, off면 중지
                    pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                    //Toast로 알람 시간 보여주기
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss", Locale.getDefault());
//                    Toast.makeText(context, "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_SHORT).show(); // 일단 this로 들어갈 부분을 context로 엮긴함

                //    저장 클릭시 데이터베이스에 알람 내용 저장 후 메인화면으로 전환
                    Intent main_intent = new Intent(context, Maintest_Activity.class);
                    startActivity(main_intent);
                    Toast.makeText(context, "알람이 저장되었습니다.", Toast.LENGTH_LONG).show();
                }


        });



        return view;
    }

    //알람 중지, ringActivity에서 알람을 끄면 stop()을 호출
    public void stop(){
        if (this.pendingIntent == null){
            return;
        }
        this.alarmanager.cancel(this.pendingIntent);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("state", "off");
        getActivity().sendBroadcast(intent);

        this.pendingIntent = null;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

}
