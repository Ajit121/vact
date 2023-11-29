package com.score.vact.di.appointment_details

import androidx.lifecycle.ViewModel
import com.score.vact.di.ViewModelKey
import com.score.vact.ui.appointment_details.AppointmentDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class AppointmentDetailsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AppointmentDetailsViewModel::class)
    abstract fun bindAppointmentDetailsModule(appointmentDetailsViewModel: AppointmentDetailsViewModel): ViewModel
}