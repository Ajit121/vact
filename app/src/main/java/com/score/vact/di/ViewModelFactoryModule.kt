
package com.score.vact.di

import androidx.lifecycle.ViewModelProvider
import com.score.vact.viewmodel.ViewModelProviderFactory


import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    /*
     @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);
     */

    /*@Binds
    abstract fun bindViewModelFactory(factory: VACTViewModelFactory): ViewModelProvider.Factory*/

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

}
