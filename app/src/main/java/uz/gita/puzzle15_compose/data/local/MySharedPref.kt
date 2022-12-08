package uz.gita.puzzle15_compose.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.puzzle15_compose.utils.SharedPreference
import javax.inject.Inject

// Created by Jamshid Isoqov an 11/11/2022
class MySharedPref @Inject constructor(
    @ApplicationContext ctx: Context,
    sharedPreferences: SharedPreferences
) : SharedPreference(
    ctx, sharedPreferences
) {

    var time3: Int by Ints(0)
    var time4: Int by Ints(0)
    var time5: Int by Ints(0)

    var move3: Int by Ints(0)
    var move4: Int by Ints(0)
    var move5: Int by Ints(0)

    var puzzle3: String by Strings("")

    var puzzle4: String by Strings("")

    var puzzle5: String by Strings("")

    var isNewGame: Boolean by Booleans(true)

    var volume: Boolean by Booleans(true)


}