package com.example.mysets.database.wishlist

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mysets.database.wishlist.WishlistDao
import com.example.mysets.models.LegoSet

@Database(entities = [LegoSet::class], version = 1)
abstract class WishlistDatabase: RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
}