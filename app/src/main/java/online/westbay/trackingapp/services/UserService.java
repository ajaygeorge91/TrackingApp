package online.westbay.trackingapp.services;



import java.util.List;

import online.westbay.trackingapp.models.OrganizationUserDTO;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.models.UserDTO;
import online.westbay.trackingapp.models.UserWithDetailsDTO;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ajay on 30-Apr-16.
 */
public interface UserService {

    @GET("api/user")
    Observable<ResponseDTO<UserDTO>> user();

    @GET("api/user/details")
    Observable<ResponseDTO<UserWithDetailsDTO>> userDetails();

}
