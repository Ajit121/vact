package com.score.vact.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.score.vact.model.QuestionData

@Dao
interface SurveyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions:List<QuestionData>)

    @Query("select * from survey")
    fun getSurveyQuestions():LiveData<List<QuestionData>>
}