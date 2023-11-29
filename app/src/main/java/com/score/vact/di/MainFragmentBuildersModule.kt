package com.score.vact.di

import com.score.vact.di.appointment_booking.AccompaniedPersonViewModelModule
import com.score.vact.di.appointment_booking.AppointmentBookingScope
import com.score.vact.di.appointment_booking.AppointmentBookingViewModelModule
import com.score.vact.di.home.HomeScope
import com.score.vact.di.home.HomeViewModelModule
import com.score.vact.di.survey.SurveyScope
import com.score.vact.di.survey.SurveyViewModelModule
import com.score.vact.di.visitor_registration.VisitorRegistrationScope
import com.score.vact.di.visitor_registration.VisitorRegistrationViewModelModule
import com.score.vact.ui.appointment_booking.AccompaniedPersonFragment
import com.score.vact.ui.survey.SurveyFragment
import com.score.vact.ui.appointment_booking.AppointmentBookingFragment
import com.score.vact.ui.home.HomeFragment
import com.score.vact.ui.visitor_registration.VisitorRegistrationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainFragmentBuildersModule {

    @HomeScope
    @ContributesAndroidInjector(modules = [HomeViewModelModule::class])
    abstract fun contributeHomeFragment(): HomeFragment

    @VisitorRegistrationScope
    @ContributesAndroidInjector(modules = [VisitorRegistrationViewModelModule::class])
    abstract fun contributeVisitorRegistrationFragment(): VisitorRegistrationFragment

    @AppointmentBookingScope
    @ContributesAndroidInjector(modules = [AppointmentBookingViewModelModule::class])
    abstract fun contributeAppointmentBookingFragment(): AppointmentBookingFragment

    @AppointmentBookingScope
    @ContributesAndroidInjector(modules = [AccompaniedPersonViewModelModule::class])
    abstract fun contributeAccompaniedPersonFragment():AccompaniedPersonFragment

    @SurveyScope
    @ContributesAndroidInjector(modules = [SurveyViewModelModule::class])
    abstract fun contributeSurveyFragment(): SurveyFragment
}