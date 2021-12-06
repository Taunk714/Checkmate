package com.teamred.checkmate;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class App extends Application {
    public static final String CHANNEL_1_ID = "noteReview";
    public static final String CHANNEL_2_ID = "test";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel note_review = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Note Review",
                    NotificationManager.IMPORTANCE_HIGH
            );
            note_review.setDescription("Time to look at your note!");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Test Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("Or not.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(note_review);
            manager.createNotificationChannel(channel2);
        }
    }
}
