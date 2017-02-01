package online.westbay.trackingapp.models;

import java.util.List;

/**
 * Created by ajayg on 11/14/2016.
 */

public class UserWithDetailsDTO {
    private UserDTO user;
    private List<OrganizationUserDTO> orgList;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<OrganizationUserDTO> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<OrganizationUserDTO> orgList) {
        this.orgList = orgList;
    }
}
