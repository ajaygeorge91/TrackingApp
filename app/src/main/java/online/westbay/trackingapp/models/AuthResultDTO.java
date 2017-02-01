package online.westbay.trackingapp.models;

import com.google.gson.annotations.SerializedName;

public class AuthResultDTO {
    @SerializedName("token")
    public String token;
    @SerializedName("userDTO")
    public UserDTO userDTO;
}
