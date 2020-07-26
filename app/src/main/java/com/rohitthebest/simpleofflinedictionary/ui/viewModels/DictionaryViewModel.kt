package com.rohitthebest.simpleofflinedictionary.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohitthebest.simpleofflinedictionary.database.model.Word
import com.rohitthebest.simpleofflinedictionary.repositories.DictionaryRepository
import kotlinx.coroutines.launch

class DictionaryViewModel @ViewModelInject constructor(
    var repository: DictionaryRepository
) : ViewModel() {

    fun insertWord(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }

    fun deleteWord(word: Word) = viewModelScope.launch {
        repository.delete(word)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun getAllWords() = repository.getAllWords()

    fun getWordsByBookmark(isBookmarked: String) = repository.getWordsByBookmark(isBookmarked)

    fun getWordsByRecent(isInRecent: String) = repository.getWordsByRecent(isInRecent)

    fun getMeaningByWord(word: String) = repository.getMeaningByWord(word)

    fun getAllWordsStartingWith(ch: String) = repository.getAllWordsStartingWith(ch)

    fun getAllWhereWordContains(search: String) = repository.getAllWhereWordContains(search)

}