package stb.com.testmapapp.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import stb.com.testmapapp.dialog.Alert;

public class AppPreferences extends PreferenceActivity {

    public static final String SHOW_ALERT = "showAlert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new UserPreferencesFragment()).commit();

        if (getIntent().getBooleanExtra(SHOW_ALERT, false)) {
            new Alert(this)
                    .modal("New User", "Please configure your preferences to get started.")
                    .show();
        }
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