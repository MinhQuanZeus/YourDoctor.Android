package com.yd.yourdoctorandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.yd.yourdoctorandroid.activities.AuthActivity;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.CheckStatusChatService;
import com.yd.yourdoctorandroid.services.CheckNetWordChangeService;
import com.yd.yourdoctorandroid.services.TimeOutChatService;
import com.yd.yourdoctorandroid.services.YDFirebaseInstanceIDService;
import com.yd.yourdoctorandroid.services.YDFirebaseMessagingService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getStringResourceByString(Context context, String name) {
        try {
            Resources res = context.getResources();
            return res.getString(res.getIdentifier(name, "string", context.getPackageName()));
        } catch (Exception ex) {
            return "";
        }
    }

    public static File persistImage(Bitmap bitmap, String name, Context context) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    public static String convertTime(long time){
        Date date = new Date(time);
        //yyyy MM dd HH:mm:ss
        Format format = new SimpleDateFormat("HH:mm, dd/MM ");
        return format.format(date);
    }

    public static String convertTimeFromMonggo(String timeString){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Date date = null;
        Format  format2;
        try {
            date = format.parse(timeString);
        } catch (Exception e) {
            date = new Date();
            Log.e("LoiDat", e.toString());
        }
        format2 = new SimpleDateFormat("HH:mm, dd/MM ");
        return format2.format(date);

    }

    public static void backToLogin(Context context){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPrefs.getInstance().get("USER_INFO", Patient.class).getId());
        //context.stopService(new Intent(context, YDFirebaseMessagingService.class));
        //context.stopService(new Intent(context, CheckNetWordChangeService.class));
        //context.stopService(new Intent(context, TimeOutChatService.class));
        SocketUtils.getInstance().disconnectConnect();
        SharedPrefs.getInstance().clear();
        Intent intent = new Intent(context, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
