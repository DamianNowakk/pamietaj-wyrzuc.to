package pl.mod3city.powiadomienia.wyrzucto.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import pl.mod3city.powiadomienia.wyrzucto.api.JSONParser;
import pl.mod3city.powiadomienia.wyrzucto.R;
import pl.mod3city.powiadomienia.wyrzucto.res.rodzajSmieci;


public class GlownyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main,container,false);
        JSONParser.getInstance().wyswietlanieDni(getContext(), rootView);
        return rootView;
    }




}
