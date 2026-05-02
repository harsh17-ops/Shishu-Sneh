package com.shishusneh.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeding_tips")
data class FeedingTipEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val minAgeMonths: Int,
    val maxAgeMonths: Int,
    val category: String,
    val titleEn: String,
    val titleHi: String,
    val contentEn: String,
    val contentHi: String
)
