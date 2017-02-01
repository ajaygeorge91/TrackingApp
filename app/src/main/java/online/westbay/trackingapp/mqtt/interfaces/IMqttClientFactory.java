package online.westbay.trackingapp.mqtt.interfaces;

import online.westbay.trackingapp.mqtt.impl.MqttException;

public interface IMqttClientFactory
{
	public IMqttClient create(String host, int port, String clientId, IMqttPersistence persistence) throws MqttException;
}
