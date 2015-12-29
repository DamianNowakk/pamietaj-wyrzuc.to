package pl.mod3city.powiadomienia.wyrzucto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mikołaj on 2015-12-21.
 */
public class JSONParser implements JsonResponse {
    private JSONObject obiekt;
    JSONParser (){
        obiekt = new JSONObject();
    }

    @Override
    public void onJsonResponse(boolean success, JSONObject response) {

        try {

            obiekt = response;
            JSONArray arr = response.getJSONArray("properties");
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
        } catch (JSONException e) {

        }
    }

    public void doStaffWithRest(){

    }
}
