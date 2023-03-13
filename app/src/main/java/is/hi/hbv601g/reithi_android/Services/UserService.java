package is.hi.hbv601g.reithi_android.Services;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.NetworkManager;

public class UserService {

    private static final String TAG = "UserService";
    private NetworkManager mNetworkManager;
    private ParserService mParserService;

    public UserService(Context context) {
        mNetworkManager = NetworkManager.getInstance(context);
        mParserService = ParserService.getInstance();
    }

    public void genericUserPOST(final NetworkCallback<String> callback, Map<String, String> params, String requestURL){
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