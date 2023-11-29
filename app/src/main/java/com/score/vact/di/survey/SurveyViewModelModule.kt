package com.score.vact.di.survey

import androidx.lifecycle.ViewModel
import com.score.vact.di.ViewModelKey
import com.score.vact.ui.survey.SurveyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SurveyViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SurveyViewModel::class)
    abstract fun bindSurveyViewModelModule(surveyViewModel: SurveyViewModel):ViewModel
}