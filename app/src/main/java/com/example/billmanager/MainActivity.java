package com.example.billmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.billmanager.data.database.AppDatabase;
import com.example.billmanager.data.entity.Customer;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        rvCustomers = findViewById(R.id.rvCustomers);
        rvCustomers.setLayoutManager(new LinearLayoutManager(this));

        List<Customer> customers =
                db.customerDao().searchCustomer("");

        CustomerAdapter adapter =
                new CustomerAdapter(customers);

        rvCustomers.setAdapter(adapter);
    }
}
