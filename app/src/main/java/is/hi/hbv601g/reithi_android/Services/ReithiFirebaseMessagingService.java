package is.hi.hbv601g.reithi_android.Services;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import is.hi.hbv601g.reithi_android.Activities.LoginActivity;
import is.hi.hbv601g.reithi_android.Entities.User;
import is.hi.hbv601g.reithi_android.NetworkCallback;
import is.hi.hbv601g.reithi_android.R;

public class ReithiFirebaseMessagingService extends FirebaseMessagingService {

    private SharedPreferences mSharedPreferences;
    private UserService mUserService;
    private final String TAG = "ReithiFirebaseMessagingService";

    @Override
    public void onNewToken(String token) {
        mUserService = new UserService(getApplicationContext());
        mSharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
        String userString = mSharedPreferences.getString("loggedInUser", "");

        Log.d("MyFirebaseMsgService", "Refreshed token: " + token);

        // Send the registration token to your backend server along with the user's ID

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("deviceToken", token);
        editor.apply();

        if (!userString.isEmpty()) {
            sendRegistrationTokenToServer(token, userString);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if the message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            // Get the notification title and message
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            // Display the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1234")
                    .setSmallIcon(R.drawable.user_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    public void sendRegistrationTokenToServer(String token, String userString) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("user", userString);
            requestBody.put("deviceToken", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mUserService.genericUserPOST(new NetworkCallback<String>() {
            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, errorString);
            }

            @Override
            public void onSuccess(String userString) {
                Log.d(TAG, "Login Successful with user " + userString);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("loggedInUser", userString);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Successfully updated token for: " + userString, Toast.LENGTH_SHORT).show();
            }
        }, requestBody, "/updatetoken");

    }

}
