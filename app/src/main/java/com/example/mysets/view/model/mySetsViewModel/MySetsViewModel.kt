package com.example.mysets.view.model.mySetsViewModel

import androidx.lifecycle.ViewModel
import com.example.mysets.repositories.Repository

class MySetsViewModel(private val repository: Repository) : ViewModel() {
    fun getAllMySets() = repository.getMySets()
}