package com.example.test.db;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test.Order;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    public AuthRepository authRepository;
    public MutableLiveData<String> statusMsg = new MutableLiveData<>();
    public MutableLiveData<Boolean> statusSuccess = new MutableLiveData<>();
    public MutableLiveData<List<Order>> orderList = new MutableLiveData<>();

    public OrderViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void placeOrder(int buyerId, int productId, int quantityToBuy) {
        authRepository.placeOrder(buyerId ,productId, quantityToBuy, (success, message) -> {
            statusMsg.postValue(message);
            statusSuccess.postValue(success);
        });
    }
    public void loadOrderByBuyer(int buyerId) {
        authRepository.fetchOrderByBuyer(buyerId, orderList::postValue);
    }

    public LiveData<String> getStatusMsg() {
        return statusMsg;
    }

    public LiveData<Boolean> getStatusSuccess() {
        return statusSuccess;
    }

    public LiveData<List<Order>> getOrderList() {
        return orderList;
    }

}
