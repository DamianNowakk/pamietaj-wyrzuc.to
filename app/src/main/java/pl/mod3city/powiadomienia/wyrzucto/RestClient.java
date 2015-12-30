package pl.mod3city.powiadomienia.wyrzucto;
import com.loopj.android.http.*;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;


public class RestClient{
    private static RestClient ourInstance;
    final int DEFAULT_TIMEOUT = 20 * 1000;
    private static AsyncHttpClient client;

    public static RestClient getInstance() {
        if(ourInstance == null) {
            ourInstance = new RestClient();
        }
        return ourInstance;
    }

    private RestClient()  {
        client = new AsyncHttpClient();
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
        client.setMaxRetriesAndTimeout(4,DEFAULT_TIMEOUT);
    }


    //Stara metoda, która będzie wywalona w przyszłości. Służy tylko jak przykład

    public void getJson(final JsonResponse callback) {
        //Zabawa z parametrami do zapytania get
            RequestParams params = new RequestParams();
            params.put("resource", "bc14ab19-621d-4607-9689-90a61d13ee4b");
            params.put("filters", "{\"Ulica_nazwa_skrócona\":\"Akwenowa\"}");

            client.get("https://api.bihapi.pl/dane/gdansk", params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    Log.i("Debuggin", "getting response");
                    try {
                        callback.onJsonResponse(true, json);
                    } catch (Exception e) {
                        Log.i("Debuggin", "Blad w metodzie onSuccess");

                    }
                }

                @Override
                public void onFailure(int statusCode,
                                      cz.msebera.android.httpclient.Header[] headers,
                                      java.lang.Throwable throwable,
                                      org.json.JSONArray errorResponse) {

                    try {
                        callback.onJsonResponse(false, errorResponse.getJSONObject(0));
                    } catch (Exception e) {
                        Log.i("tag", "Blad w onFailure JSON ARRAY");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (errorResponse != null) {
                        callback.onJsonResponse(false, errorResponse);
                    } else {
                        Log.i("blad", "onFailure zrocilo nulla");
                    }

                }

                @Override
                public void onRetry(int retryNo) {
                    Log.i("Uwaga", "Pobiernanie danych zostałao ponowione");
                }
            });

        }

    public void pobierzJsonaOdpadyMokreSucheZmieszaneDanaUlica(final JsonResponse callback,Context cont) {
        //Zabawa z parametrami do zapytania get
        /* bc14ab19-621d-4607-9689-90a61d13ee4b Sektor 1
           c75ef598-5336-4dfc-a7d9-244b7b4a5622 Sketor 2
           3bc012f3-32e4-4ec1-8089-3281366416fd Sektor 3
           0d9ee2df-adf6-4139-b5d8-e6d9db25822c Sektor 4
           2b9a3f11-5ec2-45f9-8975-27de8f703e2f Sektor 5
           906572d8-f573-4506-9a73-3cf1d8d1c993 Sektor 6
            Shared Preference DZIELNICE
        */
        RequestParams params = new RequestParams();
        //Odczytywanie z sharedprefences, potrzebny context
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(cont);
        String dzielnica = sharedPref.getString("DZIELNICA", "1");
        int dzielnicaInt =  Integer.parseInt(dzielnica);

        switch(dzielnicaInt){
            case 1:
                params.put("resource", "bc14ab19-621d-4607-9689-90a61d13ee4b");
                break;
            case 2:
                params.put("resource", "c75ef598-5336-4dfc-a7d9-244b7b4a5622");
                break;
            case 3:
                params.put("resource", "3bc012f3-32e4-4ec1-8089-3281366416fd");
                break;
            case 4:
                params.put("resource", "0d9ee2df-adf6-4139-b5d8-e6d9db25822c");
                break;
            case 5:
                params.put("resource", "2b9a3f11-5ec2-45f9-8975-27de8f703e2f");
                break;
            case 6:
                params.put("resource", "906572d8-f573-4506-9a73-3cf1d8d1c993");
                break;
            default:
                params.put("resource", "bc14ab19-621d-4607-9689-90a61d13ee4b");
                break;
        }

        String ulica = sharedPref.getString("ULICA_NAZWA", "Akwenowa");
        String filterUlica = new String("{\"Ulica_nazwa_skrócona\":\"");
        filterUlica +=  ulica + "\"}";

        params.put("filters", filterUlica);

        client.get("https://api.bihapi.pl/dane/gdansk", params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.i("Debuggin", "getting response");
                try {
                    callback.onJsonResponse(true, json);
                } catch (Exception e) {
                    Log.i("Debuggin", "Blad w metodzie onSuccess");

                }
            }

            @Override
            public void onFailure(int statusCode,
                                  cz.msebera.android.httpclient.Header[] headers,
                                  java.lang.Throwable throwable,
                                  org.json.JSONArray errorResponse) {

                try {
                    callback.onJsonResponse(false, errorResponse.getJSONObject(0));
                } catch (Exception e) {
                    Log.i("tag", "Blad w onFailure JSON ARRAY");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null) {
                    callback.onJsonResponse(false, errorResponse);
                } else {
                    Log.i("blad", "onFailure zrocilo nulla");
                }

            }

            @Override
            public void onRetry(int retryNo) {
                Log.i("Uwaga", "Pobiernanie danych zostałao ponowione");
            }
        });

    }

}
