package com.azazte.utils;

import com.google.gson.Gson;

/**
 * Created by home on 27/06/16.
 */
public class AzazteUtils {
    private static Gson GSON_INSTANCE = new Gson();

    public static <T> T fromJson(String data, Class<T> clazz) {
        return GSON_INSTANCE.fromJson(data, clazz);
    }

    public static String toJson(Object obj) {
        return GSON_INSTANCE.toJson(obj);
    }
}
