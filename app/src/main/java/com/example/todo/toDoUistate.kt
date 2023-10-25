package com.example.todo

import androidx.compose.runtime.mutableStateOf
import com.example.todo.model.TodoItem

data class toDoUistate(
    val ToDoList: MutableList<TodoItem> = mutableListOf(),
    var idToEdit : String? = "1",
    var nextId : Int = 0
)