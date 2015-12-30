package pl.mod3city.powiadomienia.wyrzucto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

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
            for(int i=0; i<licznik; i++){
                tabelaDni[i] = tab[i];
            }

            String suche = new String();
            String mokre = new String();
            String zmieszane = new String();

            for (String dzien : tabelaDni) {
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
            File file = new File("suche.txt");
            PrintWriter sucheZapis = new PrintWriter(file);
            sucheZapis.print(suche);
            sucheZapis.close();

            PrintWriter mokreZapis = new PrintWriter("mokre.txt");
            mokreZapis.print(mokre);
            mokreZapis.close();
            
            PrintWriter zmieszaneZapis = new PrintWriter("zmieszane.txt");
            zmieszaneZapis.print(zmieszane);
            zmieszaneZapis.close();

        } catch (Exception e) {
            Log.i("blad",e.toString());
        }
    }

    public void doStaffWithRest(){

    }
}
