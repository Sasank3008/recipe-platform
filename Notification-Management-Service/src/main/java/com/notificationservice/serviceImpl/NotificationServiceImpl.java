package com.notificationservice.serviceImpl;

import com.notificationservice.constants.DestinationConstants;
import com.notificationservice.entity.Notifications;
import com.notificationservice.exception.NotFoundException;
import com.notificationservice.repository.NotificationDao;
import com.notificationservice.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DestinationConstants destinationConstants;
    private final NotificationDao notificationDao;

    private String getUserEmailById(String userId) {
        return "user_email_for_" + userId + "@example.com";
    }

    @Override
    public void notifyAll(String sentMessage) {
        simpMessagingTemplate.convertAndSend(destinationConstants.getAllDestination(), sentMessage);
        Notifications notifications = Notifications.builder()
                .recipient("Everyone")
                .sender("Admin")
                .message(sentMessage)
                .build();
        notificationDao.save(notifications);
    }

    @Override
    public void notifyAdmin(String userId, String sentMessage) {
        String userEmail = getUserEmailById(userId);
        simpMessagingTemplate.convertAndSend(destinationConstants.getAdminDestination(), sentMessage);
        Notifications notifications = Notifications.builder()
                .recipient("Admin")
                .sender(userEmail)
                .message(sentMessage)
                .build();
        notificationDao.save(notifications);
    }

    @Override
    public void notifyUser(String userId, String sentMessage) {
        String userEmail = getUserEmailById(userId);
        simpMessagingTemplate.convertAndSend(destinationConstants.getUserDestination() + userId, sentMessage);
        Notifications notifications = Notifications.builder()
                .recipient(userEmail)
                .sender("Admin")
                .message(sentMessage)
                .build();
        notificationDao.save(notifications);
    }

    @Override
    public List<Notifications> getAllNotifications() {
        return notificationDao.findAll();
    }

    @Override
    public Notifications updateViewStatus(int id) {
        Notifications notifications = notificationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification with ID not found"));
        notifications.setViewed(true);
        return notificationDao.save(notifications);
    }

    @Override
    public List<Notifications> getUserNotifications(String userId) {
        String userEmail = getUserEmailById(userId);
        return notificationDao.findByRecipientInOrderByViewedAscCreateAtDesc(Arrays.asList(userId, "Everyone"));
    }

    @Override
    public List<Notifications> getAllNotificationsForUserByUserId(String userId) {
        String userEmail = getUserEmailById(userId);
        return notificationDao.findByRecipientInOrderByCreateAtDesc(Arrays.asList(userId, "Everyone"));
    }

    @Override
    public List<Notifications> getAllAdminNotifications() {
        return notificationDao.findByRecipientInOrderByCreateAtDesc(Arrays.asList("Admin"));
    }

    @Override
    public List<Notifications> getUnViewedAdminNotifications() {
        return notificationDao.findByRecipientInOrderByViewedAscCreateAtDesc(Arrays.asList("Admin"))
                .stream()
                .filter(notification -> !notification.isViewed())
                .collect(Collectors.toList());
    }
}
