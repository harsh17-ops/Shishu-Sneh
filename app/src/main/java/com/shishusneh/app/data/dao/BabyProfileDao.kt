package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shishusneh.app.data.entity.BabyProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: BabyProfileEntity): Long

    @Update
    suspend fun update(profile: BabyProfileEntity)

    @Query("SELECT * FROM baby_profiles WHERE userId = :userId LIMIT 1")
    fun observeByUser(userId: Long): Flow<BabyProfileEntity?>

    @Query("SELECT * FROM baby_profiles WHERE userId = :userId LIMIT 1")
    suspend fun getByUser(userId: Long): BabyProfileEntity?
}
