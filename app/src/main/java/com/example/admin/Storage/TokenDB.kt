package com.example.admin.Storage

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [(TokenEntity::class)], version = 1)
abstract class TokenDB : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}