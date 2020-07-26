package com.rohitthebest.simpleofflinedictionary.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rohitthebest.simpleofflinedictionary.database.model.Definition

class DefinitionListConverter {

    @TypeConverter
    fun convertFromDefinitionListToString(definitionList: List<Definition>): String {

        return try {
            val gson = Gson()
            gson.toJson(definitionList)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    @TypeConverter
    fun convertStringToDefinitionList(definitionString: String): List<Definition> {

        return try {
            val gson = Gson()
            val type = object : TypeToken<List<Definition>>() {}.type
            gson.fromJson<List<Definition>>(definitionString, type)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}