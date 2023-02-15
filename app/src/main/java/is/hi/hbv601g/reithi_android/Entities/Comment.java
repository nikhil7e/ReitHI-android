package is.hi.hbv601g.reithi_android.Entities;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("ID")
    private long ID;

    @SerializedName("user")
    private User mUser;
    @SerializedName("text")
    private String mText;

    @SerializedName("review")
    private Review mReview;

    @SerializedName("rating")
    private Rating mRating;

    @SerializedName("course")
    private Course mCourse;


    public Comment() {

    }

    public Comment(User user, String text, Course course) {
        mUser = user;
        mText = text;
        mCourse = course;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Review getReview() {
        return mReview;
    }

    public void setReview(Review review) {
        mReview = review;
    }

    public Rating getRating() {
        return mRating;
    }

    public void setRating(Rating rating) {
        mRating = rating;
    }

    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course course) {
        mCourse = course;
    }

}
