package is.hi.hbv601g.reithi_android.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Course {

    @SerializedName("id")
    private long mID;
    @SerializedName("name")
    private String mName;
    @SerializedName("number")
    private String mNumber;
    @SerializedName("course_URL")
    private String mCourse_URL;
    @SerializedName("type")
    private String mType;
    @SerializedName("credits")
    private Double mCredits;
    @SerializedName("semester")
    private String mSemester;
    @SerializedName("level")
    private String mLevel;
    @SerializedName("professor_name")
    private String mProfessor_Name;
    //private Professor professor;
    @SerializedName("minimumGrade")
    private Double mMinimumGrade;
    @SerializedName("assessment")
    private String mAssessment;
    @SerializedName("finalExam")
    private String mFinalExam;
    @SerializedName("school")
    private String mSchool;
    @SerializedName("faculty")
    private String mFaculty;
    @SerializedName("professor_email")
    private String mProfessor_Email;
    @SerializedName("professor_URL")
    private String mProfessor_URL;
/*    @SerializedName("totalOverallScore")
    private Integer mTotalOverallScore;*/
    @SerializedName("reviews")
    private List<Review> mReviews = new ArrayList<>();

    @SerializedName("totalOverall")
    private Double mTotalOverall;

    @SerializedName("totalDifficulty")
    private Double mTotalDifficulty;

    @SerializedName("totalWorkload")
    private Double mTotalWorkload;

    @SerializedName("totalTeachingQuality")
    private Double mTotalTeachingQuality;

    @SerializedName("totalCourseMaterial")
    private Double mTotalCourseMaterial;

    @SerializedName("nrReviews")
    private Double mTotalReviews;

    public Course() {

    }

    public Course(long ID, String name, String number, String course_URL, String type, Double credits, String semester, String level, String professor_Name, Double minimumGrade, String assessment, String finalExam, String school, String faculty, String professor_Email, String professor_URL, Integer totalOverallScore, List<Review> reviews, Double totalOverall, Double totalDifficulty, Double totalWorkload, Double totalTeachingQuality, Double totalCourseMaterial, Double totalReviews) {
        mID = ID;
        mName = name;
        mNumber = number;
        mCourse_URL = course_URL;
        mType = type;
        mCredits = credits;
        mSemester = semester;
        mLevel = level;
        mProfessor_Name = professor_Name;
        mMinimumGrade = minimumGrade;
        mAssessment = assessment;
        mFinalExam = finalExam;
        mSchool = school;
        mFaculty = faculty;
        mProfessor_Email = professor_Email;
        mProfessor_URL = professor_URL;
        //mTotalOverallScore = totalOverallScore;
        mReviews = reviews;
        mTotalOverall = totalOverall;
        mTotalDifficulty = totalDifficulty;
        mTotalWorkload = totalWorkload;
        mTotalTeachingQuality = totalTeachingQuality;
        mTotalCourseMaterial = totalCourseMaterial;
        mTotalReviews = totalReviews;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getCourse_URL() {
        return mCourse_URL;
    }

    public void setCourse_URL(String course_URL) {
        mCourse_URL = course_URL;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Double getCredits() {
        return mCredits;
    }

    public void setCredits(Double credits) {
        mCredits = credits;
    }

    public String getSemester() {
        return mSemester;
    }

    public void setSemester(String semester) {
        mSemester = semester;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

    public String getProfessor_Name() {
        return mProfessor_Name;
    }

    public void setProfessor_Name(String professor_Name) {
        mProfessor_Name = professor_Name;
    }

    public Double getMinimumGrade() {
        return mMinimumGrade;
    }

    public void setMinimumGrade(Double minimumGrade) {
        mMinimumGrade = minimumGrade;
    }

    public String getAssessment() {
        return mAssessment;
    }

    public void setAssessment(String assessment) {
        mAssessment = assessment;
    }

    public String getFinalExam() {
        return mFinalExam;
    }

    public void setFinalExam(String finalExam) {
        mFinalExam = finalExam;
    }

    public String getSchool() {
        return mSchool;
    }

    public void setSchool(String school) {
        mSchool = school;
    }

    public String getFaculty() {
        return mFaculty;
    }

    public void setFaculty(String faculty) {
        mFaculty = faculty;
    }

    public String getProfessor_Email() {
        return mProfessor_Email;
    }

    public void setProfessor_Email(String professor_Email) {
        mProfessor_Email = professor_Email;
    }

    public String getProfessor_URL() {
        return mProfessor_URL;
    }

    public void setProfessor_URL(String professor_URL) {
        mProfessor_URL = professor_URL;
    }

/*    public Integer getTotalOverallScore() {
        return mTotalOverallScore;
    }

    public void setTotalOverallScore(Integer totalOverallScore) {
        mTotalOverallScore = totalOverallScore;
    }*/

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }

    public Double getTotalOverall() {
        return mTotalOverall;
    }

    public void setTotalOverall(Double totalOverall) {
        mTotalOverall = totalOverall;
    }

    public Double getTotalDifficulty() {
        return mTotalDifficulty;
    }

    public void setTotalDifficulty(Double totalDifficulty) {
        mTotalDifficulty = totalDifficulty;
    }

    public Double getTotalWorkload() {
        return mTotalWorkload;
    }

    public void setTotalWorkload(Double totalWorkload) {
        mTotalWorkload = totalWorkload;
    }

    public Double getTotalTeachingQuality() {
        return mTotalTeachingQuality;
    }

    public void setTotalTeachingQuality(Double totalTeachingQuality) {
        mTotalTeachingQuality = totalTeachingQuality;
    }

    public Double getTotalCourseMaterial() {
        return mTotalCourseMaterial;
    }

    public void setTotalCourseMaterial(Double totalCourseMaterial) {
        mTotalCourseMaterial = totalCourseMaterial;
    }

    public Double getTotalReviews() {
        return mTotalReviews;
    }

    public void setTotalReviews(Double totalReviews) {
        mTotalReviews = totalReviews;
    }

    public long getID() {
        return mID;
    }

    public void setID(long ID) {
        mID = ID;
    }
}