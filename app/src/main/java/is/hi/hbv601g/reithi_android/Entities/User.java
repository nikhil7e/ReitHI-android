package is.hi.hbv601g.reithi_android.Entities;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
    @SerializedName("id")
    private long mID;
    @SerializedName("userName")
    private String mUserName;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("reviews")
    private List<Review> mReviews = new ArrayList<>();
    @SerializedName("deviceToken")
    private String mDeviceToken;

    @SerializedName("enrolledSchoolOrFaculty")
    private String mEnrolledSchoolOrFaculty;


    public User() {
    }

    public User(String userName, String password) {
        mUserName = userName;
        mPassword = password;
    }

    public User(long ID, String userName, String password) {
        mID = ID;
        mUserName = userName;
        mPassword = password;
    }

    public User(long ID, String userName, String password, String deviceToken) {
        mID = ID;
        mUserName = userName;
        mPassword = password;
        mDeviceToken = deviceToken;
    }

    public String getEnrolledSchoolOrFaculty() {
        return mEnrolledSchoolOrFaculty;
    }

    public void setEnrolledSchoolOrFaculty(String enrolledSchoolOrFaculty) {
        mEnrolledSchoolOrFaculty = enrolledSchoolOrFaculty;
    }

    public String getDeviceToken() {
        return mDeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        mDeviceToken = deviceToken;
    }

    public long getID() {
        return mID;
    }

    public void setID(long ID) {
        mID = ID;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }

    public String toString() {
        return "User{" +
                "mID=" + mID +
                ", mUserName='" + mUserName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mReviews=" + mReviews +
                '}';
    }
}
