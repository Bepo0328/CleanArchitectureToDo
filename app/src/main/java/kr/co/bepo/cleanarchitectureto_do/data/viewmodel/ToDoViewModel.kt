package kr.co.bepo.cleanarchitectureto_do.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.bepo.cleanarchitectureto_do.data.ToDoDatabase
import kr.co.bepo.cleanarchitectureto_do.data.models.ToDoData
import kr.co.bepo.cleanarchitectureto_do.data.repository.ToDoRepository

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()

    private val repository: ToDoRepository by lazy { ToDoRepository(toDoDao) }
    private val getAllData: LiveData<List<ToDoData>> by lazy { repository.getAllData }

    fun insertData(toDoData: ToDoData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(toDoData)
    }

}