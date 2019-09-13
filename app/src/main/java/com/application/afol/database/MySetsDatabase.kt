package com.application.afol.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.application.afol.models.LegoSet

@Database(entities = [LegoSet::class], version = 2)
abstract class MySetsDatabase : RoomDatabase() {
    abstract fun mySetsDao(): MySetsDao
}