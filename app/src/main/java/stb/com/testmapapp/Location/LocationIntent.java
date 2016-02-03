package stb.com.testmapapp.Location;

import android.content.Intent;
import android.location.Location;

public class LocationIntent {

    public final static String LocationUpdate = "LocationUpdate";

    private final static String Location = "location";

    public static Intent create(Location location) {
        return new Intent()
            .setAction(LocationUpdate)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .putExtra(Location, location);
    }

    public static Location getLocation(Intent intent) {
        return intent.getParcelableExtra(Location);
    }
}
