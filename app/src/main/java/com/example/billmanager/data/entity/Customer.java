package com.example.billmanager.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "customers")
public class Customer {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String phone;

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
