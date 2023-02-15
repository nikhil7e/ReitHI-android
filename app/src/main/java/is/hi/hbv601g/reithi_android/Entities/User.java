package is.hi.hbv601g.reithi_android.Entities;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
    @SerializedName("ID")
    private long mID;
    @SerializedName("userName")
    private String mUserName;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("ratings")
    private List<Rating> mRatings = new ArrayList<>();
    @SerializedName("reviews")
    private List<Review> mReviews = new ArrayList<>();


    public User() {
    }

    public User(String userName, String password) {
        mUserName = userName;
        mPassword = password;
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

    public List<Rating> getRatings() {
        return mRatings;
    }

    public void setRatings(List<Rating> ratings) {
        mRatings = ratings;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }
}
