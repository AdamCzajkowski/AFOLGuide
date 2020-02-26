package com.application.afol.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.application.afol.extension.EnumTypeConverter
import com.application.afol.models.LegoSet

@Database(entities = [LegoSet::class], version = 5)
@TypeConverters(EnumTypeConverter::class)
abstract class MySetsDatabase : RoomDatabase() {
    abstract fun mySetsDao(): MySetsDao
}