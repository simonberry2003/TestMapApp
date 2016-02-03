package stb.com.testmapapp.Location;

import android.location.Location;
import android.os.AsyncTask;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class UpdateLocationTask extends AsyncTask<Location, Long, Boolean> {

    private final String userEmail;

    public UpdateLocationTask(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    protected Boolean doInBackground(Location... locations) {

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
            for (Location location : locations) {
                MqttMessage msg = new MqttMessage((userEmail + "," + location.getLatitude() + "," + location.getLongitude()).getBytes());
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
