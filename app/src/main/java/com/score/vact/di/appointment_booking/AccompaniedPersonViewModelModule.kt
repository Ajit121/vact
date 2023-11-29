package com.score.vact.di.appointment_booking

import androidx.lifecycle.ViewModel
import com.score.vact.di.ViewModelKey
import com.score.vact.ui.appointment_booking.AccompaniedPersonViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AccompaniedPersonViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccompaniedPersonViewModel::class)
    abstract fun bindAccompaniedPersonViewModelMoldue(accompaniedPersonViewModel: AccompaniedPersonViewModel):ViewModel
}