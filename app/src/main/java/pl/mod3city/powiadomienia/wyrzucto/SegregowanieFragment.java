package pl.mod3city.powiadomienia.wyrzucto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SegregowanieFragment extends Fragment {


    public SegregowanieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_segregowanie, container, false);
        WebView webView = (WebView) rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        String pdf = "https://czystemiasto.gdansk.pl/ZDiZGdanskFiles/file/broszura_jaksegregowac_20140507.pdf";
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        return rootView;
    }

}
