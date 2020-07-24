package com.rohitthebest.simpleofflinedictionary.database.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rohitthebest.simpleofflinedictionary.converters.DefinitionListConverter
import com.rohitthebest.simpleofflinedictionary.database.dao.DictionaryDao
import com.rohitthebest.simpleofflinedictionary.model.Word

@Database(
    entities = [Word::class],
    version = 1
)
@TypeConverters(DefinitionListConverter::class)
abstract class DictionaryDatabase : RoomDatabase() {

    abstract fun getDictionaryDao(): DictionaryDao
}