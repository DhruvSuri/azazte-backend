package com.azazte.webservice;

import com.azazte.Government.GovService;
import com.azazte.Government.ResponseStructure.UnitResource;
import com.azazte.Government.ResponseStructure.VehicleResource;
import com.azazte.utils.AzazteUtils;
import com.google.gson.Gson;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by home on 21/12/16.
 */
@Path("/gov")
public class GovernmentRestAPI {


    @Path("resources")
    @GET
    public Response getResources(@QueryParam("command") String commandName, @QueryParam("zone") String zoneName) {
        if (commandName == null) {

            return Response.ok(new Gson().toJson(fillHeldData(GovService.getInstance().fetchCommandLevelResponse()))).build();
        }
        if (zoneName == null) {
            return Response.ok(new Gson().toJson(fillHeldData(GovService.getInstance().fetchZoneLevelResponse(commandName)))).build();
        }
        return Response.ok(new Gson().toJson(fillHeldData(GovService.getInstance().fetchLowestLevelRsponse(commandName, zoneName)))).build();
    }

    @Path("held")
    @GET
    public Response getHeldResourceData(@QueryParam("entity") String entityName) {
        try (BufferedReader br = new BufferedReader(new FileReader("/home/ec2-user/jetty/jetty-distribution-9.3.10.v20160621/webapps/Held.csv"))) {
            br.readLine();
            String line;
            String cvsSplitBy = ",";
            int count = 0;
            List<String> resource = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                // use comma as separator
                UnitResource unitResource = new UnitResource();
                String[] row = line.split(cvsSplitBy);
                if (row[0].equals(entityName)) {
                    resource.add(line);
                }
            }
            return Response.ok(AzazteUtils.toJson(resource)).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<VehicleResource> fillHeldData(List<VehicleResource> vehicleResources) {
        for (VehicleResource vehicleResource : vehicleResources) {
            vehicleResource.setHLDV(getFromSheet(vehicleResource.getEntityName(), "LDV"));
            vehicleResource.setHGYPSY(getFromSheet(vehicleResource.getEntityName(), "Gypsy"));
            vehicleResource.setHSCAR(getFromSheet(vehicleResource.getEntityName(), "SC"));
            vehicleResource.setHOMNI(getFromSheet(vehicleResource.getEntityName(), "Omni"));
            vehicleResource.setHMC(getFromSheet(vehicleResource.getEntityName(), "MC"));
            vehicleResource.setHLCV(getFromSheet(vehicleResource.getEntityName(), "LCV"));
        }
        return vehicleResources;
    }

    private int getFromSheet(String entityName, String str) {

        try (BufferedReader br = new BufferedReader(new FileReader("/home/ec2-user/jetty/jetty-distribution-9.3.10.v20160621/webapps/Held.csv"))) {
            br.readLine();
            String line;
            String cvsSplitBy = ",";
            int count = 0;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                UnitResource unitResource = new UnitResource();
                String[] row = line.split(cvsSplitBy);
                if (row[0].equals(entityName) && row[1].equals(str)) {
                    count++;
                }
            }
            return count;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Path("preprocess")
    @GET
    public Response preProcess(@QueryParam("path") String path) {
        GovService.getInstance().preProcess(path);
        return Response.ok().build();
    }

    public static void main(String args[]) {
        new GovernmentRestAPI().preProcess("/Users/dhruv.suri/Documents/Authorized.csv");
    }
}
