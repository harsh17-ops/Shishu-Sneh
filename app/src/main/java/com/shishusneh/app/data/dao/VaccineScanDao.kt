package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shishusneh.app.data.entity.VaccineScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineScanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scan: VaccineScanEntity): Long

    @Query("SELECT * FROM vaccine_scans WHERE babyId = :babyId ORDER BY capturedAt DESC")
    fun observeForBaby(babyId: Long): Flow<List<VaccineScanEntity>>
}
