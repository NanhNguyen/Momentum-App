package com.momentum.app.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.Task
import com.momentum.app.ui.components.*
import com.momentum.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onNavigateToAddTask: () -> Unit,
    onNavigateToEditTask: (Long) -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Tasks",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTask,
                containerColor = SageGreen,
                contentColor = Background
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add task")
            }
        },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TaskFilter.entries) { filter ->
                    FilterChip(
                        selected = uiState.selectedFilter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        label = {
                            Text(
                                when (filter) {
                                    TaskFilter.TODAY -> "Today"
                                    TaskFilter.THIS_WEEK -> "This Week"
                                    TaskFilter.ALL -> "All Tasks"
                                }
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SageGreenContainer,
                            selectedLabelColor = SageGreen
                        )
                    )
                }
            }

            if (uiState.tasks.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = SageGreen
                        )
                        Text(
                            "No tasks here yet.",
                            style = MaterialTheme.typography.titleMedium,
                            color = OnSurface
                        )
                        Text(
                            "Add a task and take the first small step.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceSubtle
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.tasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            onToggleComplete = { viewModel.toggleComplete(task) },
                            onEdit = { onNavigateToEditTask(task.id) },
                            onDelete = { taskToDelete = task }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    taskToDelete?.let { task ->
        if (task.isRecurring) {
            AlertDialog(
                onDismissRequest = { taskToDelete = null },
                title = { Text("Delete recurring task?") },
                text = {
                    Text(
                        "\"${task.title}\" is a recurring task. Would you like to delete only today's task or this and all future occurrences?",
                        color = OnSurfaceMuted
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteRecurringTask(task, deleteAllFuture = true)
                            taskToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = SoftError)
                    ) { Text("This & future") }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = { taskToDelete = null }) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                viewModel.deleteRecurringTask(task, deleteAllFuture = false)
                                taskToDelete = null
                            }
                        ) { Text("Just today") }
                    }
                },
                containerColor = SurfaceContainer
            )
        } else {
            AlertDialog(
                onDismissRequest = { taskToDelete = null },
                title = { Text("Remove task?") },
                text = {
                    Text(
                        "\"${task.title}\" will be removed. You can always add it back later.",
                        color = OnSurfaceMuted
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTask(task)
                            taskToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = SoftError)
                    ) { Text("Remove") }
                },
                dismissButton = {
                    TextButton(onClick = { taskToDelete = null }) { Text("Keep it") }
                },
                containerColor = SurfaceContainer
            )
        }
    }
}

@Composable
private fun TaskCard(
    task: Task,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    MomentumCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TaskItemRow(
                task = task,
                onToggleComplete = onToggleComplete,
                modifier = Modifier.weight(1f)
            )
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "More options",
                        tint = OnSurfaceSubtle,
                        modifier = Modifier.size(18.dp)
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    containerColor = SurfaceContainerHigh
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit task") },
                        leadingIcon = {
                            Icon(Icons.Filled.Edit, null, modifier = Modifier.size(18.dp))
                        },
                        onClick = {
                            showMenu = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Remove", color = SoftError) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Delete, null,
                                modifier = Modifier.size(18.dp),
                                tint = SoftError
                            )
                        },
                        onClick = {
                            showMenu = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}
