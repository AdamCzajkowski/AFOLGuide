package com.example.mysets.view.model.detailViewModel

import androidx.lifecycle.ViewModel
import com.example.mysets.models.LegoSet
import com.example.mysets.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

    fun removeFromMySets(legoSet: LegoSet) = scope.launch {
        repository.removeFromMySets(legoSet)
    }

    fun removeFromWishlist(legoSet: LegoSet) = scope.launch {
        repository.removeFromWishlist(legoSet)
    }
}