package com.azazte.Government.ResponseStructure.Response;

import com.azazte.Government.ResponseStructure.VehicleResource;

import java.util.Map;

/**
 * Created by home on 27/12/16.
 */
public class CommandLevelResponse {
    private String commandName;
    private VehicleResource vehicleResource;

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public VehicleResource getVehicleResource() {
        return vehicleResource;
    }

    public void setVehicleResource(VehicleResource vehicleResource) {
        this.vehicleResource = vehicleResource;
    }
}
