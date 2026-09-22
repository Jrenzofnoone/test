package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewModel> {
    public List<Order> orderList = new ArrayList<>();

    public void setOrderList(List<Order> orderList){
        this.orderList = orderList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OrderAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewModel holder, int position) {
        Order order = orderList.get(position);
        Context context = holder.itemView.getContext();
        String sellerName = DatabaseHelper.getInstance(context).getUsername(order.getSellerId());
        holder.tv_name.setText(order.getProductName());
        holder.tv_desc.setText(order.getProductDesc());
        holder.tv_quantity.setText("Amount: "+String.valueOf(order.getQuantity()));
        holder.tv_price.setText("Final amount: "+String.valueOf(order.getPrice()));
        holder.tv_seller.setText("Seller: "+sellerName);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_desc,tv_quantity, tv_price, tv_seller;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_seller = itemView.findViewById(R.id.tv_seller);

        }
    }
}
