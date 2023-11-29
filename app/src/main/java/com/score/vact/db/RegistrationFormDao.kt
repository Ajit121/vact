package com.score.vact.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.score.vact.model.CountryData
import kotlinx.coroutines.Deferred

@Dao
interface RegistrationFormDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries:List<CountryData>)

    @Query("select * from country_master")
    fun getCountries():LiveData<List<CountryData>>
}