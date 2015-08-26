package com.aut.android.danielorammicro_bloggingapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Administrator on 13/08/2015.
 */
public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
