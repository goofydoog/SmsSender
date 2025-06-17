package com.example.smsSender.smsWorker

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.smsSender.room.AppDatabase
import com.example.smsSender.room.scheduledSms.ScheduledSmsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SmsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val dao: ScheduledSmsDao by lazy {
        AppDatabase.getInstance(applicationContext).scheduledSmsDao
    }

    override suspend fun doWork(): Result {
        val phone = inputData.getString("phone")!!
        val smsId = inputData.getInt("scheduledSmsId",-1)
        val message = inputData.getString("message")

        val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applicationContext.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }

        val parts = smsManager.divideMessage(message)

        // 2) wybierz odpowiednią metodę
        if (parts.size == 1) {
            smsManager.sendTextMessage(phone, null, message, null, null)
        } else {
            smsManager.sendMultipartTextMessage(phone, null, parts, null, null)
        }

        if (smsId != -1) {
            withContext(Dispatchers.IO) {
                dao.deleteScheduleSmsById(smsId)
            }
        }

        return Result.success()
    }
}
