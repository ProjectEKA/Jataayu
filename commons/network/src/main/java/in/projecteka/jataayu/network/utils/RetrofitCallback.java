package in.projecteka.jataayu.network.utils;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;

import in.projecteka.jataayu.network.model.Error;
import in.projecteka.jataayu.network.model.ErrorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallback<T> implements Callback<T> {

    private ResponseCallback responseCallback;
    private int DEFAULT_ERROR_CODE = -1;
    private int ERROR_CODE_UNAUTHORIZED = 1017;

    private PendingAPICallQueue pendingAPICallQueue;
    private boolean isNoNetworkScreenShown = false;

    public RetrofitCallback() {
        pendingAPICallQueue = new PendingAPICallQueue();
    }

    public RetrofitCallback(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
        pendingAPICallQueue = new PendingAPICallQueue();
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
                } catch (Exception e) {
                    e.printStackTrace();
                    int errorCode = DEFAULT_ERROR_CODE;
                    if(response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                       errorCode = ERROR_CODE_UNAUTHORIZED;
                    }
                    responseCallback.onFailure(new ErrorResponse(new Error(errorCode, e.getMessage())));
                }
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {

        if (responseCallback != null) responseCallback.onFailure(t);
    }
}
