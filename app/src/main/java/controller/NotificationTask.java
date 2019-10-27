package controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import vn.edu.csc.babyapp.R;


public class NotificationTask {
    public static final String CHANNEL_ID = "vn.edu.csc.babyapp";
    public static final String CHANNEL_NAME = "babyapp";

    public static void createNotificationChannel(Context context) {
        // 1 - Kiểm tra OS có API level lớn hơn hoặc bằng 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 2 - Tạo ra notification channel với các thông tin cần thiết
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription("Reminders");
            channel.setShowBadge(true);
            // 3 - Tạo notification channel sử dụng NotificationManager
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    public static void createNotification(long timedata, Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);

        Notification notification = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setCustomContentView(remoteViews)
                .setAutoCancel(false)
                .build();

        ArrayList<String> listTime = ConverterTimer.convertTime(timedata);
        String hours = listTime.get(0);
        String minutes = listTime.get(1);
        String seconds = listTime.get(2);

        remoteViews.setTextViewText(R.id.tvNotiMess, hours + " : " + minutes + " : " + seconds + " min");

        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }

    public static void createLastNotification(Context context) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);

//        Uri customSoundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.classofdream);

        Notification notification = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setCustomContentView(remoteViews)
                .build();

        remoteViews.setTextViewText(R.id.tvNotiTittle, "Time is up");

        remoteViews.setTextViewText(R.id.tvNotiMess, "Don't forget your baby");

        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }

}
