package com.example.billmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.billmanager.data.database.AppDatabase;
import com.example.billmanager.data.entity.Bill;

import java.util.List;

public class BillsActivity extends AppCompatActivity {

    private AppDatabase db;
    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        customerId = getIntent().getIntExtra("customerId", -1);
        String customerName = getIntent().getStringExtra("customerName");

        TextView tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerName.setText(customerName);

        db = AppDatabase.getInstance(this);

        RecyclerView rvBills = findViewById(R.id.rvBills);
        rvBills.setLayoutManager(new LinearLayoutManager(this));

        List<Bill> bills =
                db.billDao().getBillsByCustomer(customerId);

        rvBills.setAdapter(new BillAdapter(bills));

        Button btnAddBill = findViewById(R.id.btnAddBill);
        btnAddBill.setOnClickListener(v ->
                        // NEXT STEP: Add bill dialog
                {}
        );
    }
}
