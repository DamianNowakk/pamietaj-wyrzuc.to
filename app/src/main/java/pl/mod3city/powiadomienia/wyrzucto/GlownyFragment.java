package pl.mod3city.powiadomienia.wyrzucto;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class GlownyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main,container,false);
        wyswietlanieDni(getContext(),rootView);
        return rootView;
    }



    public void wyswietlanieDni(Context context,View view){
        String suche = new String();
        String mokre = new String();
        String zmieszane = new String();
        ArrayList<String> listaSuchych = new ArrayList<String>();
        ArrayList<String> listaMokrych = new ArrayList<String>();
        ArrayList<String> listaZmieszanych = new ArrayList<String>();

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

}
