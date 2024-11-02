package com.example.handbook

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handbook.db.MainDb
import com.example.handbook.utils.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainDb: MainDb
): ViewModel() {
    val mainList = mutableStateOf(emptyList<ListItem>())
    private var job: Job? = null

    fun getAllItemsByCategory(category: String) {
        job?.cancel()
        job = viewModelScope.launch {
            mainDb.dao.getAllItemsFromCategory(category).collect { list ->
                mainList.value = list
            }
        }
    }

    fun getFavorites() = viewModelScope.launch {
        job?.cancel()
        job = viewModelScope.launch {
            mainDb.dao.getFavoriteItems().collect { list ->
                mainList.value = list
            }
        }
    }

    fun insertItem(item: ListItem) = viewModelScope.launch {
        mainDb.dao.insertItem(item)
    }

    fun deleteItem(item: ListItem) = viewModelScope.launch {
        mainDb.dao.deleteItem(item)
    }
}