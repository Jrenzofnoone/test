package com.example.test;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.db.OrderViewModel;

public class MyOwnOrders extends AppCompatActivity {
    public RecyclerView rv_orders;
    public OrderViewModel orderViewModel;
    public OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_own_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences pref = getSharedPreferences("session", MODE_PRIVATE);
        int id = pref.getInt("userId", -1);
        rv_orders = findViewById(R.id.rv_orders);
        rv_orders.setLayoutManager(new LinearLayoutManager(this));

        orderAdapter = new OrderAdapter();
        rv_orders.setAdapter(orderAdapter);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getOrderList().observe(this, orders -> {
            if (orders != null) {
                orderAdapter.setOrderList(orders);
            }
        });
        orderViewModel.loadOrderByBuyer(id);
    }
}