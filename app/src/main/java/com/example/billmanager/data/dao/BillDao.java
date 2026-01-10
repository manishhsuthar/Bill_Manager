package com.example.billmanager.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.billmanager.data.entity.Bill;

import java.util.List;

@Dao
public interface BillDao {

    @Insert
    void insertBill(Bill bill);

    @Query("SELECT * FROM bills WHERE customerId = :customerId ORDER BY date DESC")
    List<Bill> getBillsByCustomer(int customerId);
}
