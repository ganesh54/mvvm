package com.example.mvvm.activity.model;

import android.app.Application;

import com.example.mvvm.model.Users;
import com.example.mvvm.network.ApiManager;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private Application application;

    public UserRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<Users> getUser(int page, int perPage) {
        final MutableLiveData<Users> mutableLiveData = new MutableLiveData<>();
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("per_page", perPage);
        ApiManager.getInstance().getApiService().getUsers(map).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    mutableLiveData.setValue(response.body());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Users> getUserNext(int page, int perPage) {
        final MutableLiveData<Users> mutableLiveData = new MutableLiveData<>();
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("per_page", perPage);
        ApiManager.getInstance().getApiService().getUsers(map).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    mutableLiveData.setValue(response.body());
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}
