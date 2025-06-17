package com.example.smsSender.di

import android.content.Context
import androidx.room.Room
import com.example.smsSender.room.AppDatabase
import com.example.smsSender.room.contact.ContactDao
import com.example.smsSender.room.scheduledSms.ScheduledSmsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class) // tworzy sie raz gdy aplikacja startuje i zyje do jej zamkniecia
object AppModule {

    @Provides
    @Singleton // zwraca jedna (tą samą) instancje w calej aplikacji
    fun provideDatabase(
        @ApplicationContext ctx: Context
    ): AppDatabase {
        val database = Room.databaseBuilder(
            ctx,
            AppDatabase::class.java,
            "sms_sender.db"
        )
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration(false)
            .build()
        return database
    }

    @Provides
    @Singleton
    fun provideScheduleSmsDao(db: AppDatabase): ScheduledSmsDao {
        return db.scheduledSmsDao
    }

    @Provides
    @Singleton
    fun provideContactDao(db: AppDatabase): ContactDao {
        return db.contactDao
    }
}