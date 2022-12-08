package uz.gita.puzzle15_compose.presentation.game

import uz.gita.puzzle15_compose.data.enums.Level
import uz.gita.puzzle15_compose.utils.BaseViewModel

// Created by Jamshid Isoqov an 11/11/2022
interface GameViewModel : BaseViewModel<GameIntent, GameUiState, SideEffect> {

    fun generateOrGet(level: Level)

    fun playingNewGame()

    fun saveGame()

    fun saveTime(time: Int)

}

data class SideEffect(
    val openWinGame: Boolean = false,
    val openQuitGame: Boolean = false
)

sealed interface GameIntent {
    class NumberClick(val coordinate: Pair<Int, Int>) : GameIntent
    object Refresh : GameIntent
    data class SaveGame(val moved: Int, val time: Int) : GameIntent
    object Back:GameIntent
}

data class GameUiState(
    val time: Int = 0,
    val moved: Int = 0,
    val numberList: MutableList<Int> = arrayListOf(),
    val emptyCoordinate: Pair<Int, Int> = Pair(3, 3),
    val volume: Boolean = false,
    val isWin:Boolean = false
)