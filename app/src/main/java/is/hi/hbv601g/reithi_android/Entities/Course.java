package is.hi.hbv601g.reithi_android.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Course {

    @SerializedName("ID")
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
    @SerializedName("totalOverallScore")
    private Integer mTotalOverallScore;
    @SerializedName("reviews")
    private List<Review> mReviews = new ArrayList<>();

    public Course() {

    }

    public Course(long ID, String name, String number, String course_URL, String type, Double credits, String semester, String level, String professor_Name, Double minimumGrade, String assessment, String finalExam, String school, String faculty, String professor_Email, String professor_URL, Integer totalOverallScore, List<Review> reviews) {
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
        mTotalOverallScore = totalOverallScore;
        mReviews = reviews;
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

    public Integer getTotalOverallScore() {
        return mTotalOverallScore;
    }

    public void setTotalOverallScore(Integer totalOverallScore) {
        mTotalOverallScore = totalOverallScore;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }
}