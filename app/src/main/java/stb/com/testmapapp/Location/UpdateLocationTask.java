package stb.com.testmapapp.Location;

import android.location.Location;
import android.os.AsyncTask;

import stb.com.testmapapp.Mqtt.MqttSender;

/**
 * Background task for updating the current location. A connection is made to MQTT and an update sent on
 * the update topic.
 */
public class UpdateLocationTask extends AsyncTask<Location, Long, Boolean> {

    private static final String UPDATE_TOPIC = "position.update";

    private final String userEmail;

    public UpdateLocationTask(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    protected Boolean doInBackground(Location... locations) {

        // Email is null if the user has not set their preferences
        if (userEmail == null) {
            return true;
        }

        try (MqttSender sender = new MqttSender()) {
            for (Location location : locations) {
                LocationUpdate update = new LocationUpdate(userEmail, location.getLatitude(), location.getLongitude());
                sender.publish(UPDATE_TOPIC, update);
            }
        } catch (Exception e) {
        }
        return true;
    }
}
