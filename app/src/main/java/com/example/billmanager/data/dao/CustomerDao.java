package com.example.billmanager.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.billmanager.data.entity.Customer;

import java.util.List;

@Dao
public interface CustomerDao {

    @Insert
    void insertCustomer(Customer customer);

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :search || '%'")
    List<Customer> searchCustomer(String search);
}
