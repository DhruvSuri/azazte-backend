package com.azazte;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.BasicManagedEntity;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.message.BasicHttpResponse;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import static com.ibm.watson.developer_cloud.util.ResponseConverterUtils.getString;

/**
 * Created by home on 17/01/17.
 */
public class CodingPractice {
    public static void main(String[] args) {

        checkNumumbsers();
        String output = "";
        int num = 1601;
        String path = "/Users/dhruv.suri/Downloads/TvAnalytics/";
        try {
            int chunk = 1000;
            FileInputStream inStream = new FileInputStream(path + "test_18402.ts");


            while (chunk < 100) {
                FileOutputStream stream = new FileOutputStream(path + "Chunks/chunk_" + chunk + ".txt");
                FileOutputStream tsStream = new FileOutputStream(path + "Chunks/chunk_" + chunk + ".ts");
                byte[] bs = new byte[2000];
                inStream.read(bs);
                tsStream.write(bs);
                stream.write(bs);
                stream.close();
                tsStream.close();
                chunk++;
            }

        } catch (Exception e) {

        }

        // Base url : http://dittotv.live-s.cdn.bitgravity.com/cdn-live/_definst_/dittotv/secure/sony_sab_Web.smil/playlist.m3u8?e=1486200335&a=IN&h=f91cadfc41b6d3893e2d5a1b9a2eab59&domain=production&key1=dittotv.com~Chrome~56&key2=msisdn~00919810250918~no~~NA&key3=live~

        try {
            FileOutputStream tsStream = new FileOutputStream("test_" + num + ".ts");
            FileOutputStream textStream = new FileOutputStream("text_" + num + ".txt");
            while (true) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String first = "http://dittotv.live-s.cdn.bitgravity.com/cdn-live/_definst_/dittotv/secure/and_tv_hd_Web.smil/media_w554815213_b116000_";

                String second = ".ts?e=1486504296&a=IN&h=2a67ec8c9737343839f35f9be9d2f9b7&domain=production&key1=dittotv.com%7EChrome%7E56&key2=msisdn%7E00919810250917%7Eyes%7EFTJP2D%7ENA&key3=live%7E";
                HttpGet getRequest = new HttpGet(first + num + second);
                Thread.sleep(2000);
                HttpResponse response = httpClient.execute(getRequest);
                //new DefaultHttpClient().execute(new HttpGet("http://dittotv.live-s.cdn.bitgravity.com/cdn-live/_definst_/dittotv/secure/sony_ent_Web.smil/media_w2052997301_b116000_2223.ts?e=1486505756&a=IN&h=4503e2db614e38a811b1f6fe308724a4&domain=production&key1=dittotv.com%7EChrome%7E56&key2=msisdn%7E00919810250917%7Eyes%7EFTJP2D%7ENA&key3=live%7Ehttp://dittotv.live-s.cdn.bitgravity.com/cdn-live/_definst_/dittotv/secure/sony_ent_Web.smil/media_w2052997301_b116000_2223.ts?e=1486505756&a=IN&h=4503e2db614e38a811b1f6fe308724a4&domain=production&key1=dittotv.com%7EChrome%7E56&key2=msisdn%7E00919810250917%7Eyes%7EFTJP2D%7ENA&key3=live%7E")).getEntity().writeTo(new FileOutputStream("testTime2.ts"))
                ArrayList<Byte> arrayList = new ArrayList<Byte>();
                ArrayList<Integer> arrayList1 = new ArrayList<>();

                long contentLength = ((BasicHttpResponse) response).getEntity().getContentLength();

                int i = 0;


                while (i < contentLength) {
                    byte read = (byte) ((BasicHttpResponse) response).getEntity().getContent().read();
                    arrayList.add(read);
                    arrayList1.add((int) read);
                    i++;
                }

                tsStream.write(ArrayUtils.toPrimitive(arrayList.toArray(new Byte[arrayList.size()])));
                System.out.println(arrayList1);

                num++;
                httpClient.getConnectionManager().shutdown();
            }

        } catch (Exception e) {

        }

    }

    private static void checkNumumbsers() {

    }


}

