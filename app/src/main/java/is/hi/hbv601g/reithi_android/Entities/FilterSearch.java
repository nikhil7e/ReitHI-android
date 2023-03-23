package is.hi.hbv601g.reithi_android.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class FilterSearch {
    @SerializedName("ID")
    private long mID;
    @SerializedName("graduate")
    private Boolean mGraduate;
    @SerializedName("undergraduate")
    private Boolean mUndergraduate;
    @SerializedName("creditsRange")
    private Integer[] mCreditsRange;
    @SerializedName("overallRange")
    private Integer[] mOverallRange;
    @SerializedName("difficultyRange")
    private Integer[] mDifficultyRange;
    @SerializedName("workloadRange")
    private Integer[] mWorkloadRange;
    @SerializedName("teachingQualityRange")
    private Integer[] mTeachingQualityRange;
    @SerializedName("courseMaterialRange")
    private Integer[] mCourseMaterialRange;


    public FilterSearch(){

    }

    public long getID() {
        return mID;
    }

    public void setID(long ID) {
        mID = ID;
    }

    public Boolean getGraduate() {
        return mGraduate;
    }

    public void setGraduate(Boolean graduate) {
        mGraduate = graduate;
    }

    public Boolean getUndergraduate() {
        return mUndergraduate;
    }

    public void setUndergraduate(Boolean undergraduate) {
        mUndergraduate = undergraduate;
    }

    public Integer[] getCreditsRange() {
        return mCreditsRange;
    }

    public void setCreditsRange(Integer[] creditsRange) {
        mCreditsRange = creditsRange;
    }

    public Integer[] getOverallRange() {
        return mOverallRange;
    }

    public void setOverallRange(Integer[] overallRange) {
        mOverallRange = overallRange;
    }

    public Integer[] getDifficultyRange() {
        return mDifficultyRange;
    }

    public void setDifficultyRange(Integer[] difficultyRange) {
        mDifficultyRange = difficultyRange;
    }

    public Integer[] getWorloadRange() {
        return mWorkloadRange;
    }

    public void setWorloadRange(Integer[] worloadRange) {
        mWorkloadRange = worloadRange;
    }

    public Integer[] getTeachingQualityRange() {
        return mTeachingQualityRange;
    }

    public void setTeachingQualityRange(Integer[] teachingQualityRange) {
        mTeachingQualityRange = teachingQualityRange;
    }

    public Integer[] getCourseMaterialRange() {
        return mCourseMaterialRange;
    }

    public void setCourseMaterialRange(Integer[] courseMaterialRange) {
        mCourseMaterialRange = courseMaterialRange;
    }

    @Override
    public String toString() {
        return "FilterSearch{" +
                "mID=" + mID +
                ", mGraduate=" + mGraduate +
                ", mUndergraduate=" + mUndergraduate +
                ", mCreditsRange=" + Arrays.toString(mCreditsRange) +
                ", mOverallRange=" + Arrays.toString(mOverallRange) +
                ", mDifficultyRange=" + Arrays.toString(mDifficultyRange) +
                ", mWorkloadRange=" + Arrays.toString(mWorkloadRange) +
                ", mTeachingQualityRange=" + Arrays.toString(mTeachingQualityRange) +
                ", mCourseMaterialRange=" + Arrays.toString(mCourseMaterialRange) +
                '}';
    }
}
