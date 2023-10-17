package com.example.todo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.data.TodoItemsRepository
import com.example.todo.data.TodoListViewEvent

@Composable
fun ScreenWithToDo(
    viewModel: TodoItemsRepository,
    onEditClick: () -> Unit,
    onAddClick: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "добавить запись",
                )
            }
        }
    )
    { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(state.value.ToDoList)
            { todo ->
                ToDoCard(
                    ToDo = todo,
                    viewModel = viewModel,
                    modifier = Modifier.padding(8.dp),
                    onDelete = { viewModel.handleViewEvent(TodoListViewEvent.RemoveItem(it)) },
                    onEdit = onEditClick
                )
                Divider()

            }
        }
    }
}