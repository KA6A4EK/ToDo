package com.example.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
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
    val deadline: Date?,
    val jobDone: Boolean,
    val dateOfCreation: Date,
    val dateOfChange: Date?
)