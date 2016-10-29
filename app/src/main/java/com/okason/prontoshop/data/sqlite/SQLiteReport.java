package com.okason.prontoshop.data.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.ui.transactions.TransactionContract;
import com.okason.prontoshop.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Valentine on 10/26/2016.
 */

public class SQLiteReport {
    @Inject TransactionContract.Repository transactionRepository;
    private final SQLiteDatabase database;

    public SQLiteReport(SQLiteDatabase database) {
        this.database = database;
        ProntoShopApplication.getInstance().getAppComponent().inject(this);
    }

    public List<SalesTransaction> getSalesTransactionsByDay(int year, int month, int day){
        List<SalesTransaction> dayTransactions = transactionRepository.getAllSalesTransactions();
        for (SalesTransaction tempSalesTransaction : dayTransactions){
            Calendar transactionDate = Calendar.getInstance();
            transactionDate.setTimeInMillis(tempSalesTransaction.getModifiedDate());
            if (transactionDate != null)
            {
                if (transactionDate.get(Calendar.YEAR) == year &&
                        transactionDate.get(Calendar.MONTH) == month &&
                        transactionDate.get(Calendar.DAY_OF_MONTH) == day)
                {
                    dayTransactions.add(tempSalesTransaction);
                }
            }

        }
        return dayTransactions;
    }


    public List<SalesTransaction> getSalesTransactionsByWeek(int year, int month, int week) {
        List<SalesTransaction> weekSalesTransactions = transactionRepository.getAllSalesTransactions();
        for (SalesTransaction tempSalesTransaction : weekSalesTransactions){
            Calendar transactionDate = Calendar.getInstance();
            transactionDate.setTimeInMillis(tempSalesTransaction.getModifiedDate());
            if (transactionDate != null)
            {
                if (transactionDate.get(Calendar.YEAR) == year &&
                        transactionDate.get(Calendar.MONTH) == month &&
                        transactionDate.get(Calendar.WEEK_OF_MONTH) == week)
                {
                    weekSalesTransactions.add(tempSalesTransaction);
                }
            } else {
            }
        }
        return weekSalesTransactions;
    }


    public static List<SalesTransaction> getSalesTransactionsByMonth(int year, int month) {
        List<SalesTransaction> monthSalesTransactions = new ArrayList<SalesTransaction>();
        for (SalesTransaction tempSalesTransaction : monthSalesTransactions){
            Calendar transactionDate = Calendar.getInstance();
            transactionDate.setTimeInMillis(tempSalesTransaction.getModifiedDate());
            if (transactionDate != null)
            {
                if (transactionDate.get(Calendar.YEAR) == year &&
                        transactionDate.get(Calendar.MONTH) == month)
                {
                    monthSalesTransactions.add(tempSalesTransaction);
                }
            }
        }
        return monthSalesTransactions;
    }


    public Customer getCustomerByEmailAddress(String email){
        Cursor c = database.rawQuery("SELECT * FROM " + Constants.CUSTOMER_TABLE +" WHERE " +
                Constants.COLUMN_EMAIL + " = '" + email +"'", null);
        if (c.moveToFirst()) {
            return Customer.getCustomerFromCursor(c);
        }
        c.close();
        return null;
    }

    public boolean deleteTransactionsByCustomer(long id) {
        boolean result = false;
        // Ensure database exists.
        if (database != null) {
            if (database.delete(Constants.TRANSACTION_TABLE, Constants.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)}) > 0) {
                result = true;
            }
            // Close database connection.
            database.close();
        }

        return result;

    }
}
