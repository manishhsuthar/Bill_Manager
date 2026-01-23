package com.example.billmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.billmanager.data.database.AppDatabase;
import com.example.billmanager.data.entity.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvCustomers;
    private CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getInstance(this);

        rvCustomers = findViewById(R.id.rvCustomers);
        rvCustomers.setLayoutManager(new LinearLayoutManager(this));

        loadCustomers();

        FloatingActionButton btnAddCustomer = findViewById(R.id.btnAddCustomer);
        btnAddCustomer.setOnClickListener(v -> showAddCustomerDialog());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCustomers() {
        List<Customer> customers =
                db.customerDao().searchCustomer("");

        adapter = new CustomerAdapter(customers);
        rvCustomers.setAdapter(adapter);
    }

    private void showAddCustomerDialog() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_customer, null);

        EditText etName = view.findViewById(R.id.etName);
        EditText etPhone = view.findViewById(R.id.etPhone);

        new AlertDialog.Builder(this)
                .setTitle("Add Customer")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String phone = etPhone.getText().toString();

                    if (!name.isEmpty()) {
                        db.customerDao()
                                .insertCustomer(new Customer(name, phone));
                        loadCustomers();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
