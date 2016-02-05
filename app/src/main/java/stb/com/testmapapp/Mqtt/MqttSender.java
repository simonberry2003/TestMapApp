package stb.com.testmapapp.Mqtt;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MqttSender implements AutoCloseable {

    private static final String SERVER_URL = "tcp://simonberry2003.ddns.net:1883";
    private static final String CLIENT_ID = "TestClientId";

    private AtomicReference<MqttClient> clientReference = new AtomicReference<>();

    public void publish(String topic, Object payload) {
        try {
            String json = new Gson().toJson(payload);
            MqttMessage message = new MqttMessage(json.getBytes());
            MqttClient client = getClient();
            message.setQos(0);
            message.setRetained(false);
            client.publish(topic, message);
        } catch (Exception e) {
        }
    }

    public String requestResponse(String topic, RequestResponse requestResponse) {
        try {
            MqttClient client = getClient();
            MqttMessage message = new MqttMessage(requestResponse.getRequest().getBytes());
            message.setQos(0);
            message.setRetained(false);
            client.subscribe(requestResponse.getResponseTopic());
            final AtomicReference<MqttMessage> response = new AtomicReference<>();
            final CountDownLatch latch = new CountDownLatch(1);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    latch.countDown();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    response.set(message);
                    latch.countDown();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
            client.publish(topic, message);
            latch.await(30, TimeUnit.SECONDS);
            client.unsubscribe(requestResponse.getResponseTopic());
            if (response.get() != null) {
                return new String(response.get().getPayload());
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        MqttClient client = clientReference.get();
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }

    private MqttClient getClient() throws Exception {
        MqttClient client = clientReference.get();
        if (client == null) {
            client = new MqttClient(SERVER_URL, CLIENT_ID, new MemoryPersistence());
            clientReference.compareAndSet(null, client);
            client = clientReference.get();
        }
        if (!client.isConnected()) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
        }
        return client;
    }
}
