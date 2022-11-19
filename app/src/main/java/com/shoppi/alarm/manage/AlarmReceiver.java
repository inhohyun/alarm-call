package com.shoppi.alarm.manage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.shoppi.alarm.MainActivity;
import com.shoppi.alarm.Maintest_Activity;
import com.shoppi.alarm.RingtonePlayingService;
import com.shoppi.roomdatabase_sample.R;

public class AlarmReceiver   extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context; // context 받아서 쓰기
        //서비스이름은 RingtonePlayingService인데 ringtone을 안써버렸네
        Intent sIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(sIntent);
        sIntent.putExtra("state", intent.getStringExtra("state"));

    //Oreo(26) 버전 이후부터 Background에서 실행을 금지해서 Foreground에서 실행해야된대

               // context.startForegroundService(sIntent);


        // intent로부터 전달받은 string
        String get_yout_string = intent.getExtras().getString("state");

        // RingtonePlayingService 서비스 intent 생성
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        // RingtonePlayinService로 extra string값 보내기
        service_intent.putExtra("state", get_yout_string);
        // start the ringtone service


         //   this.context.startForegroundService(service_intent);

            this.context.startService(service_intent);

    }
}
