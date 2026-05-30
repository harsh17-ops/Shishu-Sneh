package com.shishusneh.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shishusneh.app.data.dao.AppointmentDao
import com.shishusneh.app.utils.DateUtils
import com.shishusneh.app.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AppointmentReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val appointmentDao: AppointmentDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val appointmentId = inputData.getLong(KEY_APPOINTMENT_ID, -1L)
        if (appointmentId == -1L) return Result.failure()
        val appointment = appointmentDao.getById(appointmentId) ?: return Result.failure()
        if (appointment.isCompleted) return Result.success()

        NotificationHelper.showAppointmentReminder(
            context = applicationContext,
            notificationId = appointment.id.toInt() + 10_000,
            title = "${appointment.title} tomorrow",
            body = "Dr. ${appointment.doctorName} on ${DateUtils.formatDate(appointment.appointmentAtMillis)}"
        )
        return Result.success()
    }

    companion object {
        const val KEY_APPOINTMENT_ID = "appointment_id"
    }
}
