package com.shoppi.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoppi.roomdatabase_sample.R;

public class Maintest_Activity extends AppCompatActivity {
    Button set_button;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        기존 코드, 상속받은 AppCompatActivity이외의 코드도 동작함을 의미
//        이 코드를 생략하면 이후로 작성된 코드만 동작함을 의미
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alam_main);
        set_button=   (Button)findViewById(R.id.alam_plus_btn);

        //메인화면에서 시간 설정화면으로 전환
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
