package com.example.billmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.billmanager.data.database.AppDatabase;
import com.example.billmanager.data.entity.Bill;

import java.util.List;

public class BillsActivity extends AppCompatActivity {

    private static final int PICK_PDF = 101;

    private AppDatabase db;
    private int customerId;
    private RecyclerView rvBills;
    private String selectedPdfPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        customerId = getIntent().getIntExtra("customerId", -1);
        String customerName = getIntent().getStringExtra("customerName");

        if (customerId == -1) {
            finish(); // close activity safely
            return;
        };

        TextView tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerName.setText(customerName);

        db = AppDatabase.getInstance(this);

        rvBills = findViewById(R.id.rvBills);
        rvBills.setLayoutManager(new LinearLayoutManager(this));

        loadBills();

        FloatingActionButton btnAddBill = findViewById(R.id.btnAddBill);
        btnAddBill.setOnClickListener(v -> showAddBillDialog());
    }

    private void loadBills() {
        if (customerId == -1) return;

        List<Bill> bills = db.billDao().getBillsByCustomer(customerId);
        rvBills.setAdapter(new BillAdapter(bills));
    }

    private void showAddBillDialog() {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_bill, null);

        EditText etDate = view.findViewById(R.id.etDate);
        EditText etAmount = view.findViewById(R.id.etAmount);
        EditText etDesc = view.findViewById(R.id.etDesc);
        Button btnSelectPdf = view.findViewById(R.id.btnSelectPdf);

        btnSelectPdf.setOnClickListener(v -> openPdfPicker());

        new AlertDialog.Builder(this)
                .setTitle("Add Bill")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {

                    String date = etDate.getText().toString();
                    String amountStr = etAmount.getText().toString();
                    String desc = etDesc.getText().toString();

                    if (!date.isEmpty() && !amountStr.isEmpty()) {
                        double amount = Double.parseDouble(amountStr);

                        db.billDao().insertBill(
                                new Bill(
                                        customerId,
                                        date,
                                        amount,
                                        desc,
                                        selectedPdfPath == null ? "" : selectedPdfPath
                                )
                        );
                        loadBills();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openPdfPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_PDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            // Persist permission (CRITICAL)
            getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            selectedPdfPath = uri.toString();
        }
    }

}
