package com.example.admin.Storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TokenDao {
    @Insert
    fun saveToken(token: TokenEntity)
    @Query("select * from TokenEntity")
    fun readToken(): List<TokenEntity>
}