package pl.mod3city.powiadomienia.wyrzucto;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class MainTabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Fragment mainFrag = new MainActivity();
    private String[] tabs = { "Zwykłe śmieci", "Wystawki", "Jak segregować" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        //Dymek z napisem
        final Context context = getApplicationContext();
        CharSequence text = "Witaj! Aby odswieżyć widok naciśnij przycisk synchronizacji.";
        int duration = Toast.LENGTH_LONG;
        duration++;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        //Koniec dymku

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Pobieram dane z BIHAPI", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                ConnectivityManager cm =
                        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected){
                    Snackbar.make(view, "Brak połączenia z internetem", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    //Po naciśnięciu różowego przycisku odświeżane są dane BIHAPI
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
                            } else {
                                CharSequence text1 = "Brak danych do pobrania. Sprawdź nazwę ulicy w ustawieniach.";
                                int duration2 = Toast.LENGTH_LONG;
                                Toast brakDanych = Toast.makeText(context, text1, duration2);
                                brakDanych.show();
                            }
                            //wyświetlenie dni
                            mainFrag = new MainActivity();
                        }
                    }, getBaseContext());
                }
            }
        });

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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0) {
                return mainFrag;
            }
            return PlaceholderFragment.newInstance(position + 1);
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

    public void wyswietlanieDni(Context context){
        String suche = new String();
        String mokre = new String();
        String zmieszane = new String();
        ArrayList<String> listaSuchych = new ArrayList<String>();
        ArrayList<String> listaMokrych = new ArrayList<String>();
        ArrayList<String> listaZmieszanych = new ArrayList<String>();

        TextView mokreKazdy = (TextView) findViewById(R.id.odbiorMokreKazdy);
        TextView mokreNastepne = (TextView) findViewById(R.id.odbiorMokreNastepne);

        TextView sucheKazdy = (TextView) findViewById(R.id.odbiorSucheKazdy);
        TextView sucheNastepne = (TextView) findViewById(R.id.odbiorSucheNastepne);

        TextView zmieszaneKazdy = (TextView) findViewById(R.id.odbiorZmieszaneKazdy);
        TextView zmieszaneNastepne = (TextView) findViewById(R.id.odbiorZmieszaneNastepne);

        try {
            File fileMokre = new File(context.getFilesDir(), "mokre.txt");
            Scanner mokreSkaner = new Scanner(fileMokre);
            while(mokreSkaner.hasNext()){
                String dzien = zamianaDnia(mokreSkaner.next());
                mokre += dzien;
                mokre += " ";
                listaMokrych.add(dzien);
            }
            mokreKazdy.setText(mokre);
            mokreNastepne.setText(najblizszyDzien(listaMokrych));

            File fileSuche = new File(context.getFilesDir(), "suche.txt");
            Scanner sucheSkaner = new Scanner(fileSuche);
            while(sucheSkaner.hasNext()){
                String dzien = zamianaDnia(sucheSkaner.next());
                suche += dzien;
                suche += " ";
                listaSuchych.add(dzien);
            }
            sucheKazdy.setText(suche);
            sucheNastepne.setText(najblizszyDzien(listaSuchych));

            File fileZmieszane = new File(context.getFilesDir(), "zmieszane.txt");
            Scanner zmieszaneSkaner = new Scanner(fileZmieszane);
            while(zmieszaneSkaner.hasNext()){
                String dzien = zamianaDnia(zmieszaneSkaner.next());
                zmieszane += dzien;
                zmieszane += " ";
                listaZmieszanych.add(dzien);
            }
            zmieszaneKazdy.setText(zmieszane);
            zmieszaneNastepne.setText(najblizszyDzien(listaZmieszanych));


        }catch(Exception e){
            Log.i("blad", e.toString());
        }




    }

    public String zamianaDnia(String dzien){
        if(dzien.equals("Pn"))
            return "Poniedziałek";
        else if(dzien.equals("Wt"))
            return "Wtorek";
        else if(dzien.equals("Śr"))
            return "Środa";
        else if(dzien.equals("Cz"))
            return "Czwartek";
        else if(dzien.equals("Pt"))
            return "Piątek";
        else if(dzien.equals("So"))
            return "Sobota";
        else if(dzien.equals("Nd"))
            return "Niedziela";
        else
            return "Błąd";

    }

    public Integer zamianaDniaInt(String dzien){
        if(dzien.equals("Poniedziałek"))
            return 2;
        else if(dzien.equals("Wtorek"))
            return 3;
        else if(dzien.equals("Środa"))
            return 4;
        else if(dzien.equals("Czwartek"))
            return 5;
        else if(dzien.equals("Piątek"))
            return 6;
        else if(dzien.equals("Sobota"))
            return 7;
        else if(dzien.equals("Niedziela"))
            return 1;
        else
            return 123;
    }

    public String zamianaDniaInt(Integer dzien){
        if(dzien == 2)
            return "Poniedziałek";
        else if(dzien == 3)
            return "Wtorek";
        else if(dzien == 4)
            return "Środa";
        else if(dzien == 5)
            return "Czwartek";
        else if(dzien == 6)
            return "Piątek";
        else if(dzien == 7)
            return "Sobota";
        else if(dzien == 1)
            return "Niedziela";
        else
            return "błąd";
    }

    public String najblizszyDzien(ArrayList<String> listaDni){
        Integer[] tab = new Integer[listaDni.size()];
        for(int i=0; i<listaDni.size(); i++){
            tab[i] = zamianaDniaInt(listaDni.get(i));
        }

        Date dateNow = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        int dayOfWeekNow = calendar.get(Calendar.DAY_OF_WEEK);

        while(true){
            dayOfWeekNow = dayOfWeekNow % 8;
            for(int i=0; i<tab.length; i++) {
                if(dayOfWeekNow == tab[i]){
                    return zamianaDniaInt(tab[i]);
                }
            }
            dayOfWeekNow++;
        }
    }

}
