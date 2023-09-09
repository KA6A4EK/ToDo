package com.example.todo.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import com.example.todo.model.TodoItem

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todoItems")
     suspend fun getAll(): List<TodoItem>

    @Upsert
    suspend fun Upsert( todoItem: TodoItem)

    @Delete
    suspend fun delete(todoItem: TodoItem)
}

@Database(entities = [TodoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val todoDao : ToDoDao
}