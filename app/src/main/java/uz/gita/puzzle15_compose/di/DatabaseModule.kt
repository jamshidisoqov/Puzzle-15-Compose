package uz.gita.puzzle15_compose.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.puzzle15_compose.data.local.MySharedPref
import uz.gita.puzzle15_compose.data.local.room.PuzzleDao
import uz.gita.puzzle15_compose.data.local.room.PuzzleDatabase
import javax.inject.Singleton

// Created by Jamshid Isoqov an 11/15/2022
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @[Provides Singleton]
    fun provideSharedPreferences(@ApplicationContext ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences("app_data", Context.MODE_PRIVATE)
    }

    @[Provides Singleton]
    fun provideMySharedPref(
        @ApplicationContext ctx: Context,
        sharedPreferences: SharedPreferences
    ): MySharedPref = MySharedPref(ctx, sharedPreferences)

    @[Provides Singleton]
    fun provideDatabase(@ApplicationContext ctx: Context): PuzzleDatabase {
        return Room.databaseBuilder(ctx, PuzzleDatabase::class.java, "puzzle.db")
            .build()
    }

    @[Provides Singleton]
    fun provideDao(puzzleDatabase: PuzzleDatabase): PuzzleDao = puzzleDatabase.puzzleDao()


}