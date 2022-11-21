package com.shoppi.alarm.manage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.shoppi.alarm.service.RingtonePlayingService;

public class AlarmReceiver   extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context; // context 받아서 쓰기
        //서비스이름은 RingtonePlayingService인데 ringtone을 안써버렸네->다 구현되면 Ringtone으로 바꿀듯(기본 알람)

        Intent sIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(sIntent);
        sIntent.putExtra("state", intent.getStringExtra("state"));

    //Oreo(26) 버전 이후부터 Background에서 실행을 금지해서 Foreground에서 실행해야된대

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(sIntent);
        }


        // intent로부터 전달받은 string->받아서 전달
        //state 값 잆이 써보자->off로 줘서 알람을 끄는게아닌 service자체를 종료시키는 방식으로 알람을 끄려고 함
//        String get_yout_string = intent.getExtras().getString("state");

        // RingtonePlayingService 서비스 intent 생성
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        // RingtonePlayinService로 extra string값 보내기
//        service_intent.putExtra("state", get_yout_string);
        //서비스 시작
            this.context.startService(service_intent);

    }
}
