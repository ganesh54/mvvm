package com.example.mvvm.activity.viewmodel;

import android.app.Application;

import com.example.mvvm.activity.model.UserRepository;
import com.example.mvvm.model.Users;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class UserViewModel extends AndroidViewModel {
    MutableLiveData<Users> mutableLiveData;
    UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public MutableLiveData<Users> callGetUserApi(int page, int perPage) {
        mutableLiveData = userRepository.getUser(page, perPage);
        return mutableLiveData;
    }

    public MutableLiveData<Users> callNextUser(int page, int perPage) {
        mutableLiveData = userRepository.getUserNext(page, perPage);
        return mutableLiveData;
    }
}
