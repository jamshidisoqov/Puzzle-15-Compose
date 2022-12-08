package uz.gita.puzzle15_compose.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.puzzle15_compose.data.local.room.PuzzleDao
import uz.gita.puzzle15_compose.navigation.AppNavigation
import javax.inject.Inject

@HiltViewModel
class ResultViewModelImpl @Inject constructor(
    private val navigation: AppNavigation,
    private val dao: PuzzleDao
) : ResultViewModel, ViewModel() {
    override val container: Container<ResultUiState, Nothing> = container(ResultUiState())

    init {
        viewModelScope.launch {
            dao.getAllStatistics().collectLatest { list ->
                intent {
                    reduce {
                        state.copy(resultList = list)
                    }
                }
            }
        }
    }

    override fun onEventDispatcher(intent: ResultIntent) {
        viewModelScope.launch {
            navigation.back()
        }
    }
}