package com.example.todolistapp.di

import android.content.Context
import androidx.room.Room
import com.example.todolistapp.room.NoteDao
import com.example.todolistapp.room.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(context, NoteDatabase::class.java, "NoteDatabase")
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }
}