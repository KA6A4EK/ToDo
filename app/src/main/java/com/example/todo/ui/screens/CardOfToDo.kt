@file:OptIn(ExperimentalMaterialApi::class)

package com.example.todo.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.ui.theme.ToDoTheme
import com.example.todo.model.TodoItem
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToDoCard(modifier: Modifier = Modifier, ToDo: TodoItem, onDelete: (todo: TodoItem) -> Unit) {

    val squareSize = 78.dp
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .swipeable(
                state = swipeableState, anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.4f) },
                orientation = Orientation.Horizontal
            )
    ) {
        if (swipeableState.targetValue == 1) {
            Card {
                Button(onClick = { onDelete(ToDo) }) {
                    Text(text = "удалить")
                }

            }
        }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .clickable { expanded = !expanded }
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
                    CheckJobDone(Done = ToDo.jobDone)
                    Text(
                        text = ToDo.text,
                        maxLines = 3
                    )
                }
                if (expanded) {
                    Text(
                        text = "важность ${ToDo.impotance}\nдедлайн ${
                            SimpleDateFormat("dd.MM.yyyy").format(
                                ToDo.deadline
                            )
                        }", Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}


@Composable
fun CheckJobDone(Done: Boolean) {
    val text = if (Done) "V" else "O"
    val color = if (Done) Color.Green else Color.Red
    Text(text = text, color = color, fontSize = 20.sp, modifier = Modifier.padding(16.dp))
}

@Composable
fun CardDelete() {

}

@Preview
@Composable
fun TodoPerw() {
    ToDoTheme {
    }
}