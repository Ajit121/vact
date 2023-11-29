package com.score.vact.di.visitor_registration

import androidx.lifecycle.ViewModel
import com.score.vact.di.ViewModelKey
import com.score.vact.ui.visitor_registration.VisitorRegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VisitorRegistrationViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(VisitorRegistrationViewModel::class)
    abstract fun bindVisitorRegistrationModule(registrationViewModel: VisitorRegistrationViewModel): ViewModel
}