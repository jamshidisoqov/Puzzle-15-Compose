package uz.gita.puzzle15_compose.directions

import uz.gita.puzzle15_compose.data.enums.Level

// Created by Jamshid Isoqov an 11/15/2022
interface MainScreenDirections {

   suspend fun navigateToInfoScreen()

   suspend fun navigateToStatisticsScreen()

   suspend fun navigateToGameScreen(level: Level)

}