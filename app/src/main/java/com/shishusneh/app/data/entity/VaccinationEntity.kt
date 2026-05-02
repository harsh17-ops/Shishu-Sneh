package com.shishusneh.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccinations",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId"), Index(value = ["babyId", "name", "dueDateMillis"], unique = true)]
)
data class VaccinationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val name: String,
    val diseasePrevented: String,
    val dueDateMillis: Long,
    val isCompleted: Boolean = false,
    val completedDateMillis: Long? = null
)
