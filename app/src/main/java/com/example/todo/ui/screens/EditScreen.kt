package com.example.todo.ui.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.R
import com.example.todo.data.TodoItemsRepository
import com.example.todo.model.TodoItem
import java.time.LocalDate
import java.util.Date


@Composable
fun EditToDoScreen(
    modifier: Modifier = Modifier,
    context: Context,
    viewModel: TodoItemsRepository,
    onSaveClick: (todo: TodoItem) -> Unit,
) {

    val state = viewModel.uiState.collectAsState()
    val todo =
        if (state.value.ToDoList.first { it.dateOfCreation == state.value.idToEdit } != null) {
            state.value.ToDoList.first {
                it.dateOfCreation == state.value.idToEdit
            }
        } else {
            state.value.ToDoList.first()

        }
    Log.w(TAG, todo.toString())

//    val todo = state.value.ToDoList.first { it.dateOfCreation == state.value.idToEdit }


    var text by remember { mutableStateOf(todo.text) }
    var impotance = todo.impotance
    var deadline = todo.deadline
    val jobDone = false
    val dateOfCreation = todo.dateOfCreation
    val dateOfChange: String = Date().toString()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = stringResource(id = R.string.add_description), fontSize = 22.sp)

        OutlinedTextField(
            value = text.toString(),
            onValueChange = { newText -> text = newText },
            Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        impotance = DropDownMenuImpotance(todo.impotance)

        deadline =
            pickDate(
                LocalDate.parse(todo.deadline)
            ).toString()

        Button(onClick = {
            if (text != "") {
                onSaveClick(
                    TodoItem(
                        id = todo.id,
                        text = text,
                        dateOfChange = dateOfChange,
                        dateOfCreation = dateOfCreation,
                        deadline = deadline!!,
                        impotance = impotance,
                    )
                )
                text = ""
                Toast.makeText(
                    context,
                    R.string.succefull_update, Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    R.string.bad_editing,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text(text = stringResource(R.string.save), fontSize = 22.sp)
        }
    }
}
