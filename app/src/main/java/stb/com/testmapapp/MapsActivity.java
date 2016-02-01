package stb.com.testmapapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import stb.com.testmapapp.preferences.AppPreferences;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private LocationManager locationManager;
    private GoogleMap map;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        } catch (SecurityException e) {
            // Alert
        }
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
        final LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String emailAddress = SP.getString("emailAddress", null);
        new UpdateLocationTask(emailAddress).execute(latLong);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean zoom = false;
                if (marker != null) {
                    marker.remove();
                } else {
                    zoom = true;
                }

                int resourceId = getResources().getIdentifier("raw/dad", "raw", getPackageName());
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(resourceId);
                marker = map.addMarker(new MarkerOptions()
                        .position(latLong)
                        .title("Me")
                        .icon(icon));

                if (zoom) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(marker.getPosition());
                    LatLngBounds bounds = builder.build();
                    int padding = 0;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    map.moveCamera(cu);
                }
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
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
