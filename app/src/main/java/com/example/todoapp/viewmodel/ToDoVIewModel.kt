package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todoapp.model.ToDo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ToDoViewModel : ViewModel() {

    private val _todos = MutableStateFlow<List<ToDo>>(emptyList())
    val todos: StateFlow<List<ToDo>> = _todos

    private var nextId = 0

    fun addTask(title: String) {
        if (title.isNotBlank()) {
            _todos.value = _todos.value + ToDo(
                id = nextId++,
                title = title,
                isDone = false
            )
        }
    }

    fun toggleDone(id: Int) {
        _todos.value = _todos.value.map { todo ->
            if (todo.id == id) todo.copy(isDone = !todo.isDone)
            else todo
        }
    }

    fun deleteTask(id: Int) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }
}