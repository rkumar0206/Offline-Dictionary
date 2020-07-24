package com.rohitthebest.simpleofflinedictionary.repositories

import com.rohitthebest.simpleofflinedictionary.database.dao.DictionaryDao
import com.rohitthebest.simpleofflinedictionary.model.Word
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    var dao: DictionaryDao
) {

    suspend fun insert(word: Word) = dao.insert(word)

    suspend fun delete(word: Word) = dao.delete(word)

    suspend fun deleteAll() = dao.deleteAll()

    fun getAllWords() = dao.getAllWordsMeaning()

    fun getMeaningByWord(word: String) = dao.getMeaningByWord(word)

    fun getAllWordsStartingWith(ch: String) = dao.getAllWordsStartingWith(ch)

    fun getAllWhereWordContains(search: String) = dao.getAllWhereWordContains(search)

}