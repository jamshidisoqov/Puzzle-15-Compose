package uz.gita.puzzle15_compose.presentation.result

import uz.gita.puzzle15_compose.data.local.room.PuzzleEntity
import uz.gita.puzzle15_compose.utils.BaseViewModel

// Created by Jamshid Isoqov an 11/17/2022
interface ResultViewModel : BaseViewModel<ResultIntent, ResultUiState, Nothing>


sealed interface ResultIntent {
    object Back : ResultIntent
}

data class ResultUiState(
    val resultList: List<PuzzleEntity> = emptyList()
)