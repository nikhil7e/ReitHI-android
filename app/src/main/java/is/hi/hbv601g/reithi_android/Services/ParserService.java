package is.hi.hbv601g.reithi_android.Services;

import android.content.Context;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.NetworkManager;

public class ParserService {

    private static ParserService sInstance;


    private ParserService() {

    }

    public static synchronized ParserService getInstance() {
        if (sInstance == null) {
            sInstance = new ParserService();
        }

        return sInstance;
    }

    public List<Object> parse(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

}
