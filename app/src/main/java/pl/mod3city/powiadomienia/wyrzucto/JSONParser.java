package pl.mod3city.powiadomienia.wyrzucto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mikołaj on 2015-12-21.
 */
public class JSONParser {
    private JSONObject obiekt;

    JSONParser (){
        obiekt = new JSONObject();
    }

    public void parseJSONtoArray(JSONObject response) {

        try {
            obiekt = response;
            JSONArray arr = obiekt.getJSONArray("properties");
            String[] tab = new String[arr.length()];
            int licznik=0;
            for (int i = 0; i < arr.length(); i++) {
                if (arr.getJSONObject(i).getString("value").equals("x")) {
                    tab[licznik] = arr.getJSONObject(i).getString("key");
                    licznik++;
                }
            }
            String[] tabelaDni = new String[licznik];
            tabelaDni = tab;
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

        } catch (JSONException e) {
            Log.i("blad",e.toString());
        }
    }

    public void doStaffWithRest(){

    }
}
