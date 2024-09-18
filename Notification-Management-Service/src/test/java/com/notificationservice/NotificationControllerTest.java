package com.notificationservice;

import com.notificationservice.controller.NotificationController;
import com.notificationservice.dto.NotifyUserdto;
import com.notificationservice.dto.Notifydto;
import com.notificationservice.dto.Response;
import com.notificationservice.dto.SimpleResponse;
import com.notificationservice.entity.Notifications;
import com.notificationservice.serviceImpl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NotificationControllerTest {

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNotifyEveryone() {
        // Given
        Notifydto notifydto = new Notifydto("Message for everyone");

        // When
        ResponseEntity<?> response = notificationController.notifyEveryone(notifydto);

        // Then
        verify(notificationService, times(1)).notifyAll("Message for everyone");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testNotifyAdmin() {
        // Given
        NotifyUserdto notifyUserdto = new NotifyUserdto("admin@example.com", "Message for admin");

        // When
        ResponseEntity<?> response = notificationController.notifyAdmin(notifyUserdto);

        // Then
        verify(notificationService, times(1)).notifyAdmin("admin@example.com", "Message for admin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testNotifyUser() {
        // Given
        NotifyUserdto notifyUserdto = new NotifyUserdto("user@example.com", "Message for user");

        // When
        ResponseEntity<?> response = notificationController.notifyUser(notifyUserdto);

        // Then
        verify(notificationService, times(1)).notifyUser("user@example.com", "Message for user");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllNotifications() {
        // Given
        Notifications notification1 = new Notifications(1, "admin", "user", "message", false, null);
        Notifications notification2 = new Notifications(2, "system", "admin", "system message", true, null);
        List<Notifications> notificationsList = Arrays.asList(notification1, notification2);

        when(notificationService.getAllNotifications()).thenReturn(notificationsList);

        // When
        ResponseEntity<?> response = notificationController.getAllNotifications();

        // Then
        verify(notificationService, times(1)).getAllNotifications();
        Response res = (Response) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("200", res.getStatus());
        assertEquals("Fetching all Notifications was Successful", res.getMessage());
        assertEquals(notificationsList, res.getData());
    }

    @Test
    public void testUpdateViewStatus() {
        // Given
        int id = 1;

        // When
        ResponseEntity<?> response = notificationController.updateViewStatus(id);

        // Then
        verify(notificationService, times(1)).updateViewStatus(id);
        SimpleResponse simpleResponse = (SimpleResponse) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("200", simpleResponse.getStatus());
        assertEquals("The Update was Successfull", simpleResponse.getMessage());
    }
}

