package com.example.todo.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.model.TodoItem
import com.example.todo.toDoUistate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class TodoItemsRepository(val dao: ToDoDao) : ViewModel() {
    private val _uiState = MutableStateFlow(toDoUistate())
    val uiState = _uiState.asStateFlow()

    suspend fun saveData(ToDo: TodoItem) {
        dao.Upsert(todoItem = ToDo)
    }

    suspend fun deleteData(ToDo: TodoItem) {
        dao.delete(ToDo)
    }

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(ToDoList = dao.getAll().toMutableList())
        }

    }

    fun handleViewEvent(viewEvent: TodoListViewEvent) {
        val currentState = _uiState.value
        when (viewEvent) {
            is TodoListViewEvent.RemoveItem -> {
                viewModelScope.launch {
                    val items = currentState.ToDoList.toMutableList().apply {
                        remove(viewEvent.ToDo)
                    }
                    deleteData(viewEvent.ToDo)
                    _uiState.value = _uiState.value.copy(ToDoList = items)
                }
            }

            is TodoListViewEvent.AddItem -> {
                viewModelScope.launch {
                    val items = currentState.ToDoList.toMutableList().apply {
                        add(viewEvent.ToDo)
                    }
                    saveData(ToDo = viewEvent.ToDo)
                    _uiState.value = _uiState.value.copy(ToDoList = items)
                }
            }

            is TodoListViewEvent.EditItem -> {
                viewModelScope.launch {
                    val items = currentState.ToDoList
                        .map { if (it.id == uiState.value.idToEdit) viewEvent.ToDo else it}.toMutableList()
                    saveData(viewEvent.ToDo)
                    _uiState.value = _uiState.value.copy(ToDoList = items)
                }
            }

        }
    }
}

sealed class TodoListViewEvent {
    data class AddItem(val ToDo: TodoItem) : TodoListViewEvent()
    data class RemoveItem(val ToDo: TodoItem) : TodoListViewEvent()
    data class EditItem(val ToDo: TodoItem) : TodoListViewEvent()
}
