package com.example.handbook.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.handbook.utils.ListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ListItem)

//Сделать UPDATE
//    @Update("UPDATE main SET ")
//    suspend fun updateItem(item: ListItem)

    @Delete
    suspend fun deleteItem(item: ListItem)

    //Flow - Обновление данных без постоянного вызова запроса
    @Query("SELECT * FROM main WHERE category LIKE :category")
    fun getAllItemsFromCategory(category: String): Flow<List<ListItem>>

    @Query("SELECT * FROM main WHERE isFavorite = 1")
    fun getFavoriteItems(): Flow<List<ListItem>>
}