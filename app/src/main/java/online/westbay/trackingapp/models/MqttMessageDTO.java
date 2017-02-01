package online.westbay.trackingapp.models;

/**
 * Created by ajayg on 11/16/2016.
 */

public class MqttMessageDTO {
    private String topic;
    private byte[] payload;

    public MqttMessageDTO() {
    }

    public MqttMessageDTO(String topic, byte[] payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
