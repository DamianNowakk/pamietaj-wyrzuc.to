package pl.mod3city.powiadomienia.wyrzucto.api;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
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
    private ArrayList<String> listaWystawek = new ArrayList<String>();


    JSONParser (){
    }

    public static JSONParser getInstance() {
        if(ourInstance == null) {
            ourInstance = new JSONParser();
        }
        return ourInstance;
    }

    public void parsowanieOdpadow(Context context, JSONObject response) {

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
            if(!listaWystawek.isEmpty()){
                listaWystawek.clear();
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

    public void parsowanieWystawek(Context context, JSONObject response) {
        try {
            JSONArray arr = response.getJSONObject("results").getJSONArray("properties");
            String datyOdbioru= new String();
            for (int i = 0; i < arr.length(); i++) {
                if (arr.getJSONObject(i).getString("key").equals("Dzień_odbioru")) {
                    datyOdbioru = arr.getJSONObject(i).getString("value");
                    break;
                }
            }

            String tmp = new String();
            for(int i=0; i<datyOdbioru.length(); i++){
                String znak = datyOdbioru.substring(i,i+1);
                if(znak.equals(':')){
                    tmp+='\n';
                }
                else{
                    tmp+=znak;
                }
            }

            datyOdbioru = datyOdbioru.replace(':', '\n');

            File fileWystawki = new File(context.getFilesDir(), "wystawki.txt");
            PrintWriter wystawkiZapis = new PrintWriter(fileWystawki);
            wystawkiZapis.print(datyOdbioru);
            wystawkiZapis.close();


        }catch (Exception e){
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
            if(!mokre.isEmpty()){
                mokreKazdy.setText(mokre);
                mokreNastepne.setText(najblizszyDzien(listaMokrych));
            }
            else{
                mokreKazdy.setText("Brak danych");
                mokreNastepne.setText("Brak danych");
            }


            File fileSuche = new File(context.getFilesDir(), "suche.txt");
            Scanner sucheSkaner = new Scanner(fileSuche);
            while(sucheSkaner.hasNext()){
                String dzien = zamianaDnia(sucheSkaner.next());
                suche += dzien;
                suche += " ";
                listaSuchych.add(dzien);
            }
            if(!suche.isEmpty()){
                sucheKazdy.setText(suche);
                sucheNastepne.setText(najblizszyDzien(listaSuchych));
            }
            else{
                sucheKazdy.setText("Brak danych");
                sucheNastepne.setText("Brak danych");
            }


            File fileZmieszane = new File(context.getFilesDir(), "zmieszane.txt");
            Scanner zmieszaneSkaner = new Scanner(fileZmieszane);
            while(zmieszaneSkaner.hasNext()){
                String dzien = zamianaDnia(zmieszaneSkaner.next());
                zmieszane += dzien;
                zmieszane += " ";
                listaZmieszanych.add(dzien);
            }
            if(!zmieszane.isEmpty()){
                zmieszaneKazdy.setText(zmieszane);
                zmieszaneNastepne.setText(najblizszyDzien(listaZmieszanych));
            }
            else{
                zmieszaneKazdy.setText(" ");
                zmieszaneNastepne.setText(" ");
            }


        }catch(Exception e){
            Log.i("blad", e.toString());
        }




    }

    public void wyswietlanieWystawek(Context context,View view){
        TextView wystawkaNastepne = (TextView) view.findViewById(R.id.najblizszaWystawka);

        String wystawki = new String();
        try {
            File fileWystawki = new File(context.getFilesDir(), "wystawki.txt");
            Scanner wystawkiSkaner = new Scanner(fileWystawki);
            while (wystawkiSkaner.hasNext()) {
                String dzien = wystawkiSkaner.next();
                wystawki += dzien;
                wystawki += " ";
                listaWystawek.add(dzien);
            }
            if (!wystawki.isEmpty()) {
                wystawkaNastepne.setText(najblizszaWystawka(listaWystawek));
            } else {
                wystawkaNastepne.setText("Brak danych");
            }
        }catch (Exception e){
            Log.i("blad", e.toString());
        }
    }

    public String najblizszaWystawka(ArrayList<String> lista){
        Date dateNow = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);

        int dzien = calendar.get(Calendar.DATE);
        int miesiac = calendar.get(Calendar.MONTH);

        int[] dni = new int[lista.size()];
        int[] miesiace = new int[lista.size()];

        for(int i=0; i<lista.size(); i++){
            dni[i] = Integer.parseInt(lista.get(i).substring(0,2));
        }

        /*
        for(int i=0; i<lista.size(); i++){
            miesiace[i] = Integer.parseInt(lista.get(i).substring(2));
            miesiace[i]--;
        }
        */
        miesiace[0] = 0;
        miesiace[1] = 4;
        miesiace[2] = 7;
        miesiace[3] = 11;

        while(true){
            while(miesiac<13){
                while(dzien<32){
                    for(int i=0; i<miesiace.length; i++){
                        if(miesiace[i] == miesiac){
                            for(int j=0; j<dni.length; j++){
                                if(dni[j] == dzien){
                                    String zwrot;
                                    if(miesiac<10)
                                        zwrot = dzien + "." + "0" + (miesiac+1);
                                    else
                                        zwrot = dzien + "." + (miesiac+1);
                                    return zwrot;
                                }
                            }
                        }
                    }
                    dzien++;
                }
                dzien = dzien % 32;
                miesiac++;
            }
            miesiac = miesiac%13;
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
                    int dayNow = calendar.get(Calendar.DAY_OF_WEEK);
                    if(dayNow == tab[i])
                        return "Dzisiaj";
                    else if(dayNow!=7 && dayNow+1 == tab[i])
                        return "Jutro";
                    else if(dayNow==7 && tab[i] == 1)
                        return "Jutro";
                    else
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
    public ArrayList<Integer> najblizszeDniSmieci(rodzajSmieci rodz) {

        ArrayList<Integer> dni = new ArrayList<Integer>();
        if(rodz == rodzajSmieci.MOKRE){
            for (int i = 0; i < listaMokrych.size(); i++)
            {
                dni.add(zamianaDniaInt(listaMokrych.get(i)));
            }

        }else if(rodz == rodzajSmieci.SUCHE){
            for (int i = 0; i < listaSuchych.size(); i++)
            {
                dni.add(zamianaDniaInt(listaSuchych.get(i)));
            }
        }else{
            for (int i = 0; i < listaZmieszanych.size(); i++)
            {
                dni.add(zamianaDniaInt(listaZmieszanych.get(i)));
            }
        }
        return dni;

    }


}
