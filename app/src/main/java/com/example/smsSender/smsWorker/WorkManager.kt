package com.example.smsSender.smsWorker

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.smsSender.room.scheduledSms.ScheduledSms
import java.util.UUID
import java.util.concurrent.TimeUnit

object SmsScheduler {

    fun planSms(
        context: Context,
        message: String,
        phoneNumber: String,
        smsList: List<ScheduledSms>
    ): Map<Int, String> {

        val wm = WorkManager.getInstance(context)
        val result = mutableMapOf<Int, String>()

        smsList.forEach { sms ->

            val delay = maxOf(0L, sms.timeOfSms - System.currentTimeMillis())

            val req = OneTimeWorkRequestBuilder<SmsWorker>()
                .setInputData(
                    workDataOf(
                        "phone" to "+48$phoneNumber",
                        "message" to message,
                        "scheduledSmsId" to sms.id
                    )
                )
                .apply { if (delay > 0) setInitialDelay(delay, TimeUnit.MILLISECONDS) }
                .addTag("sms_$${sms.id}")     // ← łatwiej później odszukać
                .build()

            wm.enqueue(req)
            result[sms.id] = req.id.toString()
        }
        return result
    }

    fun cancelWork(workId: String, context: Context) {
        val wm = WorkManager.getInstance(context)
        wm.cancelWorkById(UUID.fromString(workId))
    }
}

