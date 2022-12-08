package uz.gita.puzzle15_compose.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.puzzle15_compose.utils.getCurrentDate

// Created by Jamshid Isoqov an 11/16/2022
@Entity(tableName = "puzzle")
data class PuzzleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: Int,
    val move: Int,
    val date: String = getCurrentDate()
)
