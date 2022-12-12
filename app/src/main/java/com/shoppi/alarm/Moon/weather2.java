package com.shoppi.alarm.Moon;

import static com.shoppi.roomdatabase_sample.R.id.btn_back;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoppi.alarm.activity.Maintest_Activity;
import com.shoppi.roomdatabase_sample.R;

public class weather2 extends AppCompatActivity {
    private Button btn_back ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Maintest_Activity.class);
                startActivity(intent);
            }
        });


    }
}
