package stb.com.testmapapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Simon on 28/01/2016.
 */
public class UpdateLocationTask extends AsyncTask<LatLng, Long, Boolean> {

    private final String userEmail;

    public UpdateLocationTask(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    protected Boolean doInBackground(LatLng... latLongs) {

        // Email is null if the user has not set their preferences
        if (userEmail == null)
        {
            return true;
        }

        try {
            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient client = new MqttClient("tcp://simonberry2003.ddns.net:1883", "updateLocation", persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            for (LatLng latLong : latLongs) {
                MqttMessage msg = new MqttMessage((userEmail + "," + latLong.latitude + "," + latLong.longitude).getBytes());
                msg.setQos(0);
                msg.setRetained(false);
                client.publish("position.update", msg);
            }
            client.disconnect();
        }
        catch (Exception e)
        {
        }
        return true;
    }
}
