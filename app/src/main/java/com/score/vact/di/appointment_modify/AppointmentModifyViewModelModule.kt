package com.score.vact.di.appointment_modify

import androidx.lifecycle.ViewModel
import com.score.vact.di.ViewModelKey
import com.score.vact.ui.appointment_modify.AppointmentModifyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class AppointmentModifyViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AppointmentModifyViewModel::class)
    abstract fun bindAppointmentModifyViewModel(appointmentModifyViewModel: AppointmentModifyViewModel): ViewModel
}