package com.score.vact.model.appointment_booking

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "belongings_master")
data class BelongingsData(
    @PrimaryKey
    @SerializedName("ID")
    val id: Int,

    @SerializedName("NAME")
    val name: String,

    var isSelected: Boolean = false)