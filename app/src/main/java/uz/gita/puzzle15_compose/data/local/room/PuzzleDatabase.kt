package uz.gita.puzzle15_compose.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

// Created by Jamshid Isoqov an 11/16/2022
@Database(entities = [PuzzleEntity::class], exportSchema = true, version = 1)
abstract class PuzzleDatabase : RoomDatabase() {

    abstract fun puzzleDao(): PuzzleDao

}