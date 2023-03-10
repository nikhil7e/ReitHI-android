package is.hi.hbv601g.reithi_android;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.Course;

public class NetworkManager {

    private static Context sContext;
    private static NetworkManager sInstance;
    private static RequestQueue sQueue;
    //private static final String BASE_URL = "http://10.0.2.2:8080/api";
    private static final String BASE_URL = "https://reithi-production.up.railway.app/api";

    private NetworkManager(Context context) {
        sContext = context;
        sQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (sQueue == null) {
            sQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }

        return sQueue;
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NetworkManager(context);
        }

        return sInstance;
    }

//    public void searchCoursesPOST(final NetworkCallback<List<Course>> callback, Map<String, String> params) {
//        StringRequest req = new StringRequest(Request.Method.POST, BASE_URL + "/searchcourses",
//                res -> {
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<Course>>() {
//                    }.getType();
//                    List<Course> courseList = gson.fromJson(res, listType);
//                    callback.onSuccess(courseList);
//                },
//                error -> {
//                    callback.onFailure(error.toString());
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return params;
//            }
//        };
//
//        sQueue.add(req);
//    }

    public void genericPOST(final NetworkCallback<String> callback, JSONObject jsonBody, String reqURL) {
        StringRequest req = new StringRequest(Request.Method.POST, BASE_URL + reqURL,
                response -> callback.onSuccess(response),
                error -> callback.onFailure(error.toString())
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonBody.toString().getBytes();
            }
        };

        sQueue.add(req);
    }


    public void genericGET(final NetworkCallback<String> callback, String reqURL) {
        StringRequest req = new StringRequest(Request.Method.GET, BASE_URL + reqURL,
                res -> callback.onSuccess(res),
                error -> callback.onFailure(error.toString())
        );

        sQueue.add(req);
    }

}
