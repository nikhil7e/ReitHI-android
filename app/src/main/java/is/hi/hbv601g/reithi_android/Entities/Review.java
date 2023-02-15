package is.hi.hbv601g.reithi_android.Entities;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Review {

    @SerializedName("ID")
    private long mID;
    @SerializedName("user")
    private User mUser;
    @SerializedName("comment")
    private Comment mComment;
    @SerializedName("course")
    private Course mCourse;
    @SerializedName("rating")
    private Rating mRating;

    @SerializedName("upvoters")
    private List<User> mUpvoters;

    @SerializedName("downvoters")
    private List<User> mDownvoters;

    public Review() {
    }

    public Review(User user, Rating rating, Comment comment, Course course) {
        mUser = user;
        mRating = rating;
        mComment = comment;
        mCourse = course;
        mUpvoters = new ArrayList<>();
        mDownvoters = new ArrayList<>();
    }

    public long getID() {
        return mID;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Comment getComment() {
        return mComment;
    }

    public void setComment(Comment comment) {
        mComment = comment;
    }

    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course course) {
        mCourse = course;
    }

    public Rating getRating() {
        return mRating;
    }

    public void setRating(Rating rating) {
        mRating = rating;
    }

    public int getUpvotes() {
        return mUpvoters.size() - mDownvoters.size();
    }

    public void addUpvote(User user) {
        mUpvoters.add(user);
    }

    public void addDownvote(User user) {
        mDownvoters.add(user);
    }

    public List<User> getUpvoters() {
        return mUpvoters;
    }

    public void removeUpvote(User currentUser) {
        mUpvoters.remove(currentUser);
    }

    public List<User> getDownvoters() {
        return mDownvoters;
    }

    public void removeDownvote(User currentUser) {
        mDownvoters.remove(currentUser);
    }
}
