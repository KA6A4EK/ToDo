package com.example.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


enum class impotance {
    LOW, MEDIUM, HIGH
}

@Entity(tableName = "todoItems")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val text: String,
    val impotance: impotance,
    val deadline: String,
    val jobDone: Boolean,
    val dateOfCreation: String,
    val dateOfChange: String
)