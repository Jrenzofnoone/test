package com.example.test.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test.Product;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<List<Product>> productList = new MutableLiveData<>();
    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> successStatus = new MutableLiveData<>();

    public ProductViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }
    public LiveData<List<Product>> getProductList() {
        return productList;
    }
    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public LiveData<Boolean> getSuccessStatus() {
        return successStatus;
    }

    public void loadUserProducts(int userId) {
        authRepository.fetchUserProduct(userId, productList::postValue);
    }
    public void loadAllProducts() {
        authRepository.fetchAllProducts(productList::postValue);
    }
    public void addProducts(String name, String pricestr, String desc,String quanstr, String category, int userId) {
        if(name.isEmpty() || pricestr.isEmpty()) {
            statusMessage.postValue("Field cannot be empty");
            return;
        }
        try{
            double price = Double.parseDouble(pricestr);
            int quantity = Integer.parseInt(quanstr);

            authRepository.addProduct(name, price, desc, quantity, category, userId, (success, message) -> {
                statusMessage.postValue(message);
                if (success) {
                    loadUserProducts(userId);
                    loadAllProducts();
                }
            });
        } catch (NumberFormatException e){
            statusMessage.postValue("Invalid Price Format");
        }
    }



}
