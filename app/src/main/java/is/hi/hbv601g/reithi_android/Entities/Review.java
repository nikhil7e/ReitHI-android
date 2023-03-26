package is.hi.hbv601g.reithi_android.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Review {

    @SerializedName("ID")
    private long mID;
    @SerializedName("user")
    private User mUser;
    @SerializedName("course")
    private Course mCourse;

    @SerializedName("upvoters")
    private List<User> mUpvoters;

    @SerializedName("downvoters")
    private List<User> mDownvoters;

    @SerializedName("overallScore")
    private int mOverallScore;
    @SerializedName("difficulty")
    private int mDifficulty;
    @SerializedName("workload")
    private int mWorkload;
    @SerializedName("teachingQuality")
    private int mTeachingQuality;
    @SerializedName("courseMaterial")
    private int mCourseMaterial;
    @SerializedName("comment")
    private String mComment;
    @SerializedName("user_id")
    private long mUserID;
    @SerializedName("course_id")
    private long mCourseID;

    @SerializedName("user_name")
    private String mUserName;
    @SerializedName("course_name")
    private String mCourseName;



    public Review() {
        mUpvoters = new ArrayList<>();
        mDownvoters = new ArrayList<>();
    }

    public Review(User user, Course course, int overallScore, int difficulty, int workload, int teachingQuality, int courseMaterial, String comment) {
        mUpvoters = new ArrayList<>();
        mDownvoters = new ArrayList<>();
        mUser = user;
        mCourse = course;
        mOverallScore = overallScore;
        mDifficulty = difficulty;
        mWorkload = workload;
        mTeachingQuality = teachingQuality;
        mCourseMaterial = courseMaterial;
        mComment = comment;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getCourseName() {
        return mCourseName;
    }
    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setCourseName(String courseName) {
        mCourseName = courseName;
    }

    public long getUserID() {
        return mUserID;
    }

    public void setUserID(long userID) {
        mUserID = userID;
    }



    public long getCourseID() {
        return mCourseID;
    }

    public void setCourseID(long courseID) {
        mCourseID = courseID;
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


    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course course) {
        mCourse = course;
    }


    public int upvoteCount() {
        if (mUpvoters == null && mDownvoters == null) {
            return 0;
        } else if (mUpvoters == null) {
            return -mDownvoters.size();
        } else if (mDownvoters == null) {
            return mUpvoters.size();
        }
        return mUpvoters.size() - mDownvoters.size();
    }

    public void addUpvote(User user) {
        mUpvoters.add(user);
    }

    public void addDownvote(User user) {mDownvoters.add(user);}

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

    public int getOverallScore() {
        return mOverallScore;
    }

    public void setOverallScore(int overallScore) {
        mOverallScore = overallScore;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(int difficulty) {
        mDifficulty = difficulty;
    }

    public int getWorkload() {
        return mWorkload;
    }

    public void setWorkload(int workload) {
        mWorkload = workload;
    }

    public int getTeachingQuality() {
        return mTeachingQuality;
    }

    public void setTeachingQuality(int teachingQuality) {
        mTeachingQuality = teachingQuality;
    }

    public int getCourseMaterial() {
        return mCourseMaterial;
    }

    public void setCourseMaterial(int courseMaterial) {
        mCourseMaterial = courseMaterial;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    @Override
    public String toString() {
        return "Review{" +
                "mID=" + mID +
                ", mUser=" + mUser +
                ", mCourse=" + mCourse +
                ", mUpvoters=" + mUpvoters +
                ", mDownvoters=" + mDownvoters +
                ", mOverallScore=" + mOverallScore +
                ", mDifficulty=" + mDifficulty +
                ", mWorkload=" + mWorkload +
                ", mTeachingQuality=" + mTeachingQuality +
                ", mCourseMaterial=" + mCourseMaterial +
                ", mComment='" + mComment + '\'' +
                ", mUserID=" + mUserID +
                ", mCourseID=" + mCourseID +
                '}';
    }
}
