package com.splitwise.easy.data

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY name ASC")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}

