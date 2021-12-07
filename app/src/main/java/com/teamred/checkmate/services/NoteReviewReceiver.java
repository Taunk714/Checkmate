package com.teamred.checkmate.services;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

import static com.teamred.checkmate.App.CHANNEL_1_ID;
import static com.teamred.checkmate.App.CHANNEL_2_ID;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.teamred.checkmate.data.model.Post;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.teamred.checkmate.R;

import java.util.Calendar;
import java.util.List;



public class NoteReviewReceiver extends BroadcastReceiver {

    private static final String TAG = "NoteReviewReceiver";

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String ALARM_KEY = "alarm_key";

    @Override
    public void onReceive(Context context, Intent intent) {

        // Post p = intent.getExtra(BUNDLE_EXTRA);

        // (data.get("dateLastReviewed") == Calendar.getInstance().getTime())

        final NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID);
        builder.setSmallIcon(R.drawable.ic_algolia_icon);
        builder.setColor(ContextCompat.getColor(context, R.color.blue_300));
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText("the note brief");
        //builder.setTicker(alarm.getLabel());
        builder.setVibrate(new long[]{1000, 500, 1000, 500, 1000, 500});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //builder.setContentIntent(launchAlarmLandingPage(context, alarm));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);

        manager.notify(2, builder.build());

        //updateReviewDate(Post p);

    }

    /*private void updateReviewDate(Post p){
        if (data.get("dateLastReviewed") == Calendar.getInstance().getTime()){
            data.put("dateLastReviewed", Calendar.getInstance().getTime());
        }
    }*/




}
