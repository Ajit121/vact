package com.score.vact.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.score.vact.model.appointment.AccompaniedBy

class AccompaniedByDataConverter {
    @TypeConverter
    fun listToJson(value: List<AccompaniedBy>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<AccompaniedBy>::class.java).toList()
}