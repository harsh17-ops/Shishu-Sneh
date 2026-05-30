package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.app.data.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: AppointmentEntity): Long

    @Update
    suspend fun update(appointment: AppointmentEntity)

    @Query("SELECT * FROM appointments WHERE babyId = :babyId ORDER BY appointmentAtMillis ASC")
    fun observeForBaby(babyId: Long): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE id = :appointmentId LIMIT 1")
    suspend fun getById(appointmentId: Long): AppointmentEntity?

    @Query("SELECT * FROM appointments WHERE babyId = :babyId ORDER BY appointmentAtMillis ASC")
    suspend fun getForBaby(babyId: Long): List<AppointmentEntity>
}
