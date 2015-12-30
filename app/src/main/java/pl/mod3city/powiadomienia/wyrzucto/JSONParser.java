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

            String suche = new String();
            String mokre = new String();
            String zmieszane = new String();

            for (String dzien : tab) {
                if(dzien.contains("Suche")){
                    suche += dzien.substring(6);
                    suche += "\n";
                }
                else if(dzien.contains("Mokre")){
                    mokre += dzien.substring(6);
                    mokre += "\n";
                }
                else if(dzien.contains("Zmieszane")){
                    zmieszane += dzien.substring(10);
                    zmieszane += "\n";
                }
            }




        } catch (Exception e) {
            Log.i("blad",e.toString());
        }
    }

    public void doStaffWithRest(){

    }
}
