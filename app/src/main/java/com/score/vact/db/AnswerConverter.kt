package com.score.vact.db

import androidx.room.TypeConverter
import com.score.vact.vo.Answer

class AnswerConverter {
    @TypeConverter
    fun toHealth(value: String) = enumValueOf<Answer>(value)

    @TypeConverter
    fun fromHealth(value: Answer) = value.name
}