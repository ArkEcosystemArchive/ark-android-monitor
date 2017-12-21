package com.vrlcrypt.arkmonitor.scheduler;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.vrlcrypt.arkmonitor.MainActivity;
import com.vrlcrypt.arkmonitor.R;
import com.vrlcrypt.arkmonitor.models.Block;
import com.vrlcrypt.arkmonitor.models.Settings;
import com.vrlcrypt.arkmonitor.services.ArkService;
import com.vrlcrypt.arkmonitor.services.RequestListener;
import com.vrlcrypt.arkmonitor.utils.Utils;

public class ForgingSchedulingService extends IntentService {
    private static final String TAG = ForgingSchedulingService.class.getSimpleName();

    public ForgingSchedulingService() {
        super(TAG);
    }

    private static final int NOTIFICATION_ID = 1;
    private static final long FIFTEEN_MINUTES_IN_MILLISECONDS = 900000;

    private Intent mIntent;

    @Override
    protected void onHandleIntent(Intent intent) {
        this.mIntent = intent;
        loadLastForgedBlock();
    }

    private void loadLastForgedBlock() {
        Settings settings = Utils.getSettings(getApplicationContext());

        ArkService.getInstance().requestLastBlockForged(settings, new RequestListener<Block>() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, e.getMessage());
                ForgingAlarmReceiver.completeWakefulIntent(mIntent);
            }

            @Override
            public void onResponse(final Block block) {
                if (block != null && block.getTimestamp() > 0) {
                    CharSequence timeAgo = Utils.getTimeAgo(block.getTimestamp());
                    long diff = Utils.getTimeInMillisUntilNow(block.getTimestamp());

                    boolean notificationWithAlarm = diff > FIFTEEN_MINUTES_IN_MILLISECONDS;
                    sendNotification(timeAgo.toString(), notificationWithAlarm);

                    ForgingAlarmReceiver.completeWakefulIntent(mIntent);
                }
            }
        });
    }

    private void sendNotification(String msg, boolean warning) {
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.last_block_forged))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setAutoCancel(true)
                        .setContentText(msg);

        if (warning) {
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000});
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mBuilder.setSound(uri);
            mBuilder.setColor(Color.RED);
            mBuilder.setLights(Color.RED, 1, 1);
        } else {
            int colorId = ContextCompat.getColor(this, R.color.colorPrimary);
            mBuilder.setColor(colorId);
            mBuilder.setLights(Color.BLUE, 1, 1);
        }

        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
