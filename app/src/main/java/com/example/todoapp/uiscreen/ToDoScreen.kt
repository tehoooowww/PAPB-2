package com.example.todoapp.uiscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.model.ToDo
import com.example.todoapp.viewmodel.ToDoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: ToDoViewModel = viewModel()) {
    val todos by viewModel.todos.collectAsState()

    var newTask by rememberSaveable { mutableStateOf("") }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var filterOption by rememberSaveable { mutableStateOf("Semua") }

    // 🔍 Filter berdasarkan pencarian dan status
    val filteredTodos = todos
        .filter { it.title.contains(searchQuery, ignoreCase = true) }
        .filter {
            when (filterOption) {
                "Aktif" -> !it.isDone
                "Selesai" -> it.isDone
                else -> true
            }
        }

    val activeCount = todos.count { !it.isDone }
    val doneCount = todos.count { it.isDone }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ToDo App") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // ✏ Input tambah tugas
            OutlinedTextField(
                value = newTask,
                onValueChange = { newTask = it },
                label = { Text("Tugas baru") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    viewModel.addTask(newTask)
                    newTask = ""
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Tambah")
            }


            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari tugas...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )


            Row(modifier = Modifier.padding(top = 8.dp)) {
                listOf("Semua", "Aktif", "Selesai").forEach { f ->
                    FilterChip(
                        selected = filterOption == f,
                        onClick = { filterOption = f },
                        label = { Text(f) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }


            Text(
                text = "Aktif: $activeCount | Selesai: $doneCount",
                modifier = Modifier.padding(top = 8.dp)
            )


            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(filteredTodos) { todo ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(todo.title)
                        Row {
                            Checkbox(
                                checked = todo.isDone,
                                onCheckedChange = { viewModel.toggleDone(todo.id) }
                            )
                            IconButton(onClick = { viewModel.deleteTask(todo.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Hapus"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}