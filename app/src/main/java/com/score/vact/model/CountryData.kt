package com.score.vact.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "country_master")
data class CountryData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("ID")
    val id: String,

    @ColumnInfo(name = "name")
    @SerializedName("NAME")
    val name: String
){
    override fun toString(): String {
        return name
    }
}