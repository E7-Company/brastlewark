package com.axa.brastlewark.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.axa.brastlewark.model.Gnome

@Dao
interface GnomeDao {

    @Query("SELECT * from gnomes ORDER BY name")
    fun getGnomes(): LiveData<List<Gnome>>

    @Query("SELECT * from gnomes WHERE id = :id")
    fun getGnome(id: Int): LiveData<Gnome>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gnomes: List<Gnome>)

}