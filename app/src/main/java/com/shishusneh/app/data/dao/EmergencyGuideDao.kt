package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shishusneh.app.data.entity.EmergencyGuideEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyGuideDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(guides: List<EmergencyGuideEntity>)

    @Query("SELECT * FROM emergency_guides ORDER BY priority DESC, category ASC")
    fun observeAll(): Flow<List<EmergencyGuideEntity>>

    @Query("SELECT * FROM emergency_guides")
    suspend fun getAll(): List<EmergencyGuideEntity>
}
