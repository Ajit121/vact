package com.score.vact.model.appointment

import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "appointments")
data class AppointmentData(
    @PrimaryKey
    var id:Int,
    @SerializedName("image_url")
    var profileImage: String,
    var name: String,
    var number: String,
    var email: String,
    var address: String,
    var date: String,
    var time: String,
    var status: Int
)