package com.score.vact.datasource

import android.accounts.NetworkErrorException
import android.os.Handler
import com.score.vact.api.AppService
import com.score.vact.model.QuestionData
import com.score.vact.vo.Resource
import com.score.vact.vo.ResponseException
import com.score.vact.vo.Status
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject

class SurveyDataSource @Inject constructor(private val appService: AppService) {
    suspend fun getSurveyQuestions():List<QuestionData> {

        try {
            //val apiResponse = appService.getSurveyQuestions().await()
            return  getDummyQuestions()

        } catch (ex: Exception) {
            ex.printStackTrace()
            return emptyList()
        }
    }


    private  fun postError(message: String): Resource<List<QuestionData>> {
        return Resource(status = Status.ERROR, data = null, message = message)
    }

    private fun getDummyQuestions(): MutableList<QuestionData> {
        val questions: MutableList<QuestionData> = mutableListOf<QuestionData>()
        questions.add(QuestionData(1, "What are symptoms of coronavirus (COVID-19)?"))
        questions.add(
            QuestionData(
                2,
                "What are some other symptoms coronavirus patients might experience?"
            )
        )
        questions.add(
            QuestionData(
                3,
                "How long does it take to develop symptoms after you have been exposed to COVID-19?"
            )
        )
        questions.add(
            QuestionData(
                4,
                "Is it possible to have other coronavirus symptoms without the fever?"
            )
        )

        return questions
    }
}