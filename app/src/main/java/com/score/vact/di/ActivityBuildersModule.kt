package com.score.vact.di

import com.score.vact.di.login.LoginScope
import com.score.vact.di.login.LoginViewModelModule
import com.score.vact.di.scope.AppointmentScope
import com.score.vact.di.scope.MainScope
import com.score.vact.di.splash.SplashScope
import com.score.vact.di.splash.SplashViewModelModule
import com.score.vact.ui.AppointmentListActivity
import com.score.vact.ui.login.LoginActivity
import com.score.vact.ui.MainActivity
import com.score.vact.ui.splash.SplashActivity
import com.score.vact.ui.splash.SplashViewModel
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuildersModule {

    @SplashScope
    @ContributesAndroidInjector(modules = [SplashViewModelModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @LoginScope
    @ContributesAndroidInjector(modules = [LoginViewModelModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @AppointmentScope
    @ContributesAndroidInjector(modules = [AppointmentFragmentBuilderModule::class])
    abstract fun contributeAppointmentListActivity():AppointmentListActivity

}