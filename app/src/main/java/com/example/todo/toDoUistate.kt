package com.example.todo

import com.example.todo.model.TodoItem

data class toDoUistate(
    val ToDoList: MutableList<TodoItem> = mutableListOf(),
)