package com.rohitthebest.simpleofflinedictionary.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rohitthebest.simpleofflinedictionary.model.Word

@Dao
interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word : Word): Long

    @Delete
    suspend fun delete(word : Word)

    @Query("DELETE FROM offlineDictionaryTable")
    suspend fun deleteAll()

    @Query("SELECT * FROM offlineDictionaryTable")
    fun getAllWordsMeaning(): LiveData<List<Word>>

    @Query("SELECT * FROM offlineDictionaryTable WHERE word = :word")
    fun getMeaningByWord(word : String) : LiveData<List<Word>>

    @Query("SELECT * FROM offlineDictionaryTable WHERE word LIKE :w || '%'")
    fun getAllWordsStartingWith(w : String) : LiveData<List<Word>>

    @Query("SELECT * FROM offlineDictionaryTable WHERE word LIKE '%' || :search || '%' ")
    fun getAllWhereWordContains(search : String) : LiveData<List<Word>>
}