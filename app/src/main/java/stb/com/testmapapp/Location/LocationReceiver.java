package stb.com.testmapapp.Location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import stb.com.testmapapp.util.Preconditions;

/**
 * Receives location updates from the {@link LocationService} and passes them on to a {@Link LocationListener}
  */
public class LocationReceiver extends BroadcastReceiver {

    private final LocationListener listener;
    private final IntentFilter intentFilter;

    public LocationReceiver(LocationListener listener) {
        this.listener = Preconditions.checkNotNull(listener);
        intentFilter = new IntentFilter();
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addAction(LocationIntent.LocationUpdate);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = LocationIntent.getLocation(intent);
        listener.onLocationChanged(location);
    }

    public IntentFilter getFilter() {
        return intentFilter;
    }
}
