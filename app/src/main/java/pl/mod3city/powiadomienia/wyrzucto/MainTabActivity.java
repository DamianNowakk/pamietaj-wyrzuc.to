package pl.mod3city.powiadomienia.wyrzucto;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainTabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String[] tabs = {"Zwykłe śmieci", "Wystawki", "Jak segregować"};

    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifID = 33;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        final Context context = getApplicationContext();
/*        //Dymek z napisem

        CharSequence text = "Witaj! Aby odswieżyć widok naciśnij przycisk synchronizacji.";
        int duration = Toast.LENGTH_LONG;
        duration++;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        //Koniec dymku*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //setAlarm();
       //startAlarmManager();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Przycisk do pobierania danych ze stony bihapi
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "Pobieram dane z BIHAPI", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                //Uzyskanie dostępu do menadźera połączeń internetowych i sprwadzenie czy użytkownika
                //ma połaczenie z internetem
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (!isConnected) {
                    Snackbar.make(view, "Brak połączenia z internetem", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    //Po naciśnięciu różowego przycisku odświeżane są dane BIHAPI - zwykłe śmieci
                    RestClient.getInstance().pobierzJsonaOdpadyMokreSucheZmieszaneDanaUlica(new JsonResponse() {
                        //Dzięki temu pieknemu zabiegowi, po pobraniu danych z Resta zostanie wywowołana poniższa metoda
                        @Override
                        public void onJsonResponse(boolean success, JSONObject response) {
                            //Tu możemy parsować Json lub przekazać go do klasy JsonParser do dalszej obróbki
                            Log.i("mainActivity", response.toString());
                            //Wywołanie parsowania

                            JSONParser parser = new JSONParser();

                            if (success) {
                                parser.parseJSONtoArray(context, response);
                                //Przeładowanie widoku po pobraniu danych z BIHAPI
                                Fragment currentFragment = mSectionsPagerAdapter.getItem(0);
                                FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                                tr.detach(currentFragment);
                                tr.attach(currentFragment);
                                tr.commit();

                            } else {
                                //Serwer zwrócił błąd
                                Snackbar.make(view, "Brak danych do pobrania dla wywozu śmieci. Sprawdź nazwę ulicy w ustawieniach.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                 /*
                                 CharSequence text1 = "Brak danych do pobrania. Sprawdź nazwę ulicy w ustawieniach.";
                                int duration2 = Toast.LENGTH_LONG;
                                Toast brakDanych = Toast.makeText(context, text1, duration2);
                                brakDanych.show();
                                */
                            }
                        }
                    }, getBaseContext());

                 /*   //Wystawki
                    RestClient.getInstance().pobierzJsonaWystawki(new JsonResponse() {
                        //Dzięki temu pieknemu zabiegowi, po pobraniu danych z Resta zostanie wywowołana poniższa metoda
                        @Override
                        public void onJsonResponse(boolean success, JSONObject response) {
                            //Tu możemy parsować Json lub przekazać go do klasy JsonParser do dalszej obróbki
                            Log.i("mainActivity", response.toString());
                            //Wywołanie parsowania

                            JSONParser parser = new JSONParser();

                            if (success) {
                                //Nowe parsowanie

                            } else {
                                //Serwer zwrócił błąd
                                Snackbar.make(view, "Brak danych do pobrania dla wystawek. Sprawdź nazwę ulicy w ustawieniach.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                 *//*
                                 CharSequence text1 = "Brak danych do pobrania. Sprawdź nazwę ulicy w ustawieniach.";
                                int duration2 = Toast.LENGTH_LONG;
                                Toast brakDanych = Toast.makeText(context, text1, duration2);
                                brakDanych.show();
                                *//*
                            }
                        }
                    }, getBaseContext());*/
                }
            }
        });

    }

    /*@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification()
    {
        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText("New text")
                .setTicker("Alert new message")
                .setSmallIcon(R.drawable.ic_suche_icon);

        Intent moreInfoIntent = new Intent(this, MoreInfoNotification.class);

        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);

        tStackBuilder.addParentStack(MoreInfoNotification.class);

        tStackBuilder.addNextIntent(moreInfoIntent);

        PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notifID, notificBuilder.build());

        isNotificActive = true;
    }

    public void stopNotification()
    {
        if(isNotificActive)
        {
            notificationManager.cancel(notifID);
        }
    }*/

    public void setAlarm()
    {
        Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;
        Intent alertIntent = new Intent(this, AlertReceiver.class);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alertIntent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, pi);
    }

    public void startAlarmManager()
    {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.DAY_OF_MONTH, 2);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);

        Intent dialogIntent = new Intent(getBaseContext(), AlertReceiver.class);

        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, dialogIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()- System.currentTimeMillis(), 10000, pendingIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_OK);

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private GlownyFragment glo;
        private WystawkiFragment wys;
        private SegregowanieFragment seg;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            seg = new SegregowanieFragment();
            wys = new WystawkiFragment();
            glo = new GlownyFragment();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return glo;
            } else if (position == 1) {
                return wys;
            } else if (position == 2) {
                return seg;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}