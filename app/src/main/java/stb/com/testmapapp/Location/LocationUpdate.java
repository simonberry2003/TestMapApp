package stb.com.testmapapp.Location;

import com.google.gson.Gson;

import stb.com.testmapapp.util.Preconditions;

public class LocationUpdate {
    private final String emailAddress;
    private final double latitude;
    private final double longtitude;

    public LocationUpdate(String emailAddress, double latitude, double longtitude) {
        this.emailAddress = Preconditions.checkNotNull(emailAddress);
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
