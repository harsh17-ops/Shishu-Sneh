package com.shishusneh.app.di

import android.content.Context
import androidx.room.Room
import com.shishusneh.app.data.dao.BabyProfileDao
import com.shishusneh.app.data.dao.FeedingTipDao
import com.shishusneh.app.data.dao.MilestoneDao
import com.shishusneh.app.data.dao.UserDao
import com.shishusneh.app.data.dao.VaccinationDao
import com.shishusneh.app.data.dao.WeightEntryDao
import com.shishusneh.app.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "shishu_sneh.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideUserDao(database: AppDatabase): UserDao = database.userDao()
    @Provides fun provideBabyProfileDao(database: AppDatabase): BabyProfileDao = database.babyProfileDao()
    @Provides fun provideWeightDao(database: AppDatabase): WeightEntryDao = database.weightEntryDao()
    @Provides fun provideVaccinationDao(database: AppDatabase): VaccinationDao = database.vaccinationDao()
    @Provides fun provideMilestoneDao(database: AppDatabase): MilestoneDao = database.milestoneDao()
    @Provides fun provideFeedingTipDao(database: AppDatabase): FeedingTipDao = database.feedingTipDao()
}
