package com.example.handbook.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.handbook.utils.ListItem

@Database (
    entities = [ListItem::class],
    version = 1
)
abstract class MainDb : RoomDatabase() {
    abstract val dao: Dao
}