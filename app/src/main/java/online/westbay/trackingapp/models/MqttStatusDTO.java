package online.westbay.trackingapp.models;

import online.westbay.trackingapp.mqtt.service.MqttService;

/**
 * Created by ajayg on 11/16/2016.
 */

public class MqttStatusDTO {
    private MqttService.ConnectionStatus status;
    private String reason;

    public MqttStatusDTO() {
    }

    public MqttStatusDTO(MqttService.ConnectionStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public MqttService.ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(MqttService.ConnectionStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
