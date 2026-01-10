package com.example.billmanager;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.billmanager.data.entity.Bill;

import java.util.List;

public class BillAdapter
        extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private List<Bill> bills;

    public BillAdapter(List<Bill> bills) {
        this.bills = bills;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);

        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BillViewHolder holder, int position) {

        Bill bill = bills.get(position);

        holder.tvDate.setText(bill.date);
        holder.tvAmount.setText("₹ " + bill.amount);
        holder.tvDesc.setText(bill.description);

        // 👉 OPEN PDF ON CLICK
        holder.itemView.setOnClickListener(v -> {
            if (bill.pdfPath != null && !bill.pdfPath.isEmpty()) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.parse(bill.pdfPath),
                        "application/pdf"
                );
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent chooser =
                        Intent.createChooser(intent, "Open PDF");
                v.getContext().startActivity(chooser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    static class BillViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvAmount, tvDesc;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }
}
