package com.yd.yourdoctorandroid.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.activities.ChatActivity;
import com.yd.yourdoctorandroid.activities.MainActivity;
import com.yd.yourdoctorandroid.utils.Config;
import com.yd.yourdoctorandroid.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class YDFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = YDFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    private String senderId;
    private String nameSender;
    private String receiveId;
    private int type;
    private String storageId;
    private String message;
    private String createTime;

    private String title;
    private String description;

    static int i = 1;

    public YDFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null) {
            Log.e(TAG, "Notify is null");
            return;
        } else {
            Log.e(TAG,"Notify is here");
            senderId = remoteMessage.getData().get("senderId");
            nameSender = remoteMessage.getData().get("nameSender");
            receiveId = remoteMessage.getData().get("receiveId");
            type = Integer.parseInt(remoteMessage.getData().get("type"));
            storageId = remoteMessage.getData().get("storageId");
            message = remoteMessage.getData().get("message");
            createTime = remoteMessage.getData().get("createTime");

            switch (type) {
                case 1: {
                    title = "Thông báo chat";

                    Log.e("senderid",senderId );
                    Log.e("storageId",storageId );
                    Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                    resultIntent.putExtra("chatHistoryId",storageId);
                    resultIntent.putExtra("doctorChoiceId",senderId);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                    NotificationCompat.Builder mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.your_doctor_logo)
                                    .setContentTitle(title)
                                    .setContentText(message)
                                    .addAction(R.drawable.ic_done_black_24dp, "Đồng ý", resultPendingIntent);

                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(i, mBuilder.build());
                    break;
                }
                case 2: {
                    title="Thông báo Video call";
                    description = "";
                    break;
                }
                case 3: {
                    title = "Thông báo Thanh Toán";

                    Log.e("senderid",senderId );
                    Log.e("storageId",storageId);
//                    Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
//                    resultIntent.putExtra("chatHistoryId",storageId);
//                    resultIntent.putExtra("doctorChoiceId",senderId);
//                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//                    stackBuilder.addNextIntent(resultIntent);
//                    PendingIntent resultPendingIntent =
//                            stackBuilder.getPendingIntent(
//                                    0,
//                                    PendingIntent.FLAG_UPDATE_CURRENT
//                            );
                    //.addAction(R.drawable.ic_done_black_24dp, "Đồng ý", resultPendingIntent)

                    NotificationCompat.Builder mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.your_doctor_logo)
                                    .setContentTitle(title)
                                    .setContentText(message)
                                    ;

                    //mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(i, mBuilder.build());
                    break;
                }
                case 4: {
                    title="Thông báo thanh toán ngân hàng";
                    description = "";
                    break;
                }
            }


            i++;

        }

    }

}
