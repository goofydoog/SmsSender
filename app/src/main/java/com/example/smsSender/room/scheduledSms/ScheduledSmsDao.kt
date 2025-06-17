package com.example.smsSender.room.scheduledSms

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.smsSender.room.SmsWithContact
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledSmsDao {

    @Insert
    suspend fun insertAll(list: List<ScheduledSms>): List<Long>

    @Update
    suspend fun updateFutureSms(scheduledSmsModel: ScheduledSms)

    @Delete
    suspend fun deleteFutureSms(scheduledSmsModel: ScheduledSms)

    @Query("UPDATE ScheduledSms SET workRequestId = :workId WHERE id = :smsId")
    fun updateWorkId(smsId: Int, workId: String)

    @Query("SELECT * FROM ScheduledSms ORDER BY timeOfSms ASC")
    fun getScheduleSmsOrderByDateOfCompletionAsc(): Flow<List<ScheduledSms>>

    @Query("SELECT * FROM ScheduledSms ORDER BY timeOfSms DESC")
    fun getScheduleSmsOrderByDateOfCompletionDesc(): Flow<List<ScheduledSms>>

    @Query("DELETE FROM ScheduledSms WHERE id = :id")
    suspend fun deleteScheduleSmsById(id: Int): Int

    @Query("SELECT workRequestId FROM ScheduledSms WHERE id = :id")
    suspend fun getWorkRequestId(id: Int): String

    @Transaction
    @Query("""
    SELECT * 
      FROM ScheduledSms 
     WHERE timeOfSms >= :now
  """)
    fun getUpcomingSmsWithContact(
        now: Long = System.currentTimeMillis()
    ): Flow<List<SmsWithContact>>

}