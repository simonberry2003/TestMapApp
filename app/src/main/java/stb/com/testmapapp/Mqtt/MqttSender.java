package stb.com.testmapapp.Mqtt;

import android.annotation.TargetApi;
import android.os.Build;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.atomic.AtomicReference;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MqttSender implements AutoCloseable {

    private static final String SERVER_URL = "tcp://simonberry2003.ddns.net:1883";

    private AtomicReference<MqttClient> clientReference = new AtomicReference<>();

    public void publish(String topic, MqttMessage message) {
        try {
            MqttClient client = clientReference.get();
            if (client == null) {
                client = new MqttClient(SERVER_URL, "updateLocation", new MemoryPersistence());
                clientReference.compareAndSet(null, client);
                client = clientReference.get();
            }
            if (!client.isConnected()) {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                client.connect(options);
            }
            message.setQos(0);
            message.setRetained(false);
            client.publish(topic, message);
        } catch (Exception e) {
        }
    }

    @Override
    public void close() throws Exception {
        MqttClient client = clientReference.get();
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }
}
