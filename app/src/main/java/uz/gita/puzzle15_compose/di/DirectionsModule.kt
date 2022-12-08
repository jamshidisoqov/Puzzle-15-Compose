package uz.gita.puzzle15_compose.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.puzzle15_compose.directions.MainScreenDirections
import uz.gita.puzzle15_compose.directions.impl.MainScreenDirectionsImpl

// Created by Jamshid Isoqov an 11/15/2022
@Module
@InstallIn(ViewModelComponent::class)
interface DirectionsModule {

    @Binds
    fun bindMainScreenDirections(impl: MainScreenDirectionsImpl): MainScreenDirections

}