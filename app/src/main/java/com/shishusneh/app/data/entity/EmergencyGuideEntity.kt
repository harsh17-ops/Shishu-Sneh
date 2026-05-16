package com.shishusneh.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_guides")
data class EmergencyGuideEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val titleEn: String,
    val titleHi: String,
    val contentEn: String,
    val contentHi: String,
    val priority: Int = 0
)
