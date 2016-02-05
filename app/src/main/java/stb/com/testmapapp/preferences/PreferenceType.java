package stb.com.testmapapp.preferences;

import stb.com.testmapapp.util.Preconditions;

public enum PreferenceType {

    EmailAddress("emailAddress"),
    Password("password");

    private final String key;

    PreferenceType(String key) {
        this.key = Preconditions.checkNotNull(key);
    }

    public String key() {
        return key;
    }

    public boolean is(String key) {
        return this.key.equals(key);
    }
}
