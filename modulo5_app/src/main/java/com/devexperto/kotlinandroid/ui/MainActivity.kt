package com.devexperto.kotlinandroid.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.devexperto.kotlinandroid.App
import com.devexperto.kotlinandroid.data.TaskRepository
import com.devexperto.kotlinandroid.domain.AddTaskUseCase
import com.devexperto.kotlinandroid.domain.GetTasksUseCase
import com.devexperto.kotlinandroid.domain.UpdateTaskUseCase
import com.devexperto.kotlinandroid.framework.RoomTaskLocalDataSource
import com.devexperto.modulo5.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter

    private val viewModel: TasksViewModel by viewModels {
        val taskRepository = TaskRepository(
            RoomTaskLocalDataSource((application as App).db.taskDao())
        )
        TasksViewModelFactory(
            GetTasksUseCase(taskRepository),
            AddTaskUseCase(taskRepository),
            UpdateTaskUseCase(taskRepository)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskAdapter = TaskAdapter(viewModel::onTaskCheck)

        binding.tasksList.adapter = taskAdapter
        binding.addTask.setOnClickListener {
            viewModel.onTaskAdd(binding.taskInput.text.toString())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect(taskAdapter::submitList)
            }
        }
    }
}