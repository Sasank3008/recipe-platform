package com.notificationservice.service;


import com.notificationservice.entity.Notifications;

import java.util.List;

public interface NotificationService {
    public void notifyAll(String message);
    public void notifyAdmin(String email,String message);
    public void notifyUser(String useremail,String message);
    public List<Notifications> getAllNotifications();
    public List<Notifications> getUserNotifications(String email);
    public List<Notifications> getAllNotificationsForUser(String email);
    public List<Notifications> getUnviewedAdminNotifications();
    public List<Notifications> getAllAdminNotifications();
}
