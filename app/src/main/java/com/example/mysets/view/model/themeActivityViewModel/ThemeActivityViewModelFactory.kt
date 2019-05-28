package com.example.mysets.view.model.themeActivityViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mysets.repositories.Repository

class ThemeActivityViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ThemeActivityViewModel(
        repository
    ) as T
}