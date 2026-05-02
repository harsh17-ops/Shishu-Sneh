package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.app.data.entity.VaccinationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccinationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entries: List<VaccinationEntity>)

    @Update
    suspend fun update(vaccination: VaccinationEntity)

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId ORDER BY dueDateMillis ASC")
    fun observeForBaby(babyId: Long): Flow<List<VaccinationEntity>>

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId AND isCompleted = 0 ORDER BY dueDateMillis ASC LIMIT 1")
    fun observeNextPending(babyId: Long): Flow<VaccinationEntity?>

    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId ORDER BY dueDateMillis ASC")
    suspend fun getForBaby(babyId: Long): List<VaccinationEntity>

    @Query("SELECT * FROM vaccinations WHERE id = :vaccinationId LIMIT 1")
    suspend fun getById(vaccinationId: Long): VaccinationEntity?
}
