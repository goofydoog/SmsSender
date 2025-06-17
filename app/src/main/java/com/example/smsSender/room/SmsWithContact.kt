package com.example.smsSender.room

import androidx.room.Embedded
import androidx.room.Relation
import com.example.smsSender.room.contact.Contact
import com.example.smsSender.room.scheduledSms.ScheduledSms

data class SmsWithContact(
    @Embedded
    val sms: ScheduledSms,

    @Relation(
        parentColumn  = "idOfContact",
        entityColumn  = "id"
    )
    val contact: Contact
)