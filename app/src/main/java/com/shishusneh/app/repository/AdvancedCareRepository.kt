package com.shishusneh.app.repository

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.shishusneh.app.data.dao.AppointmentDao
import com.shishusneh.app.data.dao.BabyProfileDao
import com.shishusneh.app.data.dao.EmergencyGuideDao
import com.shishusneh.app.data.dao.FamilyMemberDao
import com.shishusneh.app.data.dao.MilestoneDao
import com.shishusneh.app.data.dao.VaccineScanDao
import com.shishusneh.app.data.dao.VaccinationDao
import com.shishusneh.app.data.dao.WeightEntryDao
import com.shishusneh.app.data.entity.AppointmentEntity
import com.shishusneh.app.data.entity.EmergencyGuideEntity
import com.shishusneh.app.data.entity.FamilyMemberEntity
import com.shishusneh.app.data.entity.VaccineScanEntity
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.utils.PdfExportHelper
import com.shishusneh.app.utils.SeedData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class WeeklySummary(
    val title: String,
    val content: String
)

data class PercentileInsight(
    val percentileLabel: String,
    val interpretation: String
)

@Singleton
class AdvancedCareRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val babyProfileDao: BabyProfileDao,
    private val weightEntryDao: WeightEntryDao,
    private val vaccinationDao: VaccinationDao,
    private val milestoneDao: MilestoneDao,
    private val appointmentDao: AppointmentDao,
    private val familyMemberDao: FamilyMemberDao,
    private val emergencyGuideDao: EmergencyGuideDao,
    private val vaccineScanDao: VaccineScanDao,
    private val appointmentScheduler: AppointmentScheduler,
    private val pdfExportHelper: PdfExportHelper
) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun ensureAdvancedSeedData() {
        if (emergencyGuideDao.getAll().isEmpty()) {
            emergencyGuideDao.insertAll(SeedData.emergencyGuides())
        }
    }

    fun observeAppointments(babyId: Long): Flow<List<AppointmentEntity>> = appointmentDao.observeForBaby(babyId)
    fun observeFamilyMembers(babyId: Long): Flow<List<FamilyMemberEntity>> = familyMemberDao.observeForBaby(babyId)
    fun observeEmergencyGuides(): Flow<List<EmergencyGuideEntity>> = emergencyGuideDao.observeAll()
    fun observeScans(babyId: Long): Flow<List<VaccineScanEntity>> = vaccineScanDao.observeForBaby(babyId)

    suspend fun addAppointment(
        babyId: Long,
        title: String,
        doctorName: String,
        appointmentAtMillis: Long,
        notes: String
    ): Result<Unit> = runCatching {
        require(title.isNotBlank()) { "Appointment title is required" }
        require(doctorName.isNotBlank()) { "Doctor name is required" }
        val id = appointmentDao.insert(
            AppointmentEntity(
                babyId = babyId,
                title = title.trim(),
                doctorName = doctorName.trim(),
                appointmentAtMillis = appointmentAtMillis,
                notes = notes.trim()
            )
        )
        appointmentDao.getById(id)?.let(appointmentScheduler::scheduleReminder)
    }

    suspend fun addFamilyMember(
        babyId: Long,
        name: String,
        relation: String,
        phoneNumber: String,
        accessLevel: String
    ): Result<Unit> = runCatching {
        require(name.isNotBlank()) { "Family member name is required" }
        require(relation.isNotBlank()) { "Relation is required" }
        familyMemberDao.insert(
            FamilyMemberEntity(
                babyId = babyId,
                name = name.trim(),
                relation = relation.trim(),
                phoneNumber = phoneNumber.trim(),
                accessLevel = accessLevel
            )
        )
    }

    suspend fun extractVaccineCardText(babyId: Long, uri: Uri): Result<String> = runCatching {
        val image = InputImage.fromFilePath(context, uri)
        val result = recognizer.process(image).await()
        val text = result.text.ifBlank { "No text detected from vaccination card." }
        vaccineScanDao.insert(
            VaccineScanEntity(
                babyId = babyId,
                sourceLabel = uri.lastPathSegment ?: "Vaccination card image",
                extractedText = text
            )
        )
        text
    }

    suspend fun generateWeeklySummary(userId: Long): WeeklySummary {
        val profile = babyProfileDao.getByUser(userId) ?: error("Profile not found")
        val weights = weightEntryDao.getAllForBaby(profile.id)
        val vaccines = vaccinationDao.getForBaby(profile.id)
        val milestones = milestoneDao.getForBaby(profile.id)
        val appointments = appointmentDao.getForBaby(profile.id)

        val latestWeight = weights.lastOrNull()
        val pendingVaccines = vaccines.count { !it.isCompleted }
        val achievedMilestones = milestones.count { it.isAchieved }
        val nextAppointment = appointments.firstOrNull { !it.isCompleted && it.appointmentAtMillis >= System.currentTimeMillis() }

        val lines = buildList {
            add("${profile.name} is now ${DateUtils.ageLabel(profile.dobMillis)} old.")
            add(latestWeight?.let { "Latest growth entry: ${it.weightKg} kg and ${it.heightCm} cm." } ?: "No growth entries have been added this week.")
            add("$achievedMilestones milestones have been marked as achieved so far.")
            add("$pendingVaccines vaccines are still pending in the current schedule.")
            add(nextAppointment?.let { "Next doctor appointment: ${it.title} with Dr. ${it.doctorName} on ${DateUtils.formatDate(it.appointmentAtMillis)}." }
                ?: "No upcoming doctor appointments are scheduled.")
        }

        return WeeklySummary(
            title = "Weekly Health Summary",
            content = lines.joinToString("\n")
        )
    }

    suspend fun exportPdfHealthReport(userId: Long): File {
        val profile = babyProfileDao.getByUser(userId) ?: error("Profile not found")
        val summary = generateWeeklySummary(userId)
        return pdfExportHelper.createHealthReport(
            context = context,
            profile = profile,
            weights = weightEntryDao.getAllForBaby(profile.id),
            vaccines = vaccinationDao.getForBaby(profile.id),
            milestones = milestoneDao.getForBaby(profile.id),
            appointments = appointmentDao.getForBaby(profile.id),
            summary = summary.content
        )
    }

    fun answerCareQuestion(question: String): String {
        val normalized = question.lowercase()
        return when {
            "fever" in normalized -> "Watch for poor feeding, fast breathing, or unusual sleepiness with fever. If these appear, seek medical care promptly."
            "feeding" in normalized || "milk" in normalized -> "For the first 6 months, exclusive breastfeeding is best. After 6 months, continue breastfeeding with soft complementary foods."
            "vaccine" in normalized || "vaccination" in normalized -> "Keep vaccines on time and do not delay overdue doses. Open the vaccine screen to review the next due dose."
            "cough" in normalized || "breathing" in normalized -> "If the baby is breathing fast, chest is pulling in, or lips turn blue, this is urgent and needs medical attention."
            "sleep" in normalized -> "Keep a calm sleep routine, place the baby on the back to sleep, and avoid soft loose bedding."
            else -> "I can help with feeding, growth, vaccines, sleep, fever, breathing issues, and milestone questions. Try asking one of those directly."
        }
    }

    suspend fun percentileInsight(userId: Long): PercentileInsight {
        val profile = babyProfileDao.getByUser(userId) ?: error("Profile not found")
        val latest = weightEntryDao.getAllForBaby(profile.id).lastOrNull()
            ?: return PercentileInsight("No data", "Add at least one growth entry to estimate percentile range.")
        val ageMonths = DateUtils.monthsBetween(profile.dobMillis, latest.recordedAt)
        val reference = SeedData.whoReferenceWeights().find { it.first.toInt() == ageMonths }
            ?: return PercentileInsight("Reference unavailable", "Percentile estimate is available for the first year entries.")
        val ratio = latest.weightKg / reference.second.toDouble()
        return when {
            ratio < 0.9 -> PercentileInsight("Below average range", "Weight appears below the median WHO reference. Continue monitoring and discuss with a pediatrician if the trend stays low.")
            ratio > 1.1 -> PercentileInsight("Above average range", "Weight appears above the median WHO reference. Growth may still be healthy, but keep tracking with height and feeding quality.")
            else -> PercentileInsight("Near 50th percentile", "Current weight is close to the median WHO reference for age.")
        }
    }

    fun cloudSyncStatus(): String {
        return "Cloud sync architecture is ready. To enable real Firebase backup, add Firebase project credentials and google-services configuration."
    }
}
