package com.example.todo.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.data.TodoItemsRepository
import com.example.todo.data.TodoListViewEvent

@Composable
fun ScreenWithToDo(viewModel: TodoItemsRepository) {
    val state = viewModel.uiState.collectAsState()
    LazyColumn() {
        items(state.value.ToDoList, key = { item -> item.id })
        { todo ->
            ToDoCard(
                ToDo = todo,
                modifier = Modifier.padding(8.dp),
                onDelete = { viewModel.handleViewEvent(TodoListViewEvent.RemoveItem(it)) })
            Divider()

        }
    }
}