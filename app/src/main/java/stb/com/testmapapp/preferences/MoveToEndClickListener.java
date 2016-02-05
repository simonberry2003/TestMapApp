package stb.com.testmapapp.preferences;

import android.preference.EditTextPreference;
import android.preference.Preference;

public class MoveToEndClickListener implements Preference.OnPreferenceClickListener {
    @Override
    public boolean onPreferenceClick(Preference preference) {
        EditTextPreference editPref = (EditTextPreference)preference;
        editPref.getEditText().setSelection(editPref.getText().length());
        return true;
    }
}
