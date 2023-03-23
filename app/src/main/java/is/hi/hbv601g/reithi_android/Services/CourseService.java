package is.hi.hbv601g.reithi_android.Services;

import android.content.Context;
import android.util.Log;

// import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;

public class CourseService {

    private static final String TAG = "CourseService";
    private NetworkManager mNetworkManager;
    private ParserService mParserService;

    public CourseService(Context context) {
        mNetworkManager = NetworkManager.getInstance(context);
        mParserService = ParserService.getInstance();
    }

    public void searchCoursesGET(final NetworkCallback<String> callback, String requestURL) {
        mNetworkManager.genericGET(
                new NetworkCallback<String>() {
                    @Override
                    public void onFailure(String errorString) {
                        Log.e(TAG, errorString);
                        callback.onFailure(errorString);
                    }

                    @Override
                    public void onSuccess(String json) {
                        callback.onSuccess(json);
                    }
                }
                , requestURL);
    }

    public void filterPOST(final NetworkCallback<String> callback, JSONObject
            params, String requestURL) {
        mNetworkManager.genericPOST(
                new NetworkCallback<String>() {
                    @Override
                    public void onFailure(String errorString) {
                        Log.e(TAG, errorString);
                        callback.onFailure(errorString);
                    }

                    @Override
                    public void onSuccess(String json) {
                        callback.onSuccess(json);
                    }
                }
                , params, requestURL);
    }


}
