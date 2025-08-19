package com.splitwise.easy.data

import androidx.room.*

@Dao
interface ParticipantDao {
    @Query("SELECT * FROM participants WHERE billId = :billId")
    suspend fun getParticipantsForBill(billId: String): List<Participant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participant: Participant)

    @Delete
    suspend fun deleteParticipant(participant: Participant)
}

