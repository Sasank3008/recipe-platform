package com.notificationservice.serviceImpl;

import com.notificationservice.constants.Destination;
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

    private SimpMessagingTemplate simpMessagingTemplate;
    private Destination destination;
    private NotificationDao notificationDao;

    @Override
    public void notifyAll(String Sentmessage) {
        simpMessagingTemplate.convertAndSend(destination.getAllDestination(),Sentmessage);
        Notifications notifications= Notifications.builder().recipient("Everyone").sender("Admin").message(Sentmessage).build();
        notificationDao.save(notifications);
    }

    @Override
    public void notifyAdmin(String email,String Sentmessage) {
        simpMessagingTemplate.convertAndSend(destination.getAdminDestination(),Sentmessage);
        Notifications notifications= Notifications.builder().recipient("Admin").sender(email).message(Sentmessage).build();
        notificationDao.save(notifications);
    }

    @Override
    public void notifyUser(String useremail, String Sentmessage) {
        simpMessagingTemplate.convertAndSend(destination.getUserDestination()+useremail,Sentmessage);
        Notifications notifications= Notifications.builder().recipient(useremail).sender("Admin").message(Sentmessage).build();
        notificationDao.save(notifications);
    }

    @Override
    public List<Notifications> getAllNotifications(){
        return notificationDao.findAll();
    }

    public Notifications updateViewStatus(int id){
        Notifications notifications=notificationDao.findById(id).orElseThrow(() -> new NotFoundException("Notification with ID not found"));
        notifications.setViewed(true);
        return notificationDao.save(notifications);
    }
    @Override
    public List<Notifications> getUserNotifications(String email) {
        return notificationDao.findByRecipientInOrderByViewedAscCreateAtDesc(Arrays.asList(email, "Everyone"));
    }

    @Override
    public List<Notifications> getAllNotificationsForUser(String email) {
        return notificationDao.findByRecipientInOrderByCreateAtDesc(Arrays.asList(email, "Everyone"));
    }
    @Override
    public List<Notifications> getAllAdminNotifications() {
        return notificationDao.findByRecipientInOrderByCreateAtDesc(Arrays.asList("Admin"));
    }

    @Override
    public List<Notifications> getUnviewedAdminNotifications() {
        List<Notifications> allAdminNotifications = notificationDao.findByRecipientInOrderByViewedAscCreateAtDesc(Arrays.asList("Admin"));
        return allAdminNotifications.stream()
                .filter(notification -> !notification.isViewed())
                .collect(Collectors.toList());
    }

}
