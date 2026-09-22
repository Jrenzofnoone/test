package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.db.ProductViewModel;

public class MainActivity extends AppCompatActivity {
    private ProductAdapter productAdapter;
    public RecyclerView rv_products;
    public ProductViewModel productViewModel;

    public Button btn_mylist, btn_add, btn_ownOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rv_products = findViewById(R.id.rv_products);
        rv_products.setLayoutManager(new LinearLayoutManager(this));

        btn_mylist = findViewById(R.id.btn_mylist);
        btn_add = findViewById(R.id.btn_add);
        btn_ownOrder = findViewById(R.id.btn_ownOrder);

        productAdapter = new ProductAdapter();
        rv_products.setAdapter(productAdapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProductList().observe(this, products -> {
            if (products != null) {
                productAdapter.setProductList(products);
            }
        });
        productViewModel.loadAllProducts();

        btn_add.setOnClickListener(v -> {
            Intent i = new Intent(this, AddProduct.class);
            startActivity(i);
        });

        btn_mylist.setOnClickListener(v -> {
            Intent i = new Intent(this, MyList.class);
            startActivity(i);
        });
        btn_ownOrder.setOnClickListener(v -> {
            Intent i = new Intent(this, MyOwnOrders.class);
            startActivity(i);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        productViewModel.loadAllProducts();
    }
}