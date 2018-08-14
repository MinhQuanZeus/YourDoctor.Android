
package com.yd.yourdoctorandroid.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.activities.ChatActivity;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.utils.NotificationUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;


public class YDFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = YDFirebaseMessagingService.class.getSimpleName();
    private String senderId;
    private String nameSender;
    private String receiveId;
    private int type;
    private String storageId;
    private String message;
    private String createTime;

    private String title;
    private String description;

    static int id = 1;

    private Intent intent;
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder builder;
    private NotificationManager notifManager;

    public YDFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(SharedPrefs.getInstance().get("USER_INFO", Patient.class) == null) return;
        if (remoteMessage == null) {
            Log.e(TAG, "Notify is null");
            return;
        } else {
            senderId = remoteMessage.getData().get("senderId");
            nameSender = remoteMessage.getData().get("nameSender");
            receiveId = remoteMessage.getData().get("receiveId");
            type = Integer.parseInt(remoteMessage.getData().get("type"));
            storageId = remoteMessage.getData().get("storageId");
            message = remoteMessage.getData().get("message");
            createTime = remoteMessage.getData().get("createTime");

            if(SharedPrefs.getInstance().get("USER_INFO", Patient.class) != null){
                showNotification();
            }

        }
    }

    private void showNotification() {
        if (notifManager == null) {
            notifManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id + "");
            if (mChannel == null) {
                mChannel = new NotificationChannel(id + "", "YourDoctor", importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id + "");

            switch (type) {
                case 1: {
                    intent = new Intent(getApplicationContext(), ChatActivity.class);
                    Log.e("Notify is here", "Notify is here");
                    Log.e("sender id " , senderId);
                    Log.e("storageId" , storageId);
                    intent.putExtra("chatHistoryId",storageId);
                    intent.putExtra("doctorChoiceId",senderId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent pendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    //pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    builder.setContentTitle("Thông báo chat")  // required
                            .setSmallIcon(R.drawable.your_doctor_logo) // required
                            .setContentText(message)  // required
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    break;
                }
                case 3: {

                    builder.setContentTitle("Thông báo Thanh Toán")  // required
                            .setSmallIcon(R.drawable.your_doctor_logo) // required
                            .setContentText(message)  // required
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    break;
                }
            }

        } else {


            builder = new NotificationCompat.Builder(this);

            switch (type) {
                case 1: {
                    intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra("chatHistoryId",storageId);
                    intent.putExtra("doctorChoiceId",senderId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent pendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                    builder.setContentTitle("Thông báo chat")                           // required
                            .setSmallIcon(R.drawable.your_doctor_logo) // required
                            .setContentText(message)  // required
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setTicker(message)
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                            .setPriority(Notification.PRIORITY_HIGH);
                    break;
                }
                case 3: {

                    builder.setContentTitle("Thông báo thanh toán")                           // required
                            .setSmallIcon(R.drawable.your_doctor_logo) // required
                            .setContentText(message)  // required
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setTicker(message)
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                            .setPriority(Notification.PRIORITY_HIGH);
                    break;
                }
            }
        }

        Notification notification = builder.build();
        notifManager.notify(id, notification);
        id++;
    }
}

