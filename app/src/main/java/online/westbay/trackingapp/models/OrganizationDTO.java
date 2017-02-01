package online.westbay.trackingapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrganizationDTO implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;

}
