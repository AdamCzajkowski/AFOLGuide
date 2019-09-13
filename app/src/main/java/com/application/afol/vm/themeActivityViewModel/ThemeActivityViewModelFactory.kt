package com.application.afol.vm.themeActivityViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.afol.repositories.Repository

class ThemeActivityViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ThemeActivityViewModel(
        repository
    ) as T
}