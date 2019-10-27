package controller;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ankushgrover.hourglass.Hourglass;

import model.Route;
import vn.edu.csc.babyapp.R;

public class TimerService extends Service {

    private final static String TAG = "TimerService";

    public static final String COUNTDOWN_BR = "vn.edu.csc.babyapp.countdown_br";

    Intent bi = new Intent(COUNTDOWN_BR);

    Route route;

    private long mTimeInMillis;

    private long START_TIME_IN_MILLIS;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    Hourglass hourglass = null;

    private boolean mTimerRunning = false;

    public static boolean isRunning = false;
    private String music = null;
    private MediaPlayer m = null;

    public TimerService() {
        super();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        SharedPreferences sharedPreferences = getSharedPreferences("TimeRemainingRef", MODE_PRIVATE);
        mTimeLeftInMillis = sharedPreferences.getLong("TimeRemaining", 0);

        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        Bundle bundle = new Bundle();
        bundle.putLong("TimeLeft", mTimeInMillis);
        restartService.putExtras(bundle);
        startService(restartService);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("tag", "onStartCommand");
//        if (intent.getLongExtra("timeRemaining", 0) != 0) {
//            long timeData = intent.getLongExtra("timeRemaining", 0);
//            onStartCount(timeData);
//        }


        return START_STICKY;
    }

    public void onCreate() {
        super.onCreate();
        isRunning = true;
        //Take the time out from sharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences("Pref_Transfer_Time", MODE_PRIVATE);
        START_TIME_IN_MILLIS = sharedPreferences.getLong("time_transfer", 0) * 60000;
        music = sharedPreferences.getString("songInfo", "");

        if (START_TIME_IN_MILLIS > 0) {
            onStartCount(START_TIME_IN_MILLIS);
            NotificationTask.createNotification(START_TIME_IN_MILLIS, getApplicationContext());
            START_TIME_IN_MILLIS = 0;
        }
    }

    private void onStartCount(long START_TIME_IN_MILLIS) {
        hourglass = new Hourglass(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                SharedPreferences sharedPreferences = getSharedPreferences("TimeRemainingRef", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("TimeRemaining", timeRemaining);
                editor.apply();

                mTimeLeftInMillis = timeRemaining;

                NotificationTask.createNotification(timeRemaining, getApplicationContext());

                bi.putExtra("countdown", timeRemaining);
                bi.putExtra("code", 1);

                sendBroadcast(bi);
            }

            @Override
            public void onTimerFinish() {
                isRunning = false;

//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.classofdream);
//                mediaPlayer.start();
                playBeep(music);
                stopSelf();

                NotificationTask.createLastNotification(getApplicationContext());
            }
        };
        hourglass.startTimer();
        mTimerRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("tag", "onDestroy");
        isRunning = false;
        if (hourglass.isRunning()) {
            hourglass.pauseTimer();
        } else {
            Toast.makeText(this, "Service is cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("tag", "onBind");
        return null;
    }
    public void playBeep(String songTitle) {
        try {
            m = new MediaPlayer();
//            if (m.isPlaying()) {
//                m.stop();
//                m.release();
//                m = new MediaPlayer();
//            }

            AssetFileDescriptor descriptor = getAssets().openFd("sound/" +songTitle);
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(true);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
