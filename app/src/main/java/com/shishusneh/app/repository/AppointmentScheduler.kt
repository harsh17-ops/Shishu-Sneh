package com.shishusneh.app.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.shishusneh.app.data.entity.AppointmentEntity
import com.shishusneh.app.worker.AppointmentReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun scheduleReminder(appointment: AppointmentEntity) {
        if (appointment.isCompleted) return
        val triggerAt = appointment.appointmentAtMillis - TimeUnit.DAYS.toMillis(1)
        val delay = triggerAt - System.currentTimeMillis()
        if (delay <= 0) return

        val request = OneTimeWorkRequestBuilder<AppointmentReminderWorker>()
            .setInputData(
                Data.Builder()
                    .putLong(AppointmentReminderWorker.KEY_APPOINTMENT_ID, appointment.id)
                    .build()
            )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.NONE)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniqueWork(
            "appointment_${appointment.id}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelReminder(appointmentId: Long) {
        workManager.cancelUniqueWork("appointment_$appointmentId")
    }
}
