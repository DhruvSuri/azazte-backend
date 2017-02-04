package com.azazte;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.message.BasicHttpResponse;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

import static com.ibm.watson.developer_cloud.util.ResponseConverterUtils.getString;

/**
 * Created by home on 17/01/17.
 */
public class CodingPractice {
    public static void main(String[] args) {
        String output = "";
        int num = 9263;

        try {
            FileOutputStream stream = new FileOutputStream("test_" + num + ".ts");
            while (true) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String first = "http://dittotv.live-s.cdn.bitgravity.com/cdn-live/_definst_/dittotv/secure/sony_ent_Web.smil/media_w693868035_b564000_";

                String second = ".ts?e=1485935369&a=IN&h=b4084bba998a26b12d04ae4d5ae8e635&domain=production&key1=dittotv.com%7EChrome%7E55&key2=msisdn%7E00919810250918%7Eyes%7EFTJP2D%7ENA&key3=live%7E";
                HttpGet getRequest = new HttpGet(first + num + second);
                Thread.sleep(2000);
                HttpResponse response = httpClient.execute(getRequest);
                ((BasicHttpResponse) response).getEntity().writeTo(stream);
                num++;
                httpClient.getConnectionManager().shutdown();
            }

        } catch (Exception e) {

        }

    }

    private static void sortt(ArrayList<Integer> list) {
        int low = 0;
        int mid = 0;
        int high = list.size() - 1;
        while (mid <= high) {
            if (list.get(mid) == 0) {
                swap(list, low, mid);
                low++;
                mid++;
            } else if (list.get(mid) == 1) {
                mid++;
            } else if (list.get(mid) == 2) {
                swap(list, mid, high);
                high--;
            }
        }
    }

    private static void swap(ArrayList<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}

