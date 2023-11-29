package com.score.vact.repository

import javax.inject.Inject

class SplashRepo @Inject constructor(private val sharedPrefs: SharedPrefs){
    val isLoggedIn = sharedPrefs.userId!=0
}