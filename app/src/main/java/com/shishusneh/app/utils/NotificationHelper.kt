package com.shishusneh.app.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.shishusneh.app.R

object NotificationHelper {
    private const val CHANNEL_ID = "vaccine_reminders"
    private const val APPOINTMENT_CHANNEL_ID = "appointment_reminders"

    fun ensureChannel(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val vaccineChannel = NotificationChannel(
            CHANNEL_ID,
            "Vaccination reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Timely reminders for baby vaccinations"
        }
        val appointmentChannel = NotificationChannel(
            APPOINTMENT_CHANNEL_ID,
            "Doctor appointment reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Upcoming doctor visits and follow-up reminders"
        }
        manager.createNotificationChannel(vaccineChannel)
        manager.createNotificationChannel(appointmentChannel)
    }

    fun showVaccineReminder(
        context: Context,
        notificationId: Int,
        title: String,
        body: String
    ) {
        ensureChannel(context)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_shishu_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun showAppointmentReminder(
        context: Context,
        notificationId: Int,
        title: String,
        body: String
    ) {
        ensureChannel(context)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val notification = NotificationCompat.Builder(context, APPOINTMENT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_shishu_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}
