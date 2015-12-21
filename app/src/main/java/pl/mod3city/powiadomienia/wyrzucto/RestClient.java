package pl.mod3city.powiadomienia.wyrzucto;
import com.loopj.android.http.*;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Mikołaj on 2015-12-20.
 */
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RestClient extends  Activity{
    private static RestClient ourInstance;

    private AsyncHttpClient client;
    private JSONObject dane = new JSONObject();

    public static RestClient getInstance() {
        if(ourInstance == null) {
            ourInstance = new RestClient();
        }
        return ourInstance;
    }
    private RestClient()  {
        client = new AsyncHttpClient();
        dane = new JSONObject();
        //Sprawdzanie i ustanawianie certfikatu
        try{
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null,null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }catch(Exception e){
            Log.d("tagSSL", "Błąd hurtowni certyfikatów");
        }

        //Nazwa użytkownika i hasło do połączenia z serwerem, autoryzacja
        client.setBasicAuth("ciecimi", "v78moUzE");
    }


    public void getSomething(){
        client.get("https://api.bihapi.pl/dane/gdansk?resource=bc14ab19-621d-4607-9689-90a61d13ee4b&filters={%22Ulica_nazwa_skr%C3%B3cona%22:%20%22Akwenowa%22}", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.i("Debuggin", "getting response");
                try {

                    int debuggin = Log.i("Debuggin", "Response: " + json.toString());
                    //Log.i("Debuggin", "Response: " + json.toString());
                        try {

                            JSONObject jsonnn = json.getJSONObject("results");
                            JSONArray arr = jsonnn.getJSONArray("properties");
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
                           TextView suche = (TextView)findViewById(R.id.odbiorSucheKazdy);
                            suche.setText(tabelaDni[1]);
                            TextView mokre = (TextView)findViewById(R.id.odbiorMokreKazdy);
                            mokre.setText(tabelaDni[2]);
                            ViewGroup vg = (ViewGroup) findViewById (R.id.fab);
                            vg.refreshDrawableState();
                            Log.i("Debuggin", "jestem tutaj");


                        } catch (JSONException e) {
                    }


                    dane = json;
                    getJson();
                } catch (Exception e) {
                    Log.i("Debuggin", "error");

                }

            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("Debuggin", "FAILURE");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


    public JSONObject getJson(){
        getSomething();
        Log.i("tag", dane.toString());
        return dane;
    }

}
