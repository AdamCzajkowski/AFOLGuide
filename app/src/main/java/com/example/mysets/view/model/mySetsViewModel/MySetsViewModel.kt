package com.example.mysets.view.model.mySetsViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mysets.models.LegoSet
import com.example.mysets.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MySetsViewModel(private val repository: Repository) : ViewModel() {
    suspend fun getListOfMySets(): LiveData<MutableList<LegoSet>> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getMySets()
        }
    }
}