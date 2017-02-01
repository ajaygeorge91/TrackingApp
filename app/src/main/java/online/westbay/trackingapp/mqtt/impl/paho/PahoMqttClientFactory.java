package online.westbay.trackingapp.mqtt.impl.paho;

import online.westbay.trackingapp.mqtt.impl.MqttException;
import online.westbay.trackingapp.mqtt.interfaces.IMqttClient;
import online.westbay.trackingapp.mqtt.interfaces.IMqttClientFactory;
import online.westbay.trackingapp.mqtt.interfaces.IMqttPersistence;

public class PahoMqttClientFactory implements IMqttClientFactory {
    @Override
    public IMqttClient create(String host, int port, String clientId,
                              IMqttPersistence persistence) throws MqttException {
        PahoMqttClientPersistence persistenceImpl = null;
        if (persistence != null) {
            persistenceImpl = new PahoMqttClientPersistence(persistence);
        }

        // TODO Auto-generated method stub
        return new PahoMqttClientWrapper(
                "tcp://" + host + ":" + port, clientId, persistenceImpl);
    }
}
