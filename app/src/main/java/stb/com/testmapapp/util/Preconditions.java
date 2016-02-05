package stb.com.testmapapp.util;

import android.preference.PreferenceManager;

public final class Preconditions {
    private Preconditions() {}

    public static <T> T checkNotNull(T o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return o;
    }
}
