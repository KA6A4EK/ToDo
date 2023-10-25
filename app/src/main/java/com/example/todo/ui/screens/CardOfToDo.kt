@file:OptIn(ExperimentalMaterialApi::class)

package com.example.todo.ui.screens

import android.content.res.Resources.Theme
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.data.TodoItemsRepository
import com.example.todo.model.TodoItem
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoCard(
    modifier: Modifier = Modifier,
    viewModel: TodoItemsRepository,
    ToDo: TodoItem,
    onDelete: (todo: TodoItem) -> Unit,
    onEdit: () -> Unit
) {

    val squareSize = 1.dp
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .swipeable(
                state = swipeableState, anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.4f) },
                orientation = Orientation.Horizontal
            )
    ) {
        if (swipeableState.targetValue == 1) {
            Button(onClick = { onDelete(ToDo) }) {
                Text(text = stringResource(R.string.delete))
            }
            Button(onClick = {
                onEdit()
                viewModel.uiState.value.idToEdit = ToDo.dateOfCreation
            }) {
                Text(text = stringResource(R.string.update))
            }
        }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .clickable { expanded = !expanded }
                .heightIn(min = 55.dp)
        ) {
            Column(
                Modifier.animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
                    Text(
                        text = ToDo.text.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = if (expanded) 10 else 2
                    )
                }
                if (expanded) {
                    Text(
                        text = stringResource(R.string.impotance, ToDo.impotance.toString()) +
                                stringResource(R.string.deadline, ToDo.deadline.toString()),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}


