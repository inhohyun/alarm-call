package com.shoppi.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoppi.alarm.manage.AlarmReceiver;
import com.shoppi.alarm.service.RingtonePlayingService;
import com.shoppi.roomdatabase_sample.R;

public class RingActivity extends AppCompatActivity {
    Button clear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);

//        MainActivity main = new MainActivity(); ->따로 메소드로 안 만들고 바로 적음

        clear = findViewById(R.id.btn_stop);
        //종료 버튼 클릭시 service에서 알람 종료
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment1의 stop() 메소드를 호출하면 됨
//                Intent intentService = new Intent(getApplicationContext(), AlarmReceiver.class);
//                getApplicationContext().stopService(intentService);

//                ((Fragment1) getSupportFragmentManager().findFragmentByTag("fragmentTag")).stop();

                //Mainactivity에서 stop메소드를 호출
                //  main.stop();
                Toast.makeText(RingActivity.this, "알람 중지", Toast.LENGTH_SHORT).show();

                Intent stopIntent = new Intent(RingActivity.this, RingtonePlayingService.class);
                //stopService로 호출하여 onDestroy부분을 실행
                stopService(stopIntent);

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


}
