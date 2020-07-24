package com.rohitthebest.simpleofflinedictionary.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offlineDictionaryTable")
data class Word(
    val definitions: List<Definition>?,
    val pronunciation: String?,
    val word: String?
) {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    var id: String? = null

}