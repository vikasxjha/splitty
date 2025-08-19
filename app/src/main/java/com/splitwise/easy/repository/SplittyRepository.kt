package com.splitwise.easy.repository

import com.splitwise.easy.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SplittyRepository(
    private val billDao: BillDao,
    private val participantDao: ParticipantDao,
    private val userDao: UserDao
) {

    // Bill operations
    suspend fun getAllBills(): List<Bill> = billDao.getAllBills()

    suspend fun getBillById(id: String): Bill? = billDao.getBillById(id)

    suspend fun insertBill(bill: Bill) = billDao.insertBill(bill)

    suspend fun deleteBill(bill: Bill) = billDao.deleteBill(bill)

    // Participant operations
    suspend fun getParticipantsForBill(billId: String): List<Participant> =
        participantDao.getParticipantsForBill(billId)

    suspend fun insertParticipant(participant: Participant) =
        participantDao.insertParticipant(participant)

    // User operations
    suspend fun getAllUsers(): List<User> = userDao.getAllUsers()

    suspend fun getUserById(id: String): User? = userDao.getUserById(id)

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    // Business logic methods
    suspend fun getUserBalance(userId: String): Double {
        val bills = getAllBills()
        var balance = 0.0

        bills.forEach { bill ->
            val participants = getParticipantsForBill(bill.id)
            val userParticipant = participants.find { it.userId == userId }

            if (userParticipant != null) {
                balance += userParticipant.amountPaid - userParticipant.amountOwed
            }
        }

        return balance
    }

    suspend fun getRecentTransactions(limit: Int = 10): List<Bill> {
        return getAllBills().take(limit)
    }

    suspend fun getBillsByCategory(): Map<String, List<Bill>> {
        return getAllBills().groupBy { it.category }
    }
}
