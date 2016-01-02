package pl.mod3city.powiadomienia.wyrzucto.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.mod3city.powiadomienia.wyrzucto.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WystawkiFragment extends Fragment {


    public WystawkiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wystawki, container, false);
    }

}
