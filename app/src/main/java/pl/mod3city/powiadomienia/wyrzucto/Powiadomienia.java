package pl.mod3city.powiadomienia.wyrzucto;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by Baniek on 2016-01-03.
 */
public class Powiadomienia{
    private static Powiadomienia ourInstance;
    private static final int idSuche = 100;
    private static final int idMokre = 200;
    private static final int idZmieszane = 300;

    AlarmManager alarmManager;

    public static Powiadomienia getInstance() {
        if(ourInstance == null) {
            ourInstance = new Powiadomienia();
        }
        return ourInstance;
    }

    public void powiadomieniaSuche(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //ArrayList<String> dni = JSONParser.getInstance().najblizszeDniSmieci(rodzajSmieci.SUCHE);

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        //calendar.set(Calendar.YEAR, 2016);
        //calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 19);
        calendar.set(Calendar.SECOND, 0);
        //calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.setTimeInMillis(System.currentTimeMillis());

        SaveInt("Suche", 1, context);

        Intent dialogIntent = new Intent(context, SucheReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idSuche+0, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5000, pendingIntent);
    }

    public void powiadomieniaMokre(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SaveInt("Mokre", 1, context);

        Intent dialogIntent = new Intent(context, MokreReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idMokre+0, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pendingIntent);
    }

    public void powiadomieniaZmieszane(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


        SaveInt("Zmieszane", 1, context);

        Intent dialogIntent = new Intent(context, ZmieszaneReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idZmieszane+0, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pendingIntent);
    }

    public void powiadomieniaDestroySuche(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intent dialogIntent = new Intent(context, SucheReceiver.class);
        int iloscPowiadomienSuchych = sharedPreferences.getInt("Suche", 0);
        powiadomieniaDestroy(context, dialogIntent, iloscPowiadomienSuchych, idSuche);
        SaveInt("Suche", 0, context);
    }

    public void powiadomieniaDestroyMokre(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intent dialogIntent = new Intent(context, MokreReceiver.class);
        int iloscPowiadomienMokrych = sharedPreferences.getInt("Mokre", 0);
        powiadomieniaDestroy(context, dialogIntent, iloscPowiadomienMokrych, idMokre);
        SaveInt("Mokre", 0, context);
    }

    public void powiadomieniaDestroyZmieszane(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intent dialogIntent = new Intent(context, ZmieszaneReceiver.class);
        int iloscPowiadomienZmieszane = sharedPreferences.getInt("Zmieszane", 0);
        powiadomieniaDestroy(context, dialogIntent, iloscPowiadomienZmieszane, idZmieszane);
        SaveInt("Zmieszane", 0, context);
    }

    private void powiadomieniaDestroy(Context context, Intent dialogIntent,int iloscPowiadomien, int id) {
        for(int i=0; i<iloscPowiadomien;i++)
        {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id + i, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    public void SaveInt(String key, int value, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
