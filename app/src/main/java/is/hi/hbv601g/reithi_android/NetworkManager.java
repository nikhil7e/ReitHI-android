package is.hi.hbv601g.reithi_android;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.Course;

public class NetworkManager {

    private static Context sContext;
    private static NetworkManager sInstance;
    private static RequestQueue sQueue;
    private static final String BASE_URL = "http://10.0.2.2:8080/api";

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

    // TODO: Nota lambda föll í stað anonymous klasa?
    public void searchCoursesPOST(final NetworkCallback<List<Course>> callback, Map<String, String> params) {
        StringRequest req = new StringRequest(Request.Method.POST, BASE_URL + "/searchcourses",
                res -> {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Course>>() {
                    }.getType();
                    List<Course> courseList = gson.fromJson(res, listType);
                    callback.onSuccess(courseList);
                },
                error -> {
                    callback.onFailure(error.toString());
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        sQueue.add(req);
    }

}
