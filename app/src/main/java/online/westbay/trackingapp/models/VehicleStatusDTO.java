package online.westbay.trackingapp.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ajayg on 11/17/2016.
 */

public class VehicleStatusDTO {
    public String vehicleID;
    public double latitude;
    public double longitude;
    public long time;

    public VehicleStatusDTO(String vehicleID, double latitude, double longitude, long time) {
        this.vehicleID = vehicleID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }
}
