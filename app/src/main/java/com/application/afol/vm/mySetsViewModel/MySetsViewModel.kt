package com.application.afol.vm.mySetsViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.application.afol.models.LegoSet
import com.application.afol.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MySetsViewModel(private val repository: Repository) : ViewModel() {
    suspend fun getListOfMySets(): LiveData<MutableList<LegoSet>> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getMySets()
        }
    }
}