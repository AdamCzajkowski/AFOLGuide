package com.example.mysets.view.model.wishlistViewModel

import androidx.lifecycle.ViewModel
import com.example.mysets.repositories.Repository

class WishlistViewModel(private val repository: Repository) : ViewModel() {
    fun getAllMySets() = repository.getWishlist()
}