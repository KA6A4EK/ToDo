package com.example.todo.ui

import androidx.lifecycle.ViewModel
import com.example.todo.model.TodoItem
import com.example.todo.toDoUistate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoItemsRepository : ViewModel() {
    private val _uiState = MutableStateFlow(toDoUistate())
    val uiState = _uiState.asStateFlow()

    fun handleViewEvent(viewEvent: TodoListViewEvent) {
        when (viewEvent) {
            is TodoListViewEvent.RemoveItem -> {
                val currentState = _uiState.value
                val items = currentState.ToDoList.toMutableList().apply {
                    remove(viewEvent.ToDo)
                }
                _uiState.value = _uiState.value.copy(ToDoList = items)
            }

            is TodoListViewEvent.AddItem -> {
                val currentState = _uiState.value
                val items = currentState.ToDoList.toMutableList().apply {
                    add(viewEvent.ToDo)
                }
                _uiState.value = _uiState.value.copy(ToDoList = items)
            }
        }
    }
}

sealed class TodoListViewEvent {
    data class AddItem(val ToDo: TodoItem) : TodoListViewEvent()
    data class RemoveItem(val ToDo: TodoItem) : TodoListViewEvent()
}
