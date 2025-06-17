package com.example.smsSender.room.scheduledSms

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.work.WorkRequest
import com.example.smsSender.room.contact.Contact

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Contact::class,
            parentColumns = ["id"],
            childColumns =  ["idOfContact"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idOfContact"])
    // przy filtracji i JOIN-ach bez indeksu taki SELECT bedzie musial przeszukac cala tabele,
    // co przy duze liczbie wierszy spowolni apke
    // cos jak indeksy w firestore
    ]
)
data class ScheduledSms(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timeOfVisit: Long,
    val idOfContact: Int,
    val workRequestId: String? = null,
    val timeOfSms: Long
)
