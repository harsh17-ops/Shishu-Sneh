package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shishusneh.app.data.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: WeightEntryEntity): Long

    @Query("SELECT * FROM weight_entries WHERE babyId = :babyId ORDER BY recordedAt ASC")
    fun observeAllForBaby(babyId: Long): Flow<List<WeightEntryEntity>>

    @Query("SELECT * FROM weight_entries WHERE babyId = :babyId ORDER BY recordedAt DESC LIMIT 1")
    fun observeLatestForBaby(babyId: Long): Flow<WeightEntryEntity?>

    @Query("SELECT * FROM weight_entries WHERE babyId = :babyId ORDER BY recordedAt ASC")
    suspend fun getAllForBaby(babyId: Long): List<WeightEntryEntity>
}
