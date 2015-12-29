package pl.mod3city.powiadomienia.wyrzucto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mikołaj on 2015-12-21.
 */
public class JSONParser {
    JSONParser (){
    }

    public void parseJSONtoArray(JSONObject response) {

        try {
            JSONArray arr = response.getJSONObject("results").getJSONArray("properties");
            String[] tab = new String[arr.length()];
            int licznik=0;
            for (int i = 0; i < arr.length(); i++) {
                if (arr.getJSONObject(i).getString("value").equals("x")) {
                    tab[licznik] = arr.getJSONObject(i).getString("key");
                    licznik++;
                }
            }
            String[] tabelaDni = new String[licznik];

            //Tu się dzieją dziwne rzeczy. Tego fora musiałem zrobić, bo wcześniej było tabelaDni =tab co powodowało, że
            //ten for String dzien :tabelaDni odczytywał nulla i chciał porównywać z suche,mokre itd. Może będziesz wiedział co chciałeś tu dokładnie zrobić
            //i to poprawisz
            for(int i = 0; i<licznik; i++){
                tabelaDni[i] = tab[i];
            }

            int liczSuche =0, liczMokre =0, liczZmieszane = 0;
            for (String dzien : tabelaDni) {
                if(dzien.contains("Suche"))
                    liczSuche++;
                else if(dzien.contains("Mokre"))
                    liczMokre++;
                else if(dzien.contains("Zmieszane"))
                    liczZmieszane++;
            }
            String[] tabSuche = new String[liczSuche];
            String[] tabMokre = new String[liczMokre];
            String[] tabZmieszane = new String[liczZmieszane];

            liczSuche =0;
            liczMokre =0;
            liczZmieszane = 0;

            for (String dzien : tabelaDni) {
                if(dzien.contains("Suche")){
                    tabSuche[liczSuche] = dzien.substring(6);
                    liczSuche++;
                }
                else if(dzien.contains("Mokre")){
                    tabMokre[liczMokre] = dzien.substring(6);
                    liczMokre++;
                }
                else if(dzien.contains("Zmieszane")){
                    tabZmieszane[liczZmieszane] = dzien.substring(10);
                    liczZmieszane++;
                }
            }

        } catch (Exception e) {
            Log.i("blad",e.toString());
        }
    }

    public void doStaffWithRest(){

    }
}
