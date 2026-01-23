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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import android.widget.Toast;
import com.example.billmanager.drive.DriveServiceHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
        new Thread(() -> {
            List<Bill> bills =
                    db.billDao().getBillsByCustomer(customerId);

            runOnUiThread(() -> {
                rvBills.setAdapter(new BillAdapter(bills));
            });
        }).start();
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

                    String date = new SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                    ).format(new Date());
                    double amount = Double.parseDouble(etAmount.getText().toString());
                    String description = etDesc.getText().toString();



                    Bill bill = new Bill(
                            customerId,
                            date,
                            amount,
                            description,
                            pdfUri.toString()
                    );

                    new Thread(() -> {
                        db.billDao().insertBill(bill);

                        runOnUiThread(this::loadBills);

                        uploadBillToDrive(pdfUri, customerName);

                    }).start();
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
    private void uploadBillToDrive(Uri pdfUri, String customerName) {
        GoogleSignInAccount account =
                GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
            Toast.makeText(this, "Google account not found", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(() -> {
            try {
                DriveServiceHelper driveHelper =
                        new DriveServiceHelper(this, account);

                String rootFolderId =
                        driveHelper.getOrCreateFolder("BillManager", null);

                String customerFolderId =
                        driveHelper.getOrCreateFolder(customerName, rootFolderId);

                String fileId =
                        driveHelper.uploadPdf(
                                pdfUri,
                                "bill_" + System.currentTimeMillis() + ".pdf",
                                customerFolderId
                        );

                runOnUiThread(() ->
                        Toast.makeText(this,
                                "Uploaded to Drive",
                                Toast.LENGTH_LONG).show()
                );

                // TODO: save fileId in Room DB with Bill

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                "Upload failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }



}
