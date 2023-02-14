package is.hi.hbv601g.reithi_android.Entities;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mID;
    @ManyToOne(fetch = FetchType.LAZY)
    private User mUser;
    @ManyToOne(fetch = FetchType.LAZY)
    private Course mCourse;
    @ColumnDefault("0")
    private int mOverallScore;
    @ColumnDefault("0")
    private int mDifficulty;
    @ColumnDefault("0")
    private int mWorkload;
    @ColumnDefault("0")
    private int mTeachingQuality;
    @SerializedName("courseMaterial")
    private int mCourseMaterial;


    public Rating() {
    }

    public Rating(User user, Course course, int overallScore, int difficulty, int workload, int teachingQuality, int courseMaterial) {
        this.user = user;
        this.course = course;
        this.overallScore = overallScore;
        this.difficulty = difficulty;
        this.workload = workload;
        this.teachingQuality = teachingQuality;
        this.courseMaterial = courseMaterial;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public int getTeachingQuality() {
        return teachingQuality;
    }

    public void setTeachingQuality(int teachingQuality) {
        this.teachingQuality = teachingQuality;
    }

    public int getCourseMaterial() {
        return courseMaterial;
    }

    public void setCourseMaterial(int courseMaterial) {
        this.courseMaterial = courseMaterial;
    }
}
