package com.shoppi.alarm.onoff;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoppi.roomdatabase_sample.R;

public class SwitchButton extends AppCompatActivity  {
    Switch switchbutton;
    class visibilitySwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                Toast.makeText(getApplicationContext(), "알람이 활성화 되었습니다.", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(getApplicationContext(), "알람이 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_item);

        switchbutton = findViewById(R.id.alarm_start);
        switchbutton = findViewById(R.id.alarm_start);
        switchbutton.setOnCheckedChangeListener(new visibilitySwitchListener());


    }
}
