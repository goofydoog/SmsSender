package com.example.smsSender.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smsSender.room.contact.Contact
import com.example.smsSender.room.contact.ContactDao
import com.example.smsSender.room.scheduledSms.ScheduledSms
import com.example.smsSender.room.scheduledSms.ScheduledSmsDao

@Database(entities = [Contact::class, ScheduledSms::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract val contactDao: ContactDao
    abstract val scheduledSmsDao: ScheduledSmsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDatabase::class.java,
                    "sms_sender.db"               // ← ta sama nazwa wszędzie
                )
                    .enableMultiInstanceInvalidation()   // ← MUSI być
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
