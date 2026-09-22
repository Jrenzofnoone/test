package com.example.test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DisplayProduct extends AppCompatActivity {
    TextView tv_name, tv_price, tv_desc, tv_quantity, tv_category;
    Button btn_buy, btn_cart;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            product = getIntent().getSerializableExtra("product", Product.class);
        } else {
            product = (Product) getIntent().getSerializableExtra("product");
        }
        tv_name = findViewById(R.id.tv_name);
        tv_price = findViewById(R.id.tv_price);
        tv_desc = findViewById(R.id.tv_desc);
        tv_quantity = findViewById(R.id.tv_quantity);
        tv_category = findViewById(R.id.tv_category);
        btn_buy = findViewById(R.id.btn_buy);
        btn_cart = findViewById(R.id.btn_cart);

        tv_name.setText(product.getName());
        tv_price.setText(String.valueOf(product.getPrice()));
        tv_desc.setText(product.getDesc());
        tv_quantity.setText(String.valueOf(product.getQuantity()));
        tv_category.setText(product.getCategory());

        btn_cart.setOnClickListener(v -> {
            Intent i = new Intent(this, Cart.class);
            startActivity(i);
        });
        btn_buy.setOnClickListener(v -> {
            Intent i = new Intent(this, Checkout.class);
            i.putExtra("product", product);
            startActivity(i);
        });
    }
}