package com.rohitthebest.simpleofflinedictionary.module

import android.content.Context
import androidx.room.Room
import com.rohitthebest.simpleofflinedictionary.database.databases.DictionaryDatabase
import com.rohitthebest.simpleofflinedictionary.others.Constants.DICTIONARY_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object Module {

    //==================================Dictionary Database=========================================
    @Singleton
    @Provides
    fun providesDictionaryDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        DictionaryDatabase::class.java,
        DICTIONARY_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesDictionaryDao(
        db: DictionaryDatabase
    ) = db.getDictionaryDao()

}