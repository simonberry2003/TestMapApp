package stb.com.testmapapp.Activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import stb.com.testmapapp.Location.LocationReceiver;
import stb.com.testmapapp.Location.LocationService;
import stb.com.testmapapp.Location.UpdateLocationTask;
import stb.com.testmapapp.R;
import stb.com.testmapapp.preferences.AppPreferences;
import stb.com.testmapapp.preferences.PreferenceProvider;
import stb.com.testmapapp.preferences.PreferenceType;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, ConnectionCallbacks {

    private boolean moveToLocation;
    private GoogleApiClient googleApiClient;
    private LocationReceiver locationReceiver;
    private LocationPlotter locationPlotter;
    private Location currentLocation;
    private PendingIntent pendingIntent;
    private PreferenceProvider preferenceProvider;

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
        registerReceiver(locationReceiver, locationReceiver.getFilter());

        preferenceProvider = new PreferenceProvider(getBaseContext());

        // If first run we need to show alert to get user to configure the app
        if (preferenceProvider.getString(PreferenceType.EmailAddress) == null || preferenceProvider.getString(PreferenceType.Password) == null) {
            Intent intent = new Intent(this, AppPreferences.class);
            intent.putExtra(AppPreferences.SHOW_ALERT, true);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        googleApiClient.disconnect();
        unregisterReceiver(locationReceiver);
        pendingIntent.cancel();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        locationPlotter = new LocationPlotter(this, map);
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        String emailAddress = preferenceProvider.getString(PreferenceType.EmailAddress);
        new UpdateLocationTask(emailAddress).execute(newLocation);
        currentLocation = newLocation;
        locationPlotter.plot(newLocation, moveToLocation);
        moveToLocation = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        moveToLocation = true;
        if (currentLocation != null && locationPlotter != null) {
            locationPlotter.plot(currentLocation, moveToLocation);
            moveToLocation = false;
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

            Intent intentService = new Intent(this, LocationService.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.preferences:
                Intent intent = new Intent();
                intent.setClassName(this, AppPreferences.class.getName());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
