package com.score.vact.di.splash

import androidx.lifecycle.ViewModel
import com.score.vact.di.ViewModelKey
import com.score.vact.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SplashViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindViewModelModule(splashViewModel: SplashViewModel):ViewModel
}