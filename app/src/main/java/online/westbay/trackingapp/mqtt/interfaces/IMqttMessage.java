package online.westbay.trackingapp.mqtt.interfaces;

import online.westbay.trackingapp.mqtt.impl.MqttException;

public interface IMqttMessage
{
	public int getQoS();
	public byte[] getPayload() throws MqttException;
	public boolean isRetained();	
	public boolean isDuplicate();
}
