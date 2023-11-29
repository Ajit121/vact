package com.score.vact.repository

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefs @Inject constructor(private val sharedPreferences: SharedPreferences) {


    var userName: String
        set(value) {
            val editor = sharedPreferences.edit()
            editor.putString("name", value)
            editor.apply()
        }
        get() = sharedPreferences.getString("name", "")!!

    var userId:Int
    set(value) {
        val editor = sharedPreferences.edit()
        editor.putInt("userId",value)
        editor.apply()
    }
    get() = sharedPreferences.getInt("userId",0)
}