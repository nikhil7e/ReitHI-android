package is.hi.hbv601g.reithi_android.Services;

import android.content.Context;
import android.util.Log;

// import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import is.hi.hbv601g.reithi_android.Entities.Course;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;

public class ReviewService {

    private static final String TAG = "ReviewService";
    private NetworkManager mNetworkManager;
    private ParserService mParserService;

    public ReviewService(Context context) {
        mNetworkManager = NetworkManager.getInstance(context);
        mParserService = ParserService.getInstance();
    }

    public void addReviewPOST(final NetworkCallback<String> callback, Map<String, String> params, String requestURL){
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
                ,params, requestURL);
    }
}

