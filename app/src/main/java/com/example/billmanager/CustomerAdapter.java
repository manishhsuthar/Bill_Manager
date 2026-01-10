package com.example.billmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.billmanager.data.entity.Customer;

import java.util.List;

public class CustomerAdapter
        extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<Customer> customers;

    public CustomerAdapter(List<Customer> customers) {
        this.customers = customers;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);

        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CustomerViewHolder holder, int position) {

        Customer customer = customers.get(position);
        holder.tvName.setText(customer.name);
        holder.tvPhone.setText(customer.phone);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPhone;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}
