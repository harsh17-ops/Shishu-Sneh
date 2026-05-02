package com.shishusneh.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shishusneh.app.data.dao.VaccinationDao
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class VaccineReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val vaccinationDao: VaccinationDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val vaccineId = inputData.getLong(KEY_VACCINE_ID, -1L)
        val reminderType = inputData.getString(KEY_REMINDER_TYPE).orEmpty()
        if (vaccineId == -1L) return Result.failure()

        val vaccine = vaccinationDao.getById(vaccineId) ?: return Result.failure()
        if (vaccine.isCompleted) return Result.success()

        val title = if (reminderType == "pre") {
            "${vaccine.name} due in 3 days"
        } else {
            "${vaccine.name} is due today"
        }

        val body = "${vaccine.diseasePrevented} protection. Due on ${DateUtils.formatDate(vaccine.dueDateMillis)}."
        NotificationHelper.showVaccineReminder(
            context = applicationContext,
            notificationId = vaccine.id.toInt(),
            title = title,
            body = body
        )
        return Result.success()
    }

    companion object {
        const val KEY_VACCINE_ID = "vaccine_id"
        const val KEY_REMINDER_TYPE = "reminder_type"
    }
}
