package pl.mod3city.powiadomienia.wyrzucto;
import com.loopj.android.http.*;

import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;



public class RestClient{
    private static RestClient ourInstance;

    private AsyncHttpClient client;

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
        client.setTimeout(1000);
        //Nazwa użytkownika i hasło do połączenia z serwerem, autoryzacja
        client.setBasicAuth("ciecimi", "v78moUzE");
    }

    public void getJson(final JsonResponse callback){
        client.get("https://api.bihapi.pl/dane/gdansk?resource=bc14ab19-621d-4607-9689-90a61d13ee4b&filters={%22Ulica_nazwa_skr%C3%B3cona%22:%20%22Akwenowa%22}", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.i("Debuggin", "getting response");
                try {
                    Log.i("Debuggin", "Response: " + json.toString());
                    callback.onJsonResponse(true, json);
                } catch (Exception e) {
                    Log.i("Debuggin", "error");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                    Log.i("Debuggin", "FAILURE");
                try {
                    JSONObject jObj = new JSONObject(response);
                    callback.onJsonResponse(false, jObj);
                }catch (JSONException e){
                    Log.e("Exception", "JsonException" + e.toString());
                }
            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
