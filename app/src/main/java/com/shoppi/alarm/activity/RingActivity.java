package com.shoppi.alarm.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shoppi.alarm.db.Alarm;
import com.shoppi.alarm.db.AlarmDatabase;
import com.shoppi.alarm.list.RecyclerAdapter;
import com.shoppi.alarm.manage.AlarmReceiver;
import com.shoppi.alarm.service.RingtonePlayingService;
import com.shoppi.roomdatabase_sample.R;

public class RingActivity extends AppCompatActivity {
    Button clear;

    private AlarmDatabase db;
    private Intent intent;
    private static final int TOTAL = 11 * 1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    static final int PERMISSIONS_CALL_PHONE = 1;

    private  int count = 10;
    private TextView countTxt;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);



        clear = findViewById(R.id.btn_stop);
        countTxt = findViewById(R.id.count_txt);
        countDownTimer();
        countDownTimer.start();


        //종료 버튼 클릭시 service에서 알람 종료
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment1의 stop() 메소드를 호출하면 됨
//                Intent intentService = new Intent(getApplicationContext(), AlarmReceiver.class);
//                getApplicationContext().stopService(intentService);

//                ((Fragment1) getSupportFragmentManager().findFragmentByTag("fragmentTag")).stop();

                //Mainactivity에서 stop메소드를 호출
                Intent stopIntent = new Intent(RingActivity.this, AlarmReceiver.class);
                //stopService로 호출하여 onDestroy부분을 실행
                stopIntent.putExtra("state", "off");
                //off로
                sendBroadcast(stopIntent);

//                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
//                //state에 off값을 보냄
//                //putExtra -> intent 보내는 곳으로 value 값을 보냄
//                intent.putExtra("state", "off");
//                //alarmreciver 호출
//                sendBroadcast(intent);

                //알람 종료 후 메인화면으로 이동

//                Intent mainIntent = new Intent(getApplicationContext(), Maintest_Activity.class);
//                startActivity(mainIntent);

            }
        });
    }
    public void countDownTimer(){
        countDownTimer = new CountDownTimer(TOTAL, COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long l) {
                countTxt.setText(String.valueOf(count));
                count--;
            }

            //해당 메소드에 전화연결부분 구현
            @Override
            public void onFinish() {
                countTxt.setText("알람 종료");
                //카운트 종료시 해당 번호로 전화, db 텍스트 가져오기()
                //찾고자하는 position을 어떻게 잡지?


                //인텐트로 전화번호를 받기....(미구현)
//                intent = getIntent();
//                String Num = intent.getExtras().getString("");
                String tel = "tel:" + "01029954545";
                //카운트 종료시 알람종료
                Intent call_Intent = new Intent(RingActivity.this, RingtonePlayingService.class);
                stopService(call_Intent);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(tel));
                startActivity(callIntent);



            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            countDownTimer.cancel();
        }catch (Exception e){

        }
        countDownTimer = null;
    }

}

