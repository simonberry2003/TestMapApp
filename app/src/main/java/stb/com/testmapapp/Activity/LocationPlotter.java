package stb.com.testmapapp.Activity;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import stb.com.testmapapp.util.Preconditions;

/**
 * Plots a {@link Location} on {@link GoogleMap} and optionally moves to the location.
 * If the marker has previously been plotted, it is removed.
 */
public class LocationPlotter {
    
    private final Activity activity;
    private GoogleMap map;
    private Marker marker;

    public LocationPlotter(Activity activity, GoogleMap map) {
        this.activity = Preconditions.checkNotNull(activity);
        this.map = Preconditions.checkNotNull(map);
    }

    public void plot(final Location location, final boolean moveToLocation) {

        Preconditions.checkNotNull(location);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (marker != null) {
                    marker.remove();
                }

                LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
                int resourceId = activity.getResources().getIdentifier("raw/dad", "raw", activity.getPackageName());
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(resourceId);
                marker = map.addMarker(new MarkerOptions()
                        .position(latLong)
                        .title("Me")
                        .icon(icon));

                if (moveToLocation) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                }
            }
        });
    }
}
