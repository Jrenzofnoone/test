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

import com.example.test.db.ProductViewModel;

public class MyList extends AppCompatActivity {
    public ProductAdapter productAdapter;
    public RecyclerView rv_mylist;
    public ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rv_mylist = findViewById(R.id.rv_mylist);
        rv_mylist.setLayoutManager(new LinearLayoutManager(this));

        productAdapter = new ProductAdapter();
        rv_mylist.setAdapter(productAdapter);

        SharedPreferences pref = getSharedPreferences("session", MODE_PRIVATE);


        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProductList().observe(this, products -> {
            if (products != null) {
                productAdapter.setProductList(products);
            }
        });
        int userId = pref.getInt("userId", -1);
        productViewModel.loadUserProducts(userId);
    }
}