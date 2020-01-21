package com.application.afol.vm.mySetsViewModel

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.application.afol.models.LegoSet
import com.application.afol.repositories.Repository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MySetsViewModel(private val repository: Repository) : ViewModel() {

    private val job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    suspend fun getListOfMySets(): LiveData<MutableList<LegoSet>> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.getFavorites()
        }
    }

    fun removeFromMySets(legoSet: LegoSet) = scope.launch {
        legoSet.isInFavorite = false
        repository.removeFromFavorites(legoSet)
    }

    fun addToMySets(legoSet: LegoSet) = scope.launch {
        legoSet.isInFavorite = true
        repository.addToFavorites(legoSet)
    }
}