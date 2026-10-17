package com.example.todo.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.R
import com.example.todo.data.getSampleTodos
import com.example.todo.models.DueBucket
import com.example.todo.models.Task
import com.example.todo.models.toDueBucket
import com.example.todo.ui.theme.ToDoTheme
import com.example.todo.ui.toDisplayText
import com.example.todo.ui.toUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val focusManager = LocalFocusManager.current
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppBar() },
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
                detectDragGestures { _, _ ->
                    focusManager.clearFocus()
                }
            }
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxHeight()
        ) {
            TaskList(
                tasks,
                onTaskCompleteStatusChanged = viewModel::toggleCompleteTask)
            QuickAddTaskField(
                onAddNewTask = viewModel::addNewTask,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskCompleteStatusChanged: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedTasks = tasks.groupBy { it.dueDate.toDueBucket() }
        .toSortedMap { bucket1, bucket2 -> bucket1.ordinal - bucket2.ordinal }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = modifier,
    ) {
        for ((dueBucket, tasks) in groupedTasks) {
            item(dueBucket.name) {
                TaskSection(
                    dueBucket,
                    tasks,
                    onTaskCompleteStatusChanged = onTaskCompleteStatusChanged,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
fun TaskSection(
    dueBucket: DueBucket,
    tasks: List<Task>,
    onTaskCompleteStatusChanged: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        TaskHeader(
            dueBucket,
            count = tasks.count(),
            modifier = Modifier.fillMaxWidth())
        repeat(tasks.count()) {
            val task = tasks[it]
            TaskRow(
                dueBucket,
                task,
                onCheckedChange = { completed -> onTaskCompleteStatusChanged(task, completed) },
                modifier = Modifier.fillMaxWidth())
            if (it != tasks.lastIndex) {
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}

@Composable
fun TaskHeader(
    dueBucket: DueBucket,
    count: Int,
    modifier: Modifier = Modifier
) {
    val dueBucketUi = dueBucket.toUi()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Icon(
            dueBucketUi.icon,
            null,
            tint = dueBucketUi.color,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "${dueBucketUi.displayString} ($count)",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            fontFamily = FontFamily(Font(R.font.kanit)),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun TaskRow(
    dueBucket: DueBucket,
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val dueBucketUi = dueBucket.toUi()
    val categoryUi = task.category?.toUi()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(
            checked = task.completed,
            onCheckedChange = onCheckedChange
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = task.title,
            )

            task.category?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    categoryUi?.icon?.let {
                        Icon(
                            it,
                            null,
                            tint = categoryUi.color,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Text(
                        text = task.category.name,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
        Text(
            text = task.dueDate?.toDisplayText()?:"",
            color = dueBucketUi.color,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.CenterEnd)
                .padding(end = 16.dp)
        )
    }

}

@Composable
fun QuickAddTaskField(
    onAddNewTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (newTask, setNewTask) = remember { mutableStateOf("") }
    var alpha by remember { mutableFloatStateOf(.75f) }

    TextField(
        value = newTask,
        onValueChange = setNewTask,
        placeholder = { Text("Add a task...") },
        leadingIcon = {
            Icon(
                Icons.Filled.Mic,
                null
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                onAddNewTask(newTask)
                setNewTask("")
            }) {
                Icon(
                    Icons.Filled.Add,
                    null
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
            .alpha(alpha)
            .onFocusChanged {
                alpha = if (it.isFocused) 1f else .75f
            }
            .imePadding()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(modifier: Modifier = Modifier) {
    val formatter = remember { DateTimeFormatter.ofPattern("EEEE, MMMM d") }

    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Today",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    LocalDate.now().format(formatter),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            IconButton(
                onClick = {},
            ) {
                Icon(
                    Icons.Filled.Search,
                    null,
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.MoreVert,
                    null
                )
            }
        },
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    ToDoTheme {
        TaskList(
            getSampleTodos(),
            onTaskCompleteStatusChanged = { _, _ -> }
        )
    }
}

//@Preview
//@Composable
//fun TaskSectionPreview() {
//    ToDoTheme {
//        TaskSection(
//            modifier = Modifier.padding(16.dp)
//        )
//    }
//}

//@Preview
//@Composable
//fun TaskRowPreview() {
//    ToDoTheme {
//        TaskRow(
//            task = getSampleTodos()[0],
//            modifier = Modifier.padding(16.dp)
//        )
//    }
//}

//@Preview
//@Composable
//fun TaskHeaderPreview() {
//    ToDoTheme {
//        TaskHeader(
//            dueBucket = DueBucket.TODAY,
//            modifier = Modifier
//                .padding(16.dp)
//                .width(200.dp)
//        )
//    }
//}

@Preview
@Composable
fun QuickAddTaskPreview() {
    ToDoTheme {
        QuickAddTaskField(
            onAddNewTask = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun AppBarPreview() {
    ToDoTheme {
        AppBar()
    }
}