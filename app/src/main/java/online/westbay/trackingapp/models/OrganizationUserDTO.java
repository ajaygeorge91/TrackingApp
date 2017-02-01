package online.westbay.trackingapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajayg on 11/10/2016.
 */

public class OrganizationUserDTO {
    private String role;
    private OrganizationDTO organization;
    private List<VehicleDTO> vehicles = new ArrayList<>();

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    public List<VehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDTO> vehicles) {
        this.vehicles = vehicles;
    }
}
