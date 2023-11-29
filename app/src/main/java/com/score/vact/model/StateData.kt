package com.score.vact.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class StateData(
    @SerializedName("ID")
    val id: String,

    @SerializedName("NAME")
    val name: String
){
    override fun toString(): String {
        return name
    }
}