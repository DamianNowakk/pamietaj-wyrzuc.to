package pl.mod3city.powiadomienia.wyrzucto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Dymek z napisem
        final Context context = getApplicationContext();
        CharSequence text = "Witaj!Aby odswieżyć widok naciśnij przycisk synchronizacji.";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        //Koniec dymku

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Pobieram dane z BIHAPI", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //Po naciśnięciu różowego przycisku odświeżane są dane BIHAPI
                RestClient.getInstance().getJson(new JsonResponse() {
                    //Dzięki temu pieknemu zabiegowi, po pobraniu danych z Resta zostanie wywowołana poniższa metoda
                    @Override
                    public void onJsonResponse(boolean success, JSONObject response) {
                        //Tu możemy parsować Json lub przekazać go do klasy JsonParser do dalszej obróbki
                        Log.i("mainActivity", response.toString());
                        //Wywołanie parsowania
                        JSONParser parser = new JSONParser();
                        if (success) {
                            parser.parseJSONtoArray(context, response);

                        }

                        //wyświetlenie dni

                    }
                });
            }
        });







    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_OK);

        }

        return super.onOptionsItemSelected(item);
    }
}
