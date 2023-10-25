package com.example.todo.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.model.TodoItem
import com.example.todo.model.impotanceFromString
import com.example.todo.toDoUistate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class TodoItemsRepository(db: DatabaseReference) : ViewModel() {
    private val _uiState = MutableStateFlow(toDoUistate())
    val uiState = _uiState.asStateFlow()
    val db = db



    suspend fun deleteData(ToDo: TodoItem) {
        db.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapShot = task.result
                for (childSnapshot in dataSnapShot.children) {

                    val dateOfCreation =
                        childSnapshot.child("dateOfCreation").getValue(String::class.java)

                    if (dateOfCreation == ToDo.dateOfCreation) {
                        db.child(childSnapshot.key.toString()).removeValue()
                        _uiState.value.ToDoList.remove(ToDo)
                    }
                }
            }
        }
    }


    suspend fun addItems(db1: DatabaseReference) {
        db1.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapShot = task.result
                for (childSnapshot in dataSnapShot.children) {
                    val id = childSnapshot.child("id").getValue(Int::class.java)
                    val text = childSnapshot.child("text").getValue(String::class.java)
                    val importance =
                        childSnapshot.child("importance").getValue(String::class.java)
                    val deadline = childSnapshot.child("deadline").getValue(String::class.java)
                    val dateOfCreation =
                        childSnapshot.child("dateOfCreation").getValue(String::class.java)
                    val dateOfChange =
                        childSnapshot.child("dateOfChange").getValue(String::class.java)
                    val todoItem = TodoItem(
                        id = id,
                        text = text,
                        impotance = impotanceFromString(importance),
                        deadline = deadline,
                        dateOfCreation = dateOfCreation,
                        dateOfChange = dateOfChange
                    )
                    if (todoItem !in _uiState.value.ToDoList) {
                        val items = _uiState.value.ToDoList.toMutableList().apply {
                            add(todoItem)
                        }
                        _uiState.value = _uiState.value.copy(ToDoList = items)

//                        viewModelScope.launch {
//                            saveData(todoItem)
//                        }
                    }
                }
            }

        }
    }


    init {
        viewModelScope.launch {

            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {
                        delay(700L)
                        _uiState.value = _uiState.value.copy(mutableListOf())
                        addItems(snapshot.ref)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error: ${error.message}")
                }
            }
            )

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
//                    val items = currentState.ToDoList.toMutableList().apply {
//                        add(viewEvent.ToDo)
//                    }
//                    saveData(ToDo = viewEvent.ToDo)

                    db.push().setValue(viewEvent.ToDo)
//                    _uiState.value = _uiState.value.copy(ToDoList = items)
                }
            }

            is TodoListViewEvent.EditItem -> {
                val editedTodo = viewEvent.ToDo
                viewModelScope.launch {
//                    updateData(viewEvent.ToDo)
////                    deleteData(viewEvent.ToDo)
//                    Log.w(TAG,"запись изменена ___________________________________________")
//                    val items = currentState.ToDoList
//                        .map {
//                            if (it.dateOfCreation == uiState.value.idToEdit) { viewEvent.ToDo } else it
//                        }
//                        .toMutableList()
//                    _uiState.value = _uiState.value.copy(ToDoList = items)
                    db.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dataSnapShot = task.result
                            for (childSnapshot in dataSnapShot.children) {
                                val dateOfCreation =
                                    childSnapshot.child("dateOfCreation")
                                        .getValue(String::class.java)
                                if (dateOfCreation.toString() == editedTodo.dateOfCreation.toString()) {
                                    db.child(childSnapshot.key.toString()).setValue(editedTodo)
                                    break
                                }
                            }
                        }
                    }
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