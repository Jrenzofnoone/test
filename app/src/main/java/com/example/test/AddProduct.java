package com.example.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.test.db.ProductViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class AddProduct extends AppCompatActivity {
    TextInputEditText et_name, et_price, et_desc, et_quantity, et_category;
    Button btn_add;
    ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        et_name = findViewById(R.id.et_name);
        et_price = findViewById(R.id.et_price);
        et_desc = findViewById(R.id.et_desc);
        et_quantity = findViewById(R.id.et_quantity);
        et_category = findViewById(R.id.et_category);

        btn_add = findViewById(R.id.btn_add);

        SharedPreferences pref = getSharedPreferences("session", MODE_PRIVATE);


        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getStatusMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        btn_add.setOnClickListener(v -> {
            String name = et_name.getText().toString().trim();
            String price = et_price.getText().toString().trim();
            String desc = et_desc.getText().toString().trim();
            String quantity = et_quantity.getText().toString().trim();
            String category = et_category.getText().toString().trim();
            int userId = pref.getInt("userId", -1);
            productViewModel.addProducts(name, price, desc,quantity, category, userId);
        });
    }
}