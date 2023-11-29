package com.score.vact.di

import com.score.vact.di.appointment_details.AppointmentDetailsScope
import com.score.vact.di.appointment_details.AppointmentDetailsViewModelModule
import com.score.vact.di.appointment_list.AppointmentListScope
import com.score.vact.di.appointment_list.AppointmentListViewModelModule
import com.score.vact.di.appointment_modify.AppointmentModifyScope
import com.score.vact.di.appointment_modify.AppointmentModifyViewModelModule
import com.score.vact.ui.appointment_details.AppointmentDetailsFragment
import com.score.vact.ui.appointment_list.AppointmentsFragment
import com.score.vact.ui.appointment_modify.AppointmentModifyFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AppointmentFragmentBuilderModule {

    @AppointmentListScope
    @ContributesAndroidInjector(modules = [AppointmentListViewModelModule::class])
    abstract fun contributeAppointmentListFragment(): AppointmentsFragment

    @AppointmentDetailsScope
    @ContributesAndroidInjector(modules = [AppointmentDetailsViewModelModule::class])
    abstract fun contributeAppointmentDetailsFragment(): AppointmentDetailsFragment

    @AppointmentModifyScope
    @ContributesAndroidInjector(modules = [AppointmentModifyViewModelModule::class])
    abstract fun contributeAppointmentModifyFragment(): AppointmentModifyFragment
}