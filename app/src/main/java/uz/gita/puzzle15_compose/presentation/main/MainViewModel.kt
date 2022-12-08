package uz.gita.puzzle15_compose.presentation.main

import uz.gita.puzzle15_compose.data.enums.Level
import uz.gita.puzzle15_compose.utils.BaseViewModel

// Created by Jamshid Isoqov an 11/15/2022
interface MainViewModel : BaseViewModel<MainIntent, MainUiState, Nothing>

sealed interface MainIntent {

    class OpenGameScreen(val level: Level) : MainIntent

    object OpenInfoScreen : MainIntent

    object OpenStatistics : MainIntent

    object VolumeClick : MainIntent


}

data class MainUiState(
    val volume: Boolean = true
)