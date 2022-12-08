package uz.gita.puzzle15_compose.presentation.game.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.puzzle15_compose.data.enums.Level
import uz.gita.puzzle15_compose.data.local.MySharedPref
import uz.gita.puzzle15_compose.data.local.room.PuzzleDao
import uz.gita.puzzle15_compose.data.local.room.PuzzleEntity
import uz.gita.puzzle15_compose.navigation.AppNavigation
import uz.gita.puzzle15_compose.presentation.game.GameIntent
import uz.gita.puzzle15_compose.presentation.game.GameUiState
import uz.gita.puzzle15_compose.presentation.game.GameViewModel
import uz.gita.puzzle15_compose.presentation.game.SideEffect
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@HiltViewModel
class GameViewModelImpl @Inject constructor(
    private val mySharedPref: MySharedPref,
    private val puzzleDao: PuzzleDao,
    private val navigation: AppNavigation
) : GameViewModel, ViewModel() {

    private var level: Level = Level.EASY

    private var emptyCoordinate = Pair(3, 3)

    private var time = 0

    private var move = 0


    override val container: Container<GameUiState, SideEffect> = container(GameUiState())

    init {
        intent {
            reduce {
                state.copy(volume = mySharedPref.volume)
            }
        }
    }

    override fun generateOrGet(level: Level) {
        this.level = level
        val numbers = when (level) {

            Level.EASY -> {
                time = mySharedPref.time3
                move = mySharedPref.move3
                mySharedPref.puzzle3
            }

            Level.MEDIUM -> {

                time = mySharedPref.time4
                move = mySharedPref.move4
                mySharedPref.puzzle4
            }

            Level.HARD -> {

                time = mySharedPref.time5
                move = mySharedPref.move5
                mySharedPref.puzzle5
            }
        }
        viewModelScope.launch {
            if (numbers.isEmpty()) {
                playingNewGame()
                mySharedPref.isNewGame = false
                time = 0
                move = 0
            } else {
                val type = object : TypeToken<List<Int>>() {}.type

                val list = Gson().fromJson<List<Int>>(numbers, type)

                val index = list.indexOf(0)

                val y = index / level.level
                val x = index % level.level

                emptyCoordinate = Pair(x, y)

                intent {
                    reduce {
                        state.copy(
                            numberList = list.toMutableList(),
                            time = time,
                            moved = move,
                            emptyCoordinate = emptyCoordinate
                        )
                    }
                }
            }
        }
    }

    override fun playingNewGame() {
        viewModelScope.launch {
            intent {
                reduce {
                    emptyCoordinate = Pair(level.level - 1, level.level - 1)
                    state.copy(
                        time = time,
                        moved = move,
                        numberList = generateNumber(level),
                        emptyCoordinate = emptyCoordinate
                    )
                }
            }
        }
    }

    override fun saveGame() {
        val type = object : TypeToken<List<Int>>() {}.type
        val uiState = container.stateFlow.value
        val numbers = Gson().toJson(uiState.numberList, type)
        when (level) {
            Level.EASY -> {
                mySharedPref.puzzle3 = numbers
                mySharedPref.move3 = uiState.moved
            }
            Level.MEDIUM -> {
                mySharedPref.puzzle4 = numbers
                mySharedPref.move4 = uiState.moved
            }
            Level.HARD -> {
                mySharedPref.puzzle5 = numbers
                mySharedPref.move5 = uiState.moved
            }
        }

    }

    override fun saveTime(time: Int) {
        when (level) {
            Level.EASY -> {
                mySharedPref.time3 = time
            }
            Level.MEDIUM -> {
                mySharedPref.time4 = time
            }
            Level.HARD -> {
                mySharedPref.time5 = time
            }
        }
    }

    override fun onEventDispatcher(intent: GameIntent) {
        when (intent) {
            GameIntent.Refresh -> {
                time = 0
                move = 0
                intent {
                    reduce {
                        state.copy(isWin = false)
                    }
                }
                playingNewGame()
            }

            GameIntent.Back -> {
                viewModelScope.launch {
                    when (level) {
                        Level.EASY -> {
                            mySharedPref.puzzle3 = ""
                        }

                        Level.MEDIUM -> {
                            mySharedPref.puzzle4 = ""
                        }

                        Level.HARD -> {
                            mySharedPref.puzzle5 = ""
                        }
                    }
                    navigation.back()
                }
            }
            is GameIntent.NumberClick -> {
                val x = intent.coordinate.first
                val y = intent.coordinate.second
                if (abs(emptyCoordinate.first - x) + abs(emptyCoordinate.second - y) == 1) {
                    val index = y * level.level + x
                    intent {
                        reduce {
                            val list = state.numberList
                            val num = list[index]
                            val emptyIndex =
                                emptyCoordinate.first + emptyCoordinate.second * level.level
                            list[emptyIndex] = num
                            list[index] = 0
                            move++
                            emptyCoordinate = intent.coordinate
                            if (emptyCoordinate.first == level.level - 1 && emptyCoordinate.second == level.level - 1) {
                                if (checkWinGame(list)) {
                                    intent {
                                        reduce {
                                            state.copy(isWin = true)
                                        }
                                        postSideEffect(SideEffect(openWinGame = true))
                                    }
                                }
                            }
                            state.copy(
                                numberList = list,
                                moved = move,
                                emptyCoordinate = emptyCoordinate
                            )
                        }
                    }
                }
            }

            is GameIntent.SaveGame -> {
                viewModelScope.launch {
                    val move = container.stateFlow.value.moved
                    puzzleDao.insertPuzzle(PuzzleEntity(0, intent.time, move))
                }
            }
        }
    }

    private fun checkWinGame(numberList: List<Int>): Boolean {
        val size = numberList.size
        for (i in 0 until size - 1) {
            if (numberList[i] != (i + 1)) {
                return false
            }
        }
        return true
    }

    private fun generateNumber(level: Level): MutableList<Int> {
        val numberList = ArrayList<Int>()
        for (i in 1 until level.level.toDouble().pow(2.0).toInt()) {
            numberList.add(i)
        }
        numberList.shuffle()
        numberList.add(numberList.size, 0)
        if (isSolvable(numberList.toIntArray())) {
            return numberList
        }
        return generateNumber(level)
    }

    private fun isSolvable(puzzle: IntArray): Boolean {
        var parity = 0
        val gridWidth = sqrt(puzzle.size.toDouble()).toInt()
        var row = 0
        var blankRow = 0
        for (i in puzzle.indices) {
            if (i % gridWidth == 0) {
                row++
            }
            if (puzzle[i] == 0) {
                blankRow = row
                continue
            }
            for (j in i + 1 until puzzle.size) {
                if (puzzle[i] > puzzle[i] && puzzle[i] != 0) {
                    parity++
                }
            }
        }
        return if (gridWidth % 2 == 0) {
            if (blankRow % 2 == 0) {
                parity % 2 == 0
            } else {
                parity % 2 != 0
            }
        } else {
            parity % 2 == 0
        }
    }


}