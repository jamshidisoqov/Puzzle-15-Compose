package uz.gita.puzzle15_compose.presentation.main.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.puzzle15_compose.data.local.MySharedPref
import uz.gita.puzzle15_compose.directions.MainScreenDirections
import uz.gita.puzzle15_compose.presentation.main.MainIntent
import uz.gita.puzzle15_compose.presentation.main.MainUiState
import uz.gita.puzzle15_compose.presentation.main.MainViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val directions: MainScreenDirections,
    private val mySharedPref: MySharedPref
) : MainViewModel, ViewModel() {

    override val container: Container<MainUiState, Nothing> = container(MainUiState())

    private var volume = mySharedPref.volume

    init {
        intent {
            reduce {
                state.copy(volume = volume)
            }
        }
    }

    override fun onEventDispatcher(intent: MainIntent) = intent {
        when (intent) {

            is MainIntent.OpenGameScreen -> {
                directions.navigateToGameScreen(intent.level)
            }

            MainIntent.OpenInfoScreen -> {
                directions.navigateToInfoScreen()
            }
            MainIntent.OpenStatistics -> {
                directions.navigateToStatisticsScreen()
            }

            MainIntent.VolumeClick -> {
                reduce {
                    volume = !volume
                    mySharedPref.volume = volume
                    state.copy(volume = volume)
                }
            }
        }

    }
}