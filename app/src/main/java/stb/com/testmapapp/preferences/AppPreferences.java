package stb.com.testmapapp.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppPreferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new UserPreferencesFragment()).commit();
    }

    // Over this to provide multiple pages of preferences
    //@Override
    //public void onBuildHeaders(List<Header> target) {
    //    loadHeadersFromResource(R.xml.headers_preference, target);
    //}

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return UserPreferencesFragment.class.getName().equals(fragmentName);
    }
}