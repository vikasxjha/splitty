package com.splitwise.easy.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Bill::class, Participant::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao
    abstract fun participantDao(): ParticipantDao
    abstract fun userDao(): UserDao
}

