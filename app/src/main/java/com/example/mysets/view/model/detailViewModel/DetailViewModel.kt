package com.example.mysets.view.model.detailViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mysets.models.LegoSet
import com.example.mysets.repositories.Repository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailViewModel(private val repository: Repository) : ViewModel() {

    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    fun addToMySets(legoSet: LegoSet) = scope.launch {
        repository.addToMySets(legoSet)
    }

    fun addToWishlist(legoSet: LegoSet) = scope.launch {
        repository.addToWishlist(legoSet)
    }

    suspend fun getListOfWishlist(): LiveData<MutableList<LegoSet>> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getWishlist()
        }
    }

    suspend fun getListOfMySets(): LiveData<MutableList<LegoSet>> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getMySets()
        }
    }

    fun removeFromMySets(legoSet: LegoSet) = scope.launch {
        repository.removeFromMySets(legoSet)
    }

    fun removeFromWishlist(legoSet: LegoSet) = scope.launch {
        repository.removeFromWishlist(legoSet)
    }
}