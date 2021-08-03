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

    private val repository: ToDoRepository = ToDoRepository(toDoDao)
    val getAllData: LiveData<List<ToDoData>> = repository.getAllData

    fun insertData(toDoData: ToDoData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(toDoData)
    }

    fun updateData(toDoData: ToDoData) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateData(toDoData)
    }

    fun deleteItem(toDoData: ToDoData) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteItem(toDoData)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> =
        repository.searchDatabase(searchQuery)
}
