package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.todo.data.TodoItemsRepository
import com.example.todo.ui.screens.Screen
import com.example.todo.ui.theme.ToDoTheme
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var db =
            FirebaseDatabase.getInstance("https://todo-fe651-default-rtdb.europe-west1.firebasedatabase.app/")
        db.setPersistenceEnabled(true)

        setContent {
            ToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen(context = this, viewModel = TodoItemsRepository(db = db.reference))
                }
            }
        }
    }
}

