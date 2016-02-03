package stb.com.testmapapp.Activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import stb.com.testmapapp.Location.LocationIntent;
import stb.com.testmapapp.Location.LocationService;
import stb.com.testmapapp.Location.UpdateLocationTask;
import stb.com.testmapapp.R;
import stb.com.testmapapp.preferences.AppPreferences;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, ConnectionCallbacks {

    private class LocationReceiver extends BroadcastReceiver {

        private final LocationListener listener;

        public LocationReceiver(LocationListener listener) {
            this.listener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = LocationIntent.getLocation(intent);
            listener.onLocationChanged(location);
        }
    }

    private GoogleMap map;
    private Marker marker;
    private boolean zoom;
    private GoogleApiClient googleApiClient;
    private Intent intentService;
    private PendingIntent pendingIntent;
    private LocationReceiver locationReceiver;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();

        googleApiClient.connect();
        locationReceiver = new LocationReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addAction(LocationIntent.LocationUpdate);
        registerReceiver(locationReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
        unregisterReceiver(locationReceiver);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
    }

    @Override
    public void onLocationChanged(final Location location) {

        // Update server location
        LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String emailAddress = SP.getString("emailAddress", null);
        new UpdateLocationTask(emailAddress).execute(location);

        currentLocation = location;

        plotLocation(location);
    }

    private void plotLocation(final Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (marker != null) {
                    marker.remove();
                }

                LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
                int resourceId = getResources().getIdentifier("raw/dad", "raw", getPackageName());
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(resourceId);
                marker = map.addMarker(new MarkerOptions()
                        .position(latLong)
                        .title("Me")
                        .icon(icon));

                if (zoom) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                    zoom = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        zoom = true;
        if (currentLocation != null) {
            plotLocation(currentLocation);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(5000L)
                .setInterval(10000L)
                .setSmallestDisplacement(1);

            intentService = new Intent(this, LocationService.class);
            pendingIntent = PendingIntent.getService(this, 0, intentService, PendingIntent.FLAG_UPDATE_CURRENT);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, pendingIntent);
        } catch (SecurityException e) {
            // Alert
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.preferences:
            {
                Intent intent = new Intent();
                intent.setClassName(this, AppPreferences.class.getName());
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
