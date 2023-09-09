package com.example.todo.ui.screens

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.R
import com.example.todo.model.TodoItem
import com.example.todo.model.impotance
import com.example.todo.data.TodoItemsRepository
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewToDoScreen(
    modifier: Modifier = Modifier,
    context: Context,
    viewModel: TodoItemsRepository,
    onAddClick: (todo: TodoItem) -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    var text by remember { mutableStateOf("") }
    var impotance: impotance
    var deadline: String?
    val jobDone = false
    val dateOfCreation = Date().toString()
    val dateOfChange: String = Date().toString()
    var ToDoList = state.value.ToDoList


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "введите описание", fontSize = 22.sp)

        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        impotance = DropDownMenuImpotance()

        deadline = pickDate().toString()

        Button(onClick = {
            if (text != "") {
                onAddClick(
                    TodoItem(
                        id = if (ToDoList.size != 0) ToDoList.last().id + 1 else 1,
                        text = text,
                        dateOfChange = dateOfChange,
                        dateOfCreation = dateOfCreation,
                        deadline = deadline!!,
                        impotance = impotance,
                        jobDone = jobDone
                    )
                )
                text = ""
                Toast.makeText(context, R.string.addNew, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, R.string.TextNull, Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "добавить новую запись", fontSize = 22.sp)

        }
    }
}


@Composable
fun DropDownMenuImpotance(): impotance {
    val items = listOf(impotance.LOW, impotance.MEDIUM, impotance.HIGH)
    var selectedItem by remember { mutableStateOf(items[0]) }
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("выбрать сложность") }

    Box(modifier = Modifier.clickable { expanded = !expanded }) {
        Text(text = text, fontSize = 22.sp)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text(item.toString()) }, onClick = {
                    selectedItem = item
                    text = "$selectedItem"
                    expanded = false
                })
            }
        }
    }
    return selectedItem
}

@Composable
fun pickDate(): Date {
    var date by remember { mutableStateOf(Date()) }
    val dateDialogState = rememberMaterialDialogState()
    var text by remember { mutableStateOf("выбрать дату") }
    Button(onClick = {
        dateDialogState.show()
    }) {
        Text(text = text, fontSize = 22.sp)
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Ввведите дату"
        ) {
            date = Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
            text = SimpleDateFormat("dd.MM.yyyy").format(date)
        }
    }
    return date
}
