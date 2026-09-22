package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.test.db.DatabaseHelper;
import com.example.test.db.OrderViewModel;
import com.example.test.db.ProductViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class Checkout extends AppCompatActivity {
    TextView tv_name, tv_price, tv_desc, tv_quantity, tv_category;
    Button btn_back, btn_checkout;
    Product product;
    private OrderViewModel orderViewModel;
    TextInputEditText et_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        et_quantity = findViewById(R.id.et_quantity);

        tv_name = findViewById(R.id.tv_name);
        tv_price = findViewById(R.id.tv_price);
        tv_desc = findViewById(R.id.tv_desc);
        tv_quantity = findViewById(R.id.tv_quantity);
        tv_category = findViewById(R.id.tv_category);
        btn_back = findViewById(R.id.btn_back);
        btn_checkout = findViewById(R.id.btn_checkout);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        orderViewModel.getStatusMsg().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        orderViewModel.getStatusSuccess().observe(this, success -> {
            if (success != null && success) {
                Intent i = new Intent(this, Ending.class);
                startActivity(i);
                finish();
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ){
            product = getIntent().getSerializableExtra("product", Product.class);
        } else {
            product = (Product) getIntent().getSerializableExtra("product");
        }

        SharedPreferences pref = getSharedPreferences("session", MODE_PRIVATE);
        int userId = pref.getInt("userId", -1);

        if (product != null) {
            tv_name.setText(product.getName());
            tv_price.setText(String.valueOf(product.getPrice()));
            tv_desc.setText(product.getDesc());
            tv_quantity.setText(String.valueOf(product.getQuantity()));
            tv_category.setText(product.getCategory());
        }

        btn_back.setOnClickListener(v -> {
            finish();
        });
        btn_checkout.setOnClickListener(v -> {
            String qtyStr = et_quantity.getText().toString().trim();
            String username = DatabaseHelper.getInstance(this).getUsername(userId);
            int quantityToBuy = 1;
            if (!qtyStr.isEmpty()){
                try {
                    quantityToBuy = Integer.parseInt(qtyStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid FOrmat", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (qtyStr.isEmpty()) {
                Toast.makeText(this, "Field must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (quantityToBuy <=0 ) {
                Toast.makeText(this, "Quantity must be at least 1", Toast.LENGTH_SHORT).show();
                return;
            }
            if (quantityToBuy > product.getQuantity()) {
                Toast.makeText(this, "Not enough stock!", Toast.LENGTH_SHORT).show();
                return;
            }
            orderViewModel.placeOrder(userId,product.getId(), quantityToBuy);
        });
    }
}