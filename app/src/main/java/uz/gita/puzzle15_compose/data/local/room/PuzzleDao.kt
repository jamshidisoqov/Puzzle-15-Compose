package uz.gita.puzzle15_compose.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Created by Jamshid Isoqov an 11/16/2022
@Dao
interface PuzzleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPuzzle(puzzleEntity: PuzzleEntity)

    @Query("SELECT*FROM puzzle ORDER BY time DESC")
    fun getAllStatistics():Flow<List<PuzzleEntity>>

}