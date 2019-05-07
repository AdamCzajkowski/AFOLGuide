package com.example.mysets.database.my.sets

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mysets.database.my.sets.MySetsDao
import com.example.mysets.models.LegoSet

@Database(entities = [LegoSet::class], version = 1)
abstract class MySetsDatabase : RoomDatabase() {
    abstract fun mySetsDao(): MySetsDao
}