package com.example.todo.data

sealed interface ToDoEvent{
    object saveToDo : ToDoEvent
}