package pl.mod3city.powiadomienia.wyrzucto.activities;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.mod3city.powiadomienia.wyrzucto.MokreReceiver;
import pl.mod3city.powiadomienia.wyrzucto.R;
import pl.mod3city.powiadomienia.wyrzucto.SucheReceiver;
import pl.mod3city.powiadomienia.wyrzucto.api.JSONParser;
import pl.mod3city.powiadomienia.wyrzucto.res.rodzajSmieci;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    SharedPreferences sharedPreferences;
    SharedPreferences prefs;
    private PreferenceChangeListener mPreferenceListener = null; // Preference change listener

    AlarmManager[] alarmManagerSuche=new AlarmManager[24];
    ArrayList<PendingIntent> intentArraySuche = new ArrayList<PendingIntent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferenceListener = new PreferenceChangeListener();
        prefs.registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) mPreferenceListener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

            if(prefs.getBoolean("POWIADOMIENIA_WYSTAWKI",false) && key.equals("POWIADOMIENIA_WYSTAWKI")) {

            }
            else if(!prefs.getBoolean("POWIADOMIENIA_WYSTAWKI",false) && key.equals("POWIADOMIENIA_WYSTAWKI")){

            }
            if(prefs.getBoolean("POWIADOMIENIA_WYWOZ_SUCHE",false) && key.equals("POWIADOMIENIA_WYWOZ_SUCHE")) {
                powiadomieniaSuche();
            }
            else if(!prefs.getBoolean("POWIADOMIENIA_WYWOZ_SUCHE",false) && key.equals("POWIADOMIENIA_WYWOZ_SUCHE")) {
                Intent dialogIntent = new Intent(getBaseContext(), SucheReceiver.class);
                int iloscPowiadomienSuchych = sharedPreferences.getInt("Suche", 0);
                powiadomieniaDestroy(dialogIntent, iloscPowiadomienSuchych);
            }
            if(prefs.getBoolean("POWIADOMIENIA_WYWOZ_MOKRE",false) && key.equals("POWIADOMIENIA_WYWOZ_MOKRE")) {

            }
            else if(!prefs.getBoolean("POWIADOMIENIA_WYWOZ_MOKRE",false) && key.equals("POWIADOMIENIA_WYWOZ_MOKRE")) {

            }
            if(prefs.getBoolean("POWIADOMIENIA_WYWOZ_ZMIESZANE",false) && key.equals("POWIADOMIENIA_WYWOZ_ZMIESZANE")) {

            }
            else if(!prefs.getBoolean("POWIADOMIENIA_WYWOZ_ZMIESZANE",false) && key.equals("POWIADOMIENIA_WYWOZ_ZMIESZANE")) {

            }
            if(key.equals("POWIADOMIENIA_WYSTAWKI_CZAS")) {
                //powiadomieniaSuche();
            }
            if(key.equals("POWIADOMIENIA_SMIECI_SUCHE")) {
                //powiadomieniaSuche();
            }
            if(key.equals("POWIADOMIENIA_SMIECI_MOKRE")) {
                //powiadomieniaSuche();
            }
            if(key.equals("POWIADOMIENIA_SMIECI_ZMIESZANE")) {
                //powiadomieniaSuche();
            }
        }
    }

    public void powiadomieniaSuche() {
        ArrayList<String> dni = JSONParser.getInstance().najblizszeDniSmieci(rodzajSmieci.SUCHE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.PM);

        SaveInt("Suche", 2);
        for(int i = 0; i < 2; i++)
        {
            Intent dialogIntent = new Intent(getBaseContext(), SucheReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

            alarmManagerSuche[i] = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManagerSuche[i].setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+i*2000, 6000, pendingIntent);

            intentArraySuche.add(pendingIntent);
            //alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        }
    }
    public void powiadomieniaDestroy(Intent dialogIntent, int iloscPowiadomienSuchych) {


        for(int i=0; i<iloscPowiadomienSuchych;i++)
        {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, dialogIntent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
            alarmManagerSuche[i] = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManagerSuche[i].cancel(pendingIntent);
        }
    }

    public void SaveInt(String key, int value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof SwitchPreference) {

            }
            else if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("ULICA_NAZWA"));
            bindPreferenceSummaryToValue(findPreference("ULICA_NUMER"));
            bindPreferenceSummaryToValue(findPreference("RODZAJ_ZABUDOWY"));
            bindPreferenceSummaryToValue(findPreference("DZIELNICA"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("POWIADOMIENIA_WYSTAWKI_CZAS"));
            bindPreferenceSummaryToValue(findPreference("POWIADOMIENIA_SMIECI_SUCHE"));
            bindPreferenceSummaryToValue(findPreference("POWIADOMIENIA_SMIECI_MOKRE"));
            bindPreferenceSummaryToValue(findPreference("POWIADOMIENIA_SMIECI_ZMIESZANE"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
