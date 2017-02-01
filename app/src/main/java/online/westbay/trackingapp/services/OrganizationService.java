package online.westbay.trackingapp.services;



import android.provider.ContactsContract;

import java.util.List;

import online.westbay.trackingapp.models.OrganizationDTO;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.models.UserDTO;
import online.westbay.trackingapp.models.VehicleDTO;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ajay on 30-Apr-16.
 */
public interface OrganizationService {

    @GET("api/orgs")
    Observable<ResponseDTO<List<OrganizationDTO>>> listOrgs();

    @GET("api/orgs/{orgID}/vehicles")
    Observable<ResponseDTO<List<VehicleDTO>>> listVehiclesFromOrg(@Path("orgID") String orgID);

}
