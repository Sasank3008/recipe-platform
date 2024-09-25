package com.notificationservice.service;

import com.notificationservice.entity.Notifications;

import java.util.List;

public interface NotificationService {

    void notifyAll(String message);
    void notifyAdmin(String userId, String message);
    void notifyUsers(List<String> userIds, String sentMessage);
    Notifications updateViewStatus(int id);
    List<Notifications> getAllNotifications();
    List<Notifications> getUserNotifications(String userId);
    List<Notifications> getAllNotificationsForUserByUserId(String userId);
    List<Notifications> getAllAdminNotifications();
    List<Notifications> getUnViewedAdminNotifications();
    long getUnreadNotificationCount(String userId);
}
