package pl.mod3city.powiadomienia.wyrzucto;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;

import pl.mod3city.powiadomienia.wyrzucto.api.JSONParser;
import pl.mod3city.powiadomienia.wyrzucto.res.rodzajSmieci;

/**
 * Created by Baniek on 2016-01-03.
 */
public class Powiadomienia{
    private static Powiadomienia ourInstance;
    private static final int idSuche = 100;
    private static final int idMokre = 200;
    private static final int idZmieszane = 300;
    private static final int idWystawki = 400;

    AlarmManager alarmManager;

    public static Powiadomienia getInstance() {
        if(ourInstance == null) {
            ourInstance = new Powiadomienia();
        }
        return ourInstance;
    }

    public void powiadomieniaSuche(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ileDniPrzedPowiadomicString = sharedPreferences.getString("POWIADOMIENIA_SMIECI_SUCHE", "0");
        int ileDniPrzedPowiadomic =  Integer.parseInt(ileDniPrzedPowiadomicString);
        ArrayList<Integer> dni = JSONParser.getInstance().najblizszeDniSmieci(rodzajSmieci.SUCHE);
        SaveInt("Suche", dni.size(), context);
        for(int i=0; i < dni.size(); i++) {
            Calendar calendar = rozpoczeciePowiadomienia(dni.get(i), ileDniPrzedPowiadomic);

            Intent dialogIntent = new Intent(context, SucheReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idSuche + i, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  AlarmManager.INTERVAL_DAY *7, pendingIntent);
        }
        dni.clear();
    }

    public void powiadomieniaMokre(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ileDniPrzedPowiadomicString = sharedPreferences.getString("POWIADOMIENIA_SMIECI_MOKRE", "0");
        int ileDniPrzedPowiadomic =  Integer.parseInt(ileDniPrzedPowiadomicString);
        ArrayList<Integer> dni = JSONParser.getInstance().najblizszeDniSmieci(rodzajSmieci.MOKRE);

        SaveInt("Mokre", dni.size(), context);
        for(int i=0; i < dni.size(); i++) {
            Calendar calendar = rozpoczeciePowiadomienia(dni.get(i), ileDniPrzedPowiadomic);

            Intent dialogIntent = new Intent(context, MokreReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idMokre + i, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY *7, pendingIntent);
        }
        dni.clear();
    }

    public void powiadomieniaZmieszane(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ileDniPrzedPowiadomicString = sharedPreferences.getString("POWIADOMIENIA_SMIECI_ZMIESZANE", "0");
        int ileDniPrzedPowiadomic =  Integer.parseInt(ileDniPrzedPowiadomicString);
        ArrayList<Integer> dni = JSONParser.getInstance().najblizszeDniSmieci(rodzajSmieci.ZMIESZANE);

        SaveInt("Zmieszane",  dni.size(), context);
        for(int i=0; i < dni.size(); i++) {
            Calendar calendar = rozpoczeciePowiadomienia(dni.get(i), ileDniPrzedPowiadomic);

            Intent dialogIntent = new Intent(context, ZmieszaneReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idZmieszane + i, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }
        dni.clear();
    }

    public void powiadomieniaWystawki(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ileDniPrzedPowiadomicString = sharedPreferences.getString("POWIADOMIENIA_WYSTAWKI_CZAS", "0");
        int ileDniPrzedPowiadomic =  Integer.parseInt(ileDniPrzedPowiadomicString);
        ArrayList<String> dni = JSONParser.getInstance().najblizszeWystawki();
        int iloscPowiadomienWystawki = sharedPreferences.getInt("Wystawki", 0);
        if(iloscPowiadomienWystawki == 0) {
            SaveInt("Wystawki", dni.size(), context);
            for (int i = 0; i < dni.size(); i++) {
                String[] podzial = dni.get(i).split("\\.");
                //podzial[0]; //dni
                //podzial[1]; //miesiace

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.DATE, Integer.parseInt(podzial[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(podzial[1])-1);
                //calendar.set(Calendar.YEAR, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 6);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                Intent dialogIntent = new Intent(context, WystawkiReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idWystawki + i, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            dni.clear();
        }
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

    public void powiadomieniaDestroyWystawki(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intent dialogIntent = new Intent(context, WystawkiReceiver.class);
        int iloscPowiadomienWystawki = sharedPreferences.getInt("Wystawki", 0);
        powiadomieniaDestroy(context, dialogIntent, iloscPowiadomienWystawki, idWystawki);
        SaveInt("Wystawki", 0, context);
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

    public Calendar rozpoczeciePowiadomienia(int dzienTygodnia, int ileDniPrzedPowiadomic)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        while(dzienTygodnia != calendar.get(Calendar.DAY_OF_WEEK))
        {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.add(Calendar.DATE, -ileDniPrzedPowiadomic);
        if(calendar.getTimeInMillis() < System.currentTimeMillis())
        {
            calendar.add(Calendar.DATE, 7);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }
}
