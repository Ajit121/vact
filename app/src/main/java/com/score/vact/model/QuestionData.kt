package com.score.vact.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.score.vact.vo.Answer

@Entity(tableName = "survey")
data class QuestionData(
    @PrimaryKey
    val id: Int,
    val question: String,
    var answer:Answer=Answer.NA)