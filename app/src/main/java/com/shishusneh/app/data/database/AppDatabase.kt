package com.shishusneh.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shishusneh.app.data.dao.BabyProfileDao
import com.shishusneh.app.data.dao.FeedingTipDao
import com.shishusneh.app.data.dao.MilestoneDao
import com.shishusneh.app.data.dao.UserDao
import com.shishusneh.app.data.dao.VaccinationDao
import com.shishusneh.app.data.dao.WeightEntryDao
import com.shishusneh.app.data.entity.BabyProfileEntity
import com.shishusneh.app.data.entity.FeedingTipEntity
import com.shishusneh.app.data.entity.MilestoneEntity
import com.shishusneh.app.data.entity.UserEntity
import com.shishusneh.app.data.entity.VaccinationEntity
import com.shishusneh.app.data.entity.WeightEntryEntity

@Database(
    entities = [
        UserEntity::class,
        BabyProfileEntity::class,
        WeightEntryEntity::class,
        VaccinationEntity::class,
        MilestoneEntity::class,
        FeedingTipEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun babyProfileDao(): BabyProfileDao
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun feedingTipDao(): FeedingTipDao
}
