package com.azazte.Government.ResponseStructure;

/**
 * Created by home on 27/12/16.
 */
public class UnitResource {
    private String command;
    private String zone;
    private String cwe;
    private String ge;
    private int AGEBR;
    private int AGEEM;
    private int BSO;
    private int total;
    private VehicleResource vehicleResource;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCwe() {
        return cwe;
    }

    public void setCwe(String cwe) {
        this.cwe = cwe;
    }

    public String getGe() {
        return ge;
    }

    public void setGe(String ge) {
        this.ge = ge;
    }

    public int getAGEBR() {
        return AGEBR;
    }

    public void setAGEBR(int AGEBR) {
        this.AGEBR = AGEBR;
    }

    public int getAGEEM() {
        return AGEEM;
    }

    public void setAGEEM(int AGEEM) {
        this.AGEEM = AGEEM;
    }

    public int getBSO() {
        return BSO;
    }

    public void setBSO(int BSO) {
        this.BSO = BSO;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public VehicleResource getVehicleResource() {
        return vehicleResource;
    }

    public void setVehicleResource(VehicleResource vehicleResource) {
        this.vehicleResource = vehicleResource;
    }
}
