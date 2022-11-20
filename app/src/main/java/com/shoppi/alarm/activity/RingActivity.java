package com.shoppi.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoppi.roomdatabase_sample.R;

public class RingActivity extends AppCompatActivity {
    Button clear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);

       MainActivity main = new MainActivity();

        clear = findViewById(R.id.btn_stop);
        //종료 버튼 클릭시 service에서 알람 종료
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment1의 stop() 메소드를 호출하면 됨
//                Intent intentService = new Intent(getApplicationContext(), AlarmReceiver.class);
//                getApplicationContext().stopService(intentService);

//                ((Fragment1) getSupportFragmentManager().findFragmentByTag("fragmentTag")).stop();
                main.stop();

                Toast.makeText(RingActivity.this, "알람 중지", Toast.LENGTH_SHORT).show();
                //알람 종료 후 메인화면으로 이동

                //왜 알람 설정창으로 intent 되는거지?
                Intent mainIntent = new Intent(getApplicationContext(), Maintest_Activity.class);
                startActivity(mainIntent);

            }
        });
    }


}
