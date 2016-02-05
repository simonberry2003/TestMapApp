package stb.com.testmapapp.preferences;

import stb.com.testmapapp.util.Preconditions;

public enum PreferenceType {

    EmailAddress("emailAddress");

    private final String key;

    PreferenceType(String key) {
        this.key = Preconditions.checkNotNull(key);
    }

    public String key() {
        return key;
    }
}
