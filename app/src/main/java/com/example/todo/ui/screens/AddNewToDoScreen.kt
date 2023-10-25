package com.example.todo.ui.screens

import android.content.Context
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
import com.example.todo.model.impotance
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun AddNewToDoScreen(
    modifier: Modifier = Modifier,
    context: Context,
    viewModel: TodoItemsRepository,
    onAddClick: (todo: TodoItem) -> Unit,
) {
    val state = viewModel.uiState.collectAsState()

    var text by remember { mutableStateOf("") }
    var impotance: impotance?
    var deadline: String?
    val dateOfCreation = Date().toString()
    val dateOfChange: String = Date().toString()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = stringResource(R.string.add_description), fontSize = 22.sp)

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
                        text = text,
                        dateOfChange = dateOfChange,
                        dateOfCreation = dateOfCreation,
                        deadline = deadline!!,
                        impotance = impotance,
                        id = state.value.nextId
                    )

                )
                state.value.nextId++

                text = ""
                Toast.makeText(context, R.string.addNew, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, R.string.TextNull, Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = stringResource(R.string.add_new_note), fontSize = 22.sp)

        }
    }
}


@Composable
fun DropDownMenuImpotance(Impotance: impotance? = impotance.LOW): impotance? {
    val items = listOf(impotance.LOW, impotance.MEDIUM, impotance.HIGH)
    var selectedItem by remember { mutableStateOf(Impotance) }
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(Impotance.toString()) }

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
fun pickDate(date: LocalDate = LocalDate.now()): LocalDate {
    var date by remember { mutableStateOf(date) }
    val dateDialogState = rememberMaterialDialogState()
    var text by remember { mutableStateOf(date.toString()) }
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
            initialDate = date,
            title = "Ввведите дату"
        ) {
//            date = LocalDate.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
            date = it
            text = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
    return date
}
