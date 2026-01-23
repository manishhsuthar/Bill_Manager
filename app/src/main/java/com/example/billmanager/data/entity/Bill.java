package com.example.billmanager.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "bills",
        foreignKeys = @ForeignKey(
                entity = Customer.class,
                parentColumns = "id",
                childColumns = "customerId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Bill {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int customerId;
    public String date;
    public double amount;
    public String description;
    public String driveFileId;


    // NEW
    public String pdfPath;

    public Bill(int customerId, String date, double amount,
                String description, String pdfPath) {
        this.customerId = customerId;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.pdfPath = pdfPath;
    }
}
