package com.shishusneh.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shishusneh.app.data.entity.FamilyMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: FamilyMemberEntity): Long

    @Query("SELECT * FROM family_members WHERE babyId = :babyId ORDER BY name ASC")
    fun observeForBaby(babyId: Long): Flow<List<FamilyMemberEntity>>
}
