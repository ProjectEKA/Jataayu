package in.projecteka.jataayu.network.utils;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import in.projecteka.jataayu.network.model.ErrorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallback<T> implements Callback<T> {

    private ResponseCallback responseCallback;

    public RetrofitCallback() {
    }

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
                try {
                    ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                    responseCallback.onFailure(errorResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
        if (responseCallback != null) responseCallback.onFailure(t);
    }
}
