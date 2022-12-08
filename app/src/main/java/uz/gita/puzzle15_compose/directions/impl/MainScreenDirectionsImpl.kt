package uz.gita.puzzle15_compose.directions.impl

import uz.gita.puzzle15_compose.data.enums.Level
import uz.gita.puzzle15_compose.directions.MainScreenDirections
import uz.gita.puzzle15_compose.navigation.AppNavigation
import uz.gita.puzzle15_compose.presentation.game.GameScreen
import uz.gita.puzzle15_compose.presentation.result.ResultScreen
import javax.inject.Inject

class MainScreenDirectionsImpl @Inject constructor(
    private val navigation: AppNavigation
): MainScreenDirections {

    override suspend fun navigateToInfoScreen() {
        //navigation.navigateTo()
    }

    override suspend fun navigateToStatisticsScreen() {
        navigation.navigateTo(ResultScreen())
    }

    override suspend fun navigateToGameScreen(level: Level) {
        navigation.navigateTo(GameScreen(level))
    }
}