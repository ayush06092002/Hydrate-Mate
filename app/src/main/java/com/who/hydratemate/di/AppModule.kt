package com.who.hydratemate.di

import android.content.Context
import androidx.room.Room
import com.who.hydratemate.data.NotificationDao
import com.who.hydratemate.data.NotificationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesNotificationDao(notificationDatabase: NotificationDatabase): NotificationDao {
        return notificationDatabase.notificationDao()
    }

    @Singleton
    @Provides
    fun provideNotificationDatabase(@ApplicationContext context: Context): NotificationDatabase {
        return Room.databaseBuilder(
            context,
            NotificationDatabase::class.java,
            "notification_database"
        ).fallbackToDestructiveMigration().build()
    }

}