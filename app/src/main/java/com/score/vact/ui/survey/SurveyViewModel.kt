package com.score.vact.ui.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score.vact.model.QuestionData
import com.score.vact.repository.SurveyRepository
import com.score.vact.vo.Answer
import com.score.vact.vo.Resource
import com.score.vact.vo.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SurveyViewModel @Inject constructor(private val surveyRepository: SurveyRepository) :
    ViewModel() {

    private val _questionsResponse = MutableLiveData<Resource<Int>>()

    val questionsResponse: LiveData<Resource<Int>>
        get() = _questionsResponse

    private val _questions = MutableLiveData<List<QuestionData>>()
    val question: LiveData<List<QuestionData>>
        get() = _questions

    init {
        getSurveyQuestions()
    }

    fun getSurveyQuestions() = viewModelScope.launch {
        _questionsResponse.postValue(Resource(status = Status.LOADING, data = null, message = null))
        val questions = surveyRepository.getSurveyQuestions()
        questions.observeForever {
            if (it == null) {
                return@observeForever
            }
            _questions.postValue(it)
            _questionsResponse.postValue(
                Resource(
                    status = Status.SUCCESS,
                    data = 1,//Just a dummy int value
                    message = null
                )
            )

        }
    }

    suspend fun submitAnswer(questionId: Int, answer: Answer) = viewModelScope.launch {
        _questions.value!!.forEach {
            if (questionId == it.id) {
                it.answer = answer
            }
        }
        _questions.postValue(_questions.value)

    }
}
