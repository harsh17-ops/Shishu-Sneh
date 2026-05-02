package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.app.data.entity.MilestoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<MilestoneEntity>)

    @Update
    suspend fun update(milestone: MilestoneEntity)

    @Query("SELECT * FROM milestones WHERE babyId = :babyId ORDER BY expectedAgeMonths ASC")
    fun observeForBaby(babyId: Long): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones WHERE babyId = :babyId ORDER BY expectedAgeMonths ASC")
    suspend fun getForBaby(babyId: Long): List<MilestoneEntity>
}
