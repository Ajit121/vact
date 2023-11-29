package com.score.vact.di.appointment_list

import androidx.lifecycle.ViewModel
import com.score.vact.di.ViewModelKey
import com.score.vact.ui.appointment_list.AppointmentsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppointmentListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AppointmentsViewModel::class)
    abstract fun bindAppointmentsModule(registrationViewModel: AppointmentsViewModel): ViewModel

}