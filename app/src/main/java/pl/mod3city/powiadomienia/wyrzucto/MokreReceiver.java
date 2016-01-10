package pl.mod3city.powiadomienia.wyrzucto;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import pl.mod3city.powiadomienia.wyrzucto.activities.MainTabActivity;

/**
 * Created by Baniek on 2016-01-01.
 */
public class MokreReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context,"Wyrzuc.to", "mokre", "Przypomnienie");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert)
    {
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainTabActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(msg)
                .setContentText(msgText)
                .setTicker(msgAlert)
                .setSmallIcon(R.drawable.ic_mokre_icon);

        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());

    }
}
