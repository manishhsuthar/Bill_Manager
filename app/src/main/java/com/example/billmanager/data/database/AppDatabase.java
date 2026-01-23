package com.example.billmanager.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.billmanager.data.dao.BillDao;
import com.example.billmanager.data.dao.CustomerDao;
import com.example.billmanager.data.entity.Bill;
import com.example.billmanager.data.entity.Customer;

@Database(
        entities = {Customer.class, Bill.class},
        version = 2
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CustomerDao customerDao();
    public abstract BillDao billDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                            AppDatabase.class,
                            "bill_manager_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
