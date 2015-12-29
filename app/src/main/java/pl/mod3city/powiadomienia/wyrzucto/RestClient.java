package pl.mod3city.powiadomienia.wyrzucto;
import com.loopj.android.http.*;

import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;


public class RestClient {
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
        client.setTimeout(DEFAULT_TIMEOUT);
    }


    //Stara metoda, która będzie wywalona w przyszłości. Służy tylko jak przykład

    public void getJson(final JsonResponse callback) {
        //Zabawa z parametrami do zapytania get
            RequestParams params = new RequestParams();
            params.put("resource", "bc14ab19-621d-4607-9689-90a61d13ee4b");
            params.put("filters","{\"Ulica_nazwa_skrócona\":\"Akwenowa\"}");
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
