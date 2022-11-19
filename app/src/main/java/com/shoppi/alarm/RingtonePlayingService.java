package com.shoppi.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.shoppi.roomdatabase_sample.R;

public class RingtonePlayingService extends Service {

    MediaPlayer mediaPlayer;
    boolean isRunning;

    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound1);
//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);





    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        //Foreground에서 실행하려면 Notification(위에뜨는 팝업창)을 보여줘야됨, Foreground는 안끄면 계속 실행되기 때문
//        String getState = intent.getExtras().getString("state");
//
//
//           // String channelId = createNotificationChannel();
//
//         //   NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
//          //  Notification notification = builder.setOngoing(true)
//           //         .setSmallIcon(R.mipmap.ic_launcher)
//         //           .build();
//
//        //    startForeground(1,notification);
//
//
        String state = intent.getStringExtra("state");

        if (!this.isRunning || state.equals("on")){
            //알람음 재생 off, 알람 시작 상태
            //일단 알람이 울리는 것만
            this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound1);
            mediaPlayer.setLooping(true);
            this.mediaPlayer.start();

            this.isRunning = true;
            Log.d("AlarmService", "Alarm Start");
            //알람을 지속적으로 울리면서

        }else if (this.isRunning && state.equals("off")) {
            //알람음 재생 on, 알람음 중지 상태
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
            this.mediaPlayer.release();

            this.isRunning = false;
            Log.d("AlarmService", "Alarm Stop");
//             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                stopForeground(true);
//            }
//
//        }


//서비스 종료
        }
       return START_NOT_STICKY;
   }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private String createNotificationChannel(){
//     String channelId = "Alarm";
//     String channelName = getString(R.string.app_name);
//     NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
//    channel.setSound(null,null);
//    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//    manager.createNotificationChannel(channel);
//
//        return channelId;
//    }





//    @Override
//    public void onDestroy () {
//        super.onDestroy();
//        //서비스가 종료될 때 음악, 진동 중지
//        mediaPlayer.stop();
//      //  vibrator.cancel();
//
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
