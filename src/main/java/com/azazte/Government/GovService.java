package com.azazte.Government;

import com.azazte.Government.ResponseStructure.Response.CommandLevelResponse;
import com.azazte.Government.ResponseStructure.Response.LowestLevelResponse;
import com.azazte.Government.ResponseStructure.Response.ZoneLevelResponse;
import com.azazte.Government.ResponseStructure.UnitResource;
import com.azazte.Government.ResponseStructure.VehicleResource;
import com.azazte.mongo.MongoFactory;
import jersey.repackaged.com.google.common.collect.Maps;
import jersey.repackaged.com.google.common.collect.Table;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.*;
import java.util.*;

/**
 * Created by home on 27/12/16.
 */
public class GovService {

    private static GovService instance = null;

    private void GovService() {

    }

    public static GovService getInstance() {
        if (instance == null) {
            instance = new GovService();
        }
        return instance;
    }

    public void preProcess(String path) {
        List<UnitResource> unitResources = importResources(path);


        populateVehicleCount(unitResources);
        List<LowestLevelResponse> lowestLevelResponses = buildLowestLevelResponse(unitResources);
        MongoFactory.getMongoTemplate().insertAll(lowestLevelResponses);

        List<ZoneLevelResponse> zoneLevelResponses = buildZoneLevelResponse(lowestLevelResponses);
        MongoFactory.getMongoTemplate().insertAll(zoneLevelResponses);

        List<CommandLevelResponse> commandLevelResponses = buildCommandLevelResponse(zoneLevelResponses);
        MongoFactory.getMongoTemplate().insertAll(commandLevelResponses);
    }

//    public static void main(String args[]) {
//        getInstance().preProcess("/Users/home/Desktop/Resourcesheet.csv");
//    }


    /*
    Import fields from file
     */

    private List<UnitResource> importResources(String path) {

        String line = "";
        String cvsSplitBy = ",";
        List<UnitResource> unitResources = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                UnitResource unitResource = new UnitResource();
                String[] row = line.split(cvsSplitBy);
                unitResource.setCommand(row[0].replace(' ', '_'));
                unitResource.setZone(row[1].replace(' ', '_'));
                unitResource.setCwe(row[2].replace(' ', '_'));
                unitResource.setGe(row[3].replace(' ', '_'));

                try {
                    unitResource.setAGEBR(Integer.parseInt(row[4]));
                } catch (Exception ignored) {

                }

                try {
                    unitResource.setAGEEM(Integer.parseInt(row[5]));
                } catch (Exception ignored) {

                }

                try {
                    unitResource.setBSO(Integer.parseInt(row[6]));
                } catch (Exception ignored) {

                }
                unitResources.add(unitResource);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return unitResources;
    }

    private void populateVehicleCount(List<UnitResource> unitResources) {
        for (UnitResource unitResource : unitResources) {
            VehicleResource vehicleResource = new VehicleResource();
            unitResource.setVehicleResource(vehicleResource);

            vehicleResource.setLDV(unitResource.getAGEEM() + unitResource.getAGEBR() + unitResource.getBSO());
            vehicleResource.setLCV(unitResource.getAGEEM() + unitResource.getAGEBR() + unitResource.getBSO());
            vehicleResource.setMC(unitResource.getAGEEM() + unitResource.getAGEBR() + unitResource.getBSO());
        }
    }

    /*
    END - Import fields
     */

    /*
    Command Level Response
     */

    private List<CommandLevelResponse> buildCommandLevelResponse(List<ZoneLevelResponse> zoneLevelResponses) {
        List<CommandLevelResponse> commandLevelResponses = new ArrayList<>();


        Map<String, List<ZoneLevelResponse>> commandVsZoneLevelResponse = new HashMap<>();
        for (ZoneLevelResponse zoneLevelResponse : zoneLevelResponses) {
            if (commandVsZoneLevelResponse.containsKey(zoneLevelResponse.getCommandName())) {
                commandVsZoneLevelResponse.get(zoneLevelResponse.getCommandName()).add(zoneLevelResponse);
            } else {
                List<ZoneLevelResponse> zoneLevelResponseList = new ArrayList<>();
                zoneLevelResponseList.add(zoneLevelResponse);
                commandVsZoneLevelResponse.put(zoneLevelResponse.getCommandName(), zoneLevelResponseList);

            }
        }

        for (Map.Entry<String, List<ZoneLevelResponse>> entry : commandVsZoneLevelResponse.entrySet()) {
            VehicleResource baseVehicleResource = new VehicleResource();
            for (ZoneLevelResponse zoneLevelResponse : entry.getValue()) {
                addToBase(baseVehicleResource, zoneLevelResponse.getVehicleResource());
            }
            addCommandLevelExceptions(entry.getKey(), baseVehicleResource);
            CommandLevelResponse commandLevelResponse = new CommandLevelResponse();
            commandLevelResponse.setCommandName(entry.getKey());
            commandLevelResponse.setVehicleResource(baseVehicleResource);
            commandLevelResponses.add(commandLevelResponse);
        }


        return commandLevelResponses;
    }


    public List<VehicleResource> fetchCommandLevelResponse() {
        List<CommandLevelResponse> commandLevelResponses = MongoFactory.getMongoTemplate().find(new Query(), CommandLevelResponse.class);
        List<VehicleResource> vehicleResources = new ArrayList<>();
        for (CommandLevelResponse commandLevelResponse : commandLevelResponses) {
            commandLevelResponse.getVehicleResource().setEntityName(commandLevelResponse.getCommandName());
            vehicleResources.add(commandLevelResponse.getVehicleResource());
        }
        return vehicleResources;
    }

    /* END - Command level Respone */





    /*
    Zone level response
     */

    private List<ZoneLevelResponse> buildZoneLevelResponse(List<LowestLevelResponse> lowestLevelResponses) {
        //Lowest level responses have CWE as the Root.


        Map<String, List<LowestLevelResponse>> zoneNameVsListOfLLR = Maps.newHashMap();
        for (LowestLevelResponse lowestLevelResponse : lowestLevelResponses) {
            if (zoneNameVsListOfLLR.containsKey(lowestLevelResponse.getZoneName())) {
                zoneNameVsListOfLLR.get(lowestLevelResponse.getZoneName()).add(lowestLevelResponse);
            } else {
                List<LowestLevelResponse> zoneWiseList = new ArrayList<>();
                zoneWiseList.add(lowestLevelResponse);
                zoneNameVsListOfLLR.put(lowestLevelResponse.getZoneName(), zoneWiseList);
            }
        }

        List<ZoneLevelResponse> zoneLevelResponses = new ArrayList<>();
        for (Map.Entry<String, List<LowestLevelResponse>> zoneWiseEntrySet : zoneNameVsListOfLLR.entrySet()) {
            ZoneLevelResponse zoneLevelResponse = new ZoneLevelResponse();
            zoneLevelResponse.setCommandName(zoneWiseEntrySet.getValue().get(0).getCommandName());
            zoneLevelResponse.setZoneName(zoneWiseEntrySet.getKey());
            VehicleResource vehicleResource = calculateVehicleResource(zoneWiseEntrySet.getValue());
            addZoneLevelExceptions(zoneWiseEntrySet.getKey(), vehicleResource);
            zoneLevelResponse.setVehicleResource(vehicleResource);
            zoneLevelResponses.add(zoneLevelResponse);
        }
        return zoneLevelResponses;
    }

    public List<VehicleResource> fetchZoneLevelResponse(String commandName) {
        List<ZoneLevelResponse> zoneLevelResponses = MongoFactory.getMongoTemplate().find(new Query(Criteria.where("commandName").is(commandName)), ZoneLevelResponse.class);
        List<VehicleResource> vehicleResources = new ArrayList<>();
        for (ZoneLevelResponse zoneLevelResponse : zoneLevelResponses) {
            zoneLevelResponse.getVehicleResource().setEntityName(zoneLevelResponse.getZoneName());
            vehicleResources.add(zoneLevelResponse.getVehicleResource());
        }
        return vehicleResources;
    }

    /*END - Zone level response */



    /*
    Lowest level response
     */

    private List<LowestLevelResponse> buildLowestLevelResponse(List<UnitResource> unitResources) {
        Map<String, LowestLevelResponse> cweVsGeList = Maps.newHashMap();
        for (UnitResource unitResource : unitResources) {
            LowestLevelResponse lowestLevelResponse;
            if (cweVsGeList.containsKey(unitResource.getCwe())) {
                lowestLevelResponse = cweVsGeList.get(unitResource.getCwe());
                if (unitResource.getGe().contains("GE")) {
                    unitResource.getVehicleResource().setSCAR(1);
                }
                lowestLevelResponse.getGeVsVehicleResource().put(unitResource.getGe(), unitResource.getVehicleResource());
            } else {
                lowestLevelResponse = new LowestLevelResponse();
                lowestLevelResponse.setCommandName(unitResource.getCommand());
                lowestLevelResponse.setZoneName(unitResource.getZone());
                lowestLevelResponse.setCwe(unitResource.getCwe());
                Map<String, VehicleResource> geVsVehicleResource = new HashMap();
                VehicleResource vehicleResource = unitResource.getVehicleResource();
                if (unitResource.getGe().contains("GE")) {
                    vehicleResource.setSCAR(1);

                    //TODO
                    //vehicleResource.setGYPSY(1); // based on exception O
                }
                geVsVehicleResource.put(unitResource.getGe(), vehicleResource);

                lowestLevelResponse.setGeVsVehicleResource(geVsVehicleResource);
                cweVsGeList.put(unitResource.getCwe(), lowestLevelResponse);
            }
        }
        return new ArrayList<>(cweVsGeList.values());
    }

    public List<VehicleResource> fetchLowestLevelRsponse(String commandName, String zoneName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("commandName").is(commandName));
        query.addCriteria(Criteria.where("zoneName").is(zoneName));
        List<LowestLevelResponse> lowestLevelResponses = MongoFactory.getMongoTemplate().find(query, LowestLevelResponse.class);

        List<VehicleResource> vehicleResources = new ArrayList<>();

        for (LowestLevelResponse lowestLevelResponse : lowestLevelResponses) {
            List<VehicleResource> vehicleResourcesPerCWE = mapToListTransformer(lowestLevelResponse.getGeVsVehicleResource());
            vehicleResources.add(getCWEVehicleResource(lowestLevelResponse.getCwe()));
            vehicleResources.addAll(vehicleResourcesPerCWE);
        }

        return vehicleResources;
    }

    /* END - Lowest level response */







    /*
    Helper Functions
     */

    private VehicleResource calculateVehicleResource(List<LowestLevelResponse> lowestLevelResponses) {
        VehicleResource baseVehicleResource = new VehicleResource();
        for (LowestLevelResponse lowestLevelResponse : lowestLevelResponses) {
            addToBase(baseVehicleResource, lowestLevelResponse.getCweVehicleResource());
            Map<String, VehicleResource> geVsVehicleResource = lowestLevelResponse.getGeVsVehicleResource();
            for (VehicleResource vehicleResource : geVsVehicleResource.values()) {
                addToBase(baseVehicleResource, vehicleResource);
            }
        }
        return baseVehicleResource;
    }

    private void addToBase(VehicleResource baseVehicleResource, VehicleResource vehicleResource) {
        if (vehicleResource == null) {
            return;
        }
        baseVehicleResource.setMC(baseVehicleResource.getMC() + vehicleResource.getMC());
        baseVehicleResource.setLCV(baseVehicleResource.getLCV() + vehicleResource.getLCV());
        baseVehicleResource.setLDV(baseVehicleResource.getLDV() + vehicleResource.getLDV());
        baseVehicleResource.setGYPSY(baseVehicleResource.getGYPSY() + vehicleResource.getGYPSY());
        baseVehicleResource.setOMNI(baseVehicleResource.getOMNI() + vehicleResource.getOMNI());
        baseVehicleResource.setSCAR(baseVehicleResource.getSCAR() + vehicleResource.getSCAR());
    }


    private List<VehicleResource> mapToListTransformer(Map<String, VehicleResource> vehicleResourceMap) {
        List<VehicleResource> vehicleResources = new ArrayList<>();

        for (Map.Entry<String, VehicleResource> vehicleResourceEntry : vehicleResourceMap.entrySet()) {
            vehicleResourceEntry.getValue().setEntityName(vehicleResourceEntry.getKey());
            vehicleResources.add(vehicleResourceEntry.getValue());
        }
        return vehicleResources;
    }


    private VehicleResource getCWEVehicleResource(String cweName) {
        VehicleResource vehicleResource = new VehicleResource();
        vehicleResource.setGYPSY(1);
        vehicleResource.setOMNI(1);
        vehicleResource.setSCAR(1);
        vehicleResource.setMC(1);
        vehicleResource.setEntityName(cweName);
        return vehicleResource;
    }



    /*
    Exceptions
     */

    /*
    exception (a) in annexure B
     */
    private void addCommandLevelExceptions(String commandName, VehicleResource baseVehicleResource) {
        if (getOfficerStrength(commandName) > 50) {
            baseVehicleResource.setSCAR(baseVehicleResource.getSCAR() + 3);
        } else {
            baseVehicleResource.setSCAR(baseVehicleResource.getSCAR() + 2);
        }

        baseVehicleResource.setMC(baseVehicleResource.getMC() + 2);

        baseVehicleResource.setLCV(baseVehicleResource.getLCV() + 1);

        baseVehicleResource.setLDV(baseVehicleResource.getLDV() + 2); // yet to get clarification on exception (b)

        baseVehicleResource.setLORRY_TELESCOPIC_LADDER(5);
    }


    private void addZoneLevelExceptions(String zoneName, VehicleResource baseVehicleResource) {
        if (getOfficerStrength(zoneName) > 50) {
            baseVehicleResource.setSCAR(baseVehicleResource.getSCAR() + 3);
        } else {
            baseVehicleResource.setSCAR(baseVehicleResource.getSCAR() + 3);
        }

        baseVehicleResource.setOMNI(baseVehicleResource.getOMNI() + 2 + 1);
        baseVehicleResource.setGYPSY(baseVehicleResource.getGYPSY() + 1 + 1);
        baseVehicleResource.setMC(baseVehicleResource.getMC() + 2 + 1);
        baseVehicleResource.setLDV(baseVehicleResource.getLDV() + 1 ); // yet to get clarification on exception (b)
        baseVehicleResource.setLCV(baseVehicleResource.getLCV() + 1);
    }

    private int getOfficerStrength(String name) {
        // Get officer strength from sheet.
        return 0;
    }


}
