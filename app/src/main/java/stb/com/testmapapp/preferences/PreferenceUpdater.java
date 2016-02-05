package stb.com.testmapapp.preferences;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.text.InputType;

import stb.com.testmapapp.util.Preconditions;

public class PreferenceUpdater {

    private final PreferenceManager preferenceManager;

    public PreferenceUpdater(PreferenceManager preferenceManager) {
        this.preferenceManager = Preconditions.checkNotNull(preferenceManager);
    }

    /**
     * Update the specified preference with either the value held in {@link PreferenceManager}
     * or the summary text.
     * @param preference the preference to update
     */
    public void update(Preference preference) {
        if (preference == null) {
            return;
        }

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
            return;
        }

        // Default preference to summary text
        String summary = preference.getSummary().toString();
        SharedPreferences sharedPrefs = preferenceManager.getSharedPreferences();
        String value = sharedPrefs.getString(preference.getKey(), summary);

        // If password and we have a value, mask it out
        if (!value.equals(summary) && isPassword(preference)) {
            value = value.replaceAll(".", "*");
        }
        preference.setSummary(value);
        preference.setOnPreferenceClickListener(new MoveToEndClickListener());
    }

    /**
     * Update all the preferences in the group with their setting in {@link PreferenceManager}
     * If there is no value, the preference summary text is displayed.
     * @param preferenceGroup the preference group
     */
    public void update(PreferenceGroup preferenceGroup) {
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); ++i) {
            Preference preference = preferenceGroup.getPreference(i);
            if (preference instanceof PreferenceGroup) {
                update((PreferenceGroup)preference);
            } else {
                update(preference);
            }
        }
    }

    private boolean isPassword(Preference preference) {
        boolean isPassword = false;
        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            int variation = (editTextPreference.getEditText().getInputType() & InputType.TYPE_MASK_VARIATION);
            isPassword = ((variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                    || (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    || (variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD));
        }
        return isPassword;
    }
}
