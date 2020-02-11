package in.projecteka.jataayu.network.utils;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallback<T> implements Callback<T> {

    private ResponseCallback responseCallback;

    public RetrofitCallback() {}

    public RetrofitCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    abstract protected MutableLiveData<T> observableLiveData();

    @Override
    public void onResponse(@NotNull Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            if (responseCallback != null) responseCallback.onSuccess(response.body());
            if (response.body() != null) {
                observableLiveData().setValue(response.body());
            }
        } else {
            if (response.errorBody() != null && responseCallback != null) {
                responseCallback.onFailure(response.errorBody());
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
        if (responseCallback != null) responseCallback.onFailure(t);
    }
}
