package com.shishusneh.app.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.shishusneh.app.data.entity.AppointmentEntity
import com.shishusneh.app.data.entity.BabyProfileEntity
import com.shishusneh.app.data.entity.MilestoneEntity
import com.shishusneh.app.data.entity.VaccinationEntity
import com.shishusneh.app.data.entity.WeightEntryEntity
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfExportHelper @Inject constructor() {
    fun createHealthReport(
        context: Context,
        profile: BabyProfileEntity,
        weights: List<WeightEntryEntity>,
        vaccines: List<VaccinationEntity>,
        milestones: List<MilestoneEntity>,
        appointments: List<AppointmentEntity>,
        summary: String
    ): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val titlePaint = Paint().apply { textSize = 22f; isFakeBoldText = true }
        val bodyPaint = Paint().apply { textSize = 12f }
        var y = 40

        fun line(text: String, gap: Int = 18) {
            canvas.drawText(text, 32f, y.toFloat(), bodyPaint)
            y += gap
        }

        canvas.drawText("Shishu-Sneh Health Report", 32f, y.toFloat(), titlePaint)
        y += 30
        line("Baby: ${profile.name}")
        line("Mother: ${profile.motherName}")
        line("DOB: ${DateUtils.formatDate(profile.dobMillis)}")
        line("Age: ${DateUtils.ageLabel(profile.dobMillis)}")
        y += 8
        line("Weekly Summary:", 20)
        summary.split("\n").forEach { line(it) }
        y += 8
        line("Recent Growth:", 20)
        weights.takeLast(5).forEach { line("${DateUtils.formatDate(it.recordedAt)} - ${it.weightKg} kg, ${it.heightCm} cm") }
        y += 8
        line("Vaccination Progress:", 20)
        vaccines.take(6).forEach { line("${it.name} - ${if (it.isCompleted) "Completed" else "Pending"}") }
        y += 8
        line("Milestones:", 20)
        milestones.take(6).forEach { line("${it.title} - ${if (it.isAchieved) "Achieved" else "Watch"}") }
        y += 8
        line("Appointments:", 20)
        appointments.take(4).forEach { line("${it.title} with ${it.doctorName} on ${DateUtils.formatDate(it.appointmentAtMillis)}") }

        document.finishPage(page)
        val file = File(context.cacheDir, "shishu_sneh_health_report.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return file
    }
}
