package com.shishusneh.app.repository

import android.util.Log
import com.google.gson.GsonBuilder
import com.shishusneh.app.data.dao.AppointmentDao
import com.shishusneh.app.data.dao.BabyProfileDao
import com.shishusneh.app.data.dao.FeedingTipDao
import com.shishusneh.app.data.dao.FamilyMemberDao
import com.shishusneh.app.data.dao.MilestoneDao
import com.shishusneh.app.data.dao.VaccineScanDao
import com.shishusneh.app.data.dao.VaccinationDao
import com.shishusneh.app.data.dao.WeightEntryDao
import com.shishusneh.app.data.entity.BabyProfileEntity
import com.shishusneh.app.data.entity.MilestoneEntity
import com.shishusneh.app.data.entity.VaccinationEntity
import com.shishusneh.app.data.entity.WeightEntryEntity
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.utils.SeedData
import com.shishusneh.app.utils.Validators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BabyRepository @Inject constructor(
    private val babyProfileDao: BabyProfileDao,
    private val weightEntryDao: WeightEntryDao,
    private val vaccinationDao: VaccinationDao,
    private val milestoneDao: MilestoneDao,
    private val feedingTipDao: FeedingTipDao,
    private val appointmentDao: AppointmentDao,
    private val familyMemberDao: FamilyMemberDao,
    private val vaccineScanDao: VaccineScanDao,
    private val settingsRepository: SettingsRepository,
    private val vaccineScheduler: VaccineScheduler
) {
    suspend fun ensureReferenceData() {
        if (feedingTipDao.getAll().isEmpty()) {
            feedingTipDao.insertAll(SeedData.feedingTips())
        }
    }

    fun observeProfile(userId: Long): Flow<BabyProfileEntity?> = babyProfileDao.observeByUser(userId)

    suspend fun getProfile(userId: Long): BabyProfileEntity? = babyProfileDao.getByUser(userId)

    suspend fun saveProfile(
        userId: Long,
        existingId: Long?,
        name: String,
        dobMillis: Long,
        gender: String,
        bloodGroup: String,
        motherName: String
    ): Result<Long> = runCatching {
        require(name.isNotBlank()) { "Baby name is required" }
        require(motherName.isNotBlank()) { "Mother name is required" }
        require(bloodGroup.isNotBlank()) { "Blood group is required" }
        require(dobMillis <= System.currentTimeMillis()) { "Date of birth cannot be in the future" }

        val profile = BabyProfileEntity(
            id = existingId ?: 0,
            userId = userId,
            name = name.trim(),
            dobMillis = dobMillis,
            gender = gender,
            bloodGroup = bloodGroup.trim(),
            motherName = motherName.trim(),
            updatedAt = System.currentTimeMillis()
        )

        val babyId = if (existingId == null) {
            babyProfileDao.insert(profile)
        } else {
            babyProfileDao.update(profile)
            existingId
        }

        ensureDefaultRecords(babyId, dobMillis)
        babyId
    }

    private suspend fun ensureDefaultRecords(babyId: Long, dobMillis: Long) {
        val existingVaccines = vaccinationDao.getForBaby(babyId)
        if (existingVaccines.isEmpty()) {
            val vaccines = SeedData.vaccinations(babyId, dobMillis)
            vaccinationDao.insertAll(vaccines)
            if (settingsRepository.settings.firstOrNull()?.notificationsEnabled != false) {
                vaccinationDao.getForBaby(babyId).forEach(vaccineScheduler::scheduleReminder)
            }
        }

        val existingMilestones = milestoneDao.getForBaby(babyId)
        if (existingMilestones.isEmpty()) {
            milestoneDao.insertAll(SeedData.milestones(babyId))
        }

        if (feedingTipDao.getAll().isEmpty()) {
            feedingTipDao.insertAll(SeedData.feedingTips())
        }
    }

    fun observeWeights(babyId: Long): Flow<List<WeightEntryEntity>> = weightEntryDao.observeAllForBaby(babyId)
    fun observeVaccines(babyId: Long): Flow<List<VaccinationEntity>> = vaccinationDao.observeForBaby(babyId)
    fun observeMilestones(babyId: Long): Flow<List<MilestoneEntity>> = milestoneDao.observeForBaby(babyId)

    suspend fun addGrowthEntry(babyId: Long, weight: String, height: String): Result<Long> = runCatching {
        Validators.validateWeight(weight)?.let(::error)
        Validators.validateHeight(height)?.let(::error)
        weightEntryDao.insert(
            WeightEntryEntity(
                babyId = babyId,
                weightKg = weight.toDouble(),
                heightCm = height.toDouble()
            )
        )
    }.onFailure {
        Log.e("BabyRepository", "Unable to save growth entry", it)
    }

    suspend fun markVaccinationComplete(vaccination: VaccinationEntity): Result<Unit> = runCatching {
        val updated = vaccination.copy(
            isCompleted = true,
            completedDateMillis = System.currentTimeMillis()
        )
        vaccinationDao.update(updated)
        vaccineScheduler.cancelReminder(vaccination.id)
    }

    suspend fun updateMilestone(milestone: MilestoneEntity, achieved: Boolean): Result<Unit> = runCatching {
        milestoneDao.update(
            milestone.copy(
                isAchieved = achieved,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun rescheduleAllVaccines(userId: Long, enabled: Boolean) {
        val profile = babyProfileDao.getByUser(userId) ?: return
        val vaccines = vaccinationDao.getForBaby(profile.id)
        if (enabled) {
            vaccines.filterNot { it.isCompleted }.forEach(vaccineScheduler::scheduleReminder)
        } else {
            vaccines.forEach { vaccineScheduler.cancelReminder(it.id) }
        }
    }

    suspend fun exportData(userId: Long): String {
        val profile = babyProfileDao.getByUser(userId) ?: error("Profile not found")
        val data = mapOf(
            "exportedAt" to DateUtils.formatDate(System.currentTimeMillis()),
            "profile" to profile,
            "weights" to weightEntryDao.getAllForBaby(profile.id),
            "vaccinations" to vaccinationDao.getForBaby(profile.id),
            "milestones" to milestoneDao.getForBaby(profile.id),
            "appointments" to appointmentDao.getForBaby(profile.id),
            "familyMembers" to familyMemberDao.observeForBaby(profile.id).firstOrNull().orEmpty(),
            "vaccineScans" to vaccineScanDao.observeForBaby(profile.id).firstOrNull().orEmpty()
        )
        return GsonBuilder().setPrettyPrinting().create().toJson(data)
    }
}
