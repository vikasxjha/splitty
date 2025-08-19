package com.splitwise.easy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val photoUri: String? = null,
    val addedDate: Long
)

