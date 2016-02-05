package stb.com.testmapapp.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.text.InputType;

import stb.com.testmapapp.R;

public class UserPreferencesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private PreferenceUpdater preferenceUpdater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUpdater = new PreferenceUpdater(getPreferenceManager());
        addPreferencesFromResource(R.xml.user_preferences);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        preferenceUpdater.update(findPreference(key));
    }

    @Override
    public void onResume() {
        super.onResume();
        preferenceUpdater.update(getPreferenceScreen());
    }
}
