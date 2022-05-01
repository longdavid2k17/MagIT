package pl.kantoch.dawid.magit.utils;

import com.google.gson.Gson;

public final class GsonInstance {
    private static Gson instance;
    private GsonInstance(){
        instance = new Gson();
    }

    public static Gson get(){
        if(instance==null){
            instance = new Gson();
        }
        return instance;
    }
}
