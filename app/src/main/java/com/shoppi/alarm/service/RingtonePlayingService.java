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

        //진동 활성화 구현할꺼면 사용
//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


    }

    // service 생명주기상  onCraate -> onStartCommand(호출한쪽에서 intent에 담겨진 key value로 피라미터 값을 받을 수 있음)
// service 재 실행시 여기부터 실행되므로 이 부분에 주요 코드를 몰아넣음
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound1);
        //미디어 반복여부
        mediaPlayer.setLooping(true);
        this.mediaPlayer.start();

        this.isRunning = true;
        Log.d("AlarmService", "Alarm Start");

        //알람 울리면 RingActivity 호출
        Intent i = new Intent(this, RingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);


        //아래 notification으로 Forground로 알람 울리는중... 보여줄라했는데 에러나서 일단 지움
        //notification을 추가하면 알람이 실행안되는 버그 발생->pendingintent문제인가??-> 맞는 것 같긴한데 일단 보류
        //Foreground에서 실행하려면 Notification(위에뜨는 팝업창)을 보여줘야됨, Foreground는 안끄면 계속 실행되기 때문
        //Foreground :  파일 다운같이 팝업창으로 사용자에게 백그라운드로 실행되는 상황을 보여줌
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


        //state 값을 받음->state 값 없이 service 호출하면 그냥 알람 울리는 걸로 바꿈 코드 수정 예정
        //     String state = intent.getStringExtra("state");

//        if (!isRunning || state.equals("on")) {
        //알람음 재생 off, 알람 시작 상태
        //일단 알람이 울리는 것만

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

        return START_STICKY;
    }

    //notification(팝업 알림) 쓸꺼면 활성화 -> 좀 손봐야됨
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


    //RingActivity에서 stopService ->서비스 중지 요청 -> onDestroy호출 -> (서비스 중지)알람음 중지->intent는 먹는데 알람은 안꺼짐 : why?

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.setLooping(false); // 반복해제 후 종료(그냥 종료하면 안되나?)
        mediaPlayer.stop();

        //Toast를 활용해 onDestroy가 정상적으로 실행됨을 확인 -> 근데 소리는 왜 안꺼짐?
        Toast.makeText(this, "알람 종료", Toast.LENGTH_SHORT).show();

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
