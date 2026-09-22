package com.example.test.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<String> statusMsg = new MutableLiveData<>();
    private final MutableLiveData<Boolean> authSuccess = new MutableLiveData<>();


    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }

    public LiveData<String> getStatusMessage() {
        return statusMsg;
    }
    public LiveData<Boolean> getAuthSuccess() {
        return authSuccess;
    }

    public void register(String username, String password){
        if (username.isEmpty() || password.isEmpty()){
            statusMsg.postValue("Fields cannot be empty");
            return;
        }
        repository.register(username, password, ((success, message) -> {
            statusMsg.postValue(message);
            authSuccess.postValue(success);
        }));
    }
    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()){
            statusMsg.postValue("Fields cannot be empty");
            return;
        }

        repository.login(username, password, (success, message) -> {
            statusMsg.postValue(message);
            authSuccess.postValue(success);
        });
    }
}
