package com.score.vact.repository

import androidx.lifecycle.LiveData
import com.score.vact.datasource.SurveyDataSource
import com.score.vact.db.SurveyDao
import com.score.vact.model.QuestionData
import com.score.vact.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SurveyRepository @Inject constructor(
    private val surveyDataSource: SurveyDataSource,
    private val surveyDao: SurveyDao
) {
    suspend fun getSurveyQuestions(): LiveData<List<QuestionData>> {
        val questions = surveyDataSource.getSurveyQuestions()
        surveyDao.insertQuestions(questions)
        return surveyDao.getSurveyQuestions()
    }
}