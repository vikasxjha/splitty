package com.splitwise.easy.data

import android.content.Context
import androidx.room.Room

class DatabaseProvider {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "splitty_easy_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
