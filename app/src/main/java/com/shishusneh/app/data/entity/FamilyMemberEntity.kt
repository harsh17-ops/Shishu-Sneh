package com.shishusneh.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "family_members",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId")]
)
data class FamilyMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val name: String,
    val relation: String,
    val phoneNumber: String = "",
    val accessLevel: String = "Viewer"
)
