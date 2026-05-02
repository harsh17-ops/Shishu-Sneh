package com.shishusneh.app.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.shishusneh.app.data.entity.VaccinationEntity
import com.shishusneh.app.worker.VaccineReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccineScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun scheduleReminder(vaccination: VaccinationEntity) {
        if (vaccination.isCompleted) return
        schedule(vaccination, reminderType = "pre", triggerAt = vaccination.dueDateMillis - TimeUnit.DAYS.toMillis(3))
        schedule(vaccination, reminderType = "due", triggerAt = vaccination.dueDateMillis)
    }

    private fun schedule(vaccination: VaccinationEntity, reminderType: String, triggerAt: Long) {
        val delay = triggerAt - System.currentTimeMillis()
        if (delay <= 0) return

        val request = OneTimeWorkRequestBuilder<VaccineReminderWorker>()
            .setInputData(
                Data.Builder()
                    .putLong(VaccineReminderWorker.KEY_VACCINE_ID, vaccination.id)
                    .putString(VaccineReminderWorker.KEY_REMINDER_TYPE, reminderType)
                    .build()
            )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.NONE)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniqueWork(
            uniqueName(vaccination.id, reminderType),
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelReminder(vaccinationId: Long) {
        workManager.cancelUniqueWork(uniqueName(vaccinationId, "pre"))
        workManager.cancelUniqueWork(uniqueName(vaccinationId, "due"))
    }

    private fun uniqueName(vaccinationId: Long, type: String) = "vaccine_${vaccinationId}_$type"
}
