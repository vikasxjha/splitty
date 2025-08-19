package com.splitwise.easy.data

import androidx.room.*

@Dao
interface BillDao {
    @Query("SELECT * FROM bills ORDER BY date DESC")
    suspend fun getAllBills(): List<Bill>

    @Query("SELECT * FROM bills WHERE id = :id")
    suspend fun getBillById(id: String): Bill?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: Bill)

    @Delete
    suspend fun deleteBill(bill: Bill)
}

