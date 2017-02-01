package online.westbay.trackingapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajay on 30-Apr-16.
 */
public class ResponseDTO<T> {

    @SerializedName("message")
    public String message;
    @SerializedName("success")
    public boolean success;
    @SerializedName("data")
    public T data;

    public ResponseDTO() {

    }

    public ResponseDTO(String message) {
        this.success = false;
        this.message = message;
    }


}
