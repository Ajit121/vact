package com.score.vact.di.appointment_booking

import androidx.lifecycle.ViewModel
import com.score.vact.di.ViewModelKey
import com.score.vact.ui.appointment_booking.AppointmentBookingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppointmentBookingViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AppointmentBookingViewModel::class)
    abstract fun bindBookAppointmentModule(appointmentBookingViewModel: AppointmentBookingViewModel): ViewModel

}