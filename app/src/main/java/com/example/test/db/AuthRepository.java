package com.example.test.db;

import android.content.Context;

import com.example.test.Order;
import com.example.test.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthRepository {
    private final DatabaseHelper dbHelper;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public interface AuthCallBack {
        void onResult(boolean success, String message);
    }
    public interface ProductFetchCallback {
        void onFetched(List<Product> productList);
    }

    public interface OrderFetchCallback {
        void onFetch(List<Order> orderList);
    }
    public AuthRepository(Context context){
        this.dbHelper = DatabaseHelper.getInstance(context);
    }
    public void register(String username, String password, AuthCallBack callBack){
        executor.execute(()-> {
            if(dbHelper.checkUsernameExists(username)){
                callBack.onResult(false, "Username already taken.");
                return;
            }
            boolean isInserted = dbHelper.registerUser(username, password);
            if (isInserted) {
                callBack.onResult(true, "Registration successful!");
            } else {
                callBack.onResult(false, "Registration Failed!");
            }
        });
    }
    public void login(String username, String password, AuthCallBack callBack){
        executor.execute(() -> {
            boolean exist = dbHelper.checkUserCredentials(username, password);
            if (exist) {
                callBack.onResult(true, "Successfully login!");
            } else {
                callBack.onResult(false, "Login Failed!");
            }
        });
    }
    public void addProduct(String name, double price, String desc, int quantity, String category, int userId, AuthCallBack callBack){
        executor.execute(() -> {
            boolean isInserted = dbHelper.insertProduct(name, price, desc, quantity, category, userId);
            if (isInserted) {
                callBack.onResult(true, "Product Successfully Added");
            } else {
                callBack.onResult(false, "Failed to Add Product");
            }
        });
    }
    public void fetchAllProducts(ProductFetchCallback callBack){
        executor.execute(() -> {
            List<Product> products = dbHelper.getAllProducts();
            callBack.onFetched(products);
        });
    }
    public void fetchUserProduct(int userId, ProductFetchCallback callback) {
        executor.execute(() -> {
            List<Product> products = dbHelper.getUserProducts(userId);
            callback.onFetched(products);
        });
    }

    public void placeOrder(int buyerId, int productId, int quantityToBuy, AuthCallBack callBack) {
        executor.execute(() -> {
            boolean isSuccess = dbHelper.placeOrder(buyerId, productId, quantityToBuy);
            if (isSuccess) {
                callBack.onResult(true, "Order Places Succesfully");
            } else {
                callBack.onResult(false, "Failed to Place order. Not enough of stock or product does not exist");
            }
        });
    }

    public void fetchOrderByBuyer(int buyerId, OrderFetchCallback callback) {
        executor.execute(() -> {
            List<Order> orders = dbHelper.getOrdersByBuyer(buyerId);
            callback.onFetch(orders);
        });
    }
}
