package stb.com.testmapapp.preferences;

import android.preference.PreferenceActivity;

import java.util.List;

import stb.com.testmapapp.R;

public class AppPreferences extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return UserPreferencesFragment.class.getName().equals(fragmentName);
    }
}