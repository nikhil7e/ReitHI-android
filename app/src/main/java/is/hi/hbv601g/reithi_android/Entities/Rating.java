package is.hi.hbv601g.reithi_android.Entities;

import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("ID")
    private long mID;
    @SerializedName("user")
    private User mUser;
    @SerializedName("course")
    private Course mCourse;
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

    public Rating() {
    }

    public Rating(User user, Course course, int overallScore, int difficulty, int workload, int teachingQuality, int courseMaterial) {
        this.mUser = user;
        this.mCourse = course;
        this.mOverallScore = overallScore;
        this.mDifficulty = difficulty;
        this.mWorkload = workload;
        this.mTeachingQuality = teachingQuality;
        this.mCourseMaterial = courseMaterial;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course course) {
        this.mCourse = course;
    }

    public int getOverallScore() {
        return mOverallScore;
    }

    public void setOverallScore(int overallScore) {
        this.mOverallScore = overallScore;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(int difficulty) {
        this.mDifficulty = difficulty;
    }

    public int getWorkload() {
        return mWorkload;
    }

    public void setWorkload(int workload) {
        this.mWorkload = workload;
    }

    public int getTeachingQuality() {
        return mTeachingQuality;
    }

    public void setTeachingQuality(int teachingQuality) {
        this.mTeachingQuality = teachingQuality;
    }

    public int getCourseMaterial() {
        return mCourseMaterial;
    }

    public void setCourseMaterial(int courseMaterial) {
        this.mCourseMaterial = courseMaterial;
    }
}
