package online.westbay.trackingapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable {

    @SerializedName("userID")
    public String userID;
    @SerializedName("fullName")
    public String fullName;
    @SerializedName("email")
    public String email;
    @SerializedName("mobileNo")
    public String mobileNo;
    @SerializedName("avatarURL")
    public String avatarURL;
}
