@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.screens

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todo.R
import com.example.todo.data.TodoItemsRepository
import com.example.todo.data.TodoListViewEvent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

enum class Screen(@StringRes val title: Int) {
    Start(title = R.string.startTitle),
    AddToDo(title = R.string.addTitle),
    Edit(title = R.string.EditToDo)}

@Composable
fun Screen(
    navController: NavHostController = rememberNavController(),
    viewModel: TodoItemsRepository = viewModel(),
    context: Context,

) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Start.name
    )
    Scaffold(
        topBar = {
            ToDoAppBar(
                navigateUp = { navController.navigateUp() }, currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Start.name) {
                ScreenWithToDo(viewModel = viewModel,
                    onEditClick = { navController.navigate(Screen.Edit.name) },
                    onAddClick = { navController.navigate(Screen.AddToDo.name) })
            }
            composable(route = Screen.AddToDo.name) {
                AddNewToDoScreen(
                    context = context,
                    viewModel = viewModel,
                    onAddClick = {
                        viewModel.handleViewEvent(TodoListViewEvent.AddItem(it))
                        navController.navigateUp()
                    })
            }
            composable(route = Screen.Edit.name) {
                EditToDoScreen(
                    context = context,
                    viewModel = viewModel,
                    onSaveClick = {
                        viewModel.handleViewEvent(TodoListViewEvent.EditItem(it))
                        navController.navigateUp()
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoAppBar(
    currentScreen: Screen,
    navigateUp: () -> Unit,
) {
    TopAppBar(title = { Text(text = stringResource(currentScreen.title)) },
        navigationIcon = {
            if (currentScreen == Screen.AddToDo)
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "кнопка назад")
                }
        }
    )
}