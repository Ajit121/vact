package com.score.vact.ui.splash

import androidx.lifecycle.ViewModel
import com.score.vact.repository.SplashRepo
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val splashRepo: SplashRepo): ViewModel() {
    val isLoggedIn = splashRepo.isLoggedIn
}