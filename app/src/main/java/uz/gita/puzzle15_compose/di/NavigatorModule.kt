package uz.gita.puzzle15_compose.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.puzzle15_compose.navigation.AppNavigation
import uz.gita.puzzle15_compose.navigation.NavigationDispatcher
import uz.gita.puzzle15_compose.navigation.NavigationHandler

@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {

    @Binds
    fun appNavigator(dispatcher: NavigationDispatcher): AppNavigation

    @Binds
    fun navigationHandler(dispatcher: NavigationDispatcher): NavigationHandler
}