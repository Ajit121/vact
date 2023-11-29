package com.score.vact.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class IDProofData(
    @SerializedName("ID")
    val id: String,

    @SerializedName("NAME")
    val name: String
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun toString(): String {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IDProofData> {
        override fun createFromParcel(parcel: Parcel): IDProofData {
            return IDProofData(parcel)
        }

        override fun newArray(size: Int): Array<IDProofData?> {
            return arrayOfNulls(size)
        }
    }
}