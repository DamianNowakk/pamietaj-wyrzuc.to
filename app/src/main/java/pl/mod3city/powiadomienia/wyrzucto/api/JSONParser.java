package pl.mod3city.powiadomienia.wyrzucto.api;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import pl.mod3city.powiadomienia.wyrzucto.R;
import pl.mod3city.powiadomienia.wyrzucto.res.rodzajSmieci;

/**
 * Created by Mikołaj on 2015-12-21.
 */
public class JSONParser {

    private static JSONParser ourInstance;
    private ArrayList<String> listaSuchych = new ArrayList<String>();
    private ArrayList<String> listaMokrych = new ArrayList<String>();
    private ArrayList<String> listaZmieszanych = new ArrayList<String>();

    JSONParser (){
    }

    public static JSONParser getInstance() {
        if(ourInstance == null) {
            ourInstance = new JSONParser();
        }
        return ourInstance;
    }

    public void parseJSONtoArray(Context context, JSONObject response) {

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
            if(!listaMokrych.isEmpty()){
                listaMokrych.clear();
            }
            if(!listaSuchych.isEmpty()){
                listaSuchych.clear();
            }
            if(!listaZmieszanych.isEmpty()){
                listaZmieszanych.clear();
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
            File fileSuche = new File(context.getFilesDir(), "suche.txt");
            PrintWriter sucheZapis = new PrintWriter(fileSuche);
            sucheZapis.print(suche);
            sucheZapis.close();

            File fileMokre = new File(context.getFilesDir(), "mokre.txt");
            PrintWriter mokreZapis = new PrintWriter(fileMokre);
            mokreZapis.print(mokre);
            mokreZapis.close();

            File fileZmieszane = new File(context.getFilesDir(), "zmieszane.txt");
            PrintWriter zmieszaneZapis = new PrintWriter(fileZmieszane);
            zmieszaneZapis.print(zmieszane);
            zmieszaneZapis.close();

        } catch (Exception e) {
            Log.i("blad",e.toString());
        }

    }

    public void wyswietlanieDni(Context context,View view){
        String suche = new String();
        String mokre = new String();
        String zmieszane = new String();


        TextView mokreKazdy = (TextView) view.findViewById(R.id.odbiorMokreKazdy);
        TextView mokreNastepne = (TextView) view.findViewById(R.id.odbiorMokreNastepne);

        TextView sucheKazdy = (TextView) view.findViewById(R.id.odbiorSucheKazdy);
        TextView sucheNastepne = (TextView) view.findViewById(R.id.odbiorSucheNastepne);

        TextView zmieszaneKazdy = (TextView) view.findViewById(R.id.odbiorZmieszaneKazdy);
        TextView zmieszaneNastepne = (TextView) view.findViewById(R.id.odbiorZmieszaneNastepne);

        try {
            File fileMokre = new File(context.getFilesDir(), "mokre.txt");
            Scanner mokreSkaner = new Scanner(fileMokre);
            while(mokreSkaner.hasNext()){
                String dzien = zamianaDnia(mokreSkaner.next());
                mokre += dzien;
                mokre += " ";
                listaMokrych.add(dzien);
            }
            mokreKazdy.setText(mokre);
            mokreNastepne.setText(najblizszyDzien(listaMokrych));

            File fileSuche = new File(context.getFilesDir(), "suche.txt");
            Scanner sucheSkaner = new Scanner(fileSuche);
            while(sucheSkaner.hasNext()){
                String dzien = zamianaDnia(sucheSkaner.next());
                suche += dzien;
                suche += " ";
                listaSuchych.add(dzien);
            }
            sucheKazdy.setText(suche);
            sucheNastepne.setText(najblizszyDzien(listaSuchych));

            File fileZmieszane = new File(context.getFilesDir(), "zmieszane.txt");
            Scanner zmieszaneSkaner = new Scanner(fileZmieszane);
            while(zmieszaneSkaner.hasNext()){
                String dzien = zamianaDnia(zmieszaneSkaner.next());
                zmieszane += dzien;
                zmieszane += " ";
                listaZmieszanych.add(dzien);
            }
            zmieszaneKazdy.setText(zmieszane);
            zmieszaneNastepne.setText(najblizszyDzien(listaZmieszanych));


        }catch(Exception e){
            Log.i("blad", e.toString());
        }




    }

    public String zamianaDnia(String dzien){
        if(dzien.equals("Pn"))
            return "Poniedziałek";
        else if(dzien.equals("Wt"))
            return "Wtorek";
        else if(dzien.equals("Śr"))
            return "Środa";
        else if(dzien.equals("Cz"))
            return "Czwartek";
        else if(dzien.equals("Pt"))
            return "Piątek";
        else if(dzien.equals("So"))
            return "Sobota";
        else if(dzien.equals("Nd"))
            return "Niedziela";
        else
            return "Błąd";

    }

    public Integer zamianaDniaInt(String dzien){
        if(dzien.equals("Poniedziałek"))
            return 2;
        else if(dzien.equals("Wtorek"))
            return 3;
        else if(dzien.equals("Środa"))
            return 4;
        else if(dzien.equals("Czwartek"))
            return 5;
        else if(dzien.equals("Piątek"))
            return 6;
        else if(dzien.equals("Sobota"))
            return 7;
        else if(dzien.equals("Niedziela"))
            return 1;
        else
            return 123;
    }

    public String zamianaDniaInt(Integer dzien){
        if(dzien == 2)
            return "Poniedziałek";
        else if(dzien == 3)
            return "Wtorek";
        else if(dzien == 4)
            return "Środa";
        else if(dzien == 5)
            return "Czwartek";
        else if(dzien == 6)
            return "Piątek";
        else if(dzien == 7)
            return "Sobota";
        else if(dzien == 1)
            return "Niedziela";
        else
            return "błąd";
    }

    public String najblizszyDzien(ArrayList<String> listaDni){
        Integer[] tab = new Integer[listaDni.size()];
        for(int i=0; i<listaDni.size(); i++){
            tab[i] = zamianaDniaInt(listaDni.get(i));
        }

        Date dateNow = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        int dayOfWeekNow = calendar.get(Calendar.DAY_OF_WEEK);

        while(true){
            dayOfWeekNow = dayOfWeekNow % 8;
            for(int i=0; i<tab.length; i++) {
                if(dayOfWeekNow == tab[i]){
                    return zamianaDniaInt(tab[i]);
                }
            }
            dayOfWeekNow++;
        }
    }

    public String najblizszyDzienSmieci(rodzajSmieci rodzaj){
        if(rodzaj == rodzajSmieci.MOKRE){
            return najblizszyDzien(listaMokrych);
        }else if(rodzaj == rodzajSmieci.SUCHE){
            return najblizszyDzien(listaSuchych);
        }else{
            return najblizszyDzien(listaZmieszanych);
        }
    }
    public ArrayList<String> najblizszeDniSmieci(rodzajSmieci rodz) {
    if(rodz == rodzajSmieci.MOKRE){
        return listaMokrych;

    }else if(rodz == rodzajSmieci.SUCHE){
        return listaSuchych;
    }else{
        return listaZmieszanych;
    }

    }


}
