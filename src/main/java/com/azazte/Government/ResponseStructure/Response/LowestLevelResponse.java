package com.azazte.Government.ResponseStructure.Response;

import com.azazte.Government.ResponseStructure.VehicleResource;

import java.util.Map;

/**
 * Created by home on 27/12/16.
 */
public class LowestLevelResponse {
    private String commandName;
    private String zoneName;
    private String cwe;
    private VehicleResource cweVehicleResource;
    private Map<String, VehicleResource> geVsVehicleResource;

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Map<String, VehicleResource> getGeVsVehicleResource() {
        return geVsVehicleResource;
    }

    public void setGeVsVehicleResource(Map<String, VehicleResource> geVsVehicleResource) {
        this.geVsVehicleResource = geVsVehicleResource;
    }

    public String getCwe() {
        return cwe;
    }

    public void setCwe(String cwe) {
        this.cwe = cwe;
    }

    public VehicleResource getCweVehicleResource() {
        return cweVehicleResource;
    }

    public void setCweVehicleResource(VehicleResource cweVehicleResource) {
        this.cweVehicleResource = cweVehicleResource;
    }
}
