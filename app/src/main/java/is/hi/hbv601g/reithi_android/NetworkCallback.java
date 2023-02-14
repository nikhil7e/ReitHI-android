package is.hi.hbv601g.reithi_android;

public interface NetworkCallback<T> {

    void onFailure(String errorString);

    void onSuccess(T result);

}
