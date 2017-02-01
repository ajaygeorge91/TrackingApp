package online.westbay.trackingapp.mqtt.impl.paho;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import online.westbay.trackingapp.mqtt.impl.MqttException;
import online.westbay.trackingapp.mqtt.interfaces.IMqttMessage;

public class PahoMqttMessageWrapper implements IMqttMessage {
    private MqttMessage message;

    public PahoMqttMessageWrapper(MqttMessage message) {
        this.message = message;
    }

    @Override
    public byte[] getPayload() throws MqttException {
        try {
            return message.getPayload();
        } catch (Exception e) {
            throw new MqttException(e);
        }
    }

    @Override
    public int getQoS() {
        return message.getQos();
    }

    public String toString() {
        return "PahoMqttMessageWrapper{" + message.toString() + "}";
    }

    @Override
    public boolean isRetained() {
        return message.isRetained();
    }

    @Override
    public boolean isDuplicate() {
        return message.isDuplicate();
    }
}
