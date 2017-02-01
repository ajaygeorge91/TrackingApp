package online.westbay.trackingapp.models;

import java.util.List;

import online.westbay.trackingapp.services.UserService;

/**
 * Created by ajayg on 11/11/2016.
 */

public class DashboardBundleDTO {

    private UserDTO user;
    private List<OrganizationUserDTO> orgs;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<OrganizationUserDTO> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<OrganizationUserDTO> orgs) {
        this.orgs = orgs;
    }
}
