package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shishusneh.app.data.entity.FeedingTipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedingTipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tips: List<FeedingTipEntity>)

    @Query("SELECT * FROM feeding_tips")
    suspend fun getAll(): List<FeedingTipEntity>

    @Query(
        "SELECT * FROM feeding_tips WHERE :ageMonths BETWEEN minAgeMonths AND maxAgeMonths ORDER BY category, minAgeMonths"
    )
    fun observeForAge(ageMonths: Int): Flow<List<FeedingTipEntity>>
}
