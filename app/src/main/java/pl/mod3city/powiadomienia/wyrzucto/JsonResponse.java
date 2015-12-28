package pl.mod3city.powiadomienia.wyrzucto;

import org.json.JSONObject;

/**
 * Created by Mikołaj on 2015-12-21.
 */
public interface JsonResponse {
    public void onJsonResponse(boolean success, JSONObject response);
}
