package com.shishusneh.app.repository

import com.shishusneh.app.data.dao.BabyProfileDao
import com.shishusneh.app.data.dao.FeedingTipDao
import com.shishusneh.app.data.dao.MilestoneDao
import com.shishusneh.app.data.dao.VaccinationDao
import com.shishusneh.app.data.dao.WeightEntryDao
import com.shishusneh.app.data.entity.BabyProfileEntity
import com.shishusneh.app.data.entity.FeedingTipEntity
import com.shishusneh.app.data.entity.MilestoneEntity
import com.shishusneh.app.data.entity.VaccinationEntity
import com.shishusneh.app.data.entity.WeightEntryEntity
import com.shishusneh.app.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

data class DashboardSnapshot(
    val babyProfile: BabyProfileEntity?,
    val latestWeight: WeightEntryEntity?,
    val nextVaccination: VaccinationEntity?,
    val milestoneProgress: Int,
    val feedingTip: FeedingTipEntity?
)

@Singleton
class DashboardRepository @Inject constructor(
    private val babyProfileDao: BabyProfileDao,
    private val weightEntryDao: WeightEntryDao,
    private val vaccinationDao: VaccinationDao,
    private val milestoneDao: MilestoneDao,
    private val feedingTipDao: FeedingTipDao
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeDashboard(userId: Long): Flow<DashboardSnapshot> {
        return babyProfileDao.observeByUser(userId).flatMapLatest { profile ->
                if (profile == null) {
                    flowOf(
                        DashboardSnapshot(
                            babyProfile = null,
                            latestWeight = null,
                            nextVaccination = null,
                            milestoneProgress = 0,
                            feedingTip = null
                        )
                    )
                } else {
                    combine(
                        weightEntryDao.observeLatestForBaby(profile.id),
                        vaccinationDao.observeNextPending(profile.id),
                        milestoneDao.observeForBaby(profile.id),
                        feedingTipDao.observeForAge(DateUtils.ageInMonths(profile.dobMillis))
                    ) { latestWeight, nextVaccine, milestones, tips ->
                        DashboardSnapshot(
                            babyProfile = profile,
                            latestWeight = latestWeight,
                            nextVaccination = nextVaccine,
                            milestoneProgress = milestones.progressPercent(),
                            feedingTip = tips.firstOrNull { it.category == "tip" }
                        )
                    }
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeFeedingGuide(userId: Long): Flow<Pair<BabyProfileEntity?, List<FeedingTipEntity>>> {
        return babyProfileDao.observeByUser(userId).flatMapLatest { profile ->
            if (profile == null) flowOf(null to emptyList())
            else feedingTipDao.observeForAge(DateUtils.ageInMonths(profile.dobMillis))
                .combine(flowOf(profile)) { tips, baby -> baby to tips }
        }
    }

    private fun List<MilestoneEntity>.progressPercent(): Int {
        if (isEmpty()) return 0
        val achieved = count { it.isAchieved }
        return ((achieved.toFloat() / size.toFloat()) * 100).toInt()
    }
}
