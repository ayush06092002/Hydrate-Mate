package com.who.hydratemate.repository

import com.who.hydratemate.data.notification.NotificationDao
import com.who.hydratemate.models.Notifications
import javax.inject.Inject

class NotificationRepository @Inject constructor(private val notificationDao: NotificationDao) {

        fun getNotifications() = notificationDao.getNotifications()

        fun getNotificationById(id: Long) = notificationDao.getNotificationById(id)

        suspend fun deleteNotification(id: Long) = notificationDao.deleteNotification(id)

        suspend fun deleteAllNotifications() = notificationDao.deleteAllNotifications()

        fun getNotificationsCount() = notificationDao.getNotificationsCount()

        suspend fun insertNotification(notification: Notifications) = notificationDao.insertNotification(notification)

        suspend fun markCompleted(id: Long) = notificationDao.markCompleted(id)

        suspend fun markIncomplete(id: Long) = notificationDao.markIncomplete(id)

        fun getCompletedNotificationsCount() : Int = notificationDao.getCompletedNotificationsCount()

}