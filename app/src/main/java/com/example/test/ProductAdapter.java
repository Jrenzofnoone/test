package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VIewHolder> {
    public List<Product> productList = new ArrayList<>();

    public void setProductList(List<Product> products){
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.VIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new VIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.VIewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tv_name.setText(product.getName());
        holder.tv_price.setText(String.valueOf(product.getPrice()));
        holder.tv_desc.setText(product.getDesc());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class VIewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_price, tv_desc;
        public VIewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            itemView.setOnClickListener(v -> {
                int pos = getAbsoluteAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Product product = productList.get(pos);
                    Intent i = new Intent(v.getContext(), DisplayProduct.class);
                    i.putExtra("product", product);
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
