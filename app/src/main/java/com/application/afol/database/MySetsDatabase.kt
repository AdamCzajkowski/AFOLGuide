package com.application.afol.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.application.afol.models.LegoSet

@Database(entities = [LegoSet::class], version = 3)
abstract class MySetsDatabase : RoomDatabase() {
    abstract fun mySetsDao(): MySetsDao
}