package com.devexperto.kotlinandroid.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.devexperto.kotlinandroid.data.Task
import com.devexperto.kotlinandroid.data.TaskRepository
import kotlinx.coroutines.launch

class TasksViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _items = MutableLiveData(emptyList<Task>())
    val items: LiveData<List<Task>> get() = _items

    init {
        viewModelScope.launch {
            _items.value = taskRepository.getTasks()
        }
    }

    fun onTaskAdd(task: String) {
        viewModelScope.launch {
            taskRepository.addTask(Task(0, task, false))
            _items.value = taskRepository.getTasks()
        }
    }

    fun onTaskCheck(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
            _items.value = taskRepository.getTasks()
        }
    }
}

@Suppress("UNCHECKED_CAST")
class TasksViewModelFactory(
    private val taskRepository: TaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TasksViewModel(taskRepository) as T
    }
}