package com.notificationservice.controller;

import com.notificationservice.constants.NotificationConstants;
import com.notificationservice.dto.NotifyUserDTO;
import com.notificationservice.dto.NotifyDTO;
import com.notificationservice.dto.ResponseDTO;
import com.notificationservice.entity.Notifications;
import com.notificationservice.serviceImpl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NotificationControllerTest {

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void notifyEveryone_Success() {
        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setMessage("Test Message");

        ResponseEntity<?> response = notificationController.notifyEveryone(notifyDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(notificationService, times(1)).notifyAll("Test Message");
    }

    @Test
    void notifyAdmin_Success() {
        NotifyUserDTO notifyUserDTO = new NotifyUserDTO();
        notifyUserDTO.setUserId("admin");
        notifyUserDTO.setMessage("Admin Notification");

        ResponseEntity<?> response = notificationController.notifyAdmin(notifyUserDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(notificationService, times(1)).notifyAdmin("admin", "Admin Notification");
    }

    @Test
    void notifyUser_Success() {
        NotifyUserDTO notifyUserDTO = new NotifyUserDTO();
        notifyUserDTO.setUserId("user1");
        notifyUserDTO.setMessage("User Notification");

        ResponseEntity<?> response = notificationController.notifyUser(notifyUserDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(notificationService, times(1)).notifyUser("user1", "User Notification");
    }

    @Test
    void getAllNotifications_Success() {
        List<Notifications> notifications = new ArrayList<>();
        notifications.add(new Notifications()); // add some dummy data
        when(notificationService.getAllNotifications()).thenReturn(notifications);

        ResponseEntity<?> response = notificationController.getAllNotifications();

        assertEquals(200, response.getStatusCodeValue());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertEquals(NotificationConstants.FETCHING_ALL_NOTIFICATIONS_SUCCESS, responseDTO.getMessage());
        assertEquals(notifications, responseDTO.getData());
    }

    @Test
    void updateViewStatus_Success() {
        int id = 1;

        ResponseEntity<?> response = notificationController.updateViewStatus(id);

        assertEquals(200, response.getStatusCodeValue());
        verify(notificationService, times(1)).updateViewStatus(id);
    }

    @Test
    void getUserNotifications_Success() {
        String userId = "user1";
        List<Notifications> notifications = new ArrayList<>();
        when(notificationService.getUserNotifications(userId)).thenReturn(notifications);

        ResponseEntity<?> response = notificationController.getUserNotifications(userId);

        assertEquals(200, response.getStatusCodeValue());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertEquals(NotificationConstants.FETCHING_USER_NOTIFICATIONS_SUCCESS, responseDTO.getMessage());
        assertEquals(notifications, responseDTO.getData());
    }

    @Test
    void getAllNotificationsForUser_Success() {
        String userId = "user1";
        List<Notifications> notifications = new ArrayList<>();
        when(notificationService.getAllNotificationsForUserByUserId(userId)).thenReturn(notifications);

        ResponseEntity<?> response = notificationController.getAllNotificationsForUser(userId);

        assertEquals(200, response.getStatusCodeValue());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertEquals(NotificationConstants.FETCHING_ALL_NOTIFICATIONS_SUCCESS, responseDTO.getMessage());
        assertEquals(notifications, responseDTO.getData());
    }

    @Test
    void getAllAdminNotifications_Success() {
        List<Notifications> notifications = new ArrayList<>();
        when(notificationService.getAllAdminNotifications()).thenReturn(notifications);

        ResponseEntity<?> response = notificationController.getAllAdminNotifications();

        assertEquals(200, response.getStatusCodeValue());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertEquals(NotificationConstants.FETCHING_ALL_ADMIN_NOTIFICATIONS_SUCCESS, responseDTO.getMessage());
        assertEquals(notifications, responseDTO.getData());
    }

    @Test
    void getUnViewedAdminNotifications_Success() {
        List<Notifications> notifications = new ArrayList<>();
        when(notificationService.getUnViewedAdminNotifications()).thenReturn(notifications);

        ResponseEntity<?> response = notificationController.getUnViewedAdminNotifications();

        assertEquals(200, response.getStatusCodeValue());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertEquals(NotificationConstants.FETCHING_UNVIEWED_ADMIN_NOTIFICATIONS_SUCCESS, responseDTO.getMessage());
        assertEquals(notifications, responseDTO.getData());
    }

    // Edge Case Tests
    @Test
    void notifyEveryone_EmptyMessage() {
        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setMessage("");

        ResponseEntity<?> response = notificationController.notifyEveryone(notifyDTO);

        assertEquals(200, response.getStatusCodeValue());
        verify(notificationService, times(1)).notifyAll("");
    }

    @Test
    void notifyAdmin_NullUserId() {
        NotifyUserDTO notifyUserDTO = new NotifyUserDTO();
        notifyUserDTO.setUserId(null);
        notifyUserDTO.setMessage("Notification");

        ResponseEntity<?> response = notificationController.notifyAdmin(notifyUserDTO);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getAllNotifications_NoData() {
        when(notificationService.getAllNotifications()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = notificationController.getAllNotifications();

        assertEquals(200, response.getStatusCodeValue());
        ResponseDTO responseDTO = (ResponseDTO) response.getBody();
        assertEquals(NotificationConstants.FETCHING_ALL_NOTIFICATIONS_SUCCESS, responseDTO.getMessage());
        assertEquals(new ArrayList<>(), responseDTO.getData());
    }
}
