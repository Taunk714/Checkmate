package com.teamred.checkmate.services;

import static com.teamred.checkmate.App.CHANNEL_1_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teamred.checkmate.data.CheckmateKey;
import com.teamred.checkmate.data.Constant;
import com.teamred.checkmate.data.model.Post;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.teamred.checkmate.R;
import com.teamred.checkmate.data.model.ReviewRecord;
import com.teamred.checkmate.util.DateUtil;

import java.util.Calendar;
import java.util.Date;


public class NoteReviewReceiver extends BroadcastReceiver {

    private static final String TAG = "NoteReviewReceiver";

    private static final String GROUP_ID = "Group_id";
    private static final String POST_ID = "Post_id";
//    private static final String GROUP
    //private static final String ALARM_KEY = "alarm_key";
    DocumentReference docRef;
    Post post;

    String postId = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //db.collection(CheckmateKey.GROUP_FIREBASE).document(this.group_ID).collection("posts").

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        //String postid = extras.getString(POST_ID);

        // intent.getExtra(BUNDLE_EXTRA);
        postId = extras.getString("post_title");
        if( extras != null){
        docRef = db.collection(CheckmateKey.USER_FIREBASE).document(Constant.getInstance().getCurrentUser().getUid())
                .collection("savedPosts").document(extras.getString(POST_ID));
        }


        // (data.get("dateLastReviewed") == Calendar.getInstance().getTime())

        final NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID);
        builder.setSmallIcon(R.drawable.ic_algolia_icon);
        builder.setColor(ContextCompat.getColor(context, R.color.blue));
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText("Time to Review your Notes!");
        builder.setVibrate(new long[]{1000, 500, 1000, 500, 1000, 500});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //builder.setContentIntent(launchAlarmLandingPage(context, alarm));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);

        manager.notify(2, builder.build());

        updateReviewDate(docRef);

    }

    private void updateReviewDate(DocumentReference docRef){
        docRef.update("dateLastReviewed", Calendar.getInstance().getTime());
        docRef.get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       DocumentSnapshot result1 = task.getResult();
                       int timesReviewed = Integer.parseInt(result1.get("numTimesReviewed").toString());
                       docRef.update("numTimesReviewed", timesReviewed);
                       String simpleDateString = DateUtil.getSimpleDateString(new Date());
                       ReviewRecord reviewRecord = new ReviewRecord();
                       reviewRecord.setTime(new Date());
                       reviewRecord.setTimes(timesReviewed+1);
                       reviewRecord.setPostId(postId);
                       db.collection(CheckmateKey.REVIEW_RECORD)
                               .document(Constant.getInstance().getCurrentUser().getUid())
                               .collection(simpleDateString).add(JSON.toJSON(reviewRecord));
                   }
               });
    }

}
