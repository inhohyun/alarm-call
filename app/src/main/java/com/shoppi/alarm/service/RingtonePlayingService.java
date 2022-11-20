package com.shoppi.alarm.service;

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

import com.shoppi.alarm.activity.Maintest_Activity;
import com.shoppi.alarm.activity.RingActivity;
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
        //notification을 추가하면 알람이 실행안되는 버그 발생->pendingintent문제인가??->right!
        //Foreground에서 실행하려면 Notification(위에뜨는 팝업창)을 보여줘야됨, Foreground는 안끄면 계속 실행되기 때문

        //아래 notification 관련 코드인데 살리면 알람이 안울려서 일단 죽여둠, 근데 없어도 될지도?
//        String getState = intent.getExtras().getString("state");

//        Intent notificationIntent = new Intent(this, RingActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            String channelId = createNotificationChannel();
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
//            Notification notification = builder.setOngoing(true)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("알람 울리는 중")
////                    .setPriority(NotificationCompat.PRIORITY_MAX)
////                    .setFullScreenIntent(pendingIntent,true)
//                    .build();
//
//            startForeground(1,notification);
//        }


        //state 값을 받음
        String state = intent.getStringExtra("state");

//        if (!isRunning || state.equals("on")) {
            //알람음 재생 off, 알람 시작 상태
            //일단 알람이 울리는 것만
            this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound1);
            mediaPlayer.setLooping(true);
            this.mediaPlayer.start();

            this.isRunning = true;
            Log.d("AlarmService", "Alarm Start");
            //알람을 지속적으로 울리면서 ringactivity를 호출해야함...

            Intent i = new Intent(this, RingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            //여기까진 정상
        //}
            //}else if (isRunning || state.equals("off")) {
            //알람음 재생 on, 알람음 중지 상태

//            //off로 받은 다음 다시 on으로 줘야되는거 아닌가? -> isRunnig으로 구분
//
//            this.isRunning = false;
//            Log.d("AlarmService", "Alarm Stop");
////             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                stopForeground(true);
////            }
////
////        }

////        }
//서비스 종료

       return START_NOT_STICKY;
   }

   //notification 쓸꺼면 활성화
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




// question : service가 종료되었는데 알람 소리는 왜 계속 진행되는가?-> service 생명주기 복습
    @Override
    public void onDestroy () {
        super.onDestroy();
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
            this.mediaPlayer.release();

        //이건 작동하는데 소리는 왜 안꺼짐?
        Intent j = new Intent(this, Maintest_Activity.class);
        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(j);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
