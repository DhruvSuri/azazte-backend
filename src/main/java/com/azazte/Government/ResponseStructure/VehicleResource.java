package com.azazte.Government.ResponseStructure;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by home on 27/12/16.
 */
public class VehicleResource {

    private String entityName;
    private int SCAR;
    private int GYPSY;
    private int OMNI;
    private int LDV;
    private int LCV;
    private int MC;
    private int LORRY_TELESCOPIC_LADDER;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getSCAR() {

        return SCAR;
    }

    public void setSCAR(int SCAR) {
        this.SCAR = SCAR;
    }

    public int getGYPSY() {
        return GYPSY;
    }

    public void setGYPSY(int GYPSY) {
        this.GYPSY = GYPSY;
    }

    public int getOMNI() {
        return OMNI;
    }

    public void setOMNI(int OMNI) {
        this.OMNI = OMNI;
    }

    public int getLDV() {
        return LDV;
    }

    public void setLDV(int LDV) {
        this.LDV = LDV;
    }

    public int getLCV() {
        return LCV;
    }

    public void setLCV(int LCV) {
        this.LCV = LCV;
    }

    public int getMC() {
        return MC;
    }

    public void setMC(int MC) {
        this.MC = MC;
    }

    public int getLORRY_TELESCOPIC_LADDER() {
        return LORRY_TELESCOPIC_LADDER;
    }

    public void setLORRY_TELESCOPIC_LADDER(int LORRY_TELESCOPIC_LADDER) {
        this.LORRY_TELESCOPIC_LADDER = LORRY_TELESCOPIC_LADDER;
    }
}
