package stb.com.testmapapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import stb.com.testmapapp.util.Preconditions;

public class PreferenceProvider {

    private final Context context;

    public PreferenceProvider(Context context) {
        this.context = Preconditions.checkNotNull(context);
    }

    public String getString(PreferenceType type) {
        Preconditions.checkNotNull(type);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String preference = SP.getString(type.key(), null);
        return preference == null ? null : preference.trim();
    }
}
