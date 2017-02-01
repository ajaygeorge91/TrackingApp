package online.westbay.trackingapp.services;



import online.westbay.trackingapp.models.AuthResultDTO;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.models.UserAuthDTO;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ajay on 30-Apr-16.
 */
public interface AuthService {

    @POST("api/authenticate/credentials")
    Observable<ResponseDTO<AuthResultDTO>> signIn(@Body UserAuthDTO user);

    @POST("api/signUp")
    Observable<ResponseDTO<AuthResultDTO>> signUp(@Body UserAuthDTO user);

}
