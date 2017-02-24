package com.azazte.TvAnalytics.CBIR;

import com.azazte.TvAnalytics.DefaultPaths;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dhruv.suri on 20/02/17.
 */
public class CBIRService {
    private static Map<String, String> map;
    private static CBIRService instance = new CBIRService();

    public static CBIRService getInstance() {
        return instance;
    }

    public String identifyBucket(String url) {
        if (map == null) {
            refreshMap();
        }

        Map<String, Integer> bucketMap = new HashMap<>();
        List<SimilarityResponse> responses = CBIRAPIExecutor.getInstance().searchImage(url);
        responses = responses.subList(0, 10);
        for (SimilarityResponse response : responses) {
            String bucket = map.get(response.getId() + ".png");
            if (bucketMap.containsKey(bucket)) {
                Integer bucketCount = bucketMap.get(bucket);
                bucketMap.put(bucket, ++bucketCount);
            } else {
                bucketMap.put(bucket, 1);
            }
        }
        int max = 0;
        String maxBucketName = null;

        for (Map.Entry<String, Integer> entry : bucketMap.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxBucketName = entry.getKey();
            }
        }
        if (max > DefaultPaths.BucketThreshold) {
            return maxBucketName;
        }
        return null;
    }

    public void refreshMap() {
        try {
            map = new HashMap<>();
            System.out.println("Size of map is now " + map.size());
            FileInputStream fis = new FileInputStream(DefaultPaths.mapPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

    }
}
